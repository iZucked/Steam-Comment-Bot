/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.adp;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FleetConstraint;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;
import com.mmxlabs.models.lng.adp.MinCargoConstraint;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.util.MonthlyDistributionConstraint;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselSlotCountFitnessProviderEditor;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

/**
 * @author Simon Goodall
 */
public class ADPConstraintsTransformer implements ITransformerExtension {

	@Inject
	private IMaxSlotConstraintDataProviderEditor maxSlotConstraintDataTransformer;

	@Inject
	private IVesselSlotCountFitnessProviderEditor longTermVesselSlotCountFitnessProviderEditor;

	@Inject
	private SchedulerCalculationUtils scheduleCalculationUtils;

	@Inject
	private CalendarMonthMapper calendarMonthMapper;

	@Inject
	private ModelEntityMap modelEntityMap;

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;

	@Inject(optional = true)
	private ADPModel adpModel;

	@Inject
	private @NonNull IAllowedVesselProviderEditor allowedVesselProviderEditor;

	private LNGScenarioModel rootObject;

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;

	}

	@Override
	public void finishTransforming() {

		if (adpModel == null) {
			return;
		}

		final FleetProfile fleetProfile = adpModel.getFleetProfile();
		final IVessel iDefaultVessel = modelEntityMap.getOptimiserObjectNullChecked(fleetProfile.getDefaultNominalMarket().getVessel(), IVessel.class);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(rootObject);

		for (final PurchaseContractProfile contractProfile : adpModel.getPurchaseContractProfiles()) {
			if (!contractProfile.isEnabled()) {
				continue;
			}
			YearMonth start = contractProfile.getContract().getStartDate();
			if (start == null) {
				start = adpModel.getYearStart();
			}
			final int startMonth = calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(start));
			final List<LoadSlot> slots = new LinkedList<>();
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				if (slot.getContract() == contractProfile.getContract()) {
					slots.add(slot);
				}
			}
			final List<ILoadOption> o_slots = slots.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, ILoadOption.class)) //
					.collect(Collectors.toList());

			// Forcibly add in the default vessel to the allowed vessels list.
			// Note: This is *vessel* not market!
			for (final IPortSlot slot : o_slots) {
				final Collection<@NonNull IVessel> permittedVessels = allowedVesselProviderEditor.getPermittedVessels(slot);
				if (permittedVessels != null) {
					permittedVessels.add(iDefaultVessel);
				}
			}

			for (final ProfileConstraint profileConstraint : contractProfile.getConstraints()) {
				if (profileConstraint instanceof MinCargoConstraint) {
					final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) profileConstraint;
					switch (minCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerMonth(contractProfile, profileConstraint, o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerQuarter(contractProfile, profileConstraint, o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerYear(contractProfile, profileConstraint, o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerMonthlyPeriod(contractProfile, profileConstraint, o_slots, startMonth, 2, minCargoConstraint.getMinCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + minCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof MaxCargoConstraint) {
					final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) profileConstraint;
					switch (maxCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerMonth(contractProfile, profileConstraint, o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerQuarter(contractProfile, profileConstraint, o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerYear(contractProfile, profileConstraint, o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerMonthlyPeriod(contractProfile, profileConstraint, o_slots, startMonth, 2, maxCargoConstraint.getMaxCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + maxCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof PeriodDistributionProfileConstraint) {
					PeriodDistributionProfileConstraint periodDistributionProfileConstraint = (PeriodDistributionProfileConstraint) profileConstraint;
					MonthlyDistributionConstraint monthlyDistributionConstraint = new MonthlyDistributionConstraint();
					for (PeriodDistribution periodDistribution : periodDistributionProfileConstraint.getDistributions()) {
						List<Integer> months = periodDistribution.getRange().stream()
								.map(m -> calendarMonthMapper
										.mapChangePointToMonth(dateAndCurveHelper
												.convertTime(m)))
								.collect(Collectors.toList());
						if (!months.isEmpty() && (periodDistribution.isSetMinCargoes() || periodDistribution.isSetMaxCargoes())) {
						monthlyDistributionConstraint.addRow(months, periodDistribution.isSetMinCargoes() ? periodDistribution.getMinCargoes() : null ,
								periodDistribution.isSetMaxCargoes() ? periodDistribution.getMaxCargoes() : null);
						}
					}
					maxSlotConstraintDataTransformer.addMinMaxLoadSlotsPerMultiMonthPeriod(contractProfile, profileConstraint, o_slots, monthlyDistributionConstraint);
				}
				else {
					// Not handled here.
				}

			}

		}
		for (final SalesContractProfile contractProfile : adpModel.getSalesContractProfiles()) {
			if (!contractProfile.isEnabled()) {
				continue;
			}
			YearMonth start = contractProfile.getContract().getStartDate();
			if (start == null) {
				start = adpModel.getYearStart();
			}
			final int startMonth = calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(start));

			final List<DischargeSlot> slots = new LinkedList<>();
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				if (slot.getContract() == contractProfile.getContract()) {
					slots.add(slot);
				}
			}
			final List<IDischargeOption> o_slots = slots.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IDischargeOption.class)) //
					.collect(Collectors.toList());

			// Forcibly add in the default vessel to the allowed vessels list.
			// Note: This is *vessel* not market!
			for (final IPortSlot slot : o_slots) {
				final Collection<@NonNull IVessel> permittedVessels = allowedVesselProviderEditor.getPermittedVessels(slot);
				if (permittedVessels != null) {
					permittedVessels.add(iDefaultVessel);
				}
			}

			for (final ProfileConstraint profileConstraint : contractProfile.getConstraints()) {
				if (profileConstraint instanceof MinCargoConstraint) {
					final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) profileConstraint;
					switch (minCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerMonth(contractProfile, profileConstraint, o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerQuarter(contractProfile, profileConstraint, o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerYear(contractProfile, profileConstraint, o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerMonthlyPeriod(contractProfile, profileConstraint, o_slots, startMonth, 2, minCargoConstraint.getMinCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + minCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof MaxCargoConstraint) {
					final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) profileConstraint;
					switch (maxCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerMonth(contractProfile, profileConstraint, o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerQuarter(contractProfile, profileConstraint, o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerYear(contractProfile, profileConstraint, o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerMonthlyPeriod(contractProfile, profileConstraint, o_slots, startMonth, 2, maxCargoConstraint.getMaxCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + maxCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof PeriodDistributionProfileConstraint) {
					PeriodDistributionProfileConstraint periodDistributionProfileConstraint = (PeriodDistributionProfileConstraint) profileConstraint;
					MonthlyDistributionConstraint monthlyDistributionConstraint = new MonthlyDistributionConstraint();
					for (PeriodDistribution periodDistribution : periodDistributionProfileConstraint.getDistributions()) {
						List<Integer> months = periodDistribution.getRange().stream()
								.map(m -> calendarMonthMapper
										.mapChangePointToMonth(dateAndCurveHelper
												.convertTime(m)))
								.collect(Collectors.toList());
						if (!months.isEmpty() && (periodDistribution.isSetMinCargoes() || periodDistribution.isSetMaxCargoes())) {
							monthlyDistributionConstraint.addRow(months, periodDistribution.isSetMinCargoes() ? periodDistribution.getMinCargoes() : null ,
								periodDistribution.isSetMaxCargoes() ? periodDistribution.getMaxCargoes() : null);
						}
					}
					maxSlotConstraintDataTransformer.addMinMaxDischargeSlotsPerMultiMonthPeriod(contractProfile, profileConstraint, o_slots, monthlyDistributionConstraint);
				} else {
					// Not handled here.
				}

			}
		}
		handleVesselConstraints(adpModel);
	}

	public void handleVesselConstraints(@NonNull final ADPModel model) {

		final FleetProfile fleetProfile = model.getFleetProfile();
		if (fleetProfile != null && fleetProfile.getConstraints() != null) {
			for (final FleetConstraint fleetConstraint : fleetProfile.getConstraints()) {
				if (fleetConstraint instanceof TargetCargoesOnVesselConstraint) {
					{
						final IVessel iVessel = modelEntityMap.getOptimiserObjectNullChecked(((TargetCargoesOnVesselConstraint) fleetConstraint).getVessel(), IVessel.class);

						final List<IVesselAvailability> availabilities = scheduleCalculationUtils.getAllVesselAvailabilities().stream() //
								.filter(v -> (v.getVessel() == iVessel)) //
								.collect(Collectors.toList());

						for (final IVesselAvailability availability : availabilities) {
							longTermVesselSlotCountFitnessProviderEditor.setValuesForVessel(availability, ((TargetCargoesOnVesselConstraint) fleetConstraint).getTargetNumberOfCargoes(),
									(long) ((TargetCargoesOnVesselConstraint) fleetConstraint).getWeight());
						}
					}
					final IVessel iDefaultVessel = modelEntityMap.getOptimiserObjectNullChecked(fleetProfile.getDefaultNominalMarket().getVessel(), IVessel.class);
					for (final IVesselAvailability vesselAvailability : scheduleCalculationUtils.getAllVesselAvailabilities()) {
						if (vesselAvailability.getVessel() == iDefaultVessel) {
							if (vesselAvailability.getSpotIndex() == -1) {
								// Not strictly correct yet!
								// longTermVesselSlotCountFitnessProviderEditor.setValuesForVessel(vesselAvailability, ((TargetCargoesOnVesselConstraint) fleetConstraint).getTargetNumberOfCargoes(),
								// (long) ((TargetCargoesOnVesselConstraint) fleetConstraint).getWeight());
							}
						}
					}
				}
			}
		}
	}
}

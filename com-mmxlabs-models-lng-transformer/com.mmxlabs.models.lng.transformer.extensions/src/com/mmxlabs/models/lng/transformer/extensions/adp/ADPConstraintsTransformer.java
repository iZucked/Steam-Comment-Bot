/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.models.lng.adp.ADPModel;
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
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProviderEditor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.util.MonthlyDistributionConstraint;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselSlotCountFitnessProviderEditor;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

/**
 * @author Simon Goodall
 */
public class ADPConstraintsTransformer implements ITransformerExtension {

	private static final String UNSUPPORTED_INTERVAL_TYPE = "Unsupported interval type ";

	@Inject
	private IMaxSlotConstraintDataProviderEditor maxSlotConstraintEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

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

	@Inject
	private IOptionalElementsProviderEditor optionalElementsProvider;

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

			// TODO: change to pair<IXOption,Integer> //second = month based on calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(start));

			final List<Pair<ILoadOption, Integer>> oSlots = slots.stream() //
					.<Pair<ILoadOption, Integer>> map(s -> Pair.of(modelEntityMap.getOptimiserObjectNullChecked(s, ILoadOption.class),
							calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(s.getWindowStart())))) //
					.collect(Collectors.toList());

			// Forcibly add in the default vessel to the allowed vessels list.
			// Note: This is *vessel* not market!
			for (final Pair<ILoadOption, Integer> slot : oSlots) {
				final Collection<@NonNull IVessel> permittedVessels = allowedVesselProviderEditor.getPermittedVessels(slot.getFirst());
				if (permittedVessels != null) {
					permittedVessels.add(iDefaultVessel);
				}
			}

			// Only soft required slots are the constrained ones. The optionality of these slots during optimisation is handled by NonOptionalFitnessCore by ignoring them in the fitness calculation.
			if (!contractProfile.getConstraints().isEmpty()) {
				for (final Pair<ILoadOption, Integer> oSlot : oSlots) {
					optionalElementsProvider.setSoftRequired(portSlotProvider.getElement(oSlot.getFirst()), true);
				}
			}

			for (final ProfileConstraint profileConstraint : contractProfile.getConstraints()) {
				if (profileConstraint instanceof MinCargoConstraint) {
					final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) profileConstraint;
					switch (minCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintEditor.addMinLoadSlotsPerMonth(contractProfile, profileConstraint, oSlots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintEditor.addMinLoadSlotsPerQuarter(contractProfile, profileConstraint, oSlots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case YEARLY:
						maxSlotConstraintEditor.addMinLoadSlotsPerYear(contractProfile, profileConstraint, oSlots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintEditor.addMinLoadSlotsPerMonthlyPeriod(contractProfile, profileConstraint, oSlots, startMonth, 2, minCargoConstraint.getMinCargoes());
						break;
					default:
						throw new IllegalStateException(UNSUPPORTED_INTERVAL_TYPE + minCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof MaxCargoConstraint) {
					final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) profileConstraint;
					switch (maxCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintEditor.addMaxLoadSlotsPerMonth(contractProfile, profileConstraint, oSlots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintEditor.addMaxLoadSlotsPerQuarter(contractProfile, profileConstraint, oSlots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case YEARLY:
						maxSlotConstraintEditor.addMaxLoadSlotsPerYear(contractProfile, profileConstraint, oSlots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintEditor.addMaxLoadSlotsPerMonthlyPeriod(contractProfile, profileConstraint, oSlots, startMonth, 2, maxCargoConstraint.getMaxCargoes());
						break;
					default:
						throw new IllegalStateException(UNSUPPORTED_INTERVAL_TYPE + maxCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof PeriodDistributionProfileConstraint) {
					PeriodDistributionProfileConstraint periodDistributionProfileConstraint = (PeriodDistributionProfileConstraint) profileConstraint;
					MonthlyDistributionConstraint monthlyDistributionConstraint = new MonthlyDistributionConstraint();
					for (PeriodDistribution periodDistribution : periodDistributionProfileConstraint.getDistributions()) {
						List<Integer> months = periodDistribution.getRange().stream().map(m -> calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(m)))
								.collect(Collectors.toList());
						if (!months.isEmpty() && (periodDistribution.isSetMinCargoes() || periodDistribution.isSetMaxCargoes())) {
							monthlyDistributionConstraint.addRow(months, periodDistribution.getRange(), periodDistribution.isSetMinCargoes() ? periodDistribution.getMinCargoes() : null,
									periodDistribution.isSetMaxCargoes() ? periodDistribution.getMaxCargoes() : null);
						}
					}
					maxSlotConstraintEditor.addMinMaxLoadSlotsPerMultiMonthPeriod(contractProfile, profileConstraint, oSlots, monthlyDistributionConstraint);
				} else {
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

			// Convert from LNG model slots to optimiser DischargeOptions.
			final List<Pair<IDischargeOption, Integer>> oSlots = slots.stream() //
					.<Pair<IDischargeOption, Integer>> map(s -> Pair.of(modelEntityMap.getOptimiserObjectNullChecked(s, IDischargeOption.class),
							calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(s.getWindowStart())))) //
					.collect(Collectors.toList());

			// Forcibly add in the default vessel to the allowed vessels list.
			// Note: This is *vessel* not market!
			for (final Pair<IDischargeOption, Integer> slot : oSlots) {
				final Collection<@NonNull IVessel> permittedVessels = allowedVesselProviderEditor.getPermittedVessels(slot.getFirst());
				if (permittedVessels != null) {
					permittedVessels.add(iDefaultVessel);
				}
			}

			// Only soft required slots are the constrained ones. The optionality of these slots during optimisation is handled by NonOptionalFitnessCore by ignoring them in the fitness calculation.
			if (!contractProfile.getConstraints().isEmpty()) {
				for (final Pair<IDischargeOption, Integer> oSlot : oSlots) {
					optionalElementsProvider.setSoftRequired(portSlotProvider.getElement(oSlot.getFirst()), true);
				}
			}

			for (final ProfileConstraint profileConstraint : contractProfile.getConstraints()) {
				if (profileConstraint instanceof MinCargoConstraint) {
					final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) profileConstraint;
					switch (minCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintEditor.addMinDischargeSlotsPerMonth(contractProfile, profileConstraint, oSlots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintEditor.addMinDischargeSlotsPerQuarter(contractProfile, profileConstraint, oSlots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case YEARLY:
						maxSlotConstraintEditor.addMinDischargeSlotsPerYear(contractProfile, profileConstraint, oSlots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintEditor.addMinDischargeSlotsPerMonthlyPeriod(contractProfile, profileConstraint, oSlots, startMonth, 2, minCargoConstraint.getMinCargoes());
						break;
					default:
						throw new IllegalStateException(UNSUPPORTED_INTERVAL_TYPE + minCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof MaxCargoConstraint) {
					final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) profileConstraint;
					switch (maxCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintEditor.addMaxDischargeSlotsPerMonth(contractProfile, profileConstraint, oSlots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintEditor.addMaxDischargeSlotsPerQuarter(contractProfile, profileConstraint, oSlots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case YEARLY:
						maxSlotConstraintEditor.addMaxDischargeSlotsPerYear(contractProfile, profileConstraint, oSlots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintEditor.addMaxDischargeSlotsPerMonthlyPeriod(contractProfile, profileConstraint, oSlots, startMonth, 2, maxCargoConstraint.getMaxCargoes());
						break;
					default:
						throw new IllegalStateException(UNSUPPORTED_INTERVAL_TYPE + maxCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof PeriodDistributionProfileConstraint) {
					PeriodDistributionProfileConstraint periodDistributionProfileConstraint = (PeriodDistributionProfileConstraint) profileConstraint;
					MonthlyDistributionConstraint monthlyDistributionConstraint = new MonthlyDistributionConstraint();
					for (PeriodDistribution periodDistribution : periodDistributionProfileConstraint.getDistributions()) {
						List<Integer> months = periodDistribution.getRange().stream().map(m -> calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(m)))
								.collect(Collectors.toList());
						if (!months.isEmpty() && (periodDistribution.isSetMinCargoes() || periodDistribution.isSetMaxCargoes())) {
							monthlyDistributionConstraint.addRow(months, periodDistribution.getRange(), periodDistribution.isSetMinCargoes() ? periodDistribution.getMinCargoes() : null,
									periodDistribution.isSetMaxCargoes() ? periodDistribution.getMaxCargoes() : null);
						}
					}

					maxSlotConstraintEditor.addMinMaxDischargeSlotsPerMultiMonthPeriod(contractProfile, profileConstraint, oSlots, monthlyDistributionConstraint);
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

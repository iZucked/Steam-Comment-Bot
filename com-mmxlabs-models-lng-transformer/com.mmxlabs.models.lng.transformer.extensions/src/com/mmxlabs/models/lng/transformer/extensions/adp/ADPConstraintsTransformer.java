/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.adp;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.FleetConstraint;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;
import com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint;
import com.mmxlabs.models.lng.adp.MinCargoConstraint;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermVesselSlotCountFitnessProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

/**
 * @author Simon Goodall
 */
public class ADPConstraintsTransformer implements ITransformerExtension {

	@Inject
	private IMaxSlotConstraintDataProviderEditor maxSlotConstraintDataTransformer;

	@Inject
	private ILongTermVesselSlotCountFitnessProviderEditor longTermVesselSlotCountFitnessProviderEditor;

	@Inject
	private IVesselProvider vesselProvider;
	
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

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {

	}

	@Override
	public void finishTransforming() {

		if (adpModel == null) {
			return;
		}

		final YearMonth end = adpModel.getYearEnd();

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
			for (final SubContractProfile<LoadSlot> subProfile : contractProfile.getSubProfiles()) {
				slots.addAll(subProfile.getSlots());
			}
			final List<ILoadOption> o_slots = slots.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, ILoadOption.class)) //
					.collect(Collectors.toList());
			for (final ProfileConstraint profileConstraint : contractProfile.getConstraints()) {
				if (profileConstraint instanceof MinCargoConstraint) {
					final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) profileConstraint;
					switch (minCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerMonth(o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerQuarter(o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerYear(o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMinLoadSlotsPerMonthlyPeriod(o_slots, startMonth, 2, minCargoConstraint.getMinCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + minCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof MaxCargoConstraint) {
					final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) profileConstraint;
					switch (maxCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerMonth(o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerQuarter(o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerYear(o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMaxLoadSlotsPerMonthlyPeriod(o_slots, startMonth, 2, maxCargoConstraint.getMaxCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + maxCargoConstraint.getIntervalType());
					}
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
			for (final SubContractProfile<DischargeSlot> subProfile : contractProfile.getSubProfiles()) {
				slots.addAll(subProfile.getSlots());
			}
			final List<IDischargeOption> o_slots = slots.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IDischargeOption.class)) //
					.collect(Collectors.toList());
			for (final ProfileConstraint profileConstraint : contractProfile.getConstraints()) {
				if (profileConstraint instanceof MinCargoConstraint) {
					final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) profileConstraint;
					switch (minCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerMonth(o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerQuarter(o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerYear(o_slots, startMonth, minCargoConstraint.getMinCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMinDischargeSlotsPerMonthlyPeriod(o_slots, startMonth, 2, minCargoConstraint.getMinCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + minCargoConstraint.getIntervalType());
					}
				} else if (profileConstraint instanceof MaxCargoConstraint) {
					final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) profileConstraint;
					switch (maxCargoConstraint.getIntervalType()) {
					case MONTHLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerMonth(o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case QUARTERLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerQuarter(o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case YEARLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerYear(o_slots, startMonth, maxCargoConstraint.getMaxCargoes());
						break;
					case BIMONTHLY:
						maxSlotConstraintDataTransformer.addMaxDischargeSlotsPerMonthlyPeriod(o_slots, startMonth, 2, maxCargoConstraint.getMaxCargoes());
						break;
					default:
						throw new IllegalStateException("Unsupported interval type " + maxCargoConstraint.getIntervalType());
					}
				} else {
					// Not handled here.
				}

			}
		}
		handleVesselConstraints(adpModel);
	}
	
	public void handleVesselConstraints(@NonNull ADPModel model) {
		if (model.getFleetProfile() != null && model.getFleetProfile().getConstraints() != null) {
			for (FleetConstraint fleetConstraint : model.getFleetProfile().getConstraints()) {
				if (fleetConstraint instanceof TargetCargoesOnVesselConstraint) {
					IVessel iVessel = modelEntityMap.getOptimiserObjectNullChecked(((TargetCargoesOnVesselConstraint) fleetConstraint).getVessel(), IVessel.class);
					List<IVesselAvailability> availabilities = scheduleCalculationUtils.getAllVesselAvailabilities()
							.stream().filter(v->v.getVessel() == iVessel).collect(Collectors.toList());
					for (IVesselAvailability availability : availabilities) {
						longTermVesselSlotCountFitnessProviderEditor.setValuesForVessel(availability,
								((TargetCargoesOnVesselConstraint) fleetConstraint).getTargetNumberOfCargoes(),
								(long) ((TargetCargoesOnVesselConstraint) fleetConstraint).getWeight());
					}
				}
			}
		}
	}
}

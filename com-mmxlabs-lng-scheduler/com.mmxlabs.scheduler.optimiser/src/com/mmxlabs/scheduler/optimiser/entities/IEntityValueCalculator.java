/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.evaluation.HeelValueRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.SlotHeelVolumeRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public interface IEntityValueCalculator {

	/**
	 * Evaluate a Cargo based {@link VoyagePlan} - returning the post tax P&L value
	 * 
	 * @param plan
	 * @param cargoValueAllocation
	 *            - empty {@link CargoValueAnnotation} object to populate. Initialised with an existing {@link IAllocationAnnotation}.
	 * @param vessel
	 * @param vesselStartTime
	 * @param annotatedSolution
	 * @return
	 */
	Pair<CargoValueAnnotation, Long> evaluate(VoyagePlan plan, IAllocationAnnotation currentAllocation, IVesselAvailability vesselAvailability,
			@Nullable ProfitAndLossSequences volumeAllocatedSequences, @Nullable IAnnotatedSolution annotatedSolution);

	/**
	 * Evaluate a non-cargo based {@link VoyagePlan} returning the post tax P&L value
	 * 
	 * @param plan
	 * @param vessel
	 * @param planStartTime
	 * @param vesselStartTime
	 * @param heelRecords
	 * @param annotatedSolution
	 * @return
	 */
	Pair<Map<IPortSlot, HeelValueRecord>, Long> evaluateNonCargoPlan(VoyagePlan plan, IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability, 
			int vesselStartTime, int planStartTime, @Nullable IPort firstLoadPort, 
			int lastHeelPricePerMMBTU, Map<IPortSlot, SlotHeelVolumeRecord> heelRecords, @Nullable IAnnotatedSolution annotatedSolution);

	/**
	 * Evaluate an unused port slot for P&L contributions (e.g. cancellation fees). The port slot is expected to be an instanceof {@link ILoadOption} or {@link IDischargeOption}.
	 * 
	 * @param portSlot
	 * @param annotatedSolution
	 * @return
	 */
	long evaluateUnusedSlot(IPortSlot portSlot, @Nullable IAnnotatedSolution annotatedSolution);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public interface IEntityValueCalculator {

	/**
	 * Evaluate a Cargo based {@link VoyagePlan} - returning the post tax P&L value
	 * 
	 * @param plan
	 * @param cargoValueAllocation - empty {@link CargoValueAnnotation} object to populate. Initialised with an existing {@link IAllocationAnnotation}.
	 * @param vessel
	 * @param vesselStartTime
	 * @param annotatedSolution
	 * @return
	 */
	long evaluate(@NonNull VoyagePlan plan, @NonNull CargoValueAnnotation currentAllocation, @NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @Nullable IAnnotatedSolution annotatedSolution);

	/**
	 * Evaluate a non-cargo based {@link VoyagePlan} returning the post tax P&L value
	 * 
	 * @param plan
	 * @param vessel
	 * @param planStartTime
	 * @param vesselStartTime
	 * @param annotatedSolution
	 * @return
	 */
	long evaluate(@NonNull VoyagePlan plan, @NonNull IVesselAvailability vesselAvailability, int planStartTime, int vesselStartTime, @Nullable IAnnotatedSolution annotatedSolution);

	/**
	 * Evaluate an unused port slot for P&L contributions (e.g. cancellation fees). The port slot is expected to be an instanceof {@link ILoadOption} or {@link IDischargeOption}.
	 * 
	 * @param portSlot
	 * @param annotatedSolution
	 * @return
	 */
	long evaluateUnusedSlot(@NonNull IPortSlot portSlot, @Nullable IAnnotatedSolution annotatedSolution);
}

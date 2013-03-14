package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.ProfitAndLossAllocationComponent;

/**
 * The {@link IProfitAndLossDetails} interface is intended to be used by {@link VoyageDetails} and {@link PortDetails} as a common interface to store total group P&L information. While P&L information
 * is attached to elements via annotations of type {@link IProfitAndLossAnnotation}, these are only generated during export. This interface is intended for internal use to hook into API such as the
 * {@link ProfitAndLossAllocationComponent}.
 * 
 * @author Simon Goodall
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * 
 */
public interface IProfitAndLossDetails {

	long getTotalGroupProfitAndLoss();

	void setTotalGroupProfitAndLoss(long totalGroupProfitAndLoss);
}

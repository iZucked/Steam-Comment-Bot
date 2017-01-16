/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public interface IStartEndRequirementProviderEditor extends IStartEndRequirementProvider {

	void setNotionalEndTime(int endTime);
	
	void setStartEndRequirements(@NonNull IResource resource, @NonNull IStartRequirement startRequirement, @NonNull IEndRequirement endRequirement);

	void setStartEndElements(@NonNull IResource resource, @NonNull ISequenceElement startElement, @NonNull ISequenceElement endElement);
}

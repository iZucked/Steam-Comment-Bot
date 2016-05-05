/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public interface IStartEndRequirementProvider extends IDataComponentProvider {
	
	int getNotionalEndTime();
	
	@NonNull
	IStartRequirement getStartRequirement(@NonNull IResource resource);

	@NonNull
	IEndRequirement getEndRequirement(@NonNull IResource resource);

	@NonNull
	ISequenceElement getEndElement(@NonNull IResource resource);

	@NonNull
	ISequenceElement getStartElement(@NonNull IResource resource);
}

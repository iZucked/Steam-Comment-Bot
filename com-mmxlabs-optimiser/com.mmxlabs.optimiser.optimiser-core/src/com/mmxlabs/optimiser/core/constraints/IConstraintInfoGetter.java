/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;

@NonNullByDefault
public interface IConstraintInfoGetter {

	List<Object> getFailedConstraintInfos(ISequences sequences, @Nullable Collection<IResource> changedResources);
}

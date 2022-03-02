/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

@NonNullByDefault
public interface IResourceElementConstraintChecker {
	boolean checkElement(ISequenceElement element, IResource resource, @Nullable List<@NonNull String> messages);
}

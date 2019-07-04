package com.mmxlabs.optimiser.core.constraints;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

@NonNullByDefault
public interface IResourceElementConstraintChecker {
	boolean checkElement(ISequenceElement element, IResource resource);
}

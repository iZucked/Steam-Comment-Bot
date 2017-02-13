package com.mmxlabs.scheduler.optimiser.lso.guided;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public interface ISelectedElementFilter {

	boolean canSelect(@NonNull ISequenceElement element, @Nullable IResource currentResource);
}

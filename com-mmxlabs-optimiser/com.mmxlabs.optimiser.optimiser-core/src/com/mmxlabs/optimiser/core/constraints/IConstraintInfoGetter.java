package com.mmxlabs.optimiser.core.constraints;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;

@NonNullByDefault
public interface IConstraintInfoGetter {
	public List<Object> getFailedConstraintInfos(final ISequences sequences, @Nullable final Collection<IResource> changedResources);
}

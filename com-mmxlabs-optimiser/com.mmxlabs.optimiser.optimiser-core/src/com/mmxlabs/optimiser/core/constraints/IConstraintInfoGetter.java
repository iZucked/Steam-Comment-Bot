package com.mmxlabs.optimiser.core.constraints;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;

public interface IConstraintInfoGetter {
	public @NonNull List<Object> getFailedConstraintInfos(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources);
}

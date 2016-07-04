/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.INullMove;

public class NullMove implements INullMove {

	@Override
	public Collection<@NonNull IResource> getAffectedResources() {
		// returns an empty list
		return Collections.emptyList();
	}

	@Override
	public void apply(@NonNull IModifiableSequences sequences) {
	}

	@Override
	public boolean validate(@NonNull ISequences sequences) {
		return false;
	}

}

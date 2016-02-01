/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public interface IFollowersAndPreceders {

	@NonNull
	Followers<ISequenceElement> getValidFollowers(@NonNull ISequenceElement e);

	@NonNull
	Followers<ISequenceElement> getValidPreceders(@NonNull ISequenceElement e);
}

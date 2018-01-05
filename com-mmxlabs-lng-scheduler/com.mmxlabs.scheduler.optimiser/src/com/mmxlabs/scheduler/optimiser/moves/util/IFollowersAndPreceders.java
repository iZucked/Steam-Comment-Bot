/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public interface IFollowersAndPreceders {

	@NonNull
	Followers<@NonNull  ISequenceElement> getValidFollowers(@NonNull ISequenceElement e);

	@NonNull
	Followers<@NonNull  ISequenceElement> getValidPreceders(@NonNull ISequenceElement e);
}

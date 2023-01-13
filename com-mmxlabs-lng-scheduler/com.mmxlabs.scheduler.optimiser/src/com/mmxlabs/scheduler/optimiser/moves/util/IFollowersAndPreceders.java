/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

@NonNullByDefault
public interface IFollowersAndPreceders {

	Followers<ISequenceElement> getValidFollowers(ISequenceElement e);

	Followers<ISequenceElement> getValidPreceders(ISequenceElement e);
}

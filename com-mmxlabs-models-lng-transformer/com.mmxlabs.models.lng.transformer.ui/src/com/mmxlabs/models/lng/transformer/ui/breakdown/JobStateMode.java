/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.Serializable;

/**
 * The current state of a {@link ChangeSet}.
 * 
 * @author sg
 *
 */
public enum JobStateMode implements Serializable {
	/**
	 * Default state, either we have a complete change set - or we have just started (changes.size() == 0, unless first level of recursion)
	 */
	BRANCH,

	/**
	 * Completed job state. we have a full list of changesets to get to the target solution. Do not expect anything in the changes list
	 */
	LEAF,

	/**
	 * A partial list of changes which does not get to a valid state -- but could do if we allow a larger changeset size.
	 */
	LIMITED,

	/**
	 * We have somehow evolved to a state where we cannot make any further changes, but also we have not reached our target state
	 */
	INVALID
}

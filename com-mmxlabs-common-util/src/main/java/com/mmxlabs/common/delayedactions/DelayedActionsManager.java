/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Simon Goodall
 * 
 */

public final class DelayedActionsManager {

	private final List<Runnable> actions = new ArrayList<Runnable>(20);

	/**
	 * Apply all the delayed mappings. This assumes all the required objects
	 * have been added using {@link #register(Class, String, Object)}. The
	 * delayed mappings will be cleared once completed.
	 */
	public final void performActions() {
		for (final Runnable r : actions) {
			r.run();
		}

		// Clear actions now they have been executed.
		// We could also re-write this code to remove as used
		actions.clear();
	}

	/**
	 * Queue a new {@link Runnable} for execution during the next invocation of
	 * {@link #performActions()}.
	 * 
	 * @param r
	 */
	public final void queue(Runnable r) {
		actions.add(r);
	}

	public void dispose() {
		actions.clear();
	}
}

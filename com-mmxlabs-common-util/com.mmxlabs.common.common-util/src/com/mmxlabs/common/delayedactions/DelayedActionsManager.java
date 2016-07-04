/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * 
 * @author Simon Goodall
 * 
 */

public final class DelayedActionsManager {
	/**
	 * Queue of actions to perform.
	 */
	private final Queue<Runnable> actions = new ConcurrentLinkedQueue<Runnable>();

	/**
	 * Apply all the delayed mappings. This assumes all the required objects have been added using {@link #register(Class, String, Object)}. The delayed mappings will be cleared once completed.
	 * 
	 * Actions can add more actions back to the queue while executing without causing problems, but watch out for infinite loops if you do this.
	 */
	public final void performActions() {
		Runnable r = null;
		while ((r = actions.poll()) != null) {
			r.run();
		}
	}

	/**
	 * Queue a new {@link Runnable} for execution during the next invocation of {@link #performActions()}.
	 * 
	 * @param r
	 */
	public final void queue(final Runnable r) {
		actions.add(r);
	}

	public void dispose() {
		actions.clear();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.locking;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to record {@link Runnable}s until some condition is set. Runnables will be delayed until the trigger is set. If the trigger has been set, runnables will execute immediately.
 * 
 */
@NonNullByDefault
public final class SingleUseTrigger {

	private static final Logger LOGGER = LoggerFactory.getLogger(SingleUseTrigger.class);

	private boolean triggered = false;
	private final Object lockObject = new Object();
	private final List<Runnable> runnables = new LinkedList<>();

	/**
	 * If the trigger has not been set yet, the runnables will be record and the method will return. If the trigger has already been set, runnables will execute immediately and this method will
	 * block..
	 * 
	 * @param r
	 */
	public void delayUntilTriggered(final Runnable r) {

		synchronized (lockObject) {
			if (triggered) {
				r.run();
			} else {
				runnables.add(r);
			}
		}
	}

	/**
	 * Mark as triggered. This will block until all runnables have completed.
	 */
	public void fireTrigger() {

		// Quick return
		if (triggered) {
			return;
		}
		synchronized (lockObject) {
			triggered = true;
			while (!runnables.isEmpty()) {
				try {
					runnables.remove(0).run();
				} catch (final Exception e) {
					LOGGER.error("Uncaught exception in runnable " + e.getMessage(), e);
				}
			}
		}
	}

}

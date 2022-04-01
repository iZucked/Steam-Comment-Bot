/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

public abstract class AbstractWriteLockable implements IWriteLockable {

	private boolean writeLocked = false;

	@Override
	public void writeLock() {
		writeLocked = true;
	}

	protected void checkWritable() {
		if (writeLocked) {
			throw new WriteLockedException();
		}
	}

}

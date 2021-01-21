/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * A lock implementation for limiting control around data referenced by a {@link ScenarioInstance}. This wraps round the write portion of a {@link ReadWriteLock} and adds listener notification
 * support.
 * 
 * @author Simon Goodall
 *
 */
public final class ScenarioLock {

	private static final Logger LOG = LoggerFactory.getLogger(ScenarioLock.class);

	private final @NonNull ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final @NonNull ConcurrentLinkedQueue<com.mmxlabs.scenario.service.model.manager.IScenarioLockListener> listeners = new ConcurrentLinkedQueue<>();
	private final @NonNull ModelRecord modelRecord;
	private boolean lockState;

	public ScenarioLock(final @NonNull ModelRecord modelRecord) {
		this.modelRecord = modelRecord;
	}

	public boolean tryLock() {
		final boolean r = readWriteLock.writeLock().tryLock();
		if (r) {
			fireLockStateChanged(true);
		}
		return r;
	}

	public boolean tryLock(int timeOutInMillis) {
		boolean r;
		try {
			r = readWriteLock.writeLock().tryLock(timeOutInMillis, TimeUnit.MILLISECONDS);
			if (r) {
				fireLockStateChanged(true);
			}
			return r;
		} catch (InterruptedException e) {
			return false;
		}
	}

	public boolean isLocked() {
		return lockState;
	}

	public void lock() {
		readWriteLock.writeLock().lock();
		fireLockStateChanged(true);
	}

	public void unlock() {
		readWriteLock.writeLock().unlock();
		fireLockStateChanged(false);
	}

	private void fireLockStateChanged(final boolean lockState) {
		this.lockState = lockState;
		for (final IScenarioLockListener l : listeners) {
			// Safe loop
			try {
				l.lockStateChanged(modelRecord, lockState);
			} catch (Exception e) {
				LOG.error("Error in lock listener", e);
			}
		}
	}

	public void addLockListener(@NonNull final IScenarioLockListener lockListener) {
		listeners.add(lockListener);
	}

	public void removeLockListener(@NonNull final IScenarioLockListener lockListener) {
		listeners.remove(lockListener);
	}

	public void withLock(@NonNull Runnable r) {
		lock();
		try {
			r.run();
		} finally {
			unlock();
		}
	}
}

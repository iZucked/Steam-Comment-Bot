/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.manager.SSDataManager.PostChangeType;

public class ModelRecord {

	private static final Logger LOG = LoggerFactory.getLogger(ModelRecord.class);

	protected final @NonNull Lock referenceLock = new ReentrantLock();

	protected int referenceCount = 0;

	protected boolean readOnly = false;

	protected @Nullable InstanceData data = null;

	private Exception loadFailure;

	private final @NonNull BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunction;

	/**
	 * List of model references used for debugging open references.
	 */
	protected final List<WeakReference<ModelReference>> referencesList = new LinkedList<>();

	protected final @NonNull ConcurrentLinkedQueue<IScenarioLockListener> lockListeners = new ConcurrentLinkedQueue<>();
	protected final @NonNull ConcurrentLinkedQueue<IScenarioDirtyListener> dirtyListeners = new ConcurrentLinkedQueue<>();

	protected ModelReference sharedReference = null;

	public ModelRecord(final @NonNull BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunction) {
		this.loadFunction = loadFunction;
	}

	public boolean isLoaded() {
		return data != null;
	}

	public @NonNull ModelReference aquireReference(final @NonNull String referenceID) {
		return aquireReference(referenceID, new NullProgressMonitor());
	}

	public @NonNull ModelReference aquireReference(final @NonNull String referenceID, @NonNull final IProgressMonitor monitor) {
		try {
			referenceLock.lock();
			++referenceCount;
			boolean triggerLoadCallback = false;
			if (data == null && referenceCount == 1) {
				// assert data == null;
				InstanceData pData = loadFunction.apply(this, monitor);
				if (pData == null) {
					throw new RuntimeException(loadFailure);
				}
				data = pData;

				for (final IScenarioLockListener l : lockListeners) {
					pData.getLock().addLockListener(l);
				}
				for (final IScenarioDirtyListener l : dirtyListeners) {
					pData.addDirtyListener(l);
				}
				assert sharedReference == null;
				sharedReference = ModelReference.createSharedReference(this, referenceID, pData);

				triggerLoadCallback = true;

				data.setReadOnly(readOnly);
			}
			if (loadFailure != null) {
				throw new RuntimeException(loadFailure);
			}

			assert data != null;

			final ModelReference modelReference = new ModelReference(this, referenceID, data);
			referencesList.add(new WeakReference<>(modelReference));

			if (triggerLoadCallback) {
				runPostChangeHooks(PostChangeType.LOAD);
			}

			return modelReference;
		} finally {
			referenceLock.unlock();
		}
	}

	public void runPostChangeHooks(PostChangeType type) {

	}

	public @Nullable ModelReference aquireReferenceIfLoaded(final @NonNull String referenceID) {
		try {
			// We may be loading, but timeout on slow loads and assume not loaded.
			if (referenceLock.tryLock(100, TimeUnit.MILLISECONDS)) {
				try {
					if (referenceCount == 0) {
						return null;
					} else {
						if (loadFailure == null) {
							return aquireReference(referenceID);
						} else {
							return null;
						}
					}
				} finally {
					referenceLock.unlock();
				}
			}
		} catch (final InterruptedException e) {
			// Timeout, fallthrough to null
		}
		return null;
	}

	void releaseReference() {
		try {
			referenceLock.lock();
			--referenceCount;
			if (referenceCount == 0) {
				// Release strong reference
				if (data != null && !data.isDirty()) {
					for (final IScenarioLockListener l : lockListeners) {
						data.getLock().removeLockListener(l);
					}
					for (final IScenarioDirtyListener l : dirtyListeners) {
						data.removeDirtyListener(l);
					}
					runPostChangeHooks(PostChangeType.UNLOAD);

					sharedReference.close();
					sharedReference = null;
					data.close();
					data = null;
					// Reset validation status
					onPostUnload();
				}
			}
			cleanupReferencesList();
		} finally {
			referenceLock.unlock();
		}
	}

	protected void onPostUnload() {

	}

	protected void cleanupReferencesList() {
		final Iterator<WeakReference<ModelReference>> itr = referencesList.iterator();
		while (itr.hasNext()) {
			final WeakReference<ModelReference> ref = itr.next();
			if (ref.get() == null) {
				itr.remove();
			}
		}
	}

	public boolean isLoadFailure() {
		return loadFailure != null;
	}

	public void setLoadFailure(final Exception loadFailure) {
		this.loadFailure = loadFailure;
	}

	public Exception getLoadFailure() {
		return loadFailure;
	}

	public void addDirtyListener(@NonNull final IScenarioDirtyListener l) {
		dirtyListeners.add(l);
		if (data != null) {
			try {
				referenceLock.lock();
				if (data != null) {
					data.addDirtyListener(l);
				}
			} finally {
				referenceLock.unlock();
			}
		}
	}

	public void removeDirtyListener(@NonNull final IScenarioDirtyListener l) {
		dirtyListeners.remove(l);
	}

	public void addLockListener(@NonNull final IScenarioLockListener l) {
		lockListeners.add(l);
		if (data != null) {
			try {
				referenceLock.lock();
				if (data != null) {
					data.getLock().addLockListener(l);
				}
			} finally {
				referenceLock.unlock();
			}
		}
	}

	public void removeLockListener(@NonNull final IScenarioLockListener l) {
		lockListeners.remove(l);
	}

	/**
	 * Execute some code within the context of a {@link ModelReference}
	 */
	public void execute(final @NonNull Consumer<ModelReference> hook) {
		try (ModelReference ref = aquireReference("ModelRecord:1")) {
			hook.accept(ref);
		}
	}

	public void dispose() {
		// Right now we are asserting that we have been cleaned-up properly
		if (data != null) {
			LOG.error("ModelRecord #disposed before unloaded");
		}
		// assert data == null;
		// assert lockListeners.isEmpty();
		// assert dirtyListeners.isEmpty();
	}

	/**
	 * Debugging method to dump open model references.
	 */
	public void dumpReferences() {
		try {
			referenceLock.lock();
			final Iterator<WeakReference<ModelReference>> itr = referencesList.iterator();
			while (itr.hasNext()) {
				final WeakReference<ModelReference> ref = itr.next();
				final ModelReference reference = ref.get();
				if (reference == null) {
					itr.remove();
				} else {
					if (reference.isValid()) {
						final String refID = reference.getReferenceID();
						LOG.error("ModelReference present from " + refID);
						System.err.println("ModelReference present from " + refID);
					}
				}
			}
		} finally {
			referenceLock.unlock();
		}
	}

	public @Nullable ModelReference getSharedReference() {
		return sharedReference;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		if (data != null) {
			data.setReadOnly(readOnly);
		}
	}

	public boolean isReadOnly() {
		return readOnly;
	}
}
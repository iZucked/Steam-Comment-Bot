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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.manager.SSDataManager.PostChangeType;

public final class ModelRecord {

	private static final Logger LOG = LoggerFactory.getLogger(ModelRecord.class);

	@NonNull
	private final Lock referenceLock = new ReentrantLock();

	private int referenceCount = 0;

	protected boolean readOnly = false;

	@Nullable
	private InstanceData data = null;

	private Exception loadFailure;

	@NonNull
	private final BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunction;

	/**
	 * List of model references used for debugging open references.
	 */
	private final List<WeakReference<ModelReference>> referencesList = new LinkedList<>();

	@NonNull
	private IStatus validationStatus = Status.OK_STATUS;

	private final @NonNull ConcurrentLinkedQueue<@NonNull IScenarioValidationListener> validationListeners = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<@NonNull IScenarioLockListener> lockListeners = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<@NonNull IScenarioDirtyListener> dirtyListeners = new ConcurrentLinkedQueue<>();

	private final @NonNull ScenarioInstance scenarioInstance;

	private ModelReference sharedReference = null;

	public ModelRecord(final @NonNull ScenarioInstance scenarioInstance, final BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunction) {
		this.scenarioInstance = scenarioInstance;
		this.loadFunction = loadFunction;
		if (scenarioInstance != null) {
			this.validationStatus = new Status(scenarioInstance.getValidationStatusCode(), "com.mmxlabs.scenario.service.model", "(Open scenario to refresh validation status)");
			setReadOnly(scenarioInstance.isReadonly());
			scenarioInstance.eAdapters().add(readOnlyAdapter);
		}
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
			if (referenceCount == 1) {
				assert data == null;
				data = loadFunction.apply(this, monitor);
				if (data == null) {
					throw new RuntimeException(loadFailure);
				}

				for (final IScenarioLockListener l : lockListeners) {

					data.getLock().addLockListener(l);
				}
				for (final IScenarioDirtyListener l : dirtyListeners) {
					data.addDirtyListener(l);
				}
				assert sharedReference == null;
				sharedReference = ModelReference.createSharedReference(this, referenceID, data);

				data.setReadOnly(readOnly);
			}
			if (loadFailure != null) {
				throw new RuntimeException(loadFailure);
			}
			if (referenceCount > 0) {
				assert data != null;
			}

			data.setReadOnly(readOnly);

			final ModelReference modelReference = new ModelReference(this, referenceID, data);
			referencesList.add(new WeakReference<ModelReference>(modelReference));
			return modelReference;
		} finally {
			referenceLock.unlock();
		}
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
				if (data != null) {
					for (final IScenarioLockListener l : lockListeners) {
						data.getLock().removeLockListener(l);
					}
					for (final IScenarioDirtyListener l : dirtyListeners) {
						data.removeDirtyListener(l);
					}
					SSDataManager.Instance.runPostChangeHooks(this, PostChangeType.UNLOAD);
					sharedReference.close();
					sharedReference = null;
					data.close();
				}
				data = null;
				// Reset validation status
				// validationStatus = Status.OK_STATUS;
				validationStatus = new Status(scenarioInstance.getValidationStatusCode(), "com.mmxlabs.scenario.service.model", "(Open scenario to refresh validation status)");

			}
			cleanupReferencesList();
		} finally {
			referenceLock.unlock();
		}
	}

	private void cleanupReferencesList() {
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

	public void setValidationStatus(final @NonNull IStatus status) {
		this.validationStatus = status;
		if (scenarioInstance != null) {
			scenarioInstance.setValidationStatusCode(status.getSeverity());
		}
		fireValidationStatusChanged(status);
	}

	public void setLoadFailure(final Exception loadFailure) {
		this.loadFailure = loadFailure;
	}

	public Exception getLoadFailure() {
		return loadFailure;
	}

	public int getValidationStatusSeverity() {
		return validationStatus.getSeverity();
	}

	public @NonNull IStatus getValidationStatus() {
		return validationStatus;
	}

	private void fireValidationStatusChanged(final @NonNull IStatus status) {

		for (final IScenarioValidationListener l : validationListeners) {
			// Safe loop
			try {
				l.validationChanged(this, status);
			} catch (final Exception e) {
				LOG.error("Error in validation listener", e);
			}
		}
	}

	public void addValidationListener(@NonNull final IScenarioValidationListener l) {
		validationListeners.add(l);
	}

	public void removeValidationListener(@NonNull final IScenarioValidationListener l) {
		validationListeners.remove(l);
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

	// public ScenarioInstance getScenarioInstance() {
	// return scenarioInstance;
	// }

	private final AdapterImpl readOnlyAdapter = new AdapterImpl() {
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			if (msg.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_Readonly()) {
				setReadOnly(msg.getNewBooleanValue());
			}
		};
	};

	/**
	 * Execute some code within the context of a {@link ModelReference}
	 */
	public void execute(final @NonNull Consumer<@NonNull ModelReference> hook) {
		try (ModelReference ref = aquireReference("ModelRecord:1")) {
			hook.accept(ref);
		}
	}

	public void dispose() {
		// Right now we are asserting that we have been cleaned-up properly
		if (data != null) {
			LOG.error("ModelRecord #disposed before unloaded");
		}
		if (scenarioInstance != null) {
			scenarioInstance.eAdapters().remove(readOnlyAdapter);
		}
		// assert data == null;
		// assert lockListeners.isEmpty();
		// assert validationListeners.isEmpty();
		// assert dirtyListeners.isEmpty();
	}

	public String getName() {
		return scenarioInstance.getName();
	}

	public void revert() {
		// Wait a little for reference to get closed
		for (int i = 0; i < 100; ++i) {
			System.gc();
			if (referencesList.isEmpty()) {
				break;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}
		dumpReferences();

		try {
			referenceLock.lock();
			referenceCount = 0;
			if (referenceCount == 0) {
				// Release strong reference
				if (data != null) {
					for (final IScenarioLockListener l : lockListeners) {
						data.getLock().removeLockListener(l);
					}
					for (final IScenarioDirtyListener l : dirtyListeners) {
						data.removeDirtyListener(l);
					}
					SSDataManager.Instance.runPostChangeHooks(this, PostChangeType.UNLOAD);
					sharedReference.close();
					sharedReference = null;
					data.close();
				}
				data = null;
				// Reset validation status
				validationStatus = Status.OK_STATUS;
			}
			cleanupReferencesList();
		} finally {
			referenceLock.unlock();
		}
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
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
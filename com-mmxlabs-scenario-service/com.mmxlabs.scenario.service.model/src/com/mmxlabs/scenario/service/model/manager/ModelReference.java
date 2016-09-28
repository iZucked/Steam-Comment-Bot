package com.mmxlabs.scenario.service.model.manager;

import java.io.Closeable;
import java.io.IOException;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelReference implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelReference.class);

	private @NonNull final ModelRecord record;
	private @NonNull final InstanceData data;
	private boolean valid = true;
	private final @NonNull String referenceID;

	private boolean shared;

	public ModelReference(final @NonNull ModelRecord record, final @NonNull String referenceID, @NonNull final InstanceData data) {
		this.record = record;
		this.referenceID = referenceID;
		this.data = data;
	}

	public static ModelReference createSharedReference(final @NonNull ModelRecord record, final @NonNull String referenceID, @NonNull final InstanceData data) {
		ModelReference ref = new ModelReference(record, referenceID, data);
		ref.setShared(true);
		return ref;
	}

	private void setShared(boolean shared) {
		this.shared = shared;
	}

	public @NonNull EObject getInstance() {
		if (valid) {
			final EObject obj = data.getInstance();
			assert obj != null;
			return obj;
		}
		throw new IllegalStateException();
	}

	@Override
	public void close() {
		if (!shared && valid) {
			synchronized (record) {
				if (valid) {
					valid = false;
					record.releaseReference();
				}
			}
		}
	}

	public @NonNull EditingDomain getEditingDomain() {
		if (valid) {
			final EditingDomain obj = data.getEditingDomain();
			assert obj != null;
			return obj;
		}
		throw new IllegalStateException();
	}

	public @NonNull CommandStack getCommandStack() {
		if (valid) {
			final CommandStack obj = data.getCommandStack();
			assert obj != null;
			return obj;
		}
		throw new IllegalStateException();
	}

	public @NonNull ScenarioLock getLock() {
		return data.getLock();
	}

	public boolean isLocked() {
		return data.getLock().isLocked();
	}

	public void save() throws IOException {
		data.save();
	}

	public boolean isDirty() {
		return data.isDirty();
	}

	/**
	 * Execute some code within the context of a write lock.
	 */
	public void executeWithLock(final Runnable hook) {
		final ScenarioLock lock = getLock();
		lock.lock();
		try {
			hook.run();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Execute some code within the context of a write lock.
	 */
	public boolean executeWithTryLock(final Runnable hook) {
		final ScenarioLock lock = getLock();
		if (lock.tryLock()) {
			try {
				hook.run();
			} finally {
				lock.unlock();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (valid) {
			// No need for this warning - we tend to just leave it up to the GC to free up ram now.
			// final String msg = String.format("ModelReference from %s is valid during finalisation", referenceID);
			// LOGGER.error(msg);
			// System.err.println(msg);
		}
		super.finalize();
	}

	public String getReferenceID() {
		return referenceID;
	}

	public boolean isValid() {
		return valid;
	}

	public void setLastEvaluationFailed(boolean value) {
		data.setLastEvaluationFailed(value);
	}

	public boolean isLastEvaluationFailed() {
		return data.isLastEvaluationFailed();
	}

	public void setDirty() {
		data.setDirty(true);
	}
}

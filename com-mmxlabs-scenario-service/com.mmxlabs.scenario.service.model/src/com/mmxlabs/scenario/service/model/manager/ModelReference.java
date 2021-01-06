/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.io.Closeable;
import java.io.IOException;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;

@NonNullByDefault
public class ModelReference implements Closeable {

	private final ModelRecord record;
	private final InstanceData data;
	private final String referenceID;
	private boolean valid = true;

	private boolean shared;

	public ModelReference(final ModelRecord record, final String referenceID, final InstanceData data) {
		this.record = record;
		this.referenceID = referenceID;
		this.data = data;
	}

	public static ModelReference createSharedReference(final ModelRecord record, final String referenceID, final InstanceData data) {
		final ModelReference ref = new ModelReference(record, referenceID, data);
		ref.setShared(true);
		return ref;
	}

	private void setShared(final boolean shared) {
		this.shared = shared;
	}

	public EObject getInstance() {
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

	public EditingDomain getEditingDomain() {
		if (valid) {
			final EditingDomain obj = data.getEditingDomain();
			assert obj != null;
			return obj;
		}
		throw new IllegalStateException();
	}

	public CommandStack getCommandStack() {
		if (valid) {
			final CommandStack obj = data.getCommandStack();
			assert obj != null;
			return obj;
		}
		throw new IllegalStateException();
	}

	public ScenarioLock getLock() {
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
	public void executeWithLock(final boolean disableCommandProviders, final Runnable hook) {
		final ScenarioLock lock = getLock();
		lock.lock();

		try {
			CommandProviderAwareEditingDomain commandProviderEditingDomain = null;
			try {
				if (disableCommandProviders) {
					final EditingDomain editingDomain = getEditingDomain();
					if (editingDomain instanceof CommandProviderAwareEditingDomain) {
						commandProviderEditingDomain = (CommandProviderAwareEditingDomain) editingDomain;
						commandProviderEditingDomain.setCommandProvidersDisabled(true);
					}
				}

				hook.run();
			} finally {
				if (commandProviderEditingDomain != null) {
					commandProviderEditingDomain.setCommandProvidersDisabled(false);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Execute some code within the context of a write lock.
	 */
	public boolean executeWithTryLock(final boolean disableCommandProviders, final Runnable hook) {
		final ScenarioLock lock = getLock();
		if (lock.tryLock()) {
			try {
				CommandProviderAwareEditingDomain commandProviderEditingDomain = null;
				try {
					if (disableCommandProviders) {
						final EditingDomain editingDomain = getEditingDomain();
						if (editingDomain instanceof CommandProviderAwareEditingDomain) {
							commandProviderEditingDomain = (CommandProviderAwareEditingDomain) editingDomain;
							commandProviderEditingDomain.setCommandProvidersDisabled(true);
						}
					}

					hook.run();
				} finally {
					if (commandProviderEditingDomain != null) {
						commandProviderEditingDomain.setCommandProvidersDisabled(false);
					}
				}
			} finally {
				lock.unlock();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Execute some code within the context of a write lock.
	 */
	public boolean executeWithTryLock(final boolean disableCommandProviders, int timeOutInMillis, final Runnable hook) {
		final ScenarioLock lock = getLock();
		if (lock.tryLock(timeOutInMillis)) {
			try {
				CommandProviderAwareEditingDomain commandProviderEditingDomain = null;
				try {
					if (disableCommandProviders) {
						final EditingDomain editingDomain = getEditingDomain();
						if (editingDomain instanceof CommandProviderAwareEditingDomain) {
							commandProviderEditingDomain = (CommandProviderAwareEditingDomain) editingDomain;
							commandProviderEditingDomain.setCommandProvidersDisabled(true);
						}
					}

					hook.run();
				} finally {
					if (commandProviderEditingDomain != null) {
						commandProviderEditingDomain.setCommandProvidersDisabled(false);
					}
				}
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
			// No need for this warning - we tend to just leave it up to the GC to free up
			// ram now.
			// final String msg = String.format("ModelReference from %s is valid during
			// finalisation", referenceID);
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

	public void setLastEvaluationFailed(final boolean value) {
		data.setLastEvaluationFailed(value);
	}

	public boolean isLastEvaluationFailed() {
		return data.isLastEvaluationFailed();
	}

	public void setDirty() {
		data.setDirty(true);
	}
}

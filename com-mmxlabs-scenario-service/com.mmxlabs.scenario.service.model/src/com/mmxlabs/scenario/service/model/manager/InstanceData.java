package com.mmxlabs.scenario.service.model.manager;

import java.io.IOException;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.manager.SSDataManager.PostChangeType;

public final class InstanceData {

	private static final Logger LOG = LoggerFactory.getLogger(InstanceData.class);

	private final @NonNull EObject instance;

	private final @NonNull EditingDomain editingDomain;

	private final @NonNull BasicCommandStack commandStack;

	private final @NonNull ScenarioLock lock;

	private boolean isDirty;

	private final @NonNull ModelRecord modelRecord;

	private final Consumer<InstanceData> saveCallback;
	private final Consumer<InstanceData> closeCallback;

	private final @NonNull ConcurrentLinkedQueue<@NonNull IScenarioDirtyListener> dirtyListeners = new ConcurrentLinkedQueue<>();

	private boolean lastEvaluationFailed = false;

	public InstanceData(@NonNull final ModelRecord modelRecord, @NonNull final EObject instance, @NonNull final EditingDomain editingDomain, @NonNull final BasicCommandStack commandStack,
			final Consumer<InstanceData> saveCallback, final Consumer<InstanceData> closeCallback) {
		this.modelRecord = modelRecord;
		this.instance = instance;
		this.editingDomain = editingDomain;
		this.commandStack = commandStack;
		this.saveCallback = saveCallback;
		this.closeCallback = closeCallback;
		this.lock = new ScenarioLock(modelRecord);

		commandStack.addCommandStackListener(new CommandStackListener() {
			@Override
			public void commandStackChanged(final EventObject event) {
				// TODO: Determine Undo/Redo
				final boolean newDirty = commandStack.isSaveNeeded();
				final boolean fireEvent = newDirty != isDirty;
				isDirty = newDirty;
				if (fireEvent) {
					fireDirtyEvent(isDirty);
				}
				SSDataManager.Instance.runPostChangeHooks(modelRecord, PostChangeType.EDIT);
			}
		});
		// Trigger initial hooks.
		SSDataManager.Instance.runPostChangeHooks(modelRecord, PostChangeType.LOAD);
	}

	private void fireDirtyEvent(final boolean newDirty) {
		for (final IScenarioDirtyListener l : dirtyListeners) {
			// Safe loop
			try {
				l.dirtyStatusChanged(modelRecord, newDirty);
			} catch (final Exception e) {
				LOG.error("Error in dirty listener", e);
			}
		}
	}

	public boolean isDirty() {
		return isDirty;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void save() throws IOException {
		saveCallback.accept(this);
		final boolean newDirty = commandStack.isSaveNeeded();
		final boolean fireEvent = newDirty != isDirty;
		isDirty = newDirty;
		if (fireEvent) {
			fireDirtyEvent(isDirty);
		}
	}
	
	public void setDirty(boolean newDirty) {
		final boolean fireEvent = newDirty != isDirty;
		isDirty = newDirty;
		if (fireEvent) {
			fireDirtyEvent(isDirty);
		}
	}

	public void close() {
		closeCallback.accept(this);
	}

	public @NonNull CommandStack getCommandStack() {
		return editingDomain.getCommandStack();
	}

	public @NonNull ScenarioLock getLock() {
		return lock;
	}

	public @NonNull EObject getInstance() {
		return instance;
	}

	public void addDirtyListener(@NonNull final IScenarioDirtyListener l) {
		dirtyListeners.add(l);
	}

	public void removeDirtyListener(@NonNull final IScenarioDirtyListener l) {
		dirtyListeners.remove(l);
	}

	public void setLastEvaluationFailed(boolean lastEvaluationFailed) {
		this.lastEvaluationFailed = lastEvaluationFailed;
	}

	public boolean isLastEvaluationFailed() {
		return lastEvaluationFailed;
	}

	/**
	 * Mark linked resources as read-only
	 * 
	 * @param readOnly
	 */
	public void setReadOnly(final boolean readOnly) {
		// Mark resources as read-only
		if (editingDomain instanceof AdapterFactoryEditingDomain) {
			final AdapterFactoryEditingDomain adapterFactoryEditingDomain = (AdapterFactoryEditingDomain) editingDomain;
			Map<Resource, Boolean> resourceToReadOnlyMap = adapterFactoryEditingDomain.getResourceToReadOnlyMap();
			// Init map if needed.
			if (resourceToReadOnlyMap == null) {
				synchronized (adapterFactoryEditingDomain) {
					// Re-do null check in the lock
					resourceToReadOnlyMap = adapterFactoryEditingDomain.getResourceToReadOnlyMap();
					if (resourceToReadOnlyMap == null) {
						// Concurrent hash map would be better, but it does not like null keys (even in #get())
						resourceToReadOnlyMap = Collections.synchronizedMap(new HashMap<>());
						adapterFactoryEditingDomain.setResourceToReadOnlyMap(resourceToReadOnlyMap);
					}
				}
			}
			// Find linked resources
			for (final Resource r : editingDomain.getResourceSet().getResources()) {
				if (r.getContents().contains(instance)) {
					resourceToReadOnlyMap.put(r, readOnly);
				}
			}
		}
	}
}

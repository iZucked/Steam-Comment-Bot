/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.models.lng.transformer.ui.jobmanagers.IJobManager;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioLockListener;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IProgressProvider.IProgressChanged;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * A {@link IEditorActionDelegate} implementation to start or resume an optimisation.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public abstract class AbstractStartOptimisationEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {
	protected IEditorPart editor;
	protected IAction action;
	protected ScenarioModelRecord lastModelRecord;

	protected AbstractStartOptimisationEditorActionDelegate() {
		ServiceHelper.withAllServices(IJobManager.class, null, mgr -> {
			mgr.addChangedListener(progressListener);
			return true;
		});

	}

	@Override
	public void dispose() {

		ServiceHelper.withAllServices(IJobManager.class, null, mgr -> {
			mgr.removeChangedListener(progressListener);
			return true;
		});

		action = null;
		editor = null;

	}

	protected final IProgressChanged progressListener = new IProgressChanged() {
		@Override
		public void changed(final Object element) {
			setActiveEditor(action, editor);
		}

		@Override
		public void listChanged() {
			setActiveEditor(action, editor);
		}
	};

	protected final IScenarioLockListener scenarioLockListener = (mr, value) -> updateState();

	@Override
	public void run(final IAction methodAction) {

		if (editor != null && editor.getEditorInput() instanceof final IScenarioServiceEditorInput editorInput) {

			final ScenarioInstance instance = editorInput.getScenarioInstance();
			if (instance == null) {
				return;
			}

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
			if (modelRecord == null) {
				return;
			}

			if (instance.isReadonly()) {
				return;
			}

			if (modelRecord.isLoadFailure()) {
				return;
			}

			doRun(instance, modelRecord);
		}
	}

	protected abstract void doRun(final ScenarioInstance instance, ScenarioModelRecord modelRecord);

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;

		if (lastModelRecord != null) {
			lastModelRecord.removeLockListener(scenarioLockListener);
			lastModelRecord = null;
		}

		updateState();

		if (lastModelRecord != null) {
			lastModelRecord.addLockListener(scenarioLockListener);
		}
	}

	public void updateState() {

		if (action != null) {
			action.setEnabled(false);
			if (editor != null && editor.getEditorInput() instanceof final IScenarioServiceEditorInput editorInput) {

				final ScenarioInstance instance = editorInput.getScenarioInstance();
				if (instance == null) {
					return;
				}

				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				if (modelRecord == null) {
					return;
				}

				lastModelRecord = modelRecord;

				if (instance.isReadonly()) {
					return;
				}

				if (modelRecord.isLoadFailure()) {
					return;
				}

				try (ModelReference ref = modelRecord.aquireReferenceIfLoaded(AbstractStartOptimisationEditorActionDelegate.class.getCanonicalName())) {
					if (ref != null) {
						action.setEnabled(!ref.isLocked());
					}
				}
			}
		}
	}
}

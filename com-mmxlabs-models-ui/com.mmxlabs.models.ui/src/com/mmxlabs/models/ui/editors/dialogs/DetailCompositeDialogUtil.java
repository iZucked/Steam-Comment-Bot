/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.Collections;
import java.util.function.IntSupplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class DetailCompositeDialogUtil {

	public static int editSingleObject(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target) {
		return editSingleObject(scenarioEditingLocation, target, null);
	}

	public static int editSingleObjectWithUndoOnCancel(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target, @Nullable Command undoCommand) {
		return editSingleObject(scenarioEditingLocation, target, () -> {
			if (undoCommand != null) {
				final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
				// If not ok, revert state;
				assert commandStack.getUndoCommand() == undoCommand;
				commandStack.undo();
			}
		});
	}

	public static int editSingleObject(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target, @Nullable final Runnable notOkRunnable) {

		return editInlock(scenarioEditingLocation, () -> {
			final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
			final int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(target), scenarioEditingLocation.isLocked());
			if (ret != Window.OK) {
				if (notOkRunnable != null) {
					notOkRunnable.run();
				}
			}
			return ret;
		});
	}

	public static int editSelection(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection structuredSelection) {

		return editSelection(scenarioEditingLocation, structuredSelection, null);
	}

	public static int editSelection(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection structuredSelection, @Nullable final Runnable notOKAction) {

		if (structuredSelection.isEmpty() == false) {
			if (structuredSelection.size() == 1) {
				return editInlock(scenarioEditingLocation, () -> {
					final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
					final int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), structuredSelection.toList(), scenarioEditingLocation.isLocked());
					if (ret != Window.OK) {
						if (notOKAction != null) {
							notOKAction.run();
						}
					}
					return ret;
				});
			} else {
				return editInlock(scenarioEditingLocation, () -> {
					final MultiDetailDialog mdd = new MultiDetailDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getRootObject(),
							scenarioEditingLocation.getDefaultCommandHandler());
					final int ret = mdd.open(scenarioEditingLocation, structuredSelection.toList());
					if (ret != Window.OK) {
						if (notOKAction != null) {
							notOKAction.run();
						}
					}
					return ret;
				});
			}
		}
		return IStatus.OK;
	}

	public static int editInlock(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final IntSupplier editFunc) {
		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		if (editorLock.tryLock(500)) {
			try {
				scenarioEditingLocation.setDisableUpdates(true);
				try {
					return editFunc.getAsInt();
				} finally {
					scenarioEditingLocation.setDisableUpdates(false);
				}
			} finally {
				editorLock.unlock();
			}
		}
		return Window.CANCEL;
	}
}

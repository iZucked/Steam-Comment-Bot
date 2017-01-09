/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.Collections;
import java.util.function.IntSupplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class DetailCompositeDialogUtil {

	public static int editSingleObject(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target) {
		return editSingleObject(scenarioEditingLocation, target, null);
	}

	public static int editSingleObject(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target, @Nullable Runnable notOkRunnable) {

		return editInlock(scenarioEditingLocation, () -> {
			final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
			int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(target), scenarioEditingLocation.isLocked());
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

	public static int editSelection(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection structuredSelection, @Nullable Runnable notOKAction) {

		if (structuredSelection.isEmpty() == false) {
			if (structuredSelection.size() == 1) {
				return editInlock(scenarioEditingLocation, () -> {
					final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
					int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), structuredSelection.toList(), scenarioEditingLocation.isLocked());
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
					int ret = mdd.open(scenarioEditingLocation, structuredSelection.toList());
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
		editorLock.awaitClaim();
		try {
			scenarioEditingLocation.setDisableUpdates(true);
			try {
				return editFunc.getAsInt();
			} finally {
				scenarioEditingLocation.setDisableUpdates(false);
			}
		} finally {
			editorLock.release();
		}
	}
}

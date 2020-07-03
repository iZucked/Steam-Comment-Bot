/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;

public final class ScenarioServiceModelUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServiceModelUtils.class);

	@Nullable
	public static ScenarioInstance createAndOpenFork(@NonNull final ScenarioInstance instance, final boolean forkAndOptimise) throws Exception {
		final String finalNewName = ScenarioServiceModelUtils.getNewForkName(instance, forkAndOptimise);
		if (finalNewName != null) {
			final ScenarioInstance fork = ScenarioServiceModelUtils.fork(instance, finalNewName, new NullProgressMonitor());

			try {
				OpenScenarioUtils.openScenarioInstance(fork);
				return fork;
			} catch (final PartInitException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@Nullable
	public static String getNewForkName(@NonNull final ScenarioInstance instance, final boolean forkAndOptimise) {
		String oldName = instance.getName();
		if (oldName == null) {
			oldName = "(untitled)";
		}
		final String namePrefix = forkAndOptimise ? ScenarioServiceUtils.getOptimisedName(oldName) : ScenarioServiceUtils.getForkName(oldName);
		final Set<String> existingNames = ScenarioServiceUtils.getExistingNames(instance);
		final String newName = ScenarioServiceUtils.getNextName(namePrefix, existingNames);
		return openNewNameForForkPrompt(oldName, newName, existingNames);
	}

	@Nullable
	public static String openNewNameForForkPrompt(@NonNull final String oldName, @NonNull final String suggestedName, @NonNull final Set<String> existingNames) {

		try {
			final IInputValidator validator = createExistingNamesValidator(existingNames);

			final Display display = PlatformUI.getWorkbench().getDisplay();
			final InputDialog dialog = new InputDialog(display.getActiveShell(), "Fork " + oldName, "Choose new name for fork", suggestedName, validator);

			if (dialog.open() == Window.OK) {
				return dialog.getValue();
			}
		} finally {

		}
		return null;
	}

	@NonNull
	public static IInputValidator createExistingNamesValidator(@NonNull final Set<String> existingNames) {
		final IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(final String newText) {

				if (existingNames.contains(newText)) {
					return "Name already exists";
				}
				return null;
			}
		};
		return validator;
	}

	public static boolean closeReferences(@NonNull final ScenarioInstance scenarioInstance) {

		ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, provider -> {

			// Close all open editors
			final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
			final IWorkbench workbench = PlatformUI.getWorkbench();
			for (IWorkbenchWindow window : workbench.getWorkbenchWindows()) {
				for (final IWorkbenchPage page : window.getPages()) {

					final IEditorReference[] editorReferences = page.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
					if (editorReferences != null && editorReferences.length > 0) {
						RunnerHelper.syncExec(() -> page.closeEditors(editorReferences, false));
					}
				}
			}

			// Deselect from views
			provider.deselect(scenarioInstance, true);
		});

		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post(ScenarioServiceUtils.EVENT_CLOSING_SCENARIO_INSTANCE, scenarioInstance);

		return true;
	}

	@Deprecated
	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioInstance scenario, @NonNull final Container destination, @NonNull final Set<String> existingNames,
			final IProgressMonitor monitor) throws Exception {
		return copyScenario(scenario, destination, scenario.getName(), existingNames, monitor);
	}

	// FIXME: Duplicates API in ScenarioServiceUtils
	@Deprecated
	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioInstance scenario, @NonNull final Container destination, final String currentName, @NonNull final Set<String> existingNames,
			final IProgressMonitor monitor) throws Exception {

		final boolean withinContainer = scenario.getParent() == destination;
		final String namePrefix = (withinContainer ? "Copy of " : "") + currentName;
		final String newName = ScenarioServiceUtils.getNextName(namePrefix, existingNames);

		final ScenarioModelRecord record = SSDataManager.Instance.getModelRecord(scenario);

		final IScenarioService destinationScenarioService = SSDataManager.Instance.findScenarioService(destination);

		return destinationScenarioService.copyInto(destination, record, newName, monitor);
	}

	public static ScenarioInstance fork(@NonNull final ScenarioInstance instance, final String finalNewName, final IProgressMonitor monitor) throws Exception {
		return copyScenario(instance, instance, finalNewName, Collections.emptySet(), monitor);
	}
}
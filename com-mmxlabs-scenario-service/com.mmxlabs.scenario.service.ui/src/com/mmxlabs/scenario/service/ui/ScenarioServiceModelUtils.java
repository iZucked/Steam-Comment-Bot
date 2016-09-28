/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceCommandUtil;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
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
			for (final IWorkbenchPage page : workbench.getActiveWorkbenchWindow().getPages()) {

				final IEditorReference[] editorReferences = page.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
				if (editorReferences != null && editorReferences.length > 0) {
					assert page.closeEditors(editorReferences, false);
				}
			}

			// Deselect from views
			provider.deselect(scenarioInstance, true);
		});

		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post(ScenarioServiceUtils.EVENT_CLOSING_SCENARIO_INSTANCE, scenarioInstance);

		return true;
	}

	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioInstance scenario, @NonNull final Container destination, @NonNull final Set<String> existingNames,
			final IProgressMonitor monitor) throws Exception {
		return copyScenario(scenario, destination, scenario.getName(), existingNames, monitor);
	}

	@Nullable
	public static ScenarioInstance copyScenario(@NonNull final ScenarioInstance scenario, @NonNull final Container destination, final String currentName, @NonNull final Set<String> existingNames,
			final IProgressMonitor monitor) throws Exception {

		final boolean withinContainer = scenario.getParent() == destination;
		final String namePrefix = (withinContainer ? "Copy of " : "") + currentName;
		final String newName = ScenarioServiceUtils.getNextName(namePrefix, existingNames);

		final ModelRecord record = SSDataManager.Instance.getModelRecord(scenario);

		// This check forces a slow code path to copy data by loading, migrating, closing and then the user reloads.
		// Previously we just copied the raw data and delayed load/migration until the user step
		//
		try (ModelReference ref = record.aquireReferenceIfLoaded("ScenarioServiceModelUtils#copyScenario")) {
			if (ref != null) {
				return ScenarioServiceCommandUtil.copyTo(scenario, destination, newName);
			} else {
				final IScenarioService sourceScenarioService = SSDataManager.Instance.findScenarioService(scenario);
				final URI sourceURI = sourceScenarioService == null ? URI.createURI(scenario.getRootObjectURI()) : sourceScenarioService.resolveURI(scenario.getRootObjectURI());

				final IScenarioService destinationScenarioService = SSDataManager.Instance.findScenarioService(destination);

				return destinationScenarioService.insert(destination, sourceURI, dup -> {
					dup.setName(newName);

					// Copy across various bits of information
					dup.getMetadata().setContentType(scenario.getMetadata().getContentType());
					dup.getMetadata().setCreated(scenario.getMetadata().getCreated());
					dup.getMetadata().setLastModified(new Date());

					// Copy version context information
					dup.setVersionContext(scenario.getVersionContext());
					dup.setScenarioVersion(scenario.getScenarioVersion());

					dup.setClientVersionContext(scenario.getClientVersionContext());
					dup.setClientScenarioVersion(scenario.getClientScenarioVersion());
				});
			}
		}
	}

	public static ScenarioInstance fork(@NonNull final ScenarioInstance instance, final String finalNewName, final IProgressMonitor monitor) throws Exception {
		return copyScenario(instance, instance, finalNewName, Collections.emptySet(), monitor);
	}
}
package com.mmxlabs.scenario.service.ui;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public final class ScenarioServiceModelUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServiceModelUtils.class);

	public static final String FORK_PREFIX = "~";
	public static final String OPTIMISED_PREFIX = "O~";

	@Nullable
	public static ScenarioInstance createAndOpenFork(@NonNull final ScenarioInstance instance, final boolean forkAndOptimise) throws IOException {
		final IScenarioService scenarioService = instance.getScenarioService();
		final String finalNewName = ScenarioServiceModelUtils.getNewForkName(instance, forkAndOptimise);
		if (finalNewName != null) {
			final ScenarioInstance fork = scenarioService.duplicate(instance, instance);
			fork.setName(finalNewName);

			try {
				OpenScenarioUtils.openScenarioInstance(fork);
				return fork;
			} catch (final PartInitException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@SuppressWarnings("null")
	@NonNull
	public static String getForkName(@NonNull final String currentName) {
		return String.format("%s%s", FORK_PREFIX, currentName);
	}

	@SuppressWarnings("null")
	@NonNull
	public static String getOptimisedName(@NonNull final String currentName) {
		return String.format("%s%s", OPTIMISED_PREFIX, currentName);
	}

	@Nullable
	public static String getNewForkName(@NonNull final ScenarioInstance instance, final boolean forkAndOptimise) {
		String oldName = instance.getName();
		if (oldName == null) {
			oldName = "(untitled)";
		}
		final String namePrefix = forkAndOptimise ? getOptimisedName(oldName) : getForkName(oldName);
		final Set<String> existingNames = getExistingNames(instance);
		final String newName = getNextName(instance, namePrefix, existingNames);
		return openNewNameForForkPrompt(oldName, newName, existingNames);
	}

	@NonNull
	public static String getNextName(@NonNull final Container parent, @NonNull final String currentName, @NonNull final Set<String> existingNames) {

		// Find a name that does not clash by appending a counter " (n)" if required
		String newName = currentName;
		int counter = 1;
		while (existingNames.contains(newName)) {
			newName = currentName + " (" + counter++ + ")";
		}
		return newName;
	}

	@NonNull
	public static Set<String> getExistingNames(@NonNull final Container parent) {
		final Set<String> existingNames = new HashSet<String>();
		for (final Container child : parent.getElements()) {
			if (child instanceof Folder) {
				existingNames.add(((Folder) child).getName());
			} else if (child instanceof ScenarioInstance) {
				existingNames.add(((ScenarioInstance) child).getName());
			}
		}
		return existingNames;
	}

	@Nullable
	public static String openNewNameForForkPrompt(@NonNull final String oldName, @NonNull final String suggestedName, @NonNull final Set<String> existingNames) {

		final IInputValidator validator = createExistingNamesValidator(existingNames);

		final Display display = PlatformUI.getWorkbench().getDisplay();
		final InputDialog dialog = new InputDialog(display.getActiveShell(), "Fork " + oldName, "Choose new name for fork", suggestedName, validator);

		if (dialog.open() == Window.OK) {
			return dialog.getValue();
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

}

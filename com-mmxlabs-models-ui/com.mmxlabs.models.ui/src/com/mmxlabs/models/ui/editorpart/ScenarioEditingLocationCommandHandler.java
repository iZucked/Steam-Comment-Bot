/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ScenarioEditingLocationCommandHandler implements ICommandHandler {

	private final IScenarioEditingLocation location;

	public ScenarioEditingLocationCommandHandler(final @NonNull IScenarioEditingLocation location) {
		this.location = location;
	}

	@Override
	public @NonNull IReferenceValueProviderProvider getReferenceValueProviderProvider() {
		return location.getReferenceValueProviderCache();
	}

	@Override
	public @NonNull EditingDomain getEditingDomain() {
		return location.getEditingDomain();
	}

	@Override
	public @NonNull ModelReference getModelReference() {
		return location.getModelReference();
	}

	@Override
	public void handleCommand(@NonNull Command command, @Nullable EObject target, @Nullable ETypedElement typedElement) {
		if (command.canExecute()) {
			location.getEditingDomain().getCommandStack().execute(command);
		} else {
			throw new RuntimeException("Command is not executable");
		}
	}

}

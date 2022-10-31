/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class DefaultCommandHandler implements ICommandHandler {

	private final EditingDomain editingDomain;
	private final ModelReference modelReference;
	private final IReferenceValueProviderProvider referenceValueProviderProvider;

	public DefaultCommandHandler(final ModelReference modelReference, final IReferenceValueProviderProvider referenceValueProviderProvider) {
		this(modelReference.getEditingDomain(), modelReference, referenceValueProviderProvider);
	}

	public DefaultCommandHandler(final EditingDomain editingDomain, final ModelReference modelReference, final IReferenceValueProviderProvider referenceValueProviderProvider) {
		this.editingDomain = editingDomain;
		this.modelReference = modelReference;
		this.referenceValueProviderProvider = referenceValueProviderProvider;
	}

	@Override
	public @NonNull IReferenceValueProviderProvider getReferenceValueProviderProvider() {
		return referenceValueProviderProvider;
	}

	@Override
	public @NonNull EditingDomain getEditingDomain() {
		return editingDomain;
	}

	@Override
	public @NonNull ModelReference getModelReference() {
		return modelReference;
	}

	@Override
	public void handleCommand(@NonNull Command command, @Nullable EObject target, @Nullable ETypedElement typedElement) {
		if (command.canExecute()) {
			editingDomain.getCommandStack().execute(command);
		} else {
			throw new RuntimeException("Command is not executable");
		}
	}
}

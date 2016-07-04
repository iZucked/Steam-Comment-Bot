/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.common.internal;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Proxy class to allow lazy loading of model command providers registered via extension points.
 * 
 * @author Simon Goodall
 * 
 */
class ModelCommandProviderExtensionProxy implements IModelCommandProvider {
	private final IModelCommandProviderExtension extension;

	private IModelCommandProvider provider;

	public ModelCommandProviderExtensionProxy(final IModelCommandProviderExtension extension) {
		this.extension = extension;
	}

	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (provider == null) {
			createCommandProvider();
		}

		return provider.provideAdditionalBeforeCommand(editingDomain, rootObject, overrides, editSet, commandClass, parameter, input);
	}

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (provider == null) {
			createCommandProvider();
		}

		return provider.provideAdditionalAfterCommand(editingDomain, rootObject, overrides, editSet, commandClass, parameter, input);
	}

	@Override
	public void startCommandProvision() {
		if (provider == null) {
			createCommandProvider();
		}
		provider.startCommandProvision();
	}

	@Override
	public void endCommandProvision() {
		if (provider == null) {
			createCommandProvider();
		}
		provider.endCommandProvision();
	}

	private synchronized void createCommandProvider() {
		if (provider == null) {
			provider = extension.getModelCommadProvider();
		}
	}
}

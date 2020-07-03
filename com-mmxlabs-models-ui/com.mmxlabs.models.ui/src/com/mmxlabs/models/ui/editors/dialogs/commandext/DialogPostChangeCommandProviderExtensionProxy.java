/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs.commandext;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogPostChangeCommandProvider;

/**
 * Proxy class to allow lazy loading of model command providers registered via extension points.
 * 
 * @author Simon Goodall
 * 
 */
public class DialogPostChangeCommandProviderExtensionProxy implements IDialogPostChangeCommandProvider {
	private final IDialogPostChangeCommandProviderExtension extension;

	private IDialogPostChangeCommandProvider provider;

	public DialogPostChangeCommandProviderExtensionProxy(final IDialogPostChangeCommandProviderExtension extension) {
		this.extension = extension;
	}

	@Override
	public Command provideExtraCommand(EditingDomain editingDomain, Command cmd, MMXRootObject rootObject, Collection<EObject> roots) {

		if (provider == null) {
			createCommandProvider();
		}

		return provider.provideExtraCommand(editingDomain, cmd, rootObject, roots);
	}

	private synchronized void createCommandProvider() {
		if (provider == null) {
			provider = extension.getCommandProvider();
		}
	}
}

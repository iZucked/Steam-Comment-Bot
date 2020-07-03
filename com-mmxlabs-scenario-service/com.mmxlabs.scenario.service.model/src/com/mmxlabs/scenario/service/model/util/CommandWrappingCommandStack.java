/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.util.extpoint.IWrappedCommandProvider;
import com.mmxlabs.scenario.service.model.util.extpoint.WrappedCommandProviderExtensionUtil;

/**
 * Extended version of {@link BasicCommandStack} which allows an extension point to wrap the command to execute.
 * 
 * @author Simon Goodall
 * 
 */
public class CommandWrappingCommandStack extends BasicCommandStack {

	private final Iterable<IWrappedCommandProvider> extensions;
	private EditingDomain editingDomain;

	public CommandWrappingCommandStack() {
		this(null, null);
	}

	public CommandWrappingCommandStack(Manifest manifest, final EditingDomain editignDomain) {
		super();
		editingDomain = editignDomain;
		extensions = WrappedCommandProviderExtensionUtil.getExtensions();
		if (editignDomain != null) {
			for (final IWrappedCommandProvider provider : extensions) {
				provider.registerEditingDomain(manifest, editignDomain);
			}
		}

	}

	@Override
	public void execute(final Command command) {

		Command wrappable = command;
		for (final IWrappedCommandProvider provider : extensions) {
			final Command next = provider.provideCommand(wrappable, getEditingDomain());
			if (next != null) {
				wrappable = next;
			}
		}
		super.execute(wrappable);
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(Manifest manifest, final EditingDomain editingDomain) {
		if (this.editingDomain != null) {
			for (final IWrappedCommandProvider provider : extensions) {
				provider.deregisterEditingDomain(manifest, this.editingDomain);
			}
		}
		this.editingDomain = editingDomain;
		if (this.editingDomain != null) {
			for (final IWrappedCommandProvider provider : extensions) {
				provider.registerEditingDomain(manifest, this.editingDomain);
			}
		}
	}
}
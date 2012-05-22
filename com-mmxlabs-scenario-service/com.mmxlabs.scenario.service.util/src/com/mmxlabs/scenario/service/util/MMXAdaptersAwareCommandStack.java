package com.mmxlabs.scenario.service.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;

import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Extended version of {@link BasicCommandStack} which overrides undo/redo to disable the {@link MMXAdapterImpl} instances while processing the commands.
 * 
 * @author Simon Goodall
 * 
 */
public final class MMXAdaptersAwareCommandStack extends BasicCommandStack {
	private CommandProviderAwareEditingDomain editingDomain;
	private final ScenarioInstance instance;

	public MMXAdaptersAwareCommandStack(final ScenarioInstance instance) {
		this.instance = instance;
	}

	public MMXAdaptersAwareCommandStack(final CommandProviderAwareEditingDomain editingDomain, final ScenarioInstance instance) {
		this.editingDomain = editingDomain;
		this.instance = instance;
	}

	@Override
	public void execute(final Command command) {
		synchronized (instance) {
			super.execute(command);
		}
	}

	@Override
	public void undo() {

		if (editingDomain != null) {

			editingDomain.setAdaptersEnabled(false);
			try {
				super.undo();
			} finally {
				editingDomain.setAdaptersEnabled(true);
			}
		} else {
			super.undo();
		}
	}

	@Override
	public void redo() {

		if (editingDomain != null) {

			editingDomain.setAdaptersEnabled(false);
			try {
				super.redo();
			} finally {
				editingDomain.setAdaptersEnabled(true);
			}
		} else {
			super.redo();
		}
	}

	public CommandProviderAwareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(final CommandProviderAwareEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}
}
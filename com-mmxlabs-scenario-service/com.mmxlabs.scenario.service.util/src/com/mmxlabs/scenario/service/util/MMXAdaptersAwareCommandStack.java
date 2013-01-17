/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * Extended version of {@link BasicCommandStack} which overrides undo/redo to disable the {@link MMXAdapterImpl} instances while processing the commands.
 * 
 * @author Simon Goodall
 * 
 */
public class MMXAdaptersAwareCommandStack extends BasicCommandStack {
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
		// Check command can execute, if not try and report something useful
		if (!command.canExecute()) {
			throwExceptionOnBadCommand(command);
		}
		synchronized (instance) {
			final boolean isEnabled = editingDomain.isEnabled();
			if (isEnabled) {
				editingDomain.setAdaptersEnabled(false);
			}
			try {
				super.execute(command);
			} finally {
				if (isEnabled) {
					editingDomain.setAdaptersEnabled(true);
				}
			}
		}
	}

	private void throwExceptionOnBadCommand(final Command command) {

		if (command instanceof CompoundCommand) {
			final CompoundCommand compoundCommand = (CompoundCommand) command;
			for (final Command cmd : compoundCommand.getCommandList()) {
				throwExceptionOnBadCommand(cmd);
			}
		} else {
			if (!command.canExecute()) {
				throw new RuntimeException("Unable to execute command: " + command);
			}
		}
	}

	@Override
	public void undo() {
		undo(ScenarioLock.EDITORS);
	}

	public void undo(final String lockKey) {
		final ScenarioLock lock = instance.getLock(lockKey);
		if (lock.awaitClaim()) {
			try {
				if (editingDomain != null) {
					final boolean isEnabled = editingDomain.isEnabled();
					if (isEnabled) {
						editingDomain.setAdaptersEnabled(false);
					}
					try {
						super.undo();
					} finally {
						if (isEnabled) {
							editingDomain.setAdaptersEnabled(true);
						}
					}
				} else {
					super.undo();
				}
			} finally {
				lock.release();
			}
		}
	}

	@Override
	public void redo() {
		final ScenarioLock lock = instance.getLock(ScenarioLock.EDITORS);
		if (lock.awaitClaim()) {
			try {
				if (editingDomain != null) {
					final boolean isEnabled = editingDomain.isEnabled();
					if (isEnabled) {
						editingDomain.setAdaptersEnabled(false);
					}
					try {
						super.redo();
					} finally {
						if (isEnabled) {
							editingDomain.setAdaptersEnabled(true);
						}
					}
				} else {
					super.redo();
				}
			} finally {
				lock.release();
			}
		}
	}

	public CommandProviderAwareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(final CommandProviderAwareEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}
}
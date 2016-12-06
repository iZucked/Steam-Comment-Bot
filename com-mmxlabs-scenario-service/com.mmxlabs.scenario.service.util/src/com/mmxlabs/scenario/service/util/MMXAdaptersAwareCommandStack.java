/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.util.concurrent.atomic.AtomicInteger;

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
	private final Object lockableObject;
	private final ScenarioLock lock;
	/**
	 * Used to determine when we are executing changes.
	 */
	private final AtomicInteger stackDepth = new AtomicInteger(0);

	public MMXAdaptersAwareCommandStack(final ScenarioInstance instance) {
		this(null, instance, instance.getLock(ScenarioLock.EDITORS));
	}

	public MMXAdaptersAwareCommandStack(final CommandProviderAwareEditingDomain editingDomain, final ScenarioInstance instance) {
		this(editingDomain, instance, instance.getLock(ScenarioLock.EDITORS));
	}

	public MMXAdaptersAwareCommandStack(final CommandProviderAwareEditingDomain editingDomain, final Object lockObject, ScenarioLock lock) {
		this.editingDomain = editingDomain;
		this.lockableObject = lockObject;
		this.lock = lock;
		// instance.eAdapters().add(externalChangeAdapter);
	}

	@Override
	public void execute(final Command command) {
		stackDepth.incrementAndGet();
		try {
			// Check command can execute, if not try and report something useful
			if (!command.canExecute()) {
				throwExceptionOnBadCommand(command);
			}
			synchronized (lockableObject) {
				final boolean isEnabled = editingDomain.isEnabled();
				if (isEnabled) {
					editingDomain.setAdaptersEnabled(false);
				}
				try {
					stackDepth.incrementAndGet();
					super.execute(command);
				} finally {
					if (isEnabled) {
						editingDomain.setAdaptersEnabled(true);
					}
				}
			}
		} finally {
			stackDepth.decrementAndGet();
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
		undo(true);
	}

	/**
	 * Perform undo aquiring the named lock. If the named lock is null, then perform undo with no locking at all - i.e. this is being called from within an existing lock.
	 * 
	 * @param lockKey
	 */
	public void undo(boolean useLock) {
		if (!useLock) {
			reallyUndo();
		} else {
			if (lock.awaitClaim()) {
				try {
					reallyUndo();
				} finally {
					lock.release();
				}
			}
		}
	}

	public void reallyUndo() {
		stackDepth.incrementAndGet();
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
			stackDepth.decrementAndGet();
		}
	}

	@Override
	public void redo() {
		if (lock.awaitClaim()) {
			stackDepth.incrementAndGet();
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
				stackDepth.decrementAndGet();
			}
		}
	}

	public CommandProviderAwareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(final CommandProviderAwareEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	@Override
	protected void handleError(Exception exception) {
		throw new RuntimeException(exception);
	}
}
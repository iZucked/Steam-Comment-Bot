/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.InstanceData;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

/**
 * Extended version of {@link BasicCommandStack} which overrides undo/redo to disable the {@link MMXAdapterImpl} instances while processing the commands.
 * 
 * @author Simon Goodall
 * 
 */
public class MMXAdaptersAwareCommandStack extends BasicCommandStack {
	private CommandProviderAwareEditingDomain editingDomain;
	private final Object lockableObject;
	private InstanceData instanceData;

	/**
	 * Used to determine when we are executing changes.
	 */
	private final AtomicInteger stackDepth = new AtomicInteger(0);
	// Used only for Read-only checks. Do not rely on it
	private @Nullable ScenarioInstance instance;

	public MMXAdaptersAwareCommandStack(final ScenarioInstance instance) {
		this(null, (Object) instance);
		this.instance = instance;
	}

	public MMXAdaptersAwareCommandStack(final CommandProviderAwareEditingDomain editingDomain, final ScenarioInstance instance) {
		this(editingDomain, (Object) instance);
		this.instance = instance;
	}

	public MMXAdaptersAwareCommandStack(final CommandProviderAwareEditingDomain editingDomain, final Object lockObject) {
		this.editingDomain = editingDomain;
		this.lockableObject = lockObject;
	}

	@Override
	public void execute(final Command command) {
	// All commands should be executed on display thread. Wrap up in
		// RunnerHelper.syncExecDisplayOptional(() -> { execute(); });
		assert RunnerHelper.inDisplayThread();
		
		if (instance != null) {
			assert !instance.isReadonly();
		}

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

		if (instance != null) {
			assert !instance.isReadonly();
		}

		if (!useLock) {
			reallyUndo();
		} else {
			final ScenarioLock lock = instanceData.getLock();
			lock.lock();
			try {
				reallyUndo();
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * Perform undo without aquiring the lock.
	 *
	 * @param lockKey
	 */
	public void undoWithoutLock() {
		reallyUndo();
	}

	private void reallyUndo() {
		// All commands should be executed on display thread. Wrap up in
		// RunnerHelper.syncExecDisplayOptional(() -> { execute(); });
		assert RunnerHelper.inDisplayThread();
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

		if (instance != null) {
			assert !instance.isReadonly();
		}

		// All commands should be executed on display thread. Wrap up in
		// RunnerHelper.syncExecDisplayOptional(() -> { execute(); });
		assert RunnerHelper.inDisplayThread();
		final ScenarioLock lock = instanceData.getLock();
		lock.lock();
		try {
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
				stackDepth.decrementAndGet();
			}
		} finally {
			lock.unlock();
		}
	}

	public CommandProviderAwareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(final CommandProviderAwareEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	public InstanceData getInstanceData() {
		return instanceData;
	}

	public void setInstanceData(InstanceData instanceData) {
		this.instanceData = instanceData;
	}

	@Override
	protected void handleError(Exception exception) {
		throw new RuntimeException(exception);
	}
}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.common.commandservice.CancelledCommand;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.manager.InstanceData;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

/**
 * Extended version of {@link BasicCommandStack} which overrides undo/redo to
 * disable the {@link MMXAdapterImpl} instances while processing the commands.
 * 
 * @author Simon Goodall
 * 
 */
public class MMXAdaptersAwareCommandStack extends CommandWrappingCommandStack {
	private CommandProviderAwareEditingDomain editingDomain;
	private final Object lockableObject = new Object();
	private InstanceData instanceData;

	/**
	 * Used to determine when we are executing changes.
	 */
	private final AtomicInteger stackDepth = new AtomicInteger(0);
	// Used only for Read-only checks. Do not rely on it
	private @Nullable IReadOnlyProvider readOnlyProvider;

	public MMXAdaptersAwareCommandStack() {
		this(null, null, null);
	}

	public MMXAdaptersAwareCommandStack(Manifest manifest, final CommandProviderAwareEditingDomain editingDomain, @Nullable IReadOnlyProvider readOnlyProvider) {
		super(manifest, editingDomain);
		this.editingDomain = editingDomain;
		this.readOnlyProvider = readOnlyProvider;
	}

	@Override
	public void execute(final Command command) {
		// All commands should be executed on display thread. Wrap up in
		// RunnerHelper.syncExecDisplayOptional(() -> { execute(); });
		assert RunnerHelper.inDisplayThread() : "All commands should be executed on display thread";

		if (readOnlyProvider != null) {
			assert !readOnlyProvider.isReadonly();
		}

		stackDepth.incrementAndGet();
		try {
			// Check command can execute, if not try and report something useful
			if (!command.canExecute()) {
				throwExceptionOnBadCommand(command);
			}
			synchronized (lockableObject) {
				editingDomain.withAdaptersDisabled(() -> {
					stackDepth.incrementAndGet();
					super.execute(command);
				});
			}
		} finally {
			stackDepth.decrementAndGet();
		}
	}

	private void throwExceptionOnBadCommand(final Command command) {

		if (command instanceof CompoundCommand compoundCommand) {
			for (final Command cmd : compoundCommand.getCommandList()) {
				throwExceptionOnBadCommand(cmd);
			}
		} else {
			if (!command.canExecute() && !(command instanceof CancelledCommand)) {
				throw new RuntimeException("Unable to execute command: " + command);
			}
		}
	}

	@Override
	public void undo() {
		undo(true);
	}

	/**
	 * Perform undo aquiring the named lock. If the named lock is null, then perform
	 * undo with no locking at all - i.e. this is being called from within an
	 * existing lock.
	 * 
	 * @param lockKey
	 */
	public void undo(boolean useLock) {

		if (readOnlyProvider != null) {
			assert !readOnlyProvider.isReadonly();
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
				editingDomain.withAdaptersDisabled(super::undo);
			} else {
				super.undo();
			}
		} finally {
			stackDepth.decrementAndGet();
		}
	}

	@Override
	public void redo() {

		if (readOnlyProvider != null) {
			assert !readOnlyProvider.isReadonly();
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
					editingDomain.withAdaptersDisabled(super::redo);
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

	public void setEditingDomain(Manifest manifest, final CommandProviderAwareEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
		super.setEditingDomain(manifest, editingDomain);
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
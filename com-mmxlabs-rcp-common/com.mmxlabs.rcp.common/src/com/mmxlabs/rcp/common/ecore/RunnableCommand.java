/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.ecore;

import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.annotation.NonNull;

/**
 * Sub-class of {@link CompoundCommand} taking a {@link Consumer} (taking the {@link RunnableCommand} instance) to implement the {@link #execute()} method.After execution the consumer reference is
 * nulled allowing the GC to clean up.
 * 
 * @author Simon Goodall
 *
 */
public class RunnableCommand extends CompoundCommand {

	private Consumer<RunnableCommand> runnable;

	public RunnableCommand(final String label, final @NonNull Consumer<RunnableCommand> runnable) {
		super(label);
		this.runnable = runnable;
	}

	public RunnableCommand(final @NonNull Consumer<RunnableCommand> runnable) {
		this.runnable = runnable;
	}

	@Override
	protected boolean prepare() {
		// Always prepared
		return true;
	}

	@Override
	public boolean canExecute() {
		// Always true
		return true;
	}

	@Override
	public void execute() {

		final Consumer<RunnableCommand> pRunnable = runnable;
		if (pRunnable == null) {
			throw new RuntimeException("Runnable is null. Already executed?");
		}
		try {
			pRunnable.accept(this);
		} finally {
			runnable = null;
		}
	}
}

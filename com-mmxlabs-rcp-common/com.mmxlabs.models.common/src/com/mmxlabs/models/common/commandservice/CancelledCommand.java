/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.common.commandservice;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;

/**
 * Similar to {@link UnexecutableCommand} except that it indicates a cancelation rather than error.
 */
public class CancelledCommand extends AbstractCommand {
	public static final CancelledCommand INSTANCE = new CancelledCommand();

	private CancelledCommand() {
	}

	@Override
	public boolean canExecute() {
		return false;
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public void execute() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void undo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void redo() {
		throw new UnsupportedOperationException();
	}
}

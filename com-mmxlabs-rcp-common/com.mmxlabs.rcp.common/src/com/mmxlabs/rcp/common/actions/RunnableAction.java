/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;

public final class RunnableAction extends Action {

	private final Runnable runnable;

	public RunnableAction(final String label, final Runnable runnable) {
		super(label);
		this.runnable = runnable;
	}

	public RunnableAction(final String label, int style, final Runnable runnable) {
		super(label, style);
		this.runnable = runnable;
	}

	@Override
	public void run() {
		runnable.run();
	}
}

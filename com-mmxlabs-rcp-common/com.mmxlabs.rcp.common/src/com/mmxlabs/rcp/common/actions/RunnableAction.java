/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public final class RunnableAction extends Action {

	private final Runnable runnable;

	public RunnableAction(final String label, final Runnable runnable) {
		super(label);
		this.runnable = runnable;
	}

	public RunnableAction(final String label, final int style, final Runnable runnable) {
		super(label, style);
		this.runnable = runnable;
	}

	public RunnableAction(final String label, final ImageDescriptor imgDesc, final Runnable runnable) {
		super(label);
		this.runnable = runnable;
		setImageDescriptor(imgDesc);
	}

	public RunnableAction(final String label, final ImageDescriptor imgDesc, final int style, final Runnable runnable) {
		super(label, style);
		this.runnable = runnable;
		setImageDescriptor(imgDesc);
	}

	@Override
	public void run() {
		runnable.run();
	}
}

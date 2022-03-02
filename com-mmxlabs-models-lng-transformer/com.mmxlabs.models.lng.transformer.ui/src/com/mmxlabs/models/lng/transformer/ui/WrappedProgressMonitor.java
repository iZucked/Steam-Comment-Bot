/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.optimiser.core.ISimpleProgressMonitor;

public final class WrappedProgressMonitor implements ISimpleProgressMonitor {
	private final IProgressMonitor monitor;

	public WrappedProgressMonitor(final IProgressMonitor monitor) {
		this.monitor = monitor;

	}

	@Override
	public void beginTask(final String name, final int totalWork) {
		monitor.beginTask(name, totalWork);

	}

	@Override
	public void done() {
		monitor.done();
	}

	@Override
	public boolean isCanceled() {
		return monitor.isCanceled();
	}

	@Override
	public void worked(final int work) {
		monitor.worked(1);
	}

}

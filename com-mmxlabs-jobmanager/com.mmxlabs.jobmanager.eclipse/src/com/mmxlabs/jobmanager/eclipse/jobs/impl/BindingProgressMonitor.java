/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.jobs.impl;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Progress monitor used to bind progress monitors together. When running an
 * optimisation we report progress back up to the Eclipse Job for the eclipse
 * framework. We also want to report progress to e.g. the navigator view.
 * 
 * This implementation takes the multiple progress monitors we want to report
 * back to in the constructor and updates them periodically. This will always
 * set 100 work units and will update progress at the minimum of 1 whole work
 * unit to avoid "progress spam".
 * 
 * @author Simon Goodall
 *
 */

public class BindingProgressMonitor implements IProgressMonitor {

	private double dtotal = 0.0;
	private double lastReport = 0.0;
	private int totalWork;
	private boolean canceled = false;

	private final IProgressMonitor delegate;
	private final AbstractEclipseJobControl control;

	public BindingProgressMonitor(final AbstractEclipseJobControl control, final IProgressMonitor delegate) {
		this.control = control;
		this.delegate = delegate;
	}

	@Override
	public synchronized void worked(final int work) {

		final double w = (double) work / (double) totalWork;
		internalWorked(w);
	}

	@Override
	public void subTask(final String name) {

	}

	@Override
	public void setTaskName(final String name) {

	}

	@Override
	public void setCanceled(final boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public synchronized void internalWorked(final double work) {
		dtotal += work;

		final int increment = (int) ((dtotal - lastReport) * 100.0);
		if (increment > 0) {
			delegate.worked(increment);
			control.setProgress(100.0 * dtotal);
			lastReport = dtotal;

			// System.out.printf("Progress:%.2f%n", 100.0 * dtotal);

		}
	}

	@Override
	public void done() {
		delegate.done();
	}

	@Override
	public void beginTask(final String name, final int totalWork) {
		if (totalWork != 100) {
//			throw new IllegalArgumentException("Only supports 100 work units");
		}
		this.totalWork = totalWork;
		delegate.beginTask(name, 100);
	}
}

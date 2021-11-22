/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import org.eclipse.core.runtime.IProgressMonitor;

public class ConsoleProgressMonitor implements IProgressMonitor {

	private double dtotal = 0.0;
	private int totalWork;
	private boolean canceled = false;

	@Override
	public void worked(int work) {

		double w = (double) work / (double) totalWork;
		internalWorked(w);
	}

	@Override
	public void subTask(String name) {

	}

	@Override
	public void setTaskName(String name) {

	}

	@Override
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void internalWorked(double work) {
		dtotal += work;
		System.out.printf("Progress:%.2f%n", 100.0 * dtotal);

	}

	@Override
	public void done() {

	}

	@Override
	public void beginTask(String name, int totalWork) {
		this.totalWork = totalWork;
	}
}

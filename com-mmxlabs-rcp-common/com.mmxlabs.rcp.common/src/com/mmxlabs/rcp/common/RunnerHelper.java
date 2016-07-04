/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * Helper class to execute {@link Runnable}s in the UI thread.
 * 
 * @author Simon Goodall
 *
 */
public final class RunnerHelper {

	/**
	 * Returns the {@link Display} for the {@link IWorkbench}, or null if not yet created or disposed
	 * 
	 * @return
	 */
	@Nullable
	public static Display getWorkbenchDisplay() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return null;
		}
		final Display display = workbench.getDisplay();
		if (display == null) {
			return null;
		}
		if (display.isDisposed()) {
			return null;
		}
		return display;
	}

	/**
	 * Run the task in a blocking manner.
	 * 
	 * @param runnable
	 * @return Returns false if we were unable to find a {@link Display} thread to execute the task on.
	 */
	public static boolean syncExec(@NonNull final Runnable runnable) {

		final Display display = getWorkbenchDisplay();
		if (display == null) {
			return false;
		}

		if (display.getThread() == Thread.currentThread()) {
			runnable.run();
		} else {
			display.syncExec(runnable);
		}
		return true;
	}

	/**
	 * Run the task asynchronously.
	 * 
	 * @return Returns false if we were unable to find a {@link Display} thread to execute the task on.
	 */
	public static boolean asyncExec(@NonNull final Runnable runnable) {

		final Display display = getWorkbenchDisplay();
		if (display == null) {
			return false;
		}
		display.asyncExec(runnable);
		return true;
	}

	public static boolean exec(@NonNull final Runnable runnable, final boolean syncExec) {
		if (syncExec) {
			return syncExec(runnable);
		} else {
			return asyncExec(runnable);
		}
	}
}

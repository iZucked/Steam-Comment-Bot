/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.function.Consumer;

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

		if (!PlatformUI.isWorkbenchRunning()) {
			return null;
		}

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
	 * Run the task in a blocking manner.
	 * 
	 * @param runnable
	 * @return Returns false if we were unable to find a {@link Display} thread to execute the task on.
	 */
	public static boolean syncExecDisplayOptional(@NonNull final Runnable runnable) {

		final Display display = getWorkbenchDisplay();
		if (display == null) {
			runnable.run();
			return true;
		}

		if (display.getThread() == Thread.currentThread()) {
			runnable.run();
		} else {
			display.syncExec(runnable);
		}
		return true;
	}

	public static boolean inDisplayThread() {

		final Display display = getWorkbenchDisplay();
		if (display == null) {
			return true;
		}

		if (display.getThread() == Thread.currentThread()) {
			return true;
		}
		return false;
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

	/**
	 * Run the task asynchronously.
	 * 
	 * @return Returns false if we were unable to find a {@link Display} thread to execute the task on.
	 */
	public static boolean asyncExec(@NonNull final Consumer<@NonNull Display> consumer) {

		final Display display = getWorkbenchDisplay();
		if (display == null) {
			return false;
		}
		display.asyncExec(() -> consumer.accept(display));
		return true;
	}
	
	public static boolean syncExec(@NonNull final Consumer<@NonNull Display> consumer) {
		
		final Display display = getWorkbenchDisplay();
		if (display == null) {
			return false;
		}
		display.syncExec(() -> consumer.accept(display));
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

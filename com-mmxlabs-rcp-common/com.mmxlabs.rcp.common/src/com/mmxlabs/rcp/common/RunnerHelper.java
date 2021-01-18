/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Control;
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
	public static <R> R syncExecFunc(@NonNull final Function<Display, R> runnable) {

		final Display display = getWorkbenchDisplay();
		if (display == null) {
			return null;
		}

		if (display.getThread() == Thread.currentThread()) {
			return runnable.apply(display);
		} else {
			Object[] r = new Object[1];
			display.syncExec(() -> r[0] = runnable.apply(display));
			return (R) r[0];
		}
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

	/**
	 * Run now if already in the display thread, otherwise run async
	 * 
	 * @param runnable
	 * @param syncExec
	 * @return
	 */
	public static boolean runNowOrAsync(@NonNull final Runnable runnable) {
		final Display display = getWorkbenchDisplay();
		if (display == null) {
			return false;
		}

		if (display.getThread() == Thread.currentThread()) {
			runnable.run();
		} else {
			display.asyncExec(runnable);
		}
		return true;
	}

	public static <T extends Control> void runAsyncIfControlValid(@Nullable final T control, @NonNull final Consumer<T> runnable) {
		runIfControlValid(control, false, runnable);
	}

	public static <T extends Control> void runSyncIfControlValid(@Nullable final T control, @NonNull final Consumer<T> runnable) {
		runIfControlValid(control, true, runnable);
	}

	public static <T extends Control> void runIfControlValid(@Nullable final T control, final boolean syncExec, @NonNull final Consumer<T> runnable) {
		if (control == null) {
			return;
		}
		final Runnable runnable2 = () -> {
			if (control != null && !control.isDisposed()) {
				runnable.accept(control);
			}
		};
		RunnerHelper.exec(runnable2, syncExec);
	}
}

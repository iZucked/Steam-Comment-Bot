/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

/**
 * Helper class for standard {@link Viewer} methods that require the UI thread
 * and various null or disposed checks.
 * 
 * @author Simon Goodall
 *
 */
public final class ViewerHelper {

	/**
	 * Functional interface to use with #setInput to wrap up input creation in a
	 * lambda
	 *
	 */
	@FunctionalInterface
	public interface InputProvider {

		@Nullable
		Object getInput();
	}

	public static <T extends Viewer> void runIfViewerValid(@Nullable final T viewer, final boolean syncExec, @NonNull final Runnable runnable) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable2 = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				runnable.run();
			}
		};
		RunnerHelper.exec(runnable2, syncExec);
	}

	public static <T extends Viewer> void runIfViewerValid(@Nullable final T viewer, final boolean syncExec, @NonNull final Consumer<T> consumer) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				consumer.accept(viewer);
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void setInput(@Nullable final Viewer viewer, final boolean syncExec, @NonNull final InputProvider inputProvider) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.setInput(inputProvider.getInput());
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void setInput(@Nullable final Viewer viewer, final boolean syncExec, @Nullable final Object input) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.setInput(input);
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void setInputThen(@Nullable final Viewer viewer, final boolean syncExec, @Nullable final Object input, @NonNull Runnable then) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.setInput(input);
				then.run();
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void refresh(@Nullable final Viewer viewer, final boolean syncExec) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.refresh();
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void refresh(@Nullable final StructuredViewer viewer, Object element, final boolean syncExec) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.refresh(element);
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	/**
	 * If the viewer has been created and has not been disposed, call a refresh then
	 * execute the supplied runnable
	 * 
	 * @param viewer
	 * @param syncExec
	 * @param then
	 */
	public static void refreshThen(@Nullable final Viewer viewer, final boolean syncExec, @NonNull Runnable then) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.refresh();
				then.run();
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static <T extends Viewer> void refreshThenConsumer(@Nullable final T viewer, final boolean syncExec, @NonNull Consumer<T> then) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.refresh();
				then.accept(viewer);
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void setFocus(@Nullable final Viewer viewer) {
		if (viewer == null) {
			return;
		}
		final Control control = viewer.getControl();
		if (control != null && !control.isDisposed()) {
			control.setFocus();
		}
	}

	public static void setFocus(@Nullable final Control control) {
		if (control != null && !control.isDisposed()) {
			control.setFocus();
		}
	}

	public static void setSelection(@Nullable final Viewer viewer, final boolean syncExec, @Nullable final ISelection selection) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.setSelection(selection);
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void setSelection(@Nullable final Viewer viewer, final boolean syncExec, @Nullable final ISelection selection, final boolean reveal) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			final Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.setSelection(selection, reveal);
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

}

package com.mmxlabs.rcp.common;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

/**
 * Helper class for standard {@link Viewer} methods that require the UI thread and various null or disposed checks.
 * 
 * @author Simon Goodall
 *
 */
public final class ViewerHelper {

	public static void setInput(@Nullable final Viewer viewer, final boolean syncExec, @Nullable final Object input) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final Control control = viewer.getControl();
				if (control != null && !control.isDisposed()) {
					viewer.setInput(input);
				}
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void refresh(@Nullable final Viewer viewer, final boolean syncExec) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final Control control = viewer.getControl();
				if (control != null && !control.isDisposed()) {
					viewer.refresh();
				}
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

	public static void setSelection(@Nullable final Viewer viewer, final boolean syncExec, @Nullable final ISelection selection) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final Control control = viewer.getControl();
				if (control != null && !control.isDisposed()) {
					viewer.setSelection(selection);
				}
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

	public static void setSelection(@Nullable final Viewer viewer, final boolean syncExec, @Nullable final ISelection selection, final boolean reveal) {
		if (viewer == null) {
			return;
		}
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final Control control = viewer.getControl();
				if (control != null && !control.isDisposed()) {
					viewer.setSelection(selection, reveal);
				}
			}
		};
		RunnerHelper.exec(runnable, syncExec);
	}

}

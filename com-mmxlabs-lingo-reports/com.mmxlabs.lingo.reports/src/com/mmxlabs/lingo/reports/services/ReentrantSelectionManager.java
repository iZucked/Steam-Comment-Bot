/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * A helper class to avoid views receiving a selection or scenario change from re-triggering a new selection change event, causing multiple refreshes (on inconsistent data) for an otherwise single
 * event. This is not 100% foolproof as async refreshes can still occur and skip the locking behaviour used here.
 * 
 * @author Simon Goodall
 *
 */
public class ReentrantSelectionManager implements ISelectionListener, ISelectedScenariosServiceListener {

	/**
	 * The "lock" object to ensure a single action happens at once.
	 */
	private AtomicBoolean inSelectionChanged = new AtomicBoolean(false);

	private Collection<ISelectionListener> listeners = new ConcurrentLinkedQueue<>();

	private ISelectedScenariosServiceListener selectedScenariosServiceListener;

	private Viewer viewer;

	public ReentrantSelectionManager(Viewer viewer, ISelectedScenariosServiceListener selectedScenariosServiceListener, ScenarioComparisonService scs) {
		this(viewer, selectedScenariosServiceListener, scs, true);

	}

	/**
	 * 
	 * @param viewer
	 * @param l2
	 * @param scs
	 * @param react
	 *            True if selection change events from the viewer should be sent to the selection service. E.g. is this like the old selection provider?
	 */
	public ReentrantSelectionManager(Viewer viewer, ISelectedScenariosServiceListener selectedScenariosServiceListener, ScenarioComparisonService scs, boolean react) {

		this.viewer = viewer;
		this.selectedScenariosServiceListener = selectedScenariosServiceListener;

		if (react) {
			viewer.addSelectionChangedListener(l -> {
				if (inSelectionChanged.compareAndSet(false, true)) {
					try {
						scs.setSelection(l.getSelection());
					} finally {
						inSelectionChanged.set(false);
					}
				}
			});
		}
		viewer.getControl().addDisposeListener(l -> {
			if (react) {
				scs.removeListener(this);
			}
			listeners.clear();
		});

		scs.addListener(this);
	}

	/**
	 * Update the viewer selection, but only if there is no other selection update occuring 
	 * @param selection
	 * @param syncExec
	 */
	public void setViewerSelection(ISelection selection, boolean syncExec) {

		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			if (inSelectionChanged.compareAndSet(false, true)) {
				try {

					final Control control = viewer.getControl();
					if (control != null && !control.isDisposed()) {
						viewer.setSelection(selection);
					}
				} finally {
					inSelectionChanged.set(false);
				}
			}
		};
		RunnerHelper.exec(runnable, syncExec);

	}

	public void setSelection(ISelection selection, boolean syncExec, boolean reveal) {

		if (viewer == null) {
			return;
		}
		final Runnable runnable = () -> {
			if (inSelectionChanged.compareAndSet(false, true)) {
				try {

					final Control control = viewer.getControl();
					if (control != null && !control.isDisposed()) {
						viewer.setSelection(selection, reveal);
					}
				} finally {
					inSelectionChanged.set(false);
				}
			}
		};
		RunnerHelper.exec(runnable, syncExec);

	}

	public void addListener(ISelectionListener l) {
		listeners.add(l);
	}

	//
	@Override
	public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {
		if (inSelectionChanged.compareAndSet(false, true)) {
			try {
				selectedScenariosServiceListener.selectedDataProviderChanged(selectedDataProvider, block);
			} finally {
				inSelectionChanged.set(false);
			}
		}
	}

	@Override
	public void diffOptionChanged(@NonNull EDiffOption option, @NonNull Object oldValue, @NonNull Object newValue) {
		if (inSelectionChanged.compareAndSet(false, true)) {
			try {
				selectedScenariosServiceListener.diffOptionChanged(option, oldValue, newValue);
			} finally {
				inSelectionChanged.set(false);
			}
		}
	}

	@Override
	public void selectedObjectChanged(@Nullable MPart source, @NonNull ISelection selection) {
		if (inSelectionChanged.compareAndSet(false, true)) {
			try {
				selectedScenariosServiceListener.selectedObjectChanged(source, selection);
			} finally {
				inSelectionChanged.set(false);
			}
		}
	}

	@Override
	public void selectionChanged(MPart part, Object selection) {
		if (inSelectionChanged.compareAndSet(false, true)) {
			try {
				listeners.forEach(l -> l.selectionChanged(part, selection));
			} finally {
				inSelectionChanged.set(false);
			}
		}
	}

	/**
	 * Executes the given {@link Runnable} if we are not already within a selection change
	 * 
	 * @param runnable
	 */
	public void runIfPossible(Runnable runnable) {

		if (inSelectionChanged.compareAndSet(false, true)) {
			try {
				runnable.run();
			} finally {
				inSelectionChanged.set(false);
			}
		}
	}

}

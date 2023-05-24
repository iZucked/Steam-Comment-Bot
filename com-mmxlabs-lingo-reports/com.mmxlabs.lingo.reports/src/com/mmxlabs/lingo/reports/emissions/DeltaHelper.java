/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.SWT;

public class DeltaHelper {
	private GridTableViewer viewer;
	private boolean deltaMode;
	private boolean processDeltas;
	
	private ViewerComparator defaultViewerComparator = null;
	
	public void setViewer(final GridTableViewer viewer) {
		this.viewer = viewer;
		this.defaultViewerComparator = viewer.getComparator();
		this.viewer.addFilter(createFilter());
	}
	
	public void setProcessDeltas(final boolean processDeltas) {
		this.processDeltas = processDeltas;
		if (viewer!= null) {
			if (processDeltas) {
				viewer.setComparator(getGroupDeltaComparator());
			} else {
				viewer.setComparator(defaultViewerComparator);
			}
		}
	}
	
	public Action createDeltaAction() {
		Action deltaAction = new Action("Δ", SWT.TOGGLE) {
			@Override
			public void run() {
				DeltaHelper.this.deltaMode = !DeltaHelper.this.deltaMode;
				viewer.refresh();
			}
		};
		deltaAction.setToolTipText("Show Δ rows only");
		return deltaAction;
	}
	
	private ViewerFilter createFilter() {
		return new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (!deltaMode) {
					return true;
				}
				if (element instanceof final IEmissionReportIDData ive) {
					return processDeltas && ("Total Δ".equals(ive.getScenarioName()) || "Δ".equals(ive.getScenarioName()));
				}
				return false;
			}
		};
	}
	
	public ViewerComparator getGroupDeltaComparator() {

		if (defaultViewerComparator == null) {
			defaultViewerComparator = viewer.getComparator();
		}

		final ViewerComparator vc = defaultViewerComparator;

		// Wrap around with group sorter
		return new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof final IEmissionReportIDData g1 && e2 instanceof final IEmissionReportIDData g2) {
					if (g1.getGroup() == Integer.MIN_VALUE) {
						return -1;
					}
					if (g2.getGroup() == Integer.MIN_VALUE) {
						return 1;
					}
					if (g1.getGroup() != g2.getGroup() && !deltaMode) {
						return Integer.compare(g1.getGroup(), g2.getGroup());
					}
				}

				return vc.compare(viewer, e1, e2);
			}
		};
	}
}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;

public class TradesRowGroupComparator extends ViewerComparator {
	private final ViewerComparator superViewerComparator;
	public TradesRowGroupComparator(ViewerComparator superViewerComparator) {
		super();
		this.superViewerComparator = superViewerComparator;
	}
	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {
		TradesRowGroup g1 = null;
		TradesRowGroup g2 = null;
		if (e1 instanceof final TradesRow tradesRow && tradesRow.getGroup() instanceof TradesRowGroup group) {
			g1 = group;
		}
		if (e2 instanceof final TradesRow tradesRow && tradesRow.getGroup() instanceof TradesRowGroup group) {
			g2 = group;
		}
		if (g1 == g2) {
			return superViewerComparator.compare(viewer, e1, e2);
		} else {
			final Object rd1 = (g1 == null || g1.getRows().isEmpty()) ? e1 : g1.getRows().get(0);
			final Object rd2 = (g2 == null || g2.getRows().isEmpty()) ? e2 : g2.getRows().get(0);
			return superViewerComparator.compare(viewer, rd1, rd2);
		}
	}

}

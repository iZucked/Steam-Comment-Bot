/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.cargodiff;

import javax.inject.Inject;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewColumnHelper;
import com.mmxlabs.lingo.reports.views.changeset.IPinDiffResultPlanTransformer;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;

public class CargoDiffView extends ChangeSetView {

	@Inject
	public CargoDiffView() {
		super();
	}

	@Override
	protected String getChangeSetPartName() {
		return "CargoDiff";
	}

	@Override
	protected IPinDiffResultPlanTransformer getPinDiffResultPlanTransformer() {
		return new ScenarioDiffResultPlanTransformer();
	}

	@Override
	protected ViewerFilter[] createViewerFilters() {
		final ViewerFilter[] filters = new ViewerFilter[1];
		filters[0] = new ViewerFilter() {
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				return !(element instanceof ChangeSetTableRow row && !row.isWiringOrVesselChange());
			}
		};
		return filters;
	}

	@Override
	protected ChangeSetViewColumnHelper createColumnHelper() {
		return new CargoDiffViewColumnHelper(this, viewer);
	}

	@Override
	public void makeActions() {
		getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("start"));
		makeAndAddCopyAction();
		makeAndAddPackAction();
		getViewSite().getActionBars().getToolBarManager().update(true);
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.cargodiff;

import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewColumnHelper;
import com.mmxlabs.lingo.reports.views.changeset.InsertionPlanGrouperAndFilter;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;

public class CargoDiffViewColumnHelper extends ChangeSetViewColumnHelper {

	public CargoDiffViewColumnHelper(ChangeSetView view, GridTreeViewer viewer) {
		super(view, viewer);
	}

	@Override
	protected void createNominationBreaksColumn() {
	}

	@Override
	public void showCompareColumns(final boolean showCompareColumns) {
		this.showCompareColumns = showCompareColumns;
		RunnerHelper.asyncExec(() -> {
		});
	}

	@Override
	public void packMainColumns() {
		column_SetName.getColumn().pack();
	}

	@Override
	public void makeColumns(final InsertionPlanGrouperAndFilter insertionPlanFilter) {
		// Create columns
		final GridColumnGroup pnlComponentGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER | SWT.TOGGLE);
		pnlComponentGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());

		{
			column_SetName = new GridViewerColumn(viewer, SWT.CENTER);
			column_SetName.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			column_SetName.getColumn().setText("Buy");
			column_SetName.getColumn().setWidth(75);
			column_SetName.setLabelProvider(createIDLabelProvider(true));
			column_SetName.getColumn().setCellRenderer(createCellRenderer(SlotIDRenderMode.LHS_IN_TARGET, false));
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_NOMINATIONS)) {
			createNominationBreaksColumn();
		}

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringLabelProvider());
			this.diagram = createWiringDiagram(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Sell");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createIDLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer(SlotIDRenderMode.RHS_IN_TARGET, false));
		}

		vesselColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);// | SWT.TOGGLE);
		vesselColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());

		createCenteringGroupRenderer(vesselColumnGroup);
		vesselColumnGroup.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(final TreeEvent e) {

				vesselColumnGroup.setText("Vessels");

			}

			@Override
			public void treeCollapsed(final TreeEvent e) {
				// TODO Auto-generated method stub
				vesselColumnGroup.setText("");

			}
		});
		// Vessel columns are dynamically created - create a stub column to lock down
		// the position in the table
		{
			final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setVisible(false);
			gvc.getColumn().setWidth(0);
			gvc.setLabelProvider(createStubLabelProvider());
			vesselColumnStub = gvc;
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

	}

}

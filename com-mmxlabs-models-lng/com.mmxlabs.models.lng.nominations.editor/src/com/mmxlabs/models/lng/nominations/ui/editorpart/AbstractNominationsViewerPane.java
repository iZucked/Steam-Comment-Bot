/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.action.Action;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;

public abstract class AbstractNominationsViewerPane extends ScenarioTableViewerPane {	
	public AbstractNominationsViewerPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location, IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final ETypedElement... path) {
		final GridViewerColumn gvColumn = super.addTypicalColumn(columnName, manipulatorAndRenderer, path);
		final GridColumn gColumn = gvColumn.getColumn();
	    if (gColumn != null) {
	    	gColumn.setAlignment(SWT.LEFT);
	    }
		return gvColumn;
	}
	
	@Override
	public void refresh() {
		
	}
	
	@Override
	protected Action createCopyToClipboardAction() {
		CopyGridToHtmlClipboardAction action = CopyToClipboardActionFactory.createCopyToHtmlClipboardAction(scenarioViewer, false);
		action.setShowForegroundColours(true);
		action.setShowBackgroundColours(true);
		return action;
	}
}

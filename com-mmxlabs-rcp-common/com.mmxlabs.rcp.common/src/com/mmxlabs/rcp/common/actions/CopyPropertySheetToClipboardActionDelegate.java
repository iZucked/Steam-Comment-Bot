/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;

/**
 */
public class CopyPropertySheetToClipboardActionDelegate implements IViewActionDelegate {

	private PropertySheet propertySheet;;

	@Override
	public void run(final IAction action) {
		// TODO Auto-generated method stub

		if (propertySheet != null) {
			final IPage currentPage = propertySheet.getCurrentPage();
			if (currentPage != null) {
				Action realAction = null;
				final Control control = currentPage.getControl();
				if (control instanceof Table) {
					realAction = new CopyTableToClipboardAction((Table) control);
				} else if (control instanceof Tree) {
					realAction = new CopyTreeToClipboardAction((Tree) control);
				} else if (control instanceof Grid) {
					realAction = new CopyGridToClipboardAction((Grid) control);
				}
				if (realAction != null) {
					realAction.run();
				}
			}
		}
	}

	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {

	}

	@Override
	public void init(final IViewPart view) {

		if (view instanceof PropertySheet) {
			this.propertySheet = (PropertySheet) view;
		} else {
			this.propertySheet = null;
		}
	}
}

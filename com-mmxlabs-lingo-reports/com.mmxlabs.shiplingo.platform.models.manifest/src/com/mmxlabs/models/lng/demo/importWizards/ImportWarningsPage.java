/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.demo.importWizards;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Page for displaying the warnings from an import job
 * 
 * @author hinton
 *
 */
public class ImportWarningsPage extends WizardPage {
	TableViewer viewer;
	private ImportCSVFilesPage filesPage;
	
	protected ImportWarningsPage(String pageName, ImportCSVFilesPage filesPage) {
		super(pageName);
		this.filesPage = filesPage;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	@Override
	public void createControl(final Composite parent) {
		viewer = new TableViewer(parent);
		// set up table, and load inputs
		final TableViewerColumn fileColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableViewerColumn lineColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableViewerColumn fieldColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableViewerColumn messageColumn = new TableViewerColumn(viewer, SWT.NONE);
		
		fileColumn.getColumn().setText("File");
		lineColumn.getColumn().setText("Line");
		fieldColumn.getColumn().setText("Column");
		messageColumn.getColumn().setText("Problem");
		
		
	}
}

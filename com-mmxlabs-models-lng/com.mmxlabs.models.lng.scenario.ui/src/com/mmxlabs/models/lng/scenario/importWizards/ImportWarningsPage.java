/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.common.csv.IImportProblem;

/**
 * Page for displaying the warnings from an import job
 * 
 * @author hinton
 * 
 */
public class ImportWarningsPage extends WizardPage {
	TableViewer viewer;
	ImportCSVFilesPage filesPage;

	protected ImportWarningsPage(String pageName, ImportCSVFilesPage filesPage) {
		super(pageName);
		this.filesPage = filesPage;
		setTitle("Import warnings");
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible == true) {

			viewer.setInput(filesPage.getImportContext());
			viewer.refresh();
			int errors = viewer.getTable().getItemCount();
			setMessage(errors == 0 ? null : errors + " problems during import");
			// Pack columns, but skip filename as this can be very long
			for (int i = 1; i < viewer.getTable().getColumnCount(); ++i) {
				viewer.getTable().getColumn(i).pack();
			}
		}
	}

	private TableViewerColumn addViewerColumn(final String name, final ColumnLabelProvider lp) {
		final TableViewerColumn c = new TableViewerColumn(viewer, SWT.NONE);

		c.getColumn().setResizable(true);
		c.getColumn().setText(name);
		c.getColumn().setMoveable(true);
		c.getColumn().pack();
		c.setLabelProvider(lp);

		return c;
	}

	@Override
	public void createControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		// set up table, and load inputs
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		addViewerColumn("File", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IImportProblem) element).getFilename();
			}
		});

		addViewerColumn("Line", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IImportProblem) element).getLineNumber() == null ? "" : "" + ((IImportProblem) element).getLineNumber();
			}
		});

		addViewerColumn("Field", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IImportProblem) element).getField();
			}
		});

		addViewerColumn("Problem", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IImportProblem) element).getProblemDescription();
			}
		});

		viewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return ((IImportContext) inputElement).getProblems().toArray();
			}
		});

		setControl(viewer.getControl());
	}

	@Override
	public boolean isPageComplete() {
		return filesPage.getImportContext() != null;
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}
}

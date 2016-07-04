/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.common.csv.IImportProblem;

/**
 * A quick dialog for showing a list of import problems
 * 
 * @author hinton
 *
 */
public class ImportProblemDialog extends Dialog {
	private TableViewer viewer;
	private IImportContext context;

	protected ImportProblemDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
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
	
	
	private void resizeAndCenter() {
		final Shell shell = getShell();
		if (shell != null) {
			shell.layout(true);

			shell.setSize(shell.computeSize(640, 480));

			final Rectangle shellBounds = getParentShell().getBounds();
			final Point dialogSize = shell.getSize();

			shell.setLocation(shellBounds.x
					+ ((shellBounds.width - dialogSize.x) / 2), shellBounds.y
					+ ((shellBounds.height - dialogSize.y) / 2));
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);
	
		viewer = new TableViewer(c, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		// set up table, and load inputs
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		addViewerColumn("File", 
				new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IImportProblem) element).getFilename();
			}
		});
		
		addViewerColumn("Line", 
				new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IImportProblem) element).getLineNumber() == null ? "" : "" + ((IImportProblem)element).getLineNumber();
			}
		});
		
		addViewerColumn("Field", 
				new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IImportProblem) element).getField();
			}
		});
		
		addViewerColumn("Problem", 
				new ColumnLabelProvider() {
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
		viewer.setInput(context);
		
		return c;
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#create()
	 */
	@Override
	public void create() {
		super.create();
		for (final TableColumn column : viewer.getTable().getColumns()) {
			column.pack();
		}
		resizeAndCenter();
	}

	public void open(final IImportContext context) {
		this.context = context;
		super.open();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	
}

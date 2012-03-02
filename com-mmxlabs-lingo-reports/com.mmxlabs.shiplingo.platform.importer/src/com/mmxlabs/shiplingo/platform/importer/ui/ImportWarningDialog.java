/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.ui;

import java.util.Collection;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.mmxlabs.shiplingo.platform.importer.importers.ImportWarning;

/**
 * @author hinton
 * 
 */
public class ImportWarningDialog extends TitleAreaDialog {
	private static class Sorter extends ViewerSorter {
		@Override
		public int compare(final Viewer viewer, final Object e1, final Object e2) {
			final ImportWarning i1 = (ImportWarning) e1;
			final ImportWarning i2 = (ImportWarning) e2;
			int compare = i1.filename.compareTo(i2.filename);
			if (compare == 0) {
				compare = ((Integer) (i1.lineNumber)).compareTo((i2.lineNumber));
				if (compare == 0) {
					compare = i1.columnName.compareTo(i2.columnName);
					if (compare == 0) {
						compare = i1.warning.compareTo(i2.warning);
					}
				}
			}
			return compare;
		}
	}

	private static class ContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof Collection<?>) {
				final Collection<ImportWarning> warnings = (Collection<ImportWarning>) inputElement;
				return warnings.toArray(new ImportWarning[warnings.size()]);
			}
			return new Object[0];
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			viewer.refresh();
		}
	}

	private Table table;
	private Collection<ImportWarning> warnings = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ImportWarningDialog(final Shell parentShell) {
		super(parentShell);
	}

	public void setWarnings(final Collection<ImportWarning> warnings) {
		this.warnings = warnings;
	}

	@Override
	public void create() {
		super.create();
		setHelpAvailable(false);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		setMessage("Some problems were encountered while importing from CSV, which may have affected the correctness of the imported data.");
		setTitle("Import Warnings");
		final Composite area = (Composite) super.createDialogArea(parent);
		final Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setSorter(new Sorter());
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayout(new TableLayout());
		final TableViewerColumn filenameColumn = new TableViewerColumn(tableViewer, SWT.NONE);

		filenameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(final Object element) {
				return null;
			}

			@Override
			public String getText(final Object element) {
				return element == null ? "" : ((ImportWarning) element).filename;
			}
		});
		final TableColumn tblclmnFileName = filenameColumn.getColumn();
		tblclmnFileName.setWidth(100);
		tblclmnFileName.setText("File Name");

		final TableViewerColumn lineColumn = new TableViewerColumn(tableViewer, SWT.NONE);

		lineColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(final Object element) {
				return null;
			}

			@Override
			public String getText(final Object element) {
				if (element instanceof ImportWarning) {
					final ImportWarning iw = (ImportWarning) element;
					if (iw.lineNumber > 0) {
						return iw.lineNumber + "";
					}
				}
				return "";
			}
		});
		final TableColumn tblclmnLineNumber = lineColumn.getColumn();
		tblclmnLineNumber.setWidth(34);
		tblclmnLineNumber.setText("Line");

		final TableViewerColumn columnColumn = new TableViewerColumn(tableViewer, SWT.NONE);

		columnColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(final Object element) {
				return null;
			}

			@Override
			public String getText(final Object element) {
				if (element instanceof ImportWarning) {
					final ImportWarning iw = (ImportWarning) element;
					if (iw.columnName != null) {
						return iw.columnName;
					}
				}
				return "";
			}
		});
		final TableColumn tblclmnColumnName = columnColumn.getColumn();
		tblclmnColumnName.setWidth(62);
		tblclmnColumnName.setText("Column");

		final TableViewerColumn messageColumn = new TableViewerColumn(tableViewer, SWT.NONE);

		messageColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(final Object element) {
				return null;
			}

			@Override
			public String getText(final Object element) {
				if (element instanceof ImportWarning) {
					final ImportWarning iw = (ImportWarning) element;
					if (iw.warning != null) {
						return iw.warning;
					}
				}
				return "";
			}
		});
		final TableColumn tblclmnMessage = messageColumn.getColumn();
		tblclmnMessage.setWidth(369);
		tblclmnMessage.setText("Message");
		tableViewer.setContentProvider(new ContentProvider());

		tableViewer.setInput(warnings);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(570, 300);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}

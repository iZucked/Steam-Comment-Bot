package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


public class ComboTest {
	public class MyModel {
		public int counter;

		public MyModel(int counter) {
			this.counter = counter;
		}

		@Override
		public String toString() {
			return "Item " + this.counter;
		}
	}

	public ComboTest(Shell shell) {
		final Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		final TableViewer v = new TableViewer(table);
		v.setContentProvider(ArrayContentProvider.getInstance());

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth(200);
		column.setText("Column 1");
		TableViewerColumn viewerColumn = new TableViewerColumn(v, column);
		viewerColumn.setLabelProvider(new ColumnLabelProvider());
		viewerColumn.setEditingSupport(new EditingSupport(v) {

			@Override
			protected void setValue(Object element, Object value) {
				((MyModel) element).counter = ((Integer) value).intValue() * 10;
				getViewer().update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				// We need to calculate back to the index
				return Integer.valueOf(((MyModel) element).counter / 10);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new ComboBoxCellEditor(
						v.getTable(), new String[] { "Zero", "Ten", "Twenty", "Thirty",
							"Fourty", "Fifty", "Sixty", "Seventy", "Eighty",
							"Ninety" });
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		v.setInput(createModel());
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);
	}

	private List<MyModel> createModel() {
		List<MyModel> elements = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			elements.add(new MyModel(i * 10));
		}
		return elements;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new ComboTest(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.ui.editors.AbstractTableInlineEditor;

public class SuezCanalTariffTugBandInlineEditor extends AbstractTableInlineEditor {

	public SuezCanalTariffTugBandInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	protected @NonNull String getTableLabelText() {
		return "Tugs required";
	}

	@Override
	protected TableViewer createTable(final Composite parent) {

		final TableViewer tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		final Table sdrBandsTable = tableViewer.getTable();

		sdrBandsTable.setLinesVisible(true);
		sdrBandsTable.setHeaderVisible(true);

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				if (inputElement instanceof Collection<?>) {
					Collection<?> collection = (Collection<?>) inputElement;
					return collection.toArray();
				}
				return new Object[0];
			}
		});

		createTugsLabelColumn(tableViewer);
		createTugsColumn("Tugs", PricingPackage.Literals.SUEZ_CANAL_TUG_BAND__TUGS, tableViewer);

		tableViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				final SuezCanalTugBand b1 = (SuezCanalTugBand) e1;
				final SuezCanalTugBand b2 = (SuezCanalTugBand) e2;

				final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
				final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

				return Integer.compare(v1, v2);
			}
		});

		return tableViewer;
	}

	private TableViewerColumn createTugsLabelColumn(final TableViewer tableViewer) {

		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);

		column.getColumn().setText("");

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final SuezCanalTugBand band = (SuezCanalTugBand) element;
				String label = "";
				if (!band.isSetBandStart()) {
					label = String.format("0 to %,d", band.getBandEnd());
				} else if (!band.isSetBandEnd()) {
					label = String.format("Over %,d", band.getBandStart());
				} else {
					label = String.format("%s to %d", band.getBandStart(), band.getBandEnd());
				}
				return label;
			}
		});

		return column;

	}

	private TableViewerColumn createTugsColumn(final String name, final EAttribute attr, final TableViewer tableViewer) {

		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);

		column.getColumn().setText(name);

		column.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(final Object element, final Object value) {
				final EditingDomain ed = commandHandler.getEditingDomain();
				commandHandler.handleCommand(SetCommand.create(ed, element, attr, value), (EObject) element, attr);
			}

			@Override
			protected Object getValue(final Object element) {
				return ((EObject) element).eGet(attr);
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(tableViewer.getTable());
				ed.setFormatter(new IntegerFormatter("#0"));

				addCellEditorListener(ed);
				return ed;
			}

			@Override
			protected boolean canEdit(final Object element) {
				return true;
			}
		});

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return element == null ? "" : String.format("%d", ((EObject) element).eGet(attr));
			}
		});

		return column;

	}
}

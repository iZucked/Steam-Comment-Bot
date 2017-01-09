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
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.ui.editors.AbstractTableInlineEditor;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class SuezCanalTariffBandInlineEditor extends AbstractTableInlineEditor {

	public SuezCanalTariffBandInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	protected @NonNull String getTableLabelText() {
		return "SDR pricing bands per SCNT";
	}

	@Override
	protected TableViewer createTable(Composite parent) {

		TableViewer tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		final Table sdrBandsTable = tableViewer.getTable();

		sdrBandsTable.setLinesVisible(true);
		sdrBandsTable.setHeaderVisible(true);

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Collection<?>) {
					Collection<?> collection = (Collection<?>) inputElement;
					return collection.toArray();
				}
				return new Object[0];
			}
		});

		createLabelColumn(tableViewer);
		createPriceColumn("Laden", PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF, tableViewer);
		createPriceColumn("Ballast", PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF, tableViewer);

		tableViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				final SuezCanalTariffBand b1 = (SuezCanalTariffBand) e1;
				final SuezCanalTariffBand b2 = (SuezCanalTariffBand) e2;

				final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
				final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

				return Integer.compare(v1, v2);
			}
		});

		return tableViewer;
	}

	private TableViewerColumn createPriceColumn(String name, final EAttribute attr, TableViewer tableViewer) {

		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);

		column.getColumn().setText(name);

		column.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				final EditingDomain ed = commandHandler.getEditingDomain();
				commandHandler.handleCommand(SetCommand.create(ed, element, attr, value), (EObject) element, attr);
			}

			@Override
			protected Object getValue(Object element) {
				return ((EObject) element).eGet(attr);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(tableViewer.getTable());
				ed.setFormatter(new DoubleFormatter("#0.##"));

				addCellEditorListener(ed);
				return ed;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return element == null ? "" : String.format("%.3f", ((EObject) element).eGet(attr));
			}
		});

		return column;
	}

	private TableViewerColumn createLabelColumn(TableViewer tableViewer) {

		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);

		column.getColumn().setText("");

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final SuezCanalTariffBand band = (SuezCanalTariffBand) element;
				String label = "";
				if (!band.isSetBandStart()) {
					label = String.format("First %,d", band.getBandEnd());
				} else if (!band.isSetBandEnd()) {
					label = String.format("Over %,d", band.getBandStart());
				} else {
					label = String.format("%,d to %,d", band.getBandStart() + 1, band.getBandEnd());
				}
				return label;
			}
		});

		return column;
	}
}

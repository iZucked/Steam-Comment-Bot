/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class PanamaCanalTariffDetailComposite extends Composite implements IDisplayComposite {
	private IDisplayComposite delegate;
	private ICommandHandler commandHandler;
	private TableViewer tableViewer;

	public PanamaCanalTariffDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		delegate = new DefaultDetailComposite(this, style, toolkit);
		delegate.getComposite().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		@SuppressWarnings("unused")
		final Label consumptionCurve = toolkit.createLabel(this, "Pricing bands per m³ of capacity");

		final TableViewer tableViewer = new TableViewer(this, SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayout(new TableLayout());

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object inputElement) {
				Object[] things = ((PanamaCanalTariff) inputElement).getBands().toArray();

				return things;
			}
		});

		final TableViewerColumn labelColumn = createLabelColumn(tableViewer);
		final TableViewerColumn ladenColumn = createPriceColumn("Laden", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF, tableViewer);
		final TableViewerColumn ballastColumn = createPriceColumn("Ballast", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF, tableViewer);
		final TableViewerColumn ballastRoundtripColumn = createPriceColumn("Ballast Round-trip", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF, tableViewer);

		table.addListener(SWT.Resize, new Listener() {
			boolean resizing = false;

			@Override
			public void handleEvent(Event event) {
				if (resizing)
					return;
				resizing = true;
				labelColumn.getColumn().pack();
				ladenColumn.getColumn().pack();
				ballastColumn.getColumn().pack();
				ballastRoundtripColumn.getColumn().pack();
				resizing = false;
			}
		});

		PanamaCanalTariffDetailComposite.this.tableViewer = tableViewer;
		tableViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				final PanamaCanalTariffBand b1 = (PanamaCanalTariffBand) e1;
				final PanamaCanalTariffBand b2 = (PanamaCanalTariffBand) e2;

				final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
				final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

				return Integer.compare(v1, v2);
			}
		});

	}

	@Override
	public Composite getComposite() {
		return this;
	}

	PanamaCanalTariff oldValue = null;
	final Adapter adapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(Notification notification) {
			if (!isDisposed() && isVisible()) {
				if (tableViewer != null && tableViewer.getTable().isDisposed() == false) {
					tableViewer.refresh();
				}
			} else {
				PanamaCanalTariffDetailComposite.this.removeAdapter();
			}
		}

	};

	void removeAdapter() {
		if (oldValue != null) {
			oldValue.eAdapters().remove(adapter);
			oldValue = null;
		}
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject value, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		delegate.display(dialogContext, root, value, range, dbc);
		tableViewer.setInput(value);
		removeAdapter();
		oldValue = (PanamaCanalTariff) value;
		value.eAdapters().add(adapter);
	}

	@Override
	public void dispose() {
		removeAdapter();
		super.dispose();
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		delegate.setCommandHandler(commandHandler);
		this.commandHandler = commandHandler;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		delegate.displayValidationStatus(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editors.IDisplayComposite#setEditorWrapper(com.mmxlabs.models.ui.editors.IInlineEditorWrapper)
	 */
	@Override
	public void setEditorWrapper(IInlineEditorWrapper wrapper) {
		delegate.setEditorWrapper(wrapper);
	}

	@Override
	public boolean checkVisibility(IDialogEditingContext context) {
		return delegate.checkVisibility(context);
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
				final PanamaCanalTariffBand band = (PanamaCanalTariffBand) element;
				String label = "";
				if (!band.isSetBandStart()) {
					label = String.format("First %,d", band.getBandEnd());
				} else if (!band.isSetBandEnd()) {
					label = String.format("Over %,d", band.getBandStart());
				} else {
					final int diff = band.getBandEnd() - band.getBandStart();
					label = String.format("Next %,d", diff);
				}
				return label;
			}
		});

		return column;

	}
}

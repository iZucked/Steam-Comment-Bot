/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.displaycomposites;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
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
public class VSADetailComposite extends Composite implements IDisplayComposite {
	private IDisplayComposite delegate;
	private ICommandHandler commandHandler;
	private TableViewer tableViewer;

	public VSADetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		delegate = new DefaultDetailComposite(this, style, toolkit);
		delegate.getComposite().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		@SuppressWarnings("unused")
		final Label consumptionCurve = toolkit.createLabel(this, "Fuel Consumption");

		final TableViewer tableViewer = new TableViewer(this, SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayout(new TableLayout());

		final TableViewerColumn speedColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableViewerColumn fuelColumn = new TableViewerColumn(tableViewer, SWT.NONE);

		speedColumn.getColumn().setText("Speed (knots)");
		fuelColumn.getColumn().setText("Consumption (MT/day)");

		table.addListener(SWT.Resize, new Listener() {
			boolean resizing = false;

			@Override
			public void handleEvent(Event event) {
				if (resizing)
					return;
				resizing = true;
				speedColumn.getColumn().pack();
				fuelColumn.getColumn().pack();
				resizing = false;
			}
		});

		speedColumn.setEditingSupport(new EditingSupport(tableViewer) {
			final EAttribute attr = FleetPackage.eINSTANCE.getFuelConsumption_Speed();

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
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);
				ed.setFormatter(new DoubleFormatter("#0.##"));
				return ed;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		fuelColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return element == null ? "" : String.format("%.3f", ((FuelConsumption) element).getConsumption());
			}
		});

		speedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return element == null ? "" : String.format("%.2f", ((FuelConsumption) element).getSpeed());
			}
		});

		fuelColumn.setEditingSupport(new EditingSupport(tableViewer) {
			final EAttribute attr = FleetPackage.eINSTANCE.getFuelConsumption_Consumption();

			@Override
			protected void setValue(Object element, Object value) {
				final EditingDomain ed = commandHandler.getEditingDomain();
				commandHandler.handleCommand(SetCommand.create(ed, element, attr, value)

				, (EObject) element, attr);
			}

			@Override
			protected Object getValue(Object element) {
				return ((EObject) element).eGet(attr);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);
				ed.setFormatter(new DoubleFormatter("##0.###"));
				return ed;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		VSADetailComposite.this.tableViewer = tableViewer;
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object inputElement) {
				Object[] things = ((VesselStateAttributes) inputElement).getFuelConsumption().toArray();
				Arrays.sort(things, new Comparator() {

					@Override
					public int compare(Object arg0, Object arg1) {
						return ((Double) ((FuelConsumption) arg0).getSpeed()).compareTo(((FuelConsumption) arg1).getSpeed());
					}
				});

				return things;
			}
		});

		final Composite buttons = toolkit.createComposite(this);

		buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		final GridLayout buttonLayout = new GridLayout(2, true);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;
		final Button remove = toolkit.createButton(buttons, "-", SWT.NONE);
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		remove.setEnabled(false);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				remove.setEnabled(event.getSelection().isEmpty() == false);
			}
		});

		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();
				if (sel.isEmpty())
					return;
				if (sel instanceof IStructuredSelection) {
					final FuelConsumption fc = (FuelConsumption) ((IStructuredSelection) sel).getFirstElement();
					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), fc.eContainer(), fc.eContainingFeature(), fc), oldValue,
							FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumption());
					tableViewer.refresh();
				}
			}
		});
		final Button add = toolkit.createButton(buttons, "+", SWT.NONE);
		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();

				FuelConsumption selection;
				if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
					selection = (FuelConsumption) ((IStructuredSelection) sel).getFirstElement();
				} else {
					selection = null;
					for (final FuelConsumption c : oldValue.getFuelConsumption()) {
						if (selection == null || selection.getSpeed() < c.getSpeed())
							selection = c;
					}
				}
				final FuelConsumption newConsumption = FleetFactory.eINSTANCE.createFuelConsumption();
				newConsumption.setConsumption(selection == null ? 0 : selection.getConsumption() + 1);
				newConsumption.setSpeed(selection == null ? 15 : selection.getSpeed() + 1);
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), oldValue, FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumption(), newConsumption),
						oldValue, FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumption());
				tableViewer.setSelection(new StructuredSelection(newConsumption));
			}
		});
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	VesselStateAttributes oldValue = null;
	final Adapter adapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(Notification notification) {
			if (!isDisposed() && isVisible()) {
				if (tableViewer != null && tableViewer.getTable().isDisposed() == false)
					tableViewer.refresh();
			} else {
				VSADetailComposite.this.removeAdapter();
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
		oldValue = (VesselStateAttributes) value;
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
}

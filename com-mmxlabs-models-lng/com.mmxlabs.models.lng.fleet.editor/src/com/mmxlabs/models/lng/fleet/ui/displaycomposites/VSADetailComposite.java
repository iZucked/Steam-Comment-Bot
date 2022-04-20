/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.displaycomposites;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.SafeMMXContentAdapter;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

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
	private final Map<EStructuralFeature, IInlineEditor> feature2Editor = new HashMap<>();
	private Set<EStructuralFeature> portFeatures = Sets.newHashSet(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE);

	public VSADetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		delegate = new DefaultDetailComposite(this, style, toolkit) {
			@Override
			public IInlineEditor addInlineEditor(IInlineEditor editor) {

				editor = super.addInlineEditor(editor);
				if (editor != null) {
					final EStructuralFeature f = editor.getFeature();
					feature2Editor.put(f, editor);

				}

				return editor;
			}

			@Override
			public void createControls(IDialogEditingContext dialogContext, MMXRootObject root, EObject object, EMFDataBindingContext dbc) {
				this.object = object;
				toolkit.adapt(this);
				setDefaultHelpContext(object);

				Group g_port = new Group(this, SWT.NONE);
				toolkit.adapt(g_port);
				g_port.setText("Port");
				if (object.eContainingFeature() == FleetPackage.Literals.VESSEL__LADEN_ATTRIBUTES) {
					g_port.setText("Load port");
				} else if (object.eContainingFeature() == FleetPackage.Literals.VESSEL__BALLAST_ATTRIBUTES) {
					g_port.setText("Discharge port");
				}
				g_port.setLayout(new GridLayout(2, false));
				g_port.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).grab(true, false).create());
				for (final IInlineEditor editor : editors) {
					if (portFeatures.contains(editor.getFeature())) {
						final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(g_port, SWT.NONE) : null;
						editor.setLabel(label);
						final Control control = editor.createControl(g_port, dbc, toolkit);
						dialogContext.registerEditorControl(object, editor.getFeature(), control);

						control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
						control.setData(LABEL_CONTROL_KEY, label);
						control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
						if (label != null) {
							toolkit.adapt(label, true, false);
							label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
							dialogContext.registerEditorControl(object, editor.getFeature(), label);
						}
					}
				}
				Group g_voyage = new Group(this, SWT.NONE);
				toolkit.adapt(g_voyage);
				g_voyage.setLayout(new GridLayout(2, false));
				g_voyage.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).grab(true, false).create());
				g_voyage.setText("Voyage");
				for (final IInlineEditor editor : editors) {
					if (!portFeatures.contains(editor.getFeature()) && editor.getFeature() != FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE) {
						final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(g_voyage, SWT.NONE) : null;
						editor.setLabel(label);
						final Control control = editor.createControl(g_voyage, dbc, toolkit);
						dialogContext.registerEditorControl(object, editor.getFeature(), control);
						control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
						control.setData(LABEL_CONTROL_KEY, label);
						control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
						if (label != null) {
							toolkit.adapt(label, true, false);
							label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
							dialogContext.registerEditorControl(object, editor.getFeature(), label);
						}
					}
				}
			}
		};

		delegate.getComposite().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		@SuppressWarnings("unused")
		Composite lblComposite = new Composite(this, SWT.NONE);
		lblComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());
		lblComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		final Label consumptionCurve = toolkit.createLabel(lblComposite, "Fuel Consumption");

		fuelConsumptionOverrideBtn = new Button(lblComposite, SWT.CHECK);
		fuelConsumptionOverrideBtn.setText("Override");
		fuelConsumptionOverrideBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CompoundCommand cmd = new CompoundCommand();
				cmd.append(SetCommand.create(commandHandler.getEditingDomain(), oldValue, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE,
						fuelConsumptionOverrideBtn.getSelection()));
				if (fuelConsumptionOverrideBtn.getSelection() == false) {
					cmd.append(SetCommand.create(commandHandler.getEditingDomain(), oldValue, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION, SetCommand.UNSET_VALUE));
				}
				commandHandler.handleCommand(cmd, oldValue, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE);
			}
		});
		fuelConsumptionOverrideBtn.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

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
				if (resizing) {
					return;
				}
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
				commandHandler.handleCommand(SetCommand.create(ed, element, attr, value), (EObject) element, attr);
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
				VesselStateAttributes vesselStateAttributes = (VesselStateAttributes) inputElement;
				List<?> list = (List<?>) vesselStateAttributes.eGetWithDefault(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION);
				if (list == null) {
					return new Object[0];
				}
				Object[] things = list.toArray();
				Arrays.sort(things, new Comparator<Object>() {

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

		final Button add = toolkit.createButton(buttons, null, SWT.NONE);
		{
			add.setImage(CommonImages.getImage(IconPaths.Plus, IconMode.Enabled));
		}

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
					for (final FuelConsumption c : oldValue.getVesselOrDelegateFuelConsumption()) {
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

		final Button remove = toolkit.createButton(buttons, null, SWT.NONE);
		{
			remove.setImage(CommonImages.getImage(IconPaths.Delete, IconMode.Enabled));
		}

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

	}

	@Override
	public Composite getComposite() {
		return this;
	}

	VesselStateAttributes oldValue = null;
	private final @NonNull Adapter adapter = new SafeMMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(Notification notification) {
			if (!isDisposed() && isVisible()) {

				if (notification.getFeature() == FleetPackage.Literals.VESSEL__REFERENCE) {
					fuelConsumptionOverrideBtn.setVisible(notification.getNewValue() != null);
					tableViewer.getControl().setEnabled(notification.getNewValue() == null || oldValue.isFuelConsumptionOverride());
				}

				if (tableViewer != null && tableViewer.getTable().isDisposed() == false) {
					if (notification.getFeature() == FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE && notification.getNotifier() == oldValue) {
						tableViewer.getControl().setEnabled(notification.getNewBooleanValue());
						fuelConsumptionOverrideBtn.setSelection(notification.getNewBooleanValue());

					}
					tableViewer.refresh();
				}
			} else {
				VSADetailComposite.this.removeAdapter();
			}
		}

	};
	private Button fuelConsumptionOverrideBtn;

	void removeAdapter() {
		VesselStateAttributes oldValue2 = oldValue;
		oldValue = null;
		if (oldValue2 != null) {
			oldValue2.eAdapters().remove(adapter);

			if (oldValue2.eContainer() != null) {
				oldValue2.eContainer().eAdapters().remove(adapter);
			}

		}
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject value, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		delegate.display(dialogContext, root, value, range, dbc);
		tableViewer.setInput(value);
		tableViewer.getControl().setEnabled(((Vessel) value.eContainer()).getReference() == null || ((VesselStateAttributes) value).isFuelConsumptionOverride());
		fuelConsumptionOverrideBtn.setSelection(((VesselStateAttributes) value).isFuelConsumptionOverride());

		removeAdapter();
		oldValue = (VesselStateAttributes) value;
		value.eAdapters().add(adapter);
		if (value.eContainer() != null) {
			fuelConsumptionOverrideBtn.setVisible(((Vessel) value.eContainer()).getReference() != null);
			value.eContainer().eAdapters().add(adapter);
		}
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
	 * @see com.mmxlabs.models.ui.editors.IDisplayComposite#setEditorWrapper(com.mmxlabs. models.ui.editors.IInlineEditorWrapper)
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

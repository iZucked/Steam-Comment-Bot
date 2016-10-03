/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;

public class CargoTypeInlineEditor extends MMXAdapterImpl implements IInlineEditor {

	/**
	 * Adapter factory instance. This contains all factories registered in the global registry.
	 */
	private static final ComposedAdapterFactory FACTORY = createAdapterFactory();

	/**
	 * Object being edited
	 */
	protected Cargo input;

	protected final EOperation operation;

	protected boolean currentlySettingValue = false;

	protected ICommandHandler commandHandler;

	//protected IScenarioEditingLocation scenarioEditingLocation;

	private final int style;

	private Button desButton;

	private Button fobButton;

	private Button shippedButton;

	private boolean editorEnabled = true;
	private boolean editorLocked = false;
	private boolean editorVisible = true;

	/**
	 * {@link ControlDecoration} used to show validation messages.
	 */
	private ControlDecoration validationDecoration;

	/**
	 * Reference to the {@link Control} to use to display tool tips.
	 */
	private Control tooltipControl;

	/**
	 * Cached reference to the Information icon
	 */
	protected final FieldDecoration decorationInfo = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION);

	/**
	 * Cached reference to the Warning icon
	 */
	protected final FieldDecoration decorationWarning = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);

	/**
	 * Cached reference to the Error icon
	 */
	protected final FieldDecoration decorationError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);

	private final DisposeListener disposeListener = new DisposeListener() {
		@Override
		public void widgetDisposed(final DisposeEvent e) {
			if (e.widget == tooltipControl) {
				if (input != null) {
					input.eAdapters().remove(CargoTypeInlineEditor.this);
				}
				e.widget.removeDisposeListener(this);
			}
		}
	};

	protected Label label;

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		if (this.input != null) {
			this.input.eAdapters().remove(this);
		}
		this.input = (Cargo) input;
		if (input != null) {
			input.eAdapters().add(this);
			doUpdateDisplayWithValue();
		}

		// Update control tool-tips using IItemPropertyDescriptor
		if (input != null && tooltipControl != null) {
			// Set to blank by default - and replace below if the feature is
			// found
			String toolTip = "";
			String labelText = "Cargo Type";
			// This will fetch the property source of the input object
			final IItemPropertySource inputPropertySource = (IItemPropertySource) FACTORY.adapt(input, IItemPropertySource.class);

			// Iterate through the property descriptors to find a matching
			// descriptor for the feature
			for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(input)) {

				if (operation.equals(descriptor.getFeature(input))) {
					// Found match

					toolTip = descriptor.getDescription(input).replace("{0}", EditorUtils.unmangle(input.eClass().getName()).toLowerCase());

					labelText = descriptor.getDisplayName(input);
					break;
				}
			}
			// Update tooltip text
			// tooltipControl.setToolTipText(toolTip);
			setToolTipText(tooltipControl, toolTip);
			if (label != null)
				label.setText(labelText);
		}
	}

	/**
	 * Set the tooltip on control, and any child controls if it's a composite
	 * 
	 * @param control
	 * @param toolTipText
	 */
	private void setToolTipText(final Control control, final String toolTipText) {
		control.setToolTipText(toolTipText);
		if (control instanceof Composite) {
			for (final Control sub : ((Composite) control).getChildren()) {
				setToolTipText(sub, toolTipText);
			}
			final Object data = control.getData(IDisplayComposite.LABEL_CONTROL_KEY);
			if (data instanceof Control) {
				((Control) data).setToolTipText(toolTipText);
			}
		}
	}

	private synchronized void doUpdateDisplayWithValue() {
		doUpdateDisplayWithValue(false);
	}

	//
	private synchronized void doUpdateDisplayWithValue(final boolean allowRecursion) {
		if (currentlySettingValue && !allowRecursion) {
			return;
		}
		currentlySettingValue = true;

		switch (getValue()) {
		case DES:
			desButton.setSelection(true);
			break;
		case FLEET:
			shippedButton.setSelection(true);
			break;
		case FOB:
			fobButton.setSelection(true);
			break;

		}
		currentlySettingValue = false;
	}

	/**
	 * Subclasses should call this method when their value has been changed.
	 * 
	 * @param value
	 */
	protected synchronized void doSetValue() {
		if (currentlySettingValue) {
			// avoid re-entering
			return;
		}
		currentlySettingValue = true;

		final Command command = createSetCommand();
		commandHandler.handleCommand(command, input, null);

		// create set command here.
		currentlySettingValue = false;
	}

	@Override
	public void processValidation(final IStatus status) {
		if (status.isOK()) {
			// No problems, so hide decoration
			validationDecoration.hide();
		} else {
			// Default severity
			int severity = IStatus.INFO;

			// Builder used to accumulate messages
			final StringBuilder sb = new StringBuilder();

			if (!status.isMultiStatus()) {
				if (checkStatus(status)) {

					sb.append(status.getMessage());

					// Is severity worse, then note it
					if (status.getSeverity() > severity) {
						severity = status.getSeverity();
					}
				}

			} else {
				final IStatus[] children = status.getChildren();
				for (final IStatus element : children) {
					if (checkStatus(element)) {

						sb.append(element.getMessage());
						sb.append("\n");
						// Is severity worse, then note it
						if (element.getSeverity() > severity) {
							severity = element.getSeverity();
						}
					}
				}
			}

			if (sb.toString().isEmpty()) {
				// No problems, so hide decoration
				validationDecoration.hide();
				return;
			}

			// Update description text
			validationDecoration.setDescriptionText(sb.toString());

			// Update icon
			switch (severity) {
			case IStatus.INFO:
				validationDecoration.setImage(decorationInfo.getImage());
				break;
			case IStatus.WARNING:
				validationDecoration.setImage(decorationWarning.getImage());
				break;
			case IStatus.ERROR:
			default:
				validationDecoration.setImage(decorationError.getImage());
				break;
			}

			// Show the decoration.
			validationDecoration.show();
		}
	}

	/**
	 * Check status message applies to this editor.
	 * 
	 * @param status
	 * @return
	 */
	private boolean checkStatus(final IStatus status) {

		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus s = (IDetailConstraintStatus) status;

			final Collection<EObject> objects = s.getObjects();
			if (objects.contains(input)) {
				if (s.getFeaturesForEObject(input).contains(operation)) {
					return true;
				}
			}
		}

		return false;
	}

	protected Command createSetCommand() {

		// final CompoundCommand cmd = new CompoundCommand("Change cargo type");
		// if (shippedButton.getSelection()) {
		// cmd.append(commandHandler.getEditingDomain().createCommand(SetCommand.class, new CommandParameter(input.getLoadSlot(), CargoPackage.eINSTANCE.getLoadSlot_DESPurchase(), false)));
		// cmd.append(commandHandler.getEditingDomain().createCommand(SetCommand.class, new CommandParameter(input.getDischargeSlot(), CargoPackage.eINSTANCE.getDischargeSlot_FOBSale(), false)));
		// } else if (desButton.getSelection()) {
		// cmd.append(commandHandler.getEditingDomain().createCommand(SetCommand.class, new CommandParameter(input.getLoadSlot(), CargoPackage.eINSTANCE.getLoadSlot_DESPurchase(), true)));
		// cmd.append(commandHandler.getEditingDomain().createCommand(SetCommand.class, new CommandParameter(input.getDischargeSlot(), CargoPackage.eINSTANCE.getDischargeSlot_FOBSale(), false)));
		// } else if (fobButton.getSelection()) {
		// cmd.append(commandHandler.getEditingDomain().createCommand(SetCommand.class, new CommandParameter(input.getLoadSlot(), CargoPackage.eINSTANCE.getLoadSlot_DESPurchase(), false)));
		// cmd.append(commandHandler.getEditingDomain().createCommand(SetCommand.class, new CommandParameter(input.getDischargeSlot(), CargoPackage.eINSTANCE.getDischargeSlot_FOBSale(), true)));
		// }
		//
		// return cmd;
		throw new UnsupportedOperationException("Not implemented");
	}

	//
	protected CargoType getValue() {
		return input.getCargoType();
	}

	public Control wrapControl(final Control c) {
		// Create decorator for validation items
		{
			validationDecoration = new ControlDecoration(c, SWT.LEFT | SWT.TOP);
			validationDecoration.hide();

			// These should be the defaults...
			validationDecoration.setShowHover(true);
			validationDecoration.setShowOnlyOnFocus(false);

			// Set a default image
			// commented out, because this takes about 70% of the runtime of displaying the editor
			// validationDecoration.setImage(decorationInfo.getImage());

			// Hide by default
		}

		// Store reference to this control so we can display tool tips.
		tooltipControl = c;
		c.addDisposeListener(disposeListener);
		return c;
	}

	/**
	 * Utility method to create a {@link ComposedAdapterFactory}. Taken from org.eclipse.emf.compare.util.AdapterUtils.
	 * 
	 * @return
	 */
	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	@Override
	public EStructuralFeature getFeature() {
		return null;
	}

	@Override
	public void setCommandHandler(final ICommandHandler handler) {
		this.commandHandler = handler;
	}

	@Override
	public void setLabel(final Label label) {
		this.label = label;
	}

	public CargoTypeInlineEditor(final int style) {
		this.style = style;
		operation = CargoPackage.eINSTANCE.getCargo__GetCargoType();
	}

	public Control createValueControl(final Composite parent) {

		final Group g = new Group(parent, style);
		g.setLayout(new org.eclipse.swt.layout.GridLayout(3, true));

		shippedButton = new Button(g, SWT.RADIO);
		shippedButton.setText("Shipped");

		fobButton = new Button(g, SWT.RADIO);
		fobButton.setText("FOB Sale");

		desButton = new Button(g, SWT.RADIO);
		desButton.setText("DES Purchase");

		final SelectionAdapter selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				doSetValue();
			}
		};

		shippedButton.addSelectionListener(selectionListener);
		fobButton.addSelectionListener(selectionListener);
		desButton.addSelectionListener(selectionListener);

		shippedButton.setEnabled(false);
		fobButton.setEnabled(false);
		desButton.setEnabled(false);
		return g;
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		return wrapControl(createValueControl(parent));
	}

	@Override
	public void reallyNotifyChanged(final Notification notification) {

	}

	public EObject getEditorTarget() {
		return input;
	}

	@Override
	public Label getLabel() {
		return label;
	}

	@Override
	public void setEditorLocked(boolean locked) {
		this.editorLocked = locked;
	}

	@Override
	public boolean isEditorLocked() {
		return editorLocked;
	}

	@Override
	public void setEditorEnabled(boolean enabled) {
		this.editorEnabled = enabled;
	}

	@Override
	public boolean isEditorEnabled() {
		return editorEnabled;
	}

	@Override
	public void setEditorVisible(boolean visible) {
		this.editorVisible = visible;
	}

	@Override
	public boolean isEditorVisible() {
		return editorVisible;
	}

	@Override
	public void addNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
		// TODO Auto-generated method stub

	}

	public boolean hasLabel() {
		return true;
	}
}

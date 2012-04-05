/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;

/**
 * 
 * TODO sometimes field B should be refreshed when field A changes; currently only field A knows about changes because they happen in here.
 * 
 * @author Tom Hinton
 * 
 */
public abstract class BasicAttributeInlineEditor extends MMXAdapterImpl implements IInlineEditor {

	/**
	 * Adapter factory instance. This contains all factories registered in the global registry.
	 */
	private static final ComposedAdapterFactory FACTORY = createAdapterFactory();

	/**
	 * Object being edited
	 */
	protected EObject input;

	protected final EStructuralFeature feature;
	protected boolean currentlySettingValue = false;

	protected ICommandHandler commandHandler;

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
					input.eAdapters().remove(BasicAttributeInlineEditor.this);
				}
				e.widget.removeDisposeListener(this);
			}
		}
	};

	protected Label label;

	public BasicAttributeInlineEditor(final EStructuralFeature feature) {
		this.feature = feature;
	}

	@Override
	public void display(final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		if (this.input != null) {
			this.input.eAdapters().remove(this);
		}
		this.input = input;
		if (input != null) {
			input.eAdapters().add(this);
			doUpdateDisplayWithValue();
		}

		// Update control tool-tips using IItemPropertyDescriptor
		if (input != null && tooltipControl != null) {

			// Set to blank by default - and replace below if the feature is
			// found
			String toolTip = "";
			String labelText = "";
			// This will fetch the property source of the input object
			final IItemPropertySource inputPropertySource = (IItemPropertySource) FACTORY.adapt(input, IItemPropertySource.class);

			// Iterate through the property descriptors to find a matching
			// descriptor for the feature
			for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(input)) {

				if (feature.equals(descriptor.getFeature(input))) {
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

	private synchronized void doUpdateDisplayWithValue(final boolean allowRecursion) {
		if (currentlySettingValue && !allowRecursion) {
			return;
		}
		currentlySettingValue = true;

		updateDisplay(getValue());
		currentlySettingValue = false;
	}

	/**
	 * Subclasses can override this to trigger a redisplay when other fields change
	 * 
	 * @param changedFeature
	 * @return
	 */
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return feature.equals(changedFeature);
	}

	@Override
	public void reallyNotifyChanged(final Notification msg) {
		// check if msg is relevant
		if (msg.getFeature() != null && updateOnChangeToFeature(msg.getFeature())) {
			doUpdateDisplayWithValue(feature.equals(msg.getFeature()) == false);
		}
		if (msg.getFeature() != null && msg.getFeature().equals(feature)) {
			// it is a change to our feature
			doUpdateDisplayWithValue();
		}
	}

	/**
	 * Subclasses should use this method to display a value
	 */
	protected abstract void updateDisplay(Object value);

	/**
	 * Subclasses should call this method when their value has been changed.
	 * 
	 * @param value
	 */
	protected synchronized void doSetValue(final Object value) {
		// System.err.println("setvalue on " + feature.getName() + " to " +
		// value + " (" + currentlySettingValue + ")");
		if (currentlySettingValue) {
			return; // avoid re-entering
		}
		currentlySettingValue = true;
		final Object currentValue = getValue();
		if (!(/* (currentValue == null && value == null) || */((currentValue != null && value != null) && currentValue.equals(value)))) {
			final Command command = createSetCommand(value);
			commandHandler.handleCommand(command, input, feature);
			// editingDomain.getCommandStack().execute(command);
		}

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
				if (s.getFeaturesForEObject(input).contains(feature)) {
					return true;
				}
			}
		}

		return false;
	}

	protected Command createSetCommand(final Object value) {
		// System.err.println("Creating set command (" + input + "." +
		// feature.getName() + " <- " + value + ")");

		final Command command = commandHandler.getEditingDomain().createCommand(SetCommand.class,
				new CommandParameter(input, feature, value));

		return command;
	}

	protected Object getValue() {
		return input.eGet(feature);
	}

	public Control wrapControl(final Control c) {
		// Create decorator for validation items
		{
			validationDecoration = new ControlDecoration(c, SWT.LEFT | SWT.TOP);

			// These should be the defaults...
			validationDecoration.setShowHover(true);
			validationDecoration.setShowOnlyOnFocus(false);

			// Set a default image
			validationDecoration.setImage(decorationInfo.getImage());

			// Hide by default
			validationDecoration.hide();
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
		return feature;
	}

	@Override
	public void setCommandHandler(final ICommandHandler handler) {
		this.commandHandler = handler;
	}

	@Override
	public void setLabel(final Label label) {
		this.label = label;
	}
}

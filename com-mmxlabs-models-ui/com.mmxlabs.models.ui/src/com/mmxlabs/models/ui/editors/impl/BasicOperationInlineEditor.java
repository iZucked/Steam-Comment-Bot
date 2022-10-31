/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.models.ui.editors.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * 
 * TODO sometimes field B should be refreshed when field A changes; currently only field A knows about changes because they happen in here.
 * 
 * @author Tom Hinton, FM
 * 
 */
public abstract class BasicOperationInlineEditor extends MMXAdapterImpl implements IInlineEditor {

	private static final Logger log = LoggerFactory.getLogger(BasicOperationInlineEditor.class);

	private final Set<IInlineEditorExternalNotificationListener> listeners = new HashSet<>();

	/**
	 * Adapter factory instance. This contains all factories registered in the global registry.
	 */
	private final ComposedAdapterFactory FACTORY = createAdapterFactory();

	protected IMMXContentProposalProvider proposalHelper;

	/**
	 * Object being edited
	 */
	protected EObject input;
	protected Collection<EObject> ranges;

	protected final ETypedElement typedElement;
	protected boolean currentlySettingValue = false;

	protected ICommandHandler commandHandler;

	/**
	 * Reference to the {@link Control} to use to display tool tips.
	 */
	private Control tooltipControl;

	private final DisposeListener disposeListener = new DisposeListener() {
		@Override
		public void widgetDisposed(final DisposeEvent e) {
			if (e.widget == tooltipControl) {
				if (input != null) {
					input.eAdapters().remove(BasicOperationInlineEditor.this);
				}

				if (ranges != null) {
					for (final EObject eObj : ranges) {
						eObj.eAdapters().remove(BasicOperationInlineEditor.this);
					}
				}

				e.widget.removeDisposeListener(this);
			}
		}
	};

	protected Label label;

	private boolean editorEnabled = true;
	private final boolean editorVisible = true;
	private boolean editorLocked = false;

	private IItemPropertyDescriptor propertyDescriptor;

	protected MMXRootObject rootObject;

	public BasicOperationInlineEditor(final ETypedElement feature) {
		this.typedElement = feature;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {
		this.rootObject = rootObject;
		if (this.input != null) {
			this.input.eAdapters().remove(this);
		}
		this.input = input;
		if (input != null) {
			input.eAdapters().add(this);
			doUpdateDisplayWithValue();
		}
		if (this.ranges != null) {
			for (final EObject eObj : this.ranges) {
				eObj.eAdapters().remove(this);
			}
		}
		this.ranges = range;
		if (this.ranges != null) {
			for (final EObject eObj : this.ranges) {
				eObj.eAdapters().add(this);
			}
		}

		this.propertyDescriptor = null;

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

				if (typedElement.equals(descriptor.getFeature(input))) {
					// Found match

					this.propertyDescriptor = descriptor;

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

		setControlsEnabled(!isFeatureReadonly() && isEditorEnabled());

		if (proposalHelper != null) {
			proposalHelper.setRootObject(rootObject);
		}

		firePostDisplay(dialogContext, rootObject, input, range);
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

		final Runnable runnable = new Runnable() {

			@Override
			public void run() {

				updateDisplay(getValue());
				currentlySettingValue = false;
			}

		};
		RunnerHelper.runNowOrAsync(runnable);
	}

	/**
	 * Subclasses can override this to trigger a redisplay when other fields change
	 * 
	 * @param changedFeature
	 * @return
	 */
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return typedElement.equals(changedFeature);
	}

	@Override
	public void reallyNotifyChanged(final Notification msg) {

		fireNotificationChanged(msg);

		// check if msg is relevant
		if (msg.getFeature() != null && updateOnChangeToFeature(msg.getFeature())) {
			doUpdateDisplayWithValue(typedElement.equals(msg.getFeature()) == false);
		}
		if (msg.getFeature() != null && msg.getFeature().equals(typedElement)) {
			// it is a change to our feature
			doUpdateDisplayWithValue();
		}

	}

	@Override
	protected void missedNotifications(final List<Notification> missed) {
		for (final Notification n : missed) {
			reallyNotifyChanged(n);
		}
	}

	/**
	 * Subclasses should use this method to display a value
	 */
	protected abstract void updateDisplay(Object value);


	@Override
	public void processValidation(final IStatus status) {

	}

	/**
	 * Check status message applies to this editor.
	 * 
	 * @param status
	 * @return
	 */
	protected int checkStatus(final IStatus status, int currentSeverity, final StringBuilder sb) {
		if (status.isMultiStatus()) {
			final IStatus[] children = status.getChildren();
			for (final IStatus element : children) {
				final int severity = checkStatus(element, currentSeverity, sb);

				// Is severity worse, then note it
				if (severity > currentSeverity) {
					currentSeverity = element.getSeverity();
				}

			}
		}
		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus element = (IDetailConstraintStatus) status;

			final Collection<EObject> objects = element.getObjects();
			if (objects.contains(input)) {
				if (element.getFeaturesForEObject(input).contains(typedElement)) {

					sb.append(element.getMessage());
					sb.append("\n");

					// Is severity worse, then note it
					if (element.getSeverity() > currentSeverity) {
						currentSeverity = element.getSeverity();
					}

					return currentSeverity;
				}
			}
		}

		return currentSeverity;
	}

	protected Object getValue() {
		// May be null if control is hidden/disabled, but external code has still triggered a refresh
		if (input == null) {
			return null;
		}
		if (typedElement instanceof EStructuralFeature f) {
			return input.eGet(f);
		} else if (typedElement instanceof EOperation operation) {
			try {
				return input.eInvoke(operation, null);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public Control wrapControl(final Control c) {
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
	public ETypedElement getFeature() {
		return typedElement;
	}

	@Override
	public void setCommandHandler(final ICommandHandler handler) {
		this.commandHandler = handler;
	}

	@Override
	public void setLabel(final Label label) {
		this.label = label;
	}

	/**
	 */
	@Override
	public Label getLabel() {
		return label;
	}

	/**
	 */
	@Override
	public void setEditorEnabled(final boolean enabled) {
		this.editorEnabled = enabled;
		setControlsEnabled(!editorLocked && editorEnabled);
	}

	/**
	 */
	@Override
	public boolean isEditorEnabled() {
		return editorEnabled;
	}

	/**
	 */
	@Override
	public void setEditorLocked(final boolean locked) {
		this.editorLocked = locked;
		setControlsEnabled(!editorLocked && editorEnabled);
	}

	/**
	 */
	@Override
	public boolean isEditorLocked() {
		return editorLocked;
	}

	/**
	 */
	@Override
	public void setEditorVisible(final boolean visible) {
		this.editorEnabled = visible;
		setControlsVisible(visible);
	}

	/**
	 */
	@Override
	public boolean isEditorVisible() {
		return editorVisible;
	}

	@Override
	public EObject getEditorTarget() {
		return input;
	}

	/**
	 */
	protected void setControlsEnabled(final boolean enabled) {
		if (label != null && !label.isDisposed()) {
			label.setEnabled(!isFeatureReadonly() && enabled);
		}
	}

	/**
	 */
	protected void setControlsVisible(final boolean visible) {
		if (label != null && !label.isDisposed()) {
			label.setVisible(visible);
		}
	}

	/**
	 */
	@Override
	public void addNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
		this.listeners.add(listener);
	}

	/**
	 */
	@Override
	public void removeNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
		this.listeners.remove(listener);
	}

	private void fireNotificationChanged(final Notification notification) {
		final Set<IInlineEditorExternalNotificationListener> copy = new HashSet<>(listeners);
		for (final IInlineEditorExternalNotificationListener l : copy) {
			l.notifyChanged(notification);
		}
	}

	private void firePostDisplay(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		final Set<IInlineEditorExternalNotificationListener> copy = new HashSet<>(listeners);
		for (final IInlineEditorExternalNotificationListener l : copy) {
			try {
				l.postDisplay(this, dialogContext, context, input, range);
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 */
	protected boolean isFeatureReadonly() {
		return propertyDescriptor == null ? false : !propertyDescriptor.canSetProperty(input);
	}

	@Override
	public boolean hasLabel() {
		return true;
	}
}

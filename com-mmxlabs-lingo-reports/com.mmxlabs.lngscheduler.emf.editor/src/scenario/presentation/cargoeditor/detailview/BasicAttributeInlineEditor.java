/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
/**
 * 
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.presentation.cargoeditor.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * 
 * TODO sometimes field B should be refreshed when field A changes; currently
 * only field A knows about changes because they happen in here.
 * 
 * @author Tom Hinton
 * 
 */
public abstract class BasicAttributeInlineEditor extends AdapterImpl implements
		IInlineEditor {
	protected EObject input;
	protected final EMFPath path;
	protected final EStructuralFeature feature;
	protected boolean currentlySettingValue = false;

	protected final EditingDomain editingDomain;
	protected final ICommandProcessor commandProcessor;

	public BasicAttributeInlineEditor(final EMFPath path,
			final EStructuralFeature feature,
			final EditingDomain editingDomain,
			final ICommandProcessor commandProcessor) {
		this.path = path;
		this.feature = feature;
		this.editingDomain = editingDomain;
		this.commandProcessor = commandProcessor;
	}

	@Override
	public void setInput(final EObject source) {
		final EObject object = (EObject) path.get(source);
		if (input != null) {
			input.eAdapters().remove(this);
		}
		input = object;
		if (input != null) {
			input.eAdapters().add(this);
			doUpdateDisplayWithValue();
		}
	}

	private synchronized void doUpdateDisplayWithValue() {
		if (currentlySettingValue)
			return;
		currentlySettingValue = true;
		updateDisplay(getValue());
		currentlySettingValue = false;
	}

	/**
	 * Subclasses can override this to trigger a redisplay when other fields
	 * change
	 * 
	 * @param changedFeature
	 * @return
	 */
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return feature.equals(changedFeature);
	}

	@Override
	public void notifyChanged(final Notification msg) {
		super.notifyChanged(msg);
		// check if msg is relevant
		if (msg.getFeature() != null
				&& updateOnChangeToFeature(msg.getFeature())) {
			doUpdateDisplayWithValue();
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
		if (currentlySettingValue)
			return; // avoid re-entering
		currentlySettingValue = true;
		final Object currentValue = getValue();
		if (!((currentValue == null && value == null) || ((currentValue != null && value != null) && currentValue
				.equals(value)))) {
			final Command command = createSetCommand(value);
			commandProcessor.processCommand(command, input, feature);
//			editingDomain.getCommandStack().execute(command);
		}

		// create set command here.
		currentlySettingValue = false;
	}

	protected Command createSetCommand(final Object value) {
		// System.err.println("Creating set command (" + input + "." +
		// feature.getName() + " <- " + value + ")");
		final Command command = editingDomain.createCommand(SetCommand.class,
				new CommandParameter(input, feature, value));
		if (value == null) {
			((SetCommand) command).setLabel("Clear " + feature.getName());
		} else {
			((SetCommand) command).setLabel("Set " + feature.getName() + " to "
					+ value.toString());
		}

		return command;
	}

	protected Object getValue() {
		return input.eGet(feature);
	}
}

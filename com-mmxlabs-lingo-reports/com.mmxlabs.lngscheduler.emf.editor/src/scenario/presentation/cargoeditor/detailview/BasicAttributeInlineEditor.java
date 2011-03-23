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

import scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
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

	public BasicAttributeInlineEditor(final EMFPath path,
			final EStructuralFeature feature, final EditingDomain editingDomain) {
		this.path = path;
		this.feature = feature;
		this.editingDomain = editingDomain;
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
			updateDisplay(getValue());
		}
	}

	@Override
	public void notifyChanged(final Notification msg) {
		super.notifyChanged(msg);
		// check if msg is relevant
		if (msg.getFeature() != null && msg.getFeature().equals(feature)) {
			// it is a change to our feature
			if (!currentlySettingValue)
				updateDisplay(getValue());
		}
	}

	/**
	 * Subclasses should use this method to display a value
	 */
	protected abstract void updateDisplay(Object value);

	/**
	 * Subclasses should call this method when their value has been changed.
	 * @param value
	 */
	protected void doSetValue(final Object value) {
		if (currentlySettingValue) return; //avoid re-entering
		currentlySettingValue = true;
		final Object currentValue = getValue();
		if (!((currentValue == null && value == null) || ((currentValue != null && value != null) && currentValue
				.equals(value)))) {
			final Command command = createSetCommand(value);
			editingDomain.getCommandStack().execute(command);
		}

		// create set command here.
		currentlySettingValue = false;
	}

	protected Command createSetCommand(final Object value) {
		final Command command = editingDomain.createCommand(SetCommand.class,
				new CommandParameter(input, feature, value));
		((SetCommand) command).setLabel("Set " + feature.getName() + " to "
				+ value == null ? "null " : value.toString());
		editingDomain.getCommandStack().execute(command);
		return command;
	}

	protected Object getValue() {
		return input.eGet(feature);
	}
}

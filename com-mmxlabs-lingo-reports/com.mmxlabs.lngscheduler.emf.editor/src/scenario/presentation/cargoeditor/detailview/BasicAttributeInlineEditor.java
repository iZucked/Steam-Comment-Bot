/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
/**
 * 
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditor;

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

	/**
	 * Object being edited
	 */
	protected EObject input;

	/**
	 * Source object passed into control
	 */
	protected EObject source;

	/**
	 * The path from the {@link #source} to the {@link #input} object.
	 */
	protected final EMFPath path;
	protected final EStructuralFeature feature;
	protected boolean currentlySettingValue = false;

	protected final EditingDomain editingDomain;
	protected final ICommandProcessor commandProcessor;

	/**
	 * {@link ControlDecoration} used to show validation messages.
	 */
	private ControlDecoration decoration;

	/**
	 * Cached reference to the Information icon
	 */
	protected final FieldDecoration decorationInfo = FieldDecorationRegistry
			.getDefault().getFieldDecoration(
					FieldDecorationRegistry.DEC_INFORMATION);

	/**
	 * Cached reference to the Warning icon
	 */
	protected final FieldDecoration decorationWarning = FieldDecorationRegistry
			.getDefault().getFieldDecoration(
					FieldDecorationRegistry.DEC_WARNING);

	/**
	 * Cached reference to the Error icon
	 */
	protected final FieldDecoration decorationError = FieldDecorationRegistry
			.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);

	private IBatchValidator validator;

	public BasicAttributeInlineEditor(final EMFPath path,
			final EStructuralFeature feature,
			final EditingDomain editingDomain,
			final ICommandProcessor commandProcessor) {
		this.path = path;
		this.feature = feature;
		this.editingDomain = editingDomain;
		this.commandProcessor = commandProcessor;

		validator = ModelValidationService.getInstance().newValidator(
				EvaluationMode.BATCH);

		// include live constraints, also, in batch validation
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS,
				true);
		
		// Add filter to only update on changes to this object
		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(IConstraintDescriptor constraint,
					EObject target) {

				return target == source || target == input;
			}
		});
	}

	@Override
	public void setInput(final EObject source) {
		this.source = source;
		final EObject object = (EObject) path.get(source);
		if (input != null) {
			input.eAdapters().remove(this);
		}
		input = object;
		if (input != null) {
			input.eAdapters().add(this);
			doUpdateDisplayWithValue();
		}

		// Set initial validation state
		validate(source);
	}

	private synchronized void doUpdateDisplayWithValue() {
		if (currentlySettingValue) {
			return;
		}
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
		if (currentlySettingValue) {
			return; // avoid re-entering
		}
		currentlySettingValue = true;
		final Object currentValue = getValue();
		if (!((currentValue == null && value == null) || ((currentValue != null && value != null) && currentValue
				.equals(value)))) {
			final Command command = createSetCommand(value);
			commandProcessor.processCommand(command, input, feature);
			// editingDomain.getCommandStack().execute(command);

			// Validate object changes
			validate(value);
		}

		// create set command here.
		currentlySettingValue = false;
	}

	/**
	 * Use the EMF validation service to validate a copy of the {@link #input}
	 * object with the {@link #feature} set to the specified value object. Any
	 * errors are reported via a {@link ControlDecoration}.
	 * 
	 * @param eObject
	 * @param value
	 */
	private void validate(final Object value) {

		// Disable until we work out a better way to do this
//		final IStatus status = validator.validate(source);
//		updateValidation(status);
	}

	private void updateValidation(final IStatus status) {
		if (status.isOK()) {
			// No problems, so hide decoration
			decoration.hide();
		} else {
			// Default severity
			int severity = IStatus.INFO;

			// Builder used to accumulate messages
			final StringBuilder sb = new StringBuilder();

			if (!status.isMultiStatus()) {
				final IConstraintStatus cstatus = (IConstraintStatus) status;
				sb.append(cstatus.getMessage());

				// Is severity worse, then note it
				if (cstatus.getSeverity() > severity) {
					severity = cstatus.getSeverity();
				}

			} else {
				final IStatus[] children = status.getChildren();
				for (final IStatus element : children) {
					final IConstraintStatus cstatus = (IConstraintStatus) element;
					sb.append(cstatus.getMessage());
					sb.append("\n");
					// Is severity worse, then note it
					if (cstatus.getSeverity() > severity) {
						severity = cstatus.getSeverity();
					}
				}
			}

			// Update description text
			decoration.setDescriptionText(sb.toString());

			// Update icon
			switch (severity) {
			case IStatus.INFO:
				decoration.setImage(decorationInfo.getImage());
				break;
			case IStatus.WARNING:
				decoration.setImage(decorationWarning.getImage());
				break;
			case IStatus.ERROR:
			default:
				decoration.setImage(decorationError.getImage());
				break;
			}

			// Show the decoration.
			decoration.show();
		}
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

	public Control wrapControl(final Control c) {
		decoration = new ControlDecoration(c, SWT.LEFT | SWT.TOP);

		// These should be the defaults...
		decoration.setShowHover(true);
		decoration.setShowOnlyOnFocus(false);

		// Set a default image
		decoration.setImage(decorationInfo.getImage());

		// Hide by default
		decoration.hide();

		return c;
	}
}

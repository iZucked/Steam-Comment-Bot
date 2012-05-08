package com.mmxlabs.models.ui.validation;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

/**
 * Abstract {@link EContentAdapter} implementation to run batch validation on various {@link Notification}s. This is an abstract implementation with a method {@link #validationStatus(IStatus)} to
 * notify implementing classes that validation has completed validation and returns the status result.
 */
public abstract class ValidationContentAdapter extends EContentAdapter implements IMMXAdapter {

	private boolean enabled = true;

	private IBatchValidator validator = null;

	private final ValidationHelper helper;

	private IExtraValidationContext extraContext;

	private EObject currentTarget;

	public ValidationContentAdapter(final IExtraValidationContext extraContext) {

		helper = new ValidationHelper();
		this.extraContext = extraContext;
	}

	public abstract void validationStatus(IStatus status);

	public void performValidation() {
		createValidator();

		// Run the validation
		final IStatus status = helper.runValidation(validator, extraContext, Collections.singleton(currentTarget));

		// Notify implementors
		validationStatus(status);

	}

	private void createValidator() {
		if (validator == null) {
			// Set up a batch validation
			validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
			validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
		}
	}

	public void notifyChanged(final Notification notification) {

		if (!isEnabled()) {
			return;
		}

		// Ignore adapter notifications
		if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
			createValidator();

			// Run the validation
			final EObject target = currentTarget == null ? (EObject) notification.getNotifier() : currentTarget;
			final IStatus status = helper.runValidation(validator, extraContext, Collections.singleton(target));

			// Notify implementors
			validationStatus(status);
		}
		super.notifyChanged(notification);
	}

	/**
	 * Set the validation target. This should be an instance of {@link EObject}.
	 * 
	 * @param newInput
	 */
	public void setTarget(final Object newInput) {

		if (newInput instanceof EObject) {
			this.currentTarget = (EObject) newInput;
		} else {
			this.currentTarget = null;
		}
	}

	public IExtraValidationContext getExtraContext() {
		return extraContext;
	}

	/**
	 * Set the {@link IExtraValidationContext} to use during validation
	 * 
	 * @return
	 */
	public void setExtraContext(final IExtraValidationContext extraContext) {
		this.extraContext = extraContext;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void enable() {
		enabled = true;
	}

	@Override
	public void disable() {
		enabled = false;
	}
}

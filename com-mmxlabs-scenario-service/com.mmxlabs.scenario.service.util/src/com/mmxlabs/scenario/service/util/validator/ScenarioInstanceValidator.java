package com.mmxlabs.scenario.service.util.validator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioInstanceValidator extends MMXContentAdapter {

	private ScenarioInstance scenarioInstance;

	private IBatchValidator validator = null;

	private final ValidationHelper helper;

	private IExtraValidationContext extraContext;

	public ScenarioInstanceValidator(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		helper = new ValidationHelper();
		final Object instance = scenarioInstance.getInstance();
		if (instance instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) instance;
			extraContext = new DefaultExtraValidationContext(rootObject);
			for (final MMXSubModel model : rootObject.getSubModels()) {
				model.getSubModelInstance().eAdapters().add(ScenarioInstanceValidator.this);
			}
		}
	}

	public void performValidation() {
		// Run the validation
		if (extraContext != null) {
			final Collection<EObject> modelRoots = new LinkedList<EObject>();
			for (final MMXSubModel subModel : extraContext.getRootObject().getSubModels()) {
				modelRoots.add(subModel.getSubModelInstance());
			}

			createValidator();

			final IStatus status = helper.runValidation(validator, extraContext, modelRoots);

			// Notify implementors
			validationStatus(status);
		}

	}

	private boolean processNotification(final Notification notification) {
		// Ignore adapter notifications
		if (!notification.isTouch()) {
			performValidation();
			return true;
		}
		return false;
	}

	private void validationStatus(final IStatus status) {
		scenarioInstance.getAdapters().put(IStatus.class, status);
		scenarioInstance.setValidationStatusCode(status.getSeverity());
	}

	public void reallyNotifyChanged(final Notification notification) {
		processNotification(notification);
	}

	@Override
	protected void missedNotifications(final List<Notification> notifications) {
		performValidation();
	}

	private void createValidator() {
		if (validator == null) {
			// Set up a batch validation
			validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
			validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
		}
	}
}

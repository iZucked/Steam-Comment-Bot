/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.scenarios;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioInstanceValidator extends MMXContentAdapter {

	private final ScenarioInstance scenarioInstance;

	private IBatchValidator validator = null;

	private final ValidationHelper helper;

	private IExtraValidationContext extraContext;

	private final ScenarioLock validationLock;

	public ScenarioInstanceValidator(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		// Get hold of a reference to the validation lock
		this.validationLock = scenarioInstance.getLock(ScenarioLock.VALIDATION);
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

	public void dispose() {
		final Object instance = scenarioInstance.getInstance();
		if (instance instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) instance;
			for (final MMXSubModel model : rootObject.getSubModels()) {
				model.getSubModelInstance().eAdapters().remove(ScenarioInstanceValidator.this);
			}
		}
		extraContext = null;
	}

	public void performValidation() {
		// Run the validation
		if (extraContext != null) {

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (extraContext == null) {
						return;
					}

					final Collection<EObject> modelRoots = new LinkedList<EObject>();
					final MMXRootObject rootObject = extraContext.getRootObject();
					if (rootObject == null) {
						return;
					}
					for (final MMXSubModel subModel : rootObject.getSubModels()) {
						modelRoots.add(subModel.getSubModelInstance());
					}

					createValidator();

					IStatus status = null;
					// Perform initial validation
					if (claimValidationLock()) {
						try {
							status = helper.runValidation(validator, extraContext, modelRoots);

						} finally {
							releaseValidationLock();
						}
					}
					if (status != null) {
						validationStatus(status);
					}
				}
			});
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

	protected boolean claimValidationLock() {
		return validationLock.claim();
	}

	protected void releaseValidationLock() {
		validationLock.release();
	}
}

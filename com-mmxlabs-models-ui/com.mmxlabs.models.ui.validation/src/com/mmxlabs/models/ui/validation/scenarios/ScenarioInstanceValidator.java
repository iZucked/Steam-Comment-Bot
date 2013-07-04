/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.scenarios;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioInstanceValidator extends MMXContentAdapter {

	private final ScenarioInstance scenarioInstance;

	private IValidationService validationService;

	private IExtraValidationContext extraContext;

	private final ScenarioLock validationLock;

	public ScenarioInstanceValidator(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		// Get hold of a reference to the validation lock
		this.validationLock = scenarioInstance.getLock(ScenarioLock.VALIDATION);
		final Object instance = scenarioInstance.getInstance();
		if (instance instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) instance;
			extraContext = new DefaultExtraValidationContext(rootObject, false);
			rootObject.eAdapters().add(ScenarioInstanceValidator.this);
		}
	}

	public void dispose() {
		final Object instance = scenarioInstance.getInstance();
		if (instance instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) instance;
			rootObject.eAdapters().remove(ScenarioInstanceValidator.this);
		}
		extraContext = null;
	}

	public void performValidation() {
		// Run the validation
		if (validationService != null && extraContext != null) {

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (extraContext == null) {
						return;
					}

					final MMXRootObject rootObject = extraContext.getRootObject();
					if (rootObject == null) {
						return;
					}

					IStatus status = null;
					// Perform initial validation
					if (claimValidationLock()) {
						try {
							if (validationService != null) {
								status = validationService.runValidation(createValidator(), extraContext, Collections.singleton(rootObject));
							}
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

	@Override
	public void reallyNotifyChanged(final Notification notification) {
		processNotification(notification);
	}

	@Override
	protected void missedNotifications(final List<Notification> notifications) {
		performValidation();
	}

	private IBatchValidator createValidator() {
		// Set up a batch validation
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
		return validator;
	}

	protected boolean claimValidationLock() {
		return validationLock.claim();
	}

	protected void releaseValidationLock() {
		validationLock.release();
	}

	public IValidationService getValidationService() {
		return validationService;
	}

	public void bindValidationService(final IValidationService validationService) {
		this.validationService = validationService;
	}

	public void unbindValidationService(final IValidationService validationService) {
		this.validationService = null;
	}
}

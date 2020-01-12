/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.scenarios;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IPostChangeHook;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * The {@link ScenarioInstanceValidatorService} is an {@link IScenarioServiceListener} implementation intended to be registered as a OSGi service. It adds itself a a listener to
 * {@link IScenarioService}s and hooks in to the post load event. It will then attach a {@link ScenarioInstanceValidator} to loaded {@link ScenarioInstance}s.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioInstanceValidatorService implements IPostChangeHook {

	private IValidationService validationService;

	public void start() {
		SSDataManager.Instance.registerChangeHook(this, SSDataManager.PostChangeHookPhase.VALIDATION);
	}

	public void stop() {
		SSDataManager.Instance.removeChangeHook(this, SSDataManager.PostChangeHookPhase.VALIDATION);
	}

	public void bindValidationService(final IValidationService validationService) {
		this.validationService = validationService;
	}

	public void unbindValidationService(final IValidationService validationService) {
		this.validationService = null;
	}

	@Override
	public void changed(@NonNull final ScenarioModelRecord modelRecord) {

		modelRecord.executeWithProvider(scenarioDataProvider -> {

			boolean relaxedValidation = "Period Scenario".equals(modelRecord.getName());

			final IExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation);

			final IValidationService pValidationService = validationService;
			if (pValidationService != null) {
				final IStatus status = pValidationService.runValidation(createValidator(), extraContext, scenarioDataProvider.getScenario());
				if (status != null) {
					modelRecord.setValidationStatus(status);
				}
			}
		});
	}

	private IBatchValidator createValidator() {
		// Set up a batch validation
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
		return validator;
	}
}

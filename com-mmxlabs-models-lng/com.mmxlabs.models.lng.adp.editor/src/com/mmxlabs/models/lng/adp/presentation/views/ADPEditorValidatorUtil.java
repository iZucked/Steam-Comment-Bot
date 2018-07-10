package com.mmxlabs.models.lng.adp.presentation.views;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.impl.Multi;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public final class ADPEditorValidatorUtil {
	public static IBatchValidator createValidator() {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().endsWith(".base")) {
						return true;
					} else if (cat.getId().endsWith(".evaluation")) {
						return true;
					} else if (cat.getId().endsWith(".optimisation")) {
						return true;
					} else if (cat.getId().endsWith(".adp")) {
						return true;
					}
				}

				return false;
			}
		});

		return validator;
	}

	@NonNullByDefault
	public static IStatus mergeWithADPValidation(final IStatus scenarioStatus, final IScenarioDataProvider scenarioDataProvider, final ADPModel adpModel) {
		final MMXRootObject root = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus merged = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, false);
			final IBatchValidator validator = createValidator();
			final IStatus extra = helper.runValidation(validator, extraContext, r -> Collections.emptySet(), root, adpModel);

			if (scenarioStatus.isOK()) {
				return extra;
			} else if (extra.isOK()) {
				return scenarioStatus;
			}

			final IStatus[] list = { scenarioStatus, extra };
			return new Multi("com.mmxlabs.models.lng.adp.editor", Math.max(scenarioStatus.getSeverity(), extra.getSeverity()), list, "Validation");
		});

		return merged;
	}
}

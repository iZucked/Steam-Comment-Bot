/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class LNGScenarioRunnerUtils {

	@NonNull
	public static OptimisationPlan createDefaultOptimisationPlan() {
		final OptimisationPlan optimisationPlan = ScenarioUtils.createDefaultOptimisationPlan();
		assert optimisationPlan != null;

		return createExtendedSettings(optimisationPlan);
	}

	@NonNull
	public static OptimisationPlan createExtendedSettings(@NonNull final OptimisationPlan optimisationPlan) {
		return createExtendedSettings(optimisationPlan, true, true);
	}

	/**
	 * Use the {@link IParameterModesRegistry} to extend the existing settings object.
	 * 
	 * @param optimiserSettings
	 * @return
	 */
	@NonNull
	public static OptimisationPlan createExtendedSettings(@NonNull final OptimisationPlan optimisationPlan, boolean extend, boolean customise) {

		if (!customise && !extend) {
			return optimisationPlan;
		}

		IParameterModesRegistry parameterModesRegistry = null;

		final Activator activator = Activator.getDefault();
		if (activator != null) {
			parameterModesRegistry = activator.getParameterModesRegistry();
		}

		if (parameterModesRegistry != null) {
			if (extend) {
				final Collection<IParameterModeExtender> extenders = parameterModesRegistry.getExtenders();
				if (extenders != null) {
					for (final IParameterModeExtender extender : extenders) {
						extender.extend(optimisationPlan);
					}
				}
			}
			if (customise) {
				final Collection<IParameterModeCustomiser> customisers = parameterModesRegistry.getCustomisers();
				if (customisers != null) {
					for (final IParameterModeCustomiser extender : customisers) {
						extender.customise(optimisationPlan);
					}
				}
			}
		}
		return optimisationPlan;
	}

	@NonNull
	public static ScenarioInstance saveScenarioAsChild(@NonNull final ScenarioInstance originalScenarioInstance, @NonNull final Container target, @NonNull final LNGScenarioModel scenarioModel,
			@NonNull final String newName) throws Exception {

		return saveNewScenario(originalScenarioInstance, target, EcoreUtil.copy(scenarioModel), newName);
	}

	/**
	 * Same as {@link #saveScenarioAsChild(ScenarioInstance, Container, LNGScenarioModel, String)} but without a copy
	 * 
	 * @param originalScenarioInstance
	 * @param target
	 * @param scenarioModel
	 * @param newName
	 * @return
	 * @throws IOException
	 */
	@NonNull
	public static ScenarioInstance saveNewScenario(@NonNull final ScenarioInstance originalScenarioInstance, @NonNull final Container target, @NonNull final LNGScenarioModel scenarioModel,
			@NonNull final String newName) throws Exception {
		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(target);

		return scenarioService.insert(target, scenarioModel, theDupe -> {

			theDupe.setName(newName);

			// Copy across various bits of information
			theDupe.getMetadata().setContentType(originalScenarioInstance.getMetadata().getContentType());
			theDupe.getMetadata().setCreated(originalScenarioInstance.getMetadata().getCreated());
			theDupe.getMetadata().setLastModified(new Date());

			// Copy version context information
			theDupe.setVersionContext(originalScenarioInstance.getVersionContext());
			theDupe.setScenarioVersion(originalScenarioInstance.getScenarioVersion());

			theDupe.setClientVersionContext(originalScenarioInstance.getClientVersionContext());
			theDupe.setClientScenarioVersion(originalScenarioInstance.getClientScenarioVersion());
		});
	}
}

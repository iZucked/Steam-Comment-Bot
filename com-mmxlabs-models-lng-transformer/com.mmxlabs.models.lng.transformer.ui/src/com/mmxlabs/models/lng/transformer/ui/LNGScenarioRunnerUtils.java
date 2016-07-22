/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
			@NonNull final String newName) throws IOException {

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
			@NonNull final String newName) throws IOException {
		final IScenarioService scenarioService = target.getScenarioService();

		final ScenarioInstance dup = scenarioService.insert(target, scenarioModel);
		dup.setName(newName);

		// Copy across various bits of information
		dup.getMetadata().setContentType(originalScenarioInstance.getMetadata().getContentType());
		dup.getMetadata().setCreated(originalScenarioInstance.getMetadata().getCreated());
		dup.getMetadata().setLastModified(new Date());

		// Copy version context information
		dup.setVersionContext(originalScenarioInstance.getVersionContext());
		dup.setScenarioVersion(originalScenarioInstance.getScenarioVersion());

		dup.setClientVersionContext(originalScenarioInstance.getClientVersionContext());
		dup.setClientScenarioVersion(originalScenarioInstance.getClientScenarioVersion());

		return dup;
	}
}

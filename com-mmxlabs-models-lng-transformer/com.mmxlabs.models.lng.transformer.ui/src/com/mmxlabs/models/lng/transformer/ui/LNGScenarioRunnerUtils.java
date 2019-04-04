/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Collection;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
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
	 * Use the {@link IParameterModesRegistry} to extend and customise the existing settings object.
	 * 
	 * Extending adds in e.g. client specific constraints. Customising is intended to change settings such as iterations.
	 * 
	 * @param optimiserSettings
	 * @return
	 */
	@NonNull
	public static OptimisationPlan createExtendedSettings(@NonNull final OptimisationPlan optimisationPlan, final boolean extend, final boolean customise) {

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
	public static ScenarioInstance saveScenarioAsChild(@NonNull final ScenarioInstance originalScenarioInstance, @NonNull final Container target,
			@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final String newName) throws Exception {

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(target);
		return scenarioService.copyInto(target, scenarioDataProvider, newName, new NullProgressMonitor());
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimiserTask;
import com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor.AbstractStartOptimisationEditorActionDelegate;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * Action to start the cloud optimisation The following sequence of actions is assumed: 0. Get the optimisation/optioniser/sandbox settings. 1. Evaluate the scenario. 2. Anonymise the scenario. Create
 * a map of scenario-to-anonymisation-map. 3. Create an encrypted zip archive for scenario, parameters, manifest and jvm options 3a. (Optional) Compute the shasum 4. Upload the archive 4a. Receive the
 * confirmation that archive is uploaded. 5. (Optional; depends on 3a) Upload the shasum. 5a. (Optional; Future dev) Receive the confirmation that shasum is correct. 6. (Optional; Future dev) Query
 * the time remaining. 7. Report to user that opti job is submitted.
 * 
 * @author FM
 *
 */
public class StartCloudOptimisationEditorActionDelegate extends AbstractStartOptimisationEditorActionDelegate {

	@Override
	protected void doRun(ScenarioInstance instance, ScenarioModelRecord modelRecord) {
		OptimiserTask.submit(instance, CloudJobManager.INSTANCE);
	}
}

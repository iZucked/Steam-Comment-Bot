package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.data.distances.DataConstants;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;

public abstract class AbstractSandboxTestCase extends AbstractMicroTestCase {

	// Make sure we have latest data
	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		final IScenarioDataProvider scenarioDataProvider = super.importReferenceData();
		//
		updateDistanceData(scenarioDataProvider, DataConstants.DISTANCES_LATEST_JSON);
		updatePortsData(scenarioDataProvider, DataConstants.PORTS_LATEST_JSON);

		return scenarioDataProvider;
	}

	protected void evaluateSandbox(@NonNull OptionAnalysisModel model) {

		Consumer<IAnalyticsScenarioEvaluator> func = null;
		switch (model.getMode()) {
		case 0:
			func = evaluator -> evaluator.runSandboxOptions(scenarioDataProvider, null, model, model::setResults, false);
			break;
		case 1:
			func = evaluator -> evaluator.runSandboxOptimisation(scenarioDataProvider, null, model, model::setResults, false);
			break;
		case 2:
			func = evaluator -> evaluator.runSandboxInsertion(scenarioDataProvider, null, model, model::setResults, false);
			break;
		}

		Assertions.assertNotNull(func);
		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, func);
	}

}

package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;

public class SwapValueMatrixSandboxEvaluator {

	public static void doValueMatrixSandbox(IScenarioEditingLocation scenarioEditingLocation, @NonNull final SwapValueMatrixModel swapValueMatrixModel) {
		@NonNull
		final Consumer<@NonNull IAnalyticsScenarioEvaluator> f1 = evaluator -> evaluator.evaluateSwapValueMatrixSandbox(scenarioEditingLocation.getScenarioInstance(), swapValueMatrixModel);
		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, f1);
	}
}

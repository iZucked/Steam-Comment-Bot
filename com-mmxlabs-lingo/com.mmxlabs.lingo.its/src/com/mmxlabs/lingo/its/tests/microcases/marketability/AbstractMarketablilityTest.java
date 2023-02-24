package com.mmxlabs.lingo.its.tests.microcases.marketability;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.ui.views.marketability.MarketabilitySandboxEvaluator;

public class AbstractMarketablilityTest extends AbstractMicroTestCase {

	protected void evaluateMarketabilityModel(@NonNull MarketabilityModel marketModel) {
		MarketabilitySandboxEvaluator.evaluate(scenarioDataProvider, null, marketModel, new NullProgressMonitor(), false);

	}

}

package com.mmxlabs.lingo.its.tests.microcases.marketability;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroCaseDateUtils;
import com.mmxlabs.lingo.its.tests.microcases.MicroCaseUtils;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.lingo.its.tests.microcases.sandbox.AbstractSandboxTestCase;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.marketability.MarketabilitySandboxEvaluator;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.types.TimePeriod;

public class AbstractMarketablilityTest extends AbstractMicroTestCase {

	protected void evaluateMarketabilityModel(@NonNull MarketabilityModel marketModel) {
		MarketabilitySandboxEvaluator.evaluate(scenarioDataProvider, null, marketModel, new NullProgressMonitor());

	}

}

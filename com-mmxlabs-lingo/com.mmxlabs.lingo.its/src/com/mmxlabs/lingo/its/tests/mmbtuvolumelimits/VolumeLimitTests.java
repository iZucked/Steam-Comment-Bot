/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.mmbtuvolumelimits;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.uat.suite.cases.UATMultiCargoCase;
import com.mmxlabs.lingo.its.uat.suite.cases.UATTypedCase;
import com.mmxlabs.lingo.its.uat.suite.testers.MultiCargoTester;
import com.mmxlabs.lingo.its.uat.suite.utils.DefaultUATFeatures;
import com.mmxlabs.lingo.its.uat.suite.utils.FeatureBasedUAT;

public class VolumeLimitTests extends AbstractOptimisationResultTester {
	private FeatureBasedUAT features = new DefaultUATFeatures();
	private boolean write = false;

	@Test
	@Category(MicroTest.class)
	public void MMBTU_TESTS() throws Exception {
		String scenarioFilePath = "/scenarios/mmbtu-volumes/mmbtu-tests.lingo";
		UATTypedCase[] cargoes = new UATTypedCase[] { 
				new UATTypedCase(scenarioFilePath, "L-res-fob-des-mmbtu-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-fob-des-mmbtu-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-fob-des-mmbtu-m3", features),
				new UATTypedCase(scenarioFilePath, "L-res-fob-des-mmbtu-m3", features),
				new UATTypedCase(scenarioFilePath, "L-fob-des-m3-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-res-fob-des-m3-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-des-des-m3-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-res-des-des-m3-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-res-des-des-mmbtu-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-des-des-mmbtu-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-des-des-mmbtu-m3", features),
				new UATTypedCase(scenarioFilePath, "L-res-des-des-mmbtu-m3", features),
				new UATTypedCase(scenarioFilePath, "L-fob-fob-m3-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-res-fob-fob-m3-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-res-fob-fob-mmbtu-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-fob-fob-mmbtu-mmbtu", features),
				new UATTypedCase(scenarioFilePath, "L-res-fob-fob-mmbtu-m3", features),
				new UATTypedCase(scenarioFilePath, "L-fob-fob-mmbtu-m3", features),
				// contract tests
				new UATTypedCase(scenarioFilePath, "L-res-fob-des-mmbtu-m3-contract-test", features),
				new UATTypedCase(scenarioFilePath, "L-fob-des-mmbtu-m3-contract-test", features)
		};

		UATMultiCargoCase multiCargoCase = new UATMultiCargoCase(scenarioFilePath, cargoes);
		MultiCargoTester.writeAndCheckProperties(multiCargoCase, write);
	}
	
	@Test
	@Category(MicroTest.class)
	public void MMBTU_SPOT_TESTS() throws Exception {
		String scenarioFilePath = "/scenarios/mmbtu-volumes/mmbtu-spot-tests.lingo";
		UATTypedCase[] cargoes = new UATTypedCase[] { 
				new UATTypedCase(scenarioFilePath, "Spot-Load-SpotWithRestriction", features),
				new UATTypedCase(scenarioFilePath, "Spot-Load2-PortfolioWithRestriction", features),
				new UATTypedCase(scenarioFilePath, "Spot-Load-fob-sale-PortfolioWithRestriction", features),
				new UATTypedCase(scenarioFilePath, "Spot-Load-fob-sale2-SpotWithRestriction", features),
				new UATTypedCase(scenarioFilePath, "Fob-Purchase-2012-04-0-SpotWithRestriction", features),
				new UATTypedCase(scenarioFilePath, "Fob-Purchase-2012-04-1-PortfolioWithRestriction", features),
				new UATTypedCase(scenarioFilePath, "des-purchase-2012-04-0-PortfolioWithRestriction", features),
				new UATTypedCase(scenarioFilePath, "des-purchase-2012-04-1-SpotWithRestriction", features),
		};
		
		UATMultiCargoCase multiCargoCase = new UATMultiCargoCase(scenarioFilePath, cargoes);
		MultiCargoTester.writeAndCheckProperties(multiCargoCase, write);
	}
}
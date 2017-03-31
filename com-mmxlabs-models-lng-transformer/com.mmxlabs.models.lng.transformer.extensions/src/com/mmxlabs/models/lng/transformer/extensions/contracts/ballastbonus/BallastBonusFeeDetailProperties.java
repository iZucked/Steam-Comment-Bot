/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails;
import com.mmxlabs.models.lng.schedule.LumpSumContractDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.AbstractDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class BallastBonusFeeDetailProperties extends AbstractDetailPropertyFactory {

	@Nullable
	@Override
	public
	DetailProperty createProperties(@NonNull EObject eObject) {
		if (eObject instanceof BallastBonusFeeDetails) {
			final BallastBonusFeeDetails details = (BallastBonusFeeDetails) eObject;
			return createTree(details);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull final BallastBonusFeeDetails ballastBonusFeeDetails) {
		final DetailProperty details = PropertiesFactory.eINSTANCE.createDetailProperty();
		{
			details.setName("Ballast Bonus Fee Details");
			addDetailProperty("Fee", "", "$", "", ballastBonusFeeDetails.getFee(), new StringFormatLabelProvider("%,.2f"), details);
			if (ballastBonusFeeDetails.getMatchingBallastBonusContractLine() != null) {
				if (ballastBonusFeeDetails.getMatchingBallastBonusContractLine() instanceof LumpSumContractDetails) {
					LumpSumContractDetails rule = (LumpSumContractDetails) ballastBonusFeeDetails.getMatchingBallastBonusContractLine();
					addDetailProperty("Type", "", "", "", "Lump sum", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Matching port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
				} else if (ballastBonusFeeDetails.getMatchingBallastBonusContractLine() instanceof NotionalJourneyContractDetails) {
					NotionalJourneyContractDetails rule = (NotionalJourneyContractDetails) ballastBonusFeeDetails.getMatchingBallastBonusContractLine();
					addDetailProperty("Type", "", "", "", "Notional journey", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Matching port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Return port", "", "", "", rule.getReturnPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Distance", "", "NM", "", rule.getDistance(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Travel time", "", "days", "", rule.getTotalTimeInDays(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel price", "", "$", "/MT", rule.getFuelPrice(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel used", "", "MT", "", rule.getTotalFuelUsed(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Fuel cost", "", "$", "", rule.getTotalFuelCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire rate", "", "$", "/day", rule.getHireRate(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire cost", "", "$", "", rule.getHireRate(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Route taken", "", "", "", rule.getRouteTaken(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Route cost", "", "$", "", rule.getCanalCost(), new StringFormatLabelProvider("%,d"), details);
				}
			}
		}

		return details;
	}

}

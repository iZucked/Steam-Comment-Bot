/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.LumpSumBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.LumpSumRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.AbstractDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class CharterContractDetailProperties extends AbstractDetailPropertyFactory {

	@Nullable
	@Override
	public DetailProperty createProperties(@NonNull EObject eObject) {
		if (eObject instanceof CharterContractFeeDetails) {
			final CharterContractFeeDetails details = (CharterContractFeeDetails) eObject;
			return createTree(details);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull final CharterContractFeeDetails charterContractFeeDetails) {
		final DetailProperty details = PropertiesFactory.eINSTANCE.createDetailProperty();
		{
			details.setName("Ballast Bonus Fee Details");
			addDetailProperty("Fee", "", "$", "", charterContractFeeDetails.getFee(), new StringFormatLabelProvider("%,d"), details);
			if (charterContractFeeDetails.getMatchingContractDetails() != null) {
				if (charterContractFeeDetails.getMatchingContractDetails() instanceof LumpSumBallastBonusTermDetails) {
					LumpSumBallastBonusTermDetails rule = (LumpSumBallastBonusTermDetails) charterContractFeeDetails.getMatchingContractDetails();
					addDetailProperty("Type", "", "", "", "Lump sum", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Matching port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
				} else if (charterContractFeeDetails.getMatchingContractDetails() instanceof NotionalJourneyBallastBonusTermDetails) {
					NotionalJourneyBallastBonusTermDetails rule = (NotionalJourneyBallastBonusTermDetails) charterContractFeeDetails.getMatchingContractDetails();
					addDetailProperty("Type", "", "", "", "Notional journey", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Lump sum", "", "$", "", rule.getLumpSum(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Matching port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Return port", "", "", "", rule.getReturnPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Distance", "", "NM", "", rule.getDistance(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Travel time", "", "days", "", rule.getTotalTimeInDays(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel price", "", "$", "/MT", rule.getFuelPrice(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel used", "", "MT", "", rule.getTotalFuelUsed(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Fuel cost", "", "$", "", rule.getTotalFuelCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire rate", "", "$", "/day", rule.getHireRate(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire cost", "", "$", "", rule.getHireCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Route taken", "", "", "", rule.getRouteTaken(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Route cost", "", "$", "", rule.getCanalCost(), new StringFormatLabelProvider("%,d"), details);
				} else if (charterContractFeeDetails.getMatchingContractDetails() instanceof OriginPortRepositioningFeeTermDetails) {
					details.setName("Repositioning Fee Details");
					OriginPortRepositioningFeeTermDetails rule = (OriginPortRepositioningFeeTermDetails) charterContractFeeDetails.getMatchingContractDetails();
					addDetailProperty("Type", "", "", "", "Notional journey", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Lump sum", "", "$", "", rule.getLumpSum(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Load port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Origin port", "", "", "", rule.getOriginPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Distance", "", "NM", "", rule.getDistance(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Travel time", "", "days", "", rule.getTotalTimeInDays(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel price", "", "$", "/MT", rule.getFuelPrice(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel used", "", "MT", "", rule.getTotalFuelUsed(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Fuel cost", "", "$", "", rule.getTotalFuelCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire rate", "", "$", "/day", rule.getHireRate(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire cost", "", "$", "", rule.getHireCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Route taken", "", "", "", rule.getRouteTaken(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Route cost", "", "$", "", rule.getCanalCost(), new StringFormatLabelProvider("%,d"), details);
				} else if (charterContractFeeDetails.getMatchingContractDetails() instanceof LumpSumRepositioningFeeTermDetails) {
					details.setName("Repositioning Fee Details");
					LumpSumRepositioningFeeTermDetails rule = (LumpSumRepositioningFeeTermDetails) charterContractFeeDetails.getMatchingContractDetails();
					addDetailProperty("Type", "", "", "", "Lump sum", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Load port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Origin port", "", "", "", rule.getOriginPort(), new StringFormatLabelProvider("%s"), details);
				}
			}
		}

		return details;
	}

}

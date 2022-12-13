/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.charter;

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
		if (eObject instanceof CharterContractFeeDetails details) {
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
				if (charterContractFeeDetails.getMatchingContractDetails() instanceof LumpSumBallastBonusTermDetails rule) {
					addDetailProperty("Type", "", "", "", "Lump sum", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Matching port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
				} else if (charterContractFeeDetails.getMatchingContractDetails() instanceof NotionalJourneyBallastBonusTermDetails rule) {
					addDetailProperty("Type", "", "", "", "Notional journey", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Lump sum", "", "$", "", rule.getLumpSum(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Matching port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Return port", "", "", "", rule.getReturnPort(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Distance", "", "NM", "", rule.getDistance(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Travel time", "", "days", "", rule.getTotalTimeInDays(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel price", "", "$", "/MT", rule.getFuelPrice(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("Fuel used", "", "MT", "", rule.getTotalFuelUsed(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Fuel cost", "", "$", "", rule.getTotalFuelCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("LNG price", "", "$", "/mmBtu", rule.getLngPrice(), new StringFormatLabelProvider("%,.2f"), details);
					addDetailProperty("LNG used", "", "mmBtu", "", rule.getTotalLNGUsed(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("LNG cost", "", "$", "", rule.getTotalLNGCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire rate", "", "$", "/day", rule.getHireRate(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Hire cost", "", "$", "", rule.getHireCost(), new StringFormatLabelProvider("%,d"), details);
					addDetailProperty("Route taken", "", "", "", rule.getRouteTaken(), new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Route cost", "", "$", "", rule.getCanalCost(), new StringFormatLabelProvider("%,d"), details);
				} else if (charterContractFeeDetails.getMatchingContractDetails() instanceof OriginPortRepositioningFeeTermDetails rule) {
					details.setName("Repositioning Fee Details");
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
				} else if (charterContractFeeDetails.getMatchingContractDetails() instanceof LumpSumRepositioningFeeTermDetails rule) {
					details.setName("Repositioning Fee Details");
					addDetailProperty("Type", "", "", "", "Lump sum", new StringFormatLabelProvider("%s"), details);
					addDetailProperty("Load port", "", "", "", rule.getMatchedPort(), new StringFormatLabelProvider("%s"), details);
				}
			}
		}

		return details;
	}

}

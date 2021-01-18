/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV91 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 90;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 91;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EObjectWrapper cargoModel = modelRoot.getRef("cargoModel");

		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");
		final EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		final EObjectWrapper costModel = referenceModel.getRef("costModel");
		final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");

		final List<EObjectWrapper> factors = pricingModel.getRefAsList("conversionFactors");
		if (factors == null) {
			return;
		}

		// Update conversion factor case
		// Correct mwh factor
		boolean swap_MWh_factor = false;
		for (final EObjectWrapper factor : factors) {
			final String fromName = factor.getAttrib("from");
			final String toName = factor.getAttrib("to");

			// Update the case
			if ("mwh".equalsIgnoreCase(fromName)) {
				factor.setAttrib("from", "MWh");
			}
			if ("mwh".equalsIgnoreCase(toName)) {
				factor.setAttrib("to", "MWh");
			}

			// Correct the mwh to mmbtu factor
			if ("mwh".equalsIgnoreCase(fromName) && "mmbtu".equalsIgnoreCase(toName)) {
				final double value = factor.getAttrib("factor");
				if (Math.abs(value - 3.409511) < 0.1) {
					swap_MWh_factor = true;
					factor.setAttrib("factor", 0.293287);
				}

			}
		}

		// For all slots
		updateObjects(cargoModel.getRefAsList("loadSlots"), null, swap_MWh_factor, "priceExpression", "cancellationExpression");
		updateObjects(cargoModel.getRefAsList("dischargeSlots"), null, swap_MWh_factor, "priceExpression", "cancellationExpression");

		// For all contracts
		updateObjects(commercialModel.getRefAsList("purchaseContracts"), "priceInfo", swap_MWh_factor, "cancellationExpression");
		updateObjects(commercialModel.getRefAsList("salesContracts"), "priceInfo", swap_MWh_factor, "cancellationExpression");

		// For all markets
		updateObjects(spotMarketsModel.getRef("desPurchaseSpotMarket").getRefAsList("markets"), "priceInfo", swap_MWh_factor);
		updateObjects(spotMarketsModel.getRef("desSalesSpotMarket").getRefAsList("markets"), "priceInfo", swap_MWh_factor);
		updateObjects(spotMarketsModel.getRef("fobPurchasesSpotMarket").getRefAsList("markets"), "priceInfo", swap_MWh_factor);
		updateObjects(spotMarketsModel.getRef("fobSalesSpotMarket").getRefAsList("markets"), "priceInfo", swap_MWh_factor);

		for (EObjectWrapper idx : pricingModel.getRefAsList("currencyIndices")) {
			EObjectWrapper indexData = idx.getRef("data");
			if (indexData.hasFeature("expression")) {
				update(indexData, "expression", swap_MWh_factor);
			}
		}
		for (EObjectWrapper idx : pricingModel.getRefAsList("commodityIndices")) {
			EObjectWrapper indexData = idx.getRef("data");
			if (indexData.hasFeature("expression")) {
				update(indexData, "expression", swap_MWh_factor);
			}
		}

		// Include other costs? cooldown is a port -> price expression map
		// updateObjects(costModel.getRefAsList("cooldownCosts"), null, swap_MWh_factor, "priceExpression", "cancellationExpression");

		// ALSO :- Update series parsers, autocomplete and CSV

	}

	private void updateObjects(final List<EObjectWrapper> collection, final String checkSubTreeFeature, final boolean swap_MWh_factor, final String... attributes) {
		if (collection == null) {
			return;
		}

		for (final EObjectWrapper object : collection) {
			if (object == null) {
				continue;
			}
			for (final String attribute : attributes) {
				update(object, attribute, swap_MWh_factor);
			}
			if (checkSubTreeFeature != null) {
				// Descend into subtree of contained values
				final EStructuralFeature f = object.eClass().getEStructuralFeature(checkSubTreeFeature);
				if (f instanceof EReference) {
					final EReference eReference = (EReference) f;
					if (eReference.isMany()) {
						updateAnnotatedObjects(object.getRefAsList(checkSubTreeFeature), swap_MWh_factor);
					} else {
						updateAnnotatedObjects(Collections.<EObjectWrapper> singletonList(object.getRef(checkSubTreeFeature)), swap_MWh_factor);
					}
				}
			}
		}
	}

	private void updateAnnotatedObjects(final List<EObjectWrapper> collection, final boolean swap_MWh_factor) {
		if (collection == null) {
			return;
		}

		for (final EObjectWrapper object : collection) {
			if (object == null) {
				continue;
			}
			for (final EStructuralFeature feature : object.eClass().getEAllAttributes()) {
				if (isExpressionFeature(feature)) {
					update(object, feature.getName(), swap_MWh_factor);
				}
			}
			for (final EReference ref : object.eClass().getEAllContainments()) {
				if (ref.isMany()) {
					updateAnnotatedObjects(object.getRefAsList(ref.getName()), swap_MWh_factor);
				} else {
					updateAnnotatedObjects(Collections.<EObjectWrapper> singletonList(object.getRef(ref.getName())), swap_MWh_factor);
				}
			}
		}
	}

	private boolean isExpressionFeature(final EStructuralFeature feature) {
		return feature.getEAnnotation("http://www.mmxlabs.com/models/pricing/expressionType") != null;
	}

	/**
	 * Scan the containment tree for attributes annotated with the price expression annotation.
	 * 
	 * @param object
	 * @param attribute
	 * @param swap_MWh_factor
	 */
	private void update(final EObjectWrapper object, final String attribute, final boolean swap_MWh_factor) {
		String expression = object.getAttrib(attribute);
		if (expression == null || expression.isEmpty()) {
			return;
		}
		final String lower = expression.toLowerCase();
		if (lower.contains("mwhs_per_mmbtu")) {
			expression = expression.replaceAll("(?i)mwhs_per_mmbtu", swap_MWh_factor ? "mmBtu_to_MWh" : "MWh_to_mmBtu");
		}
		if (lower.contains("mmbtus_per_mwh")) {
			expression = expression.replaceAll("(?i)mmbtus_per_mwh", swap_MWh_factor ? "MWh_to_mmBtu" : "mmBtu_to_MWh");
		}
		if (lower.contains("therms_per_mmbtu")) {
			// TOOD: Check pluralisation
			expression = expression.replaceAll("(?i)therms_per_mmbtu", "therm_to_mmBtu");
		}
		if (lower.contains("mmbtus_per_therm")) {
			// TOOD: Check pluralisation
			expression = expression.replaceAll("(?i)mmbtus_per_therm", "mmBtu_to_therm");
		}
		if (lower.contains("bbls_per_mmbtu")) {
			expression = expression.replaceAll("(?i)bbls_per_mmbtu", "bbl_to_mmBtu");
		}
		if (lower.contains("mmbtus_per_bbl")) {
			expression = expression.replaceAll("(?i)mmbtus_per_bbl", "mmBtu_to_bbl");
		}

		object.setAttrib(attribute, expression);
	}
}

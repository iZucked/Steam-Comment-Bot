/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class SuezCanalTariffImporter extends DefaultClassImporter {

	public class ExtraData extends EObjectImpl {

		public ExtraData(EStructuralFeature feature, Object value) {
			this.feature = feature;
			this.value = value;
		}

		EStructuralFeature feature;
		Object value;
	}

	public static final String TUG_COST_KEY = "tugcost";
	public static final String FIXED_COST_KEY = "fixedcosts";
	public static final String DISCOUNT_FACTOR_KEY = "discountfactor";
	public static final String SDR_TO_USD_KEY = "sdrtousd";

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {

		if (row.containsKey(TUG_COST_KEY) && !row.get(TUG_COST_KEY).isEmpty()) {
			return parseField(row, context, TUG_COST_KEY, "tug cost", PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_COST, valueString -> Double.parseDouble(valueString));
		}
		if (row.containsKey(FIXED_COST_KEY) && !row.get(FIXED_COST_KEY).isEmpty()) {
			return parseField(row, context, FIXED_COST_KEY, "fixed cost", PricingPackage.Literals.SUEZ_CANAL_TARIFF__FIXED_COSTS, valueString -> Double.parseDouble(valueString));
		}
		if (row.containsKey(DISCOUNT_FACTOR_KEY) && !row.get(DISCOUNT_FACTOR_KEY).isEmpty()) {
			return parseField(row, context, DISCOUNT_FACTOR_KEY, "discount factor", PricingPackage.Literals.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR, valueString -> Double.parseDouble(valueString));
		}
		if (row.containsKey(SDR_TO_USD_KEY) && !row.get(SDR_TO_USD_KEY).isEmpty()) {
			return parseField(row, context, SDR_TO_USD_KEY, "sdr to usd", PricingPackage.Literals.SUEZ_CANAL_TARIFF__SDR_TO_USD, valueString -> valueString);
		}

		return super.importObject(parent, eClass, row, context);
	}

	private ImportResults parseField(final Map<String, String> row, final IMMXImportContext context, String key, String name, EStructuralFeature feature, Function<String, Object> parser) {
		final String valueString = row.get(key);
		if (valueString != null && !valueString.isEmpty()) {
			try {
				Object value = parser.apply(valueString);
				final ExtraData data = new ExtraData(feature, value);
				return new ImportResults(data);
			} catch (final Exception e) {
				context.createProblem("Unable to parse " + name, true, true, true);
				return new ImportResults(null);
			}
		}
		throw new IllegalStateException();
	}

	public Collection<Map<String, String>> exportTariff(final SuezCanalTariff tariff, final IMMXExportContext context) {
		final Collection<Map<String, String>> exportObjects = new LinkedList<>();
		exportObjects.addAll(exportObjects(tariff.getBands(), context));
		exportObjects.addAll(exportObjects(tariff.getTugBands(), context));

		exportObjects.add(CollectionsUtil.<String, String> makeHashMap(TUG_COST_KEY, String.format("%0,1f", tariff.getTugCost())));
		exportObjects.add(CollectionsUtil.<String, String> makeHashMap(FIXED_COST_KEY, String.format("%0,1f", tariff.getFixedCosts())));
		exportObjects.add(CollectionsUtil.<String, String> makeHashMap(DISCOUNT_FACTOR_KEY, String.format("%0,1f", tariff.getDiscountFactor())));
		exportObjects.add(CollectionsUtil.<String, String> makeHashMap(SDR_TO_USD_KEY, tariff.getSdrToUSD()));

		return exportObjects;

	}
}

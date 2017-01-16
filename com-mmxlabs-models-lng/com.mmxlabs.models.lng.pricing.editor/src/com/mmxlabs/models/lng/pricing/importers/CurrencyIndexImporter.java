/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 * Custom import logic for loading a commodity index.
 * 
 * @author Simon Goodall, achurchill
 * 
 */
public class CurrencyIndexImporter extends AbstractMultipleDataIndexImporter<CurrencyIndex> {

	public CurrencyIndexImporter() {
		TYPE_VALUE = "currency_index";
	}
	
	@Override
	protected CurrencyIndex getResult(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context, boolean isUnified) {
		final CurrencyIndex result = PricingFactory.eINSTANCE.createCurrencyIndex();
		setIndexFromRow(result, row, context, isUnified);
		return result;
	}
	
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 * Custom import logic for loading a commodity index.
 * 
 * @author Simon Goodall, achurchill
 * 
 */
public class CommodityIndexImporter extends AbstractMultipleDataIndexImporter<CommodityIndex> {

	public CommodityIndexImporter() {
		TYPE_VALUE = "commodity_index";
	}
	
	@Override
	protected CommodityIndex getResult(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context, boolean isUnified) {
		final CommodityIndex result = PricingFactory.eINSTANCE.createCommodityIndex();
		setIndexFromRow(result, row, context, isUnified);
		return result;
	}
	
}

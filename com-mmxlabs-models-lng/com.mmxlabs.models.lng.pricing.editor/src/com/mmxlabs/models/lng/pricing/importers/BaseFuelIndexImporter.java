/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 * Custom import logic for loading a base fuel index.
 * 
 * @author Simon McGregor, achurchill
 * 
 */
public class BaseFuelIndexImporter extends AbstractMultipleDataIndexImporter<BaseFuelIndex> {
	private static final String NAME = "name";
	private static final String UNITS = "units";

	public BaseFuelIndexImporter() {
		TYPE_VALUE = "base_fuel_index";
	}
	
	@Override
	protected BaseFuelIndex getResult(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context, boolean isUnified) {
		BaseFuelIndex result = PricingFactory.eINSTANCE.createBaseFuelIndex();
		setIndexFromRow(result, row, context, isUnified);
		return result;
	}

}

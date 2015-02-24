/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

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
	protected BaseFuelIndex getResult(final EObject parent, final EClass targetClass, final Map<String, String> row, final IImportContext context, boolean isUnified) {
		BaseFuelIndex result = PricingFactory.eINSTANCE.createBaseFuelIndex();
		setIndexFromRow(result, row, context, isUnified);
		return result;
	}

}

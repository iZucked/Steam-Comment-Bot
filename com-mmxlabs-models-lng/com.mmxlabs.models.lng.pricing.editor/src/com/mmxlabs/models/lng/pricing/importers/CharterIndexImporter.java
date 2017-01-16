/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 * Custom import logic for loading a {@link CharterIndex}.
 * 
 * @author Simon Goodall
 * 
 */
public class CharterIndexImporter extends AbstractMultipleDataIndexImporter<CharterIndex> {

	public CharterIndexImporter() {
		TYPE_VALUE = "charter_index";
	}

	@Override
	protected CharterIndex getResult(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context, boolean isUnified) {
		final CharterIndex result = PricingFactory.eINSTANCE.createCharterIndex();
		setIndexFromRow(result, row, context, isUnified);
		return result;
	}
	
}

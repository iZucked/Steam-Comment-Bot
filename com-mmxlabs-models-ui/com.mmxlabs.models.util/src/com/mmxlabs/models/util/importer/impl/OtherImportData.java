/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

public class OtherImportData extends EObjectImpl {

	private final EStructuralFeature feature;
	private final Object value;

	public EStructuralFeature getFeature() {
		return feature;
	}

	public Object getValue() {
		return value;
	}

	public OtherImportData(final EStructuralFeature feature, final Object value) {
		this.feature = feature;
		this.value = value;
	}

	public static ImportResults parseField(final Map<String, String> row, final IMMXImportContext context, final String key, final String name, final EStructuralFeature feature,
			final CheckedFunction<String, Object, Exception> parser) {

		final String valueString = row.get(key);
		if (valueString != null && !valueString.isEmpty()) {
			try {
				final Object value = parser.apply(valueString);
				final OtherImportData data = new OtherImportData(feature, value);
				return new ImportResults(data);
			} catch (final Exception e) {
				context.createProblem("Unable to parse " + name, true, true, true);
				return new ImportResults(null);
			}
		}
		throw new IllegalStateException();
	}
}
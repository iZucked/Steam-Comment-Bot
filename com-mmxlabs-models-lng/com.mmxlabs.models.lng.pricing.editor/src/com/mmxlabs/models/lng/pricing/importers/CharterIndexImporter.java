/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

/**
 * Custom import logic for loading a {@link CharterIndex}.
 * 
 * @author Simon Goodall
 * @since 5.0
 * 
 */
public class CharterIndexImporter extends GenericIndexImporter<CharterIndex> {

	private static final String NAME = "name";
	private static final String UNITS = "units";

	@Override
	public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IImportContext context) {
		final CharterIndex result = PricingFactory.eINSTANCE.createCharterIndex();

		if (row.containsKey(NAME)) {
			result.setName(row.get(NAME));
		} else {
			context.addProblem(context.createProblem("CharterIndex name is missing", true, true, true));
		}

		if (row.containsKey(UNITS)) {
			result.setUnits(row.get(UNITS));
		} else {
			context.addProblem(context.createProblem("CharterIndex units is missing", true, true, true));
		}

		final Index<Integer> indexData = importIntIndex(row, Collections.singleton(NAME), context);
		result.setData(indexData);

		context.registerNamedObject(result);

		return new ImportResults(result);
	}

	protected Comparator<String> getFieldNameOrderComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(final String o1, final String o2) {
				// Always sort name column first
				if (NAME.equals(o1) && NAME.equals(o2)) {
					return 0;
				}
				if (NAME.equals(o1)) {
					return -1;
				} else if (NAME.equals(o2)) {
					return 1;
				}

				return o1.compareTo(o2);
			}
		};
	}

	@Override
	protected Map<String, String> getNonDateFields(final CharterIndex target, final Index<? extends Number> index) {
		final Map<String, String> result = super.getNonDateFields(target, index);
		result.put(NAME, target.getName());
		result.put(UNITS, target.getUnits());
		return result;
	}

	@Override
	protected Index<? extends Number> getIndexFromObject(final CharterIndex target) {
		return target.getData();
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IExportContext context) {
		return exportIndices(objects, context, true);
	}
}

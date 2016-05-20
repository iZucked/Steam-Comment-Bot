/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

/**
 * Custom import logic for loading a base fuel index.
 * 
 * @author Simon McGregor
 * 
 */
public abstract class AbstractMultipleDataIndexImporter<TargetClass extends NamedIndexContainer> extends GenericIndexImporter<TargetClass> {
	protected String TYPE_VALUE;
	protected static final String TYPE_KEY = "type_of_index";
	protected static final String NAME = "name";
	protected static final String UNITS = "units";

	@Override
	public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context) {
		final boolean unified = isUnified(row);
		boolean created = false;
		EObject result = null;
		if (unified || !multipleDataTypeInput) {
			result = getResult(parent, targetClass, row, context, unified);
			created = true;
		} else {
			if (!row.containsKey(TYPE_KEY)) {
				context.addProblem(context.createProblem("A multiple type data input CSV must contain a " + TYPE_KEY + " column", true, true, true));
			}
		}

		return new ImportResults(result, created);
	}

	protected boolean isUnified(final Map<String, String> row) {
		if (multipleDataTypeInput && (row.containsKey(TYPE_KEY) && row.get(TYPE_KEY).equals(TYPE_VALUE))) {
			return true;
		} else {
			return false;
		}
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
				// Then Units...
				if (UNITS.equals(o1)) {
					return -1;
				} else if (UNITS.equals(o2)) {
					return 1;
				}
				// Then expression
				if (EXPRESSION.equals(o1)) {
					return -1;
				} else if (EXPRESSION.equals(o2)) {
					return 1;
				}

				return o1.compareTo(o2);
			}
		};
	}

	@Override
	protected Map<String, String> getNonDateFields(final TargetClass target, final Index<? extends Number> index) {
		final Map<String, String> result = new LinkedHashMap<>();
		result.put(NAME, target.getName());
		result.put(UNITS, target.getUnits());
		result.putAll(super.getNonDateFields(target, index));
		return result;
	}

	@Override
	protected Index<? extends Number> getIndexFromObject(final TargetClass target) {
		return target.getData();
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IMMXExportContext context) {
		return exportIndices(objects, context, false);
	}

	protected abstract TargetClass getResult(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context, final boolean isUnified);

	protected void setIndexFromRow(TargetClass result, final Map<String, String> row, final IMMXImportContext context, final boolean isUnified) {
		if (row.containsKey(NAME)) {
			result.setName(row.get(NAME));
		} else {
			context.addProblem(context.createProblem(String.format("Index %s name is missing", TYPE_VALUE), true, true, true));
		}

		if (row.containsKey(UNITS)) {
			result.setUnits(row.get(UNITS));
		} else {
			// context.addProblem(context.createProblem("CommodityIndex units is missing", true, true, true));
		}
		final Index<Double> indexData = importDoubleIndex(row, isUnified ? getIgnoreSet(NAME, TYPE_KEY) : Collections.singleton(NAME), context);
		result.setData(indexData);

		context.registerNamedObject(result);
	}
}

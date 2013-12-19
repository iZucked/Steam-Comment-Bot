/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.dates.DateAttributeImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.AbstractClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

/**
 * Custom import logic for loading a {@link CharterIndex}.
 * 
 * @author Simon Goodall, hinton
 * @since 5.0
 * 
 */
public class CharterIndexImporter extends AbstractClassImporter {
	private static final String NAME = "name";
	private static final String UNITS = "units";
	private static final String EXPRESSION = "expression";
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
	final DateAttributeImporter dateParser = new DateAttributeImporter();



	/**
	 * @since 8.0
	 */
	@Override
	public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IImportContext context) {
		CharterIndex result = PricingFactory.eINSTANCE.createCharterIndex();

		final Index<Integer> indexData;
		if (row.containsKey(EXPRESSION)) {
			if (row.get(EXPRESSION).isEmpty() == false) {
				final DerivedIndex<Integer> di = PricingFactory.eINSTANCE.createDerivedIndex();
				indexData = di;
				di.setExpression(row.get(EXPRESSION));
			} else {
				indexData = PricingFactory.eINSTANCE.createDataIndex();
			}
		} else {
			indexData = PricingFactory.eINSTANCE.createDataIndex();
		}
		result.setData(indexData);

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

		if (indexData instanceof DataIndex) {
			final DataIndex<Integer> data = (DataIndex<Integer>) indexData;
			for (final String s : row.keySet()) {
				try {
					final Date date = dateParser.parseDate(s);
					final Calendar c = Calendar.getInstance();
					c.setTime(date);
					// Set back to start of month
					c.set(Calendar.DAY_OF_MONTH, 1);
					// Clear any other values
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					if (row.get(s).isEmpty())
						continue;
					try {
						final int value = Integer.parseInt(row.get(s));
						final Integer n = value;

						final IndexPoint<Integer> point = PricingFactory.eINSTANCE.createIndexPoint();
						point.setDate(date);
						point.setValue(n);
						data.getPoints().add(point);
					} catch (final NumberFormatException nfe) {
						context.addProblem(context.createProblem("The value " + row.get(s) + " is not a number", true, true, true));
					}
				} catch (final ParseException ex) {
					if (s.equals(NAME) == false && s.equals(EXPRESSION) == false && s.equals(UNITS) == false) {
						context.addProblem(context.createProblem("The field " + s + " is not a date", true, false, true));
					}
				}
			}
		} else {
			for (final String s : row.keySet()) {
				try {
					shortDate.parse(s);
					if (row.get(s).isEmpty() == false) {
						context.addProblem(context.createProblem("Indices with an expression should not have any values set", true, true, true));
					}
				} catch (final ParseException ex) {

				}
			}
		}

		context.registerNamedObject(result);

		return new ImportResults((EObject) result);
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final MMXRootObject root) {
		final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (final EObject obj : objects) {

			CharterIndex charterIndex = (CharterIndex) obj;
			Index<Integer> index = charterIndex.getData();

			if (index instanceof DataIndex) {
				final DataIndex<Integer> i = (DataIndex<Integer>) index;
				// Date sort columns, nut keep "name" column at start
				final Map<String, String> row = new TreeMap<String, String>(new Comparator<String>() {

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
				});
				row.put(NAME, charterIndex.getName());
				row.put(UNITS, charterIndex.getUnits());
				for (final IndexPoint<Integer> pt : i.getPoints()) {
					final Integer n = pt.getValue();
					final Date dt = pt.getDate();
					row.put(shortDate.format(dt), String.format("%d", n));
				}
				result.add(row);
			} else if (obj instanceof DerivedIndex) {
				final DerivedIndex<Integer> derived = (DerivedIndex<Integer>) obj;
				final Map<String, String> row = new LinkedHashMap<String, String>();
				row.put(NAME, charterIndex.getName());
				row.put(UNITS, charterIndex.getUnits());
				row.put(EXPRESSION, derived.getExpression());
				result.add(row);
			}
		}
		return result;
	}
}

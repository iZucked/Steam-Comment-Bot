/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.dates.DateAttributeImporter;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

/**
 * Custom import logic for loading a commodity index.
 * 
 * @author Simon Goodall, hinton
 * @since 5.0
 * 
 */
public class CommodityIndexImporter implements IClassImporter {
	private static final String NAME = "name";
	private static final String EXPRESSION = "expression";
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
	final DateAttributeImporter dateParser = new DateAttributeImporter();

	@Override
	public Collection<EObject> importObjects(final EClass targetClass, final CSVReader reader, final IImportContext context) {
		final List<EObject> result = new LinkedList<EObject>();

		Map<String, String> row;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow(true))) {
				result.addAll(importObject(null, targetClass, row, context).createdObjects);
			}
		} catch (final IOException e) {
			context.addProblem(context.createProblem("IO Error " + e.getMessage(), true, true, false));
		} finally {
			context.popReader();
		}

		return result;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IImportContext context) {
		CommodityIndex result = PricingFactory.eINSTANCE.createCommodityIndex();
		final Index<Double> indexData;
		if (row.containsKey(EXPRESSION)) {
			if (row.get(EXPRESSION).isEmpty() == false) {
				final DerivedIndex<Double> di = PricingFactory.eINSTANCE.createDerivedIndex();
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
			context.addProblem(context.createProblem("CommodityIndex name is missing", true, true, true));
		}

		if (indexData instanceof DataIndex) {
			final DataIndex<Double> data = (DataIndex<Double>) indexData;
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

						final double value = Double.parseDouble(row.get(s));
						final Double n = value;

						final IndexPoint<Double> point = PricingFactory.eINSTANCE.createIndexPoint();
						point.setDate(date);
						point.setValue(n);
						data.getPoints().add(point);
					} catch (final NumberFormatException nfe) {
						context.addProblem(context.createProblem("The value " + row.get(s) + " is not a number", true, true, true));
					}
				} catch (final ParseException ex) {
					if (s.equals(NAME) == false && s.equals(EXPRESSION) == false) {
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

			CommodityIndex commodityIndex = (CommodityIndex) obj;
			Index<Double> index = commodityIndex.getData();

			if (index instanceof DataIndex) {
				final DataIndex<Double> i = (DataIndex<Double>) index;
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
				row.put(NAME, commodityIndex.getName());
				for (final IndexPoint<Double> pt : i.getPoints()) {
					final Double n = pt.getValue();
					final Date dt = pt.getDate();
					row.put(shortDate.format(dt), n.toString());
				}
				result.add(row);
			} else if (index instanceof DerivedIndex) {
				final DerivedIndex<Double> derived = (DerivedIndex<Double>) index;
				final Map<String, String> row = new LinkedHashMap<String, String>();
				row.put(NAME, commodityIndex.getName());
				row.put(EXPRESSION, derived.getExpression());
				result.add(row);
			}
		}
		return result;
	}
}

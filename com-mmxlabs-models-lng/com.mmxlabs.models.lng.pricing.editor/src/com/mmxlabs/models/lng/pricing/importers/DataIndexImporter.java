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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

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
 * Custom import logic for loading a data index.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public class DataIndexImporter implements IClassImporter {
	private static final String EXPRESSION = "expression";
	boolean parseAsInt = false;
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
	final DateAttributeImporter dateParser = new DateAttributeImporter();

	/**
	 * @return the parseAsInt
	 */
	public boolean isParseAsInt() {
		return parseAsInt;
	}

	/**
	 * @param parseAsInt
	 *            the parseAsInt to set
	 */
	public void setParseAsInt(final boolean parseAsInt) {
		this.parseAsInt = parseAsInt;
	}

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
		final Index<Number> result;
		if (row.containsKey(EXPRESSION)) {
			if (row.get(EXPRESSION).isEmpty() == false) {
				final DerivedIndex<Number> di = PricingFactory.eINSTANCE.createDerivedIndex();
				result = di;
				di.setExpression(row.get(EXPRESSION));
			} else {
				result = PricingFactory.eINSTANCE.createDataIndex();
			}
		} else {
			result = PricingFactory.eINSTANCE.createDataIndex();
		}

		if (result instanceof DataIndex) {
			final DataIndex<Number> data = (DataIndex<Number>) result;
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
						final Number n;
						// This used to be a ? : statement, but for some reason the int (or even Integer) was always stored as a Double
						// @see http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.25
						// @see http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
						if (parseAsInt) {
							final int value = Integer.parseInt(row.get(s));
							n = value;
						} else {
							final double value = Double.parseDouble(row.get(s));
							n = value;
						}

						final IndexPoint<Number> point = PricingFactory.eINSTANCE.createIndexPoint();
						point.setDate(date);
						point.setValue(n);
						data.getPoints().add(point);
					} catch (final NumberFormatException nfe) {
						context.addProblem(context.createProblem("The value " + row.get(s) + " is not a number", true, true, true));
					}
				} catch (final ParseException ex) {
					if (s.equals(EXPRESSION) == false) {
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

		return new ImportResults((EObject) result);
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final MMXRootObject root) {
		final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (final EObject o : objects) {
			if (o instanceof DataIndex) {
				final DataIndex<Number> i = (DataIndex) o;
				// Date sort columns
				final Map<String, String> row = new TreeMap<String, String>(new Comparator<String>() {

					@Override
					public int compare(final String o1, final String o2) {

						return o1.compareTo(o2);
					}
				});
				for (final IndexPoint<Number> pt : i.getPoints()) {
					final Number n = pt.getValue();
					final Date dt = pt.getDate();
					if (n instanceof Integer) {
						row.put(shortDate.format(dt), String.format("%d", n));
					} else {
						row.put(shortDate.format(dt), n.toString());
					}
				}
				result.add(row);
			} else if (o instanceof DerivedIndex) {
				final DerivedIndex<Number> derived = (DerivedIndex<Number>) o;
				final Map<String, String> row = new LinkedHashMap<String, String>();
				row.put(EXPRESSION, derived.getExpression());
				result.add(row);
			}
		}
		return result;
	}
}

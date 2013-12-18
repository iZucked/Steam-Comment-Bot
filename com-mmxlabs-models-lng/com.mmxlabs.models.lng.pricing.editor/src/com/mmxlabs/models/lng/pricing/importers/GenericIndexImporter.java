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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

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
 * Generic import logic for loading index data. 
 * Currently implemented by BaseFuelIndexImporter.
 * TODO: change CharterIndexImporter and CommodityIndexImporter to implement this class. 
 * 
 * @author Simon McGregor
 * @since 5.0
 * 
 */
abstract public class GenericIndexImporter<TargetClass> extends AbstractClassImporter {
	protected static final String EXPRESSION = "expression";
	protected static final String UNITS = "units";
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
	final DateAttributeImporter dateParser = new DateAttributeImporter();
	
	@SuppressWarnings("unchecked")
	protected Index<Double> importDoubleIndex(@NonNull final Map<String, String> row, @NonNull final Set<String> columnsToIgnore,@NonNull final IImportContext context) {
		return (Index<Double>) importIndex(false, row, columnsToIgnore, context);	
	}
	
	@SuppressWarnings("unchecked")
	protected Index<Integer> importIntIndex(@NonNull final Map<String, String> row, @NonNull final Set<String> columnsToIgnore,@NonNull  final IImportContext context) {
		return (Index<Integer>) importIndex(true, row, columnsToIgnore, context);	
	}
	
	protected Index<? extends Number> importIndex(boolean parseAsInt, @NonNull final Map<String, String> row, @NonNull final Set<String> columnsToIgnore, @NonNull final IImportContext context) {
		// for expression indices, return a derived index
		if (row.containsKey(EXPRESSION) && row.get(EXPRESSION).isEmpty() == false) {
			final DerivedIndex<Number> di = PricingFactory.eINSTANCE.createDerivedIndex();
			di.setExpression(row.get(EXPRESSION));
			for (final String s : row.keySet()) {
				try {
					shortDate.parse(s);
					if (row.get(s).isEmpty() == false) {
						context.addProblem(context.createProblem("Indices with an expression should not have any values set", true, true, true));
					}
				} catch (final ParseException ex) {

				}
			}
			return di;
		}   
		
		// for other indices, return a data index		
		final DataIndex<Number> result = PricingFactory.eINSTANCE.createDataIndex();

		if (result instanceof DataIndex) {
			final DataIndex<Number> data = (DataIndex<Number>) result;
			for (final String s : row.keySet()) {
				if (columnsToIgnore.contains(s)) {
					continue;
				}
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
					if (s.equals(EXPRESSION) == false && s.equals(UNITS) == false) {
						context.addProblem(context.createProblem("The field " + s + " is not a date", true, false, true));
					}
				}
			}
		}
		
		return result;
	}

	@Override
	abstract public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IImportContext context);
	
	protected Comparator<String> getFieldNameOrderComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				return arg0.compareTo(arg1);
			}			
		};
	}
	
	abstract protected Index<? extends Number> getIndexFromObject(TargetClass target);
	 
	protected Map<String, String>  getNonDateFields(TargetClass target, Index<? extends Number> index) {
		final HashMap<String, String> result = new HashMap<String,String>();
		if (index instanceof DerivedIndex) {
			result.put(EXPRESSION, ((DerivedIndex<? extends Number>) index).getExpression());
		}
		return result;
	}
	
	protected Map<String, String> getDateFields(Index<? extends Number> index) {
		Map<String, String> map = new HashMap<String, String>();
		
		if (index instanceof DataIndex) {
			final DataIndex<? extends Number> di = (DataIndex<? extends Number>) index;
			for (final IndexPoint<? extends Number> pt : di.getPoints()) {
				map.put(shortDate.format(pt.getDate()), pt.getValue().toString());
			}		
		}
		
		return map;
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final MMXRootObject root) {
		final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (final EObject obj : objects) {

			Index<? extends Number> index = getIndexFromObject((TargetClass) obj);

			if (index instanceof DataIndex) {
				final Map<String, String> row = new TreeMap<String, String>(getFieldNameOrderComparator());
				row.putAll(getNonDateFields((TargetClass) obj, index));
				row.putAll(getDateFields((DataIndex<? extends Number>) index));
				result.add(row);
			} else if (index instanceof DerivedIndex) {
				final Map<String, String> row = new LinkedHashMap<String, String>();
				row.putAll(getNonDateFields((TargetClass) obj, index));
				result.add(row);
			}
		}
		return result;
	}
}

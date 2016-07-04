/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.text.ParseException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.datetime.importers.YearMonthAttributeImporter;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.AbstractClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;
import com.mmxlabs.models.util.importer.impl.NumberAttributeImporter;

/**
 * Generic import logic for loading index data. Currently implemented by BaseFuelIndexImporter, CharterIndexImporter and CommodityIndexImporter.
 * 
 * @author Simon McGregor
 * 
 */
abstract public class GenericIndexImporter<TargetClass> extends AbstractClassImporter {
	protected static final String EXPRESSION = "expression";
	protected static final String UNITS = "units";

	protected final YearMonthAttributeImporter dateParser = new YearMonthAttributeImporter();
	protected final LocalDateAttributeImporter dateParser2 = new LocalDateAttributeImporter();

	protected boolean multipleDataTypeInput = false;

	protected NumberImporterHelper numberImporterHelper = new NumberImporterHelper();

	@SuppressWarnings("unchecked")
	protected Index<Double> importDoubleIndex(@NonNull final Map<String, String> row, @NonNull final Set<String> columnsToIgnore, @NonNull final IImportContext context) {
		return (Index<Double>) importIndex(false, row, columnsToIgnore, context);
	}

	@SuppressWarnings("unchecked")
	protected Index<Integer> importIntIndex(@NonNull final Map<String, String> row, @NonNull final Set<String> columnsToIgnore, @NonNull final IImportContext context) {
		return (Index<Integer>) importIndex(true, row, columnsToIgnore, context);
	}

	/**
	 * Imports an Index object from a CSV row (represented as a String->String map of fields).
	 * 
	 * @param parseAsInt
	 *            Whether to parse the value fields as integers (false => parse as double).
	 * @param row
	 * @param columnsToIgnore
	 *            Any columns to explicitly ignore.
	 * @param context
	 *            The import context.
	 * @return The imported Index object.
	 */
	protected Index<? extends Number> importIndex(final boolean parseAsInt, @NonNull final Map<String, String> row, @NonNull final Set<String> columnsToIgnore, @NonNull final IImportContext context) {
		// for expression indices, return a derived index
		if (row.containsKey(EXPRESSION) && row.get(EXPRESSION).isEmpty() == false) {
			final DerivedIndex<Number> di = PricingFactory.eINSTANCE.createDerivedIndex();
			di.setExpression(row.get(EXPRESSION));
			for (final String s : row.keySet()) {
				try {
					dateParser.parseYearMonth(s);
					if (row.get(s).isEmpty() == false) {
						context.addProblem(context.createProblem("Indices with an expression should not have any values set", true, true, true));
					}
				} catch (final ParseException ex) {

				}
			}
			return di;
		}

		// final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		// for other indices, return a data index
		final DataIndex<Number> result = PricingFactory.eINSTANCE.createDataIndex();

		if (result instanceof DataIndex) {
			final DataIndex<Number> data = (DataIndex<Number>) result;
			Set<YearMonth> seenDates = new HashSet<>();

			for (final String s : row.keySet()) {
				if (columnsToIgnore.contains(s)) {
					continue;
				}
				if ("units".equals(s)) {
					continue;
				}
				final YearMonth date;
				try {
					date = parseDateString(s);
				} catch (final ParseException ex) {
					if (s.equals(EXPRESSION) == false && s.equals(UNITS) == false) {
						context.addProblem(context.createProblem("The field " + s + " is not a date", true, false, true));
					}
					continue;
				}
				if (date == null) {
					continue;
				}
				final String valueStr = row.get(s);
				if (valueStr.isEmpty())
					continue;
				try {
					final Number n = numberImporterHelper.parseNumberString(valueStr, parseAsInt);
					if (n == null) {
						continue;
					}

					if (!seenDates.add(date)) {
						context.addProblem(context.createProblem("The month " + s + " is defined multiple times", true, true, true));
						continue;
					}

					final IndexPoint<Number> point = PricingFactory.eINSTANCE.createIndexPoint();
					point.setDate(date);
					point.setValue(n);
					data.getPoints().add(point);
				} catch (final NumberFormatException | ParseException nfe) {
					context.addProblem(context.createProblem("The value " + valueStr + " is not a number", true, true, true));
				}
				// } catch (final ParseException ex) {
				// if (s.equals(EXPRESSION) == false && s.equals(UNITS) == false) {
				// context.addProblem(context.createProblem("The field " + s + " is not a date", true, false, true));
				// }
				// }
			}
		}

		return result;
	}

	@Override
	abstract public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context);

	protected Comparator<String> getFieldNameOrderComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(final String arg0, final String arg1) {
				if (UNITS.equals(arg0)) {
					return -1;
				}
				if (EXPRESSION.equals(arg0)) {
					return -1;
				}

				if (EXPRESSION.equals(arg1)) {
					return 1;
				}

				if (UNITS.equals(arg1)) {
					return 1;
				}
				return arg0.compareTo(arg1);
			}
		};
	}

	/**
	 * Return the index contained in a particular target EObject.
	 * 
	 * @param target
	 * @return
	 */
	abstract protected Index<? extends Number> getIndexFromObject(TargetClass target);

	protected Map<String, String> getNonDateFields(final TargetClass target, final Index<? extends Number> index) {
		final HashMap<String, String> result = new HashMap<String, String>();
		if (index instanceof DerivedIndex) {
			result.put(EXPRESSION, ((DerivedIndex<? extends Number>) index).getExpression());
		}
		return result;
	}

	protected Map<String, String> getDateFields(final IExportContext context, final Index<? extends Number> index, final boolean exportAsInt) {
		final Map<String, String> map = new HashMap<String, String>();
		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		if (index instanceof DataIndex) {
			final DataIndex<? extends Number> di = (DataIndex<? extends Number>) index;
			for (final IndexPoint<? extends Number> pt : di.getPoints()) {
				String value;
				if (exportAsInt) {
					value = nai.intToString(pt.getValue().intValue(), PricingPackage.Literals.INDEX_POINT__VALUE);
				} else {
					value = nai.doubleToString(pt.getValue().doubleValue(), PricingPackage.Literals.INDEX_POINT__VALUE);
				}
				map.put(dateParser.formatYearMonth(pt.getDate()), value);
			}
		}

		return map;
	}

	@Nullable
	protected YearMonth parseDateString(final String s) throws ParseException {
		try {
			return dateParser.parseYearMonth(s);
		} catch (final Exception e) {
			return YearMonth.of(dateParser2.parseLocalDate(s).getYear(), dateParser2.parseLocalDate(s).getMonthValue());
		}
	}

	protected Collection<Map<String, String>> exportIndices(final Collection<? extends EObject> objects, final IExportContext context, final boolean exportAsInt) {

		final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (final EObject obj : objects) {

			final Index<? extends Number> index = getIndexFromObject((TargetClass) obj);

			if (index instanceof DataIndex) {
				final Map<String, String> row = new LinkedHashMap<>();
				row.putAll(getNonDateFields((TargetClass) obj, index));
				// Sorted dates
				final Map<String, String> dateFields = new TreeMap<String, String>(getFieldNameOrderComparator());
				dateFields.putAll(getDateFields(context, (DataIndex<? extends Number>) index, exportAsInt));
				// Convert to linked hash map now data is sorted.
				row.putAll(dateFields);
				result.add(new LinkedHashMap<>(row));
			} else if (index instanceof DerivedIndex) {
				final Map<String, String> row = new LinkedHashMap<String, String>();
				row.putAll(getNonDateFields((TargetClass) obj, index));
				result.add(row);
			}
		}
		return result;
	}

	/*
	 * protected Class<?> getIndexDataType(EClass clazz) { EList<EGenericType> gens = clazz.getEGenericSuperTypes(); for (EGenericType gen: gens) { EList<EGenericType> args = gen.getETypeArguments();
	 * for (EGenericType arg: args) { EClassifier t = arg.getERawType(); Class<?> c = t.getInstanceClass(); c.getName(); } } }
	 */

	protected Set<String> getIgnoreSet(String... ignore) {
		HashSet<String> ignoredSet = new HashSet<String>();
		for (String i : ignore) {
			ignoredSet.add(i);
		}
		return ignoredSet;
	}

	public void setMultipleDataTypeInput(final boolean isMultiple) {
		this.multipleDataTypeInput = isMultiple;
	}

}

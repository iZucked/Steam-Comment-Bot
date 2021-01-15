/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.datetime.importers.YearMonthAttributeImporter;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.IMMXExportContext;
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
public abstract class GenericIndexImporter<T extends AbstractYearMonthCurve> extends AbstractClassImporter {
	protected String TYPE_VALUE;
	protected static final String TYPE_KEY = "type_of_index";
	protected static final String NAME = "name";

	protected static final String EXPRESSION = "expression";
	protected static final String UNITS = "units";
	protected static final String CURRENCY_UNITS = "currency_units";
	protected static final String VOLUME_UNITS = "volume_units";
	protected static final String MARKET_INDEX = "market_index";

	protected final YearMonthAttributeImporter dateParser = new YearMonthAttributeImporter();
	protected final LocalDateAttributeImporter dateParser2 = new LocalDateAttributeImporter();

	protected boolean multipleDataTypeInput = false;

	protected NumberImporterHelper numberImporterHelper = new NumberImporterHelper();

	protected abstract T createCurve();

	@Override
	public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context) {
		final boolean unified = isUnified(row);
		boolean created = false;
		EObject result = null;
		if (unified || !multipleDataTypeInput) {
			T curve = createCurve();
			importCurveRow(curve, row, context);
			result = curve;
			created = true;
		} else {
			if (!row.containsKey(TYPE_KEY)) {
				context.addProblem(context.createProblem("A multiple type data input CSV must contain a " + TYPE_KEY + " column", true, true, true));
			}
		}

		return new ImportResults(result, created);
	}

	private boolean isUnified(final Map<String, String> row) {
		return (multipleDataTypeInput && (row.containsKey(TYPE_KEY) && row.get(TYPE_KEY).equals(TYPE_VALUE)));
	}

	protected Map<String, String> getNonDateFields(final @NonNull AbstractYearMonthCurve target) {
		final Map<String, String> result = new LinkedHashMap<>();
		result.put(NAME, target.getName());
		result.put(VOLUME_UNITS, target.getVolumeUnit());
		result.put(CURRENCY_UNITS, target.getCurrencyUnit());
		result.put(EXPRESSION, target.getExpression());
		result.put(TYPE_KEY, TYPE_VALUE);

		if (target instanceof CommodityCurve) {
			final CommodityCurve cc = (CommodityCurve) target;
			if (cc.getMarketIndex() == null) return result;
			result.put(MARKET_INDEX, cc.getMarketIndex().getName());
		}

		return result;
	}

	protected Map<String, String> getDateFields(final IExportContext context, final AbstractYearMonthCurve curve) {
		final Map<String, String> map = new HashMap<>();
		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		for (final YearMonthPoint pt : curve.getPoints()) {
			String value = nai.doubleToString(pt.getValue(), PricingPackage.Literals.YEAR_MONTH_POINT__VALUE);
			map.put(dateParser.formatYearMonth(pt.getDate()), value);
		}

		return map;
	}

	@Nullable
	private YearMonth parseDateString(final String s) throws ParseException {
		try {
			return dateParser.parseYearMonth(s);
		} catch (final Exception e) {
			return YearMonth.of(dateParser2.parseLocalDate(s).getYear(), dateParser2.parseLocalDate(s).getMonthValue());
		}
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IMMXExportContext context) {
		return exportCurves(objects, context, false);
	}

	private Collection<Map<String, String>> exportCurves(final Collection<? extends EObject> objects, final IExportContext context, final boolean exportAsInt) {

		final List<Map<String, String>> result = new ArrayList<>();
		for (final EObject obj : objects) {

			if (obj instanceof AbstractYearMonthCurve) {
				AbstractYearMonthCurve curve = (AbstractYearMonthCurve) obj;
				final Map<String, String> row = new LinkedHashMap<>();
				row.putAll(getNonDateFields(curve));
				// Sorted dates
				final Map<String, String> dateFields = new TreeMap<>(getFieldNameOrderComparator());
				dateFields.putAll(getDateFields(context, curve));
				// Convert to linked hash map now data is sorted.
				row.putAll(dateFields);
				result.add(new LinkedHashMap<>(row));
			}
		}
		return result;
	}

	private Set<String> getIgnoreSet(String... ignore) {
		HashSet<String> ignoredSet = new HashSet<>();
		for (String i : ignore) {
			ignoredSet.add(i);
		}
		return ignoredSet;
	}

	public void setMultipleDataTypeInput(final boolean isMultiple) {
		this.multipleDataTypeInput = isMultiple;
	}

	private void importCurveRow(@NonNull T result, final Map<String, String> row, final IMMXImportContext context) {
		if (row.containsKey(NAME)) {
			result.setName(row.get(NAME));
		} else {
			context.addProblem(context.createProblem(String.format("Index %s name is missing", TYPE_VALUE), true, true, true));
		}

		if (row.containsKey(UNITS)) {
			String units = row.get(UNITS);
			int idx = units.indexOf('/');
			if (idx >= 0) {
				String currency = units.substring(0, idx);
				String volume = units.substring(idx + 1);
				result.setCurrencyUnit(currency);
				result.setVolumeUnit(volume);
			}
		}
		if (row.containsKey(VOLUME_UNITS)) {
			result.setVolumeUnit(row.get(VOLUME_UNITS));
		}
		if (row.containsKey(CURRENCY_UNITS)) {
			result.setCurrencyUnit(row.get(CURRENCY_UNITS));
		}
		if (row.containsKey(EXPRESSION) && row.get(EXPRESSION).isEmpty() == false) {
			result.setExpression(row.get(EXPRESSION));
		} else {

			result.getPoints().addAll(importDatePoints(row, getIgnoreSet(NAME, TYPE_KEY, MARKET_INDEX, UNITS, VOLUME_UNITS, CURRENCY_UNITS, EXPRESSION), context));
		}
		if (row.containsKey(MARKET_INDEX)) {
			if (result instanceof CommodityCurve) {
				context.doLater(new IDeferment() {

					@Override
					public void run(@NonNull IImportContext importContext) {
						final CommodityCurve ci = (CommodityCurve) result;
						final IMMXImportContext context = (IMMXImportContext) importContext;
						final NamedObject genericMarketIndex = context.getNamedObject(row.get(MARKET_INDEX), PricingPackage.eINSTANCE.getMarketIndex());
						if (genericMarketIndex == null) return;
						final MarketIndex mi = (MarketIndex) genericMarketIndex;
						ci.setMarketIndex(mi);
					}

					@Override
					public int getStage() {
						return IMMXImportContext.STAGE_RESOLVE_CROSSREFERENCES;
					}
				});
			}
		}

		context.registerNamedObject(result);
	}

	private List<YearMonthPoint> importDatePoints(Map<String, String> row, Set<String> columnsToIgnore, IMMXImportContext context) {
		Set<YearMonth> seenDates = new HashSet<>();
		List<YearMonthPoint> points = new LinkedList<>();
		for (final String s : row.keySet()) {
			if (columnsToIgnore.contains(s)) {
				continue;
			}

			final YearMonth date;
			try {
				date = parseDateString(s);
			} catch (final ParseException ex) {
				context.addProblem(context.createProblem("The field " + s + " is not a date", true, false, true));
				continue;
			}
			if (date == null) {
				continue;
			}
			final String valueStr = row.get(s);
			if (valueStr.isEmpty())
				continue;
			try {
				final Number n = numberImporterHelper.parseNumberString(valueStr, false);
				if (n == null) {
					continue;
				}

				if (!seenDates.add(date)) {
					context.addProblem(context.createProblem("The month " + s + " is defined multiple times", true, true, true));
					continue;
				}

				final YearMonthPoint point = PricingFactory.eINSTANCE.createYearMonthPoint();
				point.setDate(date);
				point.setValue(n.doubleValue());
				points.add(point);
			} catch (final NumberFormatException | ParseException nfe) {
				context.addProblem(context.createProblem("The value " + valueStr + " is not a number", true, true, true));
			}
		}
		return points;
	}

	private Comparator<String> getFieldNameOrderComparator() {
		return (o1, o2) -> {
			// Always sort name column first
			if (NAME.equals(o1) && NAME.equals(o2)) {
				return 0;
			}
			if (NAME.equals(o1)) {
				return -1;
			} else if (NAME.equals(o2)) {
				return 1;
			}
			// Then type
			if (TYPE_KEY.equals(o1)) {
				return -1;
			} else if (TYPE_KEY.equals(o2)) {
				return 1;
			}
			// Then Units...
			if (UNITS.equals(o1)) {
				return -1;
			} else if (UNITS.equals(o2)) {
				return 1;
			}
			// Then Units...
			if (CURRENCY_UNITS.equals(o1)) {
				return -1;
			} else if (CURRENCY_UNITS.equals(o2)) {
				return 1;
			}
			// Then Units...
			if (VOLUME_UNITS.equals(o1)) {
				return -1;
			} else if (VOLUME_UNITS.equals(o2)) {
				return 1;
			}
			// Then expression
			if (EXPRESSION.equals(o1)) {
				return -1;
			} else if (EXPRESSION.equals(o2)) {
				return 1;
			}

			return o1.compareTo(o2);
		};
	}
}

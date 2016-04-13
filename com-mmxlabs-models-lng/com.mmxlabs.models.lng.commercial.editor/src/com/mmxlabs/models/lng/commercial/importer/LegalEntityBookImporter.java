/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @author Simon McGregor
 */
public class LegalEntityBookImporter extends DefaultClassImporter {

	static final String ENTITY_KEY = "entity";
	static final String TYPE_KEY = "type";
	static final String TYPE_SHIPPING = "shipping";
	static final String TYPE_TRADES = "trading";
	static final String TYPE_UPSTREAM = "upstream";
	final LocalDateAttributeImporter dateParser = new LocalDateAttributeImporter();

	@Override
	public ImportResults importObject(final EObject parent, final EClass targetClass, final Map<String, String> row, final IMMXImportContext context) {

		// use the default importer to import the object's usual attributes
		final ImportResults result = super.importObject(parent, targetClass, row, context);

		final BaseEntityBook entityBook = (BaseEntityBook) result.importedObject;

		// now read the tax rates out of the CSV data row
		final LinkedList<TaxRate> rates = new LinkedList<TaxRate>();
		for (final String key : row.keySet()) {
			if ("kind".equalsIgnoreCase(key)) {
				continue;
			}
			if ("entity".equalsIgnoreCase(key)) {
				continue;
			}
			if ("type".equalsIgnoreCase(key)) {
				continue;
			}
			final String value = row.get(key);
			if (value.equals("")) {
				continue;
			}
			try {
				final LocalDate date = dateParser.parseLocalDate(key);
				final TaxRate taxRate = CommercialFactory.eINSTANCE.createTaxRate();
				taxRate.setDate(date);
				taxRate.setValue(Float.parseFloat(row.get(key)));
				rates.add(taxRate);

			} catch (final ParseException e) {
			} catch (final NumberFormatException e) {
				final String message = String.format("Could not understand '%s' as a tax rate for date '%s'.", row.get(key), key);
				context.addProblem(context.createProblem(message, true, true, true));
			}

		}

		entityBook.eSet(CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES, rates);
		final String entityName = row.get(ENTITY_KEY);
		final String bookType = row.get(TYPE_KEY);
		context.doLater(new IDeferment() {
			@Override
			public void run(final IImportContext importContext) {
				final IMMXImportContext context = (IMMXImportContext) importContext;
				final BaseLegalEntity legalEntity = (BaseLegalEntity) context.getNamedObject(entityName, CommercialPackage.Literals.BASE_LEGAL_ENTITY);
				if (legalEntity != null) {
					if (TYPE_SHIPPING.equalsIgnoreCase(bookType)) {
						legalEntity.setShippingBook(entityBook);
					} else if (TYPE_TRADES.equalsIgnoreCase(bookType)) {
						legalEntity.setTradingBook(entityBook);
					} else if (TYPE_UPSTREAM.equalsIgnoreCase(bookType)) {
						legalEntity.setUpstreamBook(entityBook);
					} else {
						context.createProblem("Unknown book type " + bookType, true, true, true);
					}
				}
			}

			@Override
			public int getStage() {
				return IMMXImportContext.STAGE_REFERENCES_RESOLVED;
			}
		});

		return result;

	}

	/**
	 * Adds tax rate data to a LegalEntity's CSV export fields. To preserve a consistent file structure, all tax rate dates specified for any entity have to be included for all other entities.
	 * 
	 * @param object
	 * @param dates
	 * @param root
	 * @return
	 */
	protected Map<String, String> exportTaxCurve(final BaseEntityBook entityBook, final Collection<String> dates, final IExportContext context) {
		final Map<String, String> result = new LinkedHashMap<String, String>();

		for (final String date : dates) {
			result.put(date, "");
		}

		final EList<TaxRate> taxRates = entityBook.getTaxRates();
		for (final TaxRate rate : taxRates) {
			result.put(dateParser.formatLocalDate(rate.getDate()), String.format("%.4f", rate.getValue()));
		}

		return result;
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IMMXExportContext context) {
		final LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();

		final SortedSet<String> dates = new TreeSet<String>();
		// make a list of objects in the order they are first traversed
		// this is paranoia in case the "objects" parameter returns its elements
		// in an inconsistent order
		final LinkedList<EObject> objectList = new LinkedList<EObject>();

		/*
		 * Every entity book must have a field for every date keypoint in any entity's tax data, not just its own tax data. This allows the CSV file to have a consistent row length and column
		 * semantics.
		 */

		for (final EObject obj : objects) {
			if (obj instanceof BaseLegalEntity) {
				final BaseLegalEntity baseLegalEntity = (BaseLegalEntity) obj;

				final BaseEntityBook shippingBook = baseLegalEntity.getShippingBook();
				if (shippingBook != null) {
					for (final TaxRate taxRate : shippingBook.getTaxRates()) {
						dates.add(dateParser.formatLocalDate(taxRate.getDate()));
					}
					objectList.add(shippingBook);
				}

				final BaseEntityBook tradingBook = baseLegalEntity.getTradingBook();
				if (tradingBook != null) {
					for (final TaxRate taxRate : tradingBook.getTaxRates()) {
						dates.add(dateParser.formatLocalDate(taxRate.getDate()));
					}
					objectList.add(tradingBook);
				}
				final BaseEntityBook upstreamBook = baseLegalEntity.getUpstreamBook();
				if (upstreamBook != null) {
					for (final TaxRate taxRate : upstreamBook.getTaxRates()) {
						dates.add(dateParser.formatLocalDate(taxRate.getDate()));
					}
					objectList.add(upstreamBook);
				}
			}
		}

		/*
		 * Add the default export maps for each entity to the method result. A minor hack occurs here: we will list all non-date fields used by any exported element, and add them to the first element
		 * of the result so that when they are exported, non-date columns will precede all date columns.
		 */

		final SortedSet<String> fields = new TreeSet<String>();

		for (final EObject object : objectList) {
			final Map<String, String> data = exportObject(object, context);
			// export the "kind" field containing the metaclass name
			data.put(KIND_KEY, object.eClass().getName());
			data.put(ENTITY_KEY, ((BaseLegalEntity) object.eContainer()).getName());
			if (object.eContainmentFeature() == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK) {
				data.put(TYPE_KEY, TYPE_SHIPPING);
			} else if (object.eContainmentFeature() == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
				data.put(TYPE_KEY, TYPE_TRADES);
			} else if (object.eContainmentFeature() == CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK) {
				data.put(TYPE_KEY, TYPE_UPSTREAM);
			}

			// add the data to the result
			result.add(data);
			// remember the fields used
			fields.addAll(data.keySet());
		}

		// if there are any fields, attach them to the first element
		if (!result.isEmpty()) {
			final Map<String, String> first = result.getFirst();
			for (final String field : fields) {
				if (first.get(field) == null) {
					first.put(field, "");
				}
			}
		}

		// now export the tax curve date fields per object
		// this guarantees they will appear after the non-date columns
		for (int i = 0; i < objectList.size(); i++) {
			final EObject eObject = objectList.get(i);
			if (eObject instanceof BaseEntityBook) {
				final BaseEntityBook baseEntityBook = (BaseEntityBook) eObject;
				result.get(i).putAll(exportTaxCurve(baseEntityBook, dates, context));
			}
		}

		return result;
	}

	protected boolean shouldImportReference(final EReference reference) {
		return reference != CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES;
	}

	@Override
	protected boolean shouldExportFeature(final EStructuralFeature feature) {
		return super.shouldExportFeature(feature) && feature != CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES;
	}
}

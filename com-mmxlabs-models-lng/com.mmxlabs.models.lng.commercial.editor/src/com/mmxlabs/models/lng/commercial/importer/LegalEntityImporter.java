package com.mmxlabs.models.lng.commercial.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.dates.DateAttributeImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 3.0
 */
public class LegalEntityImporter extends DefaultClassImporter {

	static final String NAME = "name";
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
	final DateAttributeImporter dateParser = new DateAttributeImporter();

	@Override
	public Collection<EObject> importObject(EClass targetClass,
			Map<String, String> row, IImportContext context) {
				
		// use the default importer to import the object's usual attributes 
		final Collection<EObject> result = super.importObject(targetClass, row, context);
		
		// pick the legal entity which was created by the default importer
		LegalEntity entity = null;		
		for (final EObject object: result) {
			if (object instanceof LegalEntity) {
				if (entity != null) {
					context.addProblem(context.createProblem("Multiple entities created for one import line.", true, true, true));				
				}
				entity = (LegalEntity) object;
			}
		}
		
		// now read the tax rates out of the CSV data row
		LinkedList<TaxRate> rates = new LinkedList<TaxRate>();
		for (final String key: row.keySet()) {
			final String value = row.get(key);
			if (value.equals("")) {
				continue;
			}
			try {
				Date date = dateParser.parseDate(key);
				final TaxRate taxRate = CommercialFactory.eINSTANCE.createTaxRate();
				taxRate.setDate(date);
				taxRate.setValue(Float.parseFloat(row.get(key)));
				rates.add(taxRate);
				
			} 
			catch (ParseException e) {
			}
			catch (NumberFormatException e) {					
				String message = String.format("Could not understand '%s' as a tax rate for date '%s'.", row.get(key), key);
				context.addProblem(context.createProblem(message, true, true, true));
			}
			
		}

		entity.eSet(CommercialPackage.Literals.LEGAL_ENTITY__TAX_RATES, rates);
		return result;
		
	}
	
	protected Map<String, String> exportTaxCurve(final EObject object, final Collection<String> dates, final MMXRootObject root) {
		if (object instanceof LegalEntity) {
			final LegalEntity entity = (LegalEntity) object;
			final Map<String, String> result = new LinkedHashMap<String,String>();

			for (final String date: dates) {
				result.put(date, "");
			}
			
			final EList<TaxRate> taxRates = entity.getTaxRates();
			for (final TaxRate rate: taxRates) {
				result.put(shortDate.format(rate.getDate()), String.format("%.02f", rate.getValue()));
			}
			
			return result;
		}
		return null;
	}
	
	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final MMXRootObject root) {
		final LinkedList<Map<String, String>> result = new LinkedList<Map<String,String>>();
		
		/*
		 * Every entity must have a field for every date keypoint in any entity's tax data, not just its own tax data.
		 * This allows the CSV file to have a consistent row length and column semantics.
		 */
		final SortedSet<String> dates = new TreeSet<String>();

		// make a list of objects in the order they are first traversed
		// this is paranoia in case the "objects" parameter returns its elements
		// in an inconsistent order
		final LinkedList<EObject> objectList = new LinkedList<EObject>();

		for (final EObject object: objects) {
			if (object instanceof LegalEntity) {
				for (final TaxRate taxRate: ((LegalEntity) object).getTaxRates()) {
					dates.add(shortDate.format(taxRate.getDate()));
				}
			}
			objectList.add(object);
		}
		
		
		/*
		 * Add the default export maps for each entity to the method result.
		 * A minor hack occurs here: we will list all non-date fields
		 * used by any exported element, and add them to the first element
		 * of the result so that when they are exported, non-date columns will 
		 * precede all date columns.
		 */

		final SortedSet<String> fields = new TreeSet<String>();
		
		for (final EObject object: objectList) {
			Map<String, String> data = exportObject(object, root);
			data.put(KIND_KEY, object.eClass().getName());
			// add the data to the result
			result.add(data);
			// remember the fields used
			fields.addAll(data.keySet());
		}
		
		// if there are any fields, attach them to the first element
		final Map<String, String> first = result.getFirst();
		for (final String field: fields) {
			if (first.get(field) == null) {
				first.put(field, "");
			}
		}
		
		// now export the tax curve date fields per object
		// this guarantees they will appear after the non-date columns
		for (int i = 0; i < objects.size(); i++) {
			result.get(i).putAll(exportTaxCurve(objectList.get(i), dates, root));
		}
		
		return result;
	}
	
	protected boolean shouldImportReference(final EReference reference) {
		return reference != CommercialPackage.Literals.LEGAL_ENTITY__TAX_RATES;
	}
}

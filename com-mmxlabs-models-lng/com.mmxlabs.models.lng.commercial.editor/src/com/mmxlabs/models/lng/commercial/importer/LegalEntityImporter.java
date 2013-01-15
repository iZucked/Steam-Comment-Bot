package com.mmxlabs.models.lng.commercial.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 3.0
 */
public class LegalEntityImporter extends DefaultClassImporter {

	static final String NAME = "name";
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Collection<EObject> importObject(EClass targetClass,
			Map<String, String> row, IImportContext context) {
		
		final Collection<EObject> result = new LinkedList<EObject>();
		final Set<String> keys = row.keySet();

		/**
		 * A legal entity should be serialised with a name field.
		 */
		if (keys.contains(NAME)) {
			final LegalEntity entity = CommercialFactory.eINSTANCE.createLegalEntity();
			entity.setName(row.get(NAME));
			keys.remove(NAME);

			LinkedList<TaxRate> rates = new LinkedList<TaxRate>();
			for (final String key: keys) {
				try {
					Date date = shortDate.parse(key);
					final TaxRate taxRate = CommercialFactory.eINSTANCE.createTaxRate();
					taxRate.setDate(date);
					taxRate.setValue(Float.parseFloat(row.get(key)));
					rates.add(taxRate);
					
				} 
				catch (ParseException e) {
				}
				catch (NumberFormatException e) {					
				}
			}
			
			entity.eSet(CommercialPackage.Literals.LEGAL_ENTITY__TAX_RATES, rates);
			result.add(entity);					
		}

		return result;
		
	}

	protected Map<String, String> exportEntity(final EObject object, final Collection<String> dates, final MMXRootObject root) {
		if (object instanceof LegalEntity) {
			final LegalEntity entity = (LegalEntity) object;
			final LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();

			result.put(NAME, entity.getName());
			
			// populate the result with empty columns
			for (final String date: dates) {
				result.put(date, "");
			}
			
			final EList<TaxRate> taxRates = entity.getTaxRates();
			for (final TaxRate rate: taxRates) {
				result.put(shortDate.format(rate.getDate()), String.format("%.02f", rate.getValue()));
			}
			
			return result;
		}
		// TODO Auto-generated method stub
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

		for (final EObject object: objects) {
			if (object instanceof LegalEntity) {
				for (final TaxRate taxRate: ((LegalEntity) object).getTaxRates()) {
					dates.add(shortDate.format(taxRate.getDate()));
				}
			}
		}
		
		/*
		 * Add the maps for each entity to the method result.
		 */
		
		for (final EObject object: objects) {
			result.add(exportEntity(object, dates, root));
		}
		
		return result;
	}
}

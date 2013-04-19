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
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.dates.DateAttributeImporter;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 3.0
 * @author Simon McGregor
 */
public class LegalEntityImporter extends DefaultClassImporter {

	static final String NAME = "name";
	static final String SHIPPING_KEY = "shipping";
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
	final DateAttributeImporter dateParser = new DateAttributeImporter();
	protected LegalEntity shippingEntity = null;

	@Override
	public Collection<EObject> importObjects(final EClass targetClass, final CSVReader reader, final IImportContext context) {
		// reset the shipping entity
		shippingEntity = null;
		// shipping entity may be set by importObjects
		final Collection<EObject> result = super.importObjects(targetClass, reader, context);

		// final variable for closure
		final LegalEntity finalEntity = shippingEntity;

		final IDeferment setShippingEntity = new IDeferment() {

			@Override
			public void run(IImportContext context) {
				MMXRootObject rootObject = context.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					CommercialModel commercialModel = ((LNGScenarioModel) rootObject).getCommercialModel();
					LegalEntity entity = finalEntity;
					if (entity == null) {
						context.addProblem(context.createProblem("No shipping entity was specified. An arbitrary one was chosen.", true, true, true));
						EList<LegalEntity> entities = commercialModel.getEntities();
						if (!entities.isEmpty()) {
							entity = entities.get(0);
						}
					}
					commercialModel.setShippingEntity(entity);
				}
			}

			@Override
			public int getStage() {
				return IImportContext.STAGE_MODIFY_SUBMODELS;
			}

		};
		context.doLater(setShippingEntity);
		return result;
	}

	@Override
	public Collection<EObject> importObject(EClass targetClass, Map<String, String> row, IImportContext context) {

		// use the default importer to import the object's usual attributes
		final Collection<EObject> result = super.importObject(targetClass, row, context);

		// pick the legal entity which was created by the default importer
		LegalEntity entity = null;
		for (final EObject object : result) {
			if (object instanceof LegalEntity) {
				if (entity != null) {
					context.addProblem(context.createProblem("Multiple entities created for one import line.", true, true, true));
				}
				entity = (LegalEntity) object;
			}
		}

		// if this entity is the shipping entity, set it as the shipping entity
		String shipping = row.get(SHIPPING_KEY);
		// this will require importing later, when the submodels have been built
		if (shipping != null && !shipping.equals("")) {
			if (shippingEntity != null) {
				final IImportProblem problem = context.createProblem("The importer is trying to set more than one shipping entity.", true, true, true);
				context.addProblem(problem);
			}
			shippingEntity = entity;
		}

		// now read the tax rates out of the CSV data row
		LinkedList<TaxRate> rates = new LinkedList<TaxRate>();
		for (final String key : row.keySet()) {
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

			} catch (ParseException e) {
			} catch (NumberFormatException e) {
				String message = String.format("Could not understand '%s' as a tax rate for date '%s'.", row.get(key), key);
				context.addProblem(context.createProblem(message, true, true, true));
			}

		}

		entity.eSet(CommercialPackage.Literals.LEGAL_ENTITY__TAX_RATES, rates);
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
	protected Map<String, String> exportTaxCurve(final EObject object, final Collection<String> dates, final MMXRootObject root) {
		if (object instanceof LegalEntity) {
			final LegalEntity entity = (LegalEntity) object;
			final Map<String, String> result = new LinkedHashMap<String, String>();

			for (final String date : dates) {
				result.put(date, "");
			}

			final EList<TaxRate> taxRates = entity.getTaxRates();
			for (final TaxRate rate : taxRates) {
				result.put(shortDate.format(rate.getDate()), String.format("%.4f", rate.getValue()));
			}

			return result;
		}
		return null;
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final MMXRootObject root) {
		final LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();

		// determine which entity is the default shipping entity
		LegalEntity shippingEntity = null;
		if (root instanceof LNGScenarioModel) {
			shippingEntity = ((LNGScenarioModel) root).getCommercialModel().getShippingEntity();
		}

		/*
		 * Every entity must have a field for every date keypoint in any entity's tax data, not just its own tax data. This allows the CSV file to have a consistent row length and column semantics.
		 */
		final SortedSet<String> dates = new TreeSet<String>();

		// make a list of objects in the order they are first traversed
		// this is paranoia in case the "objects" parameter returns its elements
		// in an inconsistent order
		final LinkedList<EObject> objectList = new LinkedList<EObject>();

		for (final EObject object : objects) {
			// while we are at it, remember the dates attached to each entity's tax rates
			if (object instanceof LegalEntity) {
				for (final TaxRate taxRate : ((LegalEntity) object).getTaxRates()) {
					dates.add(shortDate.format(taxRate.getDate()));
				}
			}
			objectList.add(object);
		}

		/*
		 * Add the default export maps for each entity to the method result. A minor hack occurs here: we will list all non-date fields used by any exported element, and add them to the first element
		 * of the result so that when they are exported, non-date columns will precede all date columns.
		 */

		final SortedSet<String> fields = new TreeSet<String>();

		for (final EObject object : objectList) {
			Map<String, String> data = exportObject(object, root);
			// export the "kind" field containing the metaclass name
			data.put(KIND_KEY, object.eClass().getName());
			data.put(SHIPPING_KEY, object == shippingEntity ? "Y" : "");

			// add the data to the result
			result.add(data);
			// remember the fields used
			fields.addAll(data.keySet());
		}

		// if there are any fields, attach them to the first element
		final Map<String, String> first = result.getFirst();
		for (final String field : fields) {
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

	public LegalEntity getShippingEntity() {
		return shippingEntity;
	}
}

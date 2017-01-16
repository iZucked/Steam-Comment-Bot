/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class EntityBookTaxRateRangeConstraint extends AbstractModelConstraint {
	final DateTimeFormatter shortDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof BaseEntityBook) {
			final BaseEntityBook entityBook = (BaseEntityBook) target;
			final EList<TaxRate> rates = entityBook.getTaxRates();

			String entityName = ((BaseLegalEntity) entityBook.eContainer()).getName();
			// if there are no tax rates, raise a validation error and do not check any more problems
			if (rates.isEmpty()) {
				String failureMessage = String.format("Entity '%s' has no tax data", entityName);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(entityBook, CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES);
				return dsd;
			}

			// count number of entries per date (to amalgamate multiple duplicates
			// for the same date into one validation error)
			final Map<String, Integer> dateCount = new LinkedHashMap<String, Integer>();
			for (final TaxRate rate : rates) {
				final String date = rate.getDate().format(shortDate);
				Integer count = dateCount.get(date);
				dateCount.put(date, count == null ? 1 : count + 1);
			}

			// create validation errors for duplicate dates
			for (final Entry<String, Integer> entry : dateCount.entrySet()) {
				Integer count = entry.getValue();
				String date = entry.getKey();
				if (entry.getValue() > 1) {
					String failureMessage = String.format("Entity '%s' has %d tax rate entries for the date '%s'.", entityName, count, date);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
					dsd.addEObjectAndFeature(entityBook, CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES);
					failures.add(dsd);
				}
			}
		}

		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}

}

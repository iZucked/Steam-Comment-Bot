/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class EntityBookTaxRateRangeConstraint extends AbstractModelMultiConstraint {
	private static final DateTimeFormatter shortDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> failures) {

		final EObject target = ctx.getTarget();

		if (target instanceof BaseEntityBook entityBook) {
			final EList<TaxRate> rates = entityBook.getTaxRates();

			String entityName = ((BaseLegalEntity) entityBook.eContainer()).getName();
			// if there are no tax rates, raise a validation error and do not check any more
			// problems
			if (rates.isEmpty()) {
				String failureMessage = String.format("Entity '%s' has no tax data", entityName);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(entityBook, CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES);
				failures.add(dsd);
				return;
			}

			// count number of entries per date (to amalgamate multiple duplicates
			// for the same date into one validation error)
			final Map<String, Integer> dateCount = new LinkedHashMap<>();
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
	}

}

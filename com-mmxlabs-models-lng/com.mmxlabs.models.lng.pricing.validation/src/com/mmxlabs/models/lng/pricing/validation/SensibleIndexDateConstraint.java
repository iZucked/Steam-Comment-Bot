/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;

public class SensibleIndexDateConstraint  extends AbstractModelConstraint {
	private Date earliestDate = new GregorianCalendar(2000,0,1).getTime();

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof CommodityIndex) {
			final CommodityIndex index = (CommodityIndex) target;
			final Index<Double> data = index.getData();
			if (data instanceof DataIndex) {
				for (Date date: data.getDates()) {
					if (date.before(earliestDate)) {
						return (IConstraintStatus) ctx.createFailureStatus(index.getName(), earliestDate);
					}
				}
				
			}
		}
		
		return ctx.createSuccessStatus();
	}

}

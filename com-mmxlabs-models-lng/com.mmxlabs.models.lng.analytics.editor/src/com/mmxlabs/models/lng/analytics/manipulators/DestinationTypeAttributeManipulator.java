/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.manipulators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

/**
 * A subclass of {@link ValueListAttributeManipulator} to handle the restricted set of {@link DestinationType} enums
 * 
 * @author Simon Goodall
 * 
 */
public class DestinationTypeAttributeManipulator extends ValueListAttributeManipulator {
	public DestinationTypeAttributeManipulator(final EAttribute field, final EditingDomain editingDomain) {
		super(field, editingDomain, getValues(AnalyticsPackage.eINSTANCE.getDestinationType()));
	}

	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		for (final EEnumLiteral literal : eenum.getELiterals()) {
			if (literal.getInstance().equals(DestinationType.START)) {
			} else if (literal.getInstance().equals(DestinationType.END)) {

			} else {
				values.add(new Pair<String, Object>(literal.getName(), literal.getInstance()));
			}
		}
		return values;
	}

	@Override
	public boolean canEdit(final Object object) {
		if (object instanceof ShippingCostRow) {
			final ShippingCostRow shippingCostRow = (ShippingCostRow) object;
			if (shippingCostRow.getDestinationType() == DestinationType.START || shippingCostRow.getDestinationType() == DestinationType.END) {
				return false;
			}
			return true;
		}

		return false;
	}
}

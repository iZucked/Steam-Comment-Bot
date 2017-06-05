/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.manipulators;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

/**
 * Editor for enums
 * 
 * @author hinton
 */
public class InventoryPeriodEnumAttributeManipulator extends ValueListAttributeManipulator {
	public InventoryPeriodEnumAttributeManipulator(final EAttribute field, final EditingDomain editingDomain) {
		super(field, editingDomain, getValues((EEnum) field.getEAttributeType()));
	}

	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		values.add(new Pair<String, Object>("Monthly", InventoryFrequency.MONTHLY));
		values.add(new Pair<String, Object>("Daily", InventoryFrequency.DAILY));
		values.add(new Pair<String, Object>("Hourly", InventoryFrequency.HOURLY));
		values.add(new Pair<String, Object>("Cargo", InventoryFrequency.CARGO));
		values.add(new Pair<String, Object>("Level", InventoryFrequency.LEVEL));
		return values;
	}
}

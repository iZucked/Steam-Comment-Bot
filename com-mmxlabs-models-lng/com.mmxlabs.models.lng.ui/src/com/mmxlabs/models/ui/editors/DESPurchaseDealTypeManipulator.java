/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

public class DESPurchaseDealTypeManipulator extends ValueListAttributeManipulator<DESPurchaseDealType> {

	public DESPurchaseDealTypeManipulator(final EAttribute field, final EditingDomain editingDomain) {
		super(field, editingDomain, getValues());
	}

	private static List<Pair<String, DESPurchaseDealType>> getValues() {
		final LinkedList<Pair<String, DESPurchaseDealType>> values = new LinkedList<>();
		for (final DESPurchaseDealType type : DESPurchaseDealType.values()) {
			final String name;
			switch (type) {
			case DEST_ONLY:
				name = "Dest only";
				break;
			case DEST_WITH_SOURCE:
				name = "Dest with source";
				break;
			case DIVERT_FROM_SOURCE:
				name = "Divert from source";
				break;
			default:
				name = type.getName();
				break;
			}
			values.add(Pair.of(name, type));
		}
		return values;
	}
}

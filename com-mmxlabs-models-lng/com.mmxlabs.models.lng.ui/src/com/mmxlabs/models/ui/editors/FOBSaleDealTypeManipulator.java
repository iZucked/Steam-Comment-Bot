/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

public class FOBSaleDealTypeManipulator extends ValueListAttributeManipulator<FOBSaleDealType> {

	public FOBSaleDealTypeManipulator(final EAttribute field, final ICommandHandler commandHandler) {
		super(field, commandHandler, getValues());
	}

	private static List<Pair<String, FOBSaleDealType>> getValues() {
		final LinkedList<Pair<String, FOBSaleDealType>> values = new LinkedList<>();
		for (final FOBSaleDealType type : FOBSaleDealType.values()) {
			final String name;
			switch (type) {
			case SOURCE_ONLY:
				name = "Source only";
				break;
			case SOURCE_WITH_DEST:
				name = "Source with dest";
				break;
			case DIVERT_TO_DEST:
				name = "Divert to dest";
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

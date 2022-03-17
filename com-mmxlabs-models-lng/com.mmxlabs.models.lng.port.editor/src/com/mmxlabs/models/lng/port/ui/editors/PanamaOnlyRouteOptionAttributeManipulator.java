/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

public class PanamaOnlyRouteOptionAttributeManipulator extends ValueListAttributeManipulator {
	public PanamaOnlyRouteOptionAttributeManipulator(final EAttribute field, final ICommandHandler commandHandler) {
		super(field, commandHandler, getValues());
	}

	private static List<Pair<String, Object>> getValues() {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		values.add(new Pair<String, Object>(PortModelLabeller.getName(RouteOption.PANAMA), RouteOption.PANAMA));
		return values;
	}
}
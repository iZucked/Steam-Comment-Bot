package com.mmxlabs.models.lng.port.ui.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

public class RouteOptionAttributeManipulator extends ValueListAttributeManipulator {
	public RouteOptionAttributeManipulator(final EAttribute field, final EditingDomain editingDomain) {
		super(field, editingDomain, getValues());
	}

	private static List<Pair<String, Object>> getValues() {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		for (final RouteOption literal : RouteOption.values()) {
			values.add(new Pair<String, Object>(PortModelLabeller.getName(literal), literal));
		}
		return values;
	}
}
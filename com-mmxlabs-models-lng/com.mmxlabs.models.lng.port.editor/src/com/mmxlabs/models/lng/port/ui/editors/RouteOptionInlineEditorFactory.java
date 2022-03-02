/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class RouteOptionInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {

		ArrayList<Object> objectsList = new ArrayList<>();
		for (final RouteOption routeOption : RouteOption.values()) {
			final String name = PortModelLabeller.getName(routeOption);

			objectsList.add(name);
			objectsList.add(routeOption);
		}
		return new EENumInlineEditor((EAttribute) feature, objectsList.toArray());
	}

	public static IInlineEditor createPanamaOnlyEditor(final EClass owner, final EStructuralFeature feature) {

		ArrayList<Object> objectsList = new ArrayList<>();
		final String name = PortModelLabeller.getName(RouteOption.PANAMA);
		objectsList.add(name);
		objectsList.add(RouteOption.PANAMA);
		return new EENumInlineEditor((EAttribute) feature, objectsList.toArray());
	}

}

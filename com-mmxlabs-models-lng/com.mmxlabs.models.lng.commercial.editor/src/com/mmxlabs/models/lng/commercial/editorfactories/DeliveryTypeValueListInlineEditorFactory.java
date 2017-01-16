/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.editorfactories;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

/**
 */
public class DeliveryTypeValueListInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		ArrayList<Object> objectsList = new ArrayList<>();
		for (final CargoDeliveryType type : CargoDeliveryType.values()) {
			final String name;
			switch (type) {
			case ANY:
				name = "Any";
				break;
			case NOT_SHIPPED:
				name = "Not Shipped";
				break;
			case SHIPPED:
				name = "Shipped";
				break;
			default:
				name = type.getName();
				break;
			}
			objectsList.add(name);
			objectsList.add(type);
		}
		return new EENumInlineEditor((EAttribute) feature, objectsList.toArray());
	}
}

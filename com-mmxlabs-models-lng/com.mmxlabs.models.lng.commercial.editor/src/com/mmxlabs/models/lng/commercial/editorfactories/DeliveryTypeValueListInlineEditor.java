/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.editorfactories;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.TypesPackage;

/**
 * @since 2.0
 */

public class DeliveryTypeValueListInlineEditor extends AbstractValueListInlineEditor {

	public DeliveryTypeValueListInlineEditor(final EStructuralFeature feature) {
		super(feature, TypesPackage.eINSTANCE.getCargoDeliveryType().getELiterals());

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
			names.add(name);
			values.add(type);
		}

	}
}
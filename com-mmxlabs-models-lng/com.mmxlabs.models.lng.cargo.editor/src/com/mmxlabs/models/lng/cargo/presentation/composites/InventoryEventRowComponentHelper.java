/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for InventoryEventRow instances
 *
 * @generated NOT
 */
public abstract class InventoryEventRowComponentHelper extends DefaultComponentHelper {

	protected InventoryEventRowComponentHelper(final EClass targetClass) {
		super(targetClass);
		ignoreFeatures.add(CargoPackage.Literals.INVENTORY_EVENT_ROW__VOLUME_LOW);
		ignoreFeatures.add(CargoPackage.Literals.INVENTORY_EVENT_ROW__VOLUME_HIGH);

	}
}
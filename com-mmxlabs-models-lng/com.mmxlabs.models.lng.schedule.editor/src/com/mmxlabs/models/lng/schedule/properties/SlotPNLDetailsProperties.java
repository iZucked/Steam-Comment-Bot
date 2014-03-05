/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.properties;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.AbstractDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.factory.DetailPropertyFactoryRegistry;
import com.mmxlabs.models.ui.properties.factory.IDetailPropertyFactory;

public class SlotPNLDetailsProperties extends AbstractDetailPropertyFactory {

	private static final String CATEGORY_PNL = "pnl";

	@Override
	@Nullable
	public DetailProperty createProperties(@NonNull final EObject eObject) {
		if (eObject instanceof SlotPNLDetails) {
			SlotPNLDetails slotPNLDetails = (SlotPNLDetails) eObject;
			return createTree(slotPNLDetails, null);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull final SlotPNLDetails slotPNLDetails, @Nullable final MMXRootObject rootObject) {
		final DetailPropertyFactoryRegistry registry = DetailPropertyFactoryRegistry.createRegistry();
		final DetailProperty slotProperty = PropertiesFactory.eINSTANCE.createDetailProperty();
		Slot slot = slotPNLDetails.getSlot();
		if (slot != null) {
			slotProperty.setName(slot.getName());
		}

		for (final GeneralPNLDetails generalPNLDetails : slotPNLDetails.getGeneralPNLDetails()) {

			final EClass eClass = generalPNLDetails.eClass();
			assert eClass != null;
			final IDetailPropertyFactory factory = registry.getFactory(CATEGORY_PNL, eClass);
			if (factory != null) {
				final DetailProperty p = factory.createProperties(generalPNLDetails, rootObject);
				if (p != null) {
					slotProperty.getChildren().add(p);
				}
			}
		}

		return slotProperty;

	}
}

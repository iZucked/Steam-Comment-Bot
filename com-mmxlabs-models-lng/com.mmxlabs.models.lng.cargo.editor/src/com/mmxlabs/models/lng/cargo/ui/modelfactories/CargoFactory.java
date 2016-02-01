/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.modelfactories;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

/**
 * @author hinton
 * 
 */
public class CargoFactory extends DefaultModelFactory {
	private final DefaultModelFactory slotFactory = new DefaultModelFactory();

	public CargoFactory() {
		super();
	}

	@Override
	public Collection<ISetting> createInstance(final MMXRootObject rootObject, final EObject container, final EReference containment, final ISelection selection) {
		final Collection<ISetting> loadSetting = slotFactory.createInstance(rootObject, container, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), selection);
		final Collection<ISetting> dischargeSetting = slotFactory.createInstance(rootObject, container, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), selection);
		final Collection<ISetting> superSetting = super.createInstance(rootObject, container, containment, selection);

		LoadSlot load = null;
		DischargeSlot discharge = null;
		for (final ISetting setting : loadSetting)
			if (setting.getInstance() instanceof LoadSlot)
				load = (LoadSlot) setting.getInstance();

		for (final ISetting setting : dischargeSetting)
			if (setting.getInstance() instanceof DischargeSlot)
				discharge = (DischargeSlot) setting.getInstance();

		for (final ISetting setting : superSetting) {
			if (setting.getInstance() instanceof Cargo) {
				((Cargo) setting.getInstance()).getSlots().add(load);
				((Cargo) setting.getInstance()).getSlots().add(discharge);
			}
		}

		final ArrayList<ISetting> result = new ArrayList<ISetting>();
		result.addAll(superSetting);
		result.addAll(loadSetting);
		result.addAll(dischargeSetting);
		return result;
	}

}

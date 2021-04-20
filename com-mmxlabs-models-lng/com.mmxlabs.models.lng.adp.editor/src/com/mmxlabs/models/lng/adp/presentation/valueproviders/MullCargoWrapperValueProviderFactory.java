/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class MullCargoWrapperValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory existingLoadSlots;

	public MullCargoWrapperValueProviderFactory() {
		this.existingLoadSlots = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), CargoPackage.eINSTANCE.getLoadSlot());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		return new BaseReferenceValueProvider() {

			@Override
			public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {
				final IReferenceValueProvider loadSlotProvider = existingLoadSlots.createReferenceValueProvider(CargoPackage.Literals.CARGO_MODEL, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS,
						rootObject);
				final List<Pair<String, EObject>> loadSlots = loadSlotProvider.getAllowedValues(target, field);
				return loadSlots.stream().filter(pair -> {
					final LoadSlot loadSlot = (LoadSlot) pair.getSecond();
					if (loadSlot.getCargo() == null) {
						return false;
					} else {
						final Cargo cargo = loadSlot.getCargo();
						return cargo.getSlots().get(1) != null;
					}
				}).collect(Collectors.toList());
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void cacheValues() {
				// TODO Auto-generated method stub

			}

			@Override
			public String getName(EObject referer, EReference feature, EObject referenceValue) {
				if (referenceValue != null) {
					final MullCargoWrapper mullCargoWrapper = (MullCargoWrapper) referenceValue;
					final String loadID = mullCargoWrapper.getLoadSlot().getName();
					if (loadID != null) {
						return loadID;
					}
				}
				return "";
			}
		};
	}

}

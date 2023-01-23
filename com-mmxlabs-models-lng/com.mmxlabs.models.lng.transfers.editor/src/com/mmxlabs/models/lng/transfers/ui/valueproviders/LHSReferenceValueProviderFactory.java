/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.ui.valueproviders;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;

public class LHSReferenceValueProviderFactory implements IReferenceValueProviderFactory {
	
	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			final TransferModel model = ScenarioModelUtil.getTransferModel(lngScenarioModel);
			if (model != null) {
				final EClass referenceClass = reference.getEReferenceType();
				if (CargoPackage.eINSTANCE.getSlot().isSuperTypeOf(referenceClass)) {
					final CargoModel cm = lngScenarioModel.getCargoModel();
					return new MergedReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots()) {
						@Override
						public List<Pair<String, EObject>> getAllowedValues(final EObject target, final ETypedElement field) {
							final List<Pair<String, EObject>> superValues = super.getAllowedValues(target, field);

							if (target instanceof Slot<?>) {
								return superValues.stream()
								.filter(s -> !(s.getSecond() instanceof SpotSlot))
								.filter(s -> {
									if (s.getSecond() instanceof Slot slot) {
										if (slot.getCargo() != null) {
											if (slot.getCargo().getSlots().size() > 2) {
												return false;
											}
										}
									}
									return true;
								})
								.collect(Collectors.toList());
							}
							
							return superValues;
						}
					};
				}
			}
		}
		
		return null;
	}

}

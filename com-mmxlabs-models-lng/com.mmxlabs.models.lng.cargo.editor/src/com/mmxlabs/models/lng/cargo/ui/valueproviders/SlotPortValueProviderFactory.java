/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * This is a restricted case reference value provider which filters the port list on slots to those allowed by the slots' contract.
 * 
 * @author hinton
 * 
 */
public class SlotPortValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public SlotPortValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), PortPackage.eINSTANCE.getPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (delegate == null)
			return null;
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == CargoPackage.eINSTANCE.getSlot_Port()) {
			return new IReferenceValueProvider() {
				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {
					if (changedFeature == CargoPackage.eINSTANCE.getSlot_Contract()) {
						return true;
					} else if (changedFeature == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
						return true;
					} else if (changedFeature == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
						return true;
					}

					return delegateFactory.updateOnChangeToFeature(changedFeature);
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
					return delegateFactory.getNotifiers(referer, feature, referenceValue);
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					return delegateFactory.getName(referer, feature, referenceValue);
				}

				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);

					if (target instanceof Slot) {
						final Slot slot = (Slot) target;
						final Contract contract = slot.getContract();

						PortCapability capability = null;

						final boolean isTransferSlot = (target instanceof LoadSlot && ((LoadSlot) target).getTransferFrom() != null)
								|| (target instanceof DischargeSlot && ((DischargeSlot) target).getTransferTo() != null);

						if (isTransferSlot) {
							capability = PortCapability.TRANSFER;
						} else {
							// If FOB or DES, then only one port is permitted - this should be set by the CargoTypeUpdatingCommandProvider
							// -- However if the slot is not linked, then we are free to change
							if (target instanceof LoadSlot) {
								LoadSlot loadSlot = (LoadSlot) target;
								if (!loadSlot.isDESPurchase()) {
									capability = PortCapability.LOAD;
								} else {
									if (loadSlot.getCargo() == null) {
//										capability = PortCapability.DISCHARGE;
									}
								}
							} else if (target instanceof DischargeSlot) {
								DischargeSlot dischargeSlot = (DischargeSlot) target;
								if (!dischargeSlot.isFOBSale()) {
									capability = PortCapability.DISCHARGE;
								} else {
									if (dischargeSlot.getCargo() == null) {
//										capability = PortCapability.LOAD;
									}
								}
							}
						}
						final ArrayList<Pair<String, EObject>> filterOne = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> p : delegateValue) {
							if (capability == null || ((Port) p.getSecond()).getCapabilities().contains(capability)) {
								filterOne.add(p);
							}
						}

						{
							// Make sure current selection is in the list
							final Port port = slot.getPort();
							if (port != null) {
								final Pair<String, EObject> pair = new Pair<String, EObject>(port.getName(), port);
								if (!filterOne.contains(pair)) {
									filterOne.add(pair);
								}
							}
						}

						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						if (contract == null) {
							return filterOne;
						}

						Set<Port> ports = SetUtils.getObjects(contract.getAllowedPorts());

						if (ports != null && !ports.isEmpty()) {
							for (final Pair<String, EObject> value : filterOne) {
								if (ports.contains(value.getSecond())) {
									filteredList.add(value);
								}
							}
						} else {
							return filterOne;
						}

						{
							// Make sure current selection is in the list (check again in case filtered by contract)
							final Port port = slot.getPort();
							if (port != null) {
								final Pair<String, EObject> pair = new Pair<String, EObject>(port.getName(), port);
								if (!filteredList.contains(pair)) {
									filteredList.add(pair);
								}
							}
						}

						return filteredList;
					}
					return delegateValue;
				}

				@Override
				public void dispose() {
					delegateFactory.dispose();
				}
			};
		} else {
			return delegateFactory;
		}
	}
}

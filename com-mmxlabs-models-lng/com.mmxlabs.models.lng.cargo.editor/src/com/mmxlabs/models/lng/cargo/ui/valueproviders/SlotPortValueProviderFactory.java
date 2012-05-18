/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TypesPackage;
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
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), TypesPackage.eINSTANCE.getAPort());
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

						final PortCapability capability = (target instanceof LoadSlot ? PortCapability.LOAD : PortCapability.DISCHARGE);
						final ArrayList<Pair<String, EObject>> filterOne = new ArrayList<Pair<String, EObject>>();
						for (final Pair<String, EObject> p : delegateValue) {
							if (((Port) p.getSecond()).getCapabilities().contains(capability)) {
								filterOne.add(p);
							}
						}

						{
							// Make sure current selection is in the list
							final Port port = slot.getPort();
							final Pair<String, EObject> pair = new Pair<String, EObject>(port.getName(), port);
							if (!filterOne.contains(pair)) {
								filterOne.add(pair);
							}
						}

						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						final Contract contract = slot.getContract();
						if (contract == null) {
							return filterOne;
						}
						final UniqueEList<APortSet> marks = new UniqueEList<APortSet>();
						final HashSet<APort> ports = new HashSet<APort>();
						for (final APortSet set : contract.getAllowedPorts()) {
							ports.addAll(set.collect(marks));
						}
						for (final Pair<String, EObject> value : filterOne) {
							if (ports.contains(value.getSecond())) {
								filteredList.add(value);
							}
						}

						{
							// Make sure current selection is in the list (check again in case filtered by contract)
							final Port port = slot.getPort();
							final Pair<String, EObject> pair = new Pair<String, EObject>(port.getName(), port);
							if (!filteredList.contains(pair)) {
								filteredList.add(pair);
							}
						}
						
						return filteredList;
					}
					return delegateValue;
				}
			};
		} else {
			return delegateFactory;
		}
	}
}

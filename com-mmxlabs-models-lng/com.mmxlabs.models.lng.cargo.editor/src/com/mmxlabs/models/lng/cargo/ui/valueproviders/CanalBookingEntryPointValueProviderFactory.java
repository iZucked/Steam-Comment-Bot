/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * 
 */
public class CanalBookingEntryPointValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
			return new IReferenceValueProvider() {
				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {

					// ??
					if (changedFeature == CargoPackage.eINSTANCE.getCanalBookingSlot_Route()) {
						return true;
					} else if (changedFeature == CargoPackage.eINSTANCE.getCanalBookingSlot_EntryPoint()) {
						return true;
					}

					return false;
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
					if (referenceValue == null)
						return Collections.emptySet();
					return Collections.singleton(new Pair<Notifier, List<Object>>(referenceValue, Collections.singletonList((Object) feature)));
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					if (referenceValue instanceof EntryPoint){
						EntryPoint entryPoint = (EntryPoint)referenceValue;
						if (entryPoint.getName() != null){
							return entryPoint.getName();
						}else {
							return "";
						}
					}
					return "<Not set>";
				}

				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = new LinkedList<>();
					
					delegateValue.add(new Pair("<Not set>", null));
					

					// I assume delegateValue is list of all ports??

					if (target instanceof CanalBookingSlot) {
						CanalBookingSlot canalBookingSlot = (CanalBookingSlot) target;
						
							if (canalBookingSlot.getRoute() != null && canalBookingSlot.getRoute().getEntryA() != null){
								delegateValue.add(new Pair<String, EObject>(canalBookingSlot.getRoute().getEntryA().getName(), canalBookingSlot.getRoute().getEntryA()));
							}
							if (canalBookingSlot.getRoute() != null && canalBookingSlot.getRoute().getEntryB() != null){
								delegateValue.add(new Pair<String, EObject>(canalBookingSlot.getRoute().getEntryB().getName(), canalBookingSlot.getRoute().getEntryB()));
							}
					}
					return delegateValue;
				}

				@Override
				public void dispose() {
				}
			};
	}

	private boolean contains(final EList<APortSet<Port>> ips, final EObject value) {

		for (final APortSet<Port> aPort : ips) {
			if (aPort instanceof Port) {
				final Port port = (Port) aPort;
				if (port == value)
					return true;
			} else if (aPort instanceof PortGroup) {
				final PortGroup portGroup = (PortGroup) aPort;
				return contains(portGroup.getContents(), value);
			}
		}
		return false;
	}

}

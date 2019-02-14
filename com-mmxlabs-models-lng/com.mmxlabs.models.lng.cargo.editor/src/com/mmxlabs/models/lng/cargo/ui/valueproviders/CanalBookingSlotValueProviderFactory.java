/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.TimeUtils;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * 
 */
public class CanalBookingSlotValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public CanalBookingSlotValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), CargoPackage.eINSTANCE.getSlot());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);

		return new IReferenceValueProvider() {
			@Override
			public void dispose() {
				delegateFactory.dispose();
			}

			@Override
			public boolean updateOnChangeToFeature(Object changedFeature) {
				return delegateFactory.updateOnChangeToFeature(changedFeature);
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer, EReference feature, EObject referenceValue) {
				return delegateFactory.getNotifiers(referer, feature, referenceValue);
			}

			@Override
			public String getName(EObject referer, EReference feature, EObject referenceValue) {
				String name = delegateFactory.getName(referer, feature, referenceValue);
				
				String slotName = "";
				if (referer instanceof CanalBookingSlot) {
				CanalBookingSlot canalBookingSlot = (CanalBookingSlot) referer;
					if (canalBookingSlot.getSlot() != null) {
						slotName = String.format(" (%s)", canalBookingSlot.getSlot().getWindowStart());
					}
				}
				
				return delegateFactory.getName(referer, feature, referenceValue) + slotName;
			}

			@Override
			public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
				final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);
				final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<>(delegateValue.size() + 1);
				filteredList.add(new Pair<>("<Unallocated>", null));
				
				for (Pair<String, EObject> value : delegateValue) {
					Slot slot = (Slot) value.getSecond();
					CanalBookingSlot canalBookingSlot = (CanalBookingSlot) target;
					
					if (slot == canalBookingSlot.getSlot()) {
						filteredList.add(value);
					} else {
						LocalDate windowStart = slot.getWindowStart();
						LocalDate windowEnd = windowStart;

						switch (slot.getSlotOrDelegateWindowSizeUnits()) {
						case DAYS:
							windowEnd = windowStart.plusDays(slot.getSlotOrDelegateWindowSize());
							break;
						case HOURS:
							windowEnd = windowStart.plusDays((slot.getSlotOrDelegateWindowSize() + 12) / 24);
							break;
						case MONTHS:
							windowEnd = windowStart.plusMonths(slot.getSlotOrDelegateWindowSize());
							break;
						default:
							break;
						}

						NonNullPair<LocalDate, LocalDate> selectionRange = null;
						selectionRange = new NonNullPair<>(windowStart.minusDays(2), windowEnd.plusDays(60));

						if (TimeUtils.overlaps(selectionRange, new NonNullPair<>(canalBookingSlot.getBookingDate(), canalBookingSlot.getBookingDate()), LocalDate::isBefore)) {
							filteredList.add(value);
						}
					}
				}
				return filteredList;
			}
		};
	}
}

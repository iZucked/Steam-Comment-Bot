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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * This is a restricted case reference value provider which filters the port list on slots to those allowed
 * by the slots' contract.
 * @author hinton
 *
 */
public class SlotPortValueProviderFactory implements
		IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public SlotPortValueProviderFactory() {
		this.delegate = Activator
				.getDefault()
				.getReferenceValueProviderFactoryRegistry()
				.getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(),
						TypesPackage.eINSTANCE.getAPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(
			final EClass owner, final EReference reference,
			final MMXRootObject rootObject) {
		if (delegate == null) return null;
		final IReferenceValueProvider delegateFactory = delegate
				.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == CargoPackage.eINSTANCE.getSlot_Port()) {
			return new IReferenceValueProvider() {
				@Override
				public boolean updateOnChangeToFeature(Object changedFeature) {
					return delegateFactory
							.updateOnChangeToFeature(changedFeature);
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
						EObject referer, EReference feature,
						EObject referenceValue) {
					return delegateFactory.getNotifiers(referer, feature,
							referenceValue);
				}

				@Override
				public String getName(EObject referer, EReference feature,
						EObject referenceValue) {
					return delegateFactory.getName(referer, feature,
							referenceValue);
				}

				@Override
				public List<Pair<String, EObject>> getAllowedValues(
						final EObject target, final EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory
							.getAllowedValues(target, field);

					if (target instanceof Slot) {
						final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
						final Slot slot = (Slot) target;
						final Contract contract = slot.getContract();
						final UniqueEList<APortSet> marks = new UniqueEList<APortSet>();
						final HashSet<APort> ports = new HashSet<APort>();
							for (final APortSet set : contract
									.getAllowedPorts()) {
								ports.addAll(set.collect(marks));
							}
						for (final Pair<String, EObject> value : delegateValue) {
							if (ports.contains(value.getSecond())) {
								filteredList.add(value);
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

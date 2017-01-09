/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;

public final class ObjectCopier {
	public static <T extends Slot> T copySlot(final T slot, final IScenarioEntityMapping mapping) {
		final T newSlot = createNewInstance(slot);
		assert newSlot != null;
		copyFeatures(slot, newSlot, mapping, Lists.<EStructuralFeature>newArrayList(CargoPackage.Literals.SLOT__CARGO, CargoPackage.Literals.LOAD_SLOT__TRANSFER_FROM, CargoPackage.Literals.DISCHARGE_SLOT__TRANSFER_TO));
		return newSlot;
	}

	public static Cargo copyCargo(final Cargo cargo, final IScenarioEntityMapping mapping) {
		final Cargo newCargo = createNewInstance(cargo);
		assert newCargo != null;
		copyFeatures(cargo, newCargo, mapping, Lists.<EStructuralFeature> newArrayList(CargoPackage.Literals.CARGO__SLOTS));
		return newCargo;
	}

	@SuppressWarnings("unchecked")
	public static <T extends EObject> T createNewInstance(final EObject source) {
		final EClass eClass = source.eClass();
		final EFactory eFactory = eClass.getEPackage().getEFactoryInstance();
		return (T) eFactory.create(eClass);
	}

	public static void copyFeatures(final EObject source, final EObject dest, final IScenarioEntityMapping mapping, final Collection<EStructuralFeature> skipFeatures) {
		for (final EStructuralFeature feature : source.eClass().getEAllStructuralFeatures()) {
			// Features to skip
			if (skipFeatures.contains(feature)) {
				continue;
			}
			// Only copy set features
			if (source.eIsSet(feature)) {
				// Attributes (nice and easy)
				if (feature instanceof EAttribute) {
					if (feature.isMany()) {
						throw new UnsupportedOperationException();
					} else {
						dest.eSet(feature, source.eGet(feature));
					}
				} else if (feature instanceof EReference) {
					// References, more tricky due to eOpposites and containments.
					final EReference eReference = (EReference) feature;

					// Skip references with an opposite relationship
					if (eReference.getEOpposite() != null) {
						continue;
					}

					if (!eReference.isContainment()) {
						// Non-contained, thus expect there to be an original reference somewhere
						if (feature.isMany()) {
							// Copy the source list into a new dest list, mapping between versions
							@SuppressWarnings("unchecked")
							final List<EObject> sourceReferenceList = (List<EObject>) source.eGet(feature);
							final List<EObject> destReferenceList = new ArrayList<>(sourceReferenceList.size());
							for (final EObject sourceReference : sourceReferenceList) {
								destReferenceList.add(mapping.getOriginalFromCopy(sourceReference));
							}
							dest.eSet(feature, destReferenceList);
						} else {
							final EObject sourceReference = mapping.getOriginalFromCopy((EObject) source.eGet(feature));
							dest.eSet(feature, sourceReference);
						}
					} else {
						if (feature.isMany()) {
							@SuppressWarnings("unchecked")
							final List<EObject> sourceReferenceList = (List<EObject>) source.eGet(feature);
							final List<EObject> destReferenceList = new ArrayList<>(sourceReferenceList.size());

							for (final EObject sourceReference : sourceReferenceList) {

								final EObject newReference = createNewInstance(sourceReference);
								copyFeatures(sourceReference, newReference, mapping, skipFeatures);
								destReferenceList.add(newReference);
							}
							dest.eSet(feature, destReferenceList);
						} else {
							final EObject sourceReference = (EObject) source.eGet(feature);
							final EObject newReference = createNewInstance(sourceReference);
							copyFeatures(sourceReference, newReference, mapping, skipFeatures);
							dest.eSet(feature, newReference);
						}
					}
				}
			}
		}
	}
}

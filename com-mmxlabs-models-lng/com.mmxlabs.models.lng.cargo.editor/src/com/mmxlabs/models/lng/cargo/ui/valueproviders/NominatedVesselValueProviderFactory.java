/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class NominatedVesselValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory delegate;

	public NominatedVesselValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), FleetPackage.eINSTANCE.getVessel());

	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		if (delegate == null) {
			return null;
		}
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);

		return new IReferenceValueProvider() {
			@Override
			public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
				// get a list of globally permissible values
				final List<Pair<String, EObject>> baseResult = delegateFactory.getAllowedValues(target, field);

				if (target instanceof Slot) {
					final Slot slot = (Slot) target;
					boolean noVesselsAllowed = false;
					final List<AVesselSet<Vessel>> allowedVessels = new ArrayList<>();
					// we only want to filter vessels if we are assigning them (i.e. don't do this if we are selecting vessels to add to the restricted list)
					if (field == CargoPackage.Literals.SLOT__NOMINATED_VESSEL) {
						noVesselsAllowed = AssignmentEditorHelper.compileAllowedVessels(allowedVessels, slot);
					}

					final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<AVesselSet<Vessel>>();
					// filter the global list by the object's allowed values
					if (allowedVessels != null) {

						// Expand out VesselGroups
						for (final AVesselSet<Vessel> s : allowedVessels) {
							if (s instanceof Vessel) {
								expandedVessels.add(s);
							} else if (s instanceof VesselClass) {
								expandedVessels.addAll(SetUtils.getObjects(s));
							} else {
								expandedVessels.addAll(SetUtils.getObjects(s));
							}
						}
					}
					// create list to populate
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();
					final Pair<String, EObject> none = getEmptyObject();
					if (none != null) {
						result.add(0, none);
					}
					final Vessel currentValue = slot.getNominatedVessel();

					// filter the globally permissible values by the settings for this cargo
					for (final Pair<String, EObject> pair : baseResult) {
						final EObject vessel = pair.getSecond();

						boolean display = !noVesselsAllowed && (
						// show the option if the cargo allows this vessel-set
						// (an empty list of allowed vessels means "all vessels")
								expandedVessels.isEmpty() || expandedVessels.contains(vessel));

						// Always show the option if the option is the null option
						// or the current value for the cargo is set to this vessel-set
						if (Equality.isEqual(vessel, currentValue) || vessel == null) {
							display = true;
						}

						if (display) {
							result.add(pair);
						}
					}

					return result;
				}
				return baseResult;
			}

			// @Override
			protected Pair<String, EObject> getEmptyObject() {
				return new Pair<String, EObject>("<Unassigned>", null);
			}

			@Override
			public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
				return delegateFactory.getName(referer, feature, referenceValue);
			}

			@Override
			public boolean updateOnChangeToFeature(final Object changedFeature) {
				if (changedFeature == CargoPackage.eINSTANCE.getSlot_NominatedVessel()) {
					return true;
				}

				if (changedFeature == CargoPackage.eINSTANCE.getSlot_AllowedVessels()) {
					return true;
				}

				return delegateFactory.updateOnChangeToFeature(changedFeature);
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

		};
	}
}

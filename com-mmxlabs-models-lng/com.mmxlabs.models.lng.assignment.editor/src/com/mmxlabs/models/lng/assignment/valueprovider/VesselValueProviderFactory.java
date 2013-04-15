/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.valueprovider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.impl.CargoImpl;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;

public class VesselValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject theRootObject) {
		final MMXRootObject rootObject = theRootObject;
		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;
		final FleetPackage fleet = FleetPackage.eINSTANCE;

		if (referenceClass == types.getAVesselSet()) {
			return new MergedReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselClasses(), fleet.getFleetModel_Vessels()) {
				// @Override
				public List<Pair<String, EObject>> getAllowedValues(EObject target, final EStructuralFeature field) {
					// get a list of globally permissible values
					final List<Pair<String, EObject>> baseResult = super.getAllowedValues(target, field);

					// determine the current value for the target object
					final AVesselSet currentValue;
					{
						final ElementAssignment assignment;

						// if we were passed in an ElementAssignment, the target object is actually the
						// element assignment's owner, and we use the parameter to determine the current value
						if (target instanceof ElementAssignment) {
							assignment = (ElementAssignment) target;
							target = assignment.getAssignedObject();
						}
						// otherwise, use a default ElementAssignment to determine the current value
						else {
							final AssignmentModel assignmentModel = rootObject.getSubModel(AssignmentModel.class);
							if (assignmentModel != null)
								assignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, (UUIDObject) target);
							else
								assignment = null;
						}

						if (assignment == null)
							currentValue = null;
						else
							currentValue = assignment.getAssignment();
					}

					final EList<AVesselSet<Vessel>> allowedVessels;

					// populate the list of allowed vessels for the target object
					if (target instanceof CargoImpl) {
						final CargoImpl cargo = (CargoImpl) target;
						allowedVessels = cargo.getAllowedVessels();
					} else if (target instanceof VesselEvent) {
						final VesselEvent event = (VesselEvent) target;
						allowedVessels = event.getAllowedVessels();
					} else {
						allowedVessels = null;
					}

					// filter the global list by the object's allowed values
					if (allowedVessels != null) {

						// Expand out VesselGroups
						final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<AVesselSet<Vessel>>();
						for (final AVesselSet<Vessel> s : allowedVessels) {
							if (s instanceof Vessel) {
								expandedVessels.add(s);
							} else if (s instanceof VesselClass) {
								expandedVessels.add(s);
							} else {
								expandedVessels.addAll(SetUtils.getObjects(s));
							}
						}

						// create list to populate
						final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

						// filter the globally permissible values by the settings for this cargo
						for (final Pair<String, EObject> pair : baseResult) {
							final EObject vessel = pair.getSecond();

							// determine the vessel class (if any) for this option
							VesselClass vc = null;
							if (vessel instanceof Vessel) {
								vc = ((Vessel) vessel).getVesselClass();
							}

							final boolean display =
							// show the option if the cargo allows this vessel-set
							// (an empty list of allowed vessels means "all vessels")
							expandedVessels.isEmpty() || expandedVessels.contains(vessel)
							// show the option if the cargo allows vessels of this class
									|| (vc != null && expandedVessels.contains(vc))
									// show the option if the option is the null option
									// or the current value for the cargo is set to this vessel-set
									|| Equality.isEqual(vessel, currentValue) || vessel == null;

							if (display) {
								result.add(pair);
							}
						}

						return result;
					} else
						return baseResult;
				}

				@Override
				protected Pair<String, EObject> getEmptyObject() {
					return new Pair<String, EObject>("<Unassigned>", null);
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					return super.getName(referer, feature, referenceValue) + ((referenceValue instanceof VesselClass) ? " (spot)" : "");
				}

				@Override
				protected Comparator<Pair<String, ?>> createComparator() {
					final Comparator superValue = super.createComparator();

					return new Comparator<Pair<String, ?>>() {
						@Override
						public int compare(final Pair<String, ?> o1, final Pair<String, ?> o2) {
							final Object v1 = o1.getSecond();
							final Object v2 = o2.getSecond();
							if (v1 instanceof Vessel) {
								if (v2 instanceof VesselClass) {
									return -1;
								}
							} else if (v1 instanceof VesselClass) {
								if (v2 instanceof Vessel) {
									return 1;
								}
							}
							return superValue.compare(o1, o2);
						}

					};
				}

				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {
					if (changedFeature == CargoPackage.eINSTANCE.getCargo_AllowedVessels()) {
						return true;
					}

					if (changedFeature == FleetPackage.eINSTANCE.getVesselEvent_AllowedVessels()) {
						return true;
					}

					return super.updateOnChangeToFeature(changedFeature);
				}

			};
		}

		return null;
	}

}

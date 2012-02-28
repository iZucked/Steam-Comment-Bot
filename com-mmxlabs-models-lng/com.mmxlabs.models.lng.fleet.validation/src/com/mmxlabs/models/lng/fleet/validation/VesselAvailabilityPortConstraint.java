/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.ValidationSupport;

/**
 * Check that start/end requirement matches port constraint
 * 
 * @author hinton
 * 
 */
public class VesselAvailabilityPortConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailablility) {
			final VesselAvailablility availablility = (VesselAvailablility) target;

			final Pair<EObject, EReference> container = ValidationSupport.getInstance().getContainer(availablility);
			if (container.getFirst() instanceof Vessel) {
				final Vessel vessel = (Vessel) container.getFirst();
				final VesselClass vesselClass = (VesselClass) vessel.getVesselClass();
				final Set<APort> inaccessiblePortSet = SetUtils.getPorts(vesselClass.getInaccessiblePorts());
				if (!availablility.getStartAt().isEmpty()) {

					final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getStartAt());
					for (final APort p : availabilityPortSet) {

						if (inaccessiblePortSet.contains(p)) {

							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vessel.getName(), "start", vessel
									.getVesselClass().getName(), p.getName()));

							dcsd.addEObjectAndFeature(availablility, FleetPackage.eINSTANCE.getVesselAvailablility_StartAt());
							return dcsd;

						}
					}
				}
				if (!availablility.getEndAt().isEmpty()) {
					final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getEndAt());
					for (final APort p : availabilityPortSet) {

						if (inaccessiblePortSet.contains(p)) {

							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vessel.getName(), "end", vessel
									.getVesselClass().getName(), p.getName()));

							dcsd.addEObjectAndFeature(availablility, FleetPackage.eINSTANCE.getVesselAvailablility_EndAt());
							return dcsd;

						}
					}
				}
			}
		} else if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final MMXRootObject rootObject = ValidationSupport.getInstance().getParentObjectType(MMXRootObject.class, vesselClass);
			if (rootObject == null) {
				return ctx.createSuccessStatus();
			}

			final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);

			final HashSet<String> badPorts = new HashSet<String>();
			final List<String> badVessels = new LinkedList<String>();
			for (final Vessel v : fleetModel.getVessels()) {
				if (ValidationSupport.getInstance().isSame(v.getVesselClass(), vesselClass)) {

					final VesselAvailablility availablility = (VesselAvailablility) target;

					final Set<APort> inaccessiblePortSet = SetUtils.getPorts(vesselClass.getInaccessiblePorts());

					boolean bad = false;
					{
						final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getStartAt());
						for (final APort p : availabilityPortSet) {

							// No port match
							if (inaccessiblePortSet.contains(p)) {
								badPorts.add(p.getName());
								if (!bad) {

									bad = true;
									badVessels.add(v.getName());
								}
							}
						}
					}
					{
						final Set<APort> availabilityPortSet = SetUtils.getPorts(availablility.getEndAt());
						for (final APort p : availabilityPortSet) {

							// No port match
							if (inaccessiblePortSet.contains(p)) {
								badPorts.add(p.getName());
								bad = true;
								badVessels.add(v.getName());
							}
						}
					}

				}

			}

			if (badVessels.isEmpty() == false) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(badVessels, badPorts));
				dcsd.addEObjectAndFeature(vesselClass, FleetPackage.eINSTANCE.getVesselClass_InaccessiblePorts());
				return dcsd;
			}
		}
		return ctx.createSuccessStatus();
	}
}

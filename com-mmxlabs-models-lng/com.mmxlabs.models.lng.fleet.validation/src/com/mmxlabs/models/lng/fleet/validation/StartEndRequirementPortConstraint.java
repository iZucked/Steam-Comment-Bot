/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.mmxcore.validation.context.ValidationSupport;

/**
 * Check that start/end requirement matches port constraint
 * 
 * @author hinton
 * 
 */
public class StartEndRequirementPortConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailablility) {
			VesselAvailablility availablility = (VesselAvailablility)target;
			if (availablility.getStartAt().isEmpty()) {
				final String startOrEnd = "start";
				final Pair<EObject, EReference> container = ValidationSupport.getInstance().getContainer(availablility);
				if (container.getFirst() instanceof Vessel) {
					final Vessel vessel = (Vessel) container.getFirst();
					final VesselClass vesselClass = (VesselClass)vessel.getVesselClass();
					for (final APortSet portSet : availablility.getStartAt()) {
						for (APort p : portSet.collect(new UniqueEList<APortSet>())) {
							
							// No! port match 
							if (vesselClass.getInaccessiblePorts().contains(portSet)) {
								
								
							}
							
						}
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vessel.getName(), startOrEnd, vessel.getVesselClass()
								.getName(), pat.getPort().getName()));

						dcsd.addEObjectAndFeature(pat, FleetPackage.eINSTANCE.getPortAndTime_Port());
						return dcsd;
					}
				}
			}
			}
		} else if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final Scenario scenario = ValidationSupport.getInstance().getScenario(vesselClass);

			final HashSet<String> badPorts = new HashSet<String>();
			final List<String> badVessels = new LinkedList<String>();
			if (scenario == null) return ctx.createSuccessStatus();
			for (final Vessel v : scenario.getFleetModel().getFleet()) {
				if (ValidationSupport.getInstance().isSame(v.getClass_(), vesselClass)) {
					boolean bad = false;
					if (!isValid(v.getStartRequirement(), vesselClass)) {
						badPorts.add(v.getStartRequirement().getPort().getName());
						bad = true;
						badVessels.add(v.getName());
					}
					if (!isValid(v.getEndRequirement(), vesselClass)) {
						badPorts.add(v.getEndRequirement().getPort().getName());
						if (!bad)
							badVessels.add(v.getName());
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

	private boolean isValid(final List<Port> ports, final VesselClass vc) {
			return !(vc.getInaccessiblePorts().contains(port));
		}
		return true;
	}
}

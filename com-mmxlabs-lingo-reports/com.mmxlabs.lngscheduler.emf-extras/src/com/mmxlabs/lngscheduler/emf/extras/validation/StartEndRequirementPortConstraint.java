/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import scenario.Scenario;
import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.port.Port;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.validation.context.ValidationSupport;
import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

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
		if (target instanceof PortAndTime) {
			final PortAndTime pat = (PortAndTime) target;
			if (pat.isSetPort()) {
				final Pair<EObject, EReference> container = ValidationSupport.getInstance().getContainer(pat);
				if (container.getFirst() instanceof Vessel) {
					final Vessel vessel = (Vessel) container.getFirst();
					if (isValid(pat, vessel.getClass_()) == false) {
						final String startOrEnd = (container.getSecond() == FleetPackage.eINSTANCE.getVessel_StartRequirement() ? "start" : "end");
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vessel.getName(), startOrEnd, vessel.getClass_()
								.getName(), pat.getPort().getName()));

						dcsd.addEObjectAndFeature(pat, FleetPackage.eINSTANCE.getPortAndTime_Port());
						return dcsd;
					}
				}
			}
		} else if (target instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) target;
			final Scenario scenario = ValidationSupport.getInstance().getScenario(vesselClass);

			final HashSet<String> badPorts = new HashSet<String>();
			final List<String> badVessels = new LinkedList<String>();

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

	private boolean isValid(final PortAndTime pat, final VesselClass vc) {
		if (pat.isSetPort()) {
			final Port port = pat.getPort();
			return !(vc.getInaccessiblePorts().contains(port));
		}
		return true;
	}
}

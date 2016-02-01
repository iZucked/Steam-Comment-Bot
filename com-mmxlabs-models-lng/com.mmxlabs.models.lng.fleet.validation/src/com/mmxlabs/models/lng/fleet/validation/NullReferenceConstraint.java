/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A constraint which checks that references which shouldn't be null aren't.
 * 
 * @author Tom Hinton
 * 
 */
public class NullReferenceConstraint extends AbstractModelConstraint {
	private static final List<EReference> checkedReferences = CollectionsUtil.makeArrayList(
			// TODO add any more refs to check here
			FleetPackage.eINSTANCE.getVessel_VesselClass(), FleetPackage.eINSTANCE.getVesselClass_BaseFuel(), FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(),
			FleetPackage.eINSTANCE.getVesselClass_BallastAttributes(), FleetPackage.eINSTANCE.getVesselClassRouteParameters_Route());

	private static final HashMap<EClass, Set<EReference>> cacheByClass = new HashMap<EClass, Set<EReference>>();

	private static synchronized Set<EReference> getReferencesToCheck(final EClass targetClass) {
		Set<EReference> result = cacheByClass.get(targetClass);
		if (result == null) {
			result = new HashSet<EReference>(targetClass.getEAllReferences());
			result.retainAll(checkedReferences);
			cacheByClass.put(targetClass, result);
		}

		return result;
	}

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final LinkedList<EReference> errors = new LinkedList<EReference>();
		final Set<EReference> targetRefs = getReferencesToCheck(target.eClass());
		for (final EReference ref : targetRefs) {
			if ((target.eGet(ref) == null) && !(ref.isUnsettable() && (target.eIsSet(ref) == false))) {
				errors.add(ref);
			}
		}

		if (errors.isEmpty()) {
			return ctx.createSuccessStatus();
		} else {
			final StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (final EReference ref : errors) {
				if (!first) {
					sb.append(", ");
				}
				sb.append(ref.getName());
				first = false;
			}
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(sb.toString()));
			for (final EReference ref : errors) {
				dcsd.addEObjectAndFeature(target, ref);
			}
			return dcsd;
		}
	}
}

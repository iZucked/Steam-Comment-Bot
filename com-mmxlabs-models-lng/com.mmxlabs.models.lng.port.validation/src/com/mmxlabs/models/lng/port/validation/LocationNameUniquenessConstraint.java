/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class LocationNameUniquenessConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Location) {
			Location location = (Location) target;

			Port containerPort = (Port) location.eContainer();
			if (containerPort == null) {
				return;
			}

			Set<String> bad = (Set<String>) ctx.getCurrentConstraintData();
			if (bad == null) {
				bad = new HashSet<>();
				ctx.putCurrentConstraintData(bad);

				final List<EObject> objects = extraContext.getSiblings(containerPort);

				final Set<String> temp = new HashSet<>();
				for (final EObject eObj : objects) {
					if (eObj instanceof Port) {
						Port p = (Port) eObj;
						Location no = p.getLocation();
						if (no != null) {
							final String n = (String) no.eGet(MMXCorePackage.eINSTANCE.getNamedObject_Name());
							if (n != null) {
								if (temp.contains(n.toLowerCase())) {
									bad.add(n);
								}
								temp.add(n.toLowerCase());
							}
						}
					}
				}
			}

			final String name = (String) target.eGet(MMXCorePackage.eINSTANCE.getNamedObject_Name());
			if (bad.contains(name)) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(target.eClass().getName() + " has non-unique name (names are case insensitive) " + name));
				dsd.addEObjectAndFeature(target, MMXCorePackage.eINSTANCE.getNamedObject_Name());
				statuses.add(dsd);
			}
		}
	}
}

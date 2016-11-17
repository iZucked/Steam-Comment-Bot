/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PortCountryGroupConstraints extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof PortCountryGroup) {
			final PortCountryGroup portCountryGroup = (PortCountryGroup) target;

			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

				final Set<String> countryNames = new HashSet<>();
				final Set<String> portNames = new HashSet<>();

				for (final Port p : portModel.getPorts()) {
					final String portName = p.getName();
					if (portName != null && !portName.isEmpty()) {
						portNames.add(portName);
					}
					final Location location = p.getLocation();
					if (location != null) {
						final String country = location.getCountry();
						if (country != null && !country.isEmpty()) {
							countryNames.add(country);
						}
					}
				}
				final String name = portCountryGroup.getName();
				if (name != null && !name.isEmpty()) {
					if (!countryNames.contains(name)) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("Country group %s has no ports", name)), IStatus.WARNING);
						dsd.addEObjectAndFeature(portCountryGroup, MMXCorePackage.eINSTANCE.getNamedObject_Name());
						failures.add(dsd);
					}
					if (portNames.contains(name)) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("Country group %s has the same name a port", name)), IStatus.ERROR);
						dsd.addEObjectAndFeature(portCountryGroup, MMXCorePackage.eINSTANCE.getNamedObject_Name());
						failures.add(dsd);
					}

				}

			}
		}

		return Activator.PLUGIN_ID;
	}
}

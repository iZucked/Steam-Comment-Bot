/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CargoTypeAssignmentConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof ElementAssignment) {
			final ElementAssignment assignment = (ElementAssignment) object;

			final UUIDObject uuidObject = assignment.getAssignedObject();
			if (assignment.getAssignment() == null) {
				return Activator.PLUGIN_ID;
			}
			final AVesselSet<Vessel> vessel = assignment.getAssignment();

			Cargo cargo = null;
			if (uuidObject instanceof LoadSlot) {
				cargo = ((LoadSlot) uuidObject).getCargo();
			} else if (uuidObject instanceof DischargeSlot) {
				cargo = ((DischargeSlot) uuidObject).getCargo();
			} else if (uuidObject instanceof Cargo) {
				cargo = (Cargo) uuidObject;
			}
			if (cargo != null) {

				final Set<Vessel> scenarioVessels = new HashSet<>();
				final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
				final MMXRootObject rootObject = extraValidationContext.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
					final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
					final ScenarioFleetModel scenarioFleetModel = portfolioModel.getScenarioFleetModel();
					for (final VesselAvailability va : scenarioFleetModel.getVesselAvailabilities()) {
						scenarioVessels.add(va.getVessel());
					}
				}

				if (cargo.getCargoType() == CargoType.FLEET) {
					if (vessel instanceof Vessel) {
						if (!scenarioVessels.contains(vessel)) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("None fleet cargo " + cargo.getName()
									+ " is assigned to non-scenario vessel " + vessel.getName()));
							failure.addEObjectAndFeature(cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name());

							failures.add(failure);
						}
					}
				} else { // FOD/DES cargo
					if (vessel instanceof Vessel) {
						if (scenarioVessels.contains(vessel)) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("None fleet cargo " + cargo.getName()
									+ " is assigned to scenario vessel " + vessel.getName() + "."));
							failure.addEObjectAndFeature(cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name());

							failures.add(failure);
						}
					} else if (vessel != null) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("None fleet cargo " + cargo.getName()
								+ " can only be assigned to a specific vessel"));
						failure.addEObjectAndFeature(cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name());

						failures.add(failure);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}

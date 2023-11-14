/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.time.Year;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CIIEndOptions;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.cii.CIIComparator;
import com.mmxlabs.models.lng.schedule.cii.CIIGradeFinder;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VesselCharterCIIEndOptionsDesiredGradeConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		doValidateCIIEndOptionsDesiredGrade(ctx, extraContext, failures);
	}

	private void doValidateCIIEndOptionsDesiredGrade(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		if (ctx.getTarget() instanceof final CIIEndOptions ciiEndOptions && ciiEndOptions.eContainer() instanceof VesselCharter vesselCharter //
				&& vesselCharter.getEndBy() != null) {
			final Year vesselCharterEndYear = Year.from(vesselCharter.getEndBy());
			final Vessel vessel = vesselCharter.getVessel();
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (vessel != null && rootObject instanceof final LNGScenarioModel lngScenarioModel) {
				final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
				final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
				final CIIReferenceData ciiReferenceData = fleetModel.getCiiReferences();
				final String ciiGrade = CIIGradeFinder.findCIIGradeForScheduleVesselYear(scheduleModel, ciiReferenceData, vessel, vesselCharterEndYear);
				final String desiredCIIGrade = ciiEndOptions.getDesiredCIIGrade();
				if (ciiGrade != null && !ciiGrade.isBlank()//
						&& desiredCIIGrade != null && !desiredCIIGrade.isBlank()) {
					if (CIIComparator.compareGrades(ciiGrade, ciiEndOptions.getDesiredCIIGrade()) > 0) {
						final String message = "The desired CII rating is not achieved";
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message), IStatus.WARNING);
						failure.addEObjectAndFeature(ciiEndOptions, CargoPackage.eINSTANCE.getCIIEndOptions_DesiredCIIGrade());
						failures.add(failure);
					}
				}
			}
		}
	}
}

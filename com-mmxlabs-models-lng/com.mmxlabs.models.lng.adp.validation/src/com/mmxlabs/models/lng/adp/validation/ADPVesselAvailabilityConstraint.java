/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ADPVesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

//		final EObject target = ctx.getTarget();
//		if (target instanceof VesselAvailability) {
//			final VesselAvailability va = (VesselAvailability) target;
//
//			final Vessel vessel = va.getVessel();
//			final String vesselName = ScenarioElementNameHelper.getName(vessel, "<Unknown>");
//
//			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
//					.withTypedName(ScenarioElementNameHelper.getTypeName(va), vesselName);
//
//			final ADPModel adpModel = ScenarioModelUtil.getADPModel(extraContext.getScenarioDataProvider());
//			if (adpModel != null) {
//				final LocalDateTime start = adpModel.getYearStart().atDay(1).atStartOfDay();
//				final LocalDateTime end = adpModel.getYearEnd().plusMonths(1).atDay(1).atStartOfDay();
//				if (va.getStartAfter() == null || !va.getStartAfter().equals(start)) {
//					factory.copyName() //
//							.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER) //
//							.withMessage("Vessel start date does not match ADP start.") //
//							.make(ctx, statuses);
//				}
//				if (va.getEndBy() == null || !va.getEndBy().equals(end)) {
//					factory.copyName() //
//							.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY) //
//							.withMessage("Vessel end date does not match ADP end.") //
//							.make(ctx, statuses);
//				}
//			}
//		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class FleetProfileVesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			VesselAvailability va = (VesselAvailability) target;

			DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName("ADP Fleet profile");

			EObject eContainer = va.eContainer();
			if (eContainer == null) {
				eContainer = extraContext.getContainer(target);
			}
			if (eContainer instanceof FleetProfile) {
				eContainer = eContainer.eContainer();
			}
			if (eContainer instanceof ADPModel) {
				ADPModel adpModel = (ADPModel) eContainer;
				LocalDateTime start = adpModel.getYearStart().atDay(1).atStartOfDay();
				LocalDateTime end = adpModel.getYearEnd().plusMonths(1).atDay(1).atStartOfDay();
				if (va.getStartAfter() == null || !va.getStartAfter().equals(start)) {
					factory.copyName() //
							.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER) //
							.withMessage("Vessel start date does not match ADP start.") //
							.make(ctx, statuses);
				}
				if (va.getEndBy() == null || !va.getEndBy().equals(end)) {
					factory.copyName() //
							.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY) //
							.withMessage("Vessel end date does not match ADP end.") //
							.make(ctx, statuses);
				}
			}
		}
	}
}

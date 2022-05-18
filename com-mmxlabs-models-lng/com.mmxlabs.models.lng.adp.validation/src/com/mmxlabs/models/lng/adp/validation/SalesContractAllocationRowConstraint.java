/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.presentation.customisation.IInventoryBasedGenerationPresentationCustomiser;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.rcp.common.ServiceHelper;

public class SalesContractAllocationRowConstraint extends AbstractModelMultiConstraint {

	private final String typedName;

	public SalesContractAllocationRowConstraint() {
		final IInventoryBasedGenerationPresentationCustomiser[] customiserArr = new IInventoryBasedGenerationPresentationCustomiser[1];
		ServiceHelper.withOptionalServiceConsumer(IInventoryBasedGenerationPresentationCustomiser.class, v -> customiserArr[0] = v);
		typedName = customiserArr[0] != null ? customiserArr[0].getDropDownMenuLabel() : "Inventory-based generation";
	}

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final SalesContractAllocationRow salesContractAllocationRow) {
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", typedName) //
					.withTag(ValidationConstants.TAG_ADP);

			if (salesContractAllocationRow.getContract() == null) {
				factory.copyName() //
						.withObjectAndFeature(salesContractAllocationRow, ADPPackage.Literals.SALES_CONTRACT_ALLOCATION_ROW__CONTRACT) //
						.withMessage("No sales contract selected") //
						.make(ctx, statuses);
			}

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) extraContext.getRootObject();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
			final Set<Vessel> fleetVessels = cargoModel.getVesselAvailabilities().stream().map(VesselAvailability::getVessel).collect(Collectors.toSet());
			final List<Vessel> vessels = salesContractAllocationRow.getVessels();
			for (final Vessel vessel : vessels) {
				if (!fleetVessels.contains(vessel)) {
					factory.copyName() //
							.withObjectAndFeature(salesContractAllocationRow, ADPPackage.Literals.MULL_ALLOCATION_ROW__VESSELS) //
							.withMessage(String.format("No vessel charter for %s.", vessel.getName())) //
							.make(ctx, statuses);
				}
			}
		}
	}
}
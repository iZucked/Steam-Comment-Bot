/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class VesselAvailabilityBallastBonusImporter extends DefaultClassImporter {
	static final String VESSEL_NAME_FIELD = "vessel";
	static final String CHARTER_NUMBER_FIELD = "charterno";

	@Override
	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Map<String, String> result = super.exportObject(object, context);

		if (object instanceof BallastBonusContractLine) {
			EObject parent = object.eContainer();
			if (parent instanceof RuleBasedBallastBonusContract) {
				EObject parentParent = parent.eContainer();
				if (parentParent instanceof VesselAvailability) {
					VesselAvailability vesselAvailability = (VesselAvailability) parentParent;
					Vessel vessel = vesselAvailability.getVessel();
					if (vessel != null) {
						result.put(VESSEL_NAME_FIELD, vessel.getName());
					}
					result.put(CHARTER_NUMBER_FIELD, Integer.toString(vesselAvailability.getCharterNumber()));
				}
			}
		}

		return result;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final ImportResults result = super.importObject(parent, eClass, row, context);
		final String vesselName = row.get(VESSEL_NAME_FIELD);
		final String charterNoStr = row.get(CHARTER_NUMBER_FIELD);
		int charterNo = 0;
		if (charterNoStr != null && !charterNoStr.isEmpty()) {
			charterNo = Integer.parseInt(charterNoStr);
		}
		int pCharterNo = charterNo;

		final EObject object = result.importedObject;

		if (object instanceof BallastBonusContractLine && vesselName != null) {
			context.doLater(new IDeferment() {

				@Override
				public void run(final IImportContext importContext) {
					final IMMXImportContext context = (IMMXImportContext) importContext;
					MMXRootObject rootObject = context.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {
						LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
						CargoModel cargoModel = scenarioModel.getCargoModel();
						Vessel targetVessel = (Vessel) context.getNamedObject(vesselName, FleetPackage.Literals.VESSEL);
						for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
							if (vesselAvailability.getVessel() == targetVessel) {
								if (vesselAvailability.getCharterNumber() == pCharterNo) {

									BallastBonusContract contract = vesselAvailability.getBallastBonusContract();
									if (contract == null) {
										RuleBasedBallastBonusContract ruleBasedBallastBonusContract = CommercialFactory.eINSTANCE.createRuleBasedBallastBonusContract();
										vesselAvailability.setBallastBonusContract(ruleBasedBallastBonusContract);
										contract = ruleBasedBallastBonusContract;
									}
									if (contract instanceof RuleBasedBallastBonusContract) {
										RuleBasedBallastBonusContract ruleBasedBallastBonusContract = (RuleBasedBallastBonusContract) contract;
										ruleBasedBallastBonusContract.getRules().add((BallastBonusContractLine) object);
									}
									break;
								}
							}
						}
					}
				}

				@Override
				public int getStage() {
					return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
				}

			});
		}

		return result;
	}

}

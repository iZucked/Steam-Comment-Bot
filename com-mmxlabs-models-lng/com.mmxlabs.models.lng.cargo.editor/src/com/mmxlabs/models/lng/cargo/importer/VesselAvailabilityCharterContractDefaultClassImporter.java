/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class VesselAvailabilityCharterContractDefaultClassImporter extends DefaultClassImporter {
	static final String VESSEL_NAME_FIELD = "vessel";
	static final String CHARTER_NUMBER_FIELD = "charterno";
	
	@Override
	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Map<String, String> result = super.exportObject(object, context);
		
		final EObject parent;
		if (object instanceof BallastBonusTerm || object instanceof RepositioningFeeTerm) {
			parent = object.eContainer() != null ? object.eContainer().eContainer() : null;
		} else if (object instanceof IBallastBonus || object instanceof IRepositioningFee) {
			parent = object.eContainer();
		} else {
			parent = object;
		}
		
		if (parent instanceof GenericCharterContract) {
			exportWithVesselNameAndCharterNumber(result, parent);
		}
		
		return result;
	}

	private void exportWithVesselNameAndCharterNumber(final Map<String, String> result, EObject parent) {
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

		if (object != null && vesselName != null) {
			context.doLater(new VesselAvailabilityCharterContractImporterDeferment(pCharterNo, vesselName, object));
		}

		return result;
	}
	
	private class VesselAvailabilityCharterContractImporterDeferment implements IDeferment {
		private final int pCharterNumber;
		private final String vesselName;
		private final EObject object;
		
		public VesselAvailabilityCharterContractImporterDeferment(final int pCharterNumber, final String vesselName, final EObject object) {
			this.object = object;
			this.pCharterNumber = pCharterNumber;
			this.vesselName = vesselName;
		}

		@Override
		public void run(final IImportContext importContext) {
			final IMMXImportContext context = (IMMXImportContext) importContext;
			final MMXRootObject rootObject = context.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
				CargoModel cargoModel = scenarioModel.getCargoModel();
				Vessel targetVessel = (Vessel) context.getNamedObject(vesselName, FleetPackage.Literals.VESSEL);
				
				for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
					if (vesselAvailability.getVessel() == targetVessel) {
						if (vesselAvailability.getCharterNumber() == pCharterNumber) {
							if (object instanceof GenericCharterContract) {
								vesselAvailability.setContainedCharterContract((GenericCharterContract) object);
								break;
							}
							
							GenericCharterContract contract = vesselAvailability.getContainedCharterContract();
							if (contract == null) {
								contract = CommercialFactory.eINSTANCE.createGenericCharterContract();
								contract.setName("-");
								vesselAvailability.setContainedCharterContract(contract);
							} else {
								if (contract.getName() == null || contract.getName().isEmpty()) {
									contract.setName("-");
								}
							}
							
							if (object instanceof MonthlyBallastBonusTerm) {
								final MonthlyBallastBonusContainer mbbContainer;
								if (contract.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer) {
									mbbContainer = (MonthlyBallastBonusContainer) contract.getBallastBonusTerms();
								} else if (contract.getBallastBonusTerms() == null) {
									mbbContainer = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContainer();
									contract.setBallastBonusTerms(mbbContainer);
								} else {
									throw new IllegalArgumentException(String.format(
											"Error importing vessel availability charter contract! "
											+ "%n Imported ballast bonus container is of different type to the one on the contract [%s]!", contract.getName()));
								}
								
								mbbContainer.getTerms().add((MonthlyBallastBonusTerm) object);
							} else if (object instanceof LumpSumBallastBonusTerm || object instanceof NotionalJourneyBallastBonusTerm) {
								final SimpleBallastBonusContainer bbContainer;
								if (contract.getBallastBonusTerms() instanceof SimpleBallastBonusContainer) {
									bbContainer = (SimpleBallastBonusContainer) contract.getBallastBonusTerms();
								} else if (contract.getBallastBonusTerms() == null) {
									bbContainer = CommercialFactory.eINSTANCE.createSimpleBallastBonusContainer();
									contract.setBallastBonusTerms(bbContainer);
								} else {
									throw new IllegalArgumentException(String.format(
											"Error importing vessel availability charter contract! "
											+ "%n Imported ballast bonus container is of different type to the one on the contract [%s]!", contract.getName()));
								}
								
								bbContainer.getTerms().add((BallastBonusTerm) object);
							} else if (object instanceof LumpSumRepositioningFeeTerm || object instanceof OriginPortRepositioningFeeTerm) {
								final SimpleRepositioningFeeContainer rfContainer;
								if (contract.getRepositioningFeeTerms() instanceof SimpleRepositioningFeeContainer) {
									rfContainer = (SimpleRepositioningFeeContainer) contract.getRepositioningFeeTerms();
								} else if (contract.getRepositioningFeeTerms() == null) {
									rfContainer = CommercialFactory.eINSTANCE.createSimpleRepositioningFeeContainer();
									contract.setRepositioningFeeTerms(rfContainer);
								} else {
									throw new IllegalArgumentException(String.format(
											"Error importing vessel availability charter contract! "
											+ "%n Imported ballast bonus container is of different type to the one on the contract [%s]!", contract.getName()));
								}
								
								rfContainer.getTerms().add((RepositioningFeeTerm) object);
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
	}
}

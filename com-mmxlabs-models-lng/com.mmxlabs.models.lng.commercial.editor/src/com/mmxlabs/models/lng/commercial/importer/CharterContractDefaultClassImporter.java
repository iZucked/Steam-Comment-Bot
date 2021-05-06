/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CharterContractDefaultClassImporter extends DefaultClassImporter {
	static final String CONTRACT_NAME_FIELD = "contract";

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
			result.put(CONTRACT_NAME_FIELD, ((GenericCharterContract) parent).getName());
		}

		return result;
	}
	

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final ImportResults result = super.importObject(parent, eClass, row, context);
		final String contractName = row.get(CONTRACT_NAME_FIELD);

		final EObject object = result.importedObject;

		if (object != null && contractName != null) {
			context.doLater(new CharterContractImporterDeferment(contractName, object));
		}

		return result;
	}

	private class CharterContractImporterDeferment implements IDeferment {
		private final EObject object;
		private final String contractName;

		public CharterContractImporterDeferment(final String contractName, final EObject object) {
			this.object = object;
			this.contractName = contractName;
		}

		@Override
		public void run(final IImportContext importContext) {
			final IMMXImportContext context = (IMMXImportContext) importContext;
			final MMXRootObject rootObject = context.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
				final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
				GenericCharterContract targetCharterContract = (GenericCharterContract) context.getNamedObject(contractName, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT);

				for (final GenericCharterContract contract : commercialModel.getCharterContracts()) {
					if (contract == targetCharterContract) {

						if (object instanceof IBallastBonus) {
							contract.setBallastBonusTerms((IBallastBonus) object);
						} else if (object instanceof IRepositioningFee) {
							contract.setRepositioningFeeTerms((IRepositioningFee) object);
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
										"Error importing charter contract! "
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
										"Error importing charter contract! "
												+ "%n Imported ballast bonus container is of different type to the one on the contract [%s]!", contract.getName()));
							}

							bbContainer.getTerms().add((BallastBonusTerm) object);
						} else if (object instanceof LumpSumRepositioningFeeTerm || object instanceof OriginPortRepositioningFeeTerm) {
							final SimpleRepositioningFeeContainer rfContainer;
							if (contract.getBallastBonusTerms() instanceof SimpleRepositioningFeeContainer) {
								rfContainer = (SimpleRepositioningFeeContainer) contract.getRepositioningFeeTerms();
							} else if (contract.getBallastBonusTerms() == null) {
								rfContainer = CommercialFactory.eINSTANCE.createSimpleRepositioningFeeContainer();
								contract.setRepositioningFeeTerms(rfContainer);
							} else {
								throw new IllegalArgumentException(String.format(
										"Error importing charter contract! "
												+ "%n Imported ballast bonus container is of different type to the one on the contract [%s]!", contract.getName()));
							}

							rfContainer.getTerms().add((RepositioningFeeTerm) object);
						}

						break;

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
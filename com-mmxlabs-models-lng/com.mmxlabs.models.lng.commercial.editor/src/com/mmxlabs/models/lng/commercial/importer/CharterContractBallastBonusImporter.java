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
import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CharterContractBallastBonusImporter extends DefaultClassImporter {
	static final String CONTRACT_NAME_FIELD = "contract";

	@Override
	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Map<String, String> result = super.exportObject(object, context);

		if (object instanceof BallastBonusContractLine) {
			EObject parent = object.eContainer();
			if (parent instanceof RuleBasedBallastBonusContract) {
				EObject parentParent = parent.eContainer();
				if (parentParent instanceof BallastBonusCharterContract) {
					BallastBonusCharterContract contract = (BallastBonusCharterContract) parentParent;
					if (contract != null) {
						result.put(CONTRACT_NAME_FIELD, contract.getName());
					}
				}
			}
		}

		return result;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final ImportResults result = super.importObject(parent, eClass, row, context);
		final String contractName = row.get(CONTRACT_NAME_FIELD);

		final EObject object = result.importedObject;

		if (object instanceof BallastBonusContractLine && contractName != null) {
			context.doLater(new IDeferment() {

				@Override
				public void run(final IImportContext importContext) {
					final IMMXImportContext context = (IMMXImportContext) importContext;
					CharterContract targetCharterContract = (CharterContract) context.getNamedObject(contractName, CommercialPackage.Literals.CHARTER_CONTRACT);
					if (targetCharterContract instanceof BallastBonusCharterContract) {
						BallastBonusCharterContract ballastBonusCharterContract = (BallastBonusCharterContract) targetCharterContract;
						BallastBonusContract contract = ballastBonusCharterContract.getBallastBonusContract();
						if (contract == null) {
							RuleBasedBallastBonusContract ruleBasedBallastBonusContract = CommercialFactory.eINSTANCE.createRuleBasedBallastBonusContract();
							ballastBonusCharterContract.setBallastBonusContract(ruleBasedBallastBonusContract);
							contract = ruleBasedBallastBonusContract;
						}
						if (contract instanceof RuleBasedBallastBonusContract) {
							RuleBasedBallastBonusContract ruleBasedBallastBonusContract = (RuleBasedBallastBonusContract) contract;
							ruleBasedBallastBonusContract.getRules().add((BallastBonusContractLine) object);
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

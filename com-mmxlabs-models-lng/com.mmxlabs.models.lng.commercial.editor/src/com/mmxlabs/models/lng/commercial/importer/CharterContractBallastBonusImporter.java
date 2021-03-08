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
import com.mmxlabs.models.lng.commercial.CharterContractTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CharterContractBallastBonusImporter extends DefaultClassImporter {
	static final String CONTRACT_NAME_FIELD = "contract";

	@Override
	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Map<String, String> result = super.exportObject(object, context);

		if (object instanceof CharterContractTerm) {
			EObject parent = object.eContainer();
			if (parent instanceof GenericCharterContract) {
				result.put(CONTRACT_NAME_FIELD, ((GenericCharterContract) parent).getName());
			}
		}

		return result;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final ImportResults result = super.importObject(parent, eClass, row, context);
		final String contractName = row.get(CONTRACT_NAME_FIELD);

		final EObject object = result.importedObject;

		if (object instanceof CharterContractTerm && contractName != null) {
			context.doLater(new IDeferment() {

				@Override
				public void run(final IImportContext importContext) {
					final IMMXImportContext context = (IMMXImportContext) importContext;
					GenericCharterContract targetCharterContract = (GenericCharterContract) context.getNamedObject(contractName, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT);
					if (targetCharterContract != null) {
						targetCharterContract.getTerms().add((CharterContractTerm) object);
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

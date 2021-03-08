/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.commercial.CharterContractTerm;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class CharterContractBallastBonusImporterExtraImporter implements IExtraModelImporter {

	public static final String BALLASTBONUS_KEY = "CHARTER_BALLASTBONUS";
	public static final String BALLASTBONUS_DEFAULT_NAME = "Charter Contracts--Ballast Bonus";
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	/** Use a special case importer: don't go via the registry. */
	private final IClassImporter extraImporter = new CharterContractBallastBonusImporter();

	static {
		inputs.put(BALLASTBONUS_KEY, BALLASTBONUS_DEFAULT_NAME);
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(lngScenarioModel);

			final CSVReader reader = inputs.get(BALLASTBONUS_KEY);
			if (reader != null) {
				final Collection<EObject> importObjects = extraImporter.importObjects(CommercialPackage.Literals.CHARTER_CONTRACT_TERM, reader, context);
			}
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, IMMXExportContext context) {
		MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(lngScenarioModel);
			List<CharterContractTerm> exports = new ArrayList<>();

			for (final GenericCharterContract charterContract : commercialModel.getCharteringContracts()) {
				exports.addAll(charterContract.getTerms());
			}
			output.put(BALLASTBONUS_KEY, extraImporter.exportObjects(exports, context));
		}
	}
}

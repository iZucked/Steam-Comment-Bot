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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.commercial.util.CharterContractConstants;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 * Look at com.mmxlabs.models.lng.cargo.importer.VesselAvailabilityBallastBonusImporterExtraImporter
 * @author FM
 *
 */
public class CharterContractIExtraModelImporter implements IExtraModelImporter {

	static final Map<String, String> inputs = new LinkedHashMap<String, String>();
	static final Map<@NonNull String, @NonNull EClass> keys = new LinkedHashMap<>();

	/** Use a special case importer: don't go via the registry. */
	private final IClassImporter extraImporter = new CharterContractDefaultClassImporter();

	static {
		inputs.put(CharterContractConstants.BALLAST_BONUS_KEY, CharterContractConstants.CHARTER_CONTRACT_BALLAST_BONUS_DEFAULT_NAME);
		inputs.put(CharterContractConstants.BALLAST_BONUS_CONTAINER_KEY, CharterContractConstants.CHARTER_CONTRACT_BALLAST_BONUS_CONTAINER_DEFAULT_NAME);
		inputs.put(CharterContractConstants.REPOSITIONING_FEE_KEY, CharterContractConstants.CHARTER_CONTRACT_REPOSITIONING_FEE_DEFAULT_NAME);
		inputs.put(CharterContractConstants.REPOSITIONING_FEE_CONTAINER_KEY, CharterContractConstants.CHARTER_CONTRACT_REPOSITIONING_FEE_CONTAINER_DEFAULT_NAME);

		keys.put(CharterContractConstants.CHARTER_CONTRACT_KEY, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT);
		keys.put(CharterContractConstants.BALLAST_BONUS_KEY, CommercialPackage.Literals.BALLAST_BONUS_TERM);
		keys.put(CharterContractConstants.BALLAST_BONUS_CONTAINER_KEY, CommercialPackage.Literals.IBALLAST_BONUS);
		keys.put(CharterContractConstants.REPOSITIONING_FEE_KEY, CommercialPackage.Literals.REPOSITIONING_FEE_TERM);
		keys.put(CharterContractConstants.REPOSITIONING_FEE_CONTAINER_KEY, CommercialPackage.Literals.IREPOSITIONING_FEE);
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		if (rootObject instanceof LNGScenarioModel) {
			for (Map.Entry<@NonNull String, @NonNull EClass> entry : keys.entrySet()) {
				final CSVReader reader = inputs.get(entry.getKey());
				if (reader != null) {
					extraImporter.importObjects(entry.getValue(), reader, context);
				}
			}
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, IMMXExportContext context) {
		MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(lngScenarioModel);

			List<IBallastBonus> bbExportContainers = new ArrayList<>();
			List<BallastBonusTerm> bbExports = new ArrayList<>();
			List<APortSet<Port>> mbbExports = new ArrayList<>();
			List<IRepositioningFee> rfExportContainers = new ArrayList<>();
			List<RepositioningFeeTerm> rfExports = new ArrayList<>();
			List<GenericCharterContract> ccList = new ArrayList<>();

			for (final GenericCharterContract gcc : commercialModel.getCharterContracts()) {
				if (gcc != null) {
					ccList.add(gcc);
					IBallastBonus bb = gcc.getBallastBonusTerms();
					if (bb != null) {
						bbExportContainers.add(bb);
						if (bb instanceof SimpleBallastBonusContainer) {
							bbExports.addAll(((SimpleBallastBonusContainer) bb).getTerms());
						} else if (bb instanceof MonthlyBallastBonusContainer) {
							bbExports.addAll(((MonthlyBallastBonusContainer) bb).getTerms());
							mbbExports.addAll(((MonthlyBallastBonusContainer) bb).getHubs());
						}
					}
					IRepositioningFee rf = gcc.getRepositioningFeeTerms();
					if (rf != null) {
						rfExportContainers.add(rf);
						if (rf instanceof SimpleRepositioningFeeContainer) {
							rfExports.addAll(((SimpleRepositioningFeeContainer) rf).getTerms());
						}
					}
				}

			}

			output.put(CharterContractConstants.CHARTER_CONTRACT_KEY, extraImporter.exportObjects(ccList, context));
			output.put(CharterContractConstants.BALLAST_BONUS_CONTAINER_KEY, extraImporter.exportObjects(bbExportContainers, context));
			output.put(CharterContractConstants.BALLAST_BONUS_KEY, extraImporter.exportObjects(bbExports, context));
			output.put(CharterContractConstants.REPOSITIONING_FEE_CONTAINER_KEY, extraImporter.exportObjects(rfExportContainers, context));
			output.put(CharterContractConstants.REPOSITIONING_FEE_KEY, extraImporter.exportObjects(rfExports, context));

		}
	}
}

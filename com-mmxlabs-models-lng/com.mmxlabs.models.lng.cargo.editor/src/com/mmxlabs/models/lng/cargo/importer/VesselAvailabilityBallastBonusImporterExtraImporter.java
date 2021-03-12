/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class VesselAvailabilityBallastBonusImporterExtraImporter implements IExtraModelImporter {

	public static final String CHARTER_CONTRACT_KEY = "CHARTERCONTRACT";
	public static final String CHARTER_CONTRACT_DEFAULT_NAME = "Vessel Availability--Charter Contract";
	public static final String BALLAST_BONUS_KEY = "BALLASTBONUS";
	public static final String BALLAST_BONUS_DEFAULT_NAME = "Vessel Availability--Ballast Bonus";
	private static final String BALLAST_BONUS_CONTAINER_KEY = "BALLASTBONUSCONTAINER";
	private static final String BALLAST_BONUS_CONTAINER_DEFAULT_NAME = "Vessel Availability--Ballast Bonus Container";
	private static final String MONTHLY_BALLAST_BONUS_CONTAINER_KEY = "MONTHLY_BALLAST_BONUS_HUBS";
	private static final String MONTHLY_BALLAST_BONUS_CONTAINER_DEFAULT_NAME = "Vessel Availability--Monthly Ballast Bonus Hubs";
	public static final String REPOSITIONING_FEE_KEY = "REPOSITIONINGFEE";
	public static final String REPOSITIONING_FEE_DEFAULT_NAME = "Vessel Availability--Repositioning Fee";
	private static final String REPOSITIONING_FEE_CONTAINER_KEY = "REPOSITIONINGFEECONTAINER";
	private static final String REPOSITIONING_FEE_CONTAINER_DEFAULT_NAME = "Vessel Availability--Repositioning Fee Container";
	static final Map<String, String> inputs = new LinkedHashMap<String, String>();
	static final Map<@NonNull String, @NonNull EClass> keys = new LinkedHashMap<>();

	/** Use a special case importer: don't go via the registry. */
	private final IClassImporter expressionImporter = new VesselAvailabilityCharterContractImporter();

	static {
		inputs.put(BALLAST_BONUS_KEY, BALLAST_BONUS_DEFAULT_NAME);
		inputs.put(BALLAST_BONUS_CONTAINER_KEY, BALLAST_BONUS_CONTAINER_DEFAULT_NAME);
		inputs.put(REPOSITIONING_FEE_KEY, REPOSITIONING_FEE_DEFAULT_NAME);
		inputs.put(REPOSITIONING_FEE_CONTAINER_KEY, REPOSITIONING_FEE_CONTAINER_DEFAULT_NAME);
		
		keys.put(CHARTER_CONTRACT_KEY, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT);
		keys.put(BALLAST_BONUS_KEY, CommercialPackage.Literals.BALLAST_BONUS_TERM);
		keys.put(BALLAST_BONUS_CONTAINER_KEY, CommercialPackage.Literals.IBALLAST_BONUS);
		//keys.put(MONTHLY_BALLAST_BONUS_CONTAINER_KEY, )
		keys.put(REPOSITIONING_FEE_KEY, CommercialPackage.Literals.REPOSITIONING_FEE_TERM);
		keys.put(REPOSITIONING_FEE_CONTAINER_KEY, CommercialPackage.Literals.IREPOSITIONING_FEE);
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			if (cargoModel != null) {
				for (Map.Entry<@NonNull String, @NonNull EClass> entry : keys.entrySet()) {
					final CSVReader reader = inputs.get(entry.getKey());
					if (reader != null) {
						expressionImporter.importObjects(entry.getValue(), reader, context);
					}
				}
			}
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, IMMXExportContext context) {
		MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			if (cargoModel != null) {
				List<IBallastBonus> bbExportContainers = new ArrayList<>();
				List<BallastBonusTerm> bbExports = new ArrayList<>();
				List<IRepositioningFee> rfExportContainers = new ArrayList<>();
				List<RepositioningFeeTerm> rfExports = new ArrayList<>();
				List<GenericCharterContract> ccList = new ArrayList<>();
				

				for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
					if (vesselAvailability != null) {
						GenericCharterContract gcc = vesselAvailability.getContainedCharterContract();
						if (gcc != null) {
							ccList.add(gcc);
							IBallastBonus bb = gcc.getBallastBonusTerms();
							if (bb != null) {
								bbExportContainers.add(bb);
								if (bb instanceof SimpleBallastBonusContainer) {
									bbExports.addAll(((SimpleBallastBonusContainer) bb).getTerms());
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
				}
				
				output.put(BALLAST_BONUS_CONTAINER_KEY, expressionImporter.exportObjects(bbExportContainers, context));
				output.put(BALLAST_BONUS_KEY, expressionImporter.exportObjects(bbExports, context));
			}
		}
	}
}

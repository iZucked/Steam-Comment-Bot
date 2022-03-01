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
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.commercial.util.CharterContractConstants;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class VesselAvailabilityCharterContractIExtraModelImporter implements IExtraModelImporter {

	static final Map<String, String> inputs = new LinkedHashMap<>();
	static final Map<@NonNull String, @NonNull EClass> keys = new LinkedHashMap<>();

	/** Use a special case importer: don't go via the registry. */
	private final IClassImporter extraImporter = new VesselAvailabilityCharterContractDefaultClassImporter();

	static {
		inputs.put(CharterContractConstants.CHARTER_CONTRACT_KEY, CharterContractConstants.VESSEL_AVAILAVILITY_CHARTER_CONTRACT_DEFAULT_NAME);
		inputs.put(CharterContractConstants.BALLAST_BONUS_KEY, CharterContractConstants.VESSEL_AVAILAVILITY_BALLAST_BONUS_DEFAULT_NAME);
		inputs.put(CharterContractConstants.REPOSITIONING_FEE_KEY, CharterContractConstants.VESSEL_AVAILAVILITY_REPOSITIONING_FEE_DEFAULT_NAME);
		
		keys.put(CharterContractConstants.CHARTER_CONTRACT_KEY, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT);
		keys.put(CharterContractConstants.BALLAST_BONUS_KEY, CommercialPackage.Literals.BALLAST_BONUS_TERM);
		keys.put(CharterContractConstants.REPOSITIONING_FEE_KEY, CommercialPackage.Literals.REPOSITIONING_FEE_TERM);
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			if (cargoModel != null) {
				for (Map.Entry<@NonNull String, @NonNull EClass> entry : keys.entrySet()) {
					final CSVReader reader = inputs.get(entry.getKey());
					if (reader != null) {
						extraImporter.importObjects(entry.getValue(), reader, context);
					}
				}
			}
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, IMMXExportContext context) {
		if (context.getRootObject() instanceof LNGScenarioModel lngScenarioModel) {
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			if (cargoModel != null) {
				List<IBallastBonus> bbExportContainers = new ArrayList<>();
				List<BallastBonusTerm> bbExports = new ArrayList<>();
				List<APortSet<Port>> mbbExports = new ArrayList<>();
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
								if (bb instanceof SimpleBallastBonusContainer container) {
									bbExports.addAll(container.getTerms());
								} else if (bb instanceof MonthlyBallastBonusContainer container) {
									bbExports.addAll(container.getTerms());
									mbbExports.addAll(container.getHubs());
								}
							}
							IRepositioningFee rf = gcc.getRepositioningFeeTerms();
							if (rf != null) {
								rfExportContainers.add(rf);
								if (rf instanceof SimpleRepositioningFeeContainer container) {
									rfExports.addAll(container.getTerms());
								}
							}
						}
					}
				}
				
				output.put(CharterContractConstants.CHARTER_CONTRACT_KEY, extraImporter.exportObjects(ccList, context));
				output.put(CharterContractConstants.BALLAST_BONUS_KEY, extraImporter.exportObjects(bbExports, context));
				output.put(CharterContractConstants.REPOSITIONING_FEE_KEY, extraImporter.exportObjects(rfExports, context));
			}
		}
	}
}

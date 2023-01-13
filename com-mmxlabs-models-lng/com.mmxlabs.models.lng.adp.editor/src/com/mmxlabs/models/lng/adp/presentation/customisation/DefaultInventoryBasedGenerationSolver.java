/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.customisation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.mull.IInventoryBasedGenerationSolver;
import com.mmxlabs.models.lng.adp.mull.MullUtil;
import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.DefaultInventoryBasedGenerationAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.DefaultStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.IMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryLocalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.profile.IMullProfile;
import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorData;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

@NonNullByDefault
public class DefaultInventoryBasedGenerationSolver implements IInventoryBasedGenerationSolver {

	@Override
	public Command runInventoryBasedGeneration(final ADPEditorData editorData, final com.mmxlabs.models.lng.adp.MullProfile eMullProfile) {
		checkMullProfile(eMullProfile);
		final LNGScenarioModel sm = ADPModelUtil.getNullCheckedScenarioModel(editorData);
		final ADPModel adpModel = ADPModelUtil.getNullCheckedAdpModel(editorData);
		
		final GlobalStatesContainer globalStates = MullUtil.buildDefaultGlobalStates(eMullProfile, adpModel, sm, e -> !e.isThirdParty());

		final IMullProfile mullProfile = MullUtil.createDefaultInternalMullProfile(eMullProfile);

		final IMullAlgorithm algorithm;
		
		{
			final IMullStrategyContainer strategyContainer = new DefaultStrategyContainer();
			final AlgorithmState algorithmState = MullUtil.createDefaultAlgorithmState(mullProfile, globalStates);
			final List<InventoryLocalState> inventoryLocalStates = MullUtil.createDefaultFromProfileInventoryLocalStates(mullProfile, globalStates, algorithmState, strategyContainer);
			algorithm = new DefaultInventoryBasedGenerationAlgorithm(globalStates, algorithmState, inventoryLocalStates);
		}
		algorithm.run();
		return MullUtil.createModelPopulationCommands(globalStates, algorithm, editorData);
	}

	private static void checkMullProfile(final com.mmxlabs.models.lng.adp.MullProfile eMullProfile) {
		final Set<Vessel> seenVessels = new HashSet<>();
		for (final com.mmxlabs.models.lng.adp.MullSubprofile eMullSubprofile : eMullProfile.getInventories()) {
			for (final MullEntityRow eMullEntityRow : eMullSubprofile.getEntityTable()) {
				if (!eMullEntityRow.getSalesContractAllocationRows().isEmpty()) {
					throw new IllegalStateException("Profile should not have sales contract allocation rows");
				}
				for (final DESSalesMarketAllocationRow eDesMarketRow : eMullEntityRow.getDesSalesMarketAllocationRows()) {
					if (eDesMarketRow.getVessels().size() != 1) {
						throw new IllegalStateException("Market rows should only have one vessel");
					}
					if (!seenVessels.add(eDesMarketRow.getVessels().get(0))) {
						throw new IllegalStateException("Vessels must not be shared");
					}
				}
			}
		}
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;

public class SuezCanalTariffHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPart activePart = HandlerUtil.getActivePart(event);

		final @Nullable IScenarioEditingLocation scenarioEditingLocation = (IScenarioEditingLocation) activePart.getAdapter(IScenarioEditingLocation.class);
		if (scenarioEditingLocation == null) {
			return null;
		}

		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}

		final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
		final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);
		SuezCanalTariff suezCanalTariff = costModel.getSuezCanalTariff();
		if (suezCanalTariff == null) {
			if (false) {
				// Sample data
				suezCanalTariff = PricingFactory.eINSTANCE.createSuezCanalTariff();

				suezCanalTariff.getBands().add(makeTariffBand(null, 5_000, 7.88, 6.7));
				suezCanalTariff.getBands().add(makeTariffBand(5_000, 10_000, 6.13, 5.21));
				suezCanalTariff.getBands().add(makeTariffBand(10_000, 20_000, 5.3, 4.51));
				suezCanalTariff.getBands().add(makeTariffBand(20_000, 40_000, 4.1, 3.49));
				suezCanalTariff.getBands().add(makeTariffBand(40_000, 70_000, 3.8, 3.23));
				suezCanalTariff.getBands().add(makeTariffBand(70_000, 120_000, 3.63, 3.09));
				suezCanalTariff.getBands().add(makeTariffBand(120_000, null, 3.53, 3.0));

				suezCanalTariff.getTugBands().add(makeTugBand(null, 90_000, 1));
				suezCanalTariff.getTugBands().add(makeTugBand(90_000, null, 2));

				suezCanalTariff.setTugCost(13778.78);
				suezCanalTariff.setMooringCost(2587.75);
				suezCanalTariff.setPilotageCost(395.5);
				suezCanalTariff.setDisbursements(13375.37);

				suezCanalTariff.setSdrToUSD("1.38");

				scenarioEditingLocation.getEditingDomain().getCommandStack()
						.execute(SetCommand.create(scenarioEditingLocation.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__SUEZ_CANAL_TARIFF, suezCanalTariff));
			} else {
				return null;
			}
		}
		if (scenarioEditingLocation.isLocked() == false) {
			DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, suezCanalTariff);
		}

		return null;
	}

	private SuezCanalTugBand makeTugBand(Integer start, Integer end, int tugs) {
		SuezCanalTugBand b = PricingFactory.eINSTANCE.createSuezCanalTugBand();
		if (start != null) {
			b.setBandStart(start);
		}
		if (end != null) {
			b.setBandEnd(end);
		}
		b.setTugs(tugs);
		return b;
	}

	private SuezCanalTariffBand makeTariffBand(Integer start, Integer end, double ladenValue, double ballastValue) {
		SuezCanalTariffBand b = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		if (start != null) {
			b.setBandStart(start);
		}
		if (end != null) {
			b.setBandEnd(end);
		}
		b.setLadenTariff(ladenValue);
		b.setBallastTariff(ballastValue);
		return b;
	}
}

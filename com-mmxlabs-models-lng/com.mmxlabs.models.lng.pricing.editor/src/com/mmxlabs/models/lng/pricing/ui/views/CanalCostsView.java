/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.pricing.ui.editorpart.CanalCostsPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CanalCostsView extends ScenarioTableViewerView<CanalCostsPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.CanalCostsView";

	@Override
	protected CanalCostsPane createViewerPane() {
		return new CanalCostsPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final CanalCostsPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getCostModel_RouteCosts() }), getAdapterFactory(), getModelReference());
			MMXRootObject rootObject = getRootObject();
			CostModel costModel = ScenarioModelUtil.getCostModel((LNGScenarioModel) rootObject);

			{
				SuezCanalTariff suezCanalTariff = costModel.getSuezCanalTariff();
				if (suezCanalTariff == null) {
					if (true) {
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
						suezCanalTariff.setFixedCosts(2587.75 + 395.5 + 13375.37);

						suezCanalTariff.setSdrToUSD("1.38");

						getEditingDomain().getCommandStack().execute(SetCommand.create(getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__SUEZ_CANAL_TARIFF, suezCanalTariff));

					}
				}
			}
			pane.setInput(costModel, costModel.getSuezCanalTariff(), costModel.getPanamaCanalTariff());
		}
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			final Object target = dcsd.getTarget();

			if (target instanceof RouteCost) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(target), true);
			}
		}
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

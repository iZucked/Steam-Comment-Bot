/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;


public class MergeScenarioWizard extends Wizard implements IExportWizard {

	private static final Logger log = LoggerFactory.getLogger(MergeScenarioWizard.class);

	private MergeScenarioWizardSourceSelectorPage sourceSelectorPage;
	private MergeScenarioWizardDataMapperPage purchaseContractMapperPage;
	private MergeScenarioWizardDataMapperPage salesContractMapperPage;
	private MergeScenarioWizardDataMapperPage vesselMapperPage;
	private MergeScenarioWizardDataMapperPage fobBuySpotMarketsMapperPage;
	private MergeScenarioWizardDataMapperPage fobSellSpotMarketsMapperPage;
	private MergeScenarioWizardDataMapperPage desBuySpotMarketsMapperPage;
	private MergeScenarioWizardDataMapperPage desSellSpotMarketsMapperPage;
	private MergeScenarioWizardDataMapperPage charterInMarketMapperPage;
	private MergeScenarioWizardDataMapperPage charterOutMarketMapperPage;
	private MergeScenarioWizardDataMapperPage vesselCharterMapperPage;
	private MergeScenarioWizardDataMapperPage loadSlotMapperPage;
	private MergeScenarioWizardDataMapperPage dischargeSlotMapperPage;
		
	private ScenarioInstance targetScenario;
	private ScenarioInstance sourceScenario;
	
	public MergeScenarioWizard(ScenarioInstance targetScenario) {
		this.targetScenario = targetScenario;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Scenario merge wizard");
		sourceSelectorPage = new MergeScenarioWizardSourceSelectorPage(selection);

		//Contracts.
		purchaseContractMapperPage = new MergeScenarioWizardDataMapperPage("Map purchase contracts of cargoes to target", 
				s ->  ScenarioModelUtil.getCommercialModel(s).getPurchaseContracts(), s ->  ScenarioModelUtil.getCommercialModel(s), 
				CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS);
		salesContractMapperPage = new MergeScenarioWizardDataMapperPage("Map sales contracts of cargoes to target", 
				s ->  ScenarioModelUtil.getCommercialModel(s).getSalesContracts(), s ->  ScenarioModelUtil.getCommercialModel(s), 
				CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS);

		//Vessels.
		vesselMapperPage = new MergeScenarioWizardDataMapperPage("Map vessels to target", 
				s -> ScenarioModelUtil.findReferenceModel(s).getFleetModel().getVessels(), s -> ScenarioModelUtil.getFleetModel(s), 
				FleetPackage.Literals.FLEET_MODEL__VESSELS); 
				
		//Vessel charter page.
		vesselCharterMapperPage = new MergeScenarioWizardVesselAvailabilityMapperPage("Map fleet charters to target");
		
		//Spot markets.
		fobBuySpotMarketsMapperPage = new MergeScenarioWizardDataMapperPage("Map FOB buy spot markets to target", 
				s -> ScenarioModelUtil.getSpotMarketsModel(s).getFobPurchasesSpotMarket().getMarkets(), s -> ScenarioModelUtil.getSpotMarketsModel(s).getFobPurchasesSpotMarket(), 
				SpotMarketsPackage.Literals.SPOT_MARKET_GROUP__MARKETS);
		fobSellSpotMarketsMapperPage = new MergeScenarioWizardDataMapperPage("Map FOB sell spot markets to target", 
				s -> ScenarioModelUtil.getSpotMarketsModel(s).getFobSalesSpotMarket().getMarkets(), s -> ScenarioModelUtil.getSpotMarketsModel(s).getFobSalesSpotMarket(), 
				SpotMarketsPackage.Literals.SPOT_MARKET_GROUP__MARKETS);
		desBuySpotMarketsMapperPage = new MergeScenarioWizardDataMapperPage("Map DES buy spot markets to target", 
				s -> ScenarioModelUtil.getSpotMarketsModel(s).getDesPurchaseSpotMarket().getMarkets(), s -> ScenarioModelUtil.getSpotMarketsModel(s).getDesPurchaseSpotMarket(), 
				SpotMarketsPackage.Literals.SPOT_MARKET_GROUP__MARKETS);
		desSellSpotMarketsMapperPage = new MergeScenarioWizardDataMapperPage("Map DES sell spot markets to target", 
				s -> ScenarioModelUtil.getSpotMarketsModel(s).getDesSalesSpotMarket().getMarkets(), s -> ScenarioModelUtil.getSpotMarketsModel(s).getDesSalesSpotMarket(), 
				SpotMarketsPackage.Literals.SPOT_MARKET_GROUP__MARKETS);
		
		//Charter markets.
		charterInMarketMapperPage = new MergeScenarioWizardDataMapperPage("Map charter in markets to target",  
				s -> ScenarioModelUtil.getSpotMarketsModel(s).getCharterInMarkets(), s -> ScenarioModelUtil.getSpotMarketsModel(s), 
				SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS);
		charterOutMarketMapperPage = new MergeScenarioWizardDataMapperPage("Map charter out markets to target", 
				s -> ScenarioModelUtil.getSpotMarketsModel(s).getCharterOutMarkets(), s -> ScenarioModelUtil.getSpotMarketsModel(s), 
				SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS);		
		
		//Load + discharge slots.
		loadSlotMapperPage = new MergeScenarioWizardDataMapperPage("Map load slots of cargoes to target",
				s ->  s.getCargoModel().getLoadSlots(), s -> ScenarioModelUtil.getCargoModel(s), CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS);
		dischargeSlotMapperPage = new MergeScenarioWizardDataMapperPage("Map discharge slots of cargoes to target",  
				s ->  s.getCargoModel().getDischargeSlots(), s -> ScenarioModelUtil.getCargoModel(s), CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS);
	}

	@Override
	public void addPages() {
		super.addPages();
		
		addPage(sourceSelectorPage);
				
		addPage(purchaseContractMapperPage);
		addPage(salesContractMapperPage);
		
		addPage(vesselMapperPage);
		addPage(this.vesselCharterMapperPage);
		
		addPage(fobBuySpotMarketsMapperPage);
		addPage(fobSellSpotMarketsMapperPage);
		addPage(desBuySpotMarketsMapperPage);
		addPage(desSellSpotMarketsMapperPage);

		addPage(charterInMarketMapperPage);
		addPage(charterOutMarketMapperPage);
		
		addPage(loadSlotMapperPage);
		addPage(dischargeSlotMapperPage);
	}
	
	@Override
	public boolean performFinish() {
		sourceScenario = sourceSelectorPage.getScenarioInstance();

		IWizardPage[] pages = this.getPages();

		final Exception exceptions[] = new Exception[1];

		BusyIndicator.showWhile(this.getContainer().getShell().getDisplay(), new Runnable() {
			@Override
			public void run() {
				try (MergeHelper mergeHelper = new MergeHelper(getSourceScenarioInstance(), getTargetScenarioInstance())) {
					CompoundCommand cmd = new CompoundCommand(String.format("Merge %s into %s",getSourceScenarioInstance().getName(), getTargetScenarioInstance().getName()));
					for (IWizardPage page : pages) {			
						if (page instanceof MergeScenarioWizardDataMapperPage) {
							MergeScenarioWizardDataMapperPage mapperPage = (MergeScenarioWizardDataMapperPage)page;
							mapperPage.merge(cmd, mergeHelper);
						}
					}

					RunnerHelper.syncExec(() -> {
						try {
							mergeHelper.execute(cmd);
						} catch (Exception e) {
							exceptions[0] = e;
						}
					});		
				} catch (final Exception e) {
					exceptions[0] = e;
				}
			}
		});

		if (exceptions[0] != null) {
			log.error("Unable to merge data into base case: " + exceptions[0]);
			MessageDialog.openError(this.getContainer().getShell(), "Error merging", "An error occurred merging into the selected base case.");
		}

		return true;
	}

	public ScenarioInstance getTargetScenarioInstance() {
		return targetScenario;
	}
	
	public ScenarioInstance getSourceScenarioInstance() {
		return sourceScenario;
	}
}
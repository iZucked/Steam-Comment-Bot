/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.pricing.importers.PanamaCanalTariffImporter.MarkupRate;
import com.mmxlabs.models.lng.pricing.importers.SuezCanalTariffImporter.ExtraData;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 */
public class CostModelImporter implements ISubmodelImporter {
	public static final String COOLDOWN_PRICING_KEY = "COOLDOWN_PRICING";
	public static final String PORT_COSTS_KEY = "PORT_COSTS";
	public static final String ROUTE_COSTS_KEY = "ROTUE_COSTS";
	public static final String PANAMA_CANAL_TARIFF_KEY = "PANAMA_CANAL_TARIFF";
	public static final String SUEZ_CANAL_TARIFF_KEY = "SUEZ_CANAL_TARIFF";

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter cooldownPriceImporter;
	private IClassImporter portCostImporter;
	private IClassImporter routeCostImporter;
	private final PanamaCanalTariffImporter panamaCanalTariffBandImporter = new PanamaCanalTariffImporter();
	private final SuezCanalTariffImporter suezCanalTariffBandImporter = new SuezCanalTariffImporter();

	/**
	 */
	public CostModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			cooldownPriceImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCooldownPrice());
			portCostImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getPortCost());
			routeCostImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getRouteCost());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {

		final Map<String, String> inputs = new HashMap<>();
		inputs.put(COOLDOWN_PRICING_KEY, "Cooldown Prices");
		inputs.put(PORT_COSTS_KEY, "Port Costs");
		inputs.put(ROUTE_COSTS_KEY, "Route Costs");
		inputs.put(PANAMA_CANAL_TARIFF_KEY, "Panama Canal Tariff");
		inputs.put(SUEZ_CANAL_TARIFF_KEY, "Suez Canal Tariff");

		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final CostModel costModel = PricingFactory.eINSTANCE.createCostModel();
		if (inputs.containsKey(PORT_COSTS_KEY)) {
			costModel.getPortCosts().addAll((Collection<? extends PortCost>) portCostImporter.importObjects(PricingPackage.eINSTANCE.getPortCost(), inputs.get(PORT_COSTS_KEY), context));
		}
		if (inputs.containsKey(ROUTE_COSTS_KEY)) {
			costModel.getRouteCosts().addAll((Collection<? extends RouteCost>) routeCostImporter.importObjects(PricingPackage.eINSTANCE.getRouteCost(), inputs.get(ROUTE_COSTS_KEY), context));
		}

		if (inputs.containsKey(COOLDOWN_PRICING_KEY)) {
			costModel.getCooldownCosts()
					.addAll((Collection<? extends CooldownPrice>) cooldownPriceImporter.importObjects(PricingPackage.eINSTANCE.getCooldownPrice(), inputs.get(COOLDOWN_PRICING_KEY), context));
		}

		if (inputs.containsKey(PANAMA_CANAL_TARIFF_KEY)) {
			final PanamaCanalTariff panamaCanalTariff = PricingFactory.eINSTANCE.createPanamaCanalTariff();
			costModel.setPanamaCanalTariff(panamaCanalTariff);
			for (final EObject o : panamaCanalTariffBandImporter.importObjects(PricingPackage.eINSTANCE.getPanamaCanalTariffBand(), inputs.get(PANAMA_CANAL_TARIFF_KEY), context)) {
				if (o instanceof PanamaCanalTariffBand) {
					final PanamaCanalTariffBand band = (PanamaCanalTariffBand) o;
					panamaCanalTariff.getBands().add(band);
				} else if (o instanceof MarkupRate) {
					final MarkupRate markupRate = (MarkupRate) o;
					panamaCanalTariff.setMarkupRate(markupRate.markupRate);
				}
			}
		}

		if (inputs.containsKey(SUEZ_CANAL_TARIFF_KEY)) {
			final SuezCanalTariff suezCanalTariff = PricingFactory.eINSTANCE.createSuezCanalTariff();
			costModel.setSuezCanalTariff(suezCanalTariff);
			for (final EObject o : suezCanalTariffBandImporter.importObjects(EcorePackage.eINSTANCE.getEObject(), inputs.get(SUEZ_CANAL_TARIFF_KEY), context)) {
				if (o instanceof SuezCanalTariffBand) {
					final SuezCanalTariffBand band = (SuezCanalTariffBand) o;
					suezCanalTariff.getBands().add(band);
				} else if (o instanceof SuezCanalTugBand) {
					final SuezCanalTugBand band = (SuezCanalTugBand) o;
					suezCanalTariff.getTugBands().add(band);
				} else if (o instanceof SuezCanalRouteRebate) {
					final SuezCanalRouteRebate rebate = (SuezCanalRouteRebate) o;
					suezCanalTariff.getRouteRebates().add(rebate);
				} else if (o instanceof ExtraData) {
					final ExtraData data = (ExtraData) o;
					suezCanalTariff.eSet(data.feature, data.value);
				}
			}
		}

		context.doLater(new IDeferment() {
			@Override
			public void run(final IImportContext importContext) {
				final IMMXImportContext context = (IMMXImportContext) importContext;
				final MMXRootObject root = context.getRootObject();
				if (root instanceof LNGScenarioModel) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) root;

					final PricingModel pricing = ScenarioModelUtil.getPricingModel(scenarioModel);
					final FleetModel fleet = ScenarioModelUtil.getFleetModel(scenarioModel);
					if (pricing != null && fleet != null) {
						for (final BaseFuel baseFuel : fleet.getBaseFuels()) {
							boolean found = false;
							for (final BaseFuelCost cost : costModel.getBaseFuelCosts()) {
								found = cost.getFuel() == baseFuel;
								if (found)
									break;
							}
							if (!found) {
								final BaseFuelCost cost = PricingFactory.eINSTANCE.createBaseFuelCost();
								cost.setFuel(baseFuel);
								costModel.getBaseFuelCosts().add(cost);
							}
						}
					}
				}
			}

			@Override
			public int getStage() {
				return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
			}
		});

		return costModel;
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final CostModel costModel = (CostModel) model;
		output.put(COOLDOWN_PRICING_KEY, cooldownPriceImporter.exportObjects(costModel.getCooldownCosts(), context));
		output.put(PORT_COSTS_KEY, portCostImporter.exportObjects(costModel.getPortCosts(), context));
		output.put(ROUTE_COSTS_KEY, routeCostImporter.exportObjects(costModel.getRouteCosts(), context));

		final PanamaCanalTariff panamaCanalTariff = costModel.getPanamaCanalTariff();
		if (panamaCanalTariff != null) {
			output.put(PANAMA_CANAL_TARIFF_KEY, panamaCanalTariffBandImporter.exportTariff(panamaCanalTariff, context));
		}

		final SuezCanalTariff suezCanalTariff = costModel.getSuezCanalTariff();
		if (suezCanalTariff != null) {
			output.put(SUEZ_CANAL_TARIFF_KEY, suezCanalTariffBandImporter.exportTariff(suezCanalTariff, context));
		}
	}

	@Override
	public EClass getEClass() {
		return PricingPackage.eINSTANCE.getCostModel();
	}
}

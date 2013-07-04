/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * @since 2.0
 */
public class PricingModelImporter implements ISubmodelImporter {
	private static final HashMap<String, String> inputs = new HashMap<String, String>();
	public static final String PRICE_CURVE_KEY = "PRICE_CURVES";
	public static final String CHARTER_CURVE_KEY = "CHARTER_CURVES";
	public static final String COOLDOWN_PRICING_KEY = "COOLDOWN_PRICING";
	public static final String BASEFUEL_PRICING_KEY = "BF_PRICING";
	public static final String PORT_COSTS_KEY = "PORT_COSTS";

	static {
		inputs.put(PRICE_CURVE_KEY, "Commodity Curves");
		inputs.put(CHARTER_CURVE_KEY, "Charter Curves");
		inputs.put(COOLDOWN_PRICING_KEY, "Cooldown Prices");
		inputs.put(BASEFUEL_PRICING_KEY, "Base Fuel Curves");
		inputs.put(PORT_COSTS_KEY, "Port Costs");
	}

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter commodityIndexImporter;
	private IClassImporter charterIndexImporter;

	private IClassImporter cooldownPriceImporter;
	private IClassImporter baseFuelIndexImporter;

	private IClassImporter portCostImporter;

	/**
	 * @since 2.0
	 */
	public PricingModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			commodityIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCommodityIndex());
			charterIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCharterIndex());
			cooldownPriceImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCooldownPrice());
			baseFuelIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getBaseFuelIndex());
			portCostImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getPortCost());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final PricingModel pricing = PricingFactory.eINSTANCE.createPricingModel();
		if (inputs.containsKey(PRICE_CURVE_KEY)) {
			importCommodityCurves(pricing, inputs.get(PRICE_CURVE_KEY), context);
		}
		if (inputs.containsKey(CHARTER_CURVE_KEY)) {
			importCharterCurves(pricing, inputs.get(CHARTER_CURVE_KEY), context);
		}
		if (inputs.containsKey(BASEFUEL_PRICING_KEY)) {
			importBaseFuelCurves(pricing, inputs.get(BASEFUEL_PRICING_KEY), context);
		}
		if (inputs.containsKey(PORT_COSTS_KEY)) {
			pricing.getPortCosts().addAll((Collection<? extends PortCost>) portCostImporter.importObjects(PricingPackage.eINSTANCE.getPortCost(), inputs.get(PORT_COSTS_KEY), context));
		}

		final FleetCostModel fcm = PricingFactory.eINSTANCE.createFleetCostModel();
		pricing.setFleetCost(fcm);

		if (inputs.containsKey(COOLDOWN_PRICING_KEY)) {
			pricing.getCooldownPrices().addAll(
					(Collection<? extends CooldownPrice>) cooldownPriceImporter.importObjects(PricingPackage.eINSTANCE.getCooldownPrice(), inputs.get(COOLDOWN_PRICING_KEY), context));
		}
		
		context.doLater(new IDeferment() {
			@Override
			public void run(final IImportContext context) {
				final MMXRootObject root = context.getRootObject();
				if (root instanceof LNGScenarioModel) {
					LNGScenarioModel scenarioModel = (LNGScenarioModel) root;
					final PricingModel pricing = scenarioModel.getPricingModel();
					final FleetModel fleet = scenarioModel.getFleetModel();
					final PortModel port = scenarioModel.getPortModel();
					if (pricing != null && fleet != null && port != null) {
						for (final Route route : port.getRoutes()) {
							if (route.isCanal()) {
								for (final VesselClass vesselClass : fleet.getVesselClasses()) {
									boolean found = false;
									for (final RouteCost cost : pricing.getRouteCosts()) {
										if (cost.getVesselClass() == vesselClass && cost.getRoute() == route) {
											found = true;
											break;
										}
									}
									if (!found) {
										context.addProblem(context.createProblem("There was no route cost for " + route.getName() + " with " + vesselClass.getName(), false, false, false));
										final RouteCost cost = PricingFactory.eINSTANCE.createRouteCost();
										cost.setRoute(route);
										cost.setVesselClass(vesselClass);
										pricing.getRouteCosts().add(cost);
									}
								}
							}
						}
					}
					if (pricing != null && fleet != null) {
						for (final BaseFuel baseFuel : fleet.getBaseFuels()) {
							boolean found = false;
							for (final BaseFuelCost cost : pricing.getFleetCost().getBaseFuelPrices()) {
								found = cost.getFuel() == baseFuel;
								if (found)
									break;
							}
							if (!found) {
								final BaseFuelCost cost = PricingFactory.eINSTANCE.createBaseFuelCost();
								cost.setFuel(baseFuel);
								pricing.getFleetCost().getBaseFuelPrices().add(cost);
							}
						}
					}
				}
			}

			@Override
			public int getStage() {
				return IImportContext.STAGE_MODIFY_SUBMODELS;
			}
		});

		return pricing;
	}
	
	private void importCommodityCurves(final PricingModel pricing, final CSVReader csvReader, final IImportContext context) {
		pricing.getCommodityIndices().addAll((Collection<? extends CommodityIndex>) commodityIndexImporter.importObjects(PricingPackage.eINSTANCE.getCommodityIndex(), csvReader, context));
	}

	private void importCharterCurves(final PricingModel pricing, final CSVReader csvReader, final IImportContext context) {
		pricing.getCharterIndices().addAll((Collection<? extends CharterIndex>) charterIndexImporter.importObjects(PricingPackage.eINSTANCE.getCharterIndex(), csvReader, context));
	}

	private void importBaseFuelCurves(final PricingModel pricing, final CSVReader csvReader, final IImportContext context) {
		pricing.getBaseFuelPrices().addAll((Collection<? extends BaseFuelIndex>) baseFuelIndexImporter.importObjects(PricingPackage.eINSTANCE.getBaseFuelIndex(), csvReader, context));
	}

	@Override
	public void exportModel(final MMXRootObject root, final UUIDObject model, final Map<String, Collection<Map<String, String>>> output) {
		final PricingModel pricing = (PricingModel) model;
		output.put(PRICE_CURVE_KEY, commodityIndexImporter.exportObjects(pricing.getCommodityIndices(), root));
		output.put(CHARTER_CURVE_KEY, charterIndexImporter.exportObjects(pricing.getCharterIndices(), root));
		output.put(BASEFUEL_PRICING_KEY, baseFuelIndexImporter.exportObjects(pricing.getBaseFuelPrices(), root));
		output.put(COOLDOWN_PRICING_KEY, cooldownPriceImporter.exportObjects(pricing.getCooldownPrices(), root));
		output.put(PORT_COSTS_KEY, portCostImporter.exportObjects(pricing.getPortCosts(), root));
	}

	@Override
	public EClass getEClass() {
		return PricingPackage.eINSTANCE.getPricingModel();
	}
}

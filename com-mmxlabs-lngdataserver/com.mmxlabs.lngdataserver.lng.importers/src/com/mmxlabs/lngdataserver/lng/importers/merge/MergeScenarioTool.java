/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.util.PricingModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelFinder;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.json.EMFDeserializationContext;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.rcp.common.json.JSONReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class MergeScenarioTool {

	private static final Logger logger = LoggerFactory.getLogger(MergeScenarioTool.class);

	private List<MergeMapping> purchaseContractMappings;
	private List<MergeMapping> salesContractMappings;
	
	
	
	public void doIt(ScenarioInstance source, ScenarioInstance target) {

		final ScenarioModelRecord sourceModelRecord = SSDataManager.Instance.getModelRecord(source);
		try (IScenarioDataProvider scenarioDataProvider = sourceModelRecord.aquireScenarioDataProvider("MergeScenarioTool.doIt")) {
			final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			LNGScenarioModel sm = (LNGScenarioModel) EcoreUtil.copy(scenarioModel);

			if (sm != null) {
				CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
				CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(sm);
/*
				//Merge purchase contracts		
				mergePurchaseContracts(sm, cargoModel, commercialModel, "Pluto 2021", "Pluto");
				mergePurchaseContracts(sm, cargoModel, commercialModel, "NWS 2021", "NWS");
				mergePurchaseContracts(sm, cargoModel, commercialModel, "Wheatstone 2021", "Wheatstone");

				//Merge sales contracts
				renameSalesContract(sm, cargoModel, commercialModel, "JERA", "Jera");
				renameSalesContract(sm, cargoModel, commercialModel, "RWE DES", "RWE");
				renameSalesContract(sm, cargoModel, commercialModel, "Pluto TMA (KEPA & TGP)", "Pluto TMA KEPA TGP");
				mergeSalesContracts(sm, cargoModel, commercialModel, "Pluto TMA KEPA", "Pluto TMA KEPA TGP");
				mergeSalesContracts(sm, cargoModel, commercialModel, "Pluto TMA TGP", "Pluto TMA KEPA TGP");		
				// TODO: Rename RWF_FOB_CC  to  RWE CC FOB ???
				//mergeSalesContracts(sm, cargoModel, commercialModel, "RWE CC FOB", "RWF_FOB CC");	
				mergeSalesContracts(sm, cargoModel, commercialModel, "Pluto TG DES", "TG");
				mergeSalesContracts(sm, cargoModel, commercialModel, "Pluto TG FOB", "TG", true);
				mergeSalesContracts(sm, cargoModel, commercialModel, "Pluto KE FOB", "KE", true);	
				renameSalesContract(sm, cargoModel, commercialModel, "Uncommitted Asia", "TBA_Asia");		
*/
				for (MergeMapping cm : this.purchaseContractMappings) {
					switch (cm.getTargetName()) {
					case "Add":
						break;
					case "Ignore":
						break;
					default:
						mergePurchaseContracts(sm, cargoModel, commercialModel, cm.getSourceName(), cm.getTargetName());
						break;
					}
				}
				
				for (MergeMapping cm : this.salesContractMappings) {
					switch (cm.getTargetName()) {
					case "Add":
						break;
					case "Ignore":
						break;
					default:
						mergeSalesContracts(sm, cargoModel, commercialModel, cm.getSourceName(), cm.getTargetName());
						break;
					}
				}
				
				ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(target);
				modelRecord.executeWithProvider(sdp -> {
					try {
						createCargoUpdater(sm).accept(sdp);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});

				logger.info("Finished ADP merge.");
			}
		}
	}

	private void renameSalesContract(LNGScenarioModel sm, CargoModel cargoModel, CommercialModel commercialModel, String oldName, String newName) {
		SalesContract salesContract = this.findSalesContract(commercialModel, oldName);
		salesContract.setName(newName);
	}

	PurchaseContract findPurchaseContract(CommercialModel commercialModel, String purchaseContractName) {
		PurchaseContract pc = commercialModel.getPurchaseContracts().stream().filter(c -> c.getName().equals(purchaseContractName)).findFirst().get();
		return pc;	
	}

	SalesContract findSalesContract(CommercialModel commercialModel, String salesContractName) {
		SalesContract sc = commercialModel.getSalesContracts().stream().filter(c -> c.getName().equals(salesContractName)).findFirst().get();
		return sc;	
	}
	
	List<SalesContract> findSalesContracts(CommercialModel commercialModel, String[] salesContracts) {
		List<SalesContract> contracts = new ArrayList<>();
		for (String salesContract : salesContracts) {
			SalesContract sc = findSalesContract(commercialModel, salesContract);
			if (sc != null) {
				contracts.add(sc);
			}
			else {
				logger.warn("Could not find sales contract: "+salesContract);
			}
		}
		return contracts;
	}

	List<PurchaseContract> findPurchaseContracts(CommercialModel commercialModel, String[] purchaseContracts) {
		List<PurchaseContract> contracts = new ArrayList<>();
		for (String purchaseContract : purchaseContracts) {
			PurchaseContract pc = findPurchaseContract(commercialModel, purchaseContract);
			if (pc != null) {
				contracts.add(pc);
			}
			else {
				logger.warn("Could not find purchase contract: "+purchaseContract);
			}
		}
		return contracts;
	}
	
	private void mergePurchaseContracts(LNGScenarioModel sm, CargoModel cargoModel, CommercialModel commercialModel, String oldContractName, String newContractName) {
		
		PurchaseContract newPurchaseContract = findPurchaseContract(commercialModel, newContractName);
		PurchaseContract oldPC = findPurchaseContract(commercialModel, oldContractName);

		for (LoadSlot load : cargoModel.getLoadSlots()) {
			if (isLoadWithOldContract(load, oldContractName)) {
				load.setContract(newPurchaseContract);
			}
		}

		List<EObject> oldContracts = new LinkedList<>();
		oldContracts.add(oldPC);
	
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(oldContracts, sm);
		for (EObject oldContract : oldContracts) {
			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(oldContract);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (setting.getEStructuralFeature() != CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS) {
						if (setting.getEStructuralFeature().isMany()) {
							Collection<?> collection = (Collection<?>) setting.getEObject().eGet(setting.getEStructuralFeature());
							if (collection.contains(newPurchaseContract)) {
								// Replacement is already in the collection, so just remove it
								collection.remove(oldContract);
							} else {
								EcoreUtil.replace(setting, oldContract, newPurchaseContract);
							}
						} else {
							EcoreUtil.replace(setting, oldContract, newPurchaseContract);
						}
					}
				}
			}
		}
		
		commercialModel.getPurchaseContracts().remove(oldPC);
	}

	private void mergeSalesContracts(LNGScenarioModel sm, CargoModel cargoModel, CommercialModel commercialModel, String oldContractName, String newContractName) {
		mergeSalesContracts(sm, cargoModel, commercialModel, oldContractName, newContractName, false);
	}
	
	private void mergeSalesContracts(LNGScenarioModel sm, CargoModel cargoModel, CommercialModel commercialModel, String oldContractName, String newContractName, 
			boolean takeFOBDivertToDestContractDefaults) {
		SalesContract targetSalesContract = findSalesContract(commercialModel, newContractName);
	
		SalesContract oldSC = findSalesContract(commercialModel, oldContractName);
		
		//Update the contract in the slot.
		for (DischargeSlot sale : cargoModel.getDischargeSlots()) {
			if (isDischargeWithOldContract(sale, oldContractName)) {
				if (takeFOBDivertToDestContractDefaults) {
					if (!sale.isSetFobSaleDealType()) {
						sale.setFobSaleDealType(oldSC.getFobSaleDealType());
					}
					if (!sale.isSetShippingDaysRestriction()) {
						sale.setShippingDaysRestriction(oldSC.getShippingDaysRestriction());
					}
				}
	
				sale.setContract(targetSalesContract);
			}
		}

		List<EObject> oldContracts = new LinkedList<>();
		oldContracts.add(oldSC);

		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(oldContracts, sm);
		for (EObject oldContract : oldContracts) {
			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(oldContract);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (setting.getEStructuralFeature() != CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS) {
						if (setting.getEStructuralFeature().isMany()) {
							Collection<?> collection = (Collection<?>) setting.getEObject().eGet(setting.getEStructuralFeature());
							if (collection.contains(targetSalesContract)) {
								// Replacement is already in the collection, so just remove it
								collection.remove(oldContract);
							} else {
								EcoreUtil.replace(setting, oldContract, targetSalesContract);
							}
						} else {
							EcoreUtil.replace(setting, oldContract, targetSalesContract);
						}
					}
				}
			}
		}
		
		commercialModel.getSalesContracts().remove(oldSC);
	}

	
	private boolean isDischargeWithOldContract(DischargeSlot ds, String oldContractName) {
		SalesContract contract = ds.getContract();
		return (contract != null && contract.getName().equals(oldContractName));
	}	
	
	private boolean isLoadWithOldContract(LoadSlot load, String oldContractName) {
		PurchaseContract contract = load.getContract();
		return (contract != null && contract.getName().equals(oldContractName));
	}

	public Consumer<IScenarioDataProvider> createCargoUpdater(final LNGScenarioModel sourceLSM) throws Exception {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sourceLSM);
		if (cargoModel == null) {
			return (a) -> {
			};
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new EMFJacksonModule());
		final String loadsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getLoadSlots());
		final String dischargesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getDischargeSlots());
		final String cargoesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getCargoes());

		FleetModelFinder fleetFinder = new FleetModelFinder(ScenarioModelUtil.getFleetModel(sourceLSM));
		//final String wgVesselJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fleetFinder.findVessel("WG_Charter"));
		//final String maranGasVesselJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fleetFinder.findVessel("Maran Gas"));
		final String energyAdvanceVesselJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fleetFinder.findVessel("Energy Advance"));
	
		SpotMarketsModelFinder marketsFinder = new SpotMarketsModelFinder(ScenarioModelUtil.getSpotMarketsModel(sourceLSM));
		// final String charterMarketVesselJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(marketsFinder.findCharterInMarket("charter"));

		CargoModelFinder cargoFinder = new CargoModelFinder(ScenarioModelUtil.getCargoModel(sourceLSM));
		//final String mgVesselCharterJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoFinder.findVesselAvailability("Maran Gas"));

		//final String commodityCurves = getExtraCurves(ssdp, mapper);
		PricingModelFinder pricingFinder = new PricingModelFinder(ScenarioModelUtil.getPricingModel(sourceLSM));
		List<CommodityCurve> extraCurves = new LinkedList<>();
		// extraCurves.add(pricingFinder.findCommodityCurve("CC_purchase_price"));
		// extraCurves.add(pricingFinder.findCommodityCurve("Uniper_Put_Option"));
		// extraCurves.add(pricingFinder.findCommodityCurve("RWE_base_price"));
		// extraCurves.add(pricingFinder.findCommodityCurve("TG_KE_DES_price"));

		final String commodityCurves = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extraCurves);

		// System.out.println(loadsJSON);
		// System.out.println(dischargesJSON);
		// System.out.println(cargoesJSON);
		return (target) -> {

			final List<Pair<JSONReference, String>> missingReferences = new LinkedList<>();

			final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);
			ctx.setMissingFeatureHandler((ref, lbl) -> {
				System.out.println("Unknown reference " + ref.getName() + " " + ref.getGlobalId() + " " + ref.getClassType());

				missingReferences.add(Pair.of(ref, lbl));
				return false;
			});

			// Ignore back reference
			ctx.ignoreFeature(CargoPackage.Literals.SLOT__CARGO);

			final CargoModel cam = ScenarioModelUtil.getCargoModel(target);
			cam.getVesselAvailabilities().forEach(ctx::registerType);

			final PortModel pm = ScenarioModelUtil.getPortModel(target);
			pm.getPorts().forEach(ctx::registerType);
			pm.getPortGroups().forEach(ctx::registerType);
			pm.getPortCountryGroups().forEach(ctx::registerType);
			pm.getSpecialPortGroups().forEach(ctx::registerType);

			final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
			fm.getBaseFuels().forEach(ctx::registerType);
			fm.getVessels().forEach(ctx::registerType);
			fm.getVesselGroups().forEach(ctx::registerType);

			final CommercialModel targetCM = ScenarioModelUtil.getCommercialModel(target);
			targetCM.getPurchaseContracts().forEach(ctx::registerType);
			targetCM.getSalesContracts().forEach(ctx::registerType);
			targetCM.getEntities().forEach(ctx::registerType);

			List<SalesContract> extraSales = new LinkedList<>();
			Map<String, MergeAction> salesContractActions = getActions(this.salesContractMappings);
			Map<String, MergeAction> purchaseContractActions = getActions(this.purchaseContractMappings);
			sourceLSM.getReferenceModel().getCommercialModel().getSalesContracts().forEach(c -> {
				MergeAction ma = salesContractActions.get(c.getName());
				switch (ma.getMergeType()) {
				case Add:
					extraSales.add(c);
					break;
				case Overwrite:
					extraSales.add(c);					
					break;
				}
			});
			List<PurchaseContract> extraPurchases = new LinkedList<>();
			sourceLSM.getReferenceModel().getCommercialModel().getPurchaseContracts().forEach(c -> {
				MergeAction ma = purchaseContractActions.get(c.getName());
				switch (ma.getMergeType()) {
				case Add:
					extraPurchases.add(c);
					break;
				case Overwrite:
					extraPurchases.add(c);					
					break;
				}
			});

			final SpotMarketsModel smm = ScenarioModelUtil.getSpotMarketsModel(target);
			smm.getCharterInMarkets().forEach(ctx::registerType);
			smm.getDesPurchaseSpotMarket().getMarkets().forEach(ctx::registerType);
			smm.getDesSalesSpotMarket().getMarkets().forEach(ctx::registerType);
			smm.getFobPurchasesSpotMarket().getMarkets().forEach(ctx::registerType);
			smm.getFobSalesSpotMarket().getMarkets().forEach(ctx::registerType);

			// ANYTHING ELSE?

			final ObjectMapper mapper2 = new ObjectMapper(null, null, ctx);
			mapper2.registerModule(new EMFJacksonModule());
			try {
				final List<CommodityCurve> newCurves = mapper2.readValue(commodityCurves, new TypeReference<List<CommodityCurve>>() {
				});
				//Vessel wgVessel = mapper2.readValue(wgVesselJSON, Vessel.class);
				//Vessel mgVessel = mapper2.readValue(maranGasVesselJSON, Vessel.class);
				// Vessel mgVessel = mapper2.readValue(maranGasVesselJSON, Vessel.class);
				Vessel energyAdvanceVessel = mapper2.readValue(energyAdvanceVesselJSON, Vessel.class);

				
				//VesselAvailability mgCharter = mapper2.readValue(mgVesselCharterJSON, VesselAvailability.class);
				// Run now to do partial link up and the VesselAvailability will not be
				// registered correctly.
				ctx.runDeferredActions();
				//ctx.registerType(mgCharter);

				// CharterInMarket wgSpotCharter = mapper2.readValue(charterMarketVesselJSON, CharterInMarket.class);

				final String extraPurchasesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extraPurchases);
				final String extraSalesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extraSales);
				final List<PurchaseContract> newPurchaseContracts = mapper2.readValue(extraPurchasesJSON, new TypeReference<List<PurchaseContract>>() {
				});
				final List<SalesContract> newSalesContracts = mapper2.readValue(extraSalesJSON, new TypeReference<List<SalesContract>>() {
				});

				final List<LoadSlot> newLoads = mapper2.readValue(loadsJSON, new TypeReference<List<LoadSlot>>() {
				});
				final List<DischargeSlot> newDischarges = mapper2.readValue(dischargesJSON, new TypeReference<List<DischargeSlot>>() {
				});
				final List<Cargo> newCargoes = mapper2.readValue(cargoesJSON, new TypeReference<List<Cargo>>() {
				});

				ctx.runDeferredActions();
				final EditingDomain editingDomain = target.getEditingDomain();

				CompoundCommand cmd = new CompoundCommand("Import ADP");
				
				//Copy vessel end dates.
				var sourceVas = ScenarioModelUtil.getCargoModel(sourceLSM).getVesselAvailabilities();
				var targetVas = ScenarioModelUtil.getCargoModel(target).getVesselAvailabilities();
				
				for (VesselAvailability v : sourceVas) {
					//	targetVas.stream().f
					Optional<VesselAvailability> tva = targetVas.stream().filter(va -> va.getVessel().getName().equals(v.getVessel().getName())).findFirst();
					
					if (tva.isPresent()) {
						//TODO check endby is latest one.
						if (tva.get().isSetEndBy()) {
							if (v.isSetEndBy()) {
								cmd.append(SetCommand.create(editingDomain, tva.get(), CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY, v.getEndBy()));
							}
							if (v.isSetEndAfter()) {
								cmd.append(SetCommand.create(editingDomain, tva.get(), CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER, v.getEndAfter()));
							}
						}						
					}
				}

				// Add missing price curves
				//cmd.append(AddCommand.create(editingDomain, ScenarioModelUtil.getPricingModel(target), PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES, newCurves));
				if (!newCurves.isEmpty()) {
					cmd.append(AddCommand.create(editingDomain, ScenarioModelUtil.getPricingModel(target), PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES, newCurves));
				}
				// Add new vessels
				cmd.append(AddCommand.create(editingDomain, ScenarioModelUtil.getFleetModel(target), FleetPackage.Literals.FLEET_MODEL__VESSELS, energyAdvanceVessel));
				// cmd.append(AddCommand.create(editingDomain, ScenarioModelUtil.getFleetModel(target), FleetPackage.Literals.FLEET_MODEL__VESSELS, mgVessel));
				// Add in the charter market
				// cmd.append(AddCommand.create(editingDomain, ScenarioModelUtil.getSpotMarketsModel(target), SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS, wgSpotCharter));
				// // Add in new vessel charter
				// cmd.append(AddCommand.create(editingDomain, ScenarioModelUtil.getCargoModel(target), CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES, mgCharter));

				// Add in the new contracts
				if (!newPurchaseContracts.isEmpty()) {
					cmd.append(AddCommand.create(editingDomain, targetCM, CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS, newPurchaseContracts));
				}
				if (!newSalesContracts.isEmpty()) {
					cmd.append(AddCommand.create(editingDomain, targetCM, CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS, newSalesContracts));
				}

				// Finally import the new positions.
				if (!newLoads.isEmpty()) {
					cmd.append(AddCommand.create(editingDomain, cam, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, newLoads));
				}
				if (!newDischarges.isEmpty()) {
					cmd.append(AddCommand.create(editingDomain, cam, CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS, newDischarges));
				}
				if (!newCargoes.isEmpty()) {
					cmd.append(AddCommand.create(editingDomain, cam, CargoPackage.Literals.CARGO_MODEL__CARGOES, newCargoes));
				}

				editingDomain.getCommandStack().execute(cmd);

				if (!missingReferences.isEmpty()) {
					// Revert change if there are missing references

					if (System.getProperty("lingo.suppress.dialogs") == null) {
						final StringBuilder msg = new StringBuilder("Keep changes?\n\n");

						final Set<String> messages = new HashSet<>();
						for (final Pair<JSONReference, String> p : missingReferences) {
							final JSONReference ref = p.getFirst();
							String lbl = p.getSecond();
							if (lbl == null) {
								final int idx = ref.getClassType().lastIndexOf('/');
								lbl = (idx > 0) ? ref.getClassType().substring(idx + 1) : ref.getClassType();
							}
							messages.add(String.format("Missing %s: %s", lbl, ref.getName()));
						}
						messages.forEach(m -> msg.append(m).append("\n"));

						Boolean keep = RunnerHelper.syncExecFunc((d) -> MessageDialog.openQuestion(d.getActiveShell(), "Error - unable to find all data references", msg.toString()));
						if (Boolean.FALSE == keep) {
							RunnerHelper.asyncExec(() -> editingDomain.getCommandStack().undo());
						}
					} else {
						// ITS failure exit point
						throw new IllegalStateException();
					}
				}
			} catch (final Exception e1) {
				e1.printStackTrace();
			}
		};

	}

	private Map<String, MergeAction> getActions(List<MergeMapping> mappings) {
		Map<String, MergeAction> sourceToAction = new HashMap<>();
		for (MergeMapping cm : mappings) {
			MergeType mt = MergeType.Map;
			try {
				MergeType.valueOf(cm.getTargetName());
			}
			catch (IllegalArgumentException e) {
				//Ignore, as we just map it.
			}
			sourceToAction.put(cm.getSourceName(), new MergeAction(mt, cm.getSourceName(), cm.getTargetName()));
		}
		return sourceToAction;
	}

	public void setPurchaseContractMappings(List<MergeMapping> purchaseContractMappings) {
		this.purchaseContractMappings = purchaseContractMappings;
	}

	public void setSalesContractMappings(List<MergeMapping> salesContractMappings) {
		this.salesContractMappings = salesContractMappings;
	}
}

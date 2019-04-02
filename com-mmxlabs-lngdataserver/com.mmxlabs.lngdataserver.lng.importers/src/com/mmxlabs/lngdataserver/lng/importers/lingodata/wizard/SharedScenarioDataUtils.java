/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.lngdataserver.integration.models.financial.settled.SettledPricesVersion;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsVersion;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.ImportFromBaseSelectionPage.DataOptionGroup;
import com.mmxlabs.lngdataserver.lng.io.bunkerfuels.BunkerFuelsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.bunkerfuels.BunkerFuelsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.financial.SettledPricesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.financial.SettledPricesToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.portgroups.PortGroupsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.portgroups.PortGroupsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.pricing.PricingToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vesselgroups.VesselGroupsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vesselgroups.VesselGroupsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.json.EMFDeserializationContext;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.MigrationForbiddenException;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;

public final class SharedScenarioDataUtils {
	private static final Logger log = LoggerFactory.getLogger(SharedScenarioDataUtils.class);

	private SharedScenarioDataUtils() {

	}

	// Note: Order is both display and *required* execution order
	public enum DataOptions {
		PortData("Ports"), //
		FleetDatabase("Vessels"), //
		CommercialData("Contracts && Entities"), //
		SpotCargoMarketsData("Cargo Markets"), //
		SpotCharterMarketsData("Charter Markets"), //
		PricingData("Price Curves"), //
		// SettledPriceData("Settled Price Curves"), //
		ADPData("ADP"), //
		CargoData("Cargoes") //
		;

		private DataOptions(final String name) {
			this.name = name;
		}

		private final String name;

		public String getName() {
			return name;
		}
	}

	public static class UpdateJob implements IRunnableWithProgress {

		private final @NonNull ScenarioModelRecord sourceModelRecord;
		private final List<ScenarioModelRecord> destinationScenarioRecords;
		private final Set<DataOptions> dataOptions;
		private boolean copySourceIfNeeded;

		public UpdateJob(final Set<DataOptions> enumSet, final @NonNull ScenarioModelRecord sourceScenario, final List<ScenarioModelRecord> destinationScenarios, boolean copyIfNeeded) {
			this.dataOptions = enumSet;
			this.sourceModelRecord = sourceScenario;
			this.destinationScenarioRecords = destinationScenarios;
			this.copySourceIfNeeded = copyIfNeeded;
		}

		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			final Map<DataOptions, BiConsumer<CompoundCommand, IScenarioDataProvider>> dataToUpdater = new EnumMap<>(DataOptions.class);
			try {
				try (final IScenarioDataProvider sdp = sourceModelRecord.aquireScenarioDataProvider("SharedScenarioDataImportWizard:1")) {

					final PortModel portModel = ScenarioModelUtil.getPortModel(sdp);
					final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(sdp);
					final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(sdp);

					for (final DataOptions option : dataOptions) {
						switch (option) {
						case PortData:
							dataToUpdater.put(DataOptions.PortData, createPortAndDistanceUpdater(portModel));
							break;
						case PricingData:
							dataToUpdater.put(DataOptions.PricingData, createPricingUpdater(pricingModel));
							break;
						case FleetDatabase:
							dataToUpdater.put(DataOptions.FleetDatabase, createFleetUpdater(fleetModel));
							break;
						case CommercialData:
							dataToUpdater.put(DataOptions.CommercialData, createCommercialsUpdater(sdp));
							break;
						case SpotCargoMarketsData:
							dataToUpdater.put(DataOptions.SpotCargoMarketsData, createSpotCargoMarketsUpdater(sdp));
							break;
						case SpotCharterMarketsData:
							dataToUpdater.put(DataOptions.SpotCharterMarketsData, createSpotCharterMarketsUpdater(sdp));
							break;
						case ADPData:
							dataToUpdater.put(DataOptions.ADPData, createADPUpdater(sdp));
							break;
						case CargoData:
							dataToUpdater.put(DataOptions.CargoData, createCargoUpdater(sdp));
							break;
						default:
							break;

						}
					}

					String lbl = destinationScenarioRecords.size() == 1 ? "Update scenario" : "Update scenarios";
					monitor.beginTask(lbl, destinationScenarioRecords.size());
					try {
						for (final ScenarioModelRecord destModelRecord : destinationScenarioRecords) {

							if (monitor.isCanceled()) {
								return;
							}

							monitor.subTask("Update " + destModelRecord.getName());
							// Do not import into self
							if (destModelRecord == sourceModelRecord) {
								monitor.worked(1);
								continue;
							}
							final boolean autoSave = !destModelRecord.isLoaded();
							try (final IScenarioDataProvider target = destModelRecord.aquireScenarioDataProvider("SharedScenarioDataImportWizard:2")) {
								final boolean ranUpdate = sdp.getModelReference().executeWithTryLock(true, 10_000, () -> {
									final CompoundCommand command = new CompoundCommand("Update scenario");
									for (final DataOptions option : DataOptions.values()) {
										if (dataOptions.contains(option)) {
											if (dataToUpdater.containsKey(option)) {
												dataToUpdater.get(option).accept(command, target);
											}
										}
									}
									if (!command.canExecute()) {
										log.error("Update command failed for scenario " + destModelRecord.getName());
										throw new RuntimeException("Unable to execute command");
									}
									RunnerHelper.syncExecDisplayOptional(() -> target.getCommandStack().execute(command));
									if (autoSave) {
										try {
											target.getModelReference().save();
										} catch (final IOException e) {
											log.error("Unable to auto save " + destModelRecord.getName(), e);
										}
									}
								});
								if (!ranUpdate) {
									log.error("Unable to update scenario " + destModelRecord.getName());
								}
							}
							monitor.worked(1);
						}

					} finally {
						monitor.done();
					}

				} catch (final JsonProcessingException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			} catch (final MigrationForbiddenException e) {
				RunnerHelper.asyncExec(display -> {
					MessageDialog.openError(display.getActiveShell(), "Error importing base case data", "Base case scenario is in an older data format. Please re-publish the base case.");
				});
			} catch (final RuntimeException e) {
				if (e.getCause() instanceof MigrationForbiddenException) {
					RunnerHelper.asyncExec(display -> {
						MessageDialog.openError(display.getActiveShell(), "Error importing base case data", "Base case scenario is in an older data format. Please re-publish the base case.");
					});
				} else {
					throw e;
				}
			}
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createCommercialsUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(sdp);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new EMFJacksonModule());
			final String commercialJSON = mapper.writeValueAsString(commercialModel);
			return createCommercialsUpdater(commercialJSON);
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createCommercialsUpdater(String commercialJSON) {

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						pm.getPorts().forEach(ctx::registerType);
						pm.getPortGroups().forEach(ctx::registerType);
						pm.getPortCountryGroups().forEach(ctx::registerType);

						final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
						fm.getVessels().forEach(ctx::registerType);
						fm.getVesselGroups().forEach(ctx::registerType);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						final Map<EObject, EObject> mapOldToNew = new HashMap<>();

						try {
							updateCommercialModel(target, this, mapper, commercialJSON, mapOldToNew);
							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}

						final Map<EObject, Collection<Setting>> crossReferencecs = EcoreUtil.UsageCrossReferencer.findAll(mapOldToNew.keySet(), target.getScenario());

						final EditingDomain editingDomain = target.getEditingDomain();
						for (final Map.Entry<EObject, EObject> e : mapOldToNew.entrySet()) {
							final EObject oldMarket = e.getKey();
							final EObject newMarket = e.getValue();
							final Collection<EStructuralFeature.Setting> usages = crossReferencecs.get(oldMarket);

							if (usages != null) {
								for (final EStructuralFeature.Setting setting : usages) {
									appendAndExecute(ReplaceCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), oldMarket, Collections.singleton(newMarket)));
								}
							}
							// Replace the actual object
							appendAndExecute(ReplaceCommand.create(editingDomain, oldMarket.eContainer(), oldMarket.eContainmentFeature(), oldMarket, Collections.singleton(newMarket)));
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createSuezTariffUpdater(String json) {

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final CostModel costModel = ScenarioModelUtil.getCostModel(target);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						try {
							final SuezCanalTariff newTariff = mapper.readValue(json, SuezCanalTariff.class);
							appendAndExecute(SetCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__SUEZ_CANAL_TARIFF, newTariff));
							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createPortCostsUpdater(String json) {

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						pm.getPorts().forEach(ctx::registerType);
						pm.getPortGroups().forEach(ctx::registerType);
						pm.getPortCountryGroups().forEach(ctx::registerType);

						final CostModel costModel = ScenarioModelUtil.getCostModel(target);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						try {
							final List<PortCost> newCosts = mapper.readValue(json, new TypeReference<List<PortCost>>() {
							});

							 appendAndExecute(RemoveCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__PORT_COSTS, costModel.getPortCosts()));
							 appendAndExecute(AddCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__PORT_COSTS, newCosts));
							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createCooldownCostsUpdater(String json) {

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						pm.getPorts().forEach(ctx::registerType);
						pm.getPortGroups().forEach(ctx::registerType);
						pm.getPortCountryGroups().forEach(ctx::registerType);

						final CostModel costModel = ScenarioModelUtil.getCostModel(target);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						try {
							final List<CooldownPrice> newCosts = mapper.readValue(json, new TypeReference<List<CooldownPrice>>() {
							});

							appendAndExecute(RemoveCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__COOLDOWN_COSTS, costModel.getCooldownCosts()));
							appendAndExecute(AddCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__COOLDOWN_COSTS, newCosts));
							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createrRouteCostsUpdater(String json) {

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
						fm.getVessels().forEach(ctx::registerType);
						fm.getVesselGroups().forEach(ctx::registerType);

						final CostModel costModel = ScenarioModelUtil.getCostModel(target);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						try {
							final List<RouteCost> newCosts = mapper.readValue(json, new TypeReference<List<RouteCost>>() {
							});

							appendAndExecute(RemoveCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__ROUTE_COSTS, costModel.getRouteCosts()));
							appendAndExecute(AddCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__ROUTE_COSTS, newCosts));
							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createPanamaTariffUpdater(String json) {

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final CostModel costModel = ScenarioModelUtil.getCostModel(target);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						try {
							final PanamaCanalTariff newTariff = mapper.readValue(json, PanamaCanalTariff.class);
							appendAndExecute(SetCommand.create(target.getEditingDomain(), costModel, PricingPackage.Literals.COST_MODEL__PANAMA_CANAL_TARIFF, newTariff));
							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createSpotCargoMarketsUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(sdp);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new EMFJacksonModule());
			final String dpJSON = mapper.writeValueAsString(spotMarketsModel.getDesPurchaseSpotMarket());
			final String dsJSON = mapper.writeValueAsString(spotMarketsModel.getDesSalesSpotMarket());
			final String fpJSON = mapper.writeValueAsString(spotMarketsModel.getFobPurchasesSpotMarket());
			final String fsJSON = mapper.writeValueAsString(spotMarketsModel.getFobSalesSpotMarket());

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						pm.getPorts().forEach(ctx::registerType);
						pm.getPortGroups().forEach(ctx::registerType);
						pm.getPortCountryGroups().forEach(ctx::registerType);

						final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
						fm.getVessels().forEach(ctx::registerType);
						fm.getVesselGroups().forEach(ctx::registerType);

						final CommercialModel cm = ScenarioModelUtil.getCommercialModel(target);
						cm.getPurchaseContracts().forEach(ctx::registerType);
						cm.getSalesContracts().forEach(ctx::registerType);
						cm.getEntities().forEach(ctx::registerType);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						final Map<EObject, EObject> mapOldToNew = new HashMap<>();

						try {
							updateSpotMarkets(target, this, mapper, dsJSON, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET, mapOldToNew);
							updateSpotMarkets(target, this, mapper, fsJSON, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET, mapOldToNew);
							updateSpotMarkets(target, this, mapper, dpJSON, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET, mapOldToNew);
							updateSpotMarkets(target, this, mapper, fpJSON, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET, mapOldToNew);

							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}

						final Map<EObject, Collection<Setting>> crossReferencecs = EcoreUtil.UsageCrossReferencer.findAll(mapOldToNew.keySet(), target.getScenario());

						final EditingDomain editingDomain = target.getEditingDomain();
						for (final Map.Entry<EObject, EObject> e : mapOldToNew.entrySet()) {
							final EObject oldMarket = e.getKey();
							final EObject newMarket = e.getValue();
							final Collection<EStructuralFeature.Setting> usages = crossReferencecs.get(oldMarket);

							if (usages != null) {
								for (final EStructuralFeature.Setting setting : usages) {
									appendAndExecute(ReplaceCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), oldMarket, Collections.singleton(newMarket)));
								}
							}
							// Replace the actual object
							appendAndExecute(ReplaceCommand.create(editingDomain, oldMarket.eContainer(), oldMarket.eContainmentFeature(), oldMarket, Collections.singleton(newMarket)));
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createSpotCharterMarketsUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(sdp);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new EMFJacksonModule());
			final String charterInJSON = mapper.writeValueAsString(spotMarketsModel.getCharterInMarkets());
			final String charterOutJSON = mapper.writeValueAsString(spotMarketsModel.getCharterOutMarkets());
			// final String otherJSON1 = mapper.writeValueAsString(spotMarketsModel.getCharterOutMarketParameters());
			// final String otherJSON2 = mapper.writeValueAsString(spotMarketsModel.getCharterOutStartDate());

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						pm.getPorts().forEach(ctx::registerType);
						pm.getPortGroups().forEach(ctx::registerType);
						pm.getPortCountryGroups().forEach(ctx::registerType);

						final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
						fm.getVessels().forEach(ctx::registerType);
						fm.getVesselGroups().forEach(ctx::registerType);

						final CommercialModel cm = ScenarioModelUtil.getCommercialModel(target);
						cm.getPurchaseContracts().forEach(ctx::registerType);
						cm.getSalesContracts().forEach(ctx::registerType);
						cm.getEntities().forEach(ctx::registerType);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						final Map<EObject, EObject> mapOldToNew = new HashMap<>();

						try {
							updateCharterInMarkets(target, this, mapper, charterInJSON, mapOldToNew);
							updateCharterOutMarkets(target, this, mapper, charterOutJSON, mapOldToNew);

							ctx.runDeferredActions();
						} catch (final Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}

						final Map<EObject, Collection<Setting>> crossReferencecs = EcoreUtil.UsageCrossReferencer.findAll(mapOldToNew.keySet(), target.getScenario());

						final EditingDomain editingDomain = target.getEditingDomain();
						for (final Map.Entry<EObject, EObject> e : mapOldToNew.entrySet()) {
							final EObject oldMarket = e.getKey();
							final EObject newMarket = e.getValue();
							final Collection<EStructuralFeature.Setting> usages = crossReferencecs.get(oldMarket);

							if (usages != null) {
								for (final EStructuralFeature.Setting setting : usages) {
									appendAndExecute(ReplaceCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), oldMarket, Collections.singleton(newMarket)));
								}
							}
							// Replace the actual object
							appendAndExecute(ReplaceCommand.create(editingDomain, oldMarket.eContainer(), oldMarket.eContainmentFeature(), oldMarket, Collections.singleton(newMarket)));
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createADPUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
			final ADPModel adpModel = ScenarioModelUtil.getADPModel(sdp);
			if (adpModel == null) {
				return (a, b) -> {
				};
			}
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new EMFJacksonModule());
			final String adpJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(adpModel);

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);

						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						pm.getPorts().forEach(ctx::registerType);
						pm.getPortGroups().forEach(ctx::registerType);
						pm.getPortCountryGroups().forEach(ctx::registerType);

						final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
						fm.getVessels().forEach(ctx::registerType);
						fm.getVesselGroups().forEach(ctx::registerType);

						final CommercialModel cm = ScenarioModelUtil.getCommercialModel(target);
						cm.getPurchaseContracts().forEach(ctx::registerType);
						cm.getSalesContracts().forEach(ctx::registerType);
						cm.getEntities().forEach(ctx::registerType);

						final SpotMarketsModel smm = ScenarioModelUtil.getSpotMarketsModel(target);
						smm.getCharterInMarkets().forEach(ctx::registerType);
						smm.getDesPurchaseSpotMarket().getMarkets().forEach(ctx::registerType);
						smm.getDesSalesSpotMarket().getMarkets().forEach(ctx::registerType);
						smm.getFobPurchasesSpotMarket().getMarkets().forEach(ctx::registerType);
						smm.getFobSalesSpotMarket().getMarkets().forEach(ctx::registerType);

						final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
						mapper.registerModule(new EMFJacksonModule());

						try {
							final ADPModel newADPModel = mapper.readValue(adpJSON, ADPModel.class);
							ctx.runDeferredActions();
							final EditingDomain editingDomain = target.getEditingDomain();
							appendAndExecute(SetCommand.create(editingDomain, target.getScenario(), LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModel(), newADPModel));
						} catch (final Exception e1) {
							e1.printStackTrace();
							appendAndExecute(UnexecutableCommand.INSTANCE);
						}
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createFleetUpdater(final FleetModel fleetModel) throws JsonProcessingException {
			final BunkerFuelsVersion bunkerFuelsVersion = BunkerFuelsFromScenarioCopier.generateVersion(fleetModel);
			final VesselsVersion vesselsVersion = VesselsFromScenarioCopier.generateVersion(fleetModel);
			final VesselGroupsVersion groupsVersion = VesselGroupsFromScenarioCopier.generateVersion(fleetModel);

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						super.execute();
						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
						final EditingDomain editingDomain = target.getEditingDomain();

						final Command command1 = BunkerFuelsToScenarioImporter.getUpdateCommand(editingDomain, fm, bunkerFuelsVersion);
						appendAndExecute(command1);
						final Command command2 = VesselsToScenarioCopier.getUpdateCommand(editingDomain, fm, pm, vesselsVersion);
						appendAndExecute(command2);
						final Command command3 = VesselGroupsToScenarioImporter.getUpdateCommand(editingDomain, fm, groupsVersion);
						appendAndExecute(command3);

					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createPricingUpdater(final PricingModel pricingModel) throws JsonProcessingException {
			final PricingVersion pricingVersion = PricingFromScenarioCopier.generateVersion(pricingModel);
			final SettledPricesVersion settledPricesVersion = SettledPricesFromScenarioCopier.generateVersion(pricingModel);

			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						super.execute();
						final PricingModel pm = ScenarioModelUtil.getPricingModel(target);
						final EditingDomain editingDomain = target.getEditingDomain();
						final Command command1 = PricingToScenarioCopier.getUpdateCommand(editingDomain, pm, pricingVersion);
						appendAndExecute(command1);
						final Command command2 = SettledPricesToScenarioImporter.getUpdateCommand(editingDomain, pm, settledPricesVersion);
						appendAndExecute(command2);
					}
				});
			};
		}

		public static BiConsumer<CompoundCommand, IScenarioDataProvider> createPortAndDistanceUpdater(final PortModel portModel) {
			final DistancesVersion distancesVersion = DistancesFromScenarioCopier.generateVersion(portModel);
			final PortsVersion portsVersion = PortFromScenarioCopier.generateVersion(portModel);
			final PortGroupsVersion groupsVersion = PortGroupsFromScenarioCopier.generateVersion(portModel);
			return (cmd, target) -> {
				cmd.append(new CompoundCommand() {

					@Override
					protected boolean prepare() {
						super.prepare();
						return true;
					}

					@Override
					public void execute() {
						super.execute();
						final PortModel pm = ScenarioModelUtil.getPortModel(target);
						final EditingDomain editingDomain = target.getEditingDomain();
						final Command command1 = DistancesToScenarioCopier.getUpdateCommand(editingDomain, pm, distancesVersion, true);
						appendAndExecute(command1);
						final Command command2 = PortsToScenarioCopier.getUpdateCommand(editingDomain, pm, portsVersion);
						appendAndExecute(command2);
						final Command command3 = PortGroupsToScenarioImporter.getUpdateCommand(editingDomain, pm, groupsVersion);
						appendAndExecute(command3);
					}
				});
			};
		}
	}

	private static void updateSpotMarkets(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final EReference groupFeature,
			final Map<EObject, EObject> mapOldToNew) throws JsonParseException, JsonMappingException, IOException {

		final SpotMarketsModel smm = ScenarioModelUtil.getSpotMarketsModel(target);
		final List<SpotMarket> newMarkets = new LinkedList<>();
		final SpotMarketGroup newG = mapper.readValue(json, SpotMarketGroup.class);
		final SpotMarketGroup oldG = (SpotMarketGroup) smm.eGet(groupFeature);

		for (final SpotMarket newMarket : newG.getMarkets()) {
			boolean found = false;

			for (final SpotMarket oldMarket : oldG.getMarkets()) {
				if (Objects.equals(newMarket.getName(), oldMarket.getName())) {
					found = true;
					mapOldToNew.put(oldMarket, newMarket);
					break;
				}
			}
			if (!found) {
				newMarkets.add(newMarket);
			}
		}
		cmd.appendAndExecute(AddCommand.create(target.getEditingDomain(), oldG, SpotMarketsPackage.Literals.SPOT_MARKET_GROUP__MARKETS, newMarkets));
	}

	private static void updateCommercialModel(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final Map<EObject, EObject> mapOldToNew)
			throws JsonParseException, JsonMappingException, IOException {

		final CommercialModel oldCM = ScenarioModelUtil.getCommercialModel(target);
		final CommercialModel newCM = mapper.readValue(json, CommercialModel.class);

		{
			final List<EObject> newEntities = new LinkedList<>();
			for (final BaseLegalEntity newEntity : newCM.getEntities()) {
				boolean found = false;

				for (final BaseLegalEntity oldContract : oldCM.getEntities()) {
					if (Objects.equals(newEntity.getName(), oldContract.getName())) {
						found = true;
						mapOldToNew.put(oldContract, newEntity);
						break;
					}
				}
				if (!found) {
					newEntities.add(newEntity);
				}
			}
			cmd.appendAndExecute(AddCommand.create(target.getEditingDomain(), oldCM, CommercialPackage.Literals.COMMERCIAL_MODEL__ENTITIES, newEntities));
		}

		{
			final List<EObject> newContracts = new LinkedList<>();
			for (final PurchaseContract newContract : newCM.getPurchaseContracts()) {
				boolean found = false;

				for (final PurchaseContract oldContract : oldCM.getPurchaseContracts()) {
					if (Objects.equals(newContract.getName(), oldContract.getName())) {
						found = true;
						mapOldToNew.put(oldContract, newContract);
						break;
					}
				}
				if (!found) {
					newContracts.add(newContract);
				}
			}
			cmd.appendAndExecute(AddCommand.create(target.getEditingDomain(), oldCM, CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS, newContracts));
		}
		{
			final List<EObject> newContracts = new LinkedList<>();
			for (final SalesContract newContract : newCM.getSalesContracts()) {
				boolean found = false;

				for (final SalesContract oldContract : oldCM.getSalesContracts()) {
					if (Objects.equals(newContract.getName(), oldContract.getName())) {
						found = true;
						mapOldToNew.put(oldContract, newContract);
						break;
					}
				}
				if (!found) {
					newContracts.add(newContract);
				}
			}
			cmd.appendAndExecute(AddCommand.create(target.getEditingDomain(), oldCM, CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS, newContracts));
		}

		{
			final List<EObject> newContracts = new LinkedList<>();
			for (final CharterContract newContract : newCM.getCharteringContracts()) {
				boolean found = false;

				for (final CharterContract oldContract : oldCM.getCharteringContracts()) {
					if (Objects.equals(newContract.getName(), oldContract.getName())) {
						found = true;
						mapOldToNew.put(oldContract, newContract);
						break;
					}
				}
				if (!found) {
					newContracts.add(newContract);
				}
			}
			cmd.appendAndExecute(AddCommand.create(target.getEditingDomain(), oldCM, CommercialPackage.Literals.COMMERCIAL_MODEL__CHARTERING_CONTRACTS, newContracts));
		}
	}

	private static void updateCharterInMarkets(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final Map<EObject, EObject> mapOldToNew)
			throws JsonParseException, JsonMappingException, IOException {

		final SpotMarketsModel oldCM = ScenarioModelUtil.getSpotMarketsModel(target);
		final List<CharterInMarket> newCharterMarkets = mapper.readValue(json, new TypeReference<List<CharterInMarket>>() {
		});

		{
			final List<EObject> newMarkets = new LinkedList<>();
			for (final CharterInMarket newMarket : newCharterMarkets) {
				boolean found = false;

				for (final CharterInMarket oldMarket : oldCM.getCharterInMarkets()) {
					if (Objects.equals(newMarket.getName(), oldMarket.getName())) {
						found = true;
						mapOldToNew.put(oldMarket, newMarket);
						break;
					}
				}
				if (!found) {
					newMarkets.add(newMarket);
				}
			}
			cmd.appendAndExecute(AddCommand.create(target.getEditingDomain(), oldCM, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS, newMarkets));
		}
	}

	private static void updateCharterOutMarkets(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final Map<EObject, EObject> mapOldToNew)
			throws JsonParseException, JsonMappingException, IOException {

		final SpotMarketsModel oldCM = ScenarioModelUtil.getSpotMarketsModel(target);
		final List<CharterOutMarket> newCharterMarkets = mapper.readValue(json, new TypeReference<List<CharterOutMarket>>() {
		});

		{
			final List<EObject> newMarkets = new LinkedList<>();
			for (final CharterOutMarket newMarket : newCharterMarkets) {
				boolean found = false;

				for (final CharterOutMarket oldMarket : oldCM.getCharterOutMarkets()) {
					if (Objects.equals(newMarket.getName(), oldMarket.getName())) {
						found = true;
						mapOldToNew.put(oldMarket, newMarket);
						break;
					}
				}
				if (!found) {
					newMarkets.add(newMarket);
				}
			}
			cmd.appendAndExecute(AddCommand.create(target.getEditingDomain(), oldCM, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS, newMarkets));
		}
	}

	private static BiConsumer<CompoundCommand, IScenarioDataProvider> createCargoUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		if (cargoModel == null) {
			return (a, b) -> {
			};
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new EMFJacksonModule());
		final String loadsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getLoadSlots());
		final String dischargesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getDischargeSlots());
		final String cargoesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getCargoes());

		System.out.println(loadsJSON);
		System.out.println(dischargesJSON);
		System.out.println(cargoesJSON);

		return (cmd, target) -> {
			cmd.append(new CompoundCommand() {

				@Override
				protected boolean prepare() {
					super.prepare();
					return true;
				}

				@Override
				public void execute() {
					final EMFDeserializationContext ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);
					// Ignore back reference
					ctx.ignoreFeature(CargoPackage.Literals.SLOT__CARGO);

					final PortModel pm = ScenarioModelUtil.getPortModel(target);
					pm.getPorts().forEach(ctx::registerType);
					pm.getPortGroups().forEach(ctx::registerType);
					pm.getPortCountryGroups().forEach(ctx::registerType);

					final FleetModel fm = ScenarioModelUtil.getFleetModel(target);
					fm.getVessels().forEach(ctx::registerType);
					fm.getVesselGroups().forEach(ctx::registerType);

					final CommercialModel cm = ScenarioModelUtil.getCommercialModel(target);
					cm.getPurchaseContracts().forEach(ctx::registerType);
					cm.getSalesContracts().forEach(ctx::registerType);
					cm.getEntities().forEach(ctx::registerType);

					final SpotMarketsModel smm = ScenarioModelUtil.getSpotMarketsModel(target);
					smm.getCharterInMarkets().forEach(ctx::registerType);
					smm.getDesPurchaseSpotMarket().getMarkets().forEach(ctx::registerType);
					smm.getDesSalesSpotMarket().getMarkets().forEach(ctx::registerType);
					smm.getFobPurchasesSpotMarket().getMarkets().forEach(ctx::registerType);
					smm.getFobSalesSpotMarket().getMarkets().forEach(ctx::registerType);

					// ANYTHING ELSE?

					final ObjectMapper mapper = new ObjectMapper(null, null, ctx);
					mapper.registerModule(new EMFJacksonModule());

					try {
						final List<LoadSlot> newLoads = mapper.readValue(loadsJSON, new TypeReference<List<LoadSlot>>() {
						});
						final List<DischargeSlot> newDischarges = mapper.readValue(dischargesJSON, new TypeReference<List<DischargeSlot>>() {
						});
						final List<Cargo> newCargoes = mapper.readValue(cargoesJSON, new TypeReference<List<Cargo>>() {
						});
						ctx.runDeferredActions();
						final EditingDomain editingDomain = target.getEditingDomain();
						// Renumber spots
						// Copy slots
						// Copy cargoes
						// Check back references.
						// appendAndExecute(SetCommand.create(editingDomain, target.getScenario(), LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModel(), newCargoModel));
					} catch (final Exception e1) {
						e1.printStackTrace();
						appendAndExecute(UnexecutableCommand.INSTANCE);
					}
				}
			});
		};
	}

	public static boolean checkPricingDataMatch(final IBaseCaseVersionsProvider provider, final Map<String, String> targetVersions) {
		boolean settledOK = true;// checkMatch(LNGScenarioSharedModelTypes.SETTLED_PRICES.getID(), provider, targetVersions);
		boolean marketOk = checkMatch(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), provider, targetVersions);

		return marketOk && settledOK;
	}

	public static boolean checkFleetDataMatch(final IBaseCaseVersionsProvider provider, final Map<String, String> targetVersions) {

		boolean fleetOK = checkMatch(LNGScenarioSharedModelTypes.FLEET.getID(), provider, targetVersions);
		boolean vesselGroupsOK = checkMatch(LNGScenarioSharedModelTypes.VESSEL_GROUPS.getID(), provider, targetVersions);
		boolean bunkerFuelsIO = checkMatch(LNGScenarioSharedModelTypes.BUNKER_FUELS.getID(), provider, targetVersions);

		return fleetOK && vesselGroupsOK && bunkerFuelsIO;

	}

	public static boolean checkPortAndDistanceDataMatch(final IBaseCaseVersionsProvider provider, final Map<String, String> targetVersions) {

		boolean distanceOk = checkMatch(LNGScenarioSharedModelTypes.DISTANCES.getID(), provider, targetVersions);
		boolean portOk = checkMatch(LNGScenarioSharedModelTypes.LOCATIONS.getID(), provider, targetVersions);
		boolean portGroupsOk = checkMatch(LNGScenarioSharedModelTypes.PORT_GROUPS.getID(), provider, targetVersions);

		return portOk && distanceOk && portGroupsOk;
	}

	public static boolean checkMatch(String typeId, IBaseCaseVersionsProvider provider, Map<String, String> dataVersions) {
		String baseCaseVersion = provider.getVersion(typeId);
		if (baseCaseVersion == null) {
			// No base case version, not much to do.
			return true;
		}
		String version = dataVersions.get(typeId);
		return version != null && Objects.equals(baseCaseVersion, version);
	}

	public static List<DataOptionGroup> createGroups(IBaseCaseVersionsProvider p, Map<String, String> scenarioDataVersions) {
		final List<DataOptionGroup> groups = new LinkedList<>();
		groups.add(new DataOptionGroup("Pricing", !SharedScenarioDataUtils.checkPricingDataMatch(p, scenarioDataVersions), true, true, DataOptions.PricingData));
		if (LicenseFeatures.isPermitted("features:hub-sync-distances")) {
			groups.add(new DataOptionGroup("Ports && Distances", !checkPortAndDistanceDataMatch(p, scenarioDataVersions), true, false, DataOptions.PortData));
		}
		if (LicenseFeatures.isPermitted("features:hub-sync-vessels")) {
			groups.add(new DataOptionGroup("Vessels", !checkFleetDataMatch(p, scenarioDataVersions), true, false, DataOptions.FleetDatabase));
		}
		// groups.add(new DataOptionGroup("Commercial", true, true, false, DataOptions.CommercialData));
		// // groups.add(new DataOptionGroup("Misc", true, false, false));
		// }
		return groups;
	}
}

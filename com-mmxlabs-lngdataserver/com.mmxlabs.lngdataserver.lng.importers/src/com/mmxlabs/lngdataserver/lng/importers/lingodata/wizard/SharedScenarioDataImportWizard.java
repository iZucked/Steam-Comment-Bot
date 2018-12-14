/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.io.File;
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
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.exporters.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distances.PortAndDistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedDataScenariosSelectionPage.DataOptions;
import com.mmxlabs.lngdataserver.lng.importers.port.PortsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.pricing.PricingToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
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
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class SharedScenarioDataImportWizard extends Wizard implements IImportWizard {

	private static final Logger log = LoggerFactory.getLogger(SharedScenarioDataImportWizard.class);

	private SharedDataScenariosSelectionPage destinationPage;
	private final ScenarioInstance currentScenario;

	public SharedScenarioDataImportWizard(final ScenarioInstance currentScenario) {
		super();
		this.currentScenario = currentScenario;
		this.setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		final UpdateJob job = new UpdateJob(destinationPage.getSelectedDataOptions(), currentScenario, destinationPage.getSelectedScenarios());

		try {
			getContainer().run(true, true, job);
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Shared Scenario Data Import"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		destinationPage = new SharedDataScenariosSelectionPage("Scenario Selection");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		addPage(destinationPage);
	}

	@Override
	public boolean canFinish() {
		return destinationPage.isPageComplete();
	}

	private class UpdateJob implements IRunnableWithProgress {

		private final @NonNull ScenarioInstance sourceScenario;
		private final List<ScenarioInstance> destinationScenarios;
		private final Set<DataOptions> dataOptions;

		public UpdateJob(final Set<DataOptions> enumSet, final @NonNull ScenarioInstance sourceScenario, final List<ScenarioInstance> destinationScenarios) {
			this.dataOptions = enumSet;
			this.sourceScenario = sourceScenario;
			this.destinationScenarios = destinationScenarios;
		}

		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			@NonNull
			final ScenarioModelRecord sourceModelRecord = SSDataManager.Instance.getModelRecord(sourceScenario);

			final Map<DataOptions, BiConsumer<CompoundCommand, IScenarioDataProvider>> dataToUpdater = new EnumMap<>(DataOptions.class);

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
					default:
						break;

					}
				}

				monitor.beginTask("Update Scenarios", destinationScenarios.size());
				try {
					for (final ScenarioInstance destScenario : destinationScenarios) {

						if (monitor.isCanceled()) {
							return;
						}

						monitor.subTask("Update " + destScenario.getName());
						// Do not import into self
						if (destScenario == sourceScenario) {
							monitor.worked(1);
							continue;
						}
						@NonNull
						final ScenarioModelRecord destModelRecord = SSDataManager.Instance.getModelRecord(destScenario);
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
									log.error("Update command failed for scenario " + destScenario.getName());
									throw new RuntimeException("Unable to execute command");
								}
								RunnerHelper.syncExecDisplayOptional(() -> target.getCommandStack().execute(command));
								if (autoSave) {
									try {
										target.getModelReference().save();
									} catch (final IOException e) {
										log.error("Unable to auto save " + destScenario.getName(), e);
									}
								}
							});
							if (!ranUpdate) {
								log.error("Unable to update scenario " + destScenario.getName());
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
		}

		private BiConsumer<CompoundCommand, IScenarioDataProvider> createCommercialsUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(sdp);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new EMFJacksonModule());
			final String commercialJSON = mapper.writeValueAsString(commercialModel);

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

		private BiConsumer<CompoundCommand, IScenarioDataProvider> createSpotCargoMarketsUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
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

		private BiConsumer<CompoundCommand, IScenarioDataProvider> createSpotCharterMarketsUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
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

		private BiConsumer<CompoundCommand, IScenarioDataProvider> createADPUpdater(final IScenarioDataProvider sdp) throws JsonProcessingException {
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

		private BiConsumer<CompoundCommand, IScenarioDataProvider> createFleetUpdater(final FleetModel fleetModel) throws JsonProcessingException {
			final VesselsVersion vesselsVersion = VesselsFromScenarioCopier.generateVersion(fleetModel);

			final ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules();
			mapper.registerModule(new Jdk8Module());
			final String expectedJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vesselsVersion);
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
						final Command command1 = VesselsToScenarioCopier.getUpdateCommand(editingDomain, fm, pm, vesselsVersion);
						appendAndExecute(command1);

						if (false) {
							final VesselsVersion vv = VesselsFromScenarioCopier.generateVersion(fm);

							String actualJSON;
							try {
								actualJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vv);
								if (!expectedJSON.equals(actualJSON)) {
									Files.write(expectedJSON, new File("C:/users/sg/desktop/a.txt"), Charsets.UTF_8);
									Files.write(actualJSON, new File("C:/users/sg/desktop/b.txt"), Charsets.UTF_8);
									System.out.println("Actual");
									System.out.println(actualJSON);
									System.out.println("Expected");
									System.out.println(expectedJSON);
								}
							} catch (final Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
			};
		}

		private BiConsumer<CompoundCommand, IScenarioDataProvider> createPricingUpdater(final PricingModel pricingModel) throws JsonProcessingException {
			final PricingVersion pricingVersion = PricingFromScenarioCopier.generateVersion(pricingModel);

			final ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules();
			mapper.registerModule(new Jdk8Module());
			final String expectedJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pricingModel);
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
					}
				});
			};
		}

		private BiConsumer<CompoundCommand, IScenarioDataProvider> createPortAndDistanceUpdater(final PortModel portModel) {
			final DistancesVersion distancesVersion = DistancesFromScenarioCopier.generateVersion(portModel);
			final PortsVersion portsVersion = PortFromScenarioCopier.generateVersion(portModel);
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
						final Command command1 = PortAndDistancesToScenarioCopier.getUpdateCommand(editingDomain, pm, distancesVersion, true);
						appendAndExecute(command1);
						final Command command2 = PortsToScenarioCopier.getUpdateCommand(editingDomain, pm, portsVersion);
						appendAndExecute(command2);
					}
				});
			};
		}
	}

	private void updateSpotMarkets(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final EReference groupFeature,
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

	private void updateCommercialModel(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final Map<EObject, EObject> mapOldToNew)
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

	private void updateCharterInMarkets(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final Map<EObject, EObject> mapOldToNew)
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

	private void updateCharterOutMarkets(final IScenarioDataProvider target, final CompoundCommand cmd, final ObjectMapper mapper, final String json, final Map<EObject, EObject> mapOldToNew)
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
}

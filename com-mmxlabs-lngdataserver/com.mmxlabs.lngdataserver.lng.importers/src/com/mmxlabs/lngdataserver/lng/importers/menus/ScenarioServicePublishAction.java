/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Version;
import com.mmxlabs.lngdataserver.integration.ports.PortsClient;
import com.mmxlabs.lngdataserver.integration.pricing.PricingClient;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.ReportsServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.ReportPublisherExtensionUtil;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsClient;
import com.mmxlabs.lngdataserver.lng.exporters.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioServicePublishAction {

	public static void publishScenario(ScenarioInstance scenarioInstance) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

		try {
			dialog.run(true, false, m -> publishScenario(scenarioInstance, m));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void publishScenario(ScenarioInstance scenarioInstance, IProgressMonitor parent_progressMonitor) {

		parent_progressMonitor.beginTask("Publish base case", 1000);
		SubMonitor progressMonitor = SubMonitor.convert(parent_progressMonitor, 1000);
		try {
			progressMonitor.subTask("Prepare base case");
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

			// Copy the scenario
			// TODO

			IScenarioDataProvider scenarioDataProvider = null;
			LNGScenarioModel scenarioModel = null;

			try (IScenarioDataProvider o_scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
				final LNGScenarioModel o_scenarioModel = o_scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

				final EcoreUtil.Copier copier = new Copier();
				scenarioModel = (LNGScenarioModel) copier.copy(o_scenarioModel);

				copier.copyReferences();
				AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

				// Strip optimisation result
				analyticsModel.getOptionModels().clear();
				analyticsModel.getOptimisations().clear();

				scenarioDataProvider = SimpleScenarioDataProvider.make(EcoreUtil.copy(modelRecord.getManifest()), scenarioModel);

				// Evaluate scenario
				// try (IScenarioDataProvider o_scenarioDataProvider =
				// modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL"))
				// {
				final CleanableExecutorService executorService = LNGScenarioChainBuilder.createExecutorService(1);

				try {
					final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

					// final LNGScenarioModel o_scenarioModel =
					// o_scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
					OptimisationPlan optimisationPlan = OptimisationHelper.getOptimiserSettings(o_scenarioModel, true, null, false, false, null);
					assert optimisationPlan != null;

					// Hack: Add on shipping only hint to avoid generating spot markets during eval.
					final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, scenarioDataProvider, scenarioInstance, optimisationPlan, editingDomain, null, true,
							LNGTransformerHelper.HINT_SHIPPING_ONLY);
					scenarioDataProvider.setLastEvaluationFailed(true);
					runner.evaluateInitialState();
					scenarioDataProvider.setLastEvaluationFailed(false);
				} finally {
					executorService.shutdown();
				}
			}

			// CreateFile
			File tmpScenarioFile = null;

			try {
				tmpScenarioFile = ScenarioStorageUtil.getTempDirectory().createTempFile("publishScenarioUtil_", "");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			try {
				ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, tmpScenarioFile);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			progressMonitor.worked(200);

			BaseCaseServiceClient baseCaseServiceClient = new BaseCaseServiceClient();
			progressMonitor.subTask("Upload base case");
			/*
			 * // Port data
			 * 
			 * String portsVersionUUID = null; try { PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel); portsVersionUUID = portModel.getUuid();
			 * 
			 * PortApi p = new PortApi(); p.fetchVersionUsingGET(portsVersionUUID); } catch (ApiException e) { String portUUID = exportPort(modelRecord, scenarioModel); }
			 * 
			 * // Pricing data
			 * 
			 */

			// String distanceVersionUUID = checkDistancesDataVersion(modelRecord, scenarioModel);
			String pricingVersionUUID = checkPricingDataVersion(modelRecord, scenarioModel);

			// try {
			//
			//
			// if (!PricingRepository.INSTANCE.hasVersion(pricingVersionUUID)) {
			//
			// }
			//
			// PricingClient p = new PricingClient();
			// p.getUpstreamVersion("", "", pricingVersionUUID);
			// } catch (IOException e) {
			// pricingVersionUUID = exportPricing(modelRecord, scenarioModel);
			// }

			/*
			 * // Vessels data
			 * 
			 * String fleetVersionUUID = null; try { FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel); fleetVersionUUID = fleetModel.getUuid(); VesselsApi p = new VesselsApi();
			 * p.getVersionUsingGET(fleetVersionUUID); } catch (com.mmxlabs.lngdataservice.client.vessel.ApiException e) { fleetVersionUUID = exportVessel(modelRecord, scenarioModel); }
			 * 
			 * // distances Data String distancesVersionUUID = null;
			 */

			// UploadFile
			String response = null;
			SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				// response = baseCaseServiceClient.uploadBaseCase(tmpScenarioFile, portsVersionUUID, fleetVersionUUID, pricingVersionUUID, distancesVersionUUID);
				response = baseCaseServiceClient.uploadBaseCase(tmpScenarioFile, scenarioInstance.getName(), pricingVersionUUID, wrapMonitor(uploadMonitor));
			} catch (IOException e) {
				System.out.println("Error uploading the basecase scenario");
				e.printStackTrace();
			} finally {
				uploadMonitor.done();
			}
			if (response == null) {
				throw new RuntimeException("Error publishing base case");
			}
			ObjectMapper mapper = new ObjectMapper();
			String uuid = null;
			try {
				JsonNode actualObj = mapper.readTree(response);
				uuid = actualObj.get("uuid").textValue();
			} catch (IOException e) {
				System.out.println("Cannot read server response after basecase upload");
				e.printStackTrace();
			}

			Iterable<IReportPublisherExtension> publishers = ReportPublisherExtensionUtil.getContextMenuExtensions();
			ArrayList<IReportPublisherExtension> publishersList = Lists.newArrayList(publishers);

			progressMonitor.subTask("Publish base case reports");
			SubMonitor publishersMonitor = progressMonitor.split(300);
			try {

				publishersMonitor.beginTask("Publish base case reports", publishersList.size());
				for (IReportPublisherExtension reportPublisherExtension : publishersList) {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

					reportPublisherExtension.publishReport(scenarioDataProvider, ScenarioModelUtil.getScheduleModel(scenarioDataProvider), outputStream);
					String reportType = reportPublisherExtension.getReportType();

					// Call the correct upload report endpoint;
					outputStream.toByteArray();
					ReportsServiceClient reportsServiceClient = new ReportsServiceClient();

					try {
						reportsServiceClient.uploadReport(outputStream.toString(), reportType, uuid);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					publishersMonitor.worked(1);
				}
			} finally {
				publishersMonitor.done();
			}

			try {
				baseCaseServiceClient.setCurrentBaseCase(uuid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error while setting the new baseCase as current");
				e.printStackTrace();
			}
		} finally {
			progressMonitor.done();
		}
	}

	private static String checkPricingDataVersion(final ScenarioModelRecord modelRecord, LNGScenarioModel scenarioModel) {
		String pricingVersionUUID;
		{
			PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
			pricingVersionUUID = pricingModel.getMarketCurveDataVersion();

			boolean hasLocal = PricingRepository.INSTANCE.hasLocalVersion(pricingVersionUUID);
			Boolean hasUpstream = PricingRepository.INSTANCE.hasUpstreamVersion(pricingVersionUUID);
			if (hasUpstream == null) {
				// no response from upstream - abort!
			}

			if (hasLocal && hasUpstream) {
				// Great, versions all present
			}
			if (hasUpstream && !hasLocal) {
				// Pull down from upstream
				try {
					if (!PricingRepository.INSTANCE.syncUpstreamVersion(pricingVersionUUID)) {
						// Error, but not critical.
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (!hasLocal) {
					// export to backend server
					exportPricing(modelRecord, scenarioModel);
				}
				if (!hasUpstream) {
					// Publish
					try {
						PricingRepository.INSTANCE.publishVersion(pricingVersionUUID);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return pricingVersionUUID;
	}

	private static String exportPricing(ModelRecord modelRecord, LNGScenarioModel scenarioModel) {
		String url = BackEndUrlProvider.INSTANCE.getUrl();
		String versionId = null;
		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		Version version = PricingFromScenarioCopier.generateVersion(pricingModel);

		try {
			boolean res = PricingClient.saveVersion(url, version);
			if (res) {
				versionId = version.getIdentifier();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionId;
	}

	private static String exportPort(ModelRecord modelRecord, LNGScenarioModel scenarioModel) {
		String url = BackEndUrlProvider.INSTANCE.getUrl();
		String versionId = null;
		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		com.mmxlabs.lngdataservice.client.ports.model.Version version = PortFromScenarioCopier.generateVersion(portModel);

		try {
			boolean res = PortsClient.saveVersion(url, version);
			if (res) {
				versionId = version.getIdentifier();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionId;
	}

	private static String exportVessel(ModelRecord modelRecord, LNGScenarioModel scenarioModel) {
		String url = BackEndUrlProvider.INSTANCE.getUrl();
		String versionId = null;
		FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		com.mmxlabs.lngdataservice.client.vessel.model.Version version = VesselsFromScenarioCopier.generateVersion(fleetModel);

		try {
			boolean res = VesselsClient.saveVersion(url, version);
			if (res) {
				versionId = version.getIdentifier();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionId;
	}

	private static IProgressListener wrapMonitor(IProgressMonitor monitor) {
		if (monitor == null) {
			return null;
		}

		return new IProgressListener() {
			boolean firstCall = true;

			@Override
			public void update(long bytesRead, long contentLength, boolean done) {
				if (firstCall) {
					int total = (int) (contentLength / 1000L);
					if (total == 0) {
						total = 1;
					}
					monitor.beginTask("Transfer", total);
					firstCall = false;
				}
				int worked = (int) (bytesRead / 1000L);
				monitor.worked(worked);
			}
		};
	}

}

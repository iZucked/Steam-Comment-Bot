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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.integration.distances.DistanceUploaderClient;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.ports.PortsUploaderClient;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.pricing.PricingUploadClient;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.ReportsServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.ReportPublisherExtensionUtil;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsUploaderClient;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.exporters.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.menus.PublishBasecaseException.Type;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioServicePublishAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServicePublishAction.class);

	public static void publishScenario(final ScenarioInstance scenarioInstance) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

		try {
			dialog.run(true, false, m -> publishScenario(scenarioInstance, m));
		} catch (final InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
			final Throwable cause = e.getCause();
			if (cause instanceof PublishBasecaseException) {
				final PublishBasecaseException publishBasecaseException = (PublishBasecaseException) cause;
				switch (publishBasecaseException.getType()) {
				case FAILED_TO_EVALUATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error publishing", "Failed to evaluate the scenario. Unable to publish as base case.");
					break;
				case FAILED_TO_SAVE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error publishing", "Failed to save the base case scenario to a temporary file. Unable to publish as base case.");
					break;
				case FAILED_TO_UPLOAD_BASECASE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error publishing", "Failed to upload the base case scenario. Unable to publish as base case.");
					break;
				case FAILED_TO_GENERATE_REPORT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error publishing", e.getMessage() + " Unable to publish as base case.");
					break;
				case FAILED_TO_UPLOAD_REPORT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error publishing", e.getMessage() + " Unable to publish as base case.");
					break;
				case FAILED_TO_MAKE_CURRENT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error publishing", "Base case uploaded but was unable to mark as current.");
					break;
				default:
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error publishing", "Unknown error occurred. Unable to publish as base case.");
					break;
				}
			}
		} catch (final InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	public static void publishScenario(final ScenarioInstance scenarioInstance, final IProgressMonitor parent_progressMonitor) {

		parent_progressMonitor.beginTask("Publish base case", 1000);
		final SubMonitor progressMonitor = SubMonitor.convert(parent_progressMonitor, 1000);
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
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

				// Strip optimisation result
				LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);

				scenarioDataProvider = SimpleScenarioDataProvider.make(EcoreUtil.copy(modelRecord.getManifest()), scenarioModel);

				// Evaluate scenario
				final OptimisationPlan optimisationPlan = OptimisationHelper.getOptimiserSettings(o_scenarioModel, true, null, false, false, null);
				assert optimisationPlan != null;

				// Hack: Add on shipping only hint to avoid generating spot markets during eval.
				final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, scenarioInstance) //
						.withThreadCount(1) //
						.withOptimisationPlan(optimisationPlan) //
						.withHints(LNGTransformerHelper.HINT_SHIPPING_ONLY) //
						.buildDefaultRunner();

				try {
					scenarioDataProvider.setLastEvaluationFailed(true);
					runnerBuilder.evaluateInitialState();
					scenarioDataProvider.setLastEvaluationFailed(false);
				} catch (final Exception e) {
					throw new PublishBasecaseException("Error evaluating scenario.", Type.FAILED_TO_EVALUATE);
				} finally {
					runnerBuilder.dispose();
				}
			}

			// CreateFile
			File tmpScenarioFile = null;

			try {
				tmpScenarioFile = ScenarioStorageUtil.getTempDirectory().createTempFile("publishScenarioUtil_", "");
			} catch (final IOException e) {
				e.printStackTrace();
				throw new PublishBasecaseException("Error saving temporary scenario.", Type.FAILED_TO_SAVE, e);
			}

			try {
				ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, tmpScenarioFile);
			} catch (final IOException e) {
				e.printStackTrace();
				throw new PublishBasecaseException("Error saving temporary scenario.", Type.FAILED_TO_SAVE, e);
			}

			progressMonitor.worked(200);

			final BaseCaseServiceClient baseCaseServiceClient = new BaseCaseServiceClient();
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
			final String pricingVersionUUID = checkPricingDataVersion(modelRecord, scenarioModel);

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
			final SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				// response = baseCaseServiceClient.uploadBaseCase(tmpScenarioFile, portsVersionUUID, fleetVersionUUID, pricingVersionUUID, distancesVersionUUID);
				response = baseCaseServiceClient.uploadBaseCase(tmpScenarioFile, scenarioInstance.getName(), pricingVersionUUID, wrapMonitor(uploadMonitor));
			} catch (final IOException e) {
				System.out.println("Error uploading the basecase scenario");
				e.printStackTrace();
				throw new PublishBasecaseException("Error uploading scenario.", Type.FAILED_TO_UPLOAD_BASECASE, e);

			} finally {
				uploadMonitor.done();
			}
			if (response == null) {
				throw new RuntimeException("Error publishing base case");
			}
			final ObjectMapper mapper = new ObjectMapper();
			String uuid = null;
			try {
				final JsonNode actualObj = mapper.readTree(response);
				uuid = actualObj.get("uuid").textValue();
			} catch (final IOException e) {
				System.out.println("Cannot read server response after basecase upload");
				e.printStackTrace();
			}

			final Iterable<IReportPublisherExtension> publishers = ReportPublisherExtensionUtil.getContextMenuExtensions();
			final ArrayList<IReportPublisherExtension> publishersList = Lists.newArrayList(publishers);

			progressMonitor.subTask("Publish base case reports");
			final SubMonitor publishersMonitor = progressMonitor.split(300);
			try {

				publishersMonitor.beginTask("Publish base case reports", publishersList.size());
				for (final IReportPublisherExtension reportPublisherExtension : publishersList) {
					try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

						reportPublisherExtension.publishReport(scenarioDataProvider, ScenarioModelUtil.getScheduleModel(scenarioDataProvider), outputStream);
						final String reportType = reportPublisherExtension.getReportType();

						// Call the correct upload report endpoint;
						outputStream.toByteArray();
						final ReportsServiceClient reportsServiceClient = new ReportsServiceClient();

						try {
							reportsServiceClient.uploadReport(outputStream.toString(), reportType, uuid);
						} catch (final IOException e) {
							e.printStackTrace();
							throw new PublishBasecaseException("Error uploading report " + reportType, Type.FAILED_TO_UPLOAD_REPORT, e);

						}
					} catch (final Exception e) {
						LOG.error(e.getMessage(), e);
						throw new PublishBasecaseException("Error generating report " + reportPublisherExtension.getReportType(), Type.FAILED_TO_GENERATE_REPORT, e);
					}
					publishersMonitor.worked(1);
				}
			} finally {
				publishersMonitor.done();
			}

			try {
				baseCaseServiceClient.setCurrentBaseCase(uuid);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error while setting the new baseCase as current");
				e.printStackTrace();
				throw new PublishBasecaseException("Error marking base case current", Type.FAILED_TO_MAKE_CURRENT, e);

			}
		} finally {
			progressMonitor.done();
		}
	}

	private static String checkPricingDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) {
		String pricingVersionUUID;
		{
			final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
			pricingVersionUUID = pricingModel.getMarketCurveDataVersion();

			final boolean hasLocal = PricingRepository.INSTANCE.hasLocalVersion(pricingVersionUUID);
			final Boolean hasUpstream = PricingRepository.INSTANCE.hasUpstreamVersion(pricingVersionUUID);
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
				} catch (final Exception e) {
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
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return pricingVersionUUID;
	}

	private static String exportPricing(final ModelRecord modelRecord, final LNGScenarioModel scenarioModel) {
		final String url = BackEndUrlProvider.INSTANCE.getUrl();
		String versionId = null;
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		final PricingVersion version = PricingFromScenarioCopier.generateVersion(pricingModel);

		try {
			final boolean res = PricingUploadClient.saveVersion(url, version);
			if (res) {
				versionId = version.getIdentifier();
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionId;
	}

	private static String exportDistances(final ModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final DistancesVersion version = DistancesFromScenarioCopier.generateVersion(portModel);
		DistanceUploaderClient.saveVersion(BackEndUrlProvider.INSTANCE.getUrl(), version);
		return version.getIdentifier();
	}

	private static String exportPort(final ModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final PortsVersion version = PortFromScenarioCopier.generateVersion(portModel);
		PortsUploaderClient.saveVersion(BackEndUrlProvider.INSTANCE.getUrl(), version);
		return version.getIdentifier();
	}

	private static String exportVessel(final ModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final VesselsVersion version = VesselsFromScenarioCopier.generateVersion(fleetModel);
		VesselsUploaderClient.saveVersion(BackEndUrlProvider.INSTANCE.getUrl(), version);
		return version.getIdentifier();
	}

	private static IProgressListener wrapMonitor(final IProgressMonitor monitor) {
		if (monitor == null) {
			return null;
		}

		return new IProgressListener() {
			boolean firstCall = true;

			@Override
			public void update(final long bytesRead, final long contentLength, final boolean done) {
				if (firstCall) {
					int total = (int) (contentLength / 1000L);
					if (total == 0) {
						total = 1;
					}
					monitor.beginTask("Transfer", total);
					firstCall = false;
				}
				final int worked = (int) (bytesRead / 1000L);
				monitor.worked(worked);
			}
		};
	}

}

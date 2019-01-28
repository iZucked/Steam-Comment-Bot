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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mmxlabs.common.util.CheckedSupplier;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.ReportsServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.ReportPublisherExtensionUtil;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsRepository;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.exporters.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.menus.PublishBasecaseException.Type;
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
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioServicePublishAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServicePublishAction.class);

	private static final String MSG_ERROR_PUBLISHING = "Error publishing";

	private ScenarioServicePublishAction() {

	}

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
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Failed to evaluate the scenario. Unable to publish as base case.");
					break;
				case FAILED_TO_SAVE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Failed to save the base case scenario to a temporary file. Unable to publish as base case.");
					break;
				case FAILED_TO_UPLOAD_BASECASE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Failed to upload the base case scenario. Unable to publish as base case.");
					break;
				case FAILED_TO_GENERATE_REPORT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, e.getMessage() + " Unable to publish as base case.");
					break;
				case FAILED_TO_UPLOAD_BACKING_DATA:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Error uploading refernce data. Unable to publish as base case.");
					break;
				case FAILED_TO_UPLOAD_REPORT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, e.getMessage() + " Unable to publish as base case.");
					break;
				case FAILED_TO_MAKE_CURRENT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Base case uploaded but was unable to mark as current.");
					break;
				default:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Unknown error occurred. Unable to publish as base case.");
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

			final String pricingVersionUUID;
			final String distanceVersionUUID;
			final String portVersionUUID;
			final String fleetVersionUUID;
			try {
				pricingVersionUUID = checkPricingDataVersion(modelRecord, scenarioModel);
				distanceVersionUUID = ""; // checkDistanceDataVersion(modelRecord, scenarioModel);
				portVersionUUID = ""; // checkPortDataVersion(modelRecord, scenarioModel);
				fleetVersionUUID = ""; // checkFleetDataVersion(modelRecord, scenarioModel);
			} catch (final IOException e) {
				System.out.println("Error uploading the basecase scenario");
				e.printStackTrace();
				throw new PublishBasecaseException("Error uploading scenario.", Type.FAILED_TO_UPLOAD_BACKING_DATA, e);
			} finally {
			}
			// UploadFile
			String response = null;
			final SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				response = baseCaseServiceClient.uploadBaseCase(tmpScenarioFile, scenarioInstance.getName(), //
						pricingVersionUUID, //
						distanceVersionUUID, portVersionUUID, //
						fleetVersionUUID, //
						wrapMonitor(uploadMonitor));
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

						// Call the correct upload report endpoint
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
				System.out.println("Error while setting the new baseCase as current");
				e.printStackTrace();
				throw new PublishBasecaseException("Error marking base case current", Type.FAILED_TO_MAKE_CURRENT, e);

			}
		} finally {
			progressMonitor.done();
		}
	}

	private static @Nullable String checkPricingDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		final String version = pricingModel.getMarketCurveDataVersion();
		if (genericVersionUpload(PricingRepository.INSTANCE, version, () -> exportPricing(scenarioModel))) {
			return version;
		}
		return null;
	}

	private static @Nullable String checkDistanceDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final String version = portModel.getDistanceDataVersion();
		if (genericVersionUpload(DistanceRepository.INSTANCE, version, () -> exportDistances(scenarioModel))) {
			return version;
		}
		return null;
	}

	private static @Nullable String checkFleetDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final String version = fleetModel.getFleetDataVersion();
		if (genericVersionUpload(VesselsRepository.INSTANCE, version, () -> exportVessels(scenarioModel))) {
			return version;
		}
		return null;
	}

	private static @Nullable String checkPortDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final String version = portModel.getPortDataVersion();
		if (genericVersionUpload(PortsRepository.INSTANCE, version, () -> exportPort(scenarioModel))) {
			return version;
		}
		return null;
	}

	private static String exportPricing(final LNGScenarioModel scenarioModel) throws IOException {
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		final PricingVersion version = PricingFromScenarioCopier.generateVersion(pricingModel);
		return new ObjectMapper().writeValueAsString(version);
	}

	private static String exportDistances(final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final DistancesVersion version = DistancesFromScenarioCopier.generateVersion(portModel);
		return new ObjectMapper().writeValueAsString(version);
	}

	private static String exportPort(final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final PortsVersion version = PortFromScenarioCopier.generateVersion(portModel);
		return new ObjectMapper().writeValueAsString(version);
	}

	private static String exportVessels(final LNGScenarioModel scenarioModel) throws IOException {

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final VesselsVersion version = VesselsFromScenarioCopier.generateVersion(fleetModel);
		return new ObjectMapper().writeValueAsString(version);
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

	private static boolean genericVersionUpload(final IDataRepository repository, final String versionID, final CheckedSupplier<String, IOException> doLocalExport) throws IOException {
		final boolean hasUpstream = repository.hasUpstreamVersion(versionID);
		if (hasUpstream) {
			// Version already exists
			return true;
		} else {
			// Publish
			try {
				repository.publishUpstreamVersion(doLocalExport.get());
				return true;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

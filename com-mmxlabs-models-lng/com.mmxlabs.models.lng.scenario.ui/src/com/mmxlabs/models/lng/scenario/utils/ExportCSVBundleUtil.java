/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultExportContext;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * @author hinton
 * 
 */
public class ExportCSVBundleUtil {

	private ExportCSVBundleUtil() {

	}

	/**
	 * Export CSV to zip file bundle.
	 * 
	 * 
	 * @param scenarioDataProvider
	 * @param targetFile
	 */
	public static void exportScenarioToZip(final IScenarioDataProvider scenarioDataProvider, final File targetFile) {

		final char delimiter = ',';
		final char decimalSeparator = '.';
		final String baseURL = "archive:" + targetFile.toURI().toString() + "!/";

		exportScenario(scenarioDataProvider, delimiter, decimalSeparator, baseURL);
	}

	public static void exportScenario(final IScenarioDataProvider scenarioDataProvider, final char delimiter, final char decimalSeparator, final String baseURL) {

		final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		final IMMXExportContext context = new DefaultExportContext(scenarioModel, scenarioDataProvider, decimalSeparator);

		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

		// generate export files
		for (final UUIDObject modelInstance : getSubModels(scenarioDataProvider)) {
			final ISubmodelImporter importer = Activator.getDefault().getImporterRegistry().getSubmodelImporter(modelInstance.eClass());
			if (importer != null) {
				final Map<String, Collection<Map<String, String>>> outputs = new HashMap<>();

				importer.exportModel(modelInstance, outputs, context);

				for (final Map.Entry<String, Collection<Map<String, String>>> e : outputs.entrySet()) {
					final String key = e.getKey();
					final Collection<Map<String, String>> rows = e.getValue();
					if (rows != null && !rows.isEmpty()) {
						final String friendlyName = importer.getRequiredInputs().get(key);

						// export CSV for this file
						final URI uri = URI.createURI(baseURL + friendlyName + ".csv");
						try (OutputStreamWriter writer = new OutputStreamWriter(uc.createOutputStream(uri))) {
							writeCSV(rows, writer, delimiter);
						} catch (final IOException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
					}
				}
			}
		}

		final Collection<IExtraModelImporter> extra = Activator.getDefault().getImporterRegistry().getExtraModelImporters();
		for (final IExtraModelImporter importer : extra) {
			final Map<String, Collection<Map<String, String>>> outputs = new HashMap<>();
			importer.exportModel(outputs, context);
			for (final Map.Entry<String, Collection<Map<String, String>>> e : outputs.entrySet()) {
				final String key = e.getKey();
				final Collection<Map<String, String>> rows = e.getValue();
				if (rows != null && !rows.isEmpty()) {
					final String friendlyName = importer.getRequiredInputs().get(key);

					final URI uri = URI.createURI(baseURL + friendlyName + ".csv");
					try (OutputStreamWriter writer = new OutputStreamWriter(uc.createOutputStream(uri))) {
						writeCSV(rows, writer, delimiter);
					} catch (final IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
			}
		}
	}

	private static List<UUIDObject> getSubModels(final @NonNull IScenarioDataProvider scenarioDataProvider) {
		final List<UUIDObject> subModels = new ArrayList<>();

		subModels.add(ScenarioModelUtil.getPortModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getFleetModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getPricingModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getCostModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getCommercialModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider));
		subModels.add(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		subModels.add(ScenarioModelUtil.getCargoModel(scenarioDataProvider));
		// subModels.add(scenarioModel.getActualsModel());
		subModels.add(ScenarioModelUtil.getScheduleModel(scenarioDataProvider));

		subModels.add(ScenarioModelUtil.findNominationsModel(scenarioDataProvider));

		// Remove any null references
		while (subModels.remove(null))
			;
		return subModels;
	}

	/**
	 * @param rows
	 * @param outputFile
	 */
	private static void writeCSV(final Collection<Map<String, String>> rows, final OutputStreamWriter os, final char delimiter) {
		if (rows.isEmpty()) {
			return;
		}
		final LinkedHashSet<String> keys = new LinkedHashSet<>();
		for (final Map<String, String> row : rows) {
			keys.addAll(row.keySet());
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(os);
			boolean firstKey = true;
			for (final String key : keys) {
				if (!firstKey)
					writer.write(delimiter);
				firstKey = false;
				writer.write(escape(key, delimiter));
			}
			for (final Map<String, String> row : rows) {
				writer.write("\n");
				firstKey = true;
				for (final String key : keys) {
					if (!firstKey)
						writer.write(delimiter);
					firstKey = false;
					writer.write(escape(row.get(key), delimiter));
				}
			}
		} catch (final IOException e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	private static String escape(final String key, final char delimiter) {
		if (key == null)
			return "";
		String sub = key.trim().replace("\"", "\\\"");
		sub = sub.contains("" + delimiter) ? "\"" + sub + "\"" : sub;
		// Ensure string is quoted if there is a new line present.
		if (sub.contains("\n")) {
			sub = "\"" + sub + "\"";
		}
		return sub;
	}

}

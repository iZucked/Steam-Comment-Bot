package com.mmxlabs.models.lng.scenario.ui.importers;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.joda.time.LocalDate;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class PromptPeriodImporter implements IExtraModelImporter {

	public static final String PROMPT_PERIOD_KEY = "PROMPT_PERIOD";

	public static final String PROMPT_PERIOD_START_KEY = "periodstart";
	public static final String PROMPT_PERIOD_END_KEY = "periodend";

	private final LocalDateAttributeImporter dateImporter = new LocalDateAttributeImporter();

	@NonNull
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	static {
		inputs.put(PROMPT_PERIOD_KEY, "Prompt Period");
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
			if (portfolioModel != null) {

				// Always set a default period. Then overwrite with values from the CSV file.
				portfolioModel.setPromptPeriodStart(new LocalDate());
				portfolioModel.setPromptPeriodEnd(new LocalDate().plusDays(90));
				
				final CSVReader reader = inputs.get(PROMPT_PERIOD_KEY);
				if (reader != null) {

					try {
						context.pushReader(reader);

						IFieldMap row = null;
						try {
							row = reader.readRow(true);
						} catch (final IOException e1) {
							context.createProblem("Error parsing row", true, true, true);
							return;
						}
						if (row != null) {
							{
								final String periodStart = row.get(PROMPT_PERIOD_START_KEY);
								if (periodStart != null && !periodStart.isEmpty()) {
									try {
										final LocalDate periodStartDate = dateImporter.parseLocalDate(periodStart);
										if (periodStartDate != null) {
											portfolioModel.setPromptPeriodStart(periodStartDate);
										}
									} catch (final Exception e) {
										context.createProblem("Error parsing date", true, true, true);
									}
								}
							}
							{
								final String periodEnd = row.get(PROMPT_PERIOD_END_KEY);
								if (periodEnd != null && !periodEnd.isEmpty()) {
									try {
										final LocalDate periodEndDate = dateImporter.parseLocalDate(periodEnd);
										if (periodEndDate != null) {
											portfolioModel.setPromptPeriodEnd(periodEndDate);
										}
									} catch (final Exception e) {
										context.createProblem("Error parsing date", true, true, true);
									}
								}
							}
						}
					} finally {
						try {
							reader.close();
						} catch (final IOException e) {
						}
						context.popReader();
					}
				}
			}
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
			if (portfolioModel != null) {
				final Map<String, String> row = new LinkedHashMap<>();
				row.put(PROMPT_PERIOD_START_KEY, dateImporter.formatLocalDate(portfolioModel.getPromptPeriodStart()));
				row.put(PROMPT_PERIOD_END_KEY, dateImporter.formatLocalDate(portfolioModel.getPromptPeriodEnd()));

				output.put(PROMPT_PERIOD_KEY, Collections.singleton(row));
			}
		}
	}
}

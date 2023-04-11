/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.pricesensitivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.AbstractSensitivityResult;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.CargoPnLResult;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PortfolioSensitivityResult;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.analytics.SensitivitySolutionSet;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator.BreakEvenMode;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.ExistingBaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerPriceCurveSetRunner;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxJob;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManager;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerContainer;
import com.mmxlabs.scheduler.optimiser.providers.IPriceExpressionProvider;
import com.mmxlabs.scheduler.optimiser.providers.PriceCurveKey;

@NonNullByDefault
public class PriceSensitivityJobRunner extends AbstractJobRunner {

	public static final String JOB_TYPE = "sensitivity";
	public static final String JOB_DISPLAY_NAME = "Price Sensitivity";

	@Nullable
	private PriceSensitivitySettings priceSensitivitySettings;
	@Nullable
	private Object loggingData;

	public void withParams(PriceSensitivitySettings settings) {
		priceSensitivitySettings = settings;
	}

	@Override
	public void withParams(@NonNull String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		priceSensitivitySettings = mapper.readValue(json, PriceSensitivitySettings.class);

	}

	@Override
	public @Nullable AbstractSolutionSet run(int threadsAvailable, @NonNull IProgressMonitor monitor) {
		final PriceSensitivitySettings pPriceSensitivitySettings = priceSensitivitySettings;
		if (pPriceSensitivitySettings == null) {
			throw new IllegalStateException("Price sensitivity parameters have not been set.");
		}
		final IScenarioDataProvider pSDP = sdp;
		if (pSDP == null) {
			throw new IllegalStateException("Scenario has not been set.");
		}

		if (enableLogging) {

		}

		return doJobRun(pSDP, pPriceSensitivitySettings, loggingData, threadsAvailable, SubMonitor.convert(monitor));
	}

	@Override
	public void saveLogs(@NonNull File file) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(new FileOutputStream(file))) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public void saveLogs(@NonNull OutputStream os) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(os)) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public @NonNull String saveLogsAsString() throws IOException {
		if (enableLogging && loggingData != null) {

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggingData);

		}
		throw new IllegalStateException("Logging not configured");
	}

	@Nullable
	public AbstractSolutionSet doJobRun(final IScenarioDataProvider sdp, final PriceSensitivitySettings priceSensitivitySettings, @Nullable final Object loggingData, final int numThreads,
			final IProgressMonitor monitor) {
		final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);
		final UserSettings userSettings = priceSensitivitySettings.getUserSettings();

		final SensitivityModel sensitivityModel = lngScenarioModel.getSensitivityModel();
		if (sensitivityModel == null) {
			if (priceSensitivitySettings.getSensitivityUUID() != null) {
				throw new IllegalArgumentException("Missing sensitivity model " + priceSensitivitySettings.getSensitivityUUID());
			}
			throw new IllegalArgumentException("Missing sensitivity model");
		}

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		return createPriceSensitivityFunction(sdp, null, userSettings, lngScenarioModel.getSensitivityModel().getSensitivityModel()).apply(subMonitor);
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createPriceSensitivityFunction(final IScenarioDataProvider sdp, @Nullable final ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model) {
		final SensitivitySolutionSet sensitivityResult = AnalyticsFactory.eINSTANCE.createSensitivitySolutionSet();
		sensitivityResult.setUseScenarioBase(true);
		return createPriceSetFunction(sdp, scenarioInstance, userSettings, model, sensitivityResult, (mapper, baseScheduleSpecification) -> {
			model.getCommodityCurves().stream() //
					.filter(CommodityCurveOverlay.class::isInstance) //
					.map(CommodityCurveOverlay.class::cast) //
					.forEach(mapper::addMapping);
			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();
			final LNGSchedulerPriceCurveSetRunner priceSensitivityRunner = new LNGSchedulerPriceCurveSetRunner(scenarioInstance, model, sdp, sdp.getEditingDomain(), userSettings, extraDataProvider, (mem, data, injector) -> {
				final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
				return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
			}, new LinkedList<>());
			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return priceSensitivityRunner.getBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					return priceSensitivityRunner.run();
				}
			};
		});
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createPriceSetFunction(final IScenarioDataProvider sdp, @Nullable final ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model, final SensitivitySolutionSet sensitivityResult, final BiFunction<IMapperClass, ScheduleSpecification, SandboxJob> jobAction) {
		return monitor -> {
			final boolean dualPNLModel = false;

			final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);
			final IMapperClass mapper = new Mapper(lngScenarioModel, false);
			final ScheduleSpecification baseScheduleSpecification = createBaseScheduleSpecification(sdp, model, mapper);

			final SandboxJob sandboxJob = jobAction.apply(mapper, baseScheduleSpecification);
			final IMultiStateResult results = sandboxJob.run(monitor);

			if (results == null) {
				sensitivityResult.setName("SensitivityResult");
				sensitivityResult.setHasDualModeSolutions(dualPNLModel);
				sensitivityResult.setUserSettings(EMFCopier.copy(userSettings));
				return sensitivityResult;
			}

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = sandboxJob.getScenarioRunner();
			final JobExecutorFactory jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService();

			final SolutionSetExporterUnit.Util<SolutionOption> exporter = new SolutionSetExporterUnit.Util<>(scenarioToOptimiserBridge, userSettings, AnalyticsFactory.eINSTANCE::createSolutionOption,
					dualPNLModel, true);
			exporter.setBreakEvenMode(model.isUseTargetPNL() ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT);
			sensitivityResult.setBaseOption(exporter.useAsBaseSolution(baseScheduleSpecification));

			final Map<Cargo, List<Long>> cargoPnls = new HashMap<>();
			final List<Long> portfolioPnls = new ArrayList<>();

			try (JobExecutor jobExecutor = jobExecutorFactory.begin()) {
				final List<@Nullable Future<?>> jobs = new LinkedList<>();
				if (results != null) {
					final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
					for (final NonNullPair<ISequences, Map<String, Object>> p : solutions) {
						jobs.add(jobExecutor.submit(() -> {
							final IPriceExpressionProvider priceExpressionProvider = scenarioToOptimiserBridge.getInjector().getInstance(IPriceExpressionProvider.class);
							final ILazyExpressionManagerContainer lazyExpressionManagerContainer = scenarioToOptimiserBridge.getInjector().getInstance(ILazyExpressionManagerContainer.class);
							final ISequences seq = p.getFirst();
							final SolutionOption resultSet;
							try (ILazyExpressionManager lazyExpressionManager = lazyExpressionManagerContainer.getExpressionManager()) {
								final List<PriceCurveKey> priceCurveCombination = (List<PriceCurveKey>) p.getSecond().get("combination");
								for (final PriceCurveKey key : priceCurveCombination) {
									lazyExpressionManager.setPriceCurve(key.getIndexName().toLowerCase(), key.seriesType(), priceExpressionProvider.getExpression(key));
								}
								lazyExpressionManager.initialiseAllPricingData();
								resultSet = exporter.computeOption(seq);
							}
							final long thisPortfolioPnL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(resultSet.getScheduleModel().getSchedule());
							synchronized (portfolioPnls) {
								portfolioPnls.add(thisPortfolioPnL);
							}
							for (final CargoAllocation cargoAllocation : resultSet.getScheduleModel().getSchedule().getCargoAllocations()) {
								final Cargo cargo = cargoAllocation.getSlotAllocations().get(0).getSlot().getCargo();
								final long thisPnl = ScheduleModelKPIUtils.getElementPNL(cargoAllocation);
								final List<Long> thisCargoPnls;
								synchronized (cargoPnls) {
									thisCargoPnls = cargoPnls.computeIfAbsent(cargo, c -> new ArrayList<>());
								}
								synchronized (thisCargoPnls) {
									thisCargoPnls.add(thisPnl);
								}
							}

							return null;
						}));
					}
					jobs.forEach(f -> {
						try {
							f.get();
						} catch (final Exception e) {

						}
					});
					populateSensitivityResult(sensitivityResult, portfolioPnls, cargoPnls);
				}
			}
			sensitivityResult.setName("Sensitivity Result");
			sensitivityResult.setUserSettings(EMFCopier.copy(userSettings));
			return sensitivityResult;
		};
	}

	private void populateSensitivityResult(final SensitivitySolutionSet sensitivityResult, final List<Long> portfolioPnls, final Map<Cargo, List<Long>> cargoPnls) {
		final PortfolioSensitivityResult portfolioSensitivityResult = AnalyticsFactory.eINSTANCE.createPortfolioSensitivityResult();
		populatePnLResult(portfolioSensitivityResult, portfolioPnls);
		sensitivityResult.setPorfolioPnLResult(portfolioSensitivityResult);

		for (final Entry<Cargo, List<Long>> entry : cargoPnls.entrySet()) {
			final CargoPnLResult pnlResult = AnalyticsFactory.eINSTANCE.createCargoPnLResult();
			pnlResult.setCargo(entry.getKey());
			populatePnLResult(pnlResult, entry.getValue());
			sensitivityResult.getCargoPnLResults().add(pnlResult);
		}
	}

	private void populatePnLResult(final AbstractSensitivityResult pnlResult, final List<Long> values) {
		pnlResult.setMinPnL(values.stream().mapToLong(Long::longValue).min().getAsLong());
		pnlResult.setMaxPnL(values.stream().mapToLong(Long::longValue).max().getAsLong());
		pnlResult.setAveragePnL(values.stream().mapToLong(Long::longValue).sum() / values.size());
		pnlResult.setVariance(calculateStandardDeviation(values, pnlResult.getAveragePnL()));
	}

	private double calculateStandardDeviation(final List<Long> values, final long average) {
		if (values.size() <= 1) {
			return 0.0;
		} else {
			return values.stream() //
					.mapToLong(p -> p.longValue() - average) //
					.map(p -> p * p) //
					.mapToDouble(Math::sqrt) //
					.sum() / (values.size() - 1);
		}
	}

	private ScheduleSpecification createBaseScheduleSpecification(final IScenarioDataProvider scenarioDataProvider, final OptionAnalysisModel model, final IMapperClass mapper) {
		ScheduleSpecification baseScheduleSpecification;

		if (model.getBaseCase().isKeepExistingScenario()) {
			final ExistingBaseCaseToScheduleSpecification builder = new ExistingBaseCaseToScheduleSpecification(scenarioDataProvider, mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase(), false);
		} else {

			final BaseCaseToScheduleSpecification builder = new BaseCaseToScheduleSpecification(scenarioDataProvider.getTypedScenario(LNGScenarioModel.class), mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase(), false);
		}
		return baseScheduleSpecification;
	}
}
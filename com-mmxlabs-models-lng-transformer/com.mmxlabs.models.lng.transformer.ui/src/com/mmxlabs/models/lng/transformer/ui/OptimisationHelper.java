/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ReduceSequencesStage;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.StrategicLocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper.NameProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenIdleTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.MinMaxSlotGroupConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.VesselUtilisationFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

public final class OptimisationHelper {
	private static final Logger LOG = LoggerFactory.getLogger(OptimisationHelper.class);

	private OptimisationHelper() {
	}

	public static final String PARAMETER_MODE_CUSTOM = "Custom";

	public static final int EPOCH_LENGTH_PERIOD = 10_000;
	public static final int EPOCH_LENGTH_FULL = 10_000;

	@Nullable
	public static OptimisationPlan getOptimiserSettings(@NonNull final LNGScenarioModel scenario, final boolean forEvaluation, @Nullable final String parameterMode, final boolean promptUser,
			final boolean promptOnlyIfOptionsEnabled, final NameProvider nameProvider) {

		UserSettings previousSettings = null;
		if (scenario != null) {
			previousSettings = scenario.getUserSettings();
		}

		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		if (previousSettings == null) {
			previousSettings = userSettings;
		}

		// Only permit LT mode for an LT scenario
		if (scenario != null && scenario.isLongTerm()) {
			userSettings.setMode(OptimisationMode.LONG_TERM);
			previousSettings.setMode(OptimisationMode.LONG_TERM);
		}

		// Permit the user to override the settings object. Use the previous settings as
		// the initial value
		if (promptUser) {
			final boolean forADP = scenario.getAdpModel() != null;

			// Do not allow optimisation if break even present in slots.
			if (!forEvaluation && UserSettingsHelper.checkForBreakEven(scenario)) {
				final String errMessage = "Optimisation does not support break-evens. Replace \"?\" in price expressions.";
				final Display display = PlatformUI.getWorkbench().getDisplay();
				if (display != null) {
					display.syncExec(() -> MessageDialog.openError(display.getActiveShell(), "Unable to start optimisation", errMessage));
				}

				return null;
			}

			previousSettings = UserSettingsHelper.openUserDialog(scenario, forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled, nameProvider, forADP);
		}

		if (previousSettings == null) {
			return null;
		}

		// Only merge across specific fields - not all of them. This permits additions
		// to the default settings to pass through to the scenario.
		UserSettingsHelper.mergeFields(previousSettings, userSettings);

		if (!UserSettingsHelper.checkUserSettings(userSettings, false)) {
			return null;
		}

		final OptimisationPlan optimisationPlan = transformUserSettings(userSettings, parameterMode, scenario);
		if (nameProvider != null) {
			optimisationPlan.setResultName(nameProvider.getNameSuggestion());
		}

		return optimisationPlan;
	}

	public static Object evaluateScenarioInstance(@NonNull final IEclipseJobManager jobManager, @NonNull final ScenarioInstance instance, @Nullable final String parameterMode,
			final boolean promptForOptimiserSettings, final boolean optimising, final boolean promptOnlyIfOptionsEnabled, final String nameSuggestion, final Set<String> existingNames) {
		return evaluateScenarioInstance(jobManager, instance, parameterMode, promptForOptimiserSettings, optimising, promptOnlyIfOptionsEnabled, new NameProvider(nameSuggestion, existingNames));
	}

	public static Object evaluateScenarioInstance(@NonNull final IEclipseJobManager jobManager, @NonNull final ScenarioInstance instance, @Nullable final String parameterMode,
			final boolean promptForOptimiserSettings, final boolean optimising, final boolean promptOnlyIfOptionsEnabled, final NameProvider nameProvider) {

		final IScenarioService service = SSDataManager.Instance.findScenarioService(instance);
		if (service == null) {
			return null;
		}
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);

		final OptimisationPlan[] planRef = new OptimisationPlan[1];
		final BiPredicate<IScenarioDataProvider, LNGScenarioModel> prepareCallback = (ref, root) -> {
			final OptimisationPlan optimisationPlan = getOptimiserSettings(root, !optimising, parameterMode, promptForOptimiserSettings, promptOnlyIfOptionsEnabled, nameProvider);

			if (optimisationPlan == null) {
				return false;
			}
			final EditingDomain editingDomain = ref.getEditingDomain();
			if (editingDomain != null) {
				final CompoundCommand cmd = new CompoundCommand("Update settings");
				cmd.append(SetCommand.create(editingDomain, root, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), EMFCopier.copy(optimisationPlan.getUserSettings())));
				RunnerHelper.syncExecDisplayOptional(() -> editingDomain.getCommandStack().execute(cmd));
			}

			planRef[0] = optimisationPlan;
			return true;
		};

		final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
			return new LNGSchedulerJobDescriptor(instance.getName(), instance, planRef[0], optimising);
		};
		final String taskName = "Optimise " + instance.getName();

		final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
		jobRunner.run(taskName, instance, modelRecord, prepareCallback, createJobDescriptorCallback, null);

		return null;
	}

	public static @NonNull OptimisationPlan transformUserSettings(@NonNull final UserSettings userSettings, @Nullable final String parameterMode, final LNGScenarioModel lngScenarioModel) {

		final OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();

		plan.setUserSettings(userSettings);

		plan.setSolutionBuilderSettings(ScenarioUtils.createDefaultSolutionBuilderSettings());

		@NonNull
		final ConstraintAndFitnessSettings baseConstraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		Objective similarityObjective = findObjective(SimilarityFitnessCoreFactory.NAME, baseConstraintAndFitnessSettings);
		if (similarityObjective == null) {
			similarityObjective = ParametersFactory.eINSTANCE.createObjective();
			similarityObjective.setName(SimilarityFitnessCoreFactory.NAME);
			baseConstraintAndFitnessSettings.getObjectives().add(similarityObjective);
		}
		similarityObjective.setEnabled(true);
		similarityObjective.setWeight(1.0);

		final @Nullable LocalDate periodStart = userSettings.getPeriodStartDate();
		final @Nullable YearMonth periodEnd = userSettings.getPeriodEnd();

		final LocalDate periodStartOrDefault = getPeriodStartOrDefault(periodStart, lngScenarioModel);
		final YearMonth periodEndOrDefault = getPeriodEndOrDefault(periodEnd, lngScenarioModel);

		int epochLength;
		if (userSettings.isSetPeriodStartDate() && userSettings.isSetPeriodEnd()) {
			epochLength = EPOCH_LENGTH_PERIOD;
		} else {
			epochLength = EPOCH_LENGTH_FULL;
		}

		final SimilarityMode similarityMode = userSettings.getSimilarityMode();

		boolean shouldUseRestartingLSO = false;
		if (userSettings.getMode() == OptimisationMode.ADP) { // ADP Optimisation
			baseConstraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.OFF, periodStartOrDefault, periodEndOrDefault));
			shouldUseRestartingLSO = false;
		} else if (userSettings.getMode() == OptimisationMode.STRATEGIC) { // Strategic optimisation
			baseConstraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.OFF, periodStartOrDefault, periodEndOrDefault));
			shouldUseRestartingLSO = false;
		} else { // Normal optimisation.
			switch (similarityMode) {
			case ALL:
				baseConstraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.LOW, periodStartOrDefault, periodEndOrDefault));
				shouldUseRestartingLSO = true;
				break;
			case OFF:
				baseConstraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.OFF, periodStartOrDefault, periodEndOrDefault));
				shouldUseRestartingLSO = false;
				break;
			default:
				assert false;
				break;
			}
		}

		// Disable nominal vessel rules (well constraints here...) in ADP
		if (userSettings.getMode() == OptimisationMode.ADP) {
			ScenarioUtils.removeAllConstraints(plan, RoundTripVesselPermissionConstraintCheckerFactory.NAME);
			ScenarioUtils.removeConstraints(RoundTripVesselPermissionConstraintCheckerFactory.NAME, baseConstraintAndFitnessSettings);
		}

		if (userSettings.isCleanSlateOptimisation() || userSettings.getMode() == OptimisationMode.STRATEGIC) {
			shouldUseRestartingLSO = false;
			baseConstraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.OFF, periodStartOrDefault, periodEndOrDefault));

			final CleanStateOptimisationStage stage = ScenarioUtils.createDefaultCleanStateParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));
			stage.getAnnealingSettings().setEpochLength(epochLength);
			plan.getStages().add(stage);

			if (!userSettings.isNominalOnly()) {
				plan.getStages().add(ParametersFactory.eINSTANCE.createResetInitialSequencesStage());
			} else {
				// Return here for Nominal only modes
				if (userSettings.getMode() == OptimisationMode.ADP) {
					ScenarioUtils.createOrUpdateAllConstraints(plan, MinMaxSlotGroupConstraintCheckerFactory.NAME, true);
					ScenarioUtils.createOrUpdateAllConstraints(plan, LadenIdleTimeConstraintCheckerFactory.NAME, true);
					ScenarioUtils.createOrUpdateAllObjectives(plan, VesselUtilisationFitnessCoreFactory.NAME, true, 1);
					ScenarioUtils.createOrUpdateAllObjectives(plan, NonOptionalSlotFitnessCoreFactory.NAME, true, 24_000_000);

				}
				return LNGScenarioRunnerUtils.createExtendedSettings(plan);
			}
		}
		if (userSettings.getMode() == OptimisationMode.STRATEGIC) { // Strategic optimiser
			{
				final int jobCount = 10;// 30
				final StrategicLocalSearchOptimisationStage stage = ScenarioUtils.createDefaultStrategicLSOParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));

				stage.getAnnealingSettings().setInitialTemperature(5_000_000);

				stage.getAnnealingSettings().setEpochLength(1_000);
				stage.getAnnealingSettings().setIterations(400_000);
				stage.getAnnealingSettings().setRestarting(false);
				stage.setCount(jobCount);
				
				plan.getStages().add(stage);
			}
			{
				final ReduceSequencesStage stage = ParametersFactory.eINSTANCE.createReduceSequencesStage();
				stage.setName("reduce");
				plan.getStages().add(stage);
			}
			{
				final LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));
				stage.getAnnealingSettings().setEpochLength(epochLength);
				stage.getAnnealingSettings().setRestarting(shouldUseRestartingLSO);
				plan.getStages().add(stage);
			}
			// Follow by hill-climb stage
			{
				final HillClimbOptimisationStage stage = ScenarioUtils.createDefaultHillClimbingParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));
				stage.getAnnealingSettings().setEpochLength(epochLength);
				plan.getStages().add(stage);
			}
		} else if (userSettings.getMode() == OptimisationMode.ADP) { // ADP optimiser
			final LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));
			stage.getAnnealingSettings().setEpochLength(epochLength);
			stage.getAnnealingSettings().setRestarting(shouldUseRestartingLSO);
			plan.getStages().add(stage);

			// Add in ADP constraints and objectives
			ScenarioUtils.createOrUpdateAllConstraints(plan, MinMaxSlotGroupConstraintCheckerFactory.NAME, true);
			ScenarioUtils.createOrUpdateAllConstraints(plan, LadenIdleTimeConstraintCheckerFactory.NAME, true);
			ScenarioUtils.createOrUpdateAllObjectives(plan, VesselUtilisationFitnessCoreFactory.NAME, true, 1);
			ScenarioUtils.createOrUpdateAllObjectives(plan, NonOptionalSlotFitnessCoreFactory.NAME, true, 24_000_000);
		} else {
			if (similarityMode != SimilarityMode.OFF) {
				final MultipleSolutionSimilarityOptimisationStage stage = ScenarioUtils.createDefaultMultipleSolutionSimilarityParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));
				stage.getAnnealingSettings().setEpochLength(epochLength);
				stage.getAnnealingSettings().setRestarting(shouldUseRestartingLSO);

				final SimilaritySettings similaritySettings = stage.getConstraintAndFitnessSettings().getSimilaritySettings();
				if (similaritySettings != null) {
					stage.getConstraintAndFitnessSettings().setSimilaritySettings(ScenarioUtils.createUnweightedSimilaritySettings());
				}

				plan.getStages().add(stage);
			} else {
				// Normal LSO
				{
					final LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));
					stage.getAnnealingSettings().setEpochLength(epochLength);
					stage.getAnnealingSettings().setRestarting(shouldUseRestartingLSO);
					plan.getStages().add(stage);
				}
				// Follow by hill-climb stage
				{
					final HillClimbOptimisationStage stage = ScenarioUtils.createDefaultHillClimbingParameters(EMFCopier.copy(baseConstraintAndFitnessSettings));
					stage.getAnnealingSettings().setEpochLength(epochLength);
					plan.getStages().add(stage);
				}
			}
		}

		return LNGScenarioRunnerUtils.createExtendedSettings(plan);
	}

	private static Objective findObjective(final String objective, final ConstraintAndFitnessSettings settings) {
		for (final Objective o : settings.getObjectives()) {
			if (Objects.equals(objective, o.getName())) {
				return o;
			}
		}
		return null;
	}

	private static YearMonth getPeriodEndOrDefault(final YearMonth periodEnd, final LNGScenarioModel scenario) {
		if (periodEnd != null) {
			return periodEnd;
		} else if (scenario == null) {
			return periodEnd;
		} else {
			final List<LoadSlot> loadSlots = new LinkedList<>(scenario.getCargoModel().getLoadSlots());
			Collections.sort(loadSlots, (o1, o2) -> {
				return o1.getSchedulingTimeWindow().getEndWithFlex().compareTo(o2.getSchedulingTimeWindow().getEndWithFlex()) * -1;
			});
			if (loadSlots.isEmpty()) {
				return YearMonth.of(2000, 1);
			}
			return YearMonth.of(loadSlots.get(0).getSchedulingTimeWindow().getStart().getYear(), loadSlots.get(0).getSchedulingTimeWindow().getStart().getMonth());
		}
	}

	private static LocalDate getPeriodStartOrDefault(final LocalDate periodStart, final LNGScenarioModel scenario) {
		if (periodStart != null) {
			return periodStart;
		} else if (scenario == null) {
			return periodStart;
		} else {
			final List<LoadSlot> loadSlots = new LinkedList<>(scenario.getCargoModel().getLoadSlots());
			Collections.sort(loadSlots, (o1, o2) -> {
				return o1.getSchedulingTimeWindow().getStart().compareTo(o2.getSchedulingTimeWindow().getStart());
			});
			if (loadSlots.isEmpty()) {
				// FIXME: (SG) This is not always a good idea... I have seen arrays created to
				// hold data from 2000 to now - and this can take a long time.
				return LocalDate.of(2000, 1, 1);
			}
			return loadSlots.get(0).getSchedulingTimeWindow().getStart().toLocalDate();
		}
	}

	public static boolean validateScenario(final IScenarioDataProvider scenarioDataProvider, @Nullable EObject extraTarget, final boolean optimising, final boolean displayErrors,
			final boolean relaxedValidation, Set<String> extraCategories) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter((constraint, target) -> {
			for (final Category cat : constraint.getCategories()) {
				if (cat.getId().endsWith(".base")) {
					return true;
				} else if (optimising && cat.getId().endsWith(".optimisation")) {
					return true;
				} else if (!optimising && cat.getId().endsWith(".evaluation")) {
					return true;
				}
				// Any extra validation category suffixes to include e.g. for sandbox
				for (String catSuffix : extraCategories) {
					if (cat.getId().endsWith(catSuffix)) {
						return true;
					}
				}
			}

			return false;
		});

		final MMXRootObject rootObject = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation);
			return helper.runValidation(validator, extraContext, rootObject, extraTarget);
		});

		if (status == null) {
			return false;
		}

		if (!status.isOK()) {

			if (optimising || status.getSeverity() == IStatus.ERROR) {

				// See if this command was executed in the UI thread - if so fire up the dialog
				// box.
				if (displayErrors) {
					final boolean[] res = new boolean[1];
					Display.getDefault().syncExec(() -> {
						final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getDefault().getActiveShell(), status, status.getSeverity() != IStatus.ERROR);

						// Wait for use to press a button before continuing.
						dialog.setBlockOnOpen(true);

						if (dialog.open() == Window.CANCEL) {
							res[0] = false;
						} else {
							res[0] = true;
						}
					});
					if (!res[0]) {
						return false;
					}
				}
			}
		}
		if (status.getSeverity() == IStatus.ERROR) {
			return false;
		}

		return true;
	}

	public static SimilaritySettings createSimilaritySettings(final SimilarityMode mode, final LocalDate periodStart, final YearMonth periodEnd) {
		if (periodStart == null || periodEnd == null || mode == null || mode == SimilarityMode.OFF) {
			return ScenarioUtils.createOffSimilaritySettings();
		} else {
			return SimilarityUIParameters.getSimilaritySettings(mode, periodStart, periodEnd);
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.provider.ParametersItemProviderAdapterFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.DataSection;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.DataType;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.Option;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.OptionGroup;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.models.util.StringEscaper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;

public final class OptimisationHelper {

	public static final String PARAMETER_MODE_CUSTOM = "Custom";

	private static final Logger log = LoggerFactory.getLogger(OptimisationHelper.class);

	public static final int EPOCH_LENGTH_PERIOD = 10_000;
	public static final int EPOCH_LENGTH_FULL = 10_000;

	// Note: SWTBOT ids are linked to the display string for radio buttons
	public static final String SWTBOT_SHIPPING_ONLY_PREFIX = "swtbot.shippingonly";
	public static final String SWTBOT_SHIPPING_ONLY_ON = SWTBOT_SHIPPING_ONLY_PREFIX + ".On";
	public static final String SWTBOT_SHIPPING_ONLY_OFF = SWTBOT_SHIPPING_ONLY_PREFIX + ".Off";

	public static final String SWTBOT_WITH_SPOT_CARGO_MARKETS_PREFIX = "swtbot.withspotcargomarkets";
	public static final String SWTBOT_WITH_SPOT_CARGO_MARKETS_ON = SWTBOT_WITH_SPOT_CARGO_MARKETS_PREFIX + ".On";
	public static final String SWTBOT_WITH_SPOT_CARGO_MARKETS_OFF = SWTBOT_WITH_SPOT_CARGO_MARKETS_PREFIX + ".Off";

	public static final String SWTBOT_CLEAN_STATE_PREFIX = "swtbot.cleanstate";
	public static final String SWTBOT_CLEAN_STATE_ON = SWTBOT_CLEAN_STATE_PREFIX + ".On";
	public static final String SWTBOT_CLEAN_STATE_OFF = SWTBOT_CLEAN_STATE_PREFIX + ".Off";

	public static final String SWTBOT_ACTION_SET_PREFIX = "swtbot.actionset";
	public static final String SWTBOT_ACTION_SET_ON = SWTBOT_ACTION_SET_PREFIX + ".On";
	public static final String SWTBOT_ACTION_SET_OFF = SWTBOT_ACTION_SET_PREFIX + ".Off";

	public static final String SWTBOT_CHARTEROUTGENERATION_PREFIX = "swtbot.charteroutgeneration";
	public static final String SWTBOT_CHARTEROUTGENERATION_ON = SWTBOT_CHARTEROUTGENERATION_PREFIX + ".On";
	public static final String SWTBOT_CHARTEROUTGENERATION_OFF = SWTBOT_CHARTEROUTGENERATION_PREFIX + ".Off";

	public static final String SWTBOT_SIMILARITY_PREFIX = "swtbot.similaritymode";
	public static final String SWTBOT_SIMILARITY_PREFIX_OFF = SWTBOT_SIMILARITY_PREFIX + ".Off";
	public static final String SWTBOT_SIMILARITY_PREFIX_LOW = SWTBOT_SIMILARITY_PREFIX + ".Low";
	public static final String SWTBOT_SIMILARITY_PREFIX_MEDIUM = SWTBOT_SIMILARITY_PREFIX + ".Med";
	public static final String SWTBOT_SIMILARITY_PREFIX_HIGH = SWTBOT_SIMILARITY_PREFIX + ".High";

	public static final String SWTBOT_PERIOD_START = "swtbot.period.start";
	public static final String SWTBOT_PERIOD_END = "swtbot.period.end";

	public static final String SWTBOT_IDLE_DAYS = "swtbot.idledays";

	public static Object evaluateScenarioInstance(@NonNull final IEclipseJobManager jobManager, @NonNull final ScenarioInstance instance, @Nullable final String parameterMode,
			final boolean promptForOptimiserSettings, final boolean optimising, final String lockName, final boolean promptOnlyIfOptionsEnabled) {

		final IScenarioService service = instance.getScenarioService();
		if (service == null) {
			return null;
		}

		// While we only keep the reference for the duration of this method call, the two current concrete implementations of IJobControl will obtain a ModelReference
		try (final ModelReference modelRefence = instance.getReference("OptimisationHelper")) {
			final EObject object = modelRefence.getInstance();

			if (object instanceof LNGScenarioModel) {
				final LNGScenarioModel root = (LNGScenarioModel) object;

				final String uuid = instance.getUuid();
				// Check for existing job and return if there is one.
				{
					final IJobDescriptor job = jobManager.findJobForResource(uuid);
					if (job != null) {
						return null;
					}
				}
				final OptimisationPlan optimisationPlan = getOptimiserSettings(root, !optimising, parameterMode, promptForOptimiserSettings, promptOnlyIfOptionsEnabled);

				if (optimisationPlan == null) {
					return null;
				}
				final EditingDomain editingDomain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
				if (editingDomain != null) {
					final CompoundCommand cmd = new CompoundCommand("Update settings");
					cmd.append(SetCommand.create(editingDomain, root, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), EcoreUtil.copy(optimisationPlan.getUserSettings())));
					editingDomain.getCommandStack().execute(cmd);
				}
				// Pair<UserSettings, OptimiserSettings>

				final ScenarioLock scenarioLock = instance.getLock(lockName);
				if (scenarioLock.awaitClaim()) {
					IJobControl control = null;
					IJobDescriptor job = null;
					try {
						// create a new job
						job = new LNGSchedulerJobDescriptor(instance.getName(), instance, optimisationPlan, optimising);

						// New optimisation, so check there are no validation errors.
						if (!validateScenario(root, optimising)) {
							scenarioLock.release();
							return null;
						}

						// Automatically clean up job when removed from manager
						jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
						control = jobManager.submitJob(job, uuid);
						// Add listener to clean up job when it finishes or has an exception.
						final IJobDescriptor finalJob = job;
						control.addListener(new IJobControlListener() {

							@Override
							public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {

								if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
									scenarioLock.release();
									jobManager.removeJob(finalJob);
									return false;
								}
								return true;
							}

							@Override
							public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
								return true;
							}
						});
						// Start the job!
						control.prepare();
						control.start();
					} catch (final Throwable ex) {
						log.error(ex.getMessage(), ex);
						if (control != null) {
							control.cancel();
						}
						// Manual clean up incase the control listener doesn't fire
						if (job != null) {
							jobManager.removeJob(job);
						}
						// instance.setLocked(false);
						scenarioLock.release();

						final Display display = Display.getDefault();
						if (display != null) {
							display.asyncExec(() -> {
								final String message = StringEscaper.escapeUIString(ex.getMessage());
								MessageDialog.openError(display.getActiveShell(), "Error starting optimisation", "An error occured. See Error Log for more details.\n" + message);
							});
						}
					}
				}
			}
		}

		return null;
	}

	public static boolean validateScenario(final MMXRootObject root, final boolean optimising) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().endsWith(".base")) {
						return true;
					} else if (optimising && cat.getId().endsWith(".optimisation")) {
						return true;
					} else if (!optimising && cat.getId().endsWith(".evaluation")) {
						return true;
					}
				}

				return false;
			}
		});

		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(root, false);
			return helper.runValidation(validator, extraContext, Collections.singleton(root));
		});

		if (status == null) {
			return false;
		}

		if (status.isOK() == false) {

			// See if this command was executed in the UI thread - if so fire up the dialog box.
			if (Display.getCurrent() != null) {

				final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getCurrent().getActiveShell(), status, status.getSeverity() != IStatus.ERROR);

				// Wait for use to press a button before continuing.
				dialog.setBlockOnOpen(true);

				if (dialog.open() == Window.CANCEL) {
					return false;
				}
			}
		}

		if (status.getSeverity() == IStatus.ERROR) {
			return false;
		}

		return true;
	}

	public static UserSettings promptForUserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final boolean promptUser, final boolean promptOnlyIfOptionsEnabled) {
		UserSettings previousSettings = null;
		if (scenario != null) {
			previousSettings = scenario.getUserSettings();
		}

		final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
		if (previousSettings == null) {
			previousSettings = userSettings;
		}

		// Permit the user to override the settings object. Use the previous settings as the initial value
		if (promptUser) {
			previousSettings = openUserDialog(scenario, forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled);
		}

		if (previousSettings == null) {
			return null;
		}

		// Only merge across specific fields - not all of them. This permits additions to the default settings to pass through to the scenario.
		mergeFields(previousSettings, userSettings);

		if (!checkUserSettings(userSettings, false)) {
			return null;
		}
		return userSettings;
	}

	@Nullable
	public static OptimisationPlan getOptimiserSettings(@NonNull final LNGScenarioModel scenario, final boolean forEvaluation, @Nullable final String parameterMode, final boolean promptUser,
			final boolean promptOnlyIfOptionsEnabled) {

		UserSettings previousSettings = null;
		if (scenario != null) {
			previousSettings = scenario.getUserSettings();
		}

		final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
		if (previousSettings == null) {
			previousSettings = userSettings;
		}

		// Permit the user to override the settings object. Use the previous settings as the initial value
		if (promptUser) {
			previousSettings = openUserDialog(scenario, forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled);
		}

		if (previousSettings == null) {
			return null;
		}

		// Only merge across specific fields - not all of them. This permits additions to the default settings to pass through to the scenario.
		mergeFields(previousSettings, userSettings);

		if (!checkUserSettings(userSettings, false)) {
			return null;
		}

		final OptimisationPlan optimisationPlan = transformUserSettings(userSettings, parameterMode, scenario);

		return optimisationPlan;
	}

	public static UserSettings openUserDialog(final LNGScenarioModel scenario, final boolean forEvaluation, final UserSettings previousSettings, final UserSettings defaultSettings,
			final boolean displayOnlyIfOptionsEnabled) {
		return openUserDialog(scenario, PlatformUI.getWorkbench().getDisplay(), PlatformUI.getWorkbench().getDisplay().getActiveShell(), forEvaluation, previousSettings, defaultSettings,
				displayOnlyIfOptionsEnabled);
	}

	public static UserSettings openUserDialog(final LNGScenarioModel scenario, final Display display, final Shell shell, final boolean forEvaluation, final UserSettings previousSettings,
			final UserSettings defaultSettings, final boolean displayOnlyIfOptionsEnabled) {
		boolean optionAdded = false;
		boolean enabledOptionAdded = false;

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ParametersItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		//
		// Fire up a dialog
		final ParameterModesDialog dialog = new ParameterModesDialog(shell) {
			protected void configureShell(final org.eclipse.swt.widgets.Shell newShell) {

				super.configureShell(newShell);
				newShell.setText(forEvaluation ? "Evaluation Settings" : "Optimisation Settings");
			}
		};

		// final OptimiserSettings copy = EcoreUtil.copy(previousSettings);
		final UserSettings copy = EcoreUtil.copy(previousSettings);

		// Reset disabled features
		resetDisabledFeatures(copy);

		if (!forEvaluation) {
			// dialog.addOption(DataSection.Controls, null, editingDomain, "Number of Iterations", copy, defaultSettings, DataType.PositiveInt,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings(), ParametersPackage.eINSTANCE.getAnnealingSettings_Iterations());
			// optionAdded = true;

			// Check period optimisation is permitted
			// if (SecurityUtils.getSubject().isPermitted("features:optimisation-period")) {
			{
				final OptionGroup group = dialog.createGroup(DataSection.Controls, "Optimise period");
				final Option optStart = dialog.addOption(DataSection.Controls, group, editingDomain, "Start of (mm/yyyy)", copy, defaultSettings, DataType.MonthYear, SWTBOT_PERIOD_START,
						ParametersPackage.eINSTANCE.getUserSettings_PeriodStart());
				final Option optEnd = dialog.addOption(DataSection.Controls, group, editingDomain, "Up to start of (mm/yyyy)", copy, defaultSettings, DataType.MonthYear, SWTBOT_PERIOD_END,
						ParametersPackage.eINSTANCE.getUserSettings_PeriodEnd());
				if (!LicenseFeatures.isPermitted("features:optimisation-period")) {
					optStart.enabled = false;
					optEnd.enabled = false;
				} else {
					enabledOptionAdded = true;
				}
				optionAdded = true;
			}

			if (LicenseFeatures.isPermitted("features:optimisation-clean-state")) {
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				final Option option = dialog.addOption(DataSection.Toggles, null, editingDomain, "Clean State: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_CLEAN_STATE_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_CleanStateOptimisation());
				optionAdded = true;
				enabledOptionAdded = true;
				dialog.addValidation(option, new IValidator() {

					@Override
					public IStatus validate(final Object value) {
						if (value instanceof UserSettings) {
							final UserSettings userSettings = (UserSettings) value;
							if (userSettings.isCleanStateOptimisation()) {
								if (userSettings.getSimilarityMode() != SimilarityMode.OFF) {
									return ValidationStatus.error("Similarity must be disabled with clean state optimisation");
								}
							}
						}
						return Status.OK_STATUS;
					}
				});
			}

			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Shipping only: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_SHIPPING_ONLY_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_ShippingOnly());
				optionAdded = true;
				enabledOptionAdded = true;
			}
			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Spot cargo markets: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_WITH_SPOT_CARGO_MARKETS_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_WithSpotCargoMarkets());
				optionAdded = true;
				enabledOptionAdded = true;
			}
		}
		// if (SecurityUtils.getSubject().isPermitted("features:optimisation-charter-out-generation")) {
		{
			// dialog.addOption(DataSection.Main, null, editingDomian, "Generate Charter Outs", copy, defaultSettings, DataType.Choice,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_GenerateCharterOuts());

			final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
			choiceData.addChoice("Off", Boolean.FALSE);
			choiceData.addChoice("On", Boolean.TRUE);

			choiceData.enabled = LicenseFeatures.isPermitted("features:optimisation-charter-out-generation") && isAllowedGCO(scenario);
			if (choiceData.enabled == false) {
				// if not enabled make sure to set setting to false
				copy.setGenerateCharterOuts(false);
			}
			// dialog.addOption(DataSection.Main, null, editingDomian, "Similarity", copy, defaultSettings, DataType.Choice, choiceData,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_Range(), ParametersPackage.eINSTANCE.getOptimisationRange_OptimiseAfter());
			final Option gcoOption = dialog.addOption(DataSection.Toggles, null, editingDomain, "Generate charter outs: ", copy, defaultSettings, DataType.Choice, choiceData,
					SWTBOT_CHARTEROUTGENERATION_PREFIX, ParametersPackage.eINSTANCE.getUserSettings_GenerateCharterOuts());
			optionAdded = true;
			enabledOptionAdded = choiceData.enabled;
		}
		if (!forEvaluation) {
			{
				if (LicenseFeatures.isPermitted("features:optimisation-idle-days")) {
					final Option idleDays = dialog.addOption(DataSection.Toggles, null, editingDomain, "Netback idle day tolerance", copy, defaultSettings, DataType.PositiveInt, SWTBOT_IDLE_DAYS,
							ParametersPackage.eINSTANCE.getUserSettings_FloatingDaysLimit());
				}
			}
			// if (SecurityUtils.getSubject().isPermitted("features:optimisation-similarity")) {
			{

				final OptionGroup group = dialog.createGroup(DataSection.Controls, "Similarity");

				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", SimilarityMode.OFF);
				choiceData.addChoice("Low", SimilarityMode.LOW);
				choiceData.addChoice("Med", SimilarityMode.MEDIUM);
				choiceData.addChoice("High", SimilarityMode.HIGH);
				// choiceData.addChoice("All", SimilarityMode.ALL);

				choiceData.enabled = LicenseFeatures.isPermitted("features:optimisation-similarity");

				final Option option = dialog.addOption(DataSection.Controls, group, editingDomain, "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_SIMILARITY_PREFIX,
						ParametersPackage.Literals.USER_SETTINGS__SIMILARITY_MODE);
				optionAdded = true;
				enabledOptionAdded = choiceData.enabled;

			}
			{

				final OptionGroup group = dialog.createGroup(DataSection.Controls, "Action sets");

				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);

				choiceData.enabled = LicenseFeatures.isPermitted("features:optimisation-actionset");

				final Option option = dialog.addOption(DataSection.Controls, group, editingDomain, " ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_ACTION_SET_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_BuildActionSets());
				optionAdded = true;
				dialog.addValidation(option, new IValidator() {

					@Override
					public IStatus validate(final Object value) {
						if (value instanceof UserSettings) {
							final UserSettings userSettings = (UserSettings) value;
							if (userSettings.isBuildActionSets()) {
								if (userSettings.getSimilarityMode() == SimilarityMode.OFF) {
									return ValidationStatus.error("Similarity must be enabled to use action sets");
								}
								final YearMonth periodStart = userSettings.getPeriodStart();
								final YearMonth periodEnd = userSettings.getPeriodEnd();
								if (periodStart != null && periodEnd != null) {
									// 3 month window?
									if (Months.between(periodStart, periodEnd) > 6) {
										return ValidationStatus.error("Unable to run with Action Sets as the period range is greater than six months");
									} else if (Months.between(periodStart, periodEnd) > 3 && userSettings.getSimilarityMode() == SimilarityMode.LOW) {
										return ValidationStatus
												.error("Unable to run with Action Sets as the period range is too long for the low similarity setting (max 3 Months). Please try medium or high");
									}
								} else {
									return ValidationStatus.error("Unable to run with Action Sets as the period range is greater than six months");
								}
							}
						}
						return Status.OK_STATUS;
					}
				});
				optionAdded = true;
				enabledOptionAdded = choiceData.enabled;
			}
		}

		if (optionAdded && (enabledOptionAdded || !displayOnlyIfOptionsEnabled)) {
			final int[] ret = new int[1];
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					ret[0] = dialog.open();
				}
			});

			if (ret[0] != Window.OK) {
				return null;
			}
		}
		return copy;
	}

	public static @NonNull OptimisationPlan transformUserSettings(@NonNull final UserSettings userSettings, @Nullable final String parameterMode, final LNGScenarioModel lngScenarioModel) {

		final OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();

		plan.setUserSettings(userSettings);

		plan.setSolutionBuilderSettings(ScenarioUtils.createDefaultSolutionBuilderSettings());

		@NonNull
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		Objective similarityObjective = findObjective(SimilarityFitnessCoreFactory.NAME, constraintAndFitnessSettings);
		if (similarityObjective == null) {
			similarityObjective = ParametersFactory.eINSTANCE.createObjective();
			similarityObjective.setName(SimilarityFitnessCoreFactory.NAME);
			constraintAndFitnessSettings.getObjectives().add(similarityObjective);
		}
		similarityObjective.setEnabled(true);
		similarityObjective.setWeight(1.0);

		final YearMonth periodStart = userSettings.getPeriodStart();
		final YearMonth periodEnd = userSettings.getPeriodEnd();

		final YearMonth periodStartOrDefault = getPeriodStartOrDefault(periodStart, lngScenarioModel);
		final YearMonth periodEndOrDefault = getPeriodEndOrDefault(periodEnd, lngScenarioModel);

		final SimilarityMode similarityMode = userSettings.getSimilarityMode();

		boolean shouldUseRestartingLSO = false;

		switch (similarityMode) {
		case ALL:
			assert false;
			break;
		case HIGH:
			constraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.HIGH, periodStartOrDefault, periodEndOrDefault));
			shouldUseRestartingLSO = true;
			if (shouldDisableActionSets(SimilarityMode.HIGH, periodStart, periodEnd)) {
				userSettings.setBuildActionSets(false);
			}
			break;
		case LOW:
			constraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.LOW, periodStartOrDefault, periodEndOrDefault));
			shouldUseRestartingLSO = false;
			if (shouldDisableActionSets(SimilarityMode.LOW, periodStart, periodEnd)) {
				userSettings.setBuildActionSets(false);
			}
			break;
		case MEDIUM:
			constraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.MEDIUM, periodStartOrDefault, periodEndOrDefault));
			shouldUseRestartingLSO = false;
			if (shouldDisableActionSets(SimilarityMode.MEDIUM, periodStart, periodEnd)) {
				userSettings.setBuildActionSets(false);
			}
			break;
		case OFF:
			constraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.OFF, periodStartOrDefault, periodEndOrDefault));
			shouldUseRestartingLSO = false;
			userSettings.setBuildActionSets(false);
			break;
		default:
			assert false;
			break;
		}

		int epochLength;
		// TODO: make this better!
		if (userSettings.isSetPeriodStart() && userSettings.isSetPeriodEnd()) {
			epochLength = EPOCH_LENGTH_PERIOD;
		} else {
			epochLength = EPOCH_LENGTH_FULL;
		}
		if (userSettings.isCleanStateOptimisation()) {
			final CleanStateOptimisationStage stage = ScenarioUtils.createDefaultCleanStateParameters(EcoreUtil.copy(constraintAndFitnessSettings));
			stage.getAnnealingSettings().setEpochLength(epochLength);
			plan.getStages().add(stage);
		}
		{
			final LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(EcoreUtil.copy(constraintAndFitnessSettings));
			stage.getAnnealingSettings().setEpochLength(epochLength);
			stage.getAnnealingSettings().setRestarting(shouldUseRestartingLSO);
			plan.getStages().add(stage);
		}
		{
			final HillClimbOptimisationStage stage = ScenarioUtils.createDefaultHillClimbingParameters(EcoreUtil.copy(constraintAndFitnessSettings));
			stage.getAnnealingSettings().setEpochLength(epochLength);
			plan.getStages().add(stage);
		}

		if (userSettings.isBuildActionSets()) {
			if (periodStart != null && periodEnd != null) {
				final ActionPlanOptimisationStage stage = ScenarioUtils.getActionPlanSettings(similarityMode, periodStart, periodEnd, EcoreUtil.copy(constraintAndFitnessSettings));
				plan.getStages().add(stage);
			} else {
				final ActionPlanOptimisationStage stage = ScenarioUtils.createDefaultActionPlanParameters(EcoreUtil.copy(constraintAndFitnessSettings));
				plan.getStages().add(stage);
			}
		}
		return LNGScenarioRunnerUtils.createExtendedSettings(plan);
	}

	private static boolean shouldDisableActionSets(final SimilarityMode mode, final YearMonth periodStart, final YearMonth periodEnd) {
		if (periodStart == null || periodEnd == null) {
			return true;
		}
		if (mode == SimilarityMode.LOW && Months.between(periodStart, periodEnd) > 3) {
			return true;
		} else if (mode == SimilarityMode.MEDIUM && Months.between(periodStart, periodEnd) > 6) {
			return true;
		} else if (mode == SimilarityMode.HIGH && Months.between(periodStart, periodEnd) > 6) {
			return true;
		} else if (mode == SimilarityMode.OFF) {
			return true;
		}
		return false;
	}

	private static SimilaritySettings createSimilaritySettings(final SimilarityMode mode, final YearMonth periodStart, final YearMonth periodEnd) {
		if (periodStart == null || periodEnd == null || mode == null) {
			return ScenarioUtils.createOffSimilaritySettings();
		} else {
			return SimilarityUIParameters.getSimilaritySettings(mode, periodStart, periodEnd);
		}
	}

	private static void resetDisabledFeatures(@NonNull final UserSettings copy) {
		if (!LicenseFeatures.isPermitted("features:optimisation-actionset")) {
			copy.setBuildActionSets(false);
		}
		if (!LicenseFeatures.isPermitted("features:optimisation-period")) {
			copy.unsetPeriodStart();
			copy.unsetPeriodEnd();
		}
		if (!LicenseFeatures.isPermitted("features:optimisation-charter-out-generation")) {
			copy.setGenerateCharterOuts(false);
		}
		if (!LicenseFeatures.isPermitted("features:optimisation-similarity")) {
			copy.setSimilarityMode(SimilarityMode.OFF);
		}
	}

	/**
	 * Returns true if merged parameters are valid.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static void mergeFields(@NonNull final UserSettings from, @NonNull final UserSettings to) {

		resetDisabledFeatures(from);

		// Sometime these are the same instance, so don't bother with the next bit
		if (from == to) {
			return;
		}
		// TODO: replace all this ugly code by a list of EStructuralFeatures and loop through
		// them doing the right thing
		if (from.isSetPeriodStart() == false || from.getPeriodStart() == null) {
			to.unsetPeriodStart();
		} else {
			to.setPeriodStart(from.getPeriodStart());
		}
		if (from.isSetPeriodEnd() == false || from.getPeriodEnd() == null) {
			to.unsetPeriodEnd();
		} else {
			to.setPeriodEnd(from.getPeriodEnd());
		}

		to.setShippingOnly(from.isShippingOnly());
		to.setGenerateCharterOuts(from.isGenerateCharterOuts());
		to.setWithSpotCargoMarkets(from.isWithSpotCargoMarkets());

		if (from.getSimilarityMode() != null) {
			to.setSimilarityMode(from.getSimilarityMode());
		}
		to.setBuildActionSets(from.isBuildActionSets());

		to.setFloatingDaysLimit(from.getFloatingDaysLimit());
	}

	public static boolean checkUserSettings(@NonNull final UserSettings to, final boolean quiet) {
		resetDisabledFeatures(to);

		// Turn off if settings are not nice
		if (to.isBuildActionSets()) {

			String actionSetErrorMessage = null;

			if (to.getSimilarityMode() == SimilarityMode.OFF) {
				to.setBuildActionSets(false);
				actionSetErrorMessage = "Unable to run with Action Sets as similarity is turned off";
			}
			if (to.isBuildActionSets()) {
				if (to.getPeriodStart() == null && to.getPeriodEnd() == null) {
					actionSetErrorMessage = "Unable to run with Action Sets as there is no period range set";
					to.setBuildActionSets(false);
				} else {
					final YearMonth periodStart = to.getPeriodStart();
					final YearMonth periodEnd = to.getPeriodEnd();
					if (periodStart != null && periodEnd != null) {
						// 3 month window?
						if (Months.between(periodStart, periodEnd) > 6) {
							actionSetErrorMessage = "Unable to run with Action Sets as the period range is too long";
							to.setBuildActionSets(false);
						} else if (Months.between(periodStart, periodEnd) > 3 && to.getSimilarityMode() == SimilarityMode.LOW) {
							actionSetErrorMessage = "Unable to run with Action Sets as the period range is too long for the low similarity setting. Please try medium or high";
							to.setBuildActionSets(false);
						}
					} else {
						actionSetErrorMessage = "Unable to run with Action Sets as the period range is too long";
						to.setBuildActionSets(false);
					}
				}
			}

			if (!quiet && actionSetErrorMessage != null) {
				log.info(actionSetErrorMessage);

				if (System.getProperty("lingo.suppress.dialogs") == null) {
					final String errMessage = actionSetErrorMessage;
					final Display display = PlatformUI.getWorkbench().getDisplay();
					if (display != null) {
						display.syncExec(new Runnable() {

							@Override
							public void run() {
								MessageDialog.openError(display.getActiveShell(), "Unable to start optimisation", errMessage);
							}
						});
					}
				}
				return false;
			}
		}
		return true;
	}

	private static Objective findObjective(final String objective, final ConstraintAndFitnessSettings settings) {
		for (final Objective o : settings.getObjectives()) {
			if (SimilarityFitnessCoreFactory.NAME.equals(o.getName())) {
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
			Collections.sort(loadSlots, new Comparator<LoadSlot>() {

				@Override
				public int compare(final LoadSlot o1, final LoadSlot o2) {
					return o1.getWindowEndWithSlotOrPortTimeWithFlex().compareTo(o2.getWindowEndWithSlotOrPortTimeWithFlex()) * -1;
				}
			});
			if (loadSlots.isEmpty()) {
				return YearMonth.of(2000, 1);
			}
			return YearMonth.of(loadSlots.get(0).getWindowStartWithSlotOrPortTime().getYear(), loadSlots.get(0).getWindowStartWithSlotOrPortTime().getMonth());
		}
	}

	private static YearMonth getPeriodStartOrDefault(final YearMonth periodStart, final LNGScenarioModel scenario) {
		if (periodStart != null) {
			return periodStart;
		} else if (scenario == null) {
			return periodStart;
		} else {
			final List<LoadSlot> loadSlots = new LinkedList<>(scenario.getCargoModel().getLoadSlots());
			Collections.sort(loadSlots, new Comparator<LoadSlot>() {

				@Override
				public int compare(final LoadSlot o1, final LoadSlot o2) {
					return o1.getWindowStartWithSlotOrPortTime().compareTo(o2.getWindowStartWithSlotOrPortTime());
				}
			});
			if (loadSlots.isEmpty()) {
				return YearMonth.of(2000, 1);
			}
			return YearMonth.of(loadSlots.get(0).getWindowStartWithSlotOrPortTime().getYear(), loadSlots.get(0).getWindowStartWithSlotOrPortTime().getMonth());
		}
	}

	public static boolean isAllowedGCO(final LNGScenarioModel lngScenarioModel) {
		if (lngScenarioModel == null) {
			return false;
		}
		if (!checkBreakEvenInSlot(lngScenarioModel.getCargoModel().getLoadSlots()) && !checkBreakEvenInSlot(lngScenarioModel.getCargoModel().getDischargeSlots())) {
			return true;
		}
		return false;
	}

	private static boolean checkBreakEvenInSlot(final Collection<? extends Slot> slots) {
		for (final Slot slot : slots) {
			if (slot.isSetPriceExpression() && slot.getPriceExpression().contains(IBreakEvenEvaluator.MARKER)) {
				return true;
			}
		}
		return false;
	}

}

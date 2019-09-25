/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.shiro.SecurityUtils;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.MultiValidator;
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
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
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
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
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;

public final class OptimisationHelper {
	public static class NameProvider {
		public NameProvider(String suggestion, Set<String> existingNames) {
			this.nameSuggestion = suggestion;
			this.existingNames = existingNames;
		}

		String nameSuggestion;
		Set<String> existingNames;

		public String getNameSuggestion() {
			return nameSuggestion;
		}

		public Set<String> getExistingNames() {
			return existingNames;
		}
	}

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

	public static final String SWTBOT_ADP_PREFIX = "swtbot.adp";
	public static final String SWTBOT_ADP_ON = SWTBOT_ADP_PREFIX + ".On";
	public static final String SWTBOT_ADP_OFF = SWTBOT_ADP_PREFIX + ".Off";

	public static final String SWTBOT_CLEAN_STATE_PREFIX = "swtbot.cleanstate";
	public static final String SWTBOT_CLEAN_STATE_ON = SWTBOT_CLEAN_STATE_PREFIX + ".On";
	public static final String SWTBOT_CLEAN_STATE_OFF = SWTBOT_CLEAN_STATE_PREFIX + ".Off";

	public static final String SWTBOT_NOMINAL_ADP_PREFIX = "swtbot.nominal.adp";
	public static final String SWTBOT_NOMINAL_ADP_ON = SWTBOT_NOMINAL_ADP_PREFIX + ".On";
	public static final String SWTBOT_NOMINAL_ADP_OFF = SWTBOT_NOMINAL_ADP_PREFIX + ".Off";

	public static final String SWTBOT_ACTION_SET_PREFIX = "swtbot.actionset";
	public static final String SWTBOT_ACTION_SET_ON = SWTBOT_ACTION_SET_PREFIX + ".On";
	public static final String SWTBOT_ACTION_SET_OFF = SWTBOT_ACTION_SET_PREFIX + ".Off";

	public static final String SWTBOT_CHARTEROUTGENERATION_PREFIX = "swtbot.charteroutgeneration";
	public static final String SWTBOT_CHARTEROUTGENERATION_ON = SWTBOT_CHARTEROUTGENERATION_PREFIX + ".On";
	public static final String SWTBOT_CHARTEROUTGENERATION_OFF = SWTBOT_CHARTEROUTGENERATION_PREFIX + ".Off";

	public static final String SWTBOT_CHARTERLENGTH_PREFIX = "swtbot.charterlength";
	public static final String SWTBOT_CHARTERLENGTH_ON = SWTBOT_CHARTERLENGTH_PREFIX + ".On";
	public static final String SWTBOT_CHARTERLENGTH_OFF = SWTBOT_CHARTERLENGTH_PREFIX + ".Off";

	public static final String SWTBOT_DUAL_MODE_PREFIX = "swtbot.dualmode";
	public static final String SWTBOT_DUAL_MODE_ON = SWTBOT_DUAL_MODE_PREFIX + ".On";
	public static final String SWTBOT_DUAL_MODE_OFF = SWTBOT_DUAL_MODE_PREFIX + ".Off";

	public static final String SWTBOT_SIMILARITY_PREFIX = "swtbot.similaritymode";	
	public static final String SWTBOT_SIMILARITY_PREFIX_OFF = SWTBOT_SIMILARITY_PREFIX + ".Off";
	/**
	 * @deprecated
	 */
	public static final String SWTBOT_SIMILARITY_PREFIX_LOW = SWTBOT_SIMILARITY_PREFIX + ".Low";
	/**
	 * @deprecated
	 */
	public static final String SWTBOT_SIMILARITY_PREFIX_MEDIUM = SWTBOT_SIMILARITY_PREFIX + ".Med";
	/**
	 * @deprecated
	 */
	public static final String SWTBOT_SIMILARITY_PREFIX_HIGH = SWTBOT_SIMILARITY_PREFIX + ".High";

	public static final String SWTBOT_PERIOD_START = "swtbot.period.start";
	public static final String SWTBOT_PERIOD_TODAY = "swtbot.period.today";
	public static final String SWTBOT_PERIOD_THREE_MONTH = "swtbot.period.three_month";
	public static final String SWTBOT_PERIOD_END = "swtbot.period.end";

	public static final String SWTBOT_IDLE_DAYS = "swtbot.idledays";

	public static Object evaluateScenarioInstance(@NonNull final IEclipseJobManager jobManager, @NonNull final ScenarioInstance instance, @Nullable final String parameterMode,
			final boolean promptForOptimiserSettings, final boolean optimising, final boolean promptOnlyIfOptionsEnabled, String nameSuggestion, Set<String> existingNames) {
		return evaluateScenarioInstance(jobManager, instance, parameterMode, promptForOptimiserSettings, optimising, promptOnlyIfOptionsEnabled, new NameProvider(nameSuggestion, existingNames));
	}

	public static Object evaluateScenarioInstance(@NonNull final IEclipseJobManager jobManager, @NonNull final ScenarioInstance instance, @Nullable final String parameterMode,
			final boolean promptForOptimiserSettings, final boolean optimising, final boolean promptOnlyIfOptionsEnabled, NameProvider nameProvider) {

		final IScenarioService service = SSDataManager.Instance.findScenarioService(instance);
		if (service == null) {
			return null;
		}
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

		final OptimisationPlan[] planRef = new OptimisationPlan[1];
		final BiFunction<IScenarioDataProvider, LNGScenarioModel, Boolean> prepareCallback = (ref, root) -> {
			final OptimisationPlan optimisationPlan = getOptimiserSettings(root, !optimising, parameterMode, promptForOptimiserSettings, promptOnlyIfOptionsEnabled, nameProvider);

			if (optimisationPlan == null) {
				return false;
			}
			final EditingDomain editingDomain = ref.getEditingDomain();
			if (editingDomain != null) {
				final CompoundCommand cmd = new CompoundCommand("Update settings");
				cmd.append(SetCommand.create(editingDomain, root, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), EcoreUtil.copy(optimisationPlan.getUserSettings())));
				RunnerHelper.syncExecDisplayOptional(() -> {
					editingDomain.getCommandStack().execute(cmd);
				});
			}

			planRef[0] = optimisationPlan;
			return true;
		};

		final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
			return new LNGSchedulerJobDescriptor(instance.getName(), instance, planRef[0], optimising);
		};
		String taskName = "Optimise " + instance.getName();

		final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
		jobRunner.run(taskName, instance, modelRecord, prepareCallback, createJobDescriptorCallback, null);

		return null;
	}

	public static UserSettings promptForUserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final boolean promptUser, final boolean promptOnlyIfOptionsEnabled,
			NameProvider nameProvider) {
		UserSettings previousSettings = null;
		if (scenario != null) {
			previousSettings = scenario.getUserSettings();
		}
		return promptForInsertionUserSettings(scenario, forEvaluation, promptUser, promptOnlyIfOptionsEnabled, nameProvider, previousSettings);

	}

	public static UserSettings promptForUserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final boolean promptUser, final boolean promptOnlyIfOptionsEnabled,
			NameProvider nameProvider, UserSettings previousSettings) {

		final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
		if (previousSettings == null) {
			previousSettings = userSettings;
		}

		// Permit the user to override the settings object. Use the previous settings as the initial value
		if (promptUser) {
			previousSettings = openUserDialog(scenario, forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled, nameProvider, false);
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
			final boolean promptOnlyIfOptionsEnabled, NameProvider nameProvider) {

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
			boolean forADP = scenario.getAdpModel() != null;
			previousSettings = openUserDialog(scenario, forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled, nameProvider, forADP);
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
		if (nameProvider != null) {
			optimisationPlan.setResultName(nameProvider.nameSuggestion);
		}

		return optimisationPlan;
	}

	public static UserSettings openUserDialog(final LNGScenarioModel scenario, final boolean forEvaluation, final UserSettings previousSettings, final UserSettings defaultSettings,
			final boolean displayOnlyIfOptionsEnabled, NameProvider nameProvider, boolean forADP) {
		return openUserDialog(scenario, PlatformUI.getWorkbench().getDisplay(), PlatformUI.getWorkbench().getDisplay().getActiveShell(), forEvaluation, previousSettings, defaultSettings,
				displayOnlyIfOptionsEnabled, nameProvider, forADP);
	}

	public static UserSettings openUserDialog(final LNGScenarioModel scenario, final Display display, final Shell shell, final boolean forEvaluation, final UserSettings previousSettings,
			final UserSettings defaultSettings, final boolean displayOnlyIfOptionsEnabled, NameProvider nameProvider, boolean forADP) {

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

		if (!forEvaluation && nameProvider != null) {
			dialog.addNameOption(nameProvider.nameSuggestion, nameProvider.existingNames);
		}

		if (!forEvaluation) {
			// dialog.addOption(DataSection.Controls, null, editingDomain, "Number of Iterations", copy, defaultSettings, DataType.PositiveInt,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings(), ParametersPackage.eINSTANCE.getAnnealingSettings_Iterations());
			// optionAdded = true;

			// Check period optimisation is permitted
			// if (SecurityUtils.getSubject().isPermitted(KnownFeatures.FEATURE_OPTIMISATION_PERIOD)) {
			{

				if (forADP) {
					boolean scenarioContainsForbiddedADPEvents = false;
					String adpVesselEventIssueMsg = "";
					if (scenario != null) {
						CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenario);
						for (VesselEvent event : cargoModel.getVesselEvents()) {
							if (!(event instanceof DryDockEvent || event instanceof CharterOutEvent)) {
								scenarioContainsForbiddedADPEvents = true;
								copy.setCleanStateOptimisation(false);
								adpVesselEventIssueMsg = "Clean slate only supports dry-dock and charter out vessel events.";
								break;
							}
							if ((event instanceof CharterOutEvent)) {
								CharterOutEvent charterOutEvent = (CharterOutEvent) event;
								if (charterOutEvent.isOptional() || charterOutEvent.getRelocateTo() != null) {
									scenarioContainsForbiddedADPEvents = true;
									copy.setCleanStateOptimisation(false);
									adpVesselEventIssueMsg = "Clean slate only supports non-optional and non-relocated charter out events.";
									break;
								}
							}
						}
					}
					final OptionGroup group = dialog.createGroup(DataSection.General, "ADP");
					{
						final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
						choiceData.addChoice("No", Boolean.FALSE);
						choiceData.addChoice("Yes", Boolean.TRUE);
						final Option option = dialog.addOption(DataSection.General, group, editingDomain, "Enabled: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_ADP_PREFIX,
								ParametersPackage.eINSTANCE.getUserSettings_AdpOptimisation());
						optionAdded = true;
						enabledOptionAdded = true;
					}
					{
						final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
						choiceData.addChoice("No", Boolean.FALSE);
						choiceData.addChoice("Yes", Boolean.TRUE);
						if (scenarioContainsForbiddedADPEvents) {
							choiceData.enabled = false;
							choiceData.disabledMessage = adpVesselEventIssueMsg;
						} else {
							choiceData.enabledHook = (UserSettings::isAdpOptimisation);
						}
						final Option option = dialog.addOption(DataSection.General, group, editingDomain, "Clean slate: ", "", copy, defaultSettings, DataType.Choice, choiceData,
								SWTBOT_CLEAN_STATE_PREFIX, ParametersPackage.eINSTANCE.getUserSettings_CleanStateOptimisation());
						optionAdded = true;
						enabledOptionAdded = true;

						dialog.addValidation(option, new IValidator() {

							@Override
							public IStatus validate(final Object value) {
								if (value instanceof UserSettings) {
									final UserSettings userSettings = (UserSettings) value;

									if (userSettings.isAdpOptimisation()) {
										if (userSettings.getPeriodStartDate() != null || userSettings.getPeriodEnd() != null) {
											return ValidationStatus.error("Period optimisation must be disabled with ADP optimisation");
										}
										if (userSettings.isCleanStateOptimisation()) {
											if (userSettings.getSimilarityMode() != SimilarityMode.OFF) {
												return ValidationStatus.error("Similarity must be disabled with clean slate ADP optimisation");
											}

											if (userSettings.isBuildActionSets()) {
												return ValidationStatus.error("Action sets must be disabled with clean slate ADP optimisation");
											}
											if (userSettings.isGenerateCharterOuts()) {
												return ValidationStatus.error("Charter out generation must be disabled with clean slate ADP optimisation");
											}
										}
									}
								}
								return Status.OK_STATUS;
							}
						});
					}
					{
						final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
						choiceData.addChoice("No", Boolean.FALSE);
						choiceData.addChoice("Yes", Boolean.TRUE);
						if (scenarioContainsForbiddedADPEvents) {
							choiceData.enabled = false;
							choiceData.disabledMessage = adpVesselEventIssueMsg;
						} else {
							choiceData.enabledHook = (u -> (u.isAdpOptimisation() && u.isCleanStateOptimisation()));
						}
						final Option option = dialog.addOption(DataSection.General, group, editingDomain, "Nominal ADP: ", "", copy, defaultSettings, DataType.Choice, choiceData,
								SWTBOT_NOMINAL_ADP_PREFIX, ParametersPackage.eINSTANCE.getUserSettings_NominalADP());
						optionAdded = true;
						enabledOptionAdded = true;

						dialog.addValidation(option, new IValidator() {

							@Override
							public IStatus validate(final Object value) {
								if (value instanceof UserSettings) {
									final UserSettings userSettings = (UserSettings) value;

									if (userSettings.isAdpOptimisation()) {
										if (userSettings.getPeriodStartDate() != null || userSettings.getPeriodEnd() != null) {
										}

									}
								}
								return Status.OK_STATUS;
							}
						});
					}
				}

				final OptionGroup group = dialog.createGroup(DataSection.Controls, "Optimise period");
				final Option optStart = dialog.addOption(DataSection.Controls, group, editingDomain, "Start of (dd/mm/yyyy)", "", copy, defaultSettings, DataType.Date, SWTBOT_PERIOD_START,
						ParametersPackage.eINSTANCE.getUserSettings_PeriodStartDate());
				// Added a button. Had to extend Option and DataType classes.
				final Option optToday = dialog.addOption(DataSection.Controls, group, editingDomain, "Today", "Today", copy, defaultSettings, DataType.Button, SWTBOT_PERIOD_TODAY, null);
				optToday.setListener(new MouseAdapter() {

					@Override
					public void mouseDown(MouseEvent e) {
						if (copy != null) {
							copy.setPeriodStartDate(LocalDate.now());
						}
						if (defaultSettings != null) {
							defaultSettings.setPeriodStartDate(LocalDate.now());
						}
					}
				});
				final Option optEnd = dialog.addOption(DataSection.Controls, group, editingDomain, "Up to start of (mm/yyyy)", "", copy, defaultSettings, DataType.MonthYear, SWTBOT_PERIOD_END,
						ParametersPackage.eINSTANCE.getUserSettings_PeriodEnd());
				final Option optThreeMonth = dialog.addOption(DataSection.Controls, group, editingDomain, "+3m", "Three months", copy, defaultSettings, DataType.Button, SWTBOT_PERIOD_THREE_MONTH,
						null);
				optThreeMonth.setListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						if (copy != null) {
							setPeriodEnd(copy);
						}
						if (defaultSettings != null) {
							setPeriodEnd(defaultSettings);
						}
					}
				});

				if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_PERIOD)) {
					optStart.enabled = false;
					optEnd.enabled = false;
					optToday.enabled = false;
				} else {
					enabledOptionAdded = true;

					final IObservableValue[] values = new IObservableValue[2];

					MultiValidator validator = new MultiValidator() {

						@Override
						protected IStatus validate() {
							LocalDate periodStart = null;
							if (values[0].getValue() instanceof LocalDate) {
								periodStart = (LocalDate) values[0].getValue();
							}
							YearMonth periodEnd = null;
							if (values[1].getValue() instanceof YearMonth) {
								periodEnd = (YearMonth) values[1].getValue();
							}
							if (periodStart != null && periodEnd != null) {
								if (periodEnd.atDay(1).isBefore(periodStart)) {
									return ValidationStatus.error("Period start must be before period end");
								}
							}
							return Status.OK_STATUS;
						}
					};

					dialog.addValidation(optStart, value -> {
						if (value instanceof LocalDate) {
							final LocalDate startDate = (LocalDate) value;
							if (startDate.getYear() < 2010) {
								return ValidationStatus.error("Invalid period start date");
							}
						}
						return Status.OK_STATUS;
					});
					dialog.addValidation(optEnd, value -> {
						if (value instanceof YearMonth) {
							final YearMonth endDate = (YearMonth) value;
							if (endDate.getYear() < 2010) {
								return ValidationStatus.error("Invalid period end date");
							}
						}
						return Status.OK_STATUS;
					});

					dialog.addValidationCallback(optStart, (v) -> values[0] = v);
					dialog.addValidationCallback(optEnd, (v) -> values[1] = v);

					dialog.addValidationStatusProvider(validator);
				}
				optionAdded = true;
			}

			// if (LicenseFeatures.isPermitted("features:optimisation-clean-state")) {
			// final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
			// choiceData.addChoice("Off", Boolean.FALSE);
			// choiceData.addChoice("On", Boolean.TRUE);
			// final Option option = dialog.addOption(DataSection.Toggles, null, editingDomain, "Clean State: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_CLEAN_STATE_PREFIX,
			// ParametersPackage.eINSTANCE.getUserSettings_CleanStateOptimisation());
			// optionAdded = true;
			// enabledOptionAdded = true;
			// dialog.addValidation(option, new IValidator() {
			//
			// @Override
			// public IStatus validate(final Object value) {
			// if (value instanceof UserSettings) {
			// final UserSettings userSettings = (UserSettings) value;
			// if (userSettings.isCleanStateOptimisation()) {
			// if (userSettings.getSimilarityMode() != SimilarityMode.OFF) {
			// return ValidationStatus.error("Similarity must be disabled with clean state optimisation");
			// }
			// }
			// }
			// return Status.OK_STATUS;
			// }
			// });
			// }

			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Shipping only: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_SHIPPING_ONLY_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_ShippingOnly());
				optionAdded = true;
				enabledOptionAdded = true;
			}
			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				choiceData.enabled = LicenseFeatures.isPermitted("features:charter-length");
				if (choiceData.enabled == false) {
					// if not enabled make sure to set setting to false
					copy.setWithCharterLength(false);
				}
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Charter Length: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_CHARTERLENGTH_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_WithCharterLength());
				optionAdded = true;
				enabledOptionAdded = true;
			}
			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Spot cargo markets: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_WITH_SPOT_CARGO_MARKETS_PREFIX,
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
			final Option gcoOption = dialog.addOption(DataSection.Toggles, null, editingDomain, "Generate charter outs: ", "", copy, defaultSettings, DataType.Choice, choiceData,
					SWTBOT_CHARTEROUTGENERATION_PREFIX, ParametersPackage.eINSTANCE.getUserSettings_GenerateCharterOuts());
			optionAdded = !forEvaluation;
			enabledOptionAdded = choiceData.enabled;

			if (scenario != null) {
				final SpotMarketsModel model = ScenarioModelUtil.getSpotMarketsModel(scenario);
				if (model != null) {
					final CharterOutMarketParameters params = model.getCharterOutMarketParameters();
					if (params != null) {
						String ltext = "";
						final DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");
						final LocalDate start = params.getCharterOutStartDate();
						final LocalDate end = params.getCharterOutEndDate();
						boolean hasStart = false;
						if (start != null) {
							ltext += start.format(format);
							hasStart = true;
						}
						if (end != null) {
							if (hasStart)
								ltext += "-";
							ltext += end.format(format);
						}
						if (ltext.length() > 0) {
							final String ftext = "Charter out dates (" + ltext + ")";
							choiceData.changeHandlers.add((label, value) -> {
								label.setText(ftext);
								label.setVisible((Boolean) value);
							});
						}
					}
				}
			}

		}
		if (!forEvaluation) {
			{
				if (LicenseFeatures.isPermitted("features:optimisation-idle-days")) {
					final Option idleDays = dialog.addOption(DataSection.Toggles, null, editingDomain, "Netback idle day tolerance", "", copy, defaultSettings, DataType.PositiveInt, SWTBOT_IDLE_DAYS,
							ParametersPackage.eINSTANCE.getUserSettings_FloatingDaysLimit());
				}
			}
			// if (SecurityUtils.getSubject().isPermitted("features:optimisation-similarity")) {
			{

				final OptionGroup group = dialog.createGroup(DataSection.Controls, "Similarity");

				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", SimilarityMode.OFF);
//				choiceData.addChoice("Low", SimilarityMode.LOW);
//				choiceData.addChoice("Med", SimilarityMode.MEDIUM);
//				choiceData.addChoice("High", SimilarityMode.HIGH);
				choiceData.addChoice("All", SimilarityMode.ALL);

				choiceData.enabled = LicenseFeatures.isPermitted("features:optimisation-similarity");

				final Option option = dialog.addOption(DataSection.Controls, group, editingDomain, "", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_SIMILARITY_PREFIX,
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

				final Option option = dialog.addOption(DataSection.Controls, group, editingDomain, " ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_ACTION_SET_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_BuildActionSets());
				optionAdded = true;
				dialog.addValidation(option, new IValidator() {

					@Override
					public IStatus validate(final Object value) {
						if (value instanceof UserSettings) {
							final UserSettings userSettings = (UserSettings) value;
							if (userSettings.isBuildActionSets()) {
								if (userSettings.getSimilarityMode() == SimilarityMode.OFF || userSettings.getSimilarityMode() == SimilarityMode.ALL) {
									return ValidationStatus.error("Similarity (low, medium, high) must be enabled to use action sets");
								}
								final LocalDate periodStart = userSettings.getPeriodStartDate();
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
			final int[] ret = new int[] { Window.CANCEL };
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
		if (nameProvider != null) {
			nameProvider.nameSuggestion = dialog.getNameSuggestion();
		}
		return copy;
	}

	public static UserSettings promptForInsertionUserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final boolean promptUser, final boolean promptOnlyIfOptionsEnabled,
			NameProvider nameProvider) {
		UserSettings previousSettings = null;
		if (scenario != null) {
			previousSettings = scenario.getUserSettings();
		}
		return promptForInsertionUserSettings(scenario, forEvaluation, promptUser, promptOnlyIfOptionsEnabled, nameProvider, previousSettings);

	}

	public static UserSettings promptForInsertionUserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final boolean promptUser, final boolean promptOnlyIfOptionsEnabled,
			NameProvider nameProvider, UserSettings previousSettings) {
		final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
		if (previousSettings == null) {
			previousSettings = userSettings;
		}

		// Permit the user to override the settings object. Use the previous settings as the initial value
		if (promptUser) {
			previousSettings = openInsertionPlanUserDialog(scenario, forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled, nameProvider);
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

	public static UserSettings openInsertionPlanUserDialog(final LNGScenarioModel scenario, final boolean forEvaluation, final UserSettings previousSettings, final UserSettings defaultSettings,
			final boolean displayOnlyIfOptionsEnabled, NameProvider nameProvider) {
		return openInsertionPlanUserDialog(scenario, PlatformUI.getWorkbench().getDisplay(), PlatformUI.getWorkbench().getDisplay().getActiveShell(), forEvaluation, previousSettings, defaultSettings,
				displayOnlyIfOptionsEnabled, nameProvider);
	}

	public static UserSettings openInsertionPlanUserDialog(final LNGScenarioModel scenario, final Display display, final Shell shell, final boolean forEvaluation, final UserSettings previousSettings,
			final UserSettings defaultSettings, final boolean displayOnlyIfOptionsEnabled, NameProvider nameProvider) {
		boolean optionAdded = false;
		boolean enabledOptionAdded = false;

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ParametersItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		//
		// Fire up a dialog
		final ParameterModesDialog dialog = new ParameterModesDialog(shell) {
			@Override
			protected void configureShell(final org.eclipse.swt.widgets.Shell newShell) {

				super.configureShell(newShell);
				newShell.setText("Insertion Settings");
			}
		};

		final UserSettings copy = EcoreUtil.copy(previousSettings);

		// Reset disabled features
		resetDisabledFeatures(copy);

		if (!forEvaluation && nameProvider != null) {
			dialog.addNameOption(nameProvider.nameSuggestion, nameProvider.existingNames);
		}

		if (!forEvaluation) {
			// dialog.addOption(DataSection.Controls, null, editingDomain, "Number of Iterations", copy, defaultSettings, DataType.PositiveInt,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings(), ParametersPackage.eINSTANCE.getAnnealingSettings_Iterations());
			// optionAdded = true;

			// Check period optimisation is permitted
			if (SecurityUtils.getSubject().isPermitted(KnownFeatures.FEATURE_OPTIMISATION_PERIOD)) {
				final OptionGroup group = dialog.createGroup(DataSection.Controls, "Optimise period");
				final Option optStart = dialog.addOption(DataSection.Controls, group, editingDomain, "Start of (dd/mm/yyyy)", "", copy, defaultSettings, DataType.Date, SWTBOT_PERIOD_START,
						ParametersPackage.eINSTANCE.getUserSettings_PeriodStartDate());
				// Added a button. Had to extend Option and DataType classes.
				final Option optToday = dialog.addOption(DataSection.Controls, group, editingDomain, "Today", "Today", copy, defaultSettings, DataType.Button, SWTBOT_PERIOD_TODAY, null);
				optToday.setListener(new MouseAdapter() {

					@Override
					public void mouseDown(MouseEvent e) {
						if (copy != null) {
							copy.setPeriodStartDate(LocalDate.now());
						}
						if (defaultSettings != null) {
							defaultSettings.setPeriodStartDate(LocalDate.now());
						}
					}
				});
				// TODO set the optToday button size
				final Option optEnd = dialog.addOption(DataSection.Controls, group, editingDomain, "Up to start of (mm/yyyy)", "", copy, defaultSettings, DataType.MonthYear, SWTBOT_PERIOD_END,
						ParametersPackage.eINSTANCE.getUserSettings_PeriodEnd());
				final Option optThreeMonth = dialog.addOption(DataSection.Controls, group, editingDomain, "+3m", "Three months", copy, defaultSettings, DataType.Button, SWTBOT_PERIOD_THREE_MONTH,
						null);
				optThreeMonth.setListener(new MouseAdapter() {

					@Override
					public void mouseDown(MouseEvent e) {
							if (copy != null) {
								setPeriodEnd(copy);
							}
							if (defaultSettings != null) {
								setPeriodEnd(defaultSettings);
							}
						}
				});

				if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_PERIOD)) {
					optStart.enabled = false;
					optEnd.enabled = false;
				} else {
					enabledOptionAdded = true;
					final IObservableValue[] values = new IObservableValue[2];

					MultiValidator validator = new MultiValidator() {

						@Override
						protected IStatus validate() {
							LocalDate periodStart = null;
							if (values[0].getValue() instanceof LocalDate) {
								periodStart = (LocalDate) values[0].getValue();
							}
							YearMonth periodEnd = null;
							if (values[1].getValue() instanceof YearMonth) {
								periodEnd = (YearMonth) values[1].getValue();
							}
							if (periodStart != null && periodEnd != null) {
								if (periodEnd.atDay(1).isBefore(periodStart)) {
									return ValidationStatus.error("Period start must be before period end");
								}
							}
							return Status.OK_STATUS;
						}
					};

					dialog.addValidation(optStart, new IValidator() {

						@Override
						public IStatus validate(final Object value) {
							if (value instanceof LocalDate) {
								final LocalDate startDate = (LocalDate) value;
								if (startDate.getYear() < 2010) {
									return ValidationStatus.error("Invalid period start date");
								}
							}
							return Status.OK_STATUS;
						}
					});
					dialog.addValidation(optEnd, new IValidator() {

						@Override
						public IStatus validate(final Object value) {
							if (value instanceof YearMonth) {
								final YearMonth endDate = (YearMonth) value;
								if (endDate.getYear() < 2010) {
									return ValidationStatus.error("Invalid period end date");
								}
							}
							return Status.OK_STATUS;
						}
					});

					dialog.addValidationCallback(optStart, (v) -> values[0] = v);
					dialog.addValidationCallback(optEnd, (v) -> values[1] = v);

					dialog.addValidationStatusProvider(validator);
				}
				optionAdded = true;
			}

			// if (LicenseFeatures.isPermitted("features:optimisation-clean-state")) {
			// final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
			// choiceData.addChoice("Off", Boolean.FALSE);
			// choiceData.addChoice("On", Boolean.TRUE);
			// final Option option = dialog.addOption(DataSection.Toggles, null, editingDomain, "Clean State: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_CLEAN_STATE_PREFIX,
			// ParametersPackage.eINSTANCE.getUserSettings_CleanStateOptimisation());
			// optionAdded = true;
			// enabledOptionAdded = true;
			// dialog.addValidation(option, new IValidator() {
			//
			// @Override
			// public IStatus validate(final Object value) {
			// if (value instanceof UserSettings) {
			// final UserSettings userSettings = (UserSettings) value;
			// if (userSettings.isCleanStateOptimisation()) {
			// if (userSettings.getSimilarityMode() != SimilarityMode.OFF) {
			// return ValidationStatus.error("Similarity must be disabled with clean state optimisation");
			// }
			// }
			// }
			// return Status.OK_STATUS;
			// }
			// });
			// }
			//
			// {
			// final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
			// choiceData.addChoice("Off", Boolean.FALSE);
			// choiceData.addChoice("On", Boolean.TRUE);
			// dialog.addOption(DataSection.Toggles, null, editingDomain, "Shipping only: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_SHIPPING_ONLY_PREFIX,
			// ParametersPackage.eINSTANCE.getUserSettings_ShippingOnly());
			// optionAdded = true;
			// enabledOptionAdded = true;
			// }

			if (false && LicenseFeatures.isPermitted("features:trader-based-insertions")) {
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				choiceData.enabled = LicenseFeatures.isPermitted("features:trader-based-insertions");
				if (!choiceData.enabled) {
					// if not enabled make sure to set setting to false
					copy.setDualMode(false);
				}
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Dual mode: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_DUAL_MODE_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_DualMode());
				optionAdded = true;
				enabledOptionAdded = true;
			}
			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				choiceData.enabled = LicenseFeatures.isPermitted("features:charter-length");
				if (choiceData.enabled == false) {
					// if not enabled make sure to set setting to false
					copy.setWithCharterLength(false);
				}
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Charter Length: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_CHARTERLENGTH_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_WithCharterLength());
				optionAdded = true;
				enabledOptionAdded = true;
			}
			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Spot cargo markets: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_WITH_SPOT_CARGO_MARKETS_PREFIX,
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
			final Option gcoOption = dialog.addOption(DataSection.Toggles, null, editingDomain, "Generate charter outs: ", "", copy, defaultSettings, DataType.Choice, choiceData,
					SWTBOT_CHARTEROUTGENERATION_PREFIX, ParametersPackage.eINSTANCE.getUserSettings_GenerateCharterOuts());
			optionAdded = true;
			enabledOptionAdded = choiceData.enabled;
		}
		if (!forEvaluation) {
			{
				if (LicenseFeatures.isPermitted("features:optimisation-idle-days")) {
					final Option idleDays = dialog.addOption(DataSection.Toggles, null, editingDomain, "Netback idle day tolerance", "", copy, defaultSettings, DataType.PositiveInt, SWTBOT_IDLE_DAYS,
							ParametersPackage.eINSTANCE.getUserSettings_FloatingDaysLimit());
				}
			}
			// // if (SecurityUtils.getSubject().isPermitted("features:optimisation-similarity")) {
			// {
			//
			// final OptionGroup group = dialog.createGroup(DataSection.Controls, "Similarity");
			//
			// final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
			// choiceData.addChoice("Off", SimilarityMode.OFF);
			// choiceData.addChoice("Low", SimilarityMode.LOW);
			// choiceData.addChoice("Med", SimilarityMode.MEDIUM);
			// choiceData.addChoice("High", SimilarityMode.HIGH);
			// // choiceData.addChoice("All", SimilarityMode.ALL);
			//
			// choiceData.enabled = LicenseFeatures.isPermitted("features:optimisation-similarity");
			//
			// final Option option = dialog.addOption(DataSection.Controls, group, editingDomain, "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_SIMILARITY_PREFIX,
			// ParametersPackage.Literals.USER_SETTINGS__SIMILARITY_MODE);
			// optionAdded = true;
			// enabledOptionAdded = choiceData.enabled;
			//
			// }
			// {
			//
			// final OptionGroup group = dialog.createGroup(DataSection.Controls, "Action sets");
			//
			// final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
			// choiceData.addChoice("Off", Boolean.FALSE);
			// choiceData.addChoice("On", Boolean.TRUE);
			//
			// choiceData.enabled = LicenseFeatures.isPermitted("features:optimisation-actionset");
			//
			// final Option option = dialog.addOption(DataSection.Controls, group, editingDomain, " ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_ACTION_SET_PREFIX,
			// ParametersPackage.eINSTANCE.getUserSettings_BuildActionSets());
			// optionAdded = true;
			// dialog.addValidation(option, new IValidator() {
			//
			// @Override
			// public IStatus validate(final Object value) {
			// if (value instanceof UserSettings) {
			// final UserSettings userSettings = (UserSettings) value;
			// if (userSettings.isBuildActionSets()) {
			// if (userSettings.getSimilarityMode() == SimilarityMode.OFF) {
			// return ValidationStatus.error("Similarity must be enabled to use action sets");
			// }
			// final YearMonth periodStart = userSettings.getPeriodStart();
			// final YearMonth periodEnd = userSettings.getPeriodEnd();
			// if (periodStart != null && periodEnd != null) {
			// // 3 month window?
			// if (Months.between(periodStart, periodEnd) > 6) {
			// return ValidationStatus.error("Unable to run with Action Sets as the period range is greater than six months");
			// } else if (Months.between(periodStart, periodEnd) > 3 && userSettings.getSimilarityMode() == SimilarityMode.LOW) {
			// return ValidationStatus
			// .error("Unable to run with Action Sets as the period range is too long for the low similarity setting (max 3 Months). Please try medium or high");
			// }
			// } else {
			// return ValidationStatus.error("Unable to run with Action Sets as the period range is greater than six months");
			// }
			// }
			// }
			// return Status.OK_STATUS;
			// }
			// });
			// optionAdded = true;
			// enabledOptionAdded = choiceData.enabled;
			// }
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
		if (nameProvider != null) {
			nameProvider.nameSuggestion = dialog.getNameSuggestion();
		}
		return copy;
	}
	
	public static UserSettings promptForSandboxUserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final boolean promptUser, final boolean promptOnlyIfOptionsEnabled,
			NameProvider nameProvider) {
		UserSettings previousSettings = null;
		if (scenario != null) {
			previousSettings = scenario.getUserSettings();
		}
		return promptForSandboxUserSettings(scenario, forEvaluation, promptUser, promptOnlyIfOptionsEnabled, nameProvider, previousSettings);
		
	}
	
	public static UserSettings promptForSandboxUserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final boolean promptUser, final boolean promptOnlyIfOptionsEnabled,
			NameProvider nameProvider, UserSettings previousSettings) {
		final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
		if (previousSettings == null) {
			previousSettings = userSettings;
		}
		
		// Permit the user to override the settings object. Use the previous settings as the initial value
		if (promptUser) {
			previousSettings = openSandboxUserDialog(scenario, forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled, nameProvider);
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
	
	public static UserSettings openSandboxUserDialog(final LNGScenarioModel scenario, final boolean forEvaluation, final UserSettings previousSettings, final UserSettings defaultSettings,
			final boolean displayOnlyIfOptionsEnabled, NameProvider nameProvider) {
		return openSandboxUserDialog(scenario, PlatformUI.getWorkbench().getDisplay(), PlatformUI.getWorkbench().getDisplay().getActiveShell(), forEvaluation, previousSettings, defaultSettings,
				displayOnlyIfOptionsEnabled, nameProvider);
	}
	
	public static UserSettings openSandboxUserDialog(final LNGScenarioModel scenario, final Display display, final Shell shell, final boolean forEvaluation, final UserSettings previousSettings,
			final UserSettings defaultSettings, final boolean displayOnlyIfOptionsEnabled, NameProvider nameProvider) {
		boolean optionAdded = false;
		boolean enabledOptionAdded = false;
		
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ParametersItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		//
		// Fire up a dialog
		final ParameterModesDialog dialog = new ParameterModesDialog(shell) {
			@Override
			protected void configureShell(final org.eclipse.swt.widgets.Shell newShell) {
				
				super.configureShell(newShell);
				newShell.setText("Sandbox Settings");
			}
		};
		
		final UserSettings copy = EcoreUtil.copy(previousSettings);
		
		// Reset disabled features
		resetDisabledFeatures(copy);
		
		if (!forEvaluation && nameProvider != null) {
			dialog.addNameOption(nameProvider.nameSuggestion, nameProvider.existingNames);
		}
		
		if (!forEvaluation) {
			 
			if (false && LicenseFeatures.isPermitted("features:trader-based-insertions")) {
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				choiceData.enabled = LicenseFeatures.isPermitted("features:trader-based-insertions");
				if (!choiceData.enabled) {
					// if not enabled make sure to set setting to false
					copy.setDualMode(false);
				}
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Dual mode: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_DUAL_MODE_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_DualMode());
				optionAdded = true;
				enabledOptionAdded = true;
			}
			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				choiceData.enabled = LicenseFeatures.isPermitted("features:charter-length");
				if (choiceData.enabled == false) {
					// if not enabled make sure to set setting to false
					copy.setWithCharterLength(false);
				}
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Charter Length: ", "", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_CHARTERLENGTH_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_WithCharterLength());
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
			final Option gcoOption = dialog.addOption(DataSection.Toggles, null, editingDomain, "Generate charter outs: ", "", copy, defaultSettings, DataType.Choice, choiceData,
					SWTBOT_CHARTEROUTGENERATION_PREFIX, ParametersPackage.eINSTANCE.getUserSettings_GenerateCharterOuts());
			optionAdded = true;
			enabledOptionAdded = choiceData.enabled;
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
		if (nameProvider != null) {
			nameProvider.nameSuggestion = dialog.getNameSuggestion();
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

		final @Nullable LocalDate periodStart = userSettings.getPeriodStartDate();
		final @Nullable YearMonth periodEnd = userSettings.getPeriodEnd();

		final LocalDate periodStartOrDefault = getPeriodStartOrDefault(periodStart, lngScenarioModel);
		final YearMonth periodEndOrDefault = getPeriodEndOrDefault(periodEnd, lngScenarioModel);

		final SimilarityMode similarityMode = userSettings.getSimilarityMode();

		boolean shouldUseRestartingLSO = false;

		switch (similarityMode) {
		case ALL:
			constraintAndFitnessSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.LOW, periodStartOrDefault, periodEndOrDefault));
			shouldUseRestartingLSO = true;
			userSettings.setBuildActionSets(false);
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
		if (userSettings.isSetPeriodStartDate() && userSettings.isSetPeriodEnd()) {
			epochLength = EPOCH_LENGTH_PERIOD;
		} else {
			epochLength = EPOCH_LENGTH_FULL;
		}
		if (userSettings.isCleanStateOptimisation()) {
			final CleanStateOptimisationStage stage = ScenarioUtils.createDefaultCleanStateParameters(EcoreUtil.copy(constraintAndFitnessSettings));
			stage.getAnnealingSettings().setEpochLength(epochLength);
			plan.getStages().add(stage);
			if (!userSettings.isNominalADP()) {
				plan.getStages().add(ParametersFactory.eINSTANCE.createResetInitialSequencesStage());
			}
		}

		if (!userSettings.isNominalADP()) {

			if (similarityMode != SimilarityMode.ALL) {
				final LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(EcoreUtil.copy(constraintAndFitnessSettings));
				stage.getAnnealingSettings().setEpochLength(epochLength);
				stage.getAnnealingSettings().setRestarting(shouldUseRestartingLSO);
				plan.getStages().add(stage);
			} else {
				final MultipleSolutionSimilarityOptimisationStage stage = ScenarioUtils.createDefaultMultipleSolutionSimilarityParameters(EcoreUtil.copy(constraintAndFitnessSettings));
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
		}
		return LNGScenarioRunnerUtils.createExtendedSettings(plan);
	}

	private static boolean shouldCreateDefaultSaveStage(@NonNull UserSettings userSettings) {
		if (userSettings.getSimilarityMode() != SimilarityMode.ALL) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean shouldDisableActionSets(final SimilarityMode mode, final LocalDate periodStart, final YearMonth periodEnd) {
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

	public static SimilaritySettings createSimilaritySettings(final SimilarityMode mode, final LocalDate periodStart, final YearMonth periodEnd) {
		if (periodStart == null || periodEnd == null || mode == null || mode == SimilarityMode.OFF) {
			return ScenarioUtils.createOffSimilaritySettings();
		} else {
			return SimilarityUIParameters.getSimilaritySettings(mode, periodStart, periodEnd);
		}
	}

	private static void resetDisabledFeatures(@NonNull final UserSettings copy) {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_ACTIONSET)) {
			copy.setBuildActionSets(false);
		}
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_PERIOD)) {
			copy.unsetPeriodStartDate();
			copy.unsetPeriodEnd();
		}
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_CHARTER_OUT_GENERATION)) {
			copy.setGenerateCharterOuts(false);
		}
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_SIMILARITY)) {
			copy.setSimilarityMode(SimilarityMode.OFF);
		}
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CHARTER_LENGTH)) {
			copy.setWithCharterLength(false);
		}
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRADER_BASED_INSERIONS)) {
			copy.setDualMode(false);
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
		if (from.isSetPeriodStartDate() == false || from.getPeriodStartDate() == null) {
			to.unsetPeriodStartDate();
		} else {
			to.setPeriodStartDate(from.getPeriodStartDate());
		}
		if (from.isSetPeriodEnd() == false || from.getPeriodEnd() == null) {
			to.unsetPeriodEnd();
		} else {
			to.setPeriodEnd(from.getPeriodEnd());
		}

		to.setShippingOnly(from.isShippingOnly());
		to.setGenerateCharterOuts(from.isGenerateCharterOuts());
		to.setWithSpotCargoMarkets(from.isWithSpotCargoMarkets());
		to.setWithCharterLength(from.isWithCharterLength());
		to.setAdpOptimisation(from.isAdpOptimisation());
		to.setCleanStateOptimisation(from.isCleanStateOptimisation());
		to.setNominalADP(from.isNominalADP());
		to.setDualMode(from.isDualMode());

		if (from.getSimilarityMode() != null) {
			to.setSimilarityMode(from.getSimilarityMode());
		}
		to.setBuildActionSets(from.isBuildActionSets());

		to.setFloatingDaysLimit(from.getFloatingDaysLimit());
	}

	public static boolean checkUserSettings(@NonNull final UserSettings to, final boolean quiet) {
		resetDisabledFeatures(to);

		if (!to.isAdpOptimisation()) {
			// Clean state is only valid with ADP mode
			to.setCleanStateOptimisation(false);
			to.setNominalADP(false);
		}
		if (!to.isCleanStateOptimisation()) {
			to.setNominalADP(false);
		}

		// Turn off if settings are not nice
		if (to.isBuildActionSets()) {

			String actionSetErrorMessage = null;

			if (to.getSimilarityMode() == SimilarityMode.OFF) {
				to.setBuildActionSets(false);
				actionSetErrorMessage = "Unable to run with Action Sets as similarity is turned off";
			}
			if (to.isBuildActionSets()) {
				if (to.getPeriodStartDate() == null && to.getPeriodEnd() == null) {
					actionSetErrorMessage = "Unable to run with Action Sets as there is no period range set";
					to.setBuildActionSets(false);
				} else {
					final LocalDate periodStart = to.getPeriodStartDate();
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
					return o1.getSchedulingTimeWindow().getEndWithFlex().compareTo(o2.getSchedulingTimeWindow().getEndWithFlex()) * -1;
				}
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
			Collections.sort(loadSlots, new Comparator<LoadSlot>() {

				@Override
				public int compare(final LoadSlot o1, final LoadSlot o2) {
					return o1.getSchedulingTimeWindow().getStart().compareTo(o2.getSchedulingTimeWindow().getStart());
				}
			});
			if (loadSlots.isEmpty()) {
				// FIXME: (SG) This is not always a good idea... I have seen arrays created to hold data from 2000 to now - and this can take a long time.
				return LocalDate.of(2000, 1, 1);
			}
			return loadSlots.get(0).getSchedulingTimeWindow().getStart().toLocalDate();
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

	public static boolean validateScenario(final IScenarioDataProvider scenarioDataProvider, final boolean optimising, final boolean displayErrors, boolean relaxedValidation) {
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

		final MMXRootObject root = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation);
			return helper.runValidation(validator, extraContext, root);
		});

		if (status == null) {
			return false;
		}

		if (!status.isOK()) {

			if (optimising || status.getSeverity() == IStatus.ERROR) {

				// See if this command was executed in the UI thread - if so fire up the dialog box.
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
	
	private static void setPeriodEnd(final UserSettings copy) {
		LocalDate temp = LocalDate.now();
		if (copy.getPeriodStartDate() != null) {
			temp = copy.getPeriodStartDate();
		}
		copy.setPeriodEnd(YearMonth.from(temp.plusMonths(3)));
	}
}

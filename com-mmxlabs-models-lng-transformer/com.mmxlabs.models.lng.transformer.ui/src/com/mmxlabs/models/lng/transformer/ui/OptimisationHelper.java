/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.parameters.ActionPlanSettings;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.provider.ParametersItemProviderAdapterFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
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
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;

public final class OptimisationHelper {

	public static final String PARAMETER_MODE_CUSTOM = "Custom";

	private static final Logger log = LoggerFactory.getLogger(OptimisationHelper.class);

	public static final int EPOCH_LENGTH_PERIOD = 300;
	public static final int EPOCH_LENGTH_FULL = 900;

	// Note: SWTBOT ids are linked to the display string for radio buttons
	public static final String SWTBOT_SHIPPING_ONLY_PREFIX = "swtbot.shippingonly";
	public static final String SWTBOT_SHIPPING_ONLY_ON = SWTBOT_SHIPPING_ONLY_PREFIX + ".On";
	public static final String SWTBOT_SHIPPING_ONLY_OFF = SWTBOT_SHIPPING_ONLY_PREFIX + ".Off";

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

	public static Object evaluateScenarioInstance(@NonNull final IEclipseJobManager jobManager, @NonNull final ScenarioInstance instance, @Nullable final String parameterMode,
			final boolean promptForOptimiserSettings, final boolean optimising, final String lockName, final boolean promptOnlyIfOptionsEnabled) {

		final IScenarioService service = instance.getScenarioService();
		if (service == null) {
			return null;
		}

		// While we only keep the reference for the duration of this method call, the two current concrete implementations of IJobControl will obtain a ModelReference
		try (final ModelReference modelRefence = instance.getReference()) {
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
				final Pair<UserSettings, OptimiserSettings> optimiserSettings = getOptimiserSettings(root, !optimising, parameterMode, promptForOptimiserSettings, promptOnlyIfOptionsEnabled);

				if (optimiserSettings == null || optimiserSettings.getSecond() == null) {
					return null;
				}
				final EditingDomain editingDomain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
				if (editingDomain != null) {
					final CompoundCommand cmd = new CompoundCommand("Update settings");
					cmd.append(SetCommand.create(editingDomain, root, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), optimiserSettings.getFirst()));
					editingDomain.getCommandStack().execute(cmd);
				}
				// Pair<UserSettings, OptimiserSettings>

				final ScenarioLock scenarioLock = instance.getLock(lockName);
				if (scenarioLock.awaitClaim()) {
					IJobControl control = null;
					IJobDescriptor job = null;
					try {
						// create a new job
						job = new LNGSchedulerJobDescriptor(instance.getName(), instance, optimiserSettings.getSecond(), optimising);

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
							display.asyncExec(new Runnable() {

								@Override
								public void run() {
									final String message = StringEscaper.escapeUIString(ex.getMessage());
									MessageDialog.openError(display.getActiveShell(), "Error starting optimisation", "An error occured. See Error Log for more details.\n" + message);
								}

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

		final IValidationService helper = Activator.getDefault().getValidationService();
		final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(root, false);

		final IStatus status = helper.runValidation(validator, extraContext, Collections.singleton(root));

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

	@Nullable
	public static Pair<UserSettings, OptimiserSettings> getOptimiserSettings(@NonNull final LNGScenarioModel scenario, final boolean forEvaluation, @Nullable final String parameterMode,
			final boolean promptUser, final boolean promptOnlyIfOptionsEnabled) {

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

			previousSettings = openUserDialog(forEvaluation, previousSettings, userSettings, promptOnlyIfOptionsEnabled);
		}

		if (previousSettings == null) {
			return null;
		}

		// Only merge across specific fields - not all of them. This permits additions to the default settings to pass through to the scenario.
		mergeFields(previousSettings, userSettings);

		if (!checkUserSettings(userSettings, false)) {
			return null;
		}

		final OptimiserSettings optimiserSettings = transformUserSettings(userSettings, parameterMode, scenario);

		// Tmp hack - need to update customiser API to avoid needing to back-apply
		userSettings.setGenerateCharterOuts(optimiserSettings.isGenerateCharterOuts());
		userSettings.setBuildActionSets(optimiserSettings.isBuildActionSets());
		userSettings.setShippingOnly(optimiserSettings.isShippingOnly());

		return new Pair<UserSettings, OptimiserSettings>(userSettings, optimiserSettings);
	}

	public static UserSettings openUserDialog(final boolean forEvaluation, final UserSettings previousSettings, final UserSettings defaultSettings, final boolean displayOnlyIfOptionsEnabled) {
		return openUserDialog(PlatformUI.getWorkbench().getDisplay(), PlatformUI.getWorkbench().getDisplay().getActiveShell(), forEvaluation, previousSettings, defaultSettings,
				displayOnlyIfOptionsEnabled);
	}

	public static UserSettings openUserDialog(Display display, Shell shell, final boolean forEvaluation, final UserSettings previousSettings, final UserSettings defaultSettings,
			final boolean displayOnlyIfOptionsEnabled) {
		boolean optionAdded = false;
		boolean enabledOptionAdded = false;

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ParametersItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		//
		// Fire up a dialog
		final ParameterModesDialog dialog = new ParameterModesDialog(shell) {
			protected void configureShell(org.eclipse.swt.widgets.Shell newShell) {

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

			{
				final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
				choiceData.addChoice("Off", Boolean.FALSE);
				choiceData.addChoice("On", Boolean.TRUE);
				dialog.addOption(DataSection.Toggles, null, editingDomain, "Shipping only: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_SHIPPING_ONLY_PREFIX,
						ParametersPackage.eINSTANCE.getUserSettings_ShippingOnly());
				optionAdded = true;
				enabledOptionAdded = true;
			}
			// dialog.addOption(DataSection.Main, null, editingDomian, "Shipping Only Optimisation", copy, defaultSettings, DataType.Boolean,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_ShippingOnly());
		}
		// if (SecurityUtils.getSubject().isPermitted("features:optimisation-charter-out-generation")) {
		{
			// dialog.addOption(DataSection.Main, null, editingDomian, "Generate Charter Outs", copy, defaultSettings, DataType.Choice,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_GenerateCharterOuts());

			final ParameterModesDialog.ChoiceData choiceData = new ParameterModesDialog.ChoiceData();
			choiceData.addChoice("Off", Boolean.FALSE);
			choiceData.addChoice("On", Boolean.TRUE);

			choiceData.enabled = LicenseFeatures.isPermitted("features:optimisation-charter-out-generation");
			// dialog.addOption(DataSection.Main, null, editingDomian, "Similarity", copy, defaultSettings, DataType.Choice, choiceData,
			// ParametersPackage.eINSTANCE.getOptimiserSettings_Range(), ParametersPackage.eINSTANCE.getOptimisationRange_OptimiseAfter());
			dialog.addOption(DataSection.Toggles, null, editingDomain, "Generate charter outs: ", copy, defaultSettings, DataType.Choice, choiceData, SWTBOT_CHARTEROUTGENERATION_PREFIX,
					ParametersPackage.eINSTANCE.getUserSettings_GenerateCharterOuts());
			optionAdded = true;
			enabledOptionAdded = choiceData.enabled;
		}

		if (!forEvaluation) {
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

	public static OptimiserSettings transformUserSettings(@NonNull final UserSettings userSettings, @Nullable final String parameterMode, LNGScenarioModel lngScenarioModel) {

		final OptimiserSettings optimiserSettings = ScenarioUtils.createDefaultSettings();

		// Copy across params
		if (userSettings.isSetPeriodStart()) {
			optimiserSettings.getRange().setOptimiseAfter(userSettings.getPeriodStart());
		}
		if (userSettings.isSetPeriodEnd()) {
			optimiserSettings.getRange().setOptimiseBefore(userSettings.getPeriodEnd());
		}

		optimiserSettings.setBuildActionSets(userSettings.isBuildActionSets());
		optimiserSettings.setGenerateCharterOuts(userSettings.isGenerateCharterOuts());
		optimiserSettings.setShippingOnly(userSettings.isShippingOnly());

		Objective similarityObjective = findObjective(SimilarityFitnessCoreFactory.NAME, optimiserSettings);
		if (similarityObjective == null) {
			similarityObjective = ParametersFactory.eINSTANCE.createObjective();
			similarityObjective.setName(SimilarityFitnessCoreFactory.NAME);
			optimiserSettings.getObjectives().add(similarityObjective);
		}

		similarityObjective.setEnabled(true);
		similarityObjective.setWeight(1.0);

		YearMonth periodStart = userSettings.getPeriodStart();
		YearMonth periodEnd = userSettings.getPeriodEnd();
		
		YearMonth periodStartOrDefault = getPeriodStartOrDefault(periodStart, lngScenarioModel);
		YearMonth periodEndOrDefault = getPeriodEndOrDefault(periodEnd, lngScenarioModel);

		SimilarityMode similarityMode = userSettings.getSimilarityMode();

		switch (similarityMode) {
		case ALL:
			assert false;
			break;
		case HIGH:
			optimiserSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.HIGH, periodStartOrDefault, periodEndOrDefault));
			optimiserSettings.getAnnealingSettings().setRestarting(true);
			if (shouldDisableActionSets(SimilarityMode.HIGH, periodStart, periodEnd)) {
				optimiserSettings.setBuildActionSets(false);
			}
			break;
		case LOW:
			optimiserSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.LOW, periodStartOrDefault, periodEndOrDefault));
			optimiserSettings.getAnnealingSettings().setRestarting(false);
			if (shouldDisableActionSets(SimilarityMode.LOW, periodStart, periodEnd)) {
				optimiserSettings.setBuildActionSets(false);
			}
			break;
		case MEDIUM:
			optimiserSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.MEDIUM, periodStartOrDefault, periodEndOrDefault));
			optimiserSettings.getAnnealingSettings().setRestarting(false);
			if (shouldDisableActionSets(SimilarityMode.MEDIUM, periodStart, periodEnd)) {
				optimiserSettings.setBuildActionSets(false);
			}
			break;
		case OFF:
			optimiserSettings.setSimilaritySettings(createSimilaritySettings(SimilarityMode.OFF, periodStartOrDefault, periodEndOrDefault));
			optimiserSettings.getAnnealingSettings().setRestarting(false);
			optimiserSettings.setBuildActionSets(false);
			break;
		default:
			assert false;
			break;
		}

		if (optimiserSettings.isBuildActionSets() && periodStart != null && periodEnd != null) {
			ActionPlanSettings apSettings = ActionPlanUIParameters.getActionPlanSettings(similarityMode, periodStart, periodEnd);
			optimiserSettings.setActionPlanSettings(apSettings);
		} else {
			optimiserSettings.setActionPlanSettings(ActionPlanUIParameters.getDefaultSettings());
		}

		// change epoch length
		// TODO: make this better!
		if (userSettings.isSetPeriodStart() && userSettings.isSetPeriodEnd()) {
			optimiserSettings.getAnnealingSettings().setEpochLength(EPOCH_LENGTH_PERIOD);
		} else {
			optimiserSettings.getAnnealingSettings().setEpochLength(EPOCH_LENGTH_FULL);
		}

		Activator activator = Activator.getDefault();
		if (activator != null) {
			final IParameterModesRegistry parameterModesRegistry = activator.getParameterModesRegistry();

			if (parameterMode != null) {

				if (PARAMETER_MODE_CUSTOM.equals(parameterMode)) {
					// Nothing...
				} else {

					final IParameterModeCustomiser customiser = parameterModesRegistry.getCustomiser(parameterMode);
					if (customiser != null) {
						customiser.customise(optimiserSettings);
					}
				}
			}

			final Collection<IParameterModeExtender> extenders = parameterModesRegistry.getExtenders();
			if (extenders != null) {
				for (final IParameterModeExtender extender : extenders) {
					extender.extend(optimiserSettings, parameterMode);
				}
			}
		}

		return optimiserSettings;
	}

	private static boolean shouldDisableActionSets(SimilarityMode mode, YearMonth periodStart, YearMonth periodEnd) {
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

	private static SimilaritySettings createSimilaritySettings(SimilarityMode mode, YearMonth periodStart, YearMonth periodEnd) {
		if (periodStart == null || periodEnd == null || mode == null) {
			return SimilarityUIParameters.createOffSimilaritySettings();
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
	private static void mergeFields(@NonNull final UserSettings from, @NonNull final UserSettings to) {

		resetDisabledFeatures(from);

		// TODO: replace all this ugly code by a list of EStructuralFeatures and loop through
		// them doing the right thing

		if (from.isSetPeriodStart() == false) {
			to.unsetPeriodStart();
		} else {
			to.setPeriodStart(from.getPeriodStart());
		}
		if (from.isSetPeriodEnd() == false) {
			to.unsetPeriodEnd();
		} else {
			to.setPeriodEnd(from.getPeriodEnd());
		}

		to.setShippingOnly(from.isShippingOnly());
		to.setGenerateCharterOuts(from.isGenerateCharterOuts());

		if (from.getSimilarityMode() != null) {
			to.setSimilarityMode(from.getSimilarityMode());
		}
		to.setBuildActionSets(from.isBuildActionSets());
	}

	public static boolean checkUserSettings(@NonNull final UserSettings to, boolean quiet) {
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

	private static Objective findObjective(final String objective, final OptimiserSettings settings) {
		for (final Objective o : settings.getObjectives()) {
			if (SimilarityFitnessCoreFactory.NAME.equals(o.getName())) {
				return o;
			}
		}
		return null;
	}
	
	private static YearMonth getPeriodEndOrDefault(YearMonth periodEnd, LNGScenarioModel scenario) {
		if (periodEnd != null) {
			return periodEnd;
		} else if (scenario == null) {
			return periodEnd;
		} else {
			List<LoadSlot> loadSlots = new LinkedList<>(scenario.getCargoModel().getLoadSlots());
			Collections.sort(loadSlots, new Comparator<LoadSlot>() {

				@Override
				public int compare(LoadSlot o1, LoadSlot o2) {
					return o1.getWindowEndWithSlotOrPortTimeWithFlex().compareTo(o2.getWindowEndWithSlotOrPortTimeWithFlex())*-1;
				}
			});
			return YearMonth.of(loadSlots.get(0).getWindowStartWithSlotOrPortTime().getYear(), loadSlots.get(0).getWindowStartWithSlotOrPortTime().getMonth());
		}
	}

	private static YearMonth getPeriodStartOrDefault(YearMonth periodStart, LNGScenarioModel scenario) {
		if (periodStart != null) {
			return periodStart;
		} else if (scenario == null) {
			return periodStart;
		} else {
			List<LoadSlot> loadSlots = new LinkedList<>(scenario.getCargoModel().getLoadSlots());
			Collections.sort(loadSlots, new Comparator<LoadSlot>() {

				@Override
				public int compare(LoadSlot o1, LoadSlot o2) {
					return o1.getWindowStartWithSlotOrPortTime().compareTo(o2.getWindowStartWithSlotOrPortTime());
				}
			});
			return YearMonth.of(loadSlots.get(0).getWindowStartWithSlotOrPortTime().getYear(), loadSlots.get(0).getWindowStartWithSlotOrPortTime().getMonth());
		}
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.provider.ParametersItemProviderAdapterFactory;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.DataSection;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.DataType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.models.util.StringEscaper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public final class OptimisationHelper {

	public static final String PARAMETER_MODE_CUSTOM = "Custom";

	private static final Logger log = LoggerFactory.getLogger(OptimisationHelper.class);

	public static Object evaluateScenarioInstance(final IEclipseJobManager jobManager, final ScenarioInstance instance, final String parameterMode, final boolean promptForOptimiserSettings,
			final boolean optimising, final String k) {

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
				final OptimiserSettings optimiserSettings = getOptimiserSettings(root, !optimising, parameterMode, promptForOptimiserSettings);
				if (optimiserSettings == null) {
					return null;
				}

				final ScenarioLock scenarioLock = instance.getLock(k);
				if (scenarioLock.awaitClaim()) {
					IJobControl control = null;
					IJobDescriptor job = null;
					try {
						// create a new job
						job = new LNGSchedulerJobDescriptor(instance.getName(), instance, optimiserSettings, optimising);

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

	public static OptimiserSettings getOptimiserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, final String parameterMode, final boolean promptUser) {

		OptimiserSettings previousSettings = null;
		if (scenario != null) {
			final LNGPortfolioModel portfolioModel = scenario.getPortfolioModel();
			if (portfolioModel != null) {
				previousSettings = portfolioModel.getParameters();
			}
		}

		final OptimiserSettings defaultSettings = ScenarioUtils.createDefaultSettings();
		if (previousSettings == null) {
			previousSettings = defaultSettings;
		}

		if (previousSettings == null) {
			return null;
		}

		final IParameterModesRegistry parameterModesRegistry = Activator.getDefault().getParameterModesRegistry();

		if (parameterMode != null) {

			if (PARAMETER_MODE_CUSTOM.equals(parameterMode)) {
				// Nothing...
			} else {

				final IParameterModeCustomiser customiser = parameterModesRegistry.getCustomiser(parameterMode);
				if (customiser != null) {
					customiser.customise(previousSettings);
				}
			}
		}

		// Permit the user to override the settings object. Use the previous settings as the initial value
		if (promptUser) {

			final Display display = Display.getDefault();

			final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			adapterFactory.addAdapterFactory(new ParametersItemProviderAdapterFactory());
			adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
			final EditingDomain editingDomian = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

			// Fire up a dialog
			final ParameterModesDialog dialog = new ParameterModesDialog(display.getActiveShell());

			final OptimiserSettings copy = EcoreUtil.copy(previousSettings);

			dialog.addOption(DataSection.Main, editingDomian, "Shipping Only Optimisation", copy, defaultSettings, DataType.Boolean, ParametersPackage.eINSTANCE.getOptimiserSettings_ShippingOnly());
			dialog.addOption(DataSection.Main, editingDomian, "Generate Charter Outs", copy, defaultSettings, DataType.Boolean, ParametersPackage.eINSTANCE.getOptimiserSettings_GenerateCharterOuts());

			if (!forEvaluation) {
				dialog.addOption(DataSection.Advanced, editingDomian, "Number of Iterations", copy, defaultSettings, DataType.PositiveInt,
						ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings(), ParametersPackage.eINSTANCE.getAnnealingSettings_Iterations());
				dialog.addOption(DataSection.Advanced, editingDomian, "Start Date", copy, defaultSettings, DataType.Date,
						ParametersPackage.eINSTANCE.getOptimiserSettings_Range(), ParametersPackage.eINSTANCE.getOptimisationRange_OptimiseAfter());
				dialog.addOption(DataSection.Advanced, editingDomian, "End Date", copy, defaultSettings, DataType.Date,
						ParametersPackage.eINSTANCE.getOptimiserSettings_Range(), ParametersPackage.eINSTANCE.getOptimisationRange_OptimiseAfter());
			}

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

			previousSettings = copy;
		}

		// Only merge across specific fields - not all of them. This permits additions to the default settings to pass through to the scenario.
		mergeFields(previousSettings, defaultSettings);

		final Collection<IParameterModeExtender> extenders = parameterModesRegistry.getExtenders();
		if (extenders != null) {
			for (final IParameterModeExtender extender : extenders) {
				extender.extend(defaultSettings, parameterMode);
			}
		}

		return defaultSettings;
	}

	private static void mergeFields(final OptimiserSettings from, final OptimiserSettings to) {

		to.getAnnealingSettings().setIterations(from.getAnnealingSettings().getIterations());
		to.setShippingOnly(from.isShippingOnly());
		to.setGenerateCharterOuts(from.isGenerateCharterOuts());
	}
}

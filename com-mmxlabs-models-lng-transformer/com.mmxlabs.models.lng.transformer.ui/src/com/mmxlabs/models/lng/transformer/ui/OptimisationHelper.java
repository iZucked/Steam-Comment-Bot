package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;
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
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.DataSection;
import com.mmxlabs.models.lng.transformer.ui.parameters.ParameterModesDialog.DataType;
import com.mmxlabs.models.lng.transformer.util.ScenarioUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public final class OptimisationHelper {

	public static final String PARAMETER_MODE_CUSTOM = "Custom";

	private static final Logger log = LoggerFactory.getLogger(OptimisationHelper.class);

	public static Object evaluateScenarioInstance(final IEclipseJobManager jobManager, final ScenarioInstance instance, String parameterMode, final boolean promptForOptimiserSettings,
			final boolean optimising, final String k) {

		final IScenarioService service = instance.getScenarioService();
		if (service != null) {
			try {
				final EObject object = service.load(instance);

				if (object instanceof LNGScenarioModel) {
					final LNGScenarioModel root = (LNGScenarioModel) object;

					final OptimiserSettings optimiserSettings = getOptimiserSettings(root, !optimising, parameterMode, promptForOptimiserSettings);
					if (optimiserSettings == null) {
						return null;
					}

					final String uuid = instance.getUuid();

					IJobDescriptor job = jobManager.findJobForResource(uuid);
					if (job == null) {
						// create a new job
						job = new LNGSchedulerJobDescriptor(instance.getName(), instance, optimiserSettings, optimising, k);
					}

					IJobControl control = jobManager.getControlForJob(job);
					// If there is a job, but it is terminated, then we need to create a new one
					if ((control != null) && ((control.getJobState() == EJobState.CANCELLED) || (control.getJobState() == EJobState.COMPLETED))) {
						jobManager.removeJob(job);
						control = null;
						job = new LNGSchedulerJobDescriptor(instance.getName(), instance, optimiserSettings, optimising, k);

					}

					if (control == null) {

						// New optimisation, so check there are no validation errors.
						if (!validateScenario(root)) {
							return null;
						}

						jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
						control = jobManager.submitJob(job, uuid);
					}

					// if (!jobManager.getSelectedJobs().contains(job)) {
					// // Clean up when job is removed from manager
					// jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
					// control = jobManager.submitJob(job, uuid);
					// }

					if (control.getJobState() == EJobState.CREATED) {
						try {
							final IJobDescriptor desc = job;
							// Add listener to unlock scenario when it stops optimising
							control.addListener(new IJobControlListener() {

								@Override
								public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {

									if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
										// instance.setLocked(false);
										instance.getLock(k).release();
										jobManager.removeJob(desc);
										return false;
									}
									return true;
								}

								@Override
								public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
									return true;
								}
							});
							// Set initial state.
							// instance.setLocked(true);
							instance.getLock(k).awaitClaim();
							control.prepare();
							control.start();
						} catch (final Throwable ex) {
							log.error(ex.getMessage(), ex);
							control.cancel();
							// instance.setLocked(false);
							instance.getLock(k).release();

							final Display display = Display.getDefault();
							if (display != null) {
								display.asyncExec(new Runnable() {

									@Override
									public void run() {
										MessageDialog.openError(display.getActiveShell(), "Error starting optimisation", "An error occured. See Error Log for more details.\n" + ex.getMessage());
									}
								});
							}
						}
						// Resume if paused
					} else if (control.getJobState() == EJobState.PAUSED) {
						// instance.setLocked(true);
						instance.getLock(k).awaitClaim();
						control.resume();
					} else {
						// Add listener to unlock scenario when it stops optimising
						control.addListener(new IJobControlListener() {

							@Override
							public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {

								if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
									// instance.setLocked(false);
									instance.getLock(k).release();
									return false;
								}
								return true;
							}

							@Override
							public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
								return true;
							}
						});
						// instance.setLocked(true);
						instance.getLock(k).awaitClaim();
						try {
							control.start();
						} catch (final Throwable t) {
							log.error(t.getMessage(), t);
							instance.getLock(k).release();
							control.cancel();

							final Display display = Display.getDefault();
							if (display != null) {
								display.asyncExec(new Runnable() {

									@Override
									public void run() {
										MessageDialog.openError(display.getActiveShell(), "Error starting optimisation", "An error occured. See Error Log for more details.\n" + t.getMessage());
									}
								});
							}
						}
					}
				}
			} catch (final IOException e) {
				log.error(e.getMessage(), e);
			}

		}

		return null;
	}

	public static boolean validateScenario(final MMXRootObject root) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().endsWith(".base")) {
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

	public static OptimiserSettings getOptimiserSettings(final LNGScenarioModel scenario, final boolean forEvaluation, String parameterMode, final boolean promptUser) {

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

		IParameterModesRegistry parameterModesRegistry = Activator.getDefault().getParameterModesRegistry();

		if (parameterMode != null) {

			if (PARAMETER_MODE_CUSTOM.equals(parameterMode)) {
				// Nothing...
			} else {

				IParameterModeCustomiser customiser = parameterModesRegistry.getCustomiser(parameterMode);
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

		return defaultSettings;
	}

	private static void mergeFields(final OptimiserSettings from, final OptimiserSettings to) {

		to.getAnnealingSettings().setIterations(from.getAnnealingSettings().getIterations());
		to.setShippingOnly(from.isShippingOnly());
		to.setGenerateCharterOuts(from.isGenerateCharterOuts());
	}
}

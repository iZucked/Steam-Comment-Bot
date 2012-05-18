package com.mmxlabs.scenario.service.ui.editing;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.internal.Activator;

/**
 * Helper class to save all the dirty scenarios (or not). Envisaged to be called from a {@link WorkbenchAdvisor} shutdown hook.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioServiceSaveHook {

	private static final Logger log = LoggerFactory.getLogger(ScenarioServiceSaveHook.class);

	public static List<ScenarioInstance> gatherDirtyScenarios() {
		final Map<String, WeakReference<IScenarioService>> scenarioServices = Activator.getDefault().getScenarioServices();

		final List<ScenarioInstance> dirtyScenarios = new LinkedList<ScenarioInstance>();

		for (final WeakReference<IScenarioService> serviceRef : scenarioServices.values()) {

			final IScenarioService service = serviceRef.get();
			if (service != null) {

				final Iterator<EObject> itr = service.getServiceModel().eAllContents();
				while (itr.hasNext()) {
					final EObject eObj = itr.next();
					if (eObj instanceof ScenarioInstance) {
						final ScenarioInstance scenarioInstance = (ScenarioInstance) eObj;
						final Map<Class<?>, Object> adapters = scenarioInstance.getAdapters();
						if (adapters != null) {
							final BasicCommandStack stack = (BasicCommandStack) adapters.get(BasicCommandStack.class);
							if (stack != null && stack.isSaveNeeded()) {
								dirtyScenarios.add(scenarioInstance);
							}
						}
					}
				}
			}
		}
		return dirtyScenarios;
	}

	public static boolean saveScenarioService() {

		final List<ScenarioInstance> dirtyScenarios = gatherDirtyScenarios();

		if (dirtyScenarios.isEmpty()) {
			return true;
		}

		final MessageDialog d = new MessageDialog(Display.getDefault().getActiveShell(), "Save Scenarios", null, String.format("There are %d unsaved scenarios - do you wish to save them?",
				dirtyScenarios.size()), MessageDialog.QUESTION, new String[] { "&Save All", "&Discard All", "&Cancel" }, 0);
		final int ret = d.open();
		if (ret == 2) {
			// Cancel
			return false;
		} else if (ret == 1) {
			// Discard
			return true;
		}
		// Save All

		final boolean[] success = new boolean[1];
		try {
			final ProgressMonitorDialog p = new ProgressMonitorDialog(null);

			final IRunnableWithProgress runnable = new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) {

					monitor.beginTask("Saving dirty scenarios", dirtyScenarios.size());
					try {

						for (final ScenarioInstance instance : dirtyScenarios) {
							monitor.setTaskName("Saving: " + instance.getName());
							instance.getScenarioService().save(instance);
							monitor.worked(1);
						}
						success[0] = true;
					} catch (final Exception e) {
						log.error(e.getMessage(), e);
						success[0] = false;
					} finally {
						monitor.done();
					}
				}
			};

			p.run(true, false, runnable);
		} catch (final InvocationTargetException e) {
			log.error(e.getMessage(), e);
		} catch (final InterruptedException e) {
			log.error(e.getMessage(), e);
		}

		return success[0];
	}
}

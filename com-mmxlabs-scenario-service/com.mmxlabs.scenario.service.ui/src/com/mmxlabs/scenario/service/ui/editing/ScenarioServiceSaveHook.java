/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
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

		final ITreeContentProvider contentProvider = new ITreeContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean hasChildren(final Object element) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Object getParent(final Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				// TODO Auto-generated method stub
				return dirtyScenarios.toArray();
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		final ILabelProvider labelProvider = new ILabelProvider() {

			@Override
			public void removeListener(final ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(final Object element, final String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void addListener(final ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getText(final Object element) {
				final ScenarioInstance scenario = (ScenarioInstance) element;
				return scenario.getName();
			}

			@Override
			public Image getImage(final Object element) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		final SelectionDialog sd = new ListSelectionDialog(Display.getDefault().getActiveShell(), dirtyScenarios, contentProvider, labelProvider, "Save unsaved scenarios?");
		sd.setInitialSelections(dirtyScenarios.toArray());
		final int ret = sd.open();

		final Object[] scenariosToSave = sd.getResult();

		if (ret == 2 || ret == -1) {
			// Cancel
			return false;
		}

		if (scenariosToSave == null) {
			// Not sure why this could happen, but it did once...
			// SG - 2013-02-18
			return false;
		}

		/*
		 * else if (ret == 1) { // Discard return true; } // Save All
		 */

		final Set<ScenarioInstance> ignoredInstances = new HashSet<>(dirtyScenarios);
		final boolean[] success = new boolean[1];
		try {
			final ProgressMonitorDialog p = new ProgressMonitorDialog(null);

			final IRunnableWithProgress runnable = new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) {

					monitor.beginTask("Saving dirty scenarios", scenariosToSave.length);
					try {

						for (final Object instance : scenariosToSave) {
							final ScenarioInstance scenario = (ScenarioInstance) instance;
							monitor.setTaskName("Saving: " + scenario.getName());
							scenario.save();
							monitor.worked(1);

							ignoredInstances.remove(scenario);
						}

						// Forcibly set dirty to false to avoid eclipse framework from prompting to save again.
						for (final ScenarioInstance ignoredInstance : ignoredInstances) {
							ignoredInstance.setDirty(false);
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

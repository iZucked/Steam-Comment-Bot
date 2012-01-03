/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.editor.actions;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import scenario.Scenario;
import scenario.UUIDObject;
import scenario.presentation.ScenarioEditor;

/**
 * Action to re-link the static data in a scenario to static data in another
 * file.
 * 
 * @author Tom Hinton
 * 
 */
public class LinkStaticDataAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(final IAction action) {
		if (window.getActivePage().getActiveEditor() instanceof ScenarioEditor) {
			final ScenarioEditor editor = (ScenarioEditor) window
					.getActivePage().getActiveEditor();
			// get selection
			final Scenario scenario = editor.getScenario();

			// pick a new resource
			final FilteredResourcesSelectionDialog frsd = new FilteredResourcesSelectionDialog(
					window.getShell(), false, ResourcesPlugin.getWorkspace()
							.getRoot(), IFile.FILE | IFile.FOLDER) {

				boolean applyingFilter = false;

				@Override
				protected void applyFilter() {
					// this is a pretty ugly hack to ensure matching of
					// portmodels
					// but it seems like the easiest way; for some reason doing
					// this properly
					// is really difficult.
					if (applyingFilter)
						return;
					applyingFilter = true;

					final Text hack = (Text) getPatternControl();
					final Point p = hack.getSelection();
					final String p1 = hack.getText();

					hack.setText((p1.isEmpty() ? "*" : p1) + ".portmodel");
					super.applyFilter();
					hack.setText(p1);
					hack.setSelection(p);
					applyingFilter = false;
				}

			};

			frsd.setInitialPattern("");
			if (frsd.open() == Window.OK) {
				final Object o = frsd.getFirstResult();
				if (o instanceof IResource) {
					final ResourceSet resourceSet = scenario.eResource()
							.getResourceSet();
					// load result into resource set
					final IResource resource = (IResource) o;

					// not sure if there is a better way of doing this?
					final URI eURI = URI.createPlatformResourceURI(resource
							.getProject().getName()
							+ "/"
							+ resource.getProjectRelativePath().toString(),
							true);

					final Resource linkedResource = resourceSet
							.createResource(eURI);

					try {
						linkedResource.load(null);
						if (linkedResource.getContents().size() == 1) {
							final EObject element = linkedResource
									.getContents().get(0);
							if (element instanceof Scenario) {
								// Now we need to re-link the static data
								// objects. There
								// is no easy way to do this, because references
								// are
								// always to the container, never to another
								// reference,
								// so we can't just swap out the portmodel
								// reference on
								// scenario and be done with it.

								// either we need to make sure everything has an
								// id
								// or match on names.
								final Scenario staticData = (Scenario) element;

								// generate ID matching
								final Map<String, UUIDObject> currentObjectsByID = new HashMap<String, UUIDObject>();
								final Map<String, UUIDObject> newObjectsByID = new HashMap<String, UUIDObject>();

								collectUUIDObjects(scenario.getPortModel(),
										currentObjectsByID);
								collectUUIDObjects(scenario.getCanalModel(),
										currentObjectsByID);

								collectUUIDObjects(staticData.getPortModel(),
										newObjectsByID);
								collectUUIDObjects(staticData.getCanalModel(),
										newObjectsByID);

								final Map<UUIDObject, UUIDObject> currentToNew = new HashMap<UUIDObject, UUIDObject>();

								for (final Map.Entry<String, UUIDObject> entry : currentObjectsByID
										.entrySet()) {
									final UUIDObject newObject = newObjectsByID
											.get(entry.getKey());
									if (newObject == null) {
										displayErrorMessage("The "
												+ entry.getValue().eClass()
														.getName()
												+ " with uuid "
												+ entry.getKey()
												+ " is missing from the new static data set");
									}
									currentToNew.put(entry.getValue(),
											newObject);
								}

								// find all references and redirect them from old to new objects.
								for (Map.Entry<EObject, Collection<Setting>> entry : EcoreUtil.UsageCrossReferencer
										.findAll(currentObjectsByID.values(),
												scenario).entrySet()) {
									final EObject oldObject = entry.getKey();
									final EObject newObject = currentToNew
											.get(oldObject);

									final Collection<Setting> settings = entry
											.getValue();

									for (final Setting setting : settings) {
										final EObject referer = setting
												.getEObject();
										final EReference reference = (EReference) setting
												.getEStructuralFeature();
										if (reference.isContainment() == false) {
											if (reference.isMany()) {
												final EList list = (EList) referer.eGet(reference);
												int index;
												while ((index = list.indexOf(oldObject)) != -1) {
													list.set(index, newObject);
												}
											} else {
												referer.eSet(reference, newObject);
											}
										}
									}
								}

								// waste any dangling models
								if (scenario.getPortModel().eContainer() == scenario) {
									scenario.getContainedModels().remove(scenario.getPortModel());
								}
								if (scenario.getDistanceModel().eContainer() == scenario) {
									scenario.getContainedModels().remove(scenario.getDistanceModel());
								}
								if (scenario.getCanalModel().eContainer() == scenario) {
									scenario.getContainedModels().remove(scenario.getCanalModel());
								}
								// replace models, for the benefit of the UI
								scenario.setPortModel(staticData.getPortModel());
								scenario.setDistanceModel(staticData
										.getDistanceModel());
								scenario.setCanalModel(staticData
										.getCanalModel());
								
								scenario.eResource().save(null);
							} else {
								displayErrorMessage(resource.getName()
										+ " does not contain any static data");
							}
						}

					} catch (IOException e) {
						displayErrorMessage(resource.getName()
								+ " could not be loaded: " + e.getMessage());
					}
				} else {
					displayErrorMessage(o + " is not a platform resource");
				}
			}
		}
		// find relink mapping and check nothing is missing
		// possibly present this to the user somehow in a dialog?
		// apply relink mapping
		// replace models and delete any old contained models
		// force UI refresh somehow, specifically all viewers and valueproviders
		// need updating.
	}

	/**
	 * Find all the contained objects with a UUID and map them into the given
	 * map.
	 * 
	 * @param root
	 *            the container for all objects
	 * @param output
	 *            the uuid <-> object map to add them to.
	 */
	private void collectUUIDObjects(final EObject root,
			final Map<String, UUIDObject> output) {

		if (root instanceof UUIDObject) {
			output.put(((UUIDObject) root).getUUID(), (UUIDObject) root);
		}

		final TreeIterator<EObject> ti = root.eAllContents();
		while (ti.hasNext()) {
			final EObject element = ti.next();
			if (element instanceof UUIDObject) {
				output.put(((UUIDObject) element).getUUID(),
						(UUIDObject) element);
			}
		}
	}

	/**
	 * Throw up an error dialog
	 * 
	 * @param string
	 */
	private void displayErrorMessage(String string) {

	}

	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(final IWorkbenchWindow window) {
		this.window = window;
	}

}

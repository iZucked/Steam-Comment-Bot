/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.importer.FuelCurveImporter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.DialogFeatureManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class VesselClassViewerPane extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(VesselClassViewerPane.class);

	private final IScenarioEditingLocation jointModelEditor;

	public VesselClassViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();

		getToolBarManager().appendToGroup("edit", new BaseFuelEditorAction());
		getToolBarManager().update(true);

		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain));

		addTypicalColumn("Capacity (m3)", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselClass_Capacity(), editingDomain));

		addTypicalColumn("Inaccessible Ports", new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselClass_InaccessiblePorts(), jointModelEditor.getReferenceValueProviderCache(),
				editingDomain, MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		// addTypicalColumn("Laden Fuel Usage", new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(), editingDomain));
		//
		// addTypicalColumn("Ballast Fuel Usage", new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_BallastAttributes(), editingDomain));

		setTitle("Vessel Classes", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	@Override
	protected Action createImportAction() {
		final Action def = super.createImportAction();
		def.setText("Import Vessel Classes");
		return new AbstractMenuAction("Import CSV") {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
			}

			@Override
			protected void populate(final Menu menu) {
				addActionToMenu(def, menu);
				final Action importFuels = new ImportAction(getJointModelEditorPart()) {
					{
						setText("Import Base Fuels");
					}

					@Override
					protected void doImportStages(final DefaultImportContext context) {
						final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(FleetPackage.eINSTANCE.getBaseFuel());

						final FileDialog fileDialog = new FileDialog(part.getShell());
						fileDialog.setFilterExtensions(new String[] { "*.csv" });
						final String path = fileDialog.open();

						if (path == null) {
							return;
						}

						CSVReader reader = null;
						try {
							reader = new CSVReader(new File(path));
							final Collection<EObject> importedObjects = importer.importObjects(FleetPackage.eINSTANCE.getBaseFuel(), reader, context);
							context.run();
							part.getEditingDomain().getCommandStack()
									.execute(mergeLists(getScenarioViewer().getCurrentContainer(), FleetPackage.eINSTANCE.getFleetModel_BaseFuels(), new ArrayList<EObject>(importedObjects)));
						} catch (final IOException e) {
							log.error(e.getMessage(), e);
						} finally {

							if (reader != null) {
								try {
									reader.close();
								} catch (final IOException e) {
								}
							}

						}
					}
				};
				addActionToMenu(importFuels, menu);

				final Action importFuelCurves = new LockableAction("Import Consumption Curves") {
					{
						setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
					}

					@Override
					public void run() {
						final DefaultImportContext context = new DefaultImportContext();
						final FuelCurveImporter importer = new FuelCurveImporter();
						final FileDialog fileDialog = new FileDialog(part.getSite().getShell());
						fileDialog.setFilterExtensions(new String[] { "*.csv" });
						final String path = fileDialog.open();

						if (path == null)
							return;

						CSVReader reader = null;
						MMXRootObject rootObject = jointModelEditor.getRootObject();
						if (rootObject instanceof LNGScenarioModel) {
							final FleetModel fleet = ((LNGScenarioModel) rootObject).getFleetModel();
							try {
								jointModelEditor.setDisableUpdates(true);
								jointModelEditor.setDisableCommandProviders(true);
								reader = new CSVReader(new File(path));
								final Map<String, Pair<IImportProblem, Pair<List<FuelConsumption>, List<FuelConsumption>>>> consumptions = importer.readConsumptions(reader, context);
								final EditingDomain ed = jointModelEditor.getEditingDomain();
								final CompoundCommand command = new CompoundCommand();
								each_vessel: for (final String vesselName : consumptions.keySet()) {
									// find vessel class
									for (final VesselClass vc : fleet.getVesselClasses()) {
										if (vc.getName().equals(vesselName)) {
											final Pair<List<FuelConsumption>, List<FuelConsumption>> ladenAndBallastConsumptions = consumptions.get(vesselName).getSecond();
											// apply change
											if (vc.getLadenAttributes().getFuelConsumption().isEmpty() == false)
												command.append(DeleteCommand.create(ed, vc.getLadenAttributes().getFuelConsumption()));
											if (vc.getBallastAttributes().getFuelConsumption().isEmpty() == false)
												command.append(DeleteCommand.create(ed, vc.getBallastAttributes().getFuelConsumption()));
											if (ladenAndBallastConsumptions.getFirst().isEmpty() == false)
												command.append(AddCommand.create(ed, vc.getLadenAttributes(), FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumption(),
														ladenAndBallastConsumptions.getFirst()));
											if (ladenAndBallastConsumptions.getSecond().isEmpty() == false)
												command.append(AddCommand.create(ed, vc.getBallastAttributes(), FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumption(),
														ladenAndBallastConsumptions.getSecond()));
											continue each_vessel;
										}
									}
									// collect problem
									context.addProblem(consumptions.get(vesselName).getFirst());
								}

								ed.getCommandStack().execute(command);
							} catch (final IOException e) {
								context.addProblem(context.createProblem("IO Error: " + e.getMessage(), false, false, false));
							} finally {
								jointModelEditor.setDisableUpdates(false);
								jointModelEditor.setDisableCommandProviders(false);

								if (reader != null) {
									try {
										reader.close();
									} catch (final IOException e) {
									}
								}
							}
						}
					}
				};

				addActionToMenu(importFuelCurves, menu);
			}
		};
	}

	class BaseFuelEditorAction extends AbstractMenuAction {
		public BaseFuelEditorAction() {
			super("Base Fuels");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.fleet.editor/icons/oildrop.png")));
			} catch (final MalformedURLException e) {
			}
		}

		@Override
		protected void populate(final Menu menu) {
			final MMXRootObject rootObject = jointModelEditor.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final FleetModel fleetModel = ((LNGScenarioModel)rootObject).getFleetModel();
				boolean b = false;
				for (final BaseFuel baseFuel : fleetModel.getBaseFuels()) {
					b = true;
					final Action editBase = new AbstractMenuAction(baseFuel.getName()) {
						@Override
						protected void populate(final Menu submenu) {
							final LockableAction edit = new LockableAction("Edit...") {
								public void run() {
									final DetailCompositeDialog dcd = new DetailCompositeDialog(jointModelEditor.getShell(), jointModelEditor.getDefaultCommandHandler());
									dcd.open(jointModelEditor, rootObject, Collections.singletonList((EObject) baseFuel));
								}
							};
							addActionToMenu(edit, submenu);

							final Action delete = new LockableAction("Delete...") {
								public void run() {
									final ICommandHandler handler = jointModelEditor.getDefaultCommandHandler();
									handler.handleCommand(DeleteCommand.create(handler.getEditingDomain(), Collections.singleton(baseFuel)), fleetModel,
											FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
								}
							};
							addActionToMenu(delete, submenu);
						}
					};
					addActionToMenu(editBase, menu);
				}

				if (b) {
					new MenuItem(menu, SWT.SEPARATOR);
				}
				final Action newBase = AddModelAction.create(FleetPackage.eINSTANCE.getBaseFuel(), new IAddContext() {
					@Override
					public MMXRootObject getRootObject() {
						return rootObject;
					}

					@Override
					public EReference getContainment() {
						return FleetPackage.eINSTANCE.getFleetModel_BaseFuels();
					}

					@Override
					public EObject getContainer() {
						return fleetModel;
					}

					@Override
					public ICommandHandler getCommandHandler() {
						return jointModelEditor.getDefaultCommandHandler();
					}

					@Override
					public IScenarioEditingLocation getEditorPart() {
						return jointModelEditor;
					}

					@Override
					public ISelection getCurrentSelection() {
						return viewer.getSelection();
					}
				});
				if (newBase != null) {
					newBase.setText("Add new base fuel...");
					addActionToMenu(newBase, menu);
				}
			}
		}
	}

	class VSAManipulator extends DialogFeatureManipulator {

		public VSAManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
			super(field, editingDomain);
		}

		@Override
		protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
			final DetailCompositeDialog dcd = new DetailCompositeDialog(cellEditorWindow.getShell(), jointModelEditor.getDefaultCommandHandler());

			final VesselStateAttributes attributes = (VesselStateAttributes) EcoreUtil.copy((EObject) getValue(object));

			final ScenarioLock editorLock = jointModelEditor.getEditorLock();
			try {
				editorLock.claim();
				jointModelEditor.setDisableUpdates(true);

				if (dcd.open(jointModelEditor, jointModelEditor.getRootObject(), Collections.singletonList((EObject) attributes)) == Window.OK) {
					return attributes;
				} else {
					return null;
				}
			} finally {
				jointModelEditor.setDisableUpdates(false);
				editorLock.release();
			}

		}

		@Override
		protected String renderValue(final Object object) {
			final VesselStateAttributes a = (VesselStateAttributes) object;
			if (a == null) {
				return "";
			}
			return "NBO: " + a.getNboRate() + " Idle NBO: " + a.getIdleNBORate() + " Idle Base:" + a.getIdleBaseRate();
		}

	}
}

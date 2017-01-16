/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.importer.RouteImporter;
import com.mmxlabs.models.lng.port.ui.distanceeditor.DistanceEditorDialog;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class PortEditorPane extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(PortEditorPane.class);

	public PortEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		addNameManipulator("Name");

		addTypicalColumn("Country", new BasicAttributeManipulator(PortPackage.eINSTANCE.getLocation_Country(), getEditingDomain()), PortPackage.eINSTANCE.getPort_Location());

		addTypicalColumn(PortCapability.LOAD.getName(), new CapabilityManipulator(PortCapability.LOAD, getJointModelEditorPart().getEditingDomain()));
		addTypicalColumn(PortCapability.DISCHARGE.getName(), new CapabilityManipulator(PortCapability.DISCHARGE, getJointModelEditorPart().getEditingDomain()));
		addTypicalColumn("Other Names",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), getEditingDomain())));

		// addTypicalColumn("Timezone", new BasicAttributeManipulator(PortPackage.eINSTANCE.getPort_TimeZone(), getJointModelEditorPart().getEditingDomain()));
		// addTypicalColumn("Port Code", new BasicAttributeManipulator(PortPackage.eINSTANCE.getPort_PortCode(), getJointModelEditorPart().getEditingDomain()));

		final DistanceMatrixEditorAction dmaAction = new DistanceMatrixEditorAction();
		getToolBarManager().appendToGroup(EDIT_GROUP, dmaAction);

		getToolBarManager().appendToGroup(EDIT_GROUP, new Action("Edit Port Groups") {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/group.gif"));
			}

			@Override
			public void run() {
				final DetailCompositeDialog dcd = new DetailCompositeDialog(PortEditorPane.this.getJointModelEditorPart().getShell(),
						PortEditorPane.this.getJointModelEditorPart().getDefaultCommandHandler());
				final MMXRootObject rootObject = getJointModelEditorPart().getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
					dcd.open(getJointModelEditorPart(), rootObject, lngScenarioModel.getReferenceModel().getPortModel(), PortPackage.eINSTANCE.getPortModel_PortGroups());
				}
			}
		});

		final Action importAction = createImportAction();
		if (importAction != null) {
			getToolBarManager().appendToGroup(ADD_REMOVE_GROUP, importAction);
		}
		getToolBarManager().update(true);

		// final Action tzAction = new Action("Update timezones") {
		//
		// @Override
		// public void run() {
		//
		// final ITimezoneProvider tzProvider = new GoogleTimezoneProvider();
		//
		// final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
		// final ISelection selection = getScenarioViewer().getSelection();
		// if (selection instanceof IStructuredSelection) {
		// final CompoundCommand cc = new CompoundCommand("Update Port Timezones");
		// final IStructuredSelection ss = (IStructuredSelection) selection;
		// final Iterator<?> itr = ss.iterator();
		// while (itr.hasNext()) {
		//
		// final Object next = itr.next();
		//
		// if (next instanceof Port) {
		// final Port p = (Port) next;
		// if (p.getLocation() != null) {
		// final Location l = p.getLocation();
		// try {
		// final TimeZone tz = tzProvider.findTimeZone((float) l.getLat(), (float) l.getLon());
		// if (tz != null) {
		// final Command cmd = SetCommand.create(ed, p, PortPackage.eINSTANCE.getPort_TimeZone(), tz.getID());
		// cc.append(cmd);
		// }
		// } catch (final Exception e) {
		// // Ignore - but could provide some feeback to user?
		// }
		// }
		// }
		// }
		// if (!cc.isEmpty()) {
		// ed.getCommandStack().execute(cc);
		// }
		// }
		// super.run();
		// }
		// };
		// getMenuManager().add(tzAction);
		// getMenuManager().update(true);

		defaultSetTitle("Ports");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_Ports");
	}

	protected Action createImportAction() {
		final Action importPorts = new SimpleImportAction(scenarioEditingLocation, scenarioViewer);

		importPorts.setText("Import ports...");
		final AbstractMenuAction importMenu = new AbstractMenuAction("Import Ports and Distances") {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
			}

			@Override
			protected void populate(final Menu menu) {
				addActionToMenu(importPorts, menu);
				final MMXRootObject rootObject = getJointModelEditorPart().getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final PortModel pm = ((LNGScenarioModel) rootObject).getReferenceModel().getPortModel();
					new MenuItem(menu, SWT.SEPARATOR);

					final HashSet<String> existingNames = new HashSet<String>();

					for (final Route r : pm.getRoutes()) {
						existingNames.add(r.getName());
						final ImportAction importR = new ImportAction(getJointModelEditorPart()) {
							@Override
							protected void doImportStages(final DefaultImportContext context) {
								final String path = importHooksProvider.getImportFilePath();
								if (path == null)
									return;
								final RouteImporter routeImporter = new RouteImporter();

								CSVReader reader = null;
								try {
									reader = new FileCSVReader(new File(path), importHooksProvider.getCsvSeparator());
									final Route importRoute = routeImporter.importRoute(reader, context);
									context.run();
									final CompoundCommand cc = new CompoundCommand();
									cc.append(IdentityCommand.INSTANCE);
									// remove old lines and add new ones.
									if (r.getLines().isEmpty() == false)
										cc.append(RemoveCommand.create(getEditingDomain(), new ArrayList<RouteLine>(r.getLines())));
									if (importRoute.getLines().isEmpty() == false)
										cc.append(AddCommand.create(getEditingDomain(), r, PortPackage.eINSTANCE.getRoute_Lines(), new ArrayList<RouteLine>(importRoute.getLines())));
									getJointModelEditorPart().setDisableUpdates(true);
									getEditingDomain().getCommandStack().execute(cc);
									getJointModelEditorPart().setDisableUpdates(false);
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

						importR.setText("Re-import " + r.getName() + " matrix...");
						addActionToMenu(importR, menu);
					}

//					new MenuItem(menu, SWT.SEPARATOR);
//
//					final ImportAction importNew = new ImportAction(getJointModelEditorPart()) {
//
//						@Override
//						protected void doImportStages(final DefaultImportContext context) {
//							final String path = importHooksProvider.getImportFilePath();
//
//							if (path == null)
//								return;
//
//							final InputDialog input = new InputDialog(importHooksProvider.getShell(), "Name for new canal", "Enter a new name for the new canal", "canal", new IInputValidator() {
//								@Override
//								public String isValid(final String newText) {
//									if (newText.trim().isEmpty()) {
//										return "The canal must have a name";
//									}
//									if (existingNames.contains(newText)) {
//										return "Another route already has the name " + newText;
//									}
//									return null;
//								}
//							});
//							if (input.open() == Window.OK) {
//								final String newName = input.getValue();
//								final RouteImporter routeImporter = new RouteImporter();
//
//								CSVReader reader = null;
//								try {
//									reader = new FileCSVReader(new File(path));
//
//									final Route importRoute = routeImporter.importRoute(reader, context);
//
//									context.run();
//
//									importRoute.setName(newName);
//									importRoute.setCanal(true);
//
//									getJointModelEditorPart().setDisableUpdates(true);
//									getJointModelEditorPart().getEditingDomain().getCommandStack()
//											.execute(AddCommand.create(getJointModelEditorPart().getEditingDomain(), pm, PortPackage.eINSTANCE.getPortModel_Routes(), importRoute));
//									getJointModelEditorPart().setDisableUpdates(false);
//								} catch (final IOException e) {
//									log.error(e.getMessage(), e);
//								} finally {
//
//									if (reader != null) {
//										try {
//											reader.close();
//										} catch (final IOException e) {
//										}
//									}
//
//								}
//							}
//						}
//					};
//
//					importNew.setText("Import new canal...");
//
//					addActionToMenu(importNew, menu);

					boolean hasMainRoute = false;
					for (final Route r : pm.getRoutes()) {
						if (r.isCanal() == false) {
							hasMainRoute = true;
							break;
						}
					}
					if (!hasMainRoute) {
						final ImportAction importNew2 = new ImportAction(getJointModelEditorPart()) {

							@Override
							protected void doImportStages(final DefaultImportContext context) {
								final String path = importHooksProvider.getImportFilePath();

								if (path == null)
									return;

								final String newName = "Direct";
								final RouteImporter routeImporter = new RouteImporter();

								CSVReader reader = null;
								try {
									reader = new FileCSVReader(new File(path), importHooksProvider.getCsvSeparator());

									final Route importRoute = routeImporter.importRoute(reader, context);

									context.run();

									importRoute.setName(newName);
									importRoute.setCanal(false);

									getJointModelEditorPart().setDisableUpdates(true);
									getJointModelEditorPart().getEditingDomain().getCommandStack()
											.execute(AddCommand.create(getJointModelEditorPart().getEditingDomain(), pm, PortPackage.eINSTANCE.getPortModel_Routes(), importRoute));
									getJointModelEditorPart().setDisableUpdates(false);
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

						importNew2.setText("Import direct distances...");
						addActionToMenu(importNew2, menu);
					}
				}
			}
		};

		return importMenu;
	}

	class DistanceMatrixEditorAction extends AbstractMenuAction {
		public DistanceMatrixEditorAction() {
			super("Edit distances");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.port.editor/icons/earth.gif")));
			} catch (final MalformedURLException e) {
			}
			setToolTipText("Edit distance matrices and canals");
		}

		@Override
		protected void populate(final Menu menu) {
			final MMXRootObject rootObject = getJointModelEditorPart().getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final PortModel portModel = ((LNGScenarioModel) rootObject).getReferenceModel().getPortModel();
				for (final Route canal : portModel.getRoutes()) {
					final Action canalEditor = new AbstractMenuAction(canal.getName()) {
						@Override
						protected void populate(final Menu menu2) {
							final Action editCanal = createMatrixEditor(canal.getName(), canal);
							addActionToMenu(editCanal, menu2);
							final Action renameCanal = new Action("Rename...") {
								@Override
								public void run() {
									final HashSet<String> existingNames = new HashSet<String>();
									for (final Route c : portModel.getRoutes()) {
										if (c != canal) {
											existingNames.add(c.getName());
										}
									}
									final InputDialog input = new InputDialog(part.getSite().getShell(), "Rename canal " + canal.getName(), "Enter a new name for the canal " + canal.getName(),
											canal.getName(), new IInputValidator() {
										@Override
										public String isValid(final String newText) {
											if (newText.trim().isEmpty()) {
												return "The canal must have a name";
											}
											if (existingNames.contains(newText)) {
												return "Another canal already has the name " + newText;
											}
											return null;
										}
									});
									if (input.open() == Window.OK) {
										final String newName = input.getValue();
										getJointModelEditorPart().getEditingDomain().getCommandStack()
												.execute(SetCommand.create(getJointModelEditorPart().getEditingDomain(), canal, MMXCorePackage.eINSTANCE.getNamedObject_Name(), newName));
									}
								}
							};
							addActionToMenu(renameCanal, menu2);
							// if (canal.isCanal()) {
							// final Action deleteCanal = new Action("Delete...") {
							// @Override
							// public void run() {
							// if (MessageDialog.openQuestion(part.getSite().getShell(), "Delete canal " + canal.getName(), "Are you sure you want to delete the canal \"" + canal.getName()
							// + "\"?")) {
							// getJointModelEditorPart().getEditingDomain().getCommandStack()
							// .execute(DeleteCommand.create(getJointModelEditorPart().getEditingDomain(), Collections.singleton(canal)));
							// }
							// }
							// };
							// addActionToMenu(deleteCanal, menu2);
							// }

						}
					};
					addActionToMenu(canalEditor, menu);
				}

				// new MenuItem(menu, SWT.SEPARATOR);
				//
				// addActionToMenu(new LockableAction("Add new canal...") {
				// @Override
				// public void run() {
				// final HashSet<String> existingNames = new HashSet<String>();
				// for (final Route c : portModel.getRoutes()) {
				// existingNames.add(c.getName());
				// }
				// final InputDialog input = new InputDialog(part.getSite().getShell(), "Create canal", "Enter a name for the new canal", "", new IInputValidator() {
				// @Override
				// public String isValid(final String newText) {
				// if (newText.trim().isEmpty()) {
				// return "The canal must have a name";
				// }
				// if (existingNames.contains(newText)) {
				// return "Another canal already has the name " + newText;
				// }
				// return null;
				// }
				// });
				// if (input.open() == Window.OK) {
				// final CompoundCommand cc = new CompoundCommand();
				// final String newName = input.getValue();
				// Route dm = PortFactory.eINSTANCE.createRoute();
				// dm.setName(newName);
				// dm.setCanal(true);
				// final DistanceEditorDialog ded = new DistanceEditorDialog(part.getSite().getShell());
				// if (ded.open(PortEditorPane.this.part.getSite(), getJointModelEditorPart(), dm) == Window.OK) {
				// dm = ded.getResult();
				// }
				//
				// cc.append(AddCommand.create(getJointModelEditorPart().getEditingDomain(), portModel, PortPackage.eINSTANCE.getPortModel_Routes(), dm));
				// getJointModelEditorPart().getEditingDomain().getCommandStack().execute(cc);
				//
				// }
				// }
				// }, menu);
			}
		}

		protected Action createMatrixEditor(final String name, final Route distanceModel) {
			return new LockableAction("Edit distances...") {
				@Override
				public void run() {

					final Route newModel = edit(distanceModel);
					if (newModel == null) {
						return;
					}

					final EditingDomain ed = getJointModelEditorPart().getEditingDomain();
					final CompoundCommand cc = new CompoundCommand("Edit distances");
					cc.append(DeleteCommand.create(ed, new ArrayList<>(distanceModel.getLines())));
					cc.append(AddCommand.create(ed, distanceModel, PortPackage.eINSTANCE.getRoute_Lines(), new ArrayList<>(newModel.getLines())));
					getJointModelEditorPart().setDisableUpdates(true);
					ed.getCommandStack().execute(cc);
					getJointModelEditorPart().setDisableUpdates(false);
					// display error

				}

				private Route edit(final Route distanceModel) {
					final DistanceEditorDialog ded = new DistanceEditorDialog(part.getSite().getShell());

					if (ded.open(PortEditorPane.this.part.getSite(), getJointModelEditorPart(), distanceModel) == Window.OK) {
						return ded.getResult();
					}
					return null;
				}
			};
		}
	}
}

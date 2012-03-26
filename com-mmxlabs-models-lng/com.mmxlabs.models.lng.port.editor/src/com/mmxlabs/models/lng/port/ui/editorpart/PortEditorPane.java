/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.importer.RouteImporter;
import com.mmxlabs.models.lng.port.ui.distanceeditor.DistanceEditorDialog;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class PortEditorPane extends ScenarioTableViewerPane {

	public PortEditorPane(final IWorkbenchPage page, final JointModelEditorPart part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addNameManipulator("Name");
	
		for (final PortCapability capability : PortCapability.values()) {
			addTypicalColumn(capability.getName(), new CapabilityManipulator(capability, getJointModelEditorPart().getEditingDomain()));
		}
		
		final DistanceMatrixEditorAction dmaAction = new DistanceMatrixEditorAction();
		getToolBarManager().appendToGroup(EDIT_GROUP, dmaAction);
		getToolBarManager().update(true);
		defaultSetTitle("Ports");
	}
	
	@Override
	protected Action createImportAction() {
		final Action importPorts = super.createImportAction();
		
		importPorts.setText("Import ports...");
		final AbstractMenuAction importMenu = new AbstractMenuAction("Import Ports and Distances") {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
			}
			
			@Override
			protected void populate(Menu menu) {
				addActionToMenu(importPorts, menu);
				final PortModel pm = getJointModelEditorPart().getRootObject().getSubModel(PortModel.class);
				new MenuItem(menu, SWT.SEPARATOR);
				
				final HashSet<String> existingNames = new HashSet<String>();
				
				for (final Route r : pm.getRoutes()) {
					existingNames.add(r.getName());
					final ImportAction importR = new ImportAction(getJointModelEditorPart()) {
						@Override
						protected void doImportStages(DefaultImportContext context) {
							final FileDialog fileDialog = new FileDialog(part.getSite().getShell());
							fileDialog.setFilterExtensions(new String[] {"*.csv"});
							final String path = fileDialog.open();
							
							if (path == null) return;
							final RouteImporter routeImporter = new RouteImporter();
							
							CSVReader reader;
							try {
								reader = new CSVReader(path);
								final Route importRoute = routeImporter.importRoute(reader, context);
								context.run();
								
								final CompoundCommand cc = new CompoundCommand();
								
								// remove old lines and add new ones.
								cc.append(RemoveCommand.create(
										getEditingDomain(),
										r.getLines()));
								
								cc.append(AddCommand.create(getEditingDomain(),
										r, PortPackage.eINSTANCE.getRoute_Lines(),
										importRoute.getLines()));
								
								getEditingDomain().getCommandStack().execute(cc);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};
					
					importR.setText("Re-import " + r.getName() + " matrix...");
					addActionToMenu(importR, menu);
				}
				
				new MenuItem(menu, SWT.SEPARATOR);
				
				final ImportAction importNew = new ImportAction(getJointModelEditorPart()) {
					
					@Override
					protected void doImportStages(DefaultImportContext context) {
						final FileDialog fileDialog = new FileDialog(part.getSite().getShell());
						fileDialog.setFilterExtensions(new String[] {"*.csv"});
						final String path = fileDialog.open();
						
						if (path == null) return;
						
						final InputDialog input = new InputDialog(part.getSite().getShell(), "Name for new canal", "Enter a new name for the new canal","canal", new IInputValidator() {
									@Override
									public String isValid(final String newText) {
										if (newText.trim().isEmpty()) {
											return "The canal must have a name";
										}
										if (existingNames.contains(newText)) {
											return "Another route already has the name " + newText;
										}
										return null;
									}
								});
						if (input.open() == Window.OK) {
							final String newName = input.getValue();
							final RouteImporter routeImporter = new RouteImporter();
							
							CSVReader reader;
							try {
								reader = new CSVReader(path);
								
								final Route importRoute = routeImporter.importRoute(reader, context);
								
								context.run();
								
								importRoute.setName(newName);
								importRoute.setCanal(true);
								
								getJointModelEditorPart().getEditingDomain().getCommandStack().execute(
										AddCommand.create(getJointModelEditorPart().getEditingDomain(), 
												pm, PortPackage.eINSTANCE.getPortModel_Routes(),
												importRoute));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				};
				
				importNew.setText("Import new canal...");
				
				addActionToMenu(importNew, menu);
			}
		};
		
		return importMenu;
	}

	class DistanceMatrixEditorAction extends AbstractMenuAction {
		public DistanceMatrixEditorAction() {
			super("Edit distances");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.port.editor/icons/table.gif")));
			} catch (final MalformedURLException e) {}
			setToolTipText("Edit distance matrices and canals");
		}

		@Override
		protected void populate(final Menu menu) {
			final PortModel portModel = getJointModelEditorPart().getRootObject().getSubModel(PortModel.class);
			for (final Route canal : portModel.getRoutes()) {
				final Action canalEditor = new AbstractMenuAction(canal.getName()) {
					@Override
					protected void populate(final Menu menu2) {
						final Action editCanal = createMatrixEditor(canal.getName(), canal);
						addActionToMenu(editCanal, menu2);
						final Action renameCanal = new Action("Rename " + canal.getName() + "...") {
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
									getJointModelEditorPart().getEditingDomain().getCommandStack().execute(SetCommand.create(getJointModelEditorPart().getEditingDomain(), canal, MMXCorePackage.eINSTANCE.getNamedObject_Name(), newName));
								}
							}
						};
						addActionToMenu(renameCanal, menu2);
						if (canal.isCanal()) {
							final Action deleteCanal = new Action("Delete " + canal.getName() + "...") {
							@Override
							public void run() {
								if (MessageDialog.openQuestion(part.getSite().getShell(), "Delete canal " + canal.getName(), "Are you sure you want to delete the canal \"" + canal.getName()
										+ "\"?")) {
									getJointModelEditorPart().getEditingDomain().getCommandStack().execute(
											DeleteCommand.create(getJointModelEditorPart().getEditingDomain(), Collections.singleton(canal)));
								}
							}
						};
						addActionToMenu(deleteCanal, menu2);
						}

					}
				};
				addActionToMenu(canalEditor, menu);
			}

			new MenuItem(menu, SWT.SEPARATOR);

			addActionToMenu(new LockableAction("Add new canal...") {
				@Override
				public void run() {
					final HashSet<String> existingNames = new HashSet<String>();
					for (final Route c : portModel.getRoutes()) {
						existingNames.add(c.getName());
					}
					final InputDialog input = new InputDialog(part.getSite().getShell(), "Create canal", "Enter a name for the new canal", "", new IInputValidator() {
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
						final CompoundCommand cc = new CompoundCommand();
						final String newName = input.getValue();
						Route dm = PortFactory.eINSTANCE.createRoute();
						dm.setName(newName);
						dm.setCanal(true);
						final DistanceEditorDialog ded = new DistanceEditorDialog(part.getSite().getShell());
						if (ded.open(getJointModelEditorPart(), dm) == Window.OK) {
							dm = ded.getResult();
						}

						cc.append(AddCommand.create(getJointModelEditorPart().getEditingDomain(), portModel, PortPackage.eINSTANCE.getPortModel_Routes(), dm));
						getJointModelEditorPart().getEditingDomain().getCommandStack().execute(cc);

					}
				}
			}, menu);
		}

		protected Action createMatrixEditor(final String name, final Route distanceModel) {
			return new LockableAction("Edit " + name + " distances...") {
				@Override
				public void run() {
					
					final Route newModel = edit(distanceModel);
					if (newModel == null) {
						return;
					}
					final EditingDomain ed = getJointModelEditorPart().getEditingDomain();
					final CompoundCommand cc = new CompoundCommand();
					cc.append(DeleteCommand.create(ed, distanceModel.getLines()));
					cc.append(AddCommand.create(ed, distanceModel, PortPackage.eINSTANCE.getRoute_Lines(), newModel.getLines()));
					ed.getCommandStack().execute(cc);
					// display error
				}

				private Route edit(final Route distanceModel) {
					final DistanceEditorDialog ded = new DistanceEditorDialog(part.getSite().getShell());
					if (ded.open(getJointModelEditorPart(), distanceModel) == Window.OK) {
						return ded.getResult();
					}
					return null;
				}
			};
		}
	}
}



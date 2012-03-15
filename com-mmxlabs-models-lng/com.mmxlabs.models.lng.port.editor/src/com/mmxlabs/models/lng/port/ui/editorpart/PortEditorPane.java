/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

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
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.ui.distanceeditor.DistanceEditorDialog;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;

public class PortEditorPane extends ScenarioTableViewerPane {

	private JointModelEditorPart jointModelEditorPart;

	public PortEditorPane(final IWorkbenchPage page, final JointModelEditorPart part) {
		super(page, part);
		this.jointModelEditorPart = part;
		
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addNameManipulator("Name");
	
		for (final PortCapability capability : PortCapability.values()) {
			addTypicalColumn(capability.getName(), new CapabilityManipulator(capability, jointModelEditorPart.getEditingDomain()));
		}
		
		final DistanceMatrixEditorAction dmaAction = new DistanceMatrixEditorAction();
		getToolBarManager().appendToGroup(EDIT_GROUP, dmaAction);
		getToolBarManager().update(true);
		defaultSetTitle("Ports");
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
			final PortModel portModel = jointModelEditorPart.getRootObject().getSubModel(PortModel.class);
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
									jointModelEditorPart.getEditingDomain().getCommandStack().execute(SetCommand.create(jointModelEditorPart.getEditingDomain(), canal, MMXCorePackage.eINSTANCE.getNamedObject_Name(), newName));
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
									jointModelEditorPart.getEditingDomain().getCommandStack().execute(
											DeleteCommand.create(jointModelEditorPart.getEditingDomain(), Collections.singleton(canal)));
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

			addActionToMenu(new Action("Add new canal...") {
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
						if (ded.open(jointModelEditorPart, dm) == Window.OK) {
							dm = ded.getResult();
						}

						cc.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), portModel, PortPackage.eINSTANCE.getPortModel_Routes(), dm));
						jointModelEditorPart.getEditingDomain().getCommandStack().execute(cc);

					}
				}
			}, menu);
		}

		protected Action createMatrixEditor(final String name, final Route distanceModel) {
			return new Action("Edit " + name + " distances...") {
				@Override
				public void run() {
					
					final Route newModel = edit(distanceModel);
					if (newModel == null) {
						return;
					}
					final EditingDomain ed = jointModelEditorPart.getEditingDomain();
					final CompoundCommand cc = new CompoundCommand();
					cc.append(DeleteCommand.create(ed, distanceModel.getLines()));
					cc.append(AddCommand.create(ed, distanceModel, PortPackage.eINSTANCE.getRoute_Lines(), newModel.getLines()));
					ed.getCommandStack().execute(cc);
					// display error
				}

				private Route edit(final Route distanceModel) {
					final DistanceEditorDialog ded = new DistanceEditorDialog(part.getSite().getShell());
					if (ded.open(jointModelEditorPart, distanceModel) == Window.OK) {
						return ded.getResult();
					}
					return null;
				}
			};
		}
	}
}



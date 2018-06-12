/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.rcp.common.actions.AbstractMenuLockableAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class PortEditorPane extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(PortEditorPane.class);

	public PortEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addNameManipulator("Name");

		addTypicalColumn("Country", new BasicAttributeManipulator(PortPackage.eINSTANCE.getLocation_Country(), getEditingDomain()), PortPackage.eINSTANCE.getPort_Location());
//		addTypicalColumn("MMX ID ", new BasicAttributeManipulator(PortPackage.eINSTANCE.getLocation_MmxId(), getEditingDomain()), PortPackage.eINSTANCE.getPort_Location());

		addTypicalColumn(PortCapability.LOAD.getName(), new CapabilityManipulator(PortCapability.LOAD, getJointModelEditorPart().getEditingDomain()));
		addTypicalColumn(PortCapability.DISCHARGE.getName(), new CapabilityManipulator(PortCapability.DISCHARGE, getJointModelEditorPart().getEditingDomain()));
		addTypicalColumn("Other Names",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), getEditingDomain())), PortPackage.eINSTANCE.getPort_Location());

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

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_Ports");
	}

	class DistanceMatrixEditorAction extends AbstractMenuLockableAction {
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
					final Action canalEditor = new AbstractMenuLockableAction(canal.getName()) {
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
//					// STick in menu
//					PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel)getRootObject());
//					for (Route route : portModel.getRoutes()) {
//						if (route.isCanal()) {
//							pane.getToolBarManager().appendToGroup("edit", new RunnableAction("Edit " + route.getName(), () -> {
								DetailCompositeDialogUtil.editSingleObject(PortEditorPane.this.getJointModelEditorPart(), distanceModel);	
//							}));
//						}
//					}
//					final Route newModel = edit(distanceModel);
//					if (newModel == null) {
//						return;
//					}
//
//					final EditingDomain ed = getJointModelEditorPart().getEditingDomain();
//					final CompoundCommand cc = new CompoundCommand("Edit distances");
//					cc.append(DeleteCommand.create(ed, new ArrayList<>(distanceModel.getLines())));
//					cc.append(AddCommand.create(ed, distanceModel, PortPackage.eINSTANCE.getRoute_Lines(), new ArrayList<>(newModel.getLines())));
//					getJointModelEditorPart().setDisableUpdates(true);
//					ed.getCommandStack().execute(cc);
//					getJointModelEditorPart().setDisableUpdates(false);
					// display error

				}

//				private Route edit(final Route distanceModel) {
//					final DistanceEditorDialog ded = new DistanceEditorDialog(part.getSite().getShell());
//
//					if (ded.open(PortEditorPane.this.part.getSite(), getJointModelEditorPart(), distanceModel) == Window.OK) {
//						return ded.getResult();
//					}
//					return null;
//				}
			};
		}
	}
}

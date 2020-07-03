/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.AbstractMenuLockableAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class VesselViewerPane_View extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation scenarioEditingLocation;
	private Action newVesselFromReferenceAction;

	public VesselViewerPane_View(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.scenarioEditingLocation = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		getToolBarManager().appendToGroup("edit", new BaseFuelEditorAction());
		getToolBarManager().update(true);

		final EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();
		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain));

		addTypicalColumn("Capacity (m³)", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVessel_Capacity(), editingDomain));
		addTypicalColumn("Shortname", new BasicAttributeManipulator(FleetPackage.eINSTANCE.getVessel_ShortName(), editingDomain));
		addTypicalColumn("Type", new BasicAttributeManipulator(FleetPackage.eINSTANCE.getVessel_Type(), editingDomain));
		addTypicalColumn("Reference vessel", new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_Reference(), scenarioEditingLocation.getReferenceValueProviderCache(), editingDomain));
		addTypicalColumn("Inaccessible Ports", new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_InaccessiblePorts(), scenarioEditingLocation.getReferenceValueProviderCache(),
				editingDomain, MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		addTypicalColumn("Max Speed", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVessel_MaxSpeed(), editingDomain));
		// getToolBarManager().appendToGroup(EDIT_GROUP, new BaseFuelEditorAction());
		/*
		 * getToolBarManager().appendToGroup(EDIT_GROUP, new Action("VC") {
		 * 
		 * @Override public void run() {
		 * 
		 * final MMXRootObject rootObject = jointModelEditor.getRootObject(); if (rootObject instanceof LNGScenarioModel) { final FleetModel fleetModel = ((LNGScenarioModel)
		 * rootObject).getFleetModel(); final DetailCompositeDialog dcd = new DetailCompositeDialog(VesselViewerPane_View.this.getJointModelEditorPart().getShell(),
		 * VesselViewerPane_View.this.getJointModelEditorPart() .getDefaultCommandHandler()); dcd.open(getJointModelEditorPart(), getJointModelEditorPart().getRootObject(), fleetModel,
		 * FleetPackage.eINSTANCE.getFleetModel_VesselClasses()); } } });
		 */

		getToolBarManager().appendToGroup(EDIT_GROUP, new Action() {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/group.gif"));
			}

			@Override
			public void run() {
				final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
					final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);

					final DetailCompositeDialog dcd = new DetailCompositeDialog(VesselViewerPane_View.this.getJointModelEditorPart().getShell(),
							VesselViewerPane_View.this.getJointModelEditorPart().getDefaultCommandHandler());
					dcd.open(getJointModelEditorPart(), getJointModelEditorPart().getRootObject(), fleetModel, FleetPackage.eINSTANCE.getFleetModel_VesselGroups());
				}
			}
		});
		getToolBarManager().update(true);

		setTitle("Vessels", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		viewer.addSelectionChangedListener(e -> {
			if (newVesselFromReferenceAction != null) {
				boolean active = false;
				final ISelection selection = viewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection ss = (IStructuredSelection) selection;
					if (ss.size() == 1) {
						final Object firstElement = ss.getFirstElement();
						if (firstElement instanceof Vessel) {
							final Vessel vessel = (Vessel) firstElement;
							active = vessel.getReference() == null;
						}
					}
				}
				newVesselFromReferenceAction.setEnabled(active);
			}
		});
	}

	@Override
	protected Action createAddAction(final EReference containment) {

		newVesselFromReferenceAction = new RunnableAction("Vessel from reference", () -> {

			final ISelection selection = viewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) selection;
				final Object firstElement = ss.getFirstElement();
				if (firstElement instanceof Vessel) {
					final Vessel reference = (Vessel) firstElement;

					final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
					vessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					vessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					vessel.setReference(reference);
					final CompoundCommand cmd = new CompoundCommand("New vessel");
					cmd.append(AddCommand.create(getEditingDomain(), ScenarioModelUtil.getFleetModel(getJointModelEditorPart().getScenarioDataProvider()), FleetPackage.Literals.FLEET_MODEL__VESSELS,
							Collections.singleton(vessel)));
					final CommandStack commandStack = getEditingDomain().getCommandStack();
					commandStack.execute(cmd);
					DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), vessel, commandStack.getMostRecentCommand());
				}
			}
		});

		final Action duplicateAction = createDuplicateAction();
		final Action[] extraActions = duplicateAction == null ? new Action[] { newVesselFromReferenceAction } : new Action[] { newVesselFromReferenceAction, duplicateAction };
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), extraActions);
	}

	class BaseFuelEditorAction extends AbstractMenuLockableAction {
		public BaseFuelEditorAction() {
			super("Base Fuels");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.fleet.editor/icons/oildrop.png")));
			} catch (final MalformedURLException e) {
			}
		}

		@Override
		public void run() {
			final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
				final DetailCompositeDialog dcd = new DetailCompositeDialog(VesselViewerPane_View.this.getJointModelEditorPart().getShell(),
						VesselViewerPane_View.this.getJointModelEditorPart().getDefaultCommandHandler());
				dcd.open(getJointModelEditorPart(), getJointModelEditorPart().getRootObject(), fleetModel, FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
			}
		}

		@Override
		protected void populate(final Menu menu) {
			if (scenarioEditingLocation.getRootObject() instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
				final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
				boolean b = false;
				for (final BaseFuel baseFuel : fleetModel.getBaseFuels()) {
					b = true;
					final Action editBase = new AbstractMenuLockableAction(baseFuel.getName()) {
						@Override
						protected void populate(final Menu submenu) {
							final LockableAction edit = new LockableAction("Edit...") {
								public void run() {
									final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
									dcd.open(getJointModelEditorPart(), scenarioEditingLocation.getRootObject(), Collections.singletonList((EObject) baseFuel));
								}
							};
							addActionToMenu(edit, submenu);

							final Action delete = new LockableAction("Delete...") {
								public void run() {
									final ICommandHandler handler = scenarioEditingLocation.getDefaultCommandHandler();
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
						return scenarioEditingLocation.getRootObject();
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
						return scenarioEditingLocation.getDefaultCommandHandler();
					}

					@Override
					public IScenarioEditingLocation getEditorPart() {
						return scenarioEditingLocation;
					}

					@Override
					public @Nullable Collection<@NonNull EObject> getCurrentSelection() {
						return SelectionHelper.convertToList(viewer.getSelection(), EObject.class);
					}
				});
				if (newBase != null) {
					newBase.setText("Add new base fuel...");
					addActionToMenu(newBase, menu);
				}
			}
		}
	}
}

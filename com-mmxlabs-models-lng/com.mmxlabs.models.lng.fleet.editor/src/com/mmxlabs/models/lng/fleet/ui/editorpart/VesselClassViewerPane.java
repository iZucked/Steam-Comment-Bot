/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
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
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.DialogFeatureManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class VesselClassViewerPane extends ScenarioTableViewerPane {

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

		addTypicalColumn("Capacity (m³)", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselClass_Capacity(), editingDomain));

		addTypicalColumn("Inaccessible Ports", new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselClass_InaccessiblePorts(), jointModelEditor.getReferenceValueProviderCache(),
				editingDomain, MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		// addTypicalColumn("Laden Fuel Usage", new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(), editingDomain));
		//
		// addTypicalColumn("Ballast Fuel Usage", new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_BallastAttributes(), editingDomain));

		setTitle("Vessel Classes", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_VesselClasses");
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
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);

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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.DialogFeatureManipulator;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class VesselClassViewerPane extends ScenarioTableViewerPane {
	private JointModelEditorPart jointModelEditor;

	public VesselClassViewerPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
		this.jointModelEditor = part;
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		
		getToolBarManager().appendToGroup("edit", new BaseFuelEditorAction());
		getToolBarManager().update(true);
		
		addTypicalColumn("Name", new BasicAttributeManipulator(
				MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain));

		addTypicalColumn(
				"Capacity (M3)",
				new NumericAttributeManipulator(FleetPackage.eINSTANCE
						.getVesselClass_Capacity(), editingDomain));

		addTypicalColumn(
				"Inaccessible Ports",
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE
						.getVesselClass_InaccessiblePorts(), jointModelEditor
						.getReferenceValueProviderCache(), editingDomain,
						MMXCorePackage.eINSTANCE.getNamedObject_Name()));

			addTypicalColumn("Laden Fuel Usage", 
					new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(), editingDomain)
					);
			
			addTypicalColumn("Ballast Fuel Usage", 
					new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_BallastAttributes(), editingDomain)
					);

		
		setTitle("Vessel Classes", PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_DEF_VIEW));
	}
	
	class BaseFuelEditorAction extends AbstractMenuAction {
		public BaseFuelEditorAction() {
			super("Base Fuels");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.fleet.editor/icons/oildrop.png")));
			} catch (final MalformedURLException e) {}
		}
		
		@Override
		protected void populate(final Menu menu) {
			final FleetModel fleetModel = jointModelEditor.getRootObject().getSubModel(FleetModel.class);
			boolean b = false;
			for (final BaseFuel baseFuel : fleetModel.getBaseFuels()) {
				b = true;
				final Action editBase = new AbstractMenuAction(baseFuel.getName()) {					
					@Override
					protected void populate(Menu submenu) {
						final LockableAction edit = new LockableAction("Edit...") {
							public void run() {
								final DetailCompositeDialog dcd = new DetailCompositeDialog(
										jointModelEditor.getSite().getShell(), 
										jointModelEditor.getDefaultCommandHandler());
								dcd.open(jointModelEditor.getRootObject(), Collections.singletonList((EObject) baseFuel));
							}
						};
						addActionToMenu(edit, submenu);
						
						final Action delete = new LockableAction("Delete...") {
							public void run() {
								final ICommandHandler handler = jointModelEditor.getDefaultCommandHandler();
								handler.handleCommand(DeleteCommand.create(handler.getEditingDomain(), Collections.singleton(baseFuel)), fleetModel, FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
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
			final Action newBase = new LockableAction("Add base fuel...") {
				@Override
				public void run() {
					final BaseFuel newFuel = FleetFactory.eINSTANCE.createBaseFuel();
					final DetailCompositeDialog dcd = new DetailCompositeDialog(
							jointModelEditor.getSite().getShell(), 
							jointModelEditor.getDefaultCommandHandler());
					if (dcd.open(jointModelEditor.getRootObject(), Collections.singletonList((EObject) newFuel)) == Window.OK) {
						final ICommandHandler handler = jointModelEditor.getDefaultCommandHandler();
						handler.handleCommand(
								AddCommand.create(handler.getEditingDomain(), fleetModel,  FleetPackage.eINSTANCE.getFleetModel_BaseFuels(),
										newFuel),
										fleetModel, FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
					}
				}
			};
			addActionToMenu(newBase, menu);
		}
	}
	
	class VSAManipulator extends DialogFeatureManipulator {

		public VSAManipulator(EStructuralFeature field,
				EditingDomain editingDomain) {
			super(field, editingDomain);
		}
		@Override
		protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
			final DetailCompositeDialog dcd = new DetailCompositeDialog(cellEditorWindow.getShell(), jointModelEditor.getDefaultCommandHandler());
			
			final VesselStateAttributes attributes = (VesselStateAttributes) EcoreUtil.copy((EObject)getValue(object));
			if (dcd.open(jointModelEditor.getRootObject(), Collections.singletonList((EObject) attributes)) == Window.OK) {
				return attributes;
			} else {
				return null;
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

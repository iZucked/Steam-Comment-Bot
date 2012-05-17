/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.actions.RewireAction;
import com.mmxlabs.models.lng.cargo.ui.actions.RotateSlotsAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class CargoModelViewer extends ScenarioTableViewerPane {
	private IScenarioEditingLocation part;
	
	public CargoModelViewer(IWorkbenchPage page, IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.part = location;
	}
	
	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		final RewireAction rewire = new RewireAction(getJointModelEditorPart());
		final RotateSlotsAction rotate = new RotateSlotsAction(getJointModelEditorPart());
		viewer.addSelectionChangedListener(rotate);
		viewer.addSelectionChangedListener(rewire);
		getToolBarManager().appendToGroup("edit", rewire);
		getToolBarManager().appendToGroup("edit", rotate);
		getToolBarManager().update(true);
		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;
		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = part.getReferenceValueProviderCache();
		final EditingDomain editingDomain = part.getEditingDomain();
		
		addTypicalColumn(
				"ID",
				new BasicAttributeManipulator(mmx.getNamedObject_Name(), editingDomain));
		
		addTypicalColumn(
				"Load Port",
				new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_LoadSlot());
		
		addTypicalColumn("Load Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), pkg.getCargo_LoadSlot());
		
		addTypicalColumn(
				"Load Contract",
				new SingleReferenceManipulator(pkg.getSlot_Contract(), provider, editingDomain), pkg.getCargo_LoadSlot());
		
		addTypicalColumn(
				"Discharge Port",
				new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_DischargeSlot());
		
		addTypicalColumn("Discharge Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), pkg.getCargo_DischargeSlot());
		
		addTypicalColumn(
				"Discharge Contract",
				new SingleReferenceManipulator(pkg.getSlot_Contract(), provider, editingDomain), pkg.getCargo_DischargeSlot());
		
		setTitle("Cargoes", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	@Override
	protected Action createImportAction() {
		return new SimpleImportAction(part, getScenarioViewer()) {
			@Override
			protected Command mergeImports(EObject container, EReference containment, Collection<EObject> imports) {
				final List<EObject> cargoes = new ArrayList<EObject>();
				final List<EObject> loads = new ArrayList<EObject>();
				final List<EObject> discharges = new ArrayList<EObject>();
				
				for (final EObject o : imports) {
					if (o instanceof Cargo) cargoes.add(o);
					else if (o instanceof LoadSlot) loads.add(o);
					else if (o instanceof DischargeSlot) discharges.add(o);
				}
				
				final CompoundCommand mergeAll = new CompoundCommand();
				mergeAll.append(super.mergeImports(container, containment, cargoes));
				mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loads));
				mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), discharges));
				return mergeAll;
			}
		};
	}
	
}

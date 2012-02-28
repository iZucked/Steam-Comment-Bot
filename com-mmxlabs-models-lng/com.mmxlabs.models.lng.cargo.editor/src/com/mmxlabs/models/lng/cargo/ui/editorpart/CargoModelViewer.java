/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.types.ui.tabular.DateAttributeManipulator;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class CargoModelViewer extends ScenarioTableViewerPane {
	private JointModelEditorPart part;
	
	public CargoModelViewer(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
		this.part = part;
	}
	
	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
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

		setTitle("Cargos", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}

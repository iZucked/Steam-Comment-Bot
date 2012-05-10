/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;


import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

public class VesselViewerPane extends ScenarioTableViewerPane {

	private IScenarioEditingLocation jointModelEditor;

	public VesselViewerPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location) {
		super(page, part, location);
		this.jointModelEditor = location;
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		addTypicalColumn("Name", new BasicAttributeManipulator(
				MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain));

		addTypicalColumn("Class", new SingleReferenceManipulator(
				FleetPackage.eINSTANCE.getVessel_VesselClass(),
				jointModelEditor.getReferenceValueProviderCache(),
				editingDomain));
		
		addTypicalColumn("Time Charter", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVessel_TimeCharterRate(), jointModelEditor.getEditingDomain()));
		
		addTypicalColumn("Start Port", 
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()),
				FleetPackage.eINSTANCE.getVessel_Availability());
		
		addTypicalColumn("Start Before", 
				new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartBy(), jointModelEditor.getEditingDomain()),
				FleetPackage.eINSTANCE.getVessel_Availability());
		
		addTypicalColumn("End Port", 
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()),
				FleetPackage.eINSTANCE.getVessel_Availability());
		
		addTypicalColumn("End After", 
				new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndAfter(), jointModelEditor.getEditingDomain()),
				FleetPackage.eINSTANCE.getVessel_Availability());
		
		setTitle("Vessels", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}

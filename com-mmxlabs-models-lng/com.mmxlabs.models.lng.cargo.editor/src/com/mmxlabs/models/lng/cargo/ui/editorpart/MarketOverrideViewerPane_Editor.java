/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class MarketOverrideViewerPane_Editor extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;

	public MarketOverrideViewerPane_Editor(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		ReadOnlyManipulatorWrapper<BasicAttributeManipulator> nameManipulator = new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(
				new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain));

		addTypicalColumn("Name", nameManipulator, CargoPackage.eINSTANCE.getCharterInMarketOverride_CharterInMarket());

//		addTypicalColumn("Class",
//				new ReadOnlyManipulatorWrapper<>(new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_VesselClass(), jointModelEditor.getReferenceValueProviderCache(), editingDomain)),
//				CargoPackage.eINSTANCE.getVesselAvailability_Vessel());
//
//		addTypicalColumn("Fleet", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Fleet(), editingDomain));
//
//		addTypicalColumn("Optional", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Optional(), editingDomain));
//
//		addTypicalColumn("Charter", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate(), jointModelEditor.getEditingDomain()));
//
//		addTypicalColumn("Repositioning Fee", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_RepositioningFee(), jointModelEditor.getEditingDomain()) {
//			@Override
//			public boolean canEdit(Object object) {
//				if (object instanceof VesselAvailability) {
//					if (!((VesselAvailability) object).isFleet()) {
//						return true;
//					} else {
//						return false;
//					}
//				} else {
//					return super.canEdit(object);
//				}
//			}
//		});
//
//		addTypicalColumn("Start Port",
//				new SingleReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain()));
//
//		addTypicalColumn("Start After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAfter(), jointModelEditor.getEditingDomain()));
//
//		addTypicalColumn("Start By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartBy(), jointModelEditor.getEditingDomain()));
//
//		addTypicalColumn("End Port", new MultiplePortReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAt(), jointModelEditor.getReferenceValueProviderCache(),
//				jointModelEditor.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
//
//		addTypicalColumn("End After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAfter(), jointModelEditor.getEditingDomain()));
//
//		addTypicalColumn("End By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndBy(), jointModelEditor.getEditingDomain()));

		setTitle("Overrides", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}

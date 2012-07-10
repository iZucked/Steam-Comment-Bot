/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

public class VesselEventViewerPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;

	public VesselEventViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);

		addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof CharterOutEvent) {
					return "Charter Out";
				} else if (object instanceof DryDockEvent) {
					return "Dry Dock";
				} else if (object instanceof MaintenanceEvent) {
					return "Maintenance Event";
				} else {
					return "Unknown Event";
				}
			}
		});

		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Earliest Start", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselEvent_StartAfter(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Latest Start", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselEvent_StartBy(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Port", new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVesselEvent_Port(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Duration", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselEvent_DurationInDays(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Vessels",
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselEvent_AllowedVessels(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(),
						MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		setTitle("Vessel Events", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

}

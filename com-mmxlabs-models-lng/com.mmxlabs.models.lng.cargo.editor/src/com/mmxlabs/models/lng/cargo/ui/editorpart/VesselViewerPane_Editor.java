/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;

public class VesselViewerPane_Editor extends ScenarioTableViewerPane {

	// TODO: Make these colours a preference so they can be consistently used across various UI parts
	private final Color tcVessel = new Color(Display.getDefault(), 150, 210, 230);

	private final IScenarioEditingLocation jointModelEditor;

	public VesselViewerPane_Editor(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		ReadOnlyManipulatorWrapper<BasicAttributeManipulator> nameManipulator = new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain));
		
		addTypicalColumn("Name", nameManipulator, CargoPackage.eINSTANCE.getVesselAvailability_Vessel());

		addTypicalColumn("Class", new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_VesselClass(), jointModelEditor.getReferenceValueProviderCache(), editingDomain),
				CargoPackage.eINSTANCE.getVesselAvailability_Vessel());

		addTypicalColumn("Fleet", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Fleet(), editingDomain));
		
		addTypicalColumn("Optional", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Optional(), editingDomain));

		addTypicalColumn("Charter", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("Repositioning Fee", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_RepositioningFee(), jointModelEditor.getEditingDomain()) {@Override
		public boolean canEdit(Object object) {
			if (object instanceof VesselAvailability) {
				if (((VesselAvailability) object).isOptional()) {
					return true;
				} else {
					return false;
				}
			} else {
				return super.canEdit(object);
			}
		}});

		addTypicalColumn("Ballast Bonus", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_BallastBonus(), jointModelEditor.getEditingDomain()) {@Override
			public boolean canEdit(Object object) {
			if (object instanceof VesselAvailability) {
				if (((VesselAvailability) object).isOptional()) {
					return true;
				} else {
					return false;
				}
			} else {
				return super.canEdit(object);
			}
		}});
		
		addTypicalColumn("Start Port",
				new MultiplePortReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(),
						MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("Start After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAfter(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("Start By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartBy(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("End Port",
				new MultiplePortReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(),
						MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("End After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAfter(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("End By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndBy(), jointModelEditor.getEditingDomain()));

		setTitle("Vessels", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		final ScenarioTableViewer scenarioTableViewer = super.constructViewer(parent);
		scenarioTableViewer.setColourProvider(new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {
				return null;
			}

			@Override
			public Color getBackground(final Object element) {

				if (element instanceof VesselAvailability) {

					final VesselAvailability vesselAvailability = (VesselAvailability) element;
					if (vesselAvailability.isSetTimeCharterRate()) {
						return tcVessel;
					}
				}
				return null;
			}

		});
		return scenarioTableViewer;

	}

	@Override
	public void dispose() {

		tcVessel.dispose();

		super.dispose();
	}
}

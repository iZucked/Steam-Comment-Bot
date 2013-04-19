/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.importer.FuelCurveImporter;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class VesselViewerPane_Editor extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(VesselViewerPane_Editor.class);

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
		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), FleetPackage.eINSTANCE.getVesselAvailability_Vessel());

		addTypicalColumn("Class", new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_VesselClass(), jointModelEditor.getReferenceValueProviderCache(), editingDomain),
				FleetPackage.eINSTANCE.getVesselAvailability_Vessel());

		addTypicalColumn("Time Charter", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVessel_TimeCharterRate(), jointModelEditor.getEditingDomain()),
				FleetPackage.eINSTANCE.getVesselAvailability_Vessel());

		addTypicalColumn("Start Port",
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(),
						MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("Start After", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartAfter(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("Start Before", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_StartBy(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("End Port",
				new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndAt(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain(),
						MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("End After", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndAfter(), jointModelEditor.getEditingDomain()));

		addTypicalColumn("End By", new DateAttributeManipulator(FleetPackage.eINSTANCE.getVesselAvailability_EndBy(), jointModelEditor.getEditingDomain()));

		setTitle("Vessels", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		final ScenarioTableViewer scenarioTableViewer = new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart()) {
			@Override
			public EObject getElementForNotificationTarget(final EObject source) {
				if (source instanceof VesselAvailability) {
					return source.eContainer();
				}

				return super.getElementForNotificationTarget(source);
			}
		};
		scenarioTableViewer.setColourProvider(new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {
				return null;
			}

			@Override
			public Color getBackground(final Object element) {

				if (element instanceof Vessel) {

					final Vessel vessel = (Vessel) element;
					if (vessel.isSetTimeCharterRate()) {
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

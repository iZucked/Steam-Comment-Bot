/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import scenario.fleet.FleetPackage;
import scenario.fleet.VesselStateAttributes;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.BasicAttributeManipulator;
import scenario.presentation.cargoeditor.DialogFeatureManipulator;
import scenario.presentation.cargoeditor.MultipleReferenceManipulator;
import scenario.presentation.cargoeditor.NumericAttributeManipulator;
import scenario.presentation.cargoeditor.dialogs.VesselStateAttributesDialog;
import scenario.presentation.cargoeditor.importer.CSVReader;
import scenario.presentation.cargoeditor.importer.DeferredReference;
import scenario.presentation.cargoeditor.importer.EObjectImporter;
import scenario.presentation.cargoeditor.importer.EObjectImporterFactory;
import scenario.presentation.cargoeditor.importer.ImportCSVAction;
import scenario.presentation.cargoeditor.importer.ImportUI;
import scenario.presentation.cargoeditor.importer.NamedObjectRegistry;
import scenario.presentation.cargoeditor.importer.Postprocessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * Custom editor viewer pane for vessel classes
 * 
 * @author Tom Hinton
 * 
 */
public class VesselClassEVP extends NamedObjectEVP {
	public VesselClassEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	/**
	 * Because vessel classes are split into two files we need some custom code
	 * here to ask the user to select both.
	 * 
	 * For now it just displays two open dialogs, one for each file and then
	 * runs two import sessions. The second import session is customized to hook
	 * up fuel consumption curves.
	 */
	@Override
	protected Action createImportAction(final TableViewer viewer,
			final EditingDomain editingDomain, final EMFPath ePath) {
		final ImportCSVAction delegate = (ImportCSVAction) super
				.createImportAction(viewer, editingDomain, ePath);
		return new ImportCSVAction() {
			@Override
			public void run() {
				try {
					ImportUI.beginImport();
					final FileDialog dialog = new FileDialog(PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getShell(), SWT.OPEN);

					dialog.setFilterExtensions(new String[] { "*.csv" });
					dialog.setText("Select the vessel class file");
					final String vcFile = dialog.open();
					if (vcFile == null)
						return;
					dialog.setText("Now select the fuel curves file");
					final String fcFile = dialog.open();
					if (fcFile == null)
						return;

					final ArrayList<DeferredReference> defer = new ArrayList<DeferredReference>();
					final NamedObjectRegistry reg = new NamedObjectRegistry();
					reg.addEObjects((EObject) viewer.getInput());

					final EObjectImporter vci = EObjectImporterFactory
							.getInstance().getImporter(
									FleetPackage.eINSTANCE.getVesselClass());

					final CSVReader reader = new CSVReader(vcFile);
					final Collection<EObject> vesselClasses = vci
							.importObjects(reader, defer, reg);

					// register new objects
					for (final EObject object : vesselClasses) {
						reg.addEObject(object);
					}
					// add stuff to scenario and re-register names

					final EObjectImporter fcImporter = EObjectImporterFactory
							.getInstance().getImporter(
									FleetPackage.eINSTANCE
											.getFuelConsumptionLine());

					final CSVReader reader2 = new CSVReader(fcFile);
					fcImporter.importObjects(reader2, defer, reg);

					for (final DeferredReference r : defer) {
						r.setRegistry(reg.getContents());
						r.run();
					}

					for (final EObject object : vesselClasses) {
						Postprocessor.getInstance().postprocess(object);
					}

					delegate.addObjects(vesselClasses);
					ImportUI.endImport();
				} catch (final IOException ex) {
					ImportUI.endImport();
				}
			}

			/*
			 * Because I've over-ridden the run method up there, these methods
			 * can safely return null.
			 */

			@Override
			protected EObject getToplevelObject() {
				return null;
			}

			@Override
			public void addObjects(final Collection<EObject> newObjects) {

			}

			@Override
			protected EClass getImportClass() {
				return null;
			}
		};
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		{
			final BasicAttributeManipulator capacity = new NumericAttributeManipulator(
					FleetPackage.eINSTANCE.getVesselClass_Capacity(),
					part.getEditingDomain());
			addColumn("Capacity", capacity, capacity);
		}

		{
			final MultipleReferenceManipulator capacity = new MultipleReferenceManipulator(
					FleetPackage.eINSTANCE.getVesselClass_InaccessiblePorts(),
					part.getEditingDomain(), part.getPortProvider(), namedObjectName);
			addColumn("Inaccessible Ports", capacity, capacity);
		}

		{
			final DialogFeatureManipulator laden = new DialogFeatureManipulator(
					FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(),
					part.getEditingDomain()) {
				@Override
				protected String renderValue(final Object object) {
					final VesselStateAttributes a = (VesselStateAttributes) object;
					if (a == null)
						return "";
					return "NBO: " + a.getNboRate() + " Idle NBO: "
							+ a.getIdleNBORate() + " Idle Base:"
							+ a.getIdleConsumptionRate();
				}

				@Override
				protected Object openDialogBox(final Control cellEditorWindow,
						final Object object) {
					final VesselStateAttributesDialog dlg = new VesselStateAttributesDialog(
							cellEditorWindow.getShell(),
							(SWT.DIALOG_TRIM & ~SWT.CLOSE)
									| SWT.APPLICATION_MODAL);

					return dlg.open((VesselStateAttributes) getValue(object));
				}

			};
			addColumn("Laden Fuel Usage", laden, laden);
		}

		{
			final DialogFeatureManipulator laden = new DialogFeatureManipulator(
					FleetPackage.eINSTANCE.getVesselClass_BallastAttributes(),
					part.getEditingDomain()) {

				@Override
				protected Object openDialogBox(final Control cellEditorWindow,
						final Object object) {
					final VesselStateAttributesDialog dlg = new VesselStateAttributesDialog(
							cellEditorWindow.getShell(),
							(SWT.DIALOG_TRIM & ~SWT.CLOSE)
									| SWT.APPLICATION_MODAL);

					return dlg.open((VesselStateAttributes) getValue(object));
				}

				@Override
				protected String renderValue(final Object object) {
					final VesselStateAttributes a = (VesselStateAttributes) object;
					if (a == null)
						return "";
					return "NBO: " + a.getNboRate() + " Idle NBO: "
							+ a.getIdleNBORate() + " Idle Base:"
							+ a.getIdleConsumptionRate();
				}

			};
			addColumn("Ballast Fuel Usage", laden, laden);
		}
	}
}

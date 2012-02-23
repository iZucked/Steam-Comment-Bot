package com.mmxlabs.models.lng.fleet.ui.editorpart;


import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.ui.dialogs.VesselStateAttributesDialog;
import com.mmxlabs.models.lng.fleet.ui.dialogs.VesselStateAttributesDialog2;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.DialogFeatureManipulator;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;

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
		{
			final DialogFeatureManipulator laden = new DialogFeatureManipulator(FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(), editingDomain) {
				@Override
				protected String renderValue(final Object object) {
					final VesselStateAttributes a = (VesselStateAttributes) object;
					if (a == null) {
						return "";
					}
					return "NBO: " + a.getNboRate() + " Idle NBO: " + a.getIdleNBORate() + " Idle Base:" + a.getIdleBaseRate();
				}

				@Override
				protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
					final VesselStateAttributesDialog2 dialog = new VesselStateAttributesDialog2(cellEditorWindow.getShell());

					if (dialog.open((VesselStateAttributes) getValue(object), false) == Window.OK) {
						return dialog.getResult();
					} else {
						return null;
					}
				}

			};
			addColumn("Laden Fuel Usage", laden, laden);
		}

		{
			final DialogFeatureManipulator laden = new DialogFeatureManipulator(FleetPackage.eINSTANCE.getVesselClass_BallastAttributes(), editingDomain) {

				@Override
				protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
					final VesselStateAttributesDialog dlg = new VesselStateAttributesDialog(cellEditorWindow.getShell(), (SWT.DIALOG_TRIM & ~SWT.CLOSE) | SWT.APPLICATION_MODAL);

					return dlg.open((VesselStateAttributes) getValue(object), "Ballast");
				}

				@Override
				protected String renderValue(final Object object) {
					final VesselStateAttributes a = (VesselStateAttributes) object;
					if (a == null) {
						return "";
					}
					return "NBO: " + a.getNboRate() + " Idle NBO: " + a.getIdleNBORate() + " Idle Base:" + a.getIdleBaseRate();
				}

			};
			addColumn("Ballast Fuel Usage", laden, laden);
		}
		
		setTitle("Vessel Classes", PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_DEF_VIEW));
	}
}

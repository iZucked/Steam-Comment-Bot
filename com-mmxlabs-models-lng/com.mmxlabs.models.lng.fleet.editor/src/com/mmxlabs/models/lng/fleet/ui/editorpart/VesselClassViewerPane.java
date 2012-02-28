package com.mmxlabs.models.lng.fleet.ui.editorpart;


import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
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

			addTypicalColumn("Laden Fuel Usage", 
					new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(), editingDomain)
					);
			
			addTypicalColumn("Ballast Fuel Usage", 
					new VSAManipulator(FleetPackage.eINSTANCE.getVesselClass_BallastAttributes(), editingDomain)
					);

		
		setTitle("Vessel Classes", PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_DEF_VIEW));
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
			
			
//			final VesselStateAttributesDialog dlg = new VesselStateAttributesDialog(cellEditorWindow.getShell(), (SWT.DIALOG_TRIM & ~SWT.CLOSE) | SWT.APPLICATION_MODAL);

//			return dlg.open((VesselStateAttributes) getValue(object), "Ballast");
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

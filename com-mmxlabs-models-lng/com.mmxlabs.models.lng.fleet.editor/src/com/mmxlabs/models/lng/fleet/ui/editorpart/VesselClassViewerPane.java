package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
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
		
		setTitle("Vessel Classes", PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_DEF_VIEW));
	}
}

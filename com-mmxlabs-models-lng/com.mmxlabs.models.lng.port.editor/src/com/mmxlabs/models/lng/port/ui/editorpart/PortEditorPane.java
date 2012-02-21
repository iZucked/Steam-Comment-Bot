package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.port.PortCapability;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;

public class PortEditorPane extends ScenarioTableViewerPane {

	private JointModelEditorPart jointModelEditorPart;

	public PortEditorPane(final IWorkbenchPage page, final JointModelEditorPart part) {
		super(page, part);
		this.jointModelEditorPart = part;
		
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), jointModelEditorPart.getEditingDomain()));
	
		for (final PortCapability capability : PortCapability.values()) {
			addTypicalColumn(capability.getName(), new CapabilityManipulator(capability, jointModelEditorPart.getEditingDomain()));
		}
		setTitle("Ports", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}

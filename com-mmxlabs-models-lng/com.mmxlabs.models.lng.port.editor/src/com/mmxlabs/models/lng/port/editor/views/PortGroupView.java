package com.mmxlabs.models.lng.port.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.PortGroupEditorPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class PortGroupView extends ScenarioTableViewerView<PortGroupEditorPane> {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.PortGroupView";

	/**
	 * The constructor.
	 */
	public PortGroupView() {
		
	}

	@Override
	protected PortGroupEditorPane createViewerPane() {
		return new PortGroupEditorPane(getSite().getPage(), this, this);
	}

	@Override
	protected void initViewerPane(PortGroupEditorPane pane) {
		pane.init(Arrays
				.asList(new EReference[] { PortPackage.eINSTANCE
						.getPortModel_PortGroups() }), null);
		pane.getViewer().setInput(
				getRootObject().getSubModel(PortModel.class));
	}
	
}

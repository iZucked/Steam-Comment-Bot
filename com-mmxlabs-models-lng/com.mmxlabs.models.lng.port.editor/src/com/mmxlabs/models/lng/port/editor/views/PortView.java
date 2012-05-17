package com.mmxlabs.models.lng.port.editor.views;


import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.PortEditorPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class PortView extends ScenarioTableViewerView<PortEditorPane> {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.PortView";

	public PortView() {
		
	}

	@Override
	protected PortEditorPane createViewerPane() {
		return new PortEditorPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(PortEditorPane pane) {
		pane.init(Arrays
				.asList(new EReference[] { PortPackage.eINSTANCE
						.getPortModel_Ports() }), null);
		pane.getViewer().setInput(
				getRootObject().getSubModel(PortModel.class));
	}
}
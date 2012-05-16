package com.mmxlabs.models.lng.port.editor.views;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.ui.editorpart.PortGroupViewerContainer;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PortGroupView extends ScenarioInstanceView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.PortGroupView";
	private PortGroupViewerContainer ogvc;

	/**
	 * The constructor.
	 */
	public PortGroupView() {
		
	}

	@Override
	public void createPartControl(final Composite parent) {
		listenToScenarioSelection();
		
		ogvc = new PortGroupViewerContainer();
		ogvc.createViewer(parent);
	}

	@Override
	public void setFocus() {
		
	}

	@Override
	protected void displayScenarioInstance(ScenarioInstance instance) {
		super.displayScenarioInstance(instance);
		
		if (instance != null && getRootObject() != null && ogvc != null) {
			ogvc.setInput(getRootObject().getSubModel(PortModel.class));
		} else {
			if (ogvc != null) ogvc.setInput(null);
		}
	}
}

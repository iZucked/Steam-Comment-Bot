package com.mmxlabs.models.lng.port.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.PortGroupEditorPane;
import com.mmxlabs.models.ui.editorpart.ScenarioViewerPartView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PortGroupView extends ScenarioViewerPartView {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.PortGroupView";

	private Composite childComposite;

	private PortGroupEditorPane editorPane;

	
	/**
	 * The constructor.
	 */
	public PortGroupView() {
		
	}

	
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		this.childComposite = new Composite(parent, SWT.NONE);
		listenToScenarioSelection();
	}
	
	@Override
	protected void displayScenarioInstance(ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			if (editorPane != null) {
				editorPane.dispose();
				editorPane = null;
			}
			
			final Composite parent = childComposite.getParent();
			childComposite.dispose();
			childComposite = new Composite(parent, SWT.NONE);
			childComposite.setLayout(new FillLayout());
//			new Text(childComposite, SWT.NONE).setText(index ++ + " displays");
			super.displayScenarioInstance(instance);
			if (instance != null) {
				editorPane = new PortGroupEditorPane(getSite().getPage(), this, this);
				editorPane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
				editorPane.createControl(childComposite);
				editorPane.init(Arrays
						.asList(new EReference[] { PortPackage.eINSTANCE
								.getPortModel_PortGroups() }), null);
				editorPane.getViewer().setInput(
						getRootObject().getSubModel(PortModel.class));
			}
			parent.layout(true);
		}
	}
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		if (editorPane != null) {
			editorPane.setFocus();
		} else {
			childComposite.setFocus();
		}
	}
	
}

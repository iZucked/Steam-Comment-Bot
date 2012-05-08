package com.mmxlabs.models.lng.port.editor.views;


import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.PortEditorPane;
import com.mmxlabs.models.ui.editorpart.ScenarioViewerPartView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class PortView extends ScenarioViewerPartView {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.PortView";

	private Composite childComposite;

	private PortEditorPane editorPane;

	/**
	 * The constructor.
	 */
	public PortView() {
		
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
				editorPane = new PortEditorPane(getSite().getPage(), this, this);
				editorPane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
				editorPane.createControl(childComposite);
				editorPane.init(Arrays
						.asList(new EReference[] { PortPackage.eINSTANCE
								.getPortModel_Ports() }), null);
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
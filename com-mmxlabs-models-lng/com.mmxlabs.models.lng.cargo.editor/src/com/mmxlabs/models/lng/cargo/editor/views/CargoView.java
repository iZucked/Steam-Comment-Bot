package com.mmxlabs.models.lng.cargo.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelViewer;
import com.mmxlabs.models.ui.editorpart.ScenarioViewerPartView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class CargoView extends ScenarioViewerPartView {
	public static final String ID = "com.mmxlabs.models.lng.cargo.editor.CargoView";
	private Composite childComposite;
	private CargoModelViewer editorPane;

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
				editorPane = new CargoModelViewer(getSite().getPage(), this, this);
				editorPane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
				editorPane.createControl(childComposite);
				editorPane.init(Arrays
						.asList(new EReference[] { CargoPackage.eINSTANCE
								.getCargoModel_Cargos() }), null);
				editorPane.getViewer().setInput(
						getRootObject().getSubModel(CargoModel.class));
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

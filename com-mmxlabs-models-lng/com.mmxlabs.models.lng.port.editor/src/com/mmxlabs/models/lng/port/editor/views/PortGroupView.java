package com.mmxlabs.models.lng.port.editor.views;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.PortGroupViewerContainer;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
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
		
		final IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		
		final Action adder = AddModelAction.create(PortPackage.eINSTANCE.getPortGroup(), 
				new IAddContext() {
					
					@Override
					public MMXRootObject getRootObject() {
						return PortGroupView.this.getRootObject();
					}
					
					@Override
					public IScenarioEditingLocation getEditorPart() {
						return PortGroupView.this;
					}
					
					@Override
					public EReference getContainment() {
						return PortPackage.eINSTANCE.getPortModel_PortGroups();
					}
					
					@Override
					public EObject getContainer() {
						return getRootObject().getSubModel(PortModel.class);
					}
					
					@Override
					public ICommandHandler getCommandHandler() {
						return PortGroupView.this.getDefaultCommandHandler();
					}
				}
				);
		
		
		if (adder != null) toolBarManager.add(adder);
		
		toolBarManager.add(new ScenarioModifyingAction() {
			
			@Override
			protected boolean isApplicableToSelection(ISelection selection) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		toolBarManager.update(true);
	}

	@Override
	public void setFocus() {
		
	}

	@Override
	protected void displayScenarioInstance(ScenarioInstance instance) {
		super.displayScenarioInstance(instance);
		
		if (instance != null && getRootObject() != null && ogvc != null) {
			ogvc.setInput(getRootObject().getSubModel(PortModel.class));
			ogvc.setEditingDomain(getEditingDomain());
		} else {
			if (ogvc != null) ogvc.setInput(null);
		}
	}
}

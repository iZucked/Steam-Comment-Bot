/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.views;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Generalises ScenarioTableViewerView to allow multiple panes.
 * 
 * @author Simon McGregor
 */
public abstract class MultiScenarioTableViewersView extends ScenarioInstanceView {
	private Composite childComposite;
	private LinkedList<ScenarioTableViewerPane> viewerPanes = new LinkedList<ScenarioTableViewerPane>();

	@Override
	public void createPartControl(final Composite parent) {
		childComposite = createChildControl(parent);
		listenToScenarioSelection();
	}
	
	protected Composite createChildControl(final Composite parent) {
		return new SashForm(parent, SWT.VERTICAL);		
	}

	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			for (ScenarioTableViewerPane pane: viewerPanes) {
				if (pane != null) {
					getSite().setSelectionProvider(null);
					pane.dispose();
				}				
			}
			
			viewerPanes.clear();

			final Composite parent = childComposite.getParent();
			childComposite.dispose();
			childComposite = createChildControl(parent);
			childComposite.setLayout(new FillLayout());

			super.displayScenarioInstance(instance);
			if (instance != null) {
				viewerPanes.addAll(createViewerPanes());
				for (ScenarioTableViewerPane pane: viewerPanes) {
					pane.createControl(childComposite);
					pane.setLocked(isLocked());
					
				}
				initViewerPanes(viewerPanes);

			}
			parent.layout(true);
		}
	}

	abstract protected EReference [] [] getPaneRootPaths();
	
	private void initViewerPanes(List<ScenarioTableViewerPane> panes) {
		
		final EditingDomain domain = getEditingDomain();		
		EReference [] [] references = getPaneRootPaths();
		
		if (domain != null) {
			for (int i = 0; i < panes.size(); i++) {
				panes.get(i).init(Arrays.asList(references[i]), getAdapterFactory(), domain.getCommandStack());
				panes.get(i).getViewer().setInput(getRootObject());
			}			
		}

	}

	@Override
	public void setFocus() {
		if (childComposite != null) {
			childComposite.setFocus();
		}
	}

	@Override
	public void setLocked(final boolean locked) {
		for (ScenarioTableViewerPane pane: viewerPanes) {
			if (pane != null) {
				pane.setLocked(locked);
			}
			
		}
		super.setLocked(locked);
	}

	abstract protected List<ScenarioTableViewerPane> createViewerPanes();

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			final EObject target = dcsd.getTarget();
			
			final EReference[][] paths = getPaneRootPaths();			
			
			for (int i = 0; i < paths.length; i++) {
				final EReference[] path = paths[i];				
				final EClass rootClass = path[path.length - 1].getEReferenceType();
				
				// dcsd target matches the root class for this pane
				if (rootClass.isInstance(target)) {
					getSite().getPage().activate(this);
					if (viewerPanes.isEmpty() == false) {
						final StructuredSelection selection = new StructuredSelection(target);
						viewerPanes.get(i).getScenarioViewer().setSelection(selection, true);
					}
				}
				
			}
		}
			
	}

}

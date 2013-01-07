/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.views;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Adds the assumption that the view will contain a {@link ScenarioTableViewerPane}
 * 
 * @author hinton
 * 
 */
public abstract class ScenarioTableViewerView<T extends ScenarioTableViewerPane> extends ScenarioInstanceView {
	private Composite childComposite;
	private T viewerPane;

	@Override
	public void createPartControl(final Composite parent) {
		this.childComposite = new Composite(parent, SWT.NONE);
		listenToScenarioSelection();
	}

	protected abstract T createViewerPane();

	protected abstract void initViewerPane(T pane);

	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			if (viewerPane != null) {
				getSite().setSelectionProvider(null);
				viewerPane.dispose();
				viewerPane = null;
			}

			final Composite parent = childComposite.getParent();
			childComposite.dispose();
			childComposite = new Composite(parent, SWT.NONE);
			childComposite.setLayout(new FillLayout());

			super.displayScenarioInstance(instance);
			if (instance != null) {
				viewerPane = createViewerPane();
				viewerPane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
				viewerPane.createControl(childComposite);
				viewerPane.setLocked(isLocked());
				initViewerPane(viewerPane);
			}
			parent.layout(true);
		}
	}

	@Override
	public void setFocus() {
		if (viewerPane != null) {
			viewerPane.setFocus();
		} else if (childComposite != null) {
			childComposite.setFocus();
		}
	}

	@Override
	public void setLocked(final boolean locked) {
		if (viewerPane != null) {
			viewerPane.setLocked(locked);
		}
		super.setLocked(locked);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	protected void setSelection(final ISelection selection, final boolean reveal) {
		if (viewerPane != null) {
			viewerPane.getScenarioViewer().setSelection(selection, reveal);
		}
	}
}

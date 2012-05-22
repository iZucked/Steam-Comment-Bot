package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collections;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ui.editorpart.CostMatrixViewer;
import com.mmxlabs.models.lng.analytics.ui.editorpart.UnitCostMatrixViewerPane;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class CostMatrixView extends ScenarioInstanceView {
	private SashForm sash;
	private UnitCostMatrixViewerPane viewerPane;
	private CostMatrixViewer matrixViewer;

	@Override
	public void createPartControl(final Composite parent) {
		sash = new SashForm(parent, SWT.VERTICAL);
		listenToScenarioSelection();

	}

	@Override
	public void setFocus() {
		sash.setFocus();
	}

	@Override
	protected void displayScenarioInstance(ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			if (viewerPane != null) {
				viewerPane.dispose();
				viewerPane = null;
			}
			if (matrixViewer != null) {
				matrixViewer.dispose();
				matrixViewer = null;
			}

			final Composite parent = sash.getParent();
			sash.dispose();
			sash = new SashForm(parent, SWT.VERTICAL);

			super.displayScenarioInstance(instance);
			if (instance != null) {
				viewerPane = new UnitCostMatrixViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
				viewerPane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
				viewerPane.createControl(sash);
				viewerPane.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getAnalyticsModel_RoundTripMatrices()), getAdapterFactory());
				viewerPane.getViewer().setInput(getRootObject().getSubModel(AnalyticsModel.class));
				final CostMatrixViewer matrixViewer = new CostMatrixViewer(getSite().getPage(), this, this, viewerPane);
				matrixViewer.createControl(sash);
			}
			parent.layout(true);
		}
	}
}

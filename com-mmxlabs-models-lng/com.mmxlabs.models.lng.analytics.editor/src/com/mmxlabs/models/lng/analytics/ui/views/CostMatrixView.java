package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collections;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.ui.editorpart.CostMatrixViewer;
import com.mmxlabs.models.lng.analytics.ui.editorpart.UnitCostMatrixViewerPane;
import com.mmxlabs.models.lng.analytics.ui.properties.UnitCostLinePropertySource;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class CostMatrixView extends ScenarioInstanceView {
	private SashForm sash;
	private UnitCostMatrixViewerPane viewerPane;
	private CostMatrixViewer matrixViewer;
	private PropertySheetPage propertySheetPage;

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
	public void setCurrentViewer(Viewer viewer) {
		
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
				getSite().setSelectionProvider(matrixViewer.getViewer());
			}
			parent.layout(true);
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			if (propertySheetPage == null) {
				propertySheetPage = new PropertySheetPage();
				propertySheetPage.setPropertySourceProvider(new IPropertySourceProvider() {
					@Override
					public IPropertySource getPropertySource(Object object) {
						if (object instanceof UnitCostLine) {
							return new UnitCostLinePropertySource((UnitCostLine)object);
						}
						return null;
					}
				});
			}
			return propertySheetPage;
		}
		return null;
	}
	
	
}

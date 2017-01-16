/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.lang.ref.WeakReference;
import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityView;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.rcp.common.editors.IPartGotoTarget;

/**
 * @author hinton
 * 
 */
@SuppressWarnings("restriction")
public class AnalyticsModelEditorContribution extends BaseJointModelEditorContribution<AnalyticsModel> implements IPropertySourceProvider, IPartGotoTarget {

	@Override
	public void addPages(Composite parent) {
		// final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		// final UnitCostMatrixViewerPane viewer = new UnitCostMatrixViewerPane(editorPart.getSite().getPage(), editorPart, editorPart);
		//
		// viewer.createControl(sash);
		// viewer.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getAnalyticsModel_RoundTripMatrices()), editorPart.getAdapterFactory());
		// viewer.getViewer().setInput(modelObject);
		//
		// final CostMatrixViewer matrixViewer = new CostMatrixViewer(editorPart.getSite().getPage(), editorPart, editorPart, viewer);
		// matrixViewer.createControl(sash);
		//
		// editorPart.setPageText(editorPart.addPage(sash), "Cost Matrices");
	}

	@Override
	public void setLocked(boolean locked) {

	}

	@Override
	public IPropertySource getPropertySource(Object object) {
		if (object instanceof UnitCostLine) {
			// return new UnitCostLinePropertySource((UnitCostLine) object);
		} else if (object instanceof IPropertySource) {
			return (IPropertySource) object;
		}
		return null;
	}

	@Override
	public void gotoTarget(Object object) {
		if (object instanceof OptionAnalysisModel) {
			OptionAnalysisModel optionAnalysisModel = (OptionAnalysisModel) object;
			final EModelService modelService = editorPart.getSite().getService(EModelService.class);
			final EPartService partService = editorPart.getSite().getService(EPartService.class);
			final MApplication application = editorPart.getSite().getService(MApplication.class);

			// Switch perspective
			final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
			for (final MPerspective p : perspectives) {
				if (p.getElementId().equals("com.mmxlabs.lingo.app.perspective.analysis")) {
					partService.switchPerspective(p);
				}
			}

			// Activate change set view
			final MPart part = partService.showPart("com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView", PartState.ACTIVATE);
			if (part != null && part.getObject() != null) {
				Object oPart = part.getObject();
				if (oPart instanceof CompatibilityView) {
					oPart = ((CompatibilityView) oPart).getView();

				}
				if (oPart instanceof OptionModellerView) {
					final OptionModellerView vesselViewerPane_View = (OptionModellerView) oPart;
					vesselViewerPane_View.setInput(optionAnalysisModel);
					WeakReference<OptionAnalysisModel> currentRoot = vesselViewerPane_View.getCurrentRoot();
					vesselViewerPane_View.setCurrentRoot(new WeakReference<OptionAnalysisModel>(optionAnalysisModel));
				}
			}
		}
	}
}

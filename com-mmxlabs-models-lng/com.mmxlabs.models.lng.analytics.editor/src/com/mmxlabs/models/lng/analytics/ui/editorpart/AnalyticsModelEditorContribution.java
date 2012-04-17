/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.util.Collections;
import java.util.EventObject;

import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.evaluation.IEvaluationService;
import com.mmxlabs.models.lng.analytics.presentation.AnalyticsEditorPlugin;
import com.mmxlabs.models.lng.analytics.ui.properties.UnitCostLinePropertySource;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

/**
 * @author hinton
 *
 */
public class AnalyticsModelEditorContribution extends BaseJointModelEditorContribution<AnalyticsModel> implements IPropertySourceProvider {
	private final Runnable evaluator = new Runnable() {
		final IEvaluationService service = AnalyticsEditorPlugin.getPlugin().getEvaluationService();
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return;
			}
			
			service.evaluate(editorPart.getRootObject(), null);
		}
	};
	
	private Thread evaluatorThread = new Thread(evaluator);
	
	@Override
	public void init(JointModelEditorPart editorPart, MMXRootObject rootObject, UUIDObject modelObject) {
		super.init(editorPart, rootObject, modelObject);
		
		editorPart.getEditingDomain().getCommandStack().addCommandStackListener(new CommandStackListener() {
			@Override
			public void commandStackChanged(EventObject event) {
				evaluatorThread.interrupt();
				evaluatorThread = new Thread(evaluator);
				evaluatorThread.start();
			}
		});
	}

	@Override
	public void addPages(Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		final UnitCostMatrixViewerPane viewer = new UnitCostMatrixViewerPane(editorPart.getSite().getPage(), editorPart);
		
		viewer.createControl(sash);
		viewer.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getAnalyticsModel_RoundTripMatrices()), editorPart.getAdapterFactory());
		viewer.getViewer().setInput(modelObject);
		
		final CostMatrixViewer matrixViewer = new CostMatrixViewer(editorPart.getSite().getPage(), editorPart, viewer);
		matrixViewer.createControl(sash);

		editorPart.setPageText(editorPart.addPage(sash), "Cost Matrices");
	}

	@Override
	public void setLocked(boolean locked) {
		
	}

	@Override
	public IPropertySource getPropertySource(Object object) {
		if (object instanceof UnitCostLine) {
			return new UnitCostLinePropertySource((UnitCostLine)object);
		}
		return null;
	}
}

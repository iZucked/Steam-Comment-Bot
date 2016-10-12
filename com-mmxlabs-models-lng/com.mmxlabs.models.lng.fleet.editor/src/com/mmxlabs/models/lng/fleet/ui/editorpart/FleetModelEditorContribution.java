/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityView;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.editor.views.VesselAndClassView;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class FleetModelEditorContribution extends BaseJointModelEditorContribution<FleetModel> {
	// private VesselClassViewerPane vesselClassViewerPane;

	@Override
	public void addPages(final Composite parent) {

	}

	@Override
	public void setLocked(final boolean locked) {
		// vesselClassViewerPane.setLocked(locked);
	}

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			final EObject target = dcsd.getTarget();

			if (target instanceof BaseFuelCost) {
				return true;
			} else if (target instanceof BaseFuel) {
				return true;

			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();

			if (target instanceof BaseFuel || target instanceof BaseFuelCost) {
				 
				final EModelService modelService = editorPart.getSite().getService(EModelService.class);
				final EPartService partService = editorPart.getSite().getService(EPartService.class);
				final MApplication application = editorPart.getSite().getService(MApplication.class);

				// Switch perspective
				final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
				for (final MPerspective p : perspectives) {
					if (p.getElementId().equals("com.mmxlabs.lingo.app.perspective.editing")) {
						partService.switchPerspective(p);
					}
				}

				// Activate change set view
				final MPart part = partService.showPart("com.mmxlabs.models.lng.fleet.editor.views.VesselAndClassView", PartState.ACTIVATE);
				if (part != null && part.getObject() != null) {
					Object oPart = part.getObject();
					if (oPart instanceof CompatibilityView) {
						oPart = ((CompatibilityView) oPart).getView();

					}
					if (oPart instanceof VesselAndClassView) {
						final VesselAndClassView vesselViewerPane_View = (VesselAndClassView) oPart;
						vesselViewerPane_View.editObject(target);
					}
				}
			}
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.fleet.FleetModel;
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
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class FleetModelEditorContribution extends BaseJointModelEditorContribution<FleetModel> {
	private VesselViewerPane_Editor vesselViewerPane;
	// private VesselClassViewerPane vesselClassViewerPane;
	private VesselEventViewerPane eventViewerPane;
	private int eventPage;

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);

		vesselViewerPane = new VesselViewerPane_Editor(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		vesselViewerPane.createControl(sash);
		vesselViewerPane.init(Lists.newArrayList(FleetPackage.eINSTANCE.getScenarioFleetModel_VesselAvailabilities()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());

		eventViewerPane = new VesselEventViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		eventViewerPane.createControl(sash);
		eventViewerPane.init(Lists.newArrayList(FleetPackage.eINSTANCE.getScenarioFleetModel_VesselEvents()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());

		vesselViewerPane.getViewer().setInput(modelObject);
		eventViewerPane.getViewer().setInput(modelObject);

		eventPage = editorPart.addPage(sash);
		editorPart.setPageText(eventPage, "Fleet");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution#lock()
	 */
	@Override
	public void setLocked(final boolean locked) {
		// vesselClassViewerPane.setLocked(locked);
		if (vesselViewerPane != null)
			vesselViewerPane.setLocked(locked);
		if (eventViewerPane != null)
			eventViewerPane.setLocked(locked);
	}

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof Vessel) {
				return true;
			} else if (dcsd.getTarget() instanceof VesselEvent) {
				return true;
			} else if (dcsd.getTarget() instanceof ElementAssignment) {
				final ElementAssignment elementAssignment = (ElementAssignment) dcsd.getTarget();
				if (elementAssignment.getAssignedObject() instanceof VesselEvent) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			editorPart.setActivePage(eventPage);
			if (dcsd.getTarget() instanceof Vessel) {
				final Vessel vessel = (Vessel) dcsd.getTarget();
				vesselViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vessel), true);
			} else if (dcsd.getTarget() instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) dcsd.getTarget();
				eventViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vesselEvent), true);
			} else if (dcsd.getTarget() instanceof ElementAssignment) {
				final ElementAssignment elementAssignment = (ElementAssignment) dcsd.getTarget();
				if (elementAssignment.getAssignedObject() instanceof VesselEvent) {
					eventViewerPane.getScenarioViewer().setSelection(new StructuredSelection(elementAssignment.getAssignedObject()), true);
				}
			}
		}
	}
}

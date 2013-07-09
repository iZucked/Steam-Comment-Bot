/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * @since 3.0
 */
public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int tradesViewerPageNumber = -1;
	private TradesWiringViewer tradesViewer;

	@Override
	public void addPages(final Composite parent) {
		this.tradesViewer = new TradesWiringViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		tradesViewer.createControl(parent);
		tradesViewer.init(Collections.<EReference> emptyList(), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		tradesViewer.getViewer().setInput(modelObject);
		tradesViewerPageNumber = editorPart.addPage(tradesViewer.getControl());
		editorPart.setPageText(tradesViewerPageNumber, "Trades");
	}

	@Override
	public void setLocked(final boolean locked) {
		if (tradesViewer != null) {
			tradesViewer.setLocked(locked);
		}
	}

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof Cargo) {
				return true;
			} else if (dcsd.getTarget() instanceof LoadSlot) {
				return true;
			} else if (dcsd.getTarget() instanceof DischargeSlot) {
				return true;
			} else if (dcsd.getTarget() instanceof ElementAssignment) {
				final ElementAssignment elementAssignment = (ElementAssignment) dcsd.getTarget();
				if (elementAssignment.getAssignedObject() instanceof Cargo) {
					return true;
				}
			} else if (dcsd.getTarget() instanceof AssignmentModel) {
				for (final EObject object : dcsd.getObjects()) {
					if (object instanceof Cargo) {
						return true;
					} else if (object instanceof LoadSlot) {
						return true;
					} else if (object instanceof DischargeSlot) {
						return true;
					}
				}

			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			Cargo cargo = null;
			LoadSlot loadSlot = null;
			DischargeSlot dischargeSlot = null;
			if (dcsd.getTarget() instanceof Cargo) {
				cargo = (Cargo) dcsd.getTarget();
			} else if (dcsd.getTarget() instanceof LoadSlot) {
				loadSlot = (LoadSlot) dcsd.getTarget();
				cargo = loadSlot.getCargo();
			} else if (dcsd.getTarget() instanceof DischargeSlot) {
				dischargeSlot = (DischargeSlot) dcsd.getTarget();
				cargo = dischargeSlot.getCargo();
			} else if (dcsd.getTarget() instanceof ElementAssignment) {
				final ElementAssignment elementAssignment = (ElementAssignment) dcsd.getTarget();
				if (elementAssignment.getAssignedObject() instanceof Cargo) {
					cargo = (Cargo) elementAssignment.getAssignedObject();
				}
			} else if (dcsd.getTarget() instanceof AssignmentModel) {
				for (final EObject object : dcsd.getObjects()) {
					if (object instanceof Cargo) {
						cargo = (Cargo) object;
					} else if (object instanceof LoadSlot) {
						loadSlot = (LoadSlot) object;
					} else if (object instanceof DischargeSlot) {
						dischargeSlot = (DischargeSlot) object;
					}
				}
			}
			if (cargo != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(cargo), true);
				}
			} else if (loadSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(loadSlot), true);
				}
			} else if (dischargeSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(dischargeSlot), true);
				}
			}
		}
	}
}

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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
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
			} else if (dcsd.getTarget() instanceof SlotContractParams) {
				return true;
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
			EObject target = dcsd.getTarget();
			if (target instanceof SlotContractParams) {
				target = target.eContainer();
			}
			if (target instanceof Cargo) {
				cargo = (Cargo) target;
			} else if (target instanceof LoadSlot) {
				loadSlot = (LoadSlot) target;
				cargo = loadSlot.getCargo();
			} else if (target instanceof DischargeSlot) {
				dischargeSlot = (DischargeSlot) target;
				cargo = dischargeSlot.getCargo();
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

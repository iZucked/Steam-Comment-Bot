/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int cargoPageNumber = -1;
	private int loadSlotPageNumber = -1;
	private int dischargeSlotPageNumber = -1;
	private int wiringPageNumber = -1;
	private int tradesViewerPageNumber = -1;
	private CargoModelViewer cargoViewerPane;
	private LoadSlotViewer loadSlotViewerPane;
	private DischargeSlotViewer dischargeSlotViewerPane;
	private CargoWiringViewer wiringViewer;
	private TradesWiringViewer tradesViewer;

	// Temp flag to turn on/off load/discharge slots during development
	private static final boolean showSlotTabs = false;
	private static final boolean useNewTradesEditor = true;

	@Override
	public void addPages(final Composite parent) {
		{
			this.cargoViewerPane = new CargoModelViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			cargoViewerPane.createControl(parent);
			cargoViewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_Cargoes()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
			cargoViewerPane.getViewer().setInput(modelObject);
			cargoPageNumber = editorPart.addPage(cargoViewerPane.getControl());
			editorPart.setPageText(cargoPageNumber, "Cargoes");
		}
		if (showSlotTabs) {
			this.loadSlotViewerPane = new LoadSlotViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			loadSlotViewerPane.createControl(parent);
			loadSlotViewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_LoadSlots()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
			loadSlotViewerPane.getViewer().setInput(modelObject);
			loadSlotPageNumber = editorPart.addPage(loadSlotViewerPane.getControl());
			editorPart.setPageText(loadSlotPageNumber, "Load Slots");
		}
		if (showSlotTabs) {
			this.dischargeSlotViewerPane = new DischargeSlotViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			dischargeSlotViewerPane.createControl(parent);
			dischargeSlotViewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_DischargeSlots()), editorPart.getAdapterFactory(), editorPart.getEditingDomain()
					.getCommandStack());
			dischargeSlotViewerPane.getViewer().setInput(modelObject);
			dischargeSlotPageNumber = editorPart.addPage(dischargeSlotViewerPane.getControl());
			editorPart.setPageText(dischargeSlotPageNumber, "Discharge Slots");
		}

		if (!useNewTradesEditor) {
			wiringViewer = new CargoWiringViewer(parent, editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			wiringPageNumber = editorPart.addPage(wiringViewer);
			editorPart.setPageText(wiringPageNumber, "Trades");
		}
		if (useNewTradesEditor) {
			this.tradesViewer = new TradesWiringViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			tradesViewer.createControl(parent);
			tradesViewer.init(Collections.<EReference> emptyList(), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
			tradesViewer.getViewer().setInput(modelObject);
			tradesViewerPageNumber = editorPart.addPage(tradesViewer.getControl());
			editorPart.setPageText(tradesViewerPageNumber, "Trades");
		}
	}

	@Override
	public void setLocked(final boolean locked) {
		cargoViewerPane.setLocked(locked);
		if (showSlotTabs) {
			loadSlotViewerPane.setLocked(locked);
			dischargeSlotViewerPane.setLocked(locked);
		}
		if (wiringViewer != null) {
			wiringViewer.setLocked(locked);
		}
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
			}
			if (cargo != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(cargo), true);
				} else {
					editorPart.setActivePage(cargoPageNumber);
					cargoViewerPane.getScenarioViewer().setSelection(new StructuredSelection(cargo), true);
				}
			} else if (loadSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(loadSlot), true);
				} else if (loadSlotViewerPane != null) {
					editorPart.setActivePage(loadSlotPageNumber);
					loadSlotViewerPane.getScenarioViewer().setSelection(new StructuredSelection(loadSlot), true);
				}
			} else if (dischargeSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(dischargeSlot), true);
				} else if (dischargeSlotViewerPane != null) {
					editorPart.setActivePage(dischargeSlotPageNumber);
					dischargeSlotViewerPane.getScenarioViewer().setSelection(new StructuredSelection(dischargeSlot), true);
				}
			}
		}
	}
}

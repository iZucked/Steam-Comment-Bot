/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 */
public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int tradesViewerPageNumber = -1;
	private TradesWiringViewer tradesViewer;
	private VesselViewerPane_Editor vesselViewerPane;
	// private VesselClassViewerPane vesselClassViewerPane;
	private VesselEventViewerPane eventViewerPane;
	private int eventPage;

	@Override
	public void addPages(final Composite parent) {

		this.tradesViewer = new TradesWiringViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		tradesViewer.createControl(parent);
		tradesViewer.init(Collections.<EReference> emptyList(), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		tradesViewer.getViewer().setInput(modelObject);
		tradesViewerPageNumber = editorPart.addPage(tradesViewer.getControl());
		editorPart.setPageText(tradesViewerPageNumber, "Trades");

		final SashForm sash = new SashForm(parent, SWT.VERTICAL);

		vesselViewerPane = new VesselViewerPane_Editor(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		vesselViewerPane.createControl(sash);
		vesselViewerPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getCargoModel_VesselAvailabilities()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());

		eventViewerPane = new VesselEventViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		eventViewerPane.createControl(sash);
		eventViewerPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getCargoModel_VesselEvents()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());

		vesselViewerPane.getViewer().setInput(modelObject);
		eventViewerPane.getViewer().setInput(modelObject);

		eventPage = editorPart.addPage(sash);
		editorPart.setPageText(eventPage, "Fleet");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(tradesViewer.getControl(), "com.mmxlabs.lingo.doc.Editor_Trades");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(vesselViewerPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Fleet");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(eventViewerPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Fleet");

	}

	@Override
	public void setLocked(final boolean locked) {
		if (tradesViewer != null) {
			tradesViewer.setLocked(locked);
		}
		if (vesselViewerPane != null)
			vesselViewerPane.setLocked(locked);
		if (eventViewerPane != null)
			eventViewerPane.setLocked(locked);
	}

	private static final Class<?>[] handledClasses = { Vessel.class, VesselAvailability.class, VesselEvent.class, HeelOptions.class, Cargo.class, LoadSlot.class, DischargeSlot.class,
			SlotContractParams.class, SlotVisit.class, EndEvent.class };

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();

			for (final Class<?> clazz : handledClasses) {
				if (clazz.isInstance(target)) {
					return true;
				}
			}
			for (EObject o : dcsd.getObjects()) {
				for (final Class<?> clazz : handledClasses) {
					if (clazz.isInstance(o)) {
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

			EObject target = dcsd.getTarget();
			// Look in child items for potentially handles classes.
			{
				boolean foundTarget = false;
				for (final Class<?> clazz : handledClasses) {
					if (clazz.isInstance(target)) {
						foundTarget = true;
						break;
					}
				}
				if (!foundTarget) {
					for (EObject o : dcsd.getObjects()) {
						for (final Class<?> clazz : handledClasses) {
							if (clazz.isInstance(o)) {
								target = o;
								foundTarget = true;
							}
						}
					}
				}

			}

			Cargo cargo = null;
			LoadSlot loadSlot = null;
			DischargeSlot dischargeSlot = null;
			if (target instanceof SlotContractParams) {
				target = target.eContainer();
			}
			if (target instanceof SlotVisit) {
				if (((SlotVisit) target).getSlotAllocation() != null) {
					target = ((SlotVisit) target).getSlotAllocation().getSlot();
				}
			}
			if (target instanceof VesselEventVisit) {
				target = ((VesselEventVisit) target).getVesselEvent();
			}
			if (target instanceof EndEvent) {
				final VesselAvailability availability = ((EndEvent) target).getSequence().getVesselAvailability();
				if (availability != null) {
					target = availability;
				}
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
					return;
				}
			} else if (loadSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(loadSlot), true);
					return;
				}
			} else if (dischargeSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(dischargeSlot), true);
					return;
				}
			}

			editorPart.setActivePage(eventPage);

			// extract viewable target from a faulty HeelOptions object
			if (target instanceof HeelOptions) {
				final EObject container = target.eContainer();
				if (container instanceof VesselAvailability) {
					target = (VesselAvailability) container;
				} else if (container instanceof CharterOutEvent) {
					target = container;
				}
			}

			if (target instanceof Vessel) {
				final Vessel vessel = (Vessel) target;
				vesselViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vessel), true);
			} else if (target instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) target;
				vesselViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vesselAvailability), true);
			} else if (target instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) target;
				eventViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vesselEvent), true);
			}

		}
	}
}

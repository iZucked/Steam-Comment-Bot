/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;

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
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
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
		// .getToolBarManager().add(item);

		{
			// IActionBars actionBars = editorPart.getEditorSite().getActionBars();
			// TODO: Tidy this up! - listener should be shared class rather than copy/paste. Better editor layout. Unsettable? Add action to default now + 90
			final ControlContribution cc = new ControlContribution("date-editor") {

				@Override
				protected Control createControl(final Composite ppparent) {

					final Composite pparent = new Composite(ppparent, SWT.NONE);
					// pparent.setText("Prompt");
					pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(4).create());

					final Label lbl = new Label(pparent, SWT.NONE);
					lbl.setText("Prompt: Start");
					lbl.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());

					final DateTime text = new DateTime(pparent, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
					text.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
					{
						final LNGPortfolioModel portfolioModel = ((LNGScenarioModel) editorPart.getRootObject()).getPortfolioModel();
						if (portfolioModel.isSetPromptPeriodStart()) {
							final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
							cal.setTime(portfolioModel.getPromptPeriodStart());
							text.setYear(cal.get(Calendar.YEAR));
							text.setMonth(cal.get(Calendar.MONTH));
							text.setDay(cal.get(Calendar.DAY_OF_MONTH));
						}
					}
					text.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(final SelectionEvent e) {

							// TODO Auto-generated method stub
							final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
							cal.clear();
							cal.set(Calendar.YEAR, text.getYear());
							cal.set(Calendar.MONTH, text.getMonth());
							cal.set(Calendar.DAY_OF_MONTH, text.getDay());
							final Command cmd = SetCommand.create(editorPart.getEditingDomain(), ((LNGScenarioModel) editorPart.getRootObject()).getPortfolioModel(),
									LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodStart(), cal.getTime());
							editorPart.getEditingDomain().getCommandStack().execute(cmd);
						}

						@Override
						public void widgetDefaultSelected(final SelectionEvent e) {

						}
					});
					final Label lbl2 = new Label(pparent, SWT.NONE);
					lbl2.setText("End");
					lbl2.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());

					final DateTime text2 = new DateTime(pparent, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
					text2.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
					{
						final LNGPortfolioModel portfolioModel = ((LNGScenarioModel) editorPart.getRootObject()).getPortfolioModel();
						if (portfolioModel.isSetPromptPeriodEnd()) {
							final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
							cal.setTime(portfolioModel.getPromptPeriodEnd());
							text2.setYear(cal.get(Calendar.YEAR));
							text2.setMonth(cal.get(Calendar.MONTH));
							text2.setDay(cal.get(Calendar.DAY_OF_MONTH));
						}
					}
					text2.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(final SelectionEvent e) {
							final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
							cal.clear();
							cal.set(Calendar.YEAR, text2.getYear());
							cal.set(Calendar.MONTH, text2.getMonth());
							cal.set(Calendar.DAY_OF_MONTH, text2.getDay());
							final Command cmd = SetCommand.create(editorPart.getEditingDomain(), ((LNGScenarioModel) editorPart.getRootObject()).getPortfolioModel(),
									LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodEnd(), cal.getTime());
							editorPart.getEditingDomain().getCommandStack().execute(cmd);
						}

						@Override
						public void widgetDefaultSelected(final SelectionEvent e) {

						}
					});
					return pparent;
				}

			};

			tradesViewer.getToolBarManager().add(cc);
			tradesViewer.getToolBarManager().update(true);
		}

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

	@Override
	public boolean canHandle(final IStatus status) {

		final Class<?>[] handledClasses = { Vessel.class, VesselAvailability.class, VesselEvent.class, HeelOptions.class, Cargo.class, LoadSlot.class, DischargeSlot.class, SlotContractParams.class,
				SlotVisit.class, EndEvent.class };

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();

			for (final Class<?> clazz : handledClasses) {
				if (clazz.isInstance(target)) {
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
			Cargo cargo = null;
			LoadSlot loadSlot = null;
			DischargeSlot dischargeSlot = null;
			EObject target = dcsd.getTarget();
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

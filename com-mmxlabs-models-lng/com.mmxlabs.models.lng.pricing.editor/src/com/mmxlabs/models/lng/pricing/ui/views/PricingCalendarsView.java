/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.views;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.grid.internal.IScrollBarProxy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.editorpart.PricingCalendarsViewerPane;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PricingCalendarsView extends ScenarioTableViewerView<PricingCalendarsViewerPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.PricingCalendarsView";
	
	private ComboViewer calendarSelectionViewer;
	private PricingModel pricingModel;
	private String lastSelectedCalendar;
	
	private Adapter calendarListener = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.isTouch()) {
				return;
			}

			if (msg.getFeature() == PricingPackage.Literals.PRICING_MODEL__PRICING_CALENDARS) {
				if (calendarSelectionViewer != null) {

					List<PricingCalendar> models = pricingModel.getPricingCalendars().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
					calendarSelectionViewer.setInput(models);
					PricingCalendar selectedCalendar = null;
					if (!models.isEmpty()) {
						if (lastSelectedCalendar != null) {
							for (PricingCalendar cal : pricingModel.getPricingCalendars()) {
								if (lastSelectedCalendar.equals(cal.getName())) {
									selectedCalendar = cal;
									break;
								}
							}
						}
						if (selectedCalendar == null) {
							selectedCalendar = pricingModel.getPricingCalendars().get(0);
						}
						lastSelectedCalendar = selectedCalendar.getName();
						calendarSelectionViewer.setSelection(new StructuredSelection(selectedCalendar));
					}
				}
			}
		}
	};
	
	@Override
	protected PricingCalendarsViewerPane createViewerPane() {
		return new PricingCalendarsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final PricingCalendarsViewerPane pane) {
		
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			
			if (this.pricingModel == null) {
				pricingModel = ScenarioModelUtil.getPricingModel(getScenarioDataProvider());
				pricingModel.eAdapters().add(calendarListener);
			} else {
				pricingModel.eAdapters().add(calendarListener);
			}
			final List<PricingCalendar> holidays = pricingModel.getPricingCalendars();
			calendarSelectionViewer.setContentProvider(new ArrayContentProvider());
			calendarSelectionViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(final Object element) {
					return ((PricingCalendar) element).getName();
				}
			});
			calendarSelectionViewer.setInput(holidays);
			
			pane.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingCalendar_Entries() }), getAdapterFactory(), getModelReference());
			pane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			
			calendarSelectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(final SelectionChangedEvent event) {
					final PricingCalendar hc = (PricingCalendar) ((IStructuredSelection) calendarSelectionViewer.getSelection()).getFirstElement();
					pane.setInput(hc);
				}
			});
		}
	}
	
	@Override
	protected Composite wrapChildComposite(final Composite composite) {
		
		final Composite sectionParent = new Composite(composite, SWT.NONE);
		sectionParent.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true).create());
		sectionParent.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		final Composite selector = new Composite(sectionParent, SWT.NONE);
		selector.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).grab(true, false).create());

		Button btn_new = new Button(selector, SWT.PUSH);
		btn_new.setText("New");
		
		btn_new.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
 
				final PricingCalendar pc = PricingFactory.eINSTANCE.createPricingCalendar();
				getModelReference().executeWithTryLock(true, 200, () -> {

					final CompoundCommand cmd = new CompoundCommand("New calendar");
					cmd.append(AddCommand.create(getEditingDomain(), pricingModel, PricingPackage.eINSTANCE.getPricingModel_PricingCalendars(), pc));

					CommandStack commandStack = getModelReference().getCommandStack();
					commandStack.execute(cmd);
					DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(PricingCalendarsView.this, pc, commandStack.getMostRecentCommand());
				});
				List<PricingCalendar> models = pricingModel.getPricingCalendars().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
				calendarSelectionViewer.setInput(models);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Label label = new Label(selector, SWT.NONE);
		label.setText("Calendar: ");
		
		calendarSelectionViewer = new ComboViewer(selector);
		
		{
			Button btn = new Button(selector, SWT.PUSH);
			btn.setText("Edit");
			btn.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selection = calendarSelectionViewer.getStructuredSelection();
					DetailCompositeDialogUtil.editSelection(PricingCalendarsView.this, selection);
					calendarSelectionViewer.refresh();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
		}
		
		{
			Button btn_remove = new Button(selector, SWT.PUSH);
			btn_remove.setText("Remove");
			
			btn_remove.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
	 
					if (calendarSelectionViewer == null) return;
					if (calendarSelectionViewer.getStructuredSelection() == null ||
							calendarSelectionViewer.getStructuredSelection().isEmpty()) return;
					final IStructuredSelection selection = calendarSelectionViewer.getStructuredSelection();
					final Object obj = selection.getFirstElement();
					if (!(obj instanceof PricingCalendar)) return;
					
					
					final PricingCalendar pc = (PricingCalendar) obj;
					getModelReference().executeWithTryLock(true, 200, () -> {

						final CompoundCommand cmd = new CompoundCommand("Remove calendar");
						cmd.append(RemoveCommand.create(getEditingDomain(), pricingModel, PricingPackage.eINSTANCE.getPricingModel_PricingCalendars(), pc));

						CommandStack commandStack = getModelReference().getCommandStack();
						commandStack.execute(cmd);
					});
					List<PricingCalendar> models = pricingModel.getPricingCalendars().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
					calendarSelectionViewer.setInput(models);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
		}
		
		selector.setLayout(new GridLayout(5, false));
		
		return sectionParent;
	}
	
	@Override
	protected void cleanUpInstance(final ScenarioInstance instance) {
		if(calendarSelectionViewer != null) {
			calendarSelectionViewer = null;
		}
		if (pricingModel != null) {
			pricingModel.eAdapters().remove(calendarListener);
			pricingModel = null;
		}
	}
}

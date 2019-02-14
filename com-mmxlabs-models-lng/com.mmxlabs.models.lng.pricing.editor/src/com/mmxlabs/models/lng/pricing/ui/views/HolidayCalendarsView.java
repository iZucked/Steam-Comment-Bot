/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.views;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.editorpart.HolidayCalendarsViewerPane;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class HolidayCalendarsView extends ScenarioTableViewerView<HolidayCalendarsViewerPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.HolidayCalendarsView";
	
	private ComboViewer calendarSelectionViewer;
	private PricingModel pricingModel;
	private String lastSelectedCalendar;
	
	private Adapter calendarListener = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.isTouch()) {
				return;
			}

			if (msg.getFeature() == PricingPackage.Literals.HOLIDAY_CALENDAR) {
				if (calendarSelectionViewer != null) {

					List<HolidayCalendar> models = pricingModel.getHolidayCalendars().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
					calendarSelectionViewer.setInput(models);
					HolidayCalendar selectedCalendar = null;
					if (!models.isEmpty()) {
						if (lastSelectedCalendar != null) {
							for (HolidayCalendar cal : pricingModel.getHolidayCalendars()) {
								if (lastSelectedCalendar.equals(cal.getName())) {
									selectedCalendar = cal;
									break;
								}
							}
						}
						if (selectedCalendar == null) {
							selectedCalendar = pricingModel.getHolidayCalendars().get(0);
						}
						lastSelectedCalendar = selectedCalendar.getName();
						calendarSelectionViewer.setSelection(new StructuredSelection(selectedCalendar));
					}
				}
			}
		}
	};
	
	@Override
	protected HolidayCalendarsViewerPane createViewerPane() {
		return new HolidayCalendarsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final HolidayCalendarsViewerPane pane) {
		
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			
			if (this.pricingModel == null) {
				pricingModel = ScenarioModelUtil.getPricingModel(getScenarioDataProvider());
				pricingModel.eAdapters().add(calendarListener);
			}
			final List<HolidayCalendar> holidays = pricingModel.getHolidayCalendars();
			calendarSelectionViewer.setContentProvider(new ArrayContentProvider());
			calendarSelectionViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(final Object element) {
					return ((HolidayCalendar) element).getName();
				}
			});
			calendarSelectionViewer.setInput(holidays);
			
			pane.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getHolidayCalendar_Entries() }), getAdapterFactory(), getModelReference());
			pane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			
			calendarSelectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(final SelectionChangedEvent event) {
					final HolidayCalendar hc = (HolidayCalendar) ((IStructuredSelection) calendarSelectionViewer.getSelection()).getFirstElement();
					pane.setInput(hc);
				}
			});
		}
	}
	
	@Override
	protected Composite wrapChildComposite(final Composite composite) {
		
		final Composite sectionParent = new Composite(composite, SWT.NONE);
		sectionParent.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true).create());
		
		calendarSelectionViewer = new ComboViewer(sectionParent);
		
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

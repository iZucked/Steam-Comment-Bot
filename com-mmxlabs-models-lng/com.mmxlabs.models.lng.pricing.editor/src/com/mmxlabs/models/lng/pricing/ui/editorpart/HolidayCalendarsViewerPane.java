/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * The {@link HolidayCalendarsViewerPane} displays data for various indices in a tree format. The {@link IndexTreeTransformer} is used to combine the different indices into an internal dynamic EObject
 * tree datastructure.
 * 
 * Note - call {@link #setInput(HolidayCalendar)} on {@link HolidayCalendarsViewerPane} rather than the {@link Viewer}.
 * 
 * @author FM
 * 
 */
public class HolidayCalendarsViewerPane extends ScenarioTableViewerPane {

	public HolidayCalendarsViewerPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location, IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("Date", new LocalDateAttributeManipulator(PricingPackage.eINSTANCE.getHolidayCalendarEntry_Date(), getCommandHandler()));
		addTypicalColumn("Comment", new BasicAttributeManipulator(PricingPackage.eINSTANCE.getHolidayCalendarEntry_Comment(), getCommandHandler()));

		// setTitle("Holiday Calendar", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	public void setInput(final HolidayCalendar data) {
		viewer.setInput(data);
	}

}

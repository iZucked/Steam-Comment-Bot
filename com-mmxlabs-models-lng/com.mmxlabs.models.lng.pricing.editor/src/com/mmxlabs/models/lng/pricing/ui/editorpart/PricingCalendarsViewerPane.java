/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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

import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.YearMonthAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * The {@link PricingCalendarsViewerPane} displays data for various indices in a tree format. The {@link IndexTreeTransformer} is used to combine the different indices into an internal dynamic EObject tree
 * datastructure.
 * 
 * Note - call {@link #setInput(PricingCalendar)} on {@link PricingCalendarsViewerPane} rather than the {@link Viewer}.
 * 
 * @author FM
 * 
 */
public class PricingCalendarsViewerPane extends ScenarioTableViewerPane {

	public PricingCalendarsViewerPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location, IActionBars actionBars) {
		super(page, part, location, actionBars);
	}
	
	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		
		final EditingDomain editingDomain = getEditingDomain();
		
		addTypicalColumn("Month", new YearMonthAttributeManipulator(PricingPackage.eINSTANCE.getPricingCalendarEntry_Month(), editingDomain));
		addTypicalColumn("Start", new LocalDateAttributeManipulator(PricingPackage.eINSTANCE.getPricingCalendarEntry_Start(), editingDomain));
		addTypicalColumn("End", new LocalDateAttributeManipulator(PricingPackage.eINSTANCE.getPricingCalendarEntry_End(), editingDomain));
		addTypicalColumn("Comment", new BasicAttributeManipulator(PricingPackage.eINSTANCE.getPricingCalendarEntry_Comment(), editingDomain));
		
		//setTitle("Holiday Calendar", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	public void setInput(final PricingCalendar data) {
		if (data == null) return;
		viewer.setInput(data);
	}
	
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.views;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.ui.editorpart.RelativeDateRangeNominationsViewerPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class NominationsView extends ScenarioTableViewerView<ScenarioTableViewerPane> {

	private static Logger logger = LoggerFactory.getLogger(NominationsView.class);

	public static final String ID = "com.mmxlabs.models.lng.nominations.editor.NominationsView";
	
	@Override
	protected ScenarioTableViewerPane createViewerPane() {
		try {
			String viewerPaneClassName = this.getConfigurationElement().getAttribute("viewerPane");
			if (viewerPaneClassName != null) {
				Class<?> cls = Class.forName(viewerPaneClassName);
				Constructor<?> cons = cls.getConstructor(IWorkbenchPage.class, IWorkbenchPart.class, IScenarioEditingLocation.class, IActionBars.class);
				Object viewerPaneObject = cons.newInstance(getSite().getPage(), this, this, getViewSite().getActionBars());
				if (viewerPaneObject instanceof ScenarioTableViewerPane) {
					return (ScenarioTableViewerPane)viewerPaneObject;
				}
				else {
					logger.error("Invalid viewerPane specified in plugin.xml: "+viewerPaneClassName+" is not derived from ScenarioTableViewerPane.");
				}
			}
		}
		catch (Exception ex) {
			logger.error("Error creating specified viewerPane object in NominationsView: ", ex);
		}

		//Default view pane in case none specified in plugin.xml.
		return new RelativeDateRangeNominationsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final ScenarioTableViewerPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { 
					LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_NominationsModel(), 
					NominationsPackage.eINSTANCE.getNominationsModel_SlotNominations() }),
					getAdapterFactory(), getModelReference());
			pane.getViewer().setInput(getRootObject());
		}
	}
}

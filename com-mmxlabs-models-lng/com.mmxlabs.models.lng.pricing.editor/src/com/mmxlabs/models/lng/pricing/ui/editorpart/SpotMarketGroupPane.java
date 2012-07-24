/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;

/**
 * Spot Market Group
 * 
 * @author Simon Goodall
 * 
 */
public class SpotMarketGroupPane extends ScenarioTableViewerPane {

	public SpotMarketGroupPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);

		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;

		addTypicalColumn("Name", new BasicAttributeManipulator(mmx.getNamedObject_Name(), getEditingDomain()));
	}

	@Override
	public void defaultSetTitle(final String string) {
		super.defaultSetTitle(string);
	}
}

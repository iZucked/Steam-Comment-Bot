/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class UnitConversionPane extends ScenarioTableViewerPane {
	public UnitConversionPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("From", new BasicAttributeManipulator(PricingPackage.Literals.UNIT_CONVERSION__FROM, getCommandHandler()));
		addTypicalColumn("To", new BasicAttributeManipulator(PricingPackage.Literals.UNIT_CONVERSION__TO, getCommandHandler()));
		addTypicalColumn("Factor", new NumericAttributeManipulator(PricingPackage.Literals.UNIT_CONVERSION__FACTOR, getCommandHandler()));

		defaultSetTitle("Conversion Factors");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_UnitConversion");
	}

	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return null;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * Charter price editor
 * 
 * @author hinton
 * 
 */
public class CharterInMarketPane extends ScenarioTableViewerPane {
	public CharterInMarketPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	private ContextMenuManager makeContextMenu(final Grid grid) {
		final ContextMenuManager listener = new ContextMenuManager(grid);
		grid.addMenuDetectListener(listener);
		return listener;
	}

	private class ContextMenuManager implements MenuDetectListener {

		private final LocalMenuHelper helper;
		private final Grid grid;

		public ContextMenuManager(final Grid grid) {
			this.grid = grid;
			helper = new LocalMenuHelper(grid);
			grid.addDisposeListener(e -> helper.dispose());
		}

		@Override
		public void menuDetected(final MenuDetectEvent e) {

			helper.clearActions();
			boolean showMenu = false;

			if (viewer.getSelection() instanceof final IStructuredSelection selection) {
				if (selection.size() == 1 && selection.getFirstElement() instanceof final CharterInMarket market) {
					final GenericCharterContract charterContract = market.getGenericCharterContract();
					if (charterContract != null) {
						helper.addAction(new RunnableAction("Edit terms", () -> DetailCompositeDialogUtil.editSingleObject(getScenarioEditingLocation(), charterContract)));
						showMenu = true;
					}
				}
			}

			if (showMenu) {
				helper.open();
			}
		}

	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		addTypicalColumn("Name ", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getCommandHandler()));

		addTypicalColumn("Vessel", new SingleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getCharterInMarket_Vessel(), getReferenceValueProviderCache(), getCommandHandler()));

		withWidth(75, //
				withTooltip("Usable as for mutliple cargoes", //
						addTypicalColumn("Active", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_Enabled(), getCommandHandler())) //
				));

		withTooltip("The number of term options available",
				addTypicalColumn("Number", new NumericAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterInMarket_SpotCharterCount(), getCommandHandler())));

		withTooltip("Usable for single cargoes on round-trip basis",
				addTypicalColumn("Nominal", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterInMarket_Nominal(), getCommandHandler())));

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MTM)) {
			addTypicalColumn("MTM", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterInMarket_Mtm(), getCommandHandler()));
		}
		withWidth(100, //
				addTypicalColumn("Terms",
						new SingleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getCharterInMarket_GenericCharterContract(), getReferenceValueProviderCache(), getCommandHandler()))//
		);

		defaultSetTitle("Charter in");

		makeContextMenu(getScenarioViewer().getGrid());
	}
}
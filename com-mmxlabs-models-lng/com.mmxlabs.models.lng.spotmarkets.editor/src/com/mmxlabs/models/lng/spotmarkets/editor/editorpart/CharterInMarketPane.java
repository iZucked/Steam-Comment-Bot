/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.DefaultToolTipProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;

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

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()) {
			@Override
			public @Nullable String render(final Object object) {

				String suffix = "";
				if (object instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) object;

					final SpotMarketsModel m = (SpotMarketsModel) charterInMarket.eContainer();
					if (m != null && m.getDefaultNominalMarket() == charterInMarket) {
						suffix = " (*)";
					}
				}

				return super.render(object) + suffix;
			}

		});

		addTypicalColumn("Enabled", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_Enabled(), getEditingDomain()));

		addTypicalColumn("Vessel Classes", new SingleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_VesselClass(), getReferenceValueProviderCache(), getEditingDomain()));

		addTypicalColumn("Count", new NumericAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterInMarket_SpotCharterCount(), getEditingDomain()));

		scenarioViewer.setToolTipProvider(new DefaultToolTipProvider() {

			@Override
			public String getToolTipText(final Object element) {

				if (element instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) element;

					final SpotMarketsModel m = (SpotMarketsModel) charterInMarket.eContainer();
					if (m != null && m.getDefaultNominalMarket() == charterInMarket) {
						return "This is the default market for nominal cargoes";
					}
				}

				return null;
			}
		});

		final MenuManager mgr = new MenuManager();
		scenarioViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			private void populateSingleSelectionMenu(final GridItem item, final GridColumn column) {
				if (item == null) {
					return;
				}

				final Object data = item.getData();
				if (data instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) data;

					mgr.removeAll();
					final SpotMarketsModel m = (SpotMarketsModel) charterInMarket.eContainer();
					if (m != null) {
						if (m.getDefaultNominalMarket() != charterInMarket) {
							menu = mgr.createContextMenu(scenarioViewer.getGrid());
							mgr.add(new SetDefaultMarketAction(m, charterInMarket));
						}
					}

					menu.setVisible(true);
				}
			}

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				if (getJointModelEditorPart().isLocked()) {
					return;
				}

				final Grid grid = getScenarioViewer().getGrid();

				final Point mousePoint = grid.toControl(new Point(e.x, e.y));
				final GridColumn column = grid.getColumn(mousePoint);

				final IStructuredSelection selection = (IStructuredSelection) getScenarioViewer().getSelection();
				final GridItem[] items = grid.getSelection();

				if (selection.size() == 1) {
					populateSingleSelectionMenu(grid.getItem(mousePoint), column);
				}
			}

		});

		defaultSetTitle("Charter In Market");
	}

	private class SetDefaultMarketAction extends Action {
		private final SpotMarketsModel spotMarketsModel;
		private final CharterInMarket charterInMarket;

		public SetDefaultMarketAction(final SpotMarketsModel spotMarketsModel, final CharterInMarket charterInMarket) {
			super("Set as default nominal market");
			this.spotMarketsModel = spotMarketsModel;
			this.charterInMarket = charterInMarket;

		}

		@Override
		public void run() {
			EditingDomain editingDomain = getJointModelEditorPart().getEditingDomain();
			Command set = SetCommand.create(editingDomain, spotMarketsModel, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DEFAULT_NOMINAL_MARKET, charterInMarket);
			CompoundCommand cc = new CompoundCommand("Set default nominal market");
			cc.append(set);
			editingDomain.getCommandStack().execute(cc);

		}
	}
}
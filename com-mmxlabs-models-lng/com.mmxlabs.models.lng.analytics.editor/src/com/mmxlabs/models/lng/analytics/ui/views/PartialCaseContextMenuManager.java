package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder.ShippingType;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class PartialCaseContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;

	private Menu menu;
	private OptionAnalysisModel optionAnalysisModel;

	public PartialCaseContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
		this.mgr = mgr;
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.viewer = viewer;
	}

	@Override
	public void menuDetected(final MenuDetectEvent e) {

		final Grid grid = viewer.getGrid();
		if (menu == null) {
			menu = mgr.createContextMenu(grid);
		}
		mgr.removeAll();

		final Point mousePoint = grid.toControl(new Point(e.x, e.y));
		final GridColumn column = grid.getColumn(mousePoint);

		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final GridItem[] items = grid.getSelection();
		if (items.length > 0) {
			mgr.add(new RunnableAction("Delete row(s)", () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));

				scenarioEditingLocation.getDefaultCommandHandler().handleCommand(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c), null, null);

			}));
		}
		if (items.length == 1) {
			final Object ed = items[0].getData();
			final PartialCaseRow row = (PartialCaseRow) ed;
			{
				if (!row.getBuyOptions().isEmpty() && !row.getSellOptions().isEmpty()) {
					mgr.add(new RunnableAction("Split row", () -> {

						final PartialCaseRow newRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, newRow));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), newRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, row.getSellOptions()));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, SetCommand.UNSET_VALUE));

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);

					}));
				}
			}

			if (column.getText().equals("Buy")) {

				if (!row.getBuyOptions().isEmpty()) {
					mgr.add(new RunnableAction("Remove buy(s)", () -> {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, SetCommand.UNSET_VALUE), row,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);

					}));
				}
			}
			if (column.getText().equals("Sell")) {
				if (!row.getSellOptions().isEmpty()) {
					mgr.add(new RunnableAction("Remove sell(s)", () -> {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, SetCommand.UNSET_VALUE), row,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);

					}));
				}
			}

			if (column.getText().equals("Shipping")) {
				if (row.getShipping() != null) {
					mgr.add(new RunnableAction("Remove shipping", () -> {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, SetCommand.UNSET_VALUE), row,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);

					}));
				}
				if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {
					mgr.add(new RunnableAction("Create Nominated", () -> {
						final NominatedShippingOption o = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, o), row,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
					}));
				} else if (AnalyticsBuilder.isNonShipped(row) == ShippingType.Shipped) {
					mgr.add(new RunnableAction("Create RT", () -> {
						final RoundTripShippingOption o = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, o), row,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
					}));
					mgr.add(new RunnableAction("Create fleet", () -> {
						final FleetShippingOption o = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
						AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, o);
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, o), row,
								AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
					}));
				}
			}
		}
		menu.setVisible(true);
	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}
}

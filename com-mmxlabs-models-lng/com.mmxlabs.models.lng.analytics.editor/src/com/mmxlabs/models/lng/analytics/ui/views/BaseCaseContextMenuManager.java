package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
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

public class BaseCaseContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;
	private @NonNull final Runnable refreshCallback;
	private OptionAnalysisModel optionAnalysisModel;
	private Menu menu;

	public BaseCaseContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr,
			@NonNull final Runnable refreshCallback) {
		this.mgr = mgr;
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.viewer = viewer;
		this.refreshCallback = refreshCallback;
	}

	@SuppressWarnings("unchecked")
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

			mgr.add(new RunnableAction("Copy to What if?", () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> {
					if (ee instanceof BaseCaseRow) {
						BaseCaseRow baseCaseRow = (BaseCaseRow) ee;
						PartialCaseRow partialCaseRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						if (baseCaseRow.getBuyOption() != null) {
							partialCaseRow.getBuyOptions().add(baseCaseRow.getBuyOption());
						}
						if (baseCaseRow.getSellOption() != null) {
							partialCaseRow.getSellOptions().add(baseCaseRow.getSellOption());
						}
						if (baseCaseRow.getShipping() != null) {
							partialCaseRow.setShipping(EcoreUtil.copy(baseCaseRow.getShipping()));
						}
						if (!(partialCaseRow.getBuyOptions().isEmpty() && partialCaseRow.getSellOptions().isEmpty() && partialCaseRow.getShipping() == null)) {
							c.add(partialCaseRow);
						}
					}
				});

				if (!c.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
							AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, c),
							optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE);
					refreshCallback.run();
				}

			}));
		}
		if (items.length == 1) {

			if (column.getText().equals("Buy")) {
				final Object ed = items[0].getData();
				final BaseCaseRow row = (BaseCaseRow) ed;
				if (row.getBuyOption() != null) {
					mgr.add(new RunnableAction("Remove buy", () -> {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, SetCommand.UNSET_VALUE), row,
								AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);

					}));
				}
			}
			if (column.getText().equals("Sell")) {
				final Object ed = items[0].getData();
				final BaseCaseRow row = (BaseCaseRow) ed;
				if (row.getSellOption() != null) {
					mgr.add(new RunnableAction("Remove sell", () -> {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, SetCommand.UNSET_VALUE), row,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);

					}));
				}
			}

			if (column.getText().equals("Shipping")) {
				final Object ed = items[0].getData();
				final BaseCaseRow row = (BaseCaseRow) ed;
				if (row.getShipping() != null) {
					mgr.add(new RunnableAction("Remove shipping", () -> {
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, SetCommand.UNSET_VALUE), row,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

					}));
				}
				if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {
					mgr.add(new RunnableAction("Create Nominated", () -> {
						final NominatedShippingOption o = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
						refreshCallback.run();

					}));
				} else if (AnalyticsBuilder.isNonShipped(row) == ShippingType.Shipped) {
					mgr.add(new RunnableAction("Create RT", () -> {
						final RoundTripShippingOption o = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));

						refreshCallback.run();
					}));
					mgr.add(new RunnableAction("Create fleet", () -> {
						final FleetShippingOption o = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
						AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, o);
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
								AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
						refreshCallback.run();
					}));
				}
			}
		}
		menu.setVisible(true);
	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}
}

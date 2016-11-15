package com.mmxlabs.models.lng.analytics.ui.views;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder.ShippingType;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
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

			if (column != null) {

				if (column.getText().equals("Buy")) {

					if (!row.getBuyOptions().isEmpty()) {
						mgr.add(new RunnableAction("Remove buy(s)", () -> {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, SetCommand.UNSET_VALUE), row,
									AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);

						}));
						if (row.getSellOptions().size() == 1) {

							final SellOption sellOption = row.getSellOptions().get(0);

							final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
							final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);

							final VesselClass vesselClass = row.getShipping().isEmpty() ? null : AnalyticsBuilder.getVesselClass(row.getShipping().get(0));
							final Port toPort = AnalyticsBuilder.getPort(sellOption);
							final ZonedDateTime sellDate = AnalyticsBuilder.getWindowStartDate(sellOption);

							if (toPort != null && sellDate != null) {
								final MenuManager dateMenu = new MenuManager("Set date using");
								dateMenu.add(new RunnableAction("max speed", () -> {

									final CompoundCommand cmd = new CompoundCommand("Change dates");
									for (final BuyOption buyOption : row.getBuyOptions()) {
										final Port fromPort = AnalyticsBuilder.getPort(buyOption);
										if (buyOption instanceof BuyOpportunity && fromPort != null) {
											if (AnalyticsBuilder.getShippingType(buyOption, sellOption) == ShippingType.NonShipped) {
												cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), buyOption, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE,
														AnalyticsBuilder.getDate(sellOption)));
											} else if (vesselClass != null) {
												final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getMaxSpeed(), RouteOption.DIRECT, fromPort, toPort, portModel);

												final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
												final LocalDate newDate = sellDate.minusDays(travelDays).toLocalDate();
												cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), buyOption, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE, newDate));
											}
										}
									}
									if (!cmd.isEmpty()) {
										scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, row, null);
									}
								}));
								if (vesselClass != null && vesselClass.getLadenAttributes() != null && vesselClass.getLadenAttributes().getServiceSpeed() > 0.0) {
									dateMenu.add(new RunnableAction("service speed", () -> {

										final CompoundCommand cmd = new CompoundCommand("Change dates");
										for (final BuyOption buyOption : row.getBuyOptions()) {
											final Port fromPort = AnalyticsBuilder.getPort(buyOption);
											if (buyOption instanceof BuyOpportunity && fromPort != null) {
												if (AnalyticsBuilder.getShippingType(buyOption, sellOption) == ShippingType.NonShipped) {
													cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), buyOption, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE,
															AnalyticsBuilder.getDate(sellOption)));
												} else if (vesselClass != null) {
													final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getLadenAttributes().getServiceSpeed(), RouteOption.DIRECT,
															fromPort, toPort, portModel);

													final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
													final LocalDate newDate = sellDate.minusDays(travelDays).toLocalDate();
													cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), buyOption, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE, newDate));
												}
											}
										}
										if (!cmd.isEmpty()) {
											scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, row, null);
										}
									}));
								}

								mgr.add(dateMenu);
							}
						}
					}
				}
				if (column.getText().equals("Sell")) {
					if (!row.getSellOptions().isEmpty()) {
						mgr.add(new RunnableAction("Remove sell(s)", () -> {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, SetCommand.UNSET_VALUE), row,
									AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);

						}));
						if (row.getBuyOptions().size() == 1) {

							final BuyOption buyOption = row.getBuyOptions().get(0);

							final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
							final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);

							final VesselClass vesselClass = row.getShipping().isEmpty() ? null : AnalyticsBuilder.getVesselClass(row.getShipping().get(0));
							final Port fromPort = AnalyticsBuilder.getPort(buyOption);
							final ZonedDateTime buyDate = AnalyticsBuilder.getWindowStartDate(buyOption);

							if (fromPort != null && buyDate != null) {
								final MenuManager dateMenu = new MenuManager("Set date using");
								dateMenu.add(new RunnableAction("max speed", () -> {

									final CompoundCommand cmd = new CompoundCommand("Change dates");
									for (final SellOption sellOption : row.getSellOptions()) {
										final Port toPort = AnalyticsBuilder.getPort(sellOption);
										if (sellOption instanceof SellOpportunity && toPort != null) {
											if (AnalyticsBuilder.getShippingType(buyOption, sellOption) == ShippingType.NonShipped) {
												cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), sellOption, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE,
														AnalyticsBuilder.getDate(buyOption)));
											} else if (vesselClass != null) {
												final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getMaxSpeed(), RouteOption.DIRECT, fromPort, toPort, portModel);

												final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
												final LocalDate newDate = buyDate.plusDays(travelDays).toLocalDate();
												cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), sellOption, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE, newDate));
											}
										}
									}
									if (!cmd.isEmpty()) {
										scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, row, null);
									}
								}));
								if (vesselClass.getLadenAttributes().getServiceSpeed() > 0.0) {

									dateMenu.add(new RunnableAction("service speed", () -> {

										final CompoundCommand cmd = new CompoundCommand("Change dates");
										for (final SellOption sellOption : row.getSellOptions()) {
											final Port toPort = AnalyticsBuilder.getPort(sellOption);
											if (sellOption instanceof SellOpportunity && toPort != null) {
												if (AnalyticsBuilder.getShippingType(buyOption, sellOption) == ShippingType.NonShipped) {
													cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), sellOption, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE,
															AnalyticsBuilder.getDate(buyOption)));
												} else if (vesselClass != null) {
													final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getLadenAttributes().getServiceSpeed(), RouteOption.DIRECT,
															fromPort, toPort, portModel);

													final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
													final LocalDate newDate = buyDate.plusDays(travelDays).toLocalDate();
													cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), sellOption, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE, newDate));
												}
											}
										}
										if (!cmd.isEmpty()) {
											scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, row, null);
										}
									}));

								}
								mgr.add(dateMenu);
							}
						}
					}
				}

				if (column.getText().equals("Shipping")) {
					if (row.getShipping() != null) {
						mgr.add(new RunnableAction("Remove shipping", () -> {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, SetCommand.UNSET_VALUE), row,
									AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);

						}));
						// mgr.add(new RunnableAction("Copy shipping to templates", () -> {
						// scenarioEditingLocation.getDefaultCommandHandler()
						// .handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel,
						// AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, EcoreUtil.copy(row.getShipping())), optionAnalysisModel,
						// AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
						//
						// }));
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
						// mgr.add(new RunnableAction("Create RT", () -> {
						// final RoundTripShippingOption o = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
						// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
						// SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, o), row,
						// AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						// DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
						// }));
						// mgr.add(new RunnableAction("Create fleet", () -> {
						// final FleetShippingOption o = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
						// AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, o);
						// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
						// SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, o), row,
						// AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						// DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
						// }));
					}
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

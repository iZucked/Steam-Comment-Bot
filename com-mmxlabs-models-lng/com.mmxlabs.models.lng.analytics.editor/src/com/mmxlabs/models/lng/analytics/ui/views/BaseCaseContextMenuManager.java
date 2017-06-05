/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
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
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class BaseCaseContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;
	private OptionAnalysisModel optionAnalysisModel;
	private Menu menu;

	public BaseCaseContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
		this.mgr = mgr;
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.viewer = viewer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void menuDetected(final MenuDetectEvent e) {

		final Grid grid = viewer.getGrid();
		if (menu == null) {
			menu = mgr.createContextMenu(grid);
		}
		mgr.removeAll();
		{
			IContributionItem[] items = mgr.getItems();
			mgr.removeAll();
			for (IContributionItem mItem : items) {
				mItem.dispose();
			}
		}
		
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

			mgr.add(new RunnableAction("Copy to options", () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> {
					if (ee instanceof BaseCaseRow) {
						final BaseCaseRow baseCaseRow = (BaseCaseRow) ee;
						final PartialCaseRow partialCaseRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						if (baseCaseRow.getBuyOption() != null) {
							partialCaseRow.getBuyOptions().add(baseCaseRow.getBuyOption());
						}
						if (baseCaseRow.getSellOption() != null) {
							partialCaseRow.getSellOptions().add(baseCaseRow.getSellOption());
						}
						if (baseCaseRow.getShipping() != null) {
							partialCaseRow.getShipping().add(baseCaseRow.getShipping());
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
				}
			}));
		}
		if (items.length == 1) {
			if (column != null) {
				if (column.getText().equals("Buy")) {
					final Object ed = items[0].getData();
					final BaseCaseRow row = (BaseCaseRow) ed;
					if (row.getBuyOption() != null) {
						mgr.add(new RunnableAction("Remove buy", () -> {
							scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, SetCommand.UNSET_VALUE), row,
									AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);

						}));

						if (row.getBuyOption() instanceof BuyOpportunity && row.getSellOption() != null && row.getShipping() != null) {
							final BuyOpportunity opportunity = (BuyOpportunity) row.getBuyOption();

							if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {
								mgr.add(new RunnableAction("Match dates", () -> {
									scenarioEditingLocation.getDefaultCommandHandler().handleCommand(SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity,
											AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE, AnalyticsBuilder.getDate(row.getSellOption())), opportunity,
											AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);

								}));
							} else {

								final VesselClass vesselClass = AnalyticsBuilder.getVesselClass(row.getShipping());
								final Port fromPort = AnalyticsBuilder.getPort(row.getBuyOption());
								final Port toPort = AnalyticsBuilder.getPort(row.getSellOption());
								final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
								final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
								final ZonedDateTime sellDate = AnalyticsBuilder.getWindowStartDate(row.getSellOption());

								if (vesselClass != null && fromPort != null && toPort != null && sellDate != null) {
									final MenuManager dateMenu = new MenuManager("Set date using");
									dateMenu.add(new RunnableAction("max speed", () -> {
										final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getMaxSpeed(), RouteOption.DIRECT, fromPort, toPort, portModel);

										final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
										final LocalDate newDate = sellDate.minusDays(travelDays).toLocalDate();

										scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
												SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE, newDate), opportunity,
												AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);

									}));
									if (vesselClass.getLadenAttributes().getServiceSpeed() > 0.0) {

										dateMenu.add(new RunnableAction("service speed", () -> {
											final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getLadenAttributes().getServiceSpeed(), RouteOption.DIRECT, fromPort,
													toPort, portModel);

											final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
											final LocalDate newDate = sellDate.minusDays(travelDays).toLocalDate();

											scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
													SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE, newDate), opportunity,
													AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);

										}));
									}
									// dateMenu.add(new RunnableAction("NBO speed", () -> {
									//
									// double cv = AnalyticsBuilder.getCargoCV(row.getBuyOption());
									// double nboRate = vesselClass.getLadenAttributes().getNboRate();
									// BaseFuel baseFuel = vesselClass.getBaseFuel();
									//
									// double nboEquiv = nboRate * cv * baseFuel.getEquivalenceFactor();
									// // TODO: Linear interopolate speed
									// double nboSpeed = vesselClass.getMaxSpeed();
									//
									// final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, nboSpeed, RouteOption.DIRECT, fromPort, toPort, portModel);
									//
									// final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
									// final ZonedDateTime sellDate = AnalyticsBuilder.getWindowStartDate(row.getSellOption());
									// final LocalDate newDate = sellDate.minusDays(travelDays).toLocalDate();
									//
									// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									// SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE, newDate), opportunity,
									// AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);
									// }));
									// dateMenu.add(new RunnableAction("choose speed", () -> {
									//
									// }));

									mgr.add(dateMenu);
								}
							}
						}
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

						if (row.getBuyOption() != null && row.getSellOption() instanceof SellOpportunity && row.getShipping() != null) {
							final SellOpportunity opportunity = (SellOpportunity) row.getSellOption();

							if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {
								mgr.add(new RunnableAction("Match dates", () -> {
									scenarioEditingLocation.getDefaultCommandHandler().handleCommand(SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity,
											AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE, AnalyticsBuilder.getDate(row.getBuyOption())), opportunity,
											AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);
								}));
							} else {

								final VesselClass vesselClass = AnalyticsBuilder.getVesselClass(row.getShipping());
								final Port fromPort = AnalyticsBuilder.getPort(row.getBuyOption());
								final Port toPort = AnalyticsBuilder.getPort(row.getSellOption());
								final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
								final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
								final ZonedDateTime buyDate = AnalyticsBuilder.getWindowStartDate(row.getBuyOption());

								if (vesselClass != null && fromPort != null && toPort != null && buyDate != null) {
									final MenuManager dateMenu = new MenuManager("Set date using");
									dateMenu.add(new RunnableAction("max speed", () -> {
										final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getMaxSpeed(), RouteOption.DIRECT, fromPort, toPort, portModel);

										final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
										final LocalDate newDate = buyDate.plusDays(travelDays).toLocalDate();

										scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
												SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE, newDate), opportunity,
												AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);

									}));
									if (vesselClass.getLadenAttributes().getServiceSpeed() > 0.0) {

										dateMenu.add(new RunnableAction("service speed", () -> {
											final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, vesselClass.getLadenAttributes().getServiceSpeed(), RouteOption.DIRECT, fromPort,
													toPort, portModel);

											final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
											final LocalDate newDate = buyDate.plusDays(travelDays).toLocalDate();

											scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
													SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE, newDate), opportunity,
													AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);

										}));
									}
									// dateMenu.add(new RunnableAction("NBO speed", () -> {
									//
									// double cv = AnalyticsBuilder.getCargoCV(row.getBuyOption());
									// double nboRate = vesselClass.getLadenAttributes().getNboRate();
									// BaseFuel baseFuel = vesselClass.getBaseFuel();
									//
									// double nboEquiv = nboRate * cv * baseFuel.getEquivalenceFactor();
									// // TODO: Linear interopolate speed
									// double nboSpeed = vesselClass.getMaxSpeed();
									//
									// final int travelHours = TravelTimeUtils.getTimeForRoute(vesselClass, nboSpeed, RouteOption.DIRECT, fromPort, toPort, portModel);
									//
									// final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
									// final ZonedDateTime buyDate = AnalyticsBuilder.getWindowStartDate(row.getBuyOption());
									// final LocalDate newDate = buyDate.plusDays(travelDays).toLocalDate();
									//
									// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
									// SetCommand.create(scenarioEditingLocation.getEditingDomain(), opportunity, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE, newDate), opportunity,
									// AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);
									//
									// }));
									// dateMenu.add(new RunnableAction("choose speed", () -> {
									//
									// }));

									mgr.add(dateMenu);
								}
							}
						}
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
						// mgr.add(new RunnableAction("Copy shipping to templates", () -> {
						// scenarioEditingLocation.getDefaultCommandHandler()
						// .handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel,
						// AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, EcoreUtil.copy(row.getShipping())), optionAnalysisModel,
						// AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
						//
						// }));
					}
					if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {

						// mgr.add(new RunnableAction("Create Nominated", () -> {
						// // final NominatedShippingOption o = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
						// // scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
						// // SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
						// // AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						// // DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
						// NominatedShippingOption opt = AnalyticsBuilder.getOrCreatNominatedShippingOption(optionAnalysisModel, vessel);
						// if (opt.eContainer() == null) {
						// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
						// AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
						// optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
						// }
						//
						// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
						// SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), row,
						// AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						// }));
					} else if (AnalyticsBuilder.isNonShipped(row) == ShippingType.Shipped) {
						// mgr.add(new RunnableAction("Create RT", () -> {
						// final RoundTripShippingOption o = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
						// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
						// SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
						// AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						// DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(o));
						// }));
						// mgr.add(new RunnableAction("Create fleet", () -> {
						// final FleetShippingOption o = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
						// AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, o);
						// scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
						// SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
						// AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
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

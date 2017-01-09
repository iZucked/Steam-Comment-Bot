/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.rcp.common.menus.SubLocalMenuHelper;

/**
 * Helper class to generate the buy and sell options context menus.
 * 
 * @author Simon Goodall
 *
 */
public class OptionMenuHelper {

	public static MouseListener createNewBuyOptionMenuListener(final Composite parent, final IScenarioEditingLocation scenarioEditingLocation, final Supplier<OptionAnalysisModel> modelSupplier) {
		return createNewOptionMenuListener(parent, scenarioEditingLocation, modelSupplier, true);
	}

	public static MouseListener createNewSellOptionMenuListener(final Composite parent, final IScenarioEditingLocation scenarioEditingLocation, final Supplier<OptionAnalysisModel> modelSupplier) {
		return createNewOptionMenuListener(parent, scenarioEditingLocation, modelSupplier, false);
	}

	public static <T extends Slot, U extends EObject> MouseListener createNewOptionMenuListener(final Composite parent, final IScenarioEditingLocation scenarioEditingLocation,
			final Supplier<OptionAnalysisModel> modelSupplier, final boolean purchase) {

		final EStructuralFeature containerFeature;

		final Function<T, EObject> referenceFactory;
		final Supplier<EObject> opportunityFactory;
		final Function<SpotMarket, EObject> marketFactory;
		final Function<OptionAnalysisModel, List<T>> existingSlots;
		final ICellRenderer f;

		if (purchase) {
			containerFeature = AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS;
			f = new BuyOptionDescriptionFormatter();
			referenceFactory = s -> {
				final BuyReference row = AnalyticsFactory.eINSTANCE.createBuyReference();
				row.setSlot((LoadSlot) s);
				return row;
			};
			opportunityFactory = () -> {
				final BuyOpportunity row = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
				AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, row);
				return row;
			};
			marketFactory = market -> {
				final BuyMarket row = AnalyticsFactory.eINSTANCE.createBuyMarket();
				row.setMarket(market);
				return row;
			};
			existingSlots = model -> {

				final Set<T> used = model.getBuys().stream() //
						.filter(b -> b instanceof BuyReference) //
						.map(b -> (T) ((BuyReference) b).getSlot())//
						.collect(Collectors.toSet());
				final List<LoadSlot> list = ((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getCargoModel().getLoadSlots();

				final List<T> existing = (List<T>) list.stream() //
						.filter(s -> !used.contains(s)) //
						.filter(s -> !(s instanceof SpotSlot)) //
						.collect(Collectors.toList());

				return existing;
			};
		} else {
			containerFeature = AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS;
			f = new SellOptionDescriptionFormatter();
			referenceFactory = s -> {
				final SellReference row = AnalyticsFactory.eINSTANCE.createSellReference();
				row.setSlot((DischargeSlot) s);
				return row;
			};
			opportunityFactory = () -> {
				final SellOpportunity row = AnalyticsFactory.eINSTANCE.createSellOpportunity();
				AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, row);
				return row;
			};
			marketFactory = market -> {
				final SellMarket row = AnalyticsFactory.eINSTANCE.createSellMarket();
				row.setMarket(market);
				return row;
			};
			existingSlots = model -> {

				final Set<T> used = model.getSells().stream() //
						.filter(b -> b instanceof SellReference) //
						.map(b -> (T) ((SellReference) b).getSlot())//
						.collect(Collectors.toSet());
				final List<DischargeSlot> list = ((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getCargoModel().getDischargeSlots();

				final List<T> existing = (List<T>) list.stream() //
						.filter(s -> !used.contains(s)) //
						.filter(s -> !(s instanceof SpotSlot)) //
						.collect(Collectors.toList());

				return existing;
			};
		}

		return new MouseListener() {
			LocalMenuHelper helper = new LocalMenuHelper(parent);

			@Override
			public void mouseUp(final MouseEvent e) {

			}

			@Override
			public void mouseDown(final MouseEvent e) {

				helper.clearActions();

				final OptionAnalysisModel model = modelSupplier.get();
				if (model != null) {

					helper.addAction(new RunnableAction("New", () -> {
						final EObject row = opportunityFactory.get();

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, containerFeature, row), model,
								containerFeature);
						DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row);
					}));

					final SubLocalMenuHelper existinMenuHelper = new SubLocalMenuHelper("Existing");
					helper.addSubMenu(existinMenuHelper);

					final List<T> existing = existingSlots.apply(model);
					{

						final Map<Port, List<T>> slotsByPort = existing.stream()//
								.collect(Collectors.groupingBy(Slot::getPort));
						final SubLocalMenuHelper subHelper = new SubLocalMenuHelper("By port");

						for (final Map.Entry<Port, List<T>> ee : slotsByPort.entrySet()) {
							final Port p = ee.getKey();
							assert p != null;
							final SubLocalMenuHelper portMenuHelper = new SubLocalMenuHelper(p.getName());
							for (final T s : ee.getValue()) {
								final EObject row = referenceFactory.apply(s);
								portMenuHelper.addAction(new RunnableAction(f.render(row), () -> {
									scenarioEditingLocation.getDefaultCommandHandler().handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, containerFeature, row), model,
											containerFeature);
									// DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row);
								}));
							}
							subHelper.addSubMenu(portMenuHelper);
						}
						existinMenuHelper.addSubMenu(subHelper);
					}
					{
						final Map<Contract, List<T>> slotsByContract = existing.stream()//
								.filter(s -> s.getContract() != null) //
								.collect(Collectors.groupingBy(Slot::getContract));

						final List<T> noContractSlots = existing.stream()//
								.filter(s -> s.getContract() == null) //
								.collect(Collectors.toList());

						final SubLocalMenuHelper subHelper = new SubLocalMenuHelper("By contract");
						for (final Map.Entry<Contract, List<T>> ee : slotsByContract.entrySet()) {
							final Contract p = ee.getKey();
							assert p != null;
							final SubLocalMenuHelper portMenuHelper = new SubLocalMenuHelper(p.getName());
							for (final T s : ee.getValue()) {
								final EObject row = referenceFactory.apply(s);
								portMenuHelper.addAction(new RunnableAction(f.render(row), () -> {
									scenarioEditingLocation.getDefaultCommandHandler().handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, containerFeature, row), model,
											containerFeature);
								}));
							}
							subHelper.addSubMenu(portMenuHelper);
						}
						{
							final SubLocalMenuHelper portMenuHelper = new SubLocalMenuHelper("Spot");
							for (final T s : noContractSlots) {
								final EObject row = referenceFactory.apply(s);
								portMenuHelper.addAction(new RunnableAction(f.render(row), () -> {
									scenarioEditingLocation.getDefaultCommandHandler().handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, containerFeature, row), model,
											containerFeature);
								}));
							}
							subHelper.addSubMenu(portMenuHelper);
						}
						existinMenuHelper.addSubMenu(subHelper);
					}

					final SubLocalMenuHelper marketHelper = new SubLocalMenuHelper("From market");
					final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(((LNGScenarioModel) scenarioEditingLocation.getRootObject()));

					final BiConsumer<SubLocalMenuHelper, SpotMarketGroup> menuMaker = (menuHelper, group) -> {
						for (final SpotMarket market : group.getMarkets()) {
							menuHelper.addAction(new RunnableAction(market.getName(), () -> {

								final EObject row = marketFactory.apply(market);
								scenarioEditingLocation.getDefaultCommandHandler().handleCommand(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, containerFeature, row), model,
										containerFeature);
							}));
						}
					};

					if (purchase) {
						final SubLocalMenuHelper fob = new SubLocalMenuHelper("FOB Purchase");
						menuMaker.accept(fob, spotMarketsModel.getFobPurchasesSpotMarket());
						final SubLocalMenuHelper des = new SubLocalMenuHelper("DES Purchase");
						menuMaker.accept(des, spotMarketsModel.getDesPurchaseSpotMarket());

						marketHelper.addSubMenu(fob);
						marketHelper.addSubMenu(des);
					} else {
						final SubLocalMenuHelper des = new SubLocalMenuHelper("DES Sale");
						menuMaker.accept(des, spotMarketsModel.getDesSalesSpotMarket());
						final SubLocalMenuHelper fob = new SubLocalMenuHelper("FOB Sale");
						menuMaker.accept(fob, spotMarketsModel.getFobSalesSpotMarket());

						marketHelper.addSubMenu(des);
						marketHelper.addSubMenu(fob);
					}
					helper.addSubMenu(marketHelper);

				}

				helper.open();

			}

			@Override
			public void mouseDoubleClick(final MouseEvent e) {

			}
		};

	}
}

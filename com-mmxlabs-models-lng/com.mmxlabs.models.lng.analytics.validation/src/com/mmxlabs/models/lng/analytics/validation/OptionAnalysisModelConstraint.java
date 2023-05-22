/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class OptionAnalysisModelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof final OptionAnalysisModel model) {

			if (!model.getBaseCase().isKeepExistingScenario() && model.getMode() == SandboxModeConstants.MODE_OPTIMISE) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Optimise mode needs portfolio link enabled"));
				deco.addEObjectAndFeature(model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
				deco.addEObjectAndFeature(model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__MODE);
				statuses.add(deco);
			}

			// Check for duplicated existing slots.
			{
				final Set<LoadSlot> loadSlots = new HashSet<>();
				final Set<LoadSlot> duplicatedLoadSlots = new HashSet<>();

				final Set<DischargeSlot> dischargeSlots = new HashSet<>();
				final Set<DischargeSlot> duplicatdDischargeSlots = new HashSet<>();

				// First pass, find problem slots
				processModel(model, (buy, slot) -> {
					if (!loadSlots.add(slot)) {
						duplicatedLoadSlots.add(slot);
					}
				}, (sell, slot) -> {
					if (!dischargeSlots.add(slot)) {
						duplicatdDischargeSlots.add(slot);
					}
				});
				// Second pass, report problem slots
				processModel(model, (buy, slot) -> {
					if (duplicatedLoadSlots.contains(slot)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("Existing slot used multiple times (%s).", slot.getName())));
						deco.addEObjectAndFeature(buy, AnalyticsPackage.Literals.BUY_REFERENCE__SLOT);
						statuses.add(deco);
					}
				}, (sell, slot) -> {
					if (duplicatdDischargeSlots.contains(slot)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("Existing slot used multiple times (%s).", slot.getName())));
						deco.addEObjectAndFeature(sell, AnalyticsPackage.Literals.SELL_REFERENCE__SLOT);
						statuses.add(deco);
					}
				});

			}
			// Check for unique names
			{
				final Set<String> existingNames = new HashSet<>();
				if (extraContext.getRootObject() instanceof final LNGScenarioModel sm) {
					final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
					cargoModel.getLoadSlots().stream().map(NamedObject::getName).forEach(existingNames::add);
					cargoModel.getDischargeSlots().stream().map(NamedObject::getName).forEach(existingNames::add);

					{
						final BuyOptionDescriptionFormatter renderer = new BuyOptionDescriptionFormatter();
						model.getBuys().forEach(option -> {
							if (option instanceof final BuyOpportunity op) {
								final String name = renderer.render(op);
								if (!existingNames.add(name)) {
									final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
											(IConstraintStatus) ctx.createFailureStatus(String.format("Option name is not unique (%s).", name)));
									deco.addEObjectAndFeature(op, AnalyticsPackage.Literals.BUY_OPPORTUNITY__NAME);
									statuses.add(deco);
								}
							}
						});
					}
					{
						final SellOptionDescriptionFormatter renderer = new SellOptionDescriptionFormatter();
						model.getBuys().forEach(option -> {
							if (option instanceof final SellOpportunity op) {
								final String name = renderer.render(op);
								if (!existingNames.add(name)) {
									final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
											(IConstraintStatus) ctx.createFailureStatus(String.format("Option name is not unique (%s).", name)));
									deco.addEObjectAndFeature(op, AnalyticsPackage.Literals.SELL_OPPORTUNITY__NAME);
									statuses.add(deco);
								}
							}
						});
					}
				}

			}

			// Check for unique shipping options
			{
				final Set<VesselCharter> seen = new HashSet<>();
				for (final var opt : model.getShippingTemplates()) {
					if (opt instanceof final ExistingVesselCharterOption cOpt && cOpt.getVesselCharter() != null) {
						if (!seen.add(cOpt.getVesselCharter())) {
							final String vesselName = (cOpt.getVesselCharter().getVessel() == null) ? "<no vessel>" : cOpt.getVesselCharter().getVessel().getName();
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("Vessel charter already exists (%s).", vesselName)));
							deco.addEObjectAndFeature(cOpt, AnalyticsPackage.Literals.EXISTING_VESSEL_CHARTER_OPTION__VESSEL_CHARTER);
							statuses.add(deco);
						}
					}
				}
			}
			{
				final Set<Pair<CharterInMarket, Integer>> seen = new HashSet<>();
				for (final var opt : model.getShippingTemplates()) {
					if (opt instanceof final ExistingCharterMarketOption cOpt && cOpt.getCharterInMarket() != null) {
						final Pair<CharterInMarket, Integer> key = Pair.of(cOpt.getCharterInMarket(), cOpt.getSpotIndex());
						if (!seen.add(key)) {
							final String vesselName = (cOpt.getCharterInMarket().getVessel() == null) ? "<no vessel>" : cOpt.getCharterInMarket().getVessel().getName();
							if (cOpt.getSpotIndex() == -1) {
								final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
										(IConstraintStatus) ctx.createFailureStatus(String.format("Market charter already exists (%s - nominal ).", vesselName)));
								deco.addEObjectAndFeature(cOpt, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET);
								deco.addEObjectAndFeature(cOpt, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);
								statuses.add(deco);
							} else {
								final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
										(IConstraintStatus) ctx.createFailureStatus(String.format("Market charter already exists (%s opt %d).", vesselName, cOpt.getSpotIndex() + 1)));
								deco.addEObjectAndFeature(cOpt, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET);
								deco.addEObjectAndFeature(cOpt, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);
								statuses.add(deco);
							}
						}
					}
				}
			}
		}

	}

	public void processModel(final OptionAnalysisModel model, final BiConsumer<BuyOption, LoadSlot> visitLoadSlot, final BiConsumer<SellOption, DischargeSlot> visitDischargeSlot) {
		for (final BuyOption buy : model.getBuys()) {
			if (buy instanceof final BuyReference buyReference) {
				final LoadSlot slot = buyReference.getSlot();
				if (slot != null) {
					visitLoadSlot.accept(buy, slot);
				}
			}
		}
		for (final SellOption sell : model.getSells()) {
			if (sell instanceof final SellReference sellReference) {
				final DischargeSlot slot = sellReference.getSlot();
				if (slot != null) {
					visitDischargeSlot.accept(sell, slot);
				}
			}
		}
	}
}

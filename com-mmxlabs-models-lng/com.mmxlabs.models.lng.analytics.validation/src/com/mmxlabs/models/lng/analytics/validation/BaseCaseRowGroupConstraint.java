/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowGroup;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseCaseRowGroupConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		PortModel portModel = null;
		final MMXRootObject rootObject = extraContext.getRootObject();
		if (rootObject instanceof final LNGScenarioModel lngScenarioModel) {
			portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		}
		final EObject target = ctx.getTarget();
		if (target instanceof final BaseCaseRowGroup group) {
			if (group.getRows().isEmpty()) {
				return;
			}
			final BaseCaseRow firstRow = group.getRows().get(0);
			// Check LDD rows only have a sell option
			boolean isLDD = false;
			if (group.getRows().size() > 1) {
				isLDD = true;
				for (final BaseCaseRow row : group.getRows()) {

					if (row.getBuyOption() != null && AnalyticsBuilder.isDESPurchase().test(row.getBuyOption())) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD row cannot have a DES Purchase"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
						statuses.add(deco);
					}
					if (row.getSellOption() != null && AnalyticsBuilder.isFOBSale().test(row.getSellOption())) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD row cannot have a FOB Sale"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
						statuses.add(deco);
					}

					if (row == firstRow) {
						continue;
					}
					if (row.getShipping() != null) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: second LDD row cannot have a shipping option"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
					if (row.getBuyOption() != null) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: second LDD row cannot have a buy option"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
						statuses.add(deco);
					}
					if (row.getVesselEventOption() != null) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: second LDD row cannot have a event option"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION);
						statuses.add(deco);
					}
					if (row.getOptions() != null) {
						final BaseCaseRowOptions options = row.getOptions();
						if (options.isSetLadenFuelChoice() || options.isSetLadenRoute() || options.isSetLoadDate()) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: second LDD row cannot have laden route options set"));
							deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__OPTIONS);
							if (options.isSetLadenFuelChoice()) {
								deco.addEObjectAndFeature(options, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICE);
							}
							if (options.isSetLadenRoute()) {
								deco.addEObjectAndFeature(options, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__LADEN_ROUTE);
							}
							if (options.isSetLoadDate()) {
								deco.addEObjectAndFeature(options, AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS__LOAD_DATE);
							}
							statuses.add(deco);
						}
					}
				}
			}
			if (isLDD) {
				if (firstRow.getBuyOption() == null || firstRow.getBuyOption() instanceof OpenBuy) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD cargo needs a buy option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					statuses.add(deco);
				}
				if (firstRow.getSellOption() == null || firstRow.getSellOption() instanceof OpenSell) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD cargo needs a sell option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					statuses.add(deco);
				}
				if (firstRow.getVesselEventOption() != null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD cargo cannot have a vessel event option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__VESSEL_EVENT_OPTION);
					statuses.add(deco);
				}
				if (firstRow.getShipping() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD cargo needs a shipping option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				} else {
					if (firstRow.getShipping() instanceof NominatedShippingOption) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Shipped cargo cannot use nominated ship options"));
						deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
				}

				if (firstRow.getShipping() != null && firstRow.getShipping().eContainer() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: shipping option is not present in current scenario."));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (firstRow.getShipping() != null) {
					for (int i = 0; i < group.getRows().size(); ++i) {
						final BaseCaseRow r = group.getRows().get(i);
						if (r.getSellOption() != null) {
							if (!SandboxConstraintUtils.vesselPortRestrictionsValid(firstRow.getBuyOption(), r.getSellOption(), null, firstRow.getShipping())) {
								final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
										(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: shipping option cannot visit a port in this cargo"));
								deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
								deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
								deco.addEObjectAndFeature(r, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
								statuses.add(deco);
							}

							if (!SandboxConstraintUtils.portRestrictionsValid(firstRow.getBuyOption(), r.getSellOption(), null)) {
								final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
										(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: buy or sell does not have a compatible port in this cargo"));
								deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
								deco.addEObjectAndFeature(r, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
								statuses.add(deco);
							}
							if (!SandboxConstraintUtils.vesselRestrictionsValid(firstRow.getBuyOption(), r.getSellOption(), null, firstRow.getShipping())) {
								final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
										(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: buy or sell not permitted with this shipping option"));
								deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
								deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
								deco.addEObjectAndFeature(r, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
								statuses.add(deco);
							}
						}
					}
				}

				if (firstRow.getShipping() instanceof NominatedShippingOption) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Shipped cargo cannot use nominated ship options"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				//
				Vessel vessel = null;
				if (firstRow.getShipping() instanceof final SimpleVesselCharterOption opt) {
					vessel = opt.getVessel();
				} else if (firstRow.getShipping() instanceof final RoundTripShippingOption opt) {
					vessel = opt.getVessel();
				} else if (firstRow.getShipping() instanceof final FullVesselCharterOption opt) {
					vessel = opt.getVesselCharter().getVessel();
				} else if (firstRow.getShipping() instanceof final ExistingVesselCharterOption opt) {
					if (opt.getVesselCharter() != null) {
						vessel = opt.getVesselCharter().getVessel();
					}
				} else if (firstRow.getShipping() instanceof final ExistingCharterMarketOption opt) {
					if (opt.getCharterInMarket() != null) {
						vessel = opt.getCharterInMarket().getVessel();
					}
				}

				if (vessel != null) {
					validateTravelTime(ctx, extraContext, statuses, group, portModel, vessel);
				}

				int numDischargesWithVolRange = 0;
				for (int i = 0; i < group.getRows().size(); ++i) {
					final BaseCaseRow r = group.getRows().get(i);
					if (r.getSellOption() != null) {
						// CV value not important
						final int[] sellVolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(1.0, r.getSellOption());
						if (sellVolumeInMMBTU == null || sellVolumeInMMBTU[0] != sellVolumeInMMBTU[1]) {
							numDischargesWithVolRange++;
						}
					}
				}
				if (numDischargesWithVolRange > 1) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD cargo can only have one discharge with a vol range."));
					for (int i = 0; i < group.getRows().size(); ++i) {
						final BaseCaseRow r = group.getRows().get(i);
						deco.addEObjectAndFeature(r, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					}
					statuses.add(deco);
				}

			} else if (firstRow.getVesselEventOption() != null) {
				if (firstRow.getBuyOption() != null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Event row cannot have a buy option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					statuses.add(deco);
				}
				if (firstRow.getSellOption() != null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Event row cannot have a sell option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					statuses.add(deco);
				}
				if (firstRow.getShipping() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Event row needs a shipping option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				} else {
					if (firstRow.getShipping() instanceof NominatedShippingOption) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Event row cannot use nominated ship options"));
						deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
					Vessel vessel = null;
					if (firstRow.getShipping() instanceof final SimpleVesselCharterOption opt) {
						vessel = opt.getVessel();
					} else if (firstRow.getShipping() instanceof final RoundTripShippingOption opt) {
						vessel = opt.getVessel();
					} else if (firstRow.getShipping() instanceof final FullVesselCharterOption opt) {
						vessel = opt.getVesselCharter().getVessel();
					} else if (firstRow.getShipping() instanceof final ExistingVesselCharterOption opt) {
						if (opt.getVesselCharter() != null) {
							vessel = opt.getVesselCharter().getVessel();
						}
					} else if (firstRow.getShipping() instanceof final ExistingCharterMarketOption opt) {
						if (opt.getCharterInMarket() != null) {
							vessel = opt.getCharterInMarket().getVessel();
						}
					}

					if (vessel != null) {
						validateTravelTime(ctx, extraContext, statuses, firstRow, portModel, vessel);
					}
				}
			} else {
				// LD Cargo or open row

				final ShippingType shippingType = AnalyticsBuilder.isNonShipped(firstRow);
				if (shippingType == ShippingType.Shipped) {
					if (firstRow.getBuyOption() != null && firstRow.getSellOption() != null //
							&& !(firstRow.getBuyOption() instanceof OpenBuy) && !(firstRow.getSellOption() instanceof OpenSell)) {
						if (firstRow.getShipping() == null) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point - no shipping option defined."));
							deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
							statuses.add(deco);
						}
						if (firstRow.getShipping() instanceof NominatedShippingOption) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Shipped cargo cannot use nominated ship options"));
							deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
							statuses.add(deco);
						}
						//
						Vessel vessel = null;
						if (firstRow.getShipping() instanceof final SimpleVesselCharterOption opt) {
							vessel = opt.getVessel();
						} else if (firstRow.getShipping() instanceof final RoundTripShippingOption opt) {
							vessel = opt.getVessel();
						} else if (firstRow.getShipping() instanceof final FullVesselCharterOption opt) {
							vessel = opt.getVesselCharter().getVessel();
						} else if (firstRow.getShipping() instanceof final ExistingVesselCharterOption opt) {
							if (opt.getVesselCharter() != null) {
								vessel = opt.getVesselCharter().getVessel();
							}
						} else if (firstRow.getShipping() instanceof final ExistingCharterMarketOption opt) {
							if (opt.getCharterInMarket() != null) {
								vessel = opt.getCharterInMarket().getVessel();
							}
						}

						if (vessel != null) {
							validateTravelTime(ctx, extraContext, statuses, firstRow, portModel, vessel);
						}
					}
				} else if (shippingType == ShippingType.Mixed) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: incompatible slot combination."));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					statuses.add(deco);
				}

				if (firstRow.getShipping() != null && firstRow.getShipping().eContainer() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: shipping option is not present in current scenario."));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (firstRow.getShipping() != null && !SandboxConstraintUtils.vesselPortRestrictionsValid(firstRow.getBuyOption(), firstRow.getSellOption(), null, firstRow.getShipping())) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: shipping option cannot visit a port in this cargo"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (firstRow.getShipping() != null && !SandboxConstraintUtils.portRestrictionsValid(firstRow.getBuyOption(), firstRow.getSellOption(), null)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: buy or sell does not have a compatible port in this cargo"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (firstRow.getShipping() != null && !SandboxConstraintUtils.vesselRestrictionsValid(firstRow.getBuyOption(), firstRow.getSellOption(), null, firstRow.getShipping())) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: load not permitted with this shipping option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (!SandboxConstraintUtils.checkVolumeAgainstBuyAndSell(firstRow.getBuyOption(), firstRow.getSellOption(), null)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: load and discharge volumes do not match"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					// statuses.add(deco);
				}
				if (firstRow.getShipping() != null && !SandboxConstraintUtils.checkVolumeAgainstVessel(firstRow.getBuyOption(), firstRow.getSellOption(), null, firstRow.getShipping())) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: load and discharge volumes do not match"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					// statuses.add(deco);
				}
				if (firstRow.getVesselEventOption() instanceof CharterOutOpportunity && firstRow.getShipping() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: charter out opportunity without shipping option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (firstRow.getVesselEventOption() instanceof CharterOutOpportunity && AnalyticsBuilder.isRoundTripOption(firstRow.getShipping())) {

					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: charter out opportunity with round-trip shipping option"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}

				if ((firstRow.getBuyOption() == null || firstRow.getBuyOption() instanceof OpenBuy) && firstRow.getSellOption() instanceof SellMarket) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: cannot leave market sell open"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (firstRow.getBuyOption() instanceof BuyMarket && (firstRow.getSellOption() == null || firstRow.getSellOption() instanceof OpenSell)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: cannot leave market buy open"));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}

			// Check for missing dates
			{
				int missingDates = 0;
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: Only one open date permitted"));
				for (int i = 0; i < group.getRows().size(); ++i) {
					final BaseCaseRow r = group.getRows().get(i);
					if (r.getBuyOption() != null && AnalyticsBuilder.getDate(r.getBuyOption()) == null) {
						deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
						++missingDates;
					}
					if (r.getSellOption() != null && AnalyticsBuilder.getDate(r.getSellOption()) == null) {
						deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
						++missingDates;
					}
				}
				if (missingDates > 1) {
					statuses.add(deco);
				}
			}
			//
		}
	}

	private void validateTravelTime(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses, final BaseCaseRow row, final PortModel portModel,
			final Vessel vessel) {
		final Port fromPort = AnalyticsBuilder.getPort(row.getBuyOption());
		final Port toPort = AnalyticsBuilder.getPort(row.getSellOption());
		if (fromPort != null && toPort != null && vessel != null) {

			final ZonedDateTime windowStartDate = AnalyticsBuilder.getWindowStartDate(row.getBuyOption());
			final ZonedDateTime windowEndDate = AnalyticsBuilder.getWindowEndDate(row.getSellOption());

			final double speed = vessel.getVesselOrDelegateMaxSpeed();

			final ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

			final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vessel, speed, portModel.getRoutes(), modelDistanceProvider);

			if (windowStartDate != null && windowEndDate != null) {
				final int optionDuration = AnalyticsBuilder.getDuration(row.getBuyOption());
				final int availableTime = Hours.between(windowStartDate, windowEndDate) - optionDuration;

				if (travelTime > availableTime) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Sandbox|Target: not enough travel time."),
							IStatus.WARNING);
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					statuses.add(deco);
				}
			}
		}
	}

	record DataRecord(Port port, ZonedDateTime windowStartDate, ZonedDateTime windowEndDate, int duration, BaseCaseRow row, EReference feature) {
	}

	private void validateTravelTime(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses, final BaseCaseRowGroup group, final PortModel portModel,
			final Vessel vessel) {

		if (vessel == null) {
			return;
		}
		// Crazy data structure...
		final List<DataRecord> items = new LinkedList<>();
		for (final var row : group.getRows()) {
			if (row.getBuyOption() != null) {
				final var option = row.getBuyOption();
				items.add(new DataRecord(AnalyticsBuilder.getPort(option), AnalyticsBuilder.getWindowStartDate(option), AnalyticsBuilder.getWindowStartDate(option),
						AnalyticsBuilder.getDuration(option), row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION));
			}
			if (row.getSellOption() != null) {
				final var option = row.getSellOption();
				items.add(new DataRecord(AnalyticsBuilder.getPort(option), AnalyticsBuilder.getWindowStartDate(option), AnalyticsBuilder.getWindowStartDate(option),
						AnalyticsBuilder.getDuration(option), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION));
			}
		}

		DataRecord fromRow = null;
		for (final var toRow : items) {
			if (fromRow != null) {
				final Port fromPort = fromRow.port();
				final Port toPort = toRow.port();
				if (fromPort != null && toPort != null && vessel != null) {

					final ZonedDateTime windowStartDate = fromRow.windowStartDate();
					final ZonedDateTime windowEndDate = toRow.windowEndDate();

					final double speed = vessel.getVesselOrDelegateMaxSpeed();

					final ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

					final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vessel, speed, portModel.getRoutes(), modelDistanceProvider);

					if (windowStartDate != null && windowEndDate != null) {
						final int optionDuration = fromRow.duration();
						final int availableTime = Hours.between(windowStartDate, windowEndDate) - optionDuration;

						if (travelTime > availableTime) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Sandbox|Target: not enough travel time."),
									IStatus.WARNING);
							deco.addEObjectAndFeature(fromRow.row(), fromRow.feature());
							deco.addEObjectAndFeature(toRow.row(), toRow.feature());
							statuses.add(deco);
						}
					}
				}
			}
			fromRow = toRow;
		}

	}
}

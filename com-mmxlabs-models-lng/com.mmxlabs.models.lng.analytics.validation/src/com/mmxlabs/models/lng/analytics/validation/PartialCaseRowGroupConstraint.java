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
import com.mmxlabs.common.util.QuadFunction;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowGroup;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
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
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class PartialCaseRowGroupConstraint extends AbstractModelMultiConstraint {
	public static final String viewName = "Options";

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof @NonNull final PartialCaseRowGroup group) {

			if (group.getRows().isEmpty()) {
				return;
			}

			if (!SandboxConstraintUtils.shouldValidate(group)) {
				return;
			}

			PortModel portModel = null;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof @NonNull final LNGScenarioModel lngScenarioModel) {
				portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
			}

			final IScenarioDataProvider scenarioDataProvider = extraContext.getScenarioDataProvider();
			final ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

			final PartialCaseRow firstRow = group.getRows().get(0);
			// Check LDD rows only have a sell option
			boolean isLDD = false;
			boolean isLD = false;
			if (group.getRows().size() > 1) {
				isLDD = true;
				for (final PartialCaseRow row : group.getRows()) {

					int isDES = 0;
					for (final var buy : row.getBuyOptions()) {
						if (AnalyticsBuilder.isDESPurchase().test(buy)) {
							isDES++;
						}
					}
					if (isDES > 0 && isDES == row.getBuyOptions().size()) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Starting point: LDD row needs a FOB Purchase"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
						statuses.add(deco);
					}
					int isFOB = 0;
					for (final var sell : row.getSellOptions()) {
						if (AnalyticsBuilder.isFOBSale().test(sell)) {
							isFOB++;
						}
					}
					if (isFOB > 0 && isFOB == row.getSellOptions().size()) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|" + viewName + ": LDD row needs a DES Sale"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
						statuses.add(deco);
					}

					if (row == firstRow) {
						continue;
					}
					if (!row.getShipping().isEmpty()) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|" + viewName + ": second LDD row cannot have a shipping option"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
					if (!row.getBuyOptions().isEmpty()) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|" + viewName + ": second LDD row cannot have a buy option"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
						statuses.add(deco);
					}
					if (!row.getVesselEventOptions().isEmpty()) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|" + viewName + ": second LDD row cannot have a event option"));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS);
						statuses.add(deco);
					}
					if (row.getOptions() != null) {
						final PartialCaseRowOptions options = row.getOptions();
						if (!options.getLadenFuelChoices().isEmpty() || !options.getLadenRoutes().isEmpty() || !options.getLoadDates().isEmpty()) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|" + viewName + ": second LDD row cannot have laden route options set"));
							deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__OPTIONS);
							if (!options.getLadenFuelChoices().isEmpty()) {
								deco.addEObjectAndFeature(options, AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES);
							}
							if (!options.getLadenRoutes().isEmpty()) {
								deco.addEObjectAndFeature(options, AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES);
							}
							if (!options.getLoadDates().isEmpty()) {
								deco.addEObjectAndFeature(options, AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES);
							}
							statuses.add(deco);
						}
					}
				}

				if (isLDD) {
					int numDischargesWithVolRange = 0;
					int numDischargesWithFixedRange = 0;
					for (int i = 0; i < group.getRows().size(); ++i) {
						final PartialCaseRow r = group.getRows().get(i);
						boolean foundDischargesWithVolRange = false;
						boolean foundDischargesWithFixedRange = false;
						if (!r.getSellOptions().isEmpty()) {
							for (final SellOption sell : r.getSellOptions()) {
								// CV Value not important here
								final int[] sellVolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(1.0, sell);
								if (sellVolumeInMMBTU == null || sellVolumeInMMBTU[0] != sellVolumeInMMBTU[1]) {
									if (!foundDischargesWithVolRange) {
										numDischargesWithVolRange++;
										foundDischargesWithVolRange = true;
									}
								} else {
									if (!foundDischargesWithFixedRange) {
										numDischargesWithFixedRange++;
										foundDischargesWithFixedRange = true;
									}
								}
							}
						}
					}
					if (numDischargesWithVolRange > 1) {
						final int severity = numDischargesWithFixedRange == 0 ? IStatus.ERROR : IStatus.WARNING;
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|" + viewName + ": Row contains invalid LDD cargo option. Only have one discharge with a vol range is permitted."),
								severity);
						for (int i = 0; i < group.getRows().size(); ++i) {
							final var r = group.getRows().get(i);
							deco.addEObjectAndFeature(r, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
						}
						statuses.add(deco);
					}
				}

				// Check for OpenSell in second row to see if this could be an LD option too.
				for (int i = 0; i < group.getRows().size(); ++i) {
					final var r = group.getRows().get(i);
					isLD |= r.getSellOptions().stream().anyMatch(OpenSell.class::isInstance);
				}
			}
			isLD |= !firstRow.getBuyOptions().isEmpty() && !firstRow.getSellOptions().isEmpty();
			final boolean isEvent = !firstRow.getVesselEventOptions().isEmpty();
			boolean hasShippedCargo;
			boolean hasNonShippedCargo;
			{
				long fobPurchaseCount = 0;
				long desPurchaseCount = 0;
				long fobSaleCount = 0;
				long desSaleCount = 0;

				for (final PartialCaseRow r : group.getRows()) {
					fobPurchaseCount += r.getBuyOptions().stream().filter(AnalyticsBuilder.isFOBPurchase()).count();
					desPurchaseCount += r.getBuyOptions().stream().filter(AnalyticsBuilder.isDESPurchase()).count();
					fobSaleCount += r.getSellOptions().stream().filter(AnalyticsBuilder.isFOBSale()).count();
					desSaleCount += r.getSellOptions().stream().filter(AnalyticsBuilder.isDESSale()).count();
				}

				if (desPurchaseCount > 0 && fobSaleCount > 0) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains cargo with a DES purchase and a FOB Sale", viewName)));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
					statuses.add(deco);
				}

				hasShippedCargo = fobPurchaseCount > 0 && desSaleCount > 0;
				hasNonShippedCargo = desPurchaseCount > 0 || fobSaleCount > 0;
			}

			{
				boolean hasSpotBuy = false;
				boolean hasSpotSell = false;
				for (final PartialCaseRow r : group.getRows()) {

					hasSpotBuy |= r.getBuyOptions().stream().anyMatch(BuyMarket.class::isInstance);
					hasSpotSell |= r.getSellOptions().stream().anyMatch(SellMarket.class::isInstance);
				}
				if (hasSpotBuy && hasSpotSell) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("%s - spot to spot combinations will be filtered out", viewName)), IStatus.WARNING);
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
					statuses.add(deco);
				}
			}

			// Check no shipping when required
			if (hasShippedCargo || isEvent) {
				int totalCount = 0;
				int nominated = 0;
				int roundTrip = 0;

				for (final var opt : firstRow.getShipping()) {
					if (opt instanceof NominatedShippingOption) {
						++nominated;
					}
					if (AnalyticsBuilder.isRoundTripOption(opt)) {
						++roundTrip;
					}
					++totalCount;
				}
				if (hasShippedCargo && (totalCount - nominated) == 0) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains shipped cargo and no valid shipping option", viewName)));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				if (isEvent && (totalCount - nominated - roundTrip) == 0) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains event and no valid shipping option", viewName)));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}
			{
				// Check Shipped cargo lateness and voyage duration
				validateTravelTime(ctx, extraContext, statuses, group, portModel);
			}

			final boolean volume = getVolumeValid(group);
			if (!volume) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has mismatching volume limits", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final boolean cv = getCVValid(group);
			if (!cv) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has mismatching cv limits", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final boolean vesselPorts = getVesselPortRestrictionsValid(group);
			if (!vesselPorts) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row uses a vessel that cannot visit the specified ports", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final boolean ports = getPortRestrictionsValid(group);
			if (!ports) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has incompatible port restrictions", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
				deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);

				statuses.add(deco);
			}
			final boolean vessels = getVesselRestrictionsValid(group);
			if (!vessels) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row uses a vessel that is restricted by the load slot", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			for (final ShippingOption opt : firstRow.getShipping()) {
				if (opt.eContainer() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - uncontained shipping", viewName)));
					deco.addEObjectAndFeature(firstRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}
		}
	}

	private boolean getVolumeValid(final PartialCaseRowGroup group) {

		if (!validateCargo(group, SandboxConstraintUtils::checkVolumeAgainstBuyAndSell)) {
			return false;
		}
		return validateCargo(group, (buy, sell1, sell2, shipOpt) -> {
			if (AnalyticsBuilder.isFOBPurchase().test(buy) && AnalyticsBuilder.isDESSale().test(sell1) && AnalyticsBuilder.isShipped(shipOpt)) {
				return SandboxConstraintUtils.checkVolumeAgainstVessel(buy, sell1, sell2, shipOpt);
			}
			return true;
		});
	}

	private boolean getCVValid(final PartialCaseRowGroup group) {
		return validateCargo(group, SandboxConstraintUtils::checkCVAgainstBuyAndSell);
	}

	private boolean getVesselPortRestrictionsValid(final PartialCaseRowGroup group) {
		return validateCargo(group, (buy, sell1, sell2, shipOpt) -> {
			if (AnalyticsBuilder.isFOBPurchase().test(buy) && AnalyticsBuilder.isDESSale().test(sell1) && AnalyticsBuilder.isShipped(shipOpt)) {
				return SandboxConstraintUtils.vesselPortRestrictionsValid(buy, sell1, sell2, shipOpt);
			}
			return true;
		});
	}

	private boolean getPortRestrictionsValid(final PartialCaseRowGroup group) {
		return validateCargo(group, SandboxConstraintUtils::portRestrictionsValid);
	}

	private boolean getVesselRestrictionsValid(final PartialCaseRowGroup group) {
		return validateCargo(group, (buy, sell1, sell2, shipOpt) -> {
			if (AnalyticsBuilder.isFOBPurchase().test(buy) && AnalyticsBuilder.isDESSale().test(sell1) && AnalyticsBuilder.isShipped(shipOpt)) {
				return SandboxConstraintUtils.vesselRestrictionsValid(buy, sell1, sell2, shipOpt);
			}
			return true;
		});
	}

	record DataRecord(Port port, ZonedDateTime windowStartDate, ZonedDateTime windowEndDate, int duration, PartialCaseRow row, EReference feature) {
	}

	private void validateTravelTime(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses, final PartialCaseRowGroup group,
			final PortModel portModel) {

		boolean warnedLate = false;
		boolean warnedLargeTime = false;
		final List<List<DataRecord>> allItems = new LinkedList<>();
		for (final var row : group.getRows()) {
			{
				final List<DataRecord> items = new LinkedList<>();
				for (final BuyOption option : row.getBuyOptions()) {
					if (AnalyticsBuilder.isFOBPurchase().test(option)) {
						items.add(new DataRecord(AnalyticsBuilder.getPort(option), AnalyticsBuilder.getWindowStartDate(option), AnalyticsBuilder.getWindowStartDate(option),
								AnalyticsBuilder.getDuration(option), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS));
					}
				}
				if (!items.isEmpty()) {
					allItems.add(items);
				}
			}
			{
				final List<DataRecord> items = new LinkedList<>();
				for (final SellOption option : row.getSellOptions()) {
					if (AnalyticsBuilder.isDESSale().test(option)) {
						items.add(new DataRecord(AnalyticsBuilder.getPort(option), AnalyticsBuilder.getWindowStartDate(option), AnalyticsBuilder.getWindowStartDate(option),
								AnalyticsBuilder.getDuration(option), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS));
					}
				}
				if (!items.isEmpty()) {
					allItems.add(items);
				}
			}

		}
		for (final ShippingOption option : group.getRows().get(0).getShipping()) {
			// test shipping only
			if (!AnalyticsBuilder.isShipped(option)) {
				continue;
			}
			final Vessel vessel = AnalyticsBuilder.getVessel(option);
			if (vessel == null) {
				continue;
			}

			for (int i = 1; i < allItems.size(); ++i) {
				for (final DataRecord fromRow : allItems.get(i - 1)) {
					for (final DataRecord toRow : allItems.get(i)) {
						if (fromRow != null) {
							final Port fromPort = fromRow.port();
							final Port toPort = toRow.port();
							if (fromPort != null && toPort != null && vessel != null) {

								final ZonedDateTime windowStartDate = fromRow.windowStartDate();
								final ZonedDateTime windowEndDate = toRow.windowEndDate();

								final double speed = vessel.getVesselOrDelegateMaxSpeed();

								final ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider()
										.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

								final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vessel, speed, portModel.getRoutes(), modelDistanceProvider);

								if (windowStartDate != null && windowEndDate != null) {
									final int optionDuration = fromRow.duration();
									final int availableTime = Hours.between(windowStartDate, windowEndDate) - optionDuration;

									if (travelTime > availableTime) {
										if (!warnedLate) {
											final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
													(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row will create a late cargo", viewName)), IStatus.WARNING);
											deco.addEObjectAndFeature(fromRow.row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
											statuses.add(deco);
											warnedLate = true;

											if (warnedLargeTime && warnedLate) {
												return;
											}
										}
									}

									if ((availableTime / 24) > 60) {
										if (!warnedLargeTime) {
											final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
													(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has a large travel time", viewName)), IStatus.WARNING);
											deco.addEObjectAndFeature(fromRow.row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
											statuses.add(deco);
											warnedLargeTime = true;

											if (warnedLargeTime && warnedLate) {
												return;
											}
										}
										;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean validateCargo(final PartialCaseRowGroup group, final TriFunction<BuyOption, SellOption, SellOption, Boolean> action) {

		for (final BuyOption buy : group.getRows().get(0).getBuyOptions()) {
			for (final SellOption sell1 : group.getRows().get(0).getSellOptions()) {
				if (group.getRows().size() > 1) {
					for (final SellOption sell2 : group.getRows().get(1).getSellOptions()) {
						if (sell2 instanceof OpenSell) {
							return action.apply(buy, sell1, null);
						} else {
							return action.apply(buy, sell1, null);
						}
					}
				} else {
					return action.apply(buy, sell1, null);
				}
			}
		}
		return true;
	}

	private boolean validateCargo(final PartialCaseRowGroup group, final QuadFunction<BuyOption, SellOption, SellOption, ShippingOption, Boolean> action) {

		final var firstRow = group.getRows().get(0);
		for (final ShippingOption shipOpt : firstRow.getShipping()) {

			for (final BuyOption buy : firstRow.getBuyOptions()) {
				for (final SellOption sell1 : firstRow.getSellOptions()) {
					if (group.getRows().size() > 1) {
						for (final SellOption sell2 : group.getRows().get(1).getSellOptions()) {
							if (sell2 instanceof OpenSell) {
								return action.apply(buy, sell1, null, shipOpt);
							} else {
								return action.apply(buy, sell1, null, shipOpt);
							}
						}
					} else {
						return action.apply(buy, sell1, null, shipOpt);
					}
				}
			}
		}
		return true;
	}
}

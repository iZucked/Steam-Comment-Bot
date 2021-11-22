/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
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

public class PartialCaseRowConstraint extends AbstractModelMultiConstraint {
	public static final String viewName = "Options";

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PartialCaseRow) {
			final PartialCaseRow partialCaseRow = (PartialCaseRow) target;
			PortModel portModel = null;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
			}

			final IScenarioDataProvider scenarioDataProvider = extraContext.getScenarioDataProvider();
			final ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

			final long fobPurchaseCount = partialCaseRow.getBuyOptions().stream().filter(AnalyticsBuilder.isFOBPurchase()).count();
			final long desPurchaseCount = partialCaseRow.getBuyOptions().stream().filter(AnalyticsBuilder.isDESPurchase()).count();
			final long fobSaleCount = partialCaseRow.getSellOptions().stream().filter(AnalyticsBuilder.isFOBSale()).count();
			final long desSaleCount = partialCaseRow.getSellOptions().stream().filter(AnalyticsBuilder.isDESSale()).count();

			if (desPurchaseCount > 0 && fobSaleCount > 0) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row with a DES purchase and a FOB Sale", viewName)));
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);

			}
			if (fobPurchaseCount > 0 && desSaleCount > 0 && partialCaseRow.getShipping().stream().filter(AnalyticsBuilder.isNominated()).count() > 0) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row with a FOB purchase and a DES Sale and a nominated vessel", viewName)));
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			if (fobPurchaseCount > 0 && desSaleCount > 0 && partialCaseRow.getShipping().isEmpty()) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row with a FOB purchase and a DES Sale and no shipping option", viewName)));
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final int lateness = getLateness(portModel, partialCaseRow, modelDistanceProvider);
			if (lateness < 0) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row will create a late cargo", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final int voyageDuration = getVoyageDuration(portModel, partialCaseRow, modelDistanceProvider);
			if ((voyageDuration / 24) > 60) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has a large travel time", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final boolean volume = getVolumeValid(partialCaseRow);
			if (!volume) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has mismatching volume limits", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final boolean cv = getCVValid(partialCaseRow);
			if (!cv) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has mismatching cv limits", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final boolean vesselPorts = getVesselPortRestrictionsValid(partialCaseRow);
			if (!vesselPorts) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row uses a vessel that cannot visit the specified ports", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			final boolean ports = getPortRestrictionsValid(partialCaseRow);
			if (!ports) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has incompatible port restrictions", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);

				statuses.add(deco);
			}
			final boolean vessels = getVesselRestrictionsValid(partialCaseRow);
			if (!vessels) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row uses a vessel that is restricted by the load slot", viewName)), IStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			for (final ShippingOption opt : partialCaseRow.getShipping()) {
				if (opt.eContainer() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - uncontained shipping", viewName)));
					deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}
			final boolean hasCharterOutOpportunities = partialCaseRow.getVesselEventOptions().stream().anyMatch(CharterOutOpportunity.class::isInstance);
			final boolean hasShippingOptions = !partialCaseRow.getShipping().isEmpty();
			if (hasCharterOutOpportunities && !hasShippingOptions) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s - the row contains charter out opportunities without shipping options", viewName)));
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
				statuses.add(deco);
			}
			if (hasCharterOutOpportunities) {
				int roundTripCount = 0;
				int totalCount = 0;
				for (final ShippingOption shipOpt: partialCaseRow.getShipping()) {
					if (AnalyticsBuilder.isRoundTripOption(shipOpt)) {
						roundTripCount++;
					}
					totalCount++;
				}
				if (roundTripCount > 0 && roundTripCount == totalCount) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("%s - the row contains charter out opportunities which cannot be used with round-trip shipping options", viewName)));
					deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private int getLateness(final PortModel portModel, final PartialCaseRow row, final ModelDistanceProvider modelDistanceProvider) {
		for (final BuyOption buyOption : row.getBuyOptions()) {
			for (final SellOption sellOption : row.getSellOptions()) {
				for (final ShippingOption option : row.getShipping()) {
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						final int lateness = AnalyticsBuilder.calculateLateness(buyOption, sellOption, portModel, AnalyticsBuilder.getVessel(option), modelDistanceProvider);
						if (lateness < 0) {
							return lateness;
						}
					}
				}
			}
		}
		return 0;
	}

	private int getVoyageDuration(final PortModel portModel, final PartialCaseRow row, final ModelDistanceProvider modelDistanceProvider) {
		for (final BuyOption buyOption : row.getBuyOptions()) {
			for (final SellOption sellOption : row.getSellOptions()) {
				for (final ShippingOption option : row.getShipping()) {
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {

						return AnalyticsBuilder.calculateVoyageDurationInHours(buyOption, sellOption, portModel, AnalyticsBuilder.getVessel(option), modelDistanceProvider);

					}
				}
			}
		}
		return 0;
	}

	private boolean getVolumeValid(final PartialCaseRow row) {
		for (final BuyOption buyOption : row.getBuyOptions()) {
			for (final SellOption sellOption : row.getSellOptions()) {
				for (final ShippingOption option : row.getShipping()) {
					// test slot volume
					final boolean checkVolumeAgainstBuyAndSell = SandboxConstraintUtils.checkVolumeAgainstBuyAndSell(buyOption, sellOption);
					if (!checkVolumeAgainstBuyAndSell) {
						return false;
					}
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						final boolean vesselVolume = SandboxConstraintUtils.checkVolumeAgainstVessel(buyOption, sellOption, option);
						if (!vesselVolume) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean getCVValid(final PartialCaseRow row) {
		for (final BuyOption buyOption : row.getBuyOptions()) {
			for (final SellOption sellOption : row.getSellOptions()) {
				final boolean checkVolumeAgainstBuyAndSell = SandboxConstraintUtils.checkCVAgainstBuyAndSell(buyOption, sellOption);
				if (!checkVolumeAgainstBuyAndSell) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean getVesselPortRestrictionsValid(final PartialCaseRow row) {
		for (final BuyOption buyOption : row.getBuyOptions()) {
			for (final SellOption sellOption : row.getSellOptions()) {
				for (final ShippingOption option : row.getShipping()) {
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						final boolean vesselVolume = SandboxConstraintUtils.vesselPortRestrictionsValid(buyOption, sellOption, option);
						if (!vesselVolume) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean getPortRestrictionsValid(final PartialCaseRow row) {
		for (final BuyOption buyOption : row.getBuyOptions()) {
			for (final SellOption sellOption : row.getSellOptions()) {
				final boolean valid = SandboxConstraintUtils.portRestrictionsValid(buyOption, sellOption);
				if (!valid) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean getVesselRestrictionsValid(final PartialCaseRow row) {
		for (final BuyOption buyOption : row.getBuyOptions()) {
			for (final SellOption sellOption : row.getSellOptions()) {
				for (final ShippingOption option : row.getShipping()) {
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						final boolean vesselVolume = SandboxConstraintUtils.vesselRestrictionsValid(buyOption, sellOption, option);
						if (!vesselVolume) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

}

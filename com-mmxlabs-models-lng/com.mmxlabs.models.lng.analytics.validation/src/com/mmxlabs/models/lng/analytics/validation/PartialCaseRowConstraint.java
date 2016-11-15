package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

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

			if (partialCaseRow.getBuyOptions().stream().filter(AnalyticsBuilder.isDESPurchase()).count() > 0
					&& partialCaseRow.getSellOptions().stream().filter(AnalyticsBuilder.isFOBSale()).count() > 0) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row with a DES purchase and a FOB Sale", viewName)));
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);

			}
			if (partialCaseRow.getBuyOptions().stream().filter(AnalyticsBuilder.isFOBPurchase()).count() > 0
					&& partialCaseRow.getSellOptions().stream().filter(AnalyticsBuilder.isDESSale()).count() > 0
					&& partialCaseRow.getShipping().stream().filter(AnalyticsBuilder.isNominated()).count() > 0) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row with a FOB purchase and a DES Sale and a nominated vessel", viewName)));
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			if (partialCaseRow.getBuyOptions().stream().filter(AnalyticsBuilder.isFOBPurchase()).count() > 0
					&& partialCaseRow.getSellOptions().stream().filter(AnalyticsBuilder.isDESSale()).count() > 0
					&& partialCaseRow.getShipping().isEmpty()) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row with a FOB purchase and a DES Sale and no shipping option", viewName)));
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			int lateness = getLateness(portModel, partialCaseRow);
			if (lateness < 0) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row will create a late cargo", viewName)), IConstraintStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			boolean volume = getVolumeValid(partialCaseRow);
			if (!volume) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row has mismatching volume limits", viewName)), IConstraintStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			boolean ports = getPortRestrictionsValid(partialCaseRow);
			if (!ports) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row uses a vessel that cannot visit the specified ports", viewName)), IConstraintStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			boolean vessels = getVesselRestrictionsValid(partialCaseRow);
			if (!vessels) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - a combination in the row uses a vessel that is restricted by the load slot", viewName)), IConstraintStatus.WARNING);
				deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
				statuses.add(deco);
			}
			for (ShippingOption opt : partialCaseRow.getShipping()) {
				if (opt.eContainer() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Partial case - uncontained shipping"));
					deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private int getLateness(PortModel portModel, PartialCaseRow row) {
		for (BuyOption buyOption : row.getBuyOptions()) {
			for (SellOption sellOption : row.getSellOptions()) {
				for (ShippingOption option : row.getShipping()) {
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						int lateness = AnalyticsBuilder.calculateLateness(buyOption, sellOption, portModel, AnalyticsBuilder.getVesselClass(option));
						if (lateness < 0) {
							return lateness;
						}
					}
				}
			}
		}
		return 0;
	}
	
	private boolean getVolumeValid(PartialCaseRow row) {
		for (BuyOption buyOption : row.getBuyOptions()) {
			for (SellOption sellOption : row.getSellOptions()) {
				for (ShippingOption option : row.getShipping()) {
					// test slot volume
					boolean checkVolumeAgainstBuyAndSell = SandboxConstraintUtils.checkVolumeAgainstBuyAndSell(buyOption, sellOption);
					if (!checkVolumeAgainstBuyAndSell) {
						return false;
					}
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						boolean vesselVolume = SandboxConstraintUtils.checkVolumeAgainstVessel(buyOption, sellOption, option);
						if (!vesselVolume) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean getPortRestrictionsValid(PartialCaseRow row) {
		for (BuyOption buyOption : row.getBuyOptions()) {
			for (SellOption sellOption : row.getSellOptions()) {
				for (ShippingOption option : row.getShipping()) {
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						boolean vesselVolume = SandboxConstraintUtils.portRestrictionsValid(buyOption, sellOption, option);
						if (!vesselVolume) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean getVesselRestrictionsValid(PartialCaseRow row) {
		for (BuyOption buyOption : row.getBuyOptions()) {
			for (SellOption sellOption : row.getSellOptions()) {
				for (ShippingOption option : row.getShipping()) {
					// test shipping only
					if (AnalyticsBuilder.isFOBPurchase().test(buyOption) && AnalyticsBuilder.isDESSale().test(sellOption) && AnalyticsBuilder.isShipped(option)) {
						boolean vesselVolume = SandboxConstraintUtils.vesselRestrictionsValid(buyOption, sellOption, option);
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

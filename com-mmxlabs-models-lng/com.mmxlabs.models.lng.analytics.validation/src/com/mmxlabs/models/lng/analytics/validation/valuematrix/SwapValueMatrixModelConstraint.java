/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation.valuematrix;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SwapValueMatrixModelConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();
		if (target instanceof @NonNull final SwapValueMatrixModel valueMatrixModel) {
			if (valueMatrixModel.getBaseDischargeStepSize() <= 0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Step size should be greater than zero"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeStepSize());
				failures.add(dsd);
			}

			if (valueMatrixModel.getMarketStepSize() <= 0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Step size should be greater than zero"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketStepSize());
				failures.add(dsd);
			}

			if (valueMatrixModel.getBaseDischargeMinPrice() > valueMatrixModel.getBaseDischargeMaxPrice()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Min price must not be greater than max price"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMinPrice());
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMaxPrice());
				failures.add(dsd);
			}

			if (valueMatrixModel.getMarketMinPrice() > valueMatrixModel.getMarketMaxPrice()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Min price must not be greater than max price"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMinPrice());
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMaxPrice());
				failures.add(dsd);
			}

			if (valueMatrixModel.getMarketMinPrice() < valueMatrixModel.getSwapFee()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap fee must not be greater than market min price"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket());
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMinPrice());
				failures.add(dsd);
			}

			if (valueMatrixModel.getBaseDischargeMaxPrice() > 90) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Max price must not be greater than 90"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMaxPrice());
				failures.add(dsd);
			}

			if (valueMatrixModel.getMarketMaxPrice() > 90) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Max price must not be greater than 90"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMaxPrice());
				failures.add(dsd);
			}

			final LoadSlot loadSlot;
			final BuyReference buyReference = valueMatrixModel.getBaseLoad();
			if (buyReference == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap load not present"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
				failures.add(dsd);
				loadSlot = null;
			} else {
				loadSlot = buyReference.getSlot();
				if (loadSlot == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap load not present"));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
					failures.add(dsd);
				}
			}
			final DischargeSlot dischargeSlot;
			final SellReference sellReference = valueMatrixModel.getBaseDischarge();
			if (sellReference == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap discharge not present"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
				failures.add(dsd);
				dischargeSlot = null;
			} else {
				dischargeSlot = sellReference.getSlot();
				if (dischargeSlot == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap discharge not present"));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
					failures.add(dsd);
				}
			}
			final VesselCharter vesselCharter;
			final ExistingVesselCharterOption vesselCharterOption = valueMatrixModel.getBaseVesselCharter();
			if (vesselCharterOption == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Vessel charter not present"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseVesselCharter());
				failures.add(dsd);
				vesselCharter = null;
			} else {
				vesselCharter = vesselCharterOption.getVesselCharter();
				if (vesselCharter == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Vessel charter not present"));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseVesselCharter());
					failures.add(dsd);
				}
			}
			final DESPurchaseMarket desPurchaseMarket;
			final BuyMarket buyMarket = valueMatrixModel.getSwapLoadMarket();
			if (buyMarket == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap buy market not present"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket());
				failures.add(dsd);
				desPurchaseMarket = null;
			} else {
				final SpotMarket market = buyMarket.getMarket();
				if (market == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap buy market not present"));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket());
					failures.add(dsd);
					desPurchaseMarket = null;
				} else if (market instanceof DESPurchaseMarket dpm) {
					desPurchaseMarket = dpm;
				} else {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap buy market must be DES purchase market"));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket());
					failures.add(dsd);
					desPurchaseMarket = null;
				}
			}

			final DESSalesMarket desSalesMarket;
			final SellMarket sellMarket = valueMatrixModel.getSwapDischargeMarket();
			if (sellMarket == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap sell market not present"));
				dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket());
				failures.add(dsd);
				desSalesMarket = null;
			} else {
				final SpotMarket market = sellMarket.getMarket();
				if (market == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap sell market not present"));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket());
					failures.add(dsd);
					desSalesMarket = null;
				} else if (market instanceof DESSalesMarket dsm) {
					desSalesMarket = dsm;
				} else {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Swap sell market must be DES sales market"));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket());
					failures.add(dsd);
					desSalesMarket = null;
				}
			}

			if (loadSlot != null && dischargeSlot != null) {
				final Cargo loadCargo = loadSlot.getCargo();
				if (loadCargo == null) {
					final String message = String.format("%s is not paired to %s", ScenarioElementNameHelper.getName(loadSlot), ScenarioElementNameHelper.getName(dischargeSlot));
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
					dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
					failures.add(dsd);
				} else {
					final Cargo dischargeCargo = dischargeSlot.getCargo();
					if (dischargeCargo == null) {
						final String message = String.format("%s is not paired to %s", ScenarioElementNameHelper.getName(loadSlot), ScenarioElementNameHelper.getName(dischargeSlot));
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
						dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
						failures.add(dsd);
					} else {
						if (loadCargo != dischargeCargo) {
							final String message = String.format("%s is not paired to %s", ScenarioElementNameHelper.getName(loadSlot), ScenarioElementNameHelper.getName(dischargeSlot));
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
							dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
							failures.add(dsd);
						} else if (vesselCharter != null && loadCargo.getVesselAssignmentType() != vesselCharter) {
							final String message = String.format("Charter %s does not lift cargo in base case", ScenarioElementNameHelper.getName(vesselCharter.getVessel()));
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
							dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
							dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseVesselCharter());
							failures.add(dsd);
						}
					}
				}
			}
			if (dischargeSlot != null && desPurchaseMarket != null) {
				final Port dischargePort = dischargeSlot.getPort();
				if (dischargePort != null) {
					final Set<Port> dpmPorts = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
					if (!dpmPorts.contains(dischargePort)) {
						final String message = String.format("%s cannot be backfilled from %s", ScenarioElementNameHelper.getName(dischargeSlot), ScenarioElementNameHelper.getName(desPurchaseMarket, "(unknown)"));
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
						dsd.addEObjectAndFeature(valueMatrixModel, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket());
						failures.add(dsd);
					}
				}
			}
		}
	}
}

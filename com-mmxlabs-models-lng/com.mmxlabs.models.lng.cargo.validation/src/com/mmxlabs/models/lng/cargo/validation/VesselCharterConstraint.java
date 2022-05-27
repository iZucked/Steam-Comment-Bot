/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.commercial.validation.CharterContractValidationUtils;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VesselCharterConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof VesselCharter vesselCharter) {
			final Vessel vessel = vesselCharter.getVessel();
			final String vesselName = ScenarioElementNameHelper.getName(vessel, "<Unknown>");

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(vesselCharter), vesselName);

			if (vessel == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__VESSEL) //
						.withMessage("Vessel must be specified.") //
						.make(ctx));
			}
			if (vesselCharter.getCharterOrDelegateEntity() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__ENTITY) //
						.withMessage("Entity must be specified.") //
						.make(ctx));
			}

			if (vesselCharter.isSetStartAfter() && vesselCharter.isSetStartBy()) {
				if (vesselCharter.getStartAfter().isAfter(vesselCharter.getStartBy())) {

					statuses.add(baseFactory.copyName() //
							.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_BY) //
							.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER) //
							.withMessage("Bad start window (window start date is after end date)") //
							.make(ctx));

				}
			}

			if (vesselCharter.isSetEndAfter() && vesselCharter.isSetEndBy()) {
				if (vesselCharter.getEndAfter().isAfter(vesselCharter.getEndBy())) {
					statuses.add(baseFactory.copyName() //
							.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__END_BY) //
							.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__END_AFTER) //
							.withMessage("Bad end window (window start date is after end date)") //
							.make(ctx));
				}
			}

			{
				final LocalDateTime startStart = vesselCharter.getStartAfter();
				final LocalDateTime startEnd = vesselCharter.getStartBy();

				final LocalDateTime endStart = vesselCharter.getEndAfter();
				final LocalDateTime endEnd = vesselCharter.getEndBy();

				final LocalDateTime s = startStart == null ? startEnd : startStart;
				final LocalDateTime e = endEnd == null ? endStart : endEnd;

				if ((s != null) && (e != null)) {
					if (e.isBefore(s)) {
						String msg = "Invalid start and end dates (start window is after end window).";
						DetailConstraintStatusFactory baseCopy = baseFactory.copyName().withMessage(msg);
						if (startStart != null) {
							baseCopy.withObjectAndFeature(vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_StartAfter());
						}
						if (startEnd != null) {
							baseCopy.withObjectAndFeature(vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_StartBy());
						}
						if (endStart != null) {
							baseCopy.withObjectAndFeature(vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_EndAfter());
						}
						if (endEnd != null) {
							baseCopy.withObjectAndFeature(vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_EndBy());
						}
						baseCopy.make(ctx, statuses);
					}
				}
			}

			YearMonth earliestDate = null;
			if (vesselCharter.isSetStartAfter()) {
				earliestDate = YearMonth.from(vesselCharter.getStartAfter());
			} else if (vesselCharter.isSetStartBy()) {
				earliestDate = YearMonth.from(vesselCharter.getStartBy());
			} else if (vesselCharter.isSetEndAfter()) {
				earliestDate = YearMonth.from(vesselCharter.getEndAfter());
			} else if (vesselCharter.isSetEndBy()) {
				earliestDate = YearMonth.from(vesselCharter.getEndBy());
			}
			if (earliestDate != null) {
				final String timeCharterRate = vesselCharter.getTimeCharterRate();
				if (timeCharterRate != null && !timeCharterRate.trim().isEmpty()) {
					for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(timeCharterRate)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(earliestDate)) {

							statuses.add(baseFactory.copyName() //
									.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__TIME_CHARTER_RATE) //
									.withMessage(String.format("There is no charter cost pricing data before %s %04d for curve %s", date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()),
											date.getYear(), index.getName())) //
									.make(ctx));
						}
					}
				}
			}
			if (earliestDate != null && vessel != null) {

				final Set<BaseFuel> fuels = new LinkedHashSet<>();

				fuels.add(vessel.getVesselOrDelegateBaseFuel());
				fuels.add(vessel.getVesselOrDelegateIdleBaseFuel());
				fuels.add(vessel.getVesselOrDelegateInPortBaseFuel());
				fuels.add(vessel.getVesselOrDelegatePilotLightBaseFuel());
				fuels.remove(null);

				final MMXRootObject rootObject = extraContext.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

					final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);

					for (final BaseFuel baseFuel : fuels) {
						for (final BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
							if (baseFuelCost.getFuel() == baseFuel) {

								final YearMonth key = earliestDate;
								final String priceExpression = baseFuelCost.getExpression();

								if (priceExpression != null && !priceExpression.trim().isEmpty()) {
									for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(priceExpression)) {
										final @Nullable YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
										if (date == null) {
											statuses.add(baseFactory.copyName() //
													.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__VESSEL) //
													.withMessage(String.format("There is no base fuel cost pricing data for curve %s", index.getName())) //
													.make(ctx));
										} else if (date.isAfter(key)) {
											statuses.add(baseFactory.copyName() //
													.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__VESSEL) //
													.withMessage(String.format("There is no base fuel cost pricing data before %s %04d for curve %s",
															date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())) //
													.make(ctx));
										}
									}
								}

							}
						}
					}
				}
			}
			charterContractValidation(ctx, extraContext, baseFactory, statuses, earliestDate, vesselName);
		}
	}

	private void charterContractValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory, final List<IStatus> failures,
			final YearMonth earliestDate, final String vesselName) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselCharter) {
			final VesselCharter va = (VesselCharter) target;

			final GenericCharterContract contract = va.getCharterOrDelegateCharterContract();
			if (contract != null) {
				final IBallastBonus ballastBonusContract = contract.getBallastBonusTerms();
				EStructuralFeature feature = CommercialPackage.Literals.COMMERCIAL_MODEL__CHARTER_CONTRACTS;
				String message = String.format("Charter %s end terms: ", vesselName);
				if (ballastBonusContract instanceof SimpleBallastBonusContainer) {
					CharterContractValidationUtils.simpleBallastBonusValidation(ctx, extraContext, failures, (SimpleBallastBonusContainer) ballastBonusContract, contract, feature, message);
					CharterContractValidationUtils.ballastBonusCheckPortGroups(ctx, extraContext, baseFactory, failures, va, (SimpleBallastBonusContainer) ballastBonusContract);
				} else if (ballastBonusContract instanceof MonthlyBallastBonusContainer) {
					CharterContractValidationUtils.monthlyBallastBonusValidation(ctx, extraContext, failures, contract, ballastBonusContract, message);
				}
				final IRepositioningFee repositioningFee = contract.getRepositioningFeeTerms();
				if (repositioningFee instanceof SimpleRepositioningFeeContainer) {
					CharterContractValidationUtils.simpleRepositioningFeeValidation(ctx, extraContext, failures, (SimpleRepositioningFeeContainer) repositioningFee, contract, feature, message,
							earliestDate);
				}

				if (va.getEndAt().isEmpty()) {
					final DetailConstraintStatusFactory f = baseFactory.copyName();
					f.withMessage(String.format("Redelivery ports should be specified when there is a ballast bonus."));
					f.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_CHARTER__END_AT);
					f.withSeverity(IStatus.WARNING);
					failures.add(f.make(ctx));
				}
			}
		}
	}
}

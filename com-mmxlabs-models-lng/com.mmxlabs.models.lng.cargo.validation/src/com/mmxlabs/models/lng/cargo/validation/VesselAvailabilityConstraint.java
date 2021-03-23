/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LumpSumTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.commercial.validation.CharterContractValidationUtils;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			final VesselAvailability availability = (VesselAvailability) target;
			final Vessel vessel = availability.getVessel();
			final String vesselName = ScenarioElementNameHelper.getName(vessel, "<Unknown>");

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(availability), vesselName);

			if (vessel == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
						.withMessage("Vessel must be specified.") //
						.make(ctx));
			}
			if (availability.getCharterOrDelegateEntity() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__ENTITY) //
						.withMessage("Entity must be specified.") //
						.make(ctx));
			}

			if (availability.isSetStartAfter() && availability.isSetStartBy()) {
				if (availability.getStartAfter().isAfter(availability.getStartBy())) {

					statuses.add(baseFactory.copyName() //
							.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY) //
							.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER) //
							.withMessage("Bad start window (window start date is after end date)") //
							.make(ctx));

				}
			}

			if (availability.isSetEndAfter() && availability.isSetEndBy()) {
				if (availability.getEndAfter().isAfter(availability.getEndBy())) {
					statuses.add(baseFactory.copyName() //
							.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY) //
							.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER) //
							.withMessage("Bad end window (window start date is after end date)") //
							.make(ctx));
				}
			}

			{
				final LocalDateTime startStart = availability.getStartAfter();
				final LocalDateTime startEnd = availability.getStartBy();

				final LocalDateTime endStart = availability.getEndAfter();
				final LocalDateTime endEnd = availability.getEndBy();

				final LocalDateTime s = startStart == null ? startEnd : startStart;
				final LocalDateTime e = endEnd == null ? endStart : endEnd;

				if ((s != null) && (e != null)) {
					if (e.isBefore(s)) {
						String msg = "Invalid start and end dates (start window is after end window).";
						DetailConstraintStatusFactory baseCopy = baseFactory.copyName().withMessage(msg);
						if (startStart != null) {
							baseCopy.withObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_StartAfter());
						}
						if (startEnd != null) {
							baseCopy.withObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_StartBy());
						}
						if (endStart != null) {
							baseCopy.withObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_EndAfter());
						}
						if (endEnd != null) {
							baseCopy.withObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
						}
						baseCopy.make(ctx, statuses);
					}
				}
			}

			YearMonth earliestDate = null;
			if (availability.isSetStartAfter()) {
				earliestDate = YearMonth.from(availability.getStartAfter());
			} else if (availability.isSetStartBy()) {
				earliestDate = YearMonth.from(availability.getStartBy());
			} else if (availability.isSetEndAfter()) {
				earliestDate = YearMonth.from(availability.getEndAfter());
			} else if (availability.isSetEndBy()) {
				earliestDate = YearMonth.from(availability.getEndBy());
			}
			if (earliestDate != null) {
				final String timeCharterRate = availability.getTimeCharterRate();
				if (timeCharterRate != null && !timeCharterRate.trim().isEmpty()) {
					for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(timeCharterRate)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(earliestDate)) {

							statuses.add(baseFactory.copyName() //
									.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__TIME_CHARTER_RATE) //
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
													.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
													.withMessage(String.format("There is no base fuel cost pricing data for curve %s", index.getName())) //
													.make(ctx));
										} else if (date.isAfter(key)) {
											statuses.add(baseFactory.copyName() //
													.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
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
			charterContractValidation(ctx, extraContext, baseFactory, statuses, earliestDate);
		}
		return Activator.PLUGIN_ID;
	}

	private void charterContractValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory, 
			final List<IStatus> failures, final YearMonth earliestDate) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			final VesselAvailability va = (VesselAvailability) target;

			final GenericCharterContract contract = va.getCharterOrDelegateCharterContract();
			if (contract != null) {
				final IBallastBonus ballastBonusContract = contract.getBallastBonusTerms();
				EStructuralFeature feature = CommercialPackage.Literals.COMMERCIAL_MODEL__CHARTER_CONTRACTS;
				
				if (ballastBonusContract instanceof SimpleBallastBonusContainer) {
					String message = String.format("Charter %s end terms: ", va.getVessel().getName());
					CharterContractValidationUtils.simpleBallastBonusValidation(ctx, extraContext, failures, (SimpleBallastBonusContainer) ballastBonusContract, contract, feature, message);
					CharterContractValidationUtils.ballastBonusCheckPortGroups(ctx, extraContext, baseFactory, failures, va, (SimpleBallastBonusContainer) ballastBonusContract);
				} else if (ballastBonusContract instanceof MonthlyBallastBonusContainer) {
					CharterContractValidationUtils.monthlyBallastBonusValidation(ctx, extraContext, failures, contract, ballastBonusContract);
				}
				final IRepositioningFee repositioningFee = contract.getRepositioningFeeTerms();
				if (repositioningFee instanceof SimpleRepositioningFeeContainer) {
					String message = String.format("Charter %s start terms: ", va.getVessel().getName());
					CharterContractValidationUtils.simpleRepositioningFeeValidation(ctx, extraContext, failures, (SimpleRepositioningFeeContainer) repositioningFee, contract, feature, message, earliestDate);
				}
				
				if (va.getEndAt().isEmpty()) {
					final DetailConstraintStatusFactory f = baseFactory.copyName();
					f.withMessage(String.format("Redelivery ports should be specified when there is a ballast bonus."));
					f.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__END_AT);
					f.withSeverity(IStatus.WARNING);
					failures.add(f.make(ctx));
				}
			}
		}
	}
}

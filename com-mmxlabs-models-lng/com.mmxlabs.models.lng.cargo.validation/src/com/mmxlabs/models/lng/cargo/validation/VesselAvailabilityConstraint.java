/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

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
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LumpSumTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
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
					simpleBallastBonusValidation(ctx, extraContext, failures, (SimpleBallastBonusContainer) ballastBonusContract, contract, feature, message);
					ballastBonusCheckPortGroups(ctx, extraContext, baseFactory, failures, va, (SimpleBallastBonusContainer) ballastBonusContract);
				}
				final IRepositioningFee repositioningFee = contract.getRepositioningFeeTerms();
				if (repositioningFee instanceof SimpleRepositioningFeeContainer) {
					String message = String.format("Charter %s start terms: ", va.getVessel().getName());
					simpleRepositioningFeeValidation(ctx, extraContext, failures, (SimpleRepositioningFeeContainer) repositioningFee, contract, feature, message, earliestDate);
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

	private void ballastBonusCheckPortGroups(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory,
			final List<IStatus> failures, final VesselAvailability va, final SimpleBallastBonusContainer ballastBonusContainer) {
		final Set<APortSet<Port>> coveredPorts = new HashSet<APortSet<Port>>();
		final List<APortSet<Port>> endAtPorts = new LinkedList<>();
		boolean anywhere = false;
		if (va.getEndAt().isEmpty()) {
			// could end anywhere - add all ports
			anywhere = true;
			endAtPorts.addAll(((LNGScenarioModel) extraContext.getRootObject()).getReferenceModel().getPortModel().getPorts());
		} else {
			endAtPorts.addAll(SetUtils.getObjects(va.getEndAt()));
		}
		if (!ballastBonusContainer.getTerms().isEmpty()) {
			for (final BallastBonusTerm ballastBonusContractLine : ballastBonusContainer.getTerms()) {
				final EList<APortSet<Port>> redeliveryPorts = ballastBonusContractLine.getRedeliveryPorts();
				if (redeliveryPorts.isEmpty()) {
					return;
				} else {
					coveredPorts.addAll(SetUtils.getObjects(redeliveryPorts));
				}
			}
			for (final APortSet<Port> endAtPort : endAtPorts) {
				if (!coveredPorts.contains(endAtPort)) {
					final DetailConstraintStatusFactory f = baseFactory.copyName();
					if (anywhere) {
						f.withMessage(String.format("%s is not covered by the ballast bonus rules (note the vessel can end anywhere)", ScenarioElementNameHelper.getName(endAtPort)));
					} else {
						f.withMessage(String.format("%s is not covered by the ballast bonus rules", ScenarioElementNameHelper.getName(endAtPort)));
					}
					f.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
					failures.add(f.make(ctx));
					return;
				}
			}
		}
	}
	
	private void simpleBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final SimpleBallastBonusContainer ballastBonusContainer, final EObject topLevelEObject, final EStructuralFeature topFeature, String topFeatureMessage) {
		boolean atLeastOneProblem = false;
		for (final BallastBonusTerm ballastBonusTerm : ballastBonusContainer.getTerms()) {
			if (ballastBonusTerm instanceof LumpSumTerm) {
				if (!lumpSumValidation(ctx, extraContext, failures, topFeatureMessage, (LumpSumTerm) ballastBonusTerm)) {
					atLeastOneProblem = true;
				}
			} else if (ballastBonusTerm instanceof NotionalJourneyBallastBonusTerm) {
				if (!notionalJourneyTermsValidation(ctx, extraContext, failures, topFeatureMessage, (NotionalJourneyTerm) ballastBonusTerm)) {
					atLeastOneProblem = true;
				}
			}
		}
		
		if (atLeastOneProblem) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] ballast bonus containter with at least one error", topFeatureMessage)));
			dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
			failures.add(dcsd);
		}
	}

	private boolean lumpSumValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, String topFeatureMessage,
			final LumpSumTerm term) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, term, CommercialPackage.Literals.LUMP_SUM_TERM__PRICE_EXPRESSION,
				term.getPriceExpression(), PriceExpressionUtils.getPriceIndexType(CommercialPackage.Literals.LUMP_SUM_TERM__PRICE_EXPRESSION));
		if (!result.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[%s]: Lump sum is invalid", topFeatureMessage)));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.LUMP_SUM_TERM__PRICE_EXPRESSION);
			failures.add(dcsd);
			return false;
		}
		return true;
	}
	
	private boolean notionalJourneyTermsValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, String topFeatureMessage,
			final NotionalJourneyTerm term) {
		boolean valid = true;
		final ValidationResult fuelResult = PriceExpressionUtils.validatePriceExpression(ctx, term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION,
				term.getFuelPriceExpression(), PriceIndexType.BUNKERS);
		if (!fuelResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[%s]: fuel price is invalid", topFeatureMessage)));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		final ValidationResult hireResult = PriceExpressionUtils.validatePriceExpression(ctx, term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION,
				term.getHirePriceExpression(), PriceIndexType.CHARTER);
		if (!hireResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[%s]: hire price is invalid", topFeatureMessage)));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		if (term instanceof NotionalJourneyBallastBonusTerm) {
			if (((NotionalJourneyBallastBonusTerm) term).getReturnPorts().isEmpty() && !(term instanceof MonthlyBallastBonusTerm)) {
				// need ports
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("[%s]: return ports are needed on a notional journey", topFeatureMessage)));
				dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS);
				failures.add(dcsd);
				valid = false;
			}
		}
		// Determine real vessel ballast speed range. Note as notional, we are not limited to the min/max speed defined on the class.

		// Default range if no data exists.
		double minSpeed = 5.0;
		double maxSpeed = 25.0;

		if (term.getSpeed() < minSpeed || term.getSpeed() > maxSpeed) {
			// need valid speed
			final EReference ref;
			if (term instanceof OriginPortRepositioningFeeTerm) {
				ref = CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS;
			} else {
				ref = CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS;
			}
			
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
					String.format("[%s]: speed must be between %.01f and %.01f knots on a notional journey", topFeatureMessage, minSpeed, maxSpeed)));
			dcsd.addEObjectAndFeature(term, ref);
			failures.add(dcsd);
			valid = false;
		}

		return valid;
	}
	
	private void simpleRepositioningFeeValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final SimpleRepositioningFeeContainer repositioningFeeContainer, final EObject topLevelEObject, final EStructuralFeature topFeature, String topFeatureMessage,
			final YearMonth earliestDate) {
		boolean atLeastOneProblem = false;
		for (final RepositioningFeeTerm repositioningFeeTerm : repositioningFeeContainer.getTerms()) {
			if (repositioningFeeTerm instanceof LumpSumTerm) {
				
				final String repositioningFee = ((LumpSumTerm) repositioningFeeTerm).getPriceExpression();
				if (repositioningFee != null && !repositioningFee.trim().isEmpty()) {
					for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(repositioningFee)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(earliestDate)) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] : There is no charter cost pricing data before %s %04d for curve %s",
											topFeatureMessage, date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()),
											date.getYear(), index.getName())));
								dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
							failures.add(dcsd);
						}
					}
				}
				if (!lumpSumValidation(ctx, extraContext, failures, topFeatureMessage, (LumpSumTerm) repositioningFeeTerm)) {
					atLeastOneProblem = true;
				}
			} else if (repositioningFeeTerm instanceof OriginPortRepositioningFeeTerm) {
				if (!notionalJourneyTermsValidation(ctx, extraContext, failures, topFeatureMessage, (NotionalJourneyTerm) repositioningFeeTerm)) {
					atLeastOneProblem = true;
				}
			}
		}
		
		if (atLeastOneProblem) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] repositioning fee containter with at least one error", topFeatureMessage)));
			dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
			failures.add(dcsd);
		}
	}
}

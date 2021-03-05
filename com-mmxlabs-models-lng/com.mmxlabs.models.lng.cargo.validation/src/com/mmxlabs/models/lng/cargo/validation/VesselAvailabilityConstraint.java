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
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
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

				final String repositioningFee = availability.getCharterOrDelegateRepositioningFee();
				if (repositioningFee != null && !repositioningFee.trim().isEmpty()) {
					for (final AbstractYearMonthCurve index : PriceExpressionUtils.getLinkedCurves(repositioningFee)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(earliestDate)) {

							statuses.add(baseFactory.copyName() //
									.withObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__REPOSITIONING_FEE) //
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
			ballastBonusValidation(ctx, extraContext, baseFactory, statuses);
		}
		return Activator.PLUGIN_ID;
	}

	private void ballastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			final VesselAvailability va = (VesselAvailability) target;

			final BallastBonusContract vaBallastBonusContract = va.getCharterContract();
			final CharterContract charterContract = va.getCharterContract();
			BallastBonusContract charterBallastBonusContract = null;
			if (charterContract != null && charterContract instanceof BallastBonusCharterContract) {
				charterBallastBonusContract = ((BallastBonusCharterContract) charterContract).getCharterContract();
			}
			final BallastBonusContract ballastBonusContract = vaBallastBonusContract != null ? vaBallastBonusContract : charterBallastBonusContract;
			if (ballastBonusContract != null) {
				if (ballastBonusContract instanceof RuleBasedBallastBonusContract) {
					ruleBasedballastBonusValidation(ctx, extraContext, baseFactory, failures, va, (RuleBasedBallastBonusContract) ballastBonusContract);
					ruleBasedballastBonusCheckPortGroups(ctx, extraContext, baseFactory, failures, va, (RuleBasedBallastBonusContract) ballastBonusContract);
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

	private void ruleBasedballastBonusCheckPortGroups(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory,
			final List<IStatus> failures, final VesselAvailability va, final RuleBasedBallastBonusContract ballastBonusContract) {
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
		if (!ballastBonusContract.getRules().isEmpty()) {
			for (final BallastBonusContractLine ballastBonusContractLine : ballastBonusContract.getRules()) {
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

	private void ruleBasedballastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory,
			final List<IStatus> failures, final VesselAvailability va, final RuleBasedBallastBonusContract ballastBonusContract) {
		if (ballastBonusContract.getRules().isEmpty()) {
			// need rules
			failures.add(baseFactory.copyName() //
					.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
					.withObjectAndFeature(ballastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES) //
					.withMessage("A ballast bonus contract requires at least one rule") //
					.make(ctx));
		}
		for (final BallastBonusContractLine ballastBonusContractLine : ballastBonusContract.getRules()) {
			if (ballastBonusContractLine instanceof LumpSumBallastBonusContractLine) {
				lumpSumBallastBonusValidation(ctx, extraContext, baseFactory, failures, va, (LumpSumBallastBonusContractLine) ballastBonusContractLine);
			} else if (ballastBonusContractLine instanceof NotionalJourneyBallastBonusContractLine) {
				notionalJourneyBallastBonusValidation(ctx, extraContext, baseFactory, failures, va, (NotionalJourneyBallastBonusContractLine) ballastBonusContractLine);
			}
		}
	}

	private boolean lumpSumBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory,
			final List<IStatus> failures, final VesselAvailability va, final LumpSumBallastBonusContractLine line) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION,
				line.getPriceExpression(), PriceExpressionUtils.getPriceIndexType(CommercialPackage.Literals.LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION));
		if (!result.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Charter |'%s': Ballast bonus lump sum is invalid", va.getVessel().getName())));
			// dcsd.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION);
			failures.add(dcsd);
			return false;
		}
		return true;
	}

	private boolean notionalJourneyBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory,
			final List<IStatus> failures, final VesselAvailability va, final NotionalJourneyBallastBonusContractLine line) {
		boolean valid = true;
		final ValidationResult fuelResult = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION,
				line.getFuelPriceExpression(), PriceIndexType.BUNKERS);
		if (!fuelResult.isOk()) {
			failures.add(baseFactory.copyName() //
					.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
					.withObjectAndFeature(va.getCharterContract(), CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES) //
					.withObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION) //
					.withMessage(String.format("Ballast bonus: fuel price is invalid - %s", fuelResult.getErrorDetails())) //
					.make(ctx));
			valid = false;
		}
		final ValidationResult hireResult = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION,
				line.getHirePriceExpression(), PriceIndexType.CHARTER);
		if (!hireResult.isOk()) {

			failures.add(baseFactory.copyName() //
					.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
					.withObjectAndFeature(va.getCharterContract(), CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES) //
					.withObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION) //
					.withMessage(String.format("Ballast bonus: hire price is invalid - %s", hireResult.getErrorDetails())) //
					.make(ctx));
			valid = false;
		}
		if (line.getReturnPorts().isEmpty() && !(line instanceof MonthlyBallastBonusContractLine)) {
			// need ports
			failures.add(baseFactory.copyName() //
					.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
					.withObjectAndFeature(va.getCharterContract(), CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES) //
					.withObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS) //
					.withMessage(String.format("Ballast bonus: return ports are needed on a notional journey")) //
					.make(ctx));
			valid = false;
		}
		// Determine real vessel ballast speed range. Note as notional, we are not limited to the min/max speed defined on the class.

		// Default range if no data exists.
		double minSpeed = 5.0;
		double maxSpeed = 25.0;
		final Vessel vessel = va.getVessel();
		if (vessel != null) {
			{
				final VesselStateAttributes ballastAttributes = vessel.getBallastAttributes();
				if (ballastAttributes != null) {
					final List<FuelConsumption> fuelConsumptions = ballastAttributes.getVesselOrDelegateFuelConsumption();
					if (fuelConsumptions != null && !fuelConsumptions.isEmpty()) {
						double vminSpeed = Double.MAX_VALUE;
						double vmaxSpeed = Double.MIN_VALUE;
						for (final FuelConsumption fc : fuelConsumptions) {
							if (fc.getSpeed() < vminSpeed) {
								vminSpeed = fc.getSpeed();
							}
							if (fc.getSpeed() > vmaxSpeed) {
								vmaxSpeed = fc.getSpeed();
							}
						}
						// Update records.
						minSpeed = vminSpeed;
						maxSpeed = vmaxSpeed;
					}
				}
			}
		}

		if (line.getSpeed() < minSpeed || line.getSpeed() > maxSpeed) {
			// need valid speed

			failures.add(baseFactory.copyName() //
					.withObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
					.withObjectAndFeature(va.getCharterContract(), CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES) //
					.withObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED) //
					.withMessage(String.format("Ballast bonus: speed must be between %.01f and %.01f knots on a notional journey", minSpeed, maxSpeed)) //
					.make(ctx));
			valid = false;
		}

		return valid;
	}
}

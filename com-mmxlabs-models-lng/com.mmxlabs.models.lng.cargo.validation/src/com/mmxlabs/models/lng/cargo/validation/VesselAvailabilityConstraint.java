/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashSet;
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
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			final VesselAvailability availability = (VesselAvailability) target;

			final Vessel vessel = availability.getVessel();

			final String vesselName = vessel == null ? "<Unknown>" : vessel.getName();

			if (vessel == null) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Vessel must be specified.", vesselName)));
				dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
				statuses.add(dcsd);
			}

			if (availability.isSetStartAfter() && availability.isSetStartBy()) {
				if (availability.getStartAfter().isAfter(availability.getStartBy())) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
							.createFailureStatus(String.format("[Availability|%s] has inconsistent start and end dates set (the start date is after the end date).", vesselName, "start")));
					dcsd.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_StartAfter());
					dcsd.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_StartBy());
					statuses.add(dcsd);
				}
			}

			if (availability.isSetEndAfter() && availability.isSetEndBy()) {
				if (availability.getEndAfter().isAfter(availability.getEndBy())) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
							.createFailureStatus(String.format("[Availability|%s] has inconsistent start and end dates set (the start date is after the end date).", vesselName, "end")));
					dcsd.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_EndAfter());
					dcsd.addEObjectAndFeature(availability, CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
					statuses.add(dcsd);
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
					for (final NamedIndexContainer<?> index : PriceExpressionUtils.getLinkedCurves(timeCharterRate)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(earliestDate)) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] There is no charter cost pricing data before %s %04d for curve %s", vesselName,
											date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())));
							dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__TIME_CHARTER_RATE);
							statuses.add(dcsd);
						}
					}
				}

				final String repositioningFee = availability.getRepositioningFee();
				if (repositioningFee != null && !repositioningFee.trim().isEmpty()) {
					for (final NamedIndexContainer<?> index : PriceExpressionUtils.getLinkedCurves(repositioningFee)) {
						@Nullable
						final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
						if (date == null || date.isAfter(earliestDate)) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] There is no charter cost pricing data before %s %04d for curve %s", vesselName,
											date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())));
							dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__REPOSITIONING_FEE);
							statuses.add(dcsd);
						}
					}
				}
			}
			if (earliestDate != null && vessel != null) {
				final VesselClass vesselClass = vessel.getVesselClass();
				if (vesselClass != null) {
					final BaseFuel baseFuel = vesselClass.getBaseFuel();
					if (baseFuel != null) {
						final MMXRootObject rootObject = extraContext.getRootObject();
						if (rootObject instanceof LNGScenarioModel) {
							final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

							final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);
							for (final BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
								if (baseFuelCost.getFuel() == baseFuel) {
									final BaseFuelIndex index = baseFuelCost.getIndex();
									@Nullable
									final YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
									if (date == null || date.isAfter(earliestDate)) {
										final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
												(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] There is no base fuel pricing data before %s %04d for curve %s",
														vesselName, date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), date.getYear(), index.getName())));
										dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
										statuses.add(dcsd);
									}
								}
							}
						}
					}
				}
			}
			ballastBonusValidation(ctx, extraContext, statuses);
		}
		return Activator.PLUGIN_ID;
	}

	private void ballastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselAvailability) {
			final VesselAvailability va = (VesselAvailability) target;
			if (va.getBallastBonusContract() != null) {
				if (va.getBallastBonusContract() instanceof RuleBasedBallastBonusContract) {
					ruleBasedballastBonusValidation(ctx, extraContext, failures, va, (RuleBasedBallastBonusContract) va.getBallastBonusContract());
					ruleBasedballastBonusCheckPortGroups(ctx, extraContext, failures, va, (RuleBasedBallastBonusContract) va.getBallastBonusContract());
				}
			}
		}
	}
	
	private void ruleBasedballastBonusCheckPortGroups(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			VesselAvailability va, RuleBasedBallastBonusContract ballastBonusContract) {
		Set<APortSet<Port>> coveredPorts = new HashSet<APortSet<Port>>();
		List<APortSet<Port>> endAtPorts = new LinkedList<>(va.getEndAt());
		boolean anywhere = false;
		if (endAtPorts.isEmpty()) {
			// could end anywhere - add all ports
			anywhere = true;
			endAtPorts.addAll(((LNGScenarioModel) extraContext.getRootObject()).getReferenceModel().getPortModel().getPorts());
		}
		if (!ballastBonusContract.getRules().isEmpty()) {
			for (BallastBonusContractLine ballastBonusContractLine : ballastBonusContract.getRules()) {
					EList<APortSet<Port>> redeliveryPorts = ballastBonusContractLine.getRedeliveryPorts();
					if (redeliveryPorts.isEmpty()) {
						return;
					} else {
						coveredPorts.addAll(SetUtils.getObjects(redeliveryPorts));
					}
			}
			for (APortSet<Port> endAtPort : endAtPorts) {
				if (!coveredPorts.contains(endAtPort)) {
					final DetailConstraintStatusDecorator dcsd;
					if (anywhere) {
						dcsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Port %s is not covered by the ballast bonus rules (note the vessel can end anywhere)",
										va.getVessel().getName(), endAtPort.getName())));
					} else {
						dcsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Port %s is not covered by the ballast bonus rules",
										va.getVessel().getName(), endAtPort.getName())));
					}
					
					dcsd.addEObjectAndFeature(va, CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES);
					failures.add(dcsd);
					return;
				}
			}
		}
	}


	private void ruleBasedballastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, final VesselAvailability va,
			final RuleBasedBallastBonusContract ballastBonusContract) {
		if (ballastBonusContract.getRules().isEmpty()) {
			// need rules
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] A ballast bonus contract requires at least one rule", va.getVessel().getName())));
			dcsd.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
			dcsd.addEObjectAndFeature(ballastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
			failures.add(dcsd);
		}
		for (final BallastBonusContractLine ballastBonusContractLine : ballastBonusContract.getRules()) {
			if (ballastBonusContractLine instanceof LumpSumBallastBonusContractLine) {
				lumpSumBallastBonusValidation(ctx, extraContext, failures, va, (LumpSumBallastBonusContractLine) ballastBonusContractLine);
			} else if (ballastBonusContractLine instanceof NotionalJourneyBallastBonusContractLine) {
				notionalJourneyBallastBonusValidation(ctx, extraContext, failures, va, (NotionalJourneyBallastBonusContractLine) ballastBonusContractLine);
			}
		}
	}

	private boolean lumpSumBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, final VesselAvailability va,
			final LumpSumBallastBonusContractLine line) {
		final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION,
				line.getPriceExpression(), PriceIndexType.COMMODITY);
		if (!result.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Ballast bonus lump sum is invalid", va.getVessel().getName())));
			// dcsd.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION);
			failures.add(dcsd);
			return false;
		}
		return true;
	}

	private boolean notionalJourneyBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, final VesselAvailability va,
			final NotionalJourneyBallastBonusContractLine line) {
		boolean valid = true;
		final ValidationResult fuelResult = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION,
				line.getFuelPriceExpression(), PriceIndexType.BUNKERS);
		if (!fuelResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Ballast bonus: fuel price is invalid", va.getVessel().getName())));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		final ValidationResult hireResult = PriceExpressionUtils.validatePriceExpression(ctx, line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION,
				line.getHirePriceExpression(), PriceIndexType.CHARTER);
		if (!hireResult.isOk()) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Ballast bonus: hire price is invalid", va.getVessel().getName())));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION);
			failures.add(dcsd);
			valid = false;
		}
		if (line.getReturnPorts().isEmpty()) {
			// need ports
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("[Availability|%s] Ballast bonus: return ports are needed on a notional journey", va.getVessel().getName())));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
			failures.add(dcsd);
			valid = false;
		}
		// Determine real vessel ballast speed range. Note as notional, we are not limited to the min/max speed defined on the class.

		// Default range if no data exists.
		double minSpeed = 5.0;
		double maxSpeed = 25.0;
		final Vessel vessel = va.getVessel();
		if (vessel != null) {
			final VesselClass vesselClass = vessel.getVesselClass();
			if (vesselClass != null) {
				final VesselStateAttributes ballastAttributes = vesselClass.getBallastAttributes();
				if (ballastAttributes != null) {
					final List<FuelConsumption> fuelConsumptions = ballastAttributes.getFuelConsumption();
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
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
					String.format("[Availability|%s] Ballast bonus: speed must be between %.01f and %.01f knots on a notional journey", va.getVessel().getName(), minSpeed, maxSpeed)));
			dcsd.addEObjectAndFeature(line, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
			failures.add(dcsd);
			valid = false;
		}

		return valid;
	}

}

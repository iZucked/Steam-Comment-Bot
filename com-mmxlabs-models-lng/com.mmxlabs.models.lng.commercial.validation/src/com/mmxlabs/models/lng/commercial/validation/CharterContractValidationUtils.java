package com.mmxlabs.models.lng.commercial.validation;

import java.time.LocalDate;
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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.LumpSumTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CharterContractValidationUtils {
	public static void validateMonthlyBallastBonus(final IValidationContext ctx, final List<IStatus> statuses, final GenericCharterContract contract, final LocalDate scheduleStart,
			final MonthlyBallastBonusContainer bbContract) {
		LocalDate earliestRule = LocalDate.MAX;
		for (final MonthlyBallastBonusTerm monthlyRule : bbContract.getTerms()) {
			YearMonth ym = monthlyRule.getMonth();
			if (ym != null) {
				LocalDate ymStart = LocalDate.of(ym.getYear(), ym.getMonthValue(), 1);
				if (ymStart.isBefore(earliestRule)) {
					earliestRule = ymStart;
				}
			}
			else {
				addValidationError(ctx, statuses, contract, "Month not set on monthly rule.", CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);			
			}
		}

		if (scheduleStart.isBefore(earliestRule)) {
			String earliestRuleStr = (earliestRule != LocalDate.MAX) ? ", "+earliestRule.toString()+")" : ",]";
			addValidationError(ctx, statuses, contract, "Monthly range does not cover [" + scheduleStart.toString() + earliestRuleStr, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);
		}
	}

	private static void addValidationError(final IValidationContext ctx, final List<IStatus> statuses, final GenericCharterContract contract, final String errorMsg,
			EStructuralFeature... features) {
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
				(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter contract] - monthly ballast bonus term has the following issue: %s", errorMsg)));
		for (EStructuralFeature feature : features) {
			dcsd.addEObjectAndFeature(contract, feature);
		}
		statuses.add(dcsd);
	}

	public static LocalDate getStartOfSchedule(LNGScenarioModel scenario) {
		LocalDate scheduleStartDate = LocalDate.MAX;

		// Check the fleet start dates.
		for (VesselAvailability va : scenario.getCargoModel().getVesselAvailabilities()) {
			if (va.getStartBy() != null && va.getStartBy().toLocalDate().isBefore(scheduleStartDate)) {
				scheduleStartDate = va.getStartBy().toLocalDate();
			}
			if (va.getStartAfter() != null && va.getStartAfter().toLocalDate().isBefore(scheduleStartDate)) {
				scheduleStartDate = va.getStartAfter().toLocalDate();
			}
		}

		// Check for earliest load slot (in case uses spot charter).
		for (Slot<?> slot : scenario.getCargoModel().getLoadSlots()) {
			if (slot.getWindowStart() != null && slot.getWindowStart().isBefore(scheduleStartDate)) {
				scheduleStartDate = slot.getWindowStart();
			}
		}

		// Check for earliest discharge slot (in case uses spot charter).
		for (Slot<?> slot : scenario.getCargoModel().getDischargeSlots()) {
			if (slot.getWindowStart() != null && slot.getWindowStart().isBefore(scheduleStartDate)) {
				scheduleStartDate = slot.getWindowStart();
			}
		}

		return scheduleStartDate;
	}
	
	public static void monthlyBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses, final GenericCharterContract contract,
			final IBallastBonus ballastBonusContract) {
		final EObject rootObject = extraContext.getRootObject();
		final LNGScenarioModel scenario = (LNGScenarioModel) rootObject;
		final LocalDate scheduleStart = getStartOfSchedule(scenario);

		if (scheduleStart != LocalDate.MAX) {
			validateMonthlyBallastBonus(ctx, statuses, contract, scheduleStart, (MonthlyBallastBonusContainer) ballastBonusContract);
		}
	}

	public static void simpleBallastBonusValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
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
				(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] contains a ballast bonus containter with at least one error", topFeatureMessage)));
			dcsd.addEObjectAndFeature(topLevelEObject, topFeature);
			failures.add(dcsd);
		}
	}


	
	public static boolean originPortValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final RepositioningFeeTerm term) {
		if (term.getOriginPort() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Origin port is invalid")));
			dcsd.addEObjectAndFeature(term, CommercialPackage.Literals.REPOSITIONING_FEE_TERM__ORIGIN_PORT);
			failures.add(dcsd);
			return false;
		}
		return true;
	}
	
	public static void simpleRepositioningFeeValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures,
			final SimpleRepositioningFeeContainer repositioningFeeContainer, final EObject topLevelEObject, final EStructuralFeature topFeature, String topFeatureMessage,
			final @Nullable YearMonth earliestDate) {
		boolean atLeastOneProblem = false;
		for (final RepositioningFeeTerm repositioningFeeTerm : repositioningFeeContainer.getTerms()) {
			if (repositioningFeeTerm instanceof LumpSumTerm) {
				if (earliestDate != null) {
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
	}
	
	public static void durationValidation(final IValidationContext ctx, final List<IStatus> statuses, final GenericCharterContract contract, String message) {
		if (contract.isSetMinDuration() && contract.isSetMaxDuration()) {
			if (contract.getMinDuration() > contract.getMaxDuration()) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("[%s] Min duration is greater than max duration", message)));
				dcsd.addEObjectAndFeature(contract, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__MIN_DURATION);
				dcsd.addEObjectAndFeature(contract, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__MAX_DURATION);
				statuses.add(dcsd);
			}
		}
	}
	
	private static boolean lumpSumValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, String topFeatureMessage,
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
	
	private static boolean notionalJourneyTermsValidation(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, String topFeatureMessage,
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
	
	public static void ballastBonusCheckPortGroups(final IValidationContext ctx, final IExtraValidationContext extraContext, final DetailConstraintStatusFactory baseFactory,
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
}

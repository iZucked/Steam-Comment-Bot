/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PeriodDistributionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PeriodDistribution) {
			final PeriodDistribution periodDistribution = (PeriodDistribution) target;

			String name = "<unknown>";
			final ContractProfile<?, ?> profile = getProfile(periodDistribution, extraContext);
			if (profile != null && profile.getContract() != null) {
				final String n = profile.getContract().getName();
				if (n != null && !n.isEmpty()) {
					name = n;
				}
			}		
			
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", name) //
					.withTag(ValidationConstants.TAG_ADP);

			if (periodDistribution.getRange().isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__RANGE) //
						.withMessage("No months specified") //
						.make(ctx, statuses);
			}
			if (periodDistribution.isSetMinCargoes() && periodDistribution.isSetMaxCargoes()) {
				if (periodDistribution.getMinCargoes() > periodDistribution.getMaxCargoes()) {
					factory.copyName() //
							.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__MIN_CARGOES) //
							.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__MAX_CARGOES) //
							.withMessage("Min cargoes is greater than max cargoes") //
							.make(ctx, statuses);
				}
			}
			if (periodDistribution.isSetMinCargoes()) {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(extraContext.getScenarioDataProvider());
				Map<YearMonth, Long> counts = new HashMap<>();
				List<Slot<?>> slots;
				
				if (profile.getContract() instanceof PurchaseContract) {
					slots = cargoModel.getLoadSlots().stream().filter(s -> s.getContract() == profile.getContract()).collect(Collectors.<Slot<?>>toList());
				} else {
					slots = cargoModel.getDischargeSlots().stream().filter(s -> s.getContract() == profile.getContract()).collect(Collectors.<Slot<?>>toList());
				}

				for (Slot<?> slot : slots) {
					LocalDate start = slot.getWindowStart();
					LocalDateTime endTime = start.atStartOfDay().plus(slot.getWindowSize(), slot.getWindowSizeUnits().toTemporalUnit());
					endTime = endTime.plus(slot.getWindowFlex(), slot.getWindowFlexUnits().toTemporalUnit());
					LocalDate end = LocalDate.from(endTime).minusDays(1);
					end = end.isBefore(start) ? start : end;
					
					//We only need to count the month the start date is in, since we disallow slots whose windows "overlap" with constraint ranges.
					YearMonth month = YearMonth.from(start);
					Long count = counts.computeIfAbsent(month, k -> Long.valueOf(0));
					counts.put(month, count+1);

					//Check if completely within the monthly range specified.
					List<YearMonth> outsideOfRange = fullyWithinOrFullyOutOfRange(periodDistribution.getRange(), start, end);

					//Window + flex must fall either completely in constraint's range or completely out, not half in half out..
					if (!outsideOfRange.isEmpty()) {
						factory.copyName() //
						.withObjectAndFeature(slot, CargoPackage.Literals.SLOT__WINDOW_SIZE) //Makes it go to Trades tab. Not helpful if in ADP tab.
						.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__RANGE)
						.withFormattedMessage("Slot %s is constrained by an ADP monthly min max constraint, but it's time window includes %s (outside of the constraint's range).", slot.getName(), outsideOfRange) //
						.make(ctx, statuses);
					}
				}
	
				long sum = 0;
				for (final YearMonth ym : periodDistribution.getRange()) {
					sum += counts.getOrDefault(ym, 0L);
				}
				if (periodDistribution.getMinCargoes() > sum) {
					factory.copyName() //
							.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__MIN_CARGOES) //
							.withFormattedMessage("Min cargoes is %d but only %d cargoes in the period", periodDistribution.getMinCargoes(), sum) //
							.make(ctx, statuses);
				}
			}
		}
	}
	
	/**
	 * Check that the full time window from start to end has corresponding months fully within the range or is completely outside of the range specified.
	 * @param range
	 * @param start
	 * @param end
	 * @return a list of the year months which are not present in range, if the entire range of start to end is not outside of range.
	 */
	private List<YearMonth> fullyWithinOrFullyOutOfRange(EList<YearMonth> range, LocalDate start, LocalDate end) {
		boolean inRange = false;
		boolean outRange = false;
		List<YearMonth> outsideRange = new LinkedList<>();
		for (LocalDate date = start; !date.isAfter(end); date = date.plusMonths(1)) {
			YearMonth month = YearMonth.from(date);
			if (range.contains(month)) {
				inRange = true;
			}
			else {
				outRange = true;
				outsideRange.add(month);
			}
		}
		//If partially within range, but partially out, return list of months outside of range for this slot.
		if (inRange && outRange) { 
			return outsideRange;
		}
		else {
			return Collections.emptyList();
		}
	}

	private @Nullable ContractProfile<?, ?> getProfile(final PeriodDistribution periodDistribution, @NonNull final IExtraValidationContext extraContext) {

		EObject container = extraContext.getContainer(periodDistribution);
		while (container != null) {
			if (container instanceof ContractProfile<?, ?>) {
				return (ContractProfile<?, ?>) container;
			}
			container = extraContext.getContainer(container);

		}
		return null;
	}
}

package com.mmxlabs.models.lng.cargo.ui.commands;

import java.time.LocalDate;
import java.time.YearMonth;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.pricing.InstrumentPeriod;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.types.PricingPeriod;

public class PaperDealUpdatingCommandUtil {
	
	private static final CargoPackage cp = CargoPackage.eINSTANCE;
	private static final PricingPackage pp = PricingPackage.eINSTANCE;
	
	public static LocalDate applyPeriod(final LocalDate periodStart, final int period, final PricingPeriod periodUnits) {
		LocalDate result = periodStart;
		switch (periodUnits) {
		case DAYS: {
			result = result.plusDays(period);
			break;
		}
		case WEEKS: {
			result = result.plusWeeks(period);
			break;
		}
		// special case to not go into the next month
		case MONTHS: {
			result = result.plusMonths(period).minusDays(1);
			break;
		}
		case QUARTERS: {
			result = result.plusMonths(3 * period).minusDays(1);
			break;
		}
		}
		return result;
	}
	
	/**
	 *  if pricing month has changed, then we need to re-adjust the pricing and hedging periods.
	 *  by default the hedging period is equal to the contract month
	 *  and the pricing period is equal to the contract month minus one month
	 *
	 */ 
	public static Command createCommands(final PaperDeal paperDeal, final EditingDomain editingDomain, //
			final Object changedObject, final EStructuralFeature feature, final Object newValue) {
		YearMonth contractMonth = paperDeal.getPricingMonth();
		boolean monthChanged = false;
		//
		SettleStrategy ss = paperDeal.getInstrument();
		int startDay = 1;
		int lastDay = -1;
		boolean periodsChanged = false;
		//
		int pricingPeriod = 1;
		PricingPeriod pricingPeriodUnit = PricingPeriod.MONTHS;
		int pricingPeriodOffset = 1;
		//
		int hedgingPeriod = 1;
		PricingPeriod hedgingPeriodUnit = PricingPeriod.MONTHS;
		int hedgingPeriodOffset = 0;
		
		if (changedObject instanceof final YearMonth foo) {
			contractMonth = foo;
			// we only react at the change of the contract month, if instrument is not null
			// otherwise the set pricing and hedging periods are not changing
			monthChanged = ss != null;
		}
		if (changedObject instanceof final SettleStrategy foo) {
			periodsChanged = true;
			ss = foo;
		}
		if (ss != null && ss.getPricingPeriod() != null && ss.getHedgingPeriod() != null) {
			pricingPeriod = ss.getPricingPeriod().getPeriodSize();
			pricingPeriodUnit = ss.getPricingPeriod().getPeriodSizeUnit();
			pricingPeriodOffset = ss.getPricingPeriod().getPeriodOffset();
			
			hedgingPeriod = ss.getHedgingPeriod().getPeriodSize();
			hedgingPeriodUnit = ss.getHedgingPeriod().getPeriodSizeUnit();
			hedgingPeriodOffset = ss.getHedgingPeriod().getPeriodOffset();
			
			startDay = ss.getDayOfTheMonth();
			lastDay = ss.isLastDayOfTheMonth()? 0 : -1;
		}
		if (changedObject instanceof SettleStrategy && newValue != null && feature != null) {
			if (feature == pp.getSettleStrategy_DayOfTheMonth()) {
				startDay = (int) newValue;
			} else if (feature == pp.getSettleStrategy_LastDayOfTheMonth()) {
				final boolean isLastDay = (boolean) newValue;
				lastDay = isLastDay? 0 : -1;
			}
		}
		if (changedObject instanceof final InstrumentPeriod ip && newValue != null && feature != null) {
			periodsChanged = true;
			if (pp.getSettleStrategy_HedgingPeriod() == ip.eContainingFeature()) {
				if (feature == pp.getInstrumentPeriod_PeriodOffset()) {
					hedgingPeriodOffset = (int) newValue;
				} else if (feature == pp.getInstrumentPeriod_PeriodSize()) {
					hedgingPeriod = (int) newValue;
				} else if (feature == pp.getInstrumentPeriod_PeriodSizeUnit()) {
					hedgingPeriodUnit = (PricingPeriod) newValue;
				}
			} else if (pp.getSettleStrategy_PricingPeriod() == ip.eContainingFeature()) {
				if (feature == pp.getInstrumentPeriod_PeriodOffset()) {
					pricingPeriodOffset = (int) newValue;
				} else if (feature == pp.getInstrumentPeriod_PeriodSize()) {
					pricingPeriod = (int) newValue;
				} else if (feature == pp.getInstrumentPeriod_PeriodSizeUnit()) {
					pricingPeriodUnit = (PricingPeriod) newValue;
				}
			}
		}
		if (monthChanged || periodsChanged) {
			CompoundCommand cc = new CompoundCommand();
			
			final LocalDate hedgingStart = contractMonth.atDay(1).minusMonths(hedgingPeriodOffset);
			final LocalDate hedgingEnd = PaperDealUpdatingCommandUtil.applyPeriod(hedgingStart, hedgingPeriod, hedgingPeriodUnit);

			final YearMonth pricingMonth = contractMonth.minusMonths(pricingPeriodOffset);
			final LocalDate pricingStart = pricingMonth.atDay(lastDay == -1 ? startDay : pricingMonth.lengthOfMonth());
			final LocalDate pricingEnd = PaperDealUpdatingCommandUtil.applyPeriod(pricingStart, pricingPeriod, pricingPeriodUnit);
			
			cc.append(SetCommand.create(editingDomain, paperDeal, cp.getPaperDeal_PricingPeriodStart(), pricingStart));
			cc.append(SetCommand.create(editingDomain, paperDeal, cp.getPaperDeal_PricingPeriodEnd(), pricingEnd));
			cc.append(SetCommand.create(editingDomain, paperDeal, cp.getPaperDeal_HedgingPeriodStart(), hedgingStart));
			cc.append(SetCommand.create(editingDomain, paperDeal, cp.getPaperDeal_HedgingPeriodEnd(), hedgingEnd));
			
			return cc;
		}
		return null;
	}
}

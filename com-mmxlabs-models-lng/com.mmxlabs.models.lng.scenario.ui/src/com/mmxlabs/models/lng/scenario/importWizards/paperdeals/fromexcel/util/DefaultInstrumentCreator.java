package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.util.Map;

import com.mmxlabs.models.lng.pricing.InstrumentPeriod;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.types.PricingPeriod;

/**
 * Creates instruments/settle strategies based on common instruments found in Lingo
 * @author Isaac
 *
 */
public class DefaultInstrumentCreator {
	
	private record SettleStrategyData(int dayOfMonth, int hedgingPeriodOffset, int hedgingPeriodSize, PricingPeriod hedgingPeriodUnit,
			int pricingPeriodOffset, int pricingPeriodSize, PricingPeriod pricingPeriodUnit){
	}
	
	private static final int JKM_SWAP_DAY_OF_MONTH = 15;
	private static final int JKM_SWAP_HEDGING_PERIOD_OFFSET = 0;
	private static final int JKM_SWAP_HEDGING_PERIOD_SIZE = 2;
	private static final PricingPeriod JKM_SWAP_HEDGING_PERIOD_UNIT = PricingPeriod.MONTHS;
	private static final int JKM_SWAP_PRICING_PERIOD_OFFSET = 2;
	private static final int JKM_SWAP_PRICING_PERIOD_SIZE = 1;
	private static final PricingPeriod JKM_SWAP_PRICING_PERIOD_UNIT = PricingPeriod.MONTHS;
	
	private static final int BRENT_FUTURES_DAY_OF_MONTH = 1;
	private static final int BRENT_FUTURES_HEDGING_PERIOD_OFFSET = 2;
	private static final int BRENT_FUTURES_HEDGING_PERIOD_SIZE = 1;
	private static final PricingPeriod BRENT_FUTURES_HEDGING_PERIOD_UNIT = PricingPeriod.MONTHS;
	private static final int BRENT_FUTURES_PRICING_PERIOD_OFFSET = 1;
	private static final int BRENT_FUTURES_PRICING_PERIOD_SIZE = 1;
	private static final PricingPeriod BRENT_FUTURES_PRICING_PERIOD_UNIT = PricingPeriod.MONTHS;
	
	private static final int DATED_BRENT_SWAP_DAY_OF_MONTH = 1;
	private static final int DATED_BRENT_SWAP_HEDGING_PERIOD_OFFSET = 3;
	private static final int DATED_BRENT_SWAP_HEDGING_PERIOD_SIZE = 1;
	private static final PricingPeriod DATED_BRENT_SWAP_HEDGING_PERIOD_UNIT = PricingPeriod.MONTHS;
	private static final int DATED_BRENT_SWAP_PRICING_PERIOD_OFFSET = 2;
	private static final int DATED_BRENT_SWAP_PRICING_PERIOD_SIZE = 1;
	private static final PricingPeriod DATED_BRENT_SWAP_PRICING_PERIOD_UNIT = PricingPeriod.MONTHS;
	
	private static final int TFU_SWAP_SWAP_DAY_OF_MONTH = 1;
	private static final int TFU_SWAP_SWAP_HEDGING_PERIOD_OFFSET = 2;
	private static final int TFU_SWAP_SWAP_HEDGING_PERIOD_SIZE = 1;
	private static final PricingPeriod TFU_SWAP_SWAP_HEDGING_PERIOD_UNIT = PricingPeriod.MONTHS;
	private static final int TFU_SWAP_SWAP_PRICING_PERIOD_OFFSET = 1;
	private static final int TFU_SWAP_SWAP_PRICING_PERIOD_SIZE = 1;
	private static final PricingPeriod TFU_SWAP_SWAP_PRICING_PERIOD_UNIT = PricingPeriod.MONTHS;
	
	private static final int TTF_SWAP_SWAP_DAY_OF_MONTH = 1;
	private static final int TTF_SWAP_SWAP_HEDGING_PERIOD_OFFSET = 1;
	private static final int TTF_SWAP_SWAP_HEDGING_PERIOD_SIZE = 1;
	private static final PricingPeriod TTF_SWAP_SWAP_HEDGING_PERIOD_UNIT = PricingPeriod.MONTHS;
	private static final int TTF_SWAP_SWAP_PRICING_PERIOD_OFFSET = 0;
	private static final int TTF_SWAP_SWAP_PRICING_PERIOD_SIZE = 1;
	private static final PricingPeriod TTF_SWAP_SWAP_PRICING_PERIOD_UNIT = PricingPeriod.MONTHS;

	
	private static final Map<String, SettleStrategyData> instrumentMap = Map.of(
				"JKM_SWAP", new SettleStrategyData(JKM_SWAP_DAY_OF_MONTH, JKM_SWAP_HEDGING_PERIOD_OFFSET, JKM_SWAP_HEDGING_PERIOD_SIZE, JKM_SWAP_HEDGING_PERIOD_UNIT, JKM_SWAP_PRICING_PERIOD_OFFSET, JKM_SWAP_PRICING_PERIOD_SIZE, JKM_SWAP_PRICING_PERIOD_UNIT),
				"BRENT_FUTURES", new SettleStrategyData(BRENT_FUTURES_DAY_OF_MONTH, BRENT_FUTURES_HEDGING_PERIOD_OFFSET, BRENT_FUTURES_HEDGING_PERIOD_SIZE, BRENT_FUTURES_HEDGING_PERIOD_UNIT, BRENT_FUTURES_PRICING_PERIOD_OFFSET, BRENT_FUTURES_PRICING_PERIOD_SIZE, BRENT_FUTURES_PRICING_PERIOD_UNIT),
				"DATED_BRENT_SWAP", new SettleStrategyData(DATED_BRENT_SWAP_DAY_OF_MONTH, DATED_BRENT_SWAP_HEDGING_PERIOD_OFFSET, DATED_BRENT_SWAP_HEDGING_PERIOD_SIZE, DATED_BRENT_SWAP_HEDGING_PERIOD_UNIT, DATED_BRENT_SWAP_PRICING_PERIOD_OFFSET, DATED_BRENT_SWAP_PRICING_PERIOD_SIZE, DATED_BRENT_SWAP_PRICING_PERIOD_UNIT),
				"TFU_SWAP", new SettleStrategyData(TFU_SWAP_SWAP_DAY_OF_MONTH, TFU_SWAP_SWAP_HEDGING_PERIOD_OFFSET, TFU_SWAP_SWAP_HEDGING_PERIOD_SIZE, TFU_SWAP_SWAP_HEDGING_PERIOD_UNIT, TFU_SWAP_SWAP_PRICING_PERIOD_OFFSET, TFU_SWAP_SWAP_PRICING_PERIOD_SIZE, TFU_SWAP_SWAP_PRICING_PERIOD_UNIT),
				"TTF_SWAP", new SettleStrategyData(TTF_SWAP_SWAP_DAY_OF_MONTH, TTF_SWAP_SWAP_HEDGING_PERIOD_OFFSET, TTF_SWAP_SWAP_HEDGING_PERIOD_SIZE, TTF_SWAP_SWAP_HEDGING_PERIOD_UNIT, TTF_SWAP_SWAP_PRICING_PERIOD_OFFSET, TTF_SWAP_SWAP_PRICING_PERIOD_SIZE, TTF_SWAP_SWAP_PRICING_PERIOD_UNIT)
			);
	
	public static SettleStrategy getInstrument(String name) {
		if(!instrumentMap.containsKey(name))
			return null;
			
		final SettleStrategyData data = instrumentMap.get(name);
		
		SettleStrategy instrument = PricingFactory.eINSTANCE.createSettleStrategy();
		instrument.setName(name);
		instrument.setDayOfTheMonth(data.dayOfMonth);
		
		InstrumentPeriod hedgingPeriod = PricingFactory.eINSTANCE.createInstrumentPeriod();
		hedgingPeriod.setPeriodOffset(data.hedgingPeriodOffset);
		hedgingPeriod.setPeriodSize(data.hedgingPeriodSize);
		hedgingPeriod.setPeriodSizeUnit(data.hedgingPeriodUnit);
		instrument.setHedgingPeriod(hedgingPeriod);
		
		InstrumentPeriod pricingPeriod = PricingFactory.eINSTANCE.createInstrumentPeriod();
		pricingPeriod.setPeriodOffset(data.pricingPeriodOffset);
		pricingPeriod.setPeriodSize(data.pricingPeriodSize);
		pricingPeriod.setPeriodSizeUnit(data.pricingPeriodUnit);
		instrument.setPricingPeriod(pricingPeriod);
		
		return instrument;
	}
}

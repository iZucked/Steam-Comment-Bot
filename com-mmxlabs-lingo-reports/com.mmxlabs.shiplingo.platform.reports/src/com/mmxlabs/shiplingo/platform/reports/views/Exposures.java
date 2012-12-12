package com.mmxlabs.shiplingo.platform.reports.views;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.PriceExpressionContract;
import com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * @since 2.0
 */
public class Exposures {
	/**
	 * Determines the amount of exposure to a particular index which is created by a specific contract 
	 * 
	 * @param contract
	 * @param index
	 * @return
	 */
	public static double getExposureCoefficient(Contract contract, String indexName) {
		// do a case switch on contract class
		// TODO: refactor this into the actual contract classes?
		if (contract instanceof IndexPriceContract) {
			IndexPriceContract ipc = (IndexPriceContract) contract;
			if (ipc.getIndex().getName().equals(indexName)) {
				return ipc.getMultiplier(); 
			}
		}
		else if (contract instanceof ProfitSharePurchaseContract) {
			// don't know how these work
		}
		else if (contract instanceof PriceExpressionContract) {
			PriceExpressionContract pec = (PriceExpressionContract) contract;
			String expression = pec.getPriceExpression();
			SeriesParser parser = new SeriesParser();
			ISeries parsed = parser.parse(expression).evaluate();
			
		}
		
		
		
		return 0;
	}

	/**
	 * Returns the exposure to a particular named index for a given cargo allocation,
	 * calculated by determining the sum of exposures over the purchase and sales volumes
	 * weighted by the coefficient of the index in the purchase and sales contracts 
	 * respectively
	 * 
	 * @param allocation A cargo allocation
	 * @param indexName The name of the index to calculate exposure for
	 * @return
	 */
	public static double getExposure(CargoAllocation allocation, String indexName) {
		int loadVolume = allocation.getLoadVolume();
		int dischargeVolume = allocation.getDischargeVolume();
		
		Contract purchaseContract = allocation.getLoadAllocation().getSlot().getContract();
		Contract salesContract = allocation.getDischargeAllocation().getSlot().getContract();
		
		double purchaseExposureCoefficient = getExposureCoefficient(purchaseContract, indexName);
		double salesExposureCoefficient = getExposureCoefficient(salesContract, indexName);
		
		return loadVolume * purchaseExposureCoefficient - dischargeVolume * salesExposureCoefficient;
	}
	

	/**
	 * Returns the total exposure to a particular named index for a given schedule,
	 * calculated by summing the exposure over every cargo allocation.
	 * 
	 * @param schedule A schedule
	 * @param indexName The name of the index to calculate exposure for
	 * @return
	 */
	public static double getTotalExposure(Schedule schedule, String indexName) {
		double result = 0;
		for (CargoAllocation allocation: schedule.getCargoAllocations()) {
			result += getExposure(allocation, indexName);						
		}
		return result;
	}
}

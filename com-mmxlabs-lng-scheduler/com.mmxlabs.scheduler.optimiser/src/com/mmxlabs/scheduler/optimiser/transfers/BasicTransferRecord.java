package com.mmxlabs.scheduler.optimiser.transfers;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Basic representation of the transfer record
 */
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

public class BasicTransferRecord {
	private String name;
	private ITransferAgreement agreement;
	
	private ICurve pricingSeries;
	private String priceExpression;
	private int pricingDate;
	
	private String slotName;
	private String nextTransferName;
	
	/**
	 * Slot name must be with internal slots' name prefixes: "FP", "DP", "FS" or "DS"
	 * @param name
	 * @param agreement
	 * @param pricingOverride
	 * @param pricingSeries
	 * @param priceExpression
	 * @param pricingDate
	 * @param slotName
	 * @param nextTransferName
	 */
	public BasicTransferRecord(String name, ITransferAgreement agreement, boolean pricingOverride, @Nullable ICurve  pricingSeries, @Nullable String priceExpression, //
			int pricingDate, String slotName, String nextTransferName) {
		super();
		this.name = name;
		this.agreement = agreement;
		if (pricingOverride) {
			this.pricingSeries = pricingSeries;
			this.priceExpression = priceExpression;
		} else {
			this.pricingSeries = agreement.getPricingSeries();
			this.priceExpression = agreement.getPriceExpression();
		}
		this.pricingDate = pricingDate;
		this.slotName = slotName;
		this.nextTransferName = nextTransferName;
	}
	
	public String getName() {
		return name;
	}
	public IEntity getFromEntity() {
		return this.agreement.getFromEntity();
	}
	public IEntity getToEntity() {
		return this.agreement.getToEntity();
	}
	public ICurve getPricingSeries() {
		return pricingSeries;
	}
	public String getPriceExpression() {
		return priceExpression;
	}
	public int getPricingDate() {
		return pricingDate;
	}
	/**
	 * Must return internal slot name with "FP", "DP", "FS" or "DS" prefixes
	 * @return
	 */
	public String getSlotName() {
		return slotName;
	}
	public String getNextTransferName() {
		return nextTransferName;
	}
	
	public void setNextTransferName(String nextTransferName) {
		this.nextTransferName = nextTransferName;
	}
	
}

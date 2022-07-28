package com.mmxlabs.scheduler.optimiser.transfers;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Basic representation of the transfer record
 */
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

public class BasicTransferRecord {
	private String name;
	private ITransferAgreement agreement;
	
	private ICurve pricingSeries;
	private String priceExpression;
	private int pricingDate;
	
	// reference the IPortSlot instead
	private IPortSlot slot;
	// reference the BasicTransferRecord
	private String nextTransferName;
	
	/**
	 * @param name
	 * @param agreement
	 * @param pricingOverride
	 * @param pricingSeries
	 * @param priceExpression
	 * @param pricingDate
	 * @param slot
	 * @param nextTransferName
	 */
	public BasicTransferRecord(String name, ITransferAgreement agreement, boolean pricingOverride, @Nullable ICurve  pricingSeries, @Nullable String priceExpression, //
			int pricingDate, IPortSlot slot, String nextTransferName) {
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
		this.slot = slot;
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
	public IPortSlot getSlot() {
		return slot;
	}
	public String getNextTransferName() {
		return nextTransferName;
	}
	
	public void setNextTransferName(String nextTransferName) {
		this.nextTransferName = nextTransferName;
	}
	
}

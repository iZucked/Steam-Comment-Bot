package com.mmxlabs.scheduler.optimiser.transfers;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Basic representation of the transfer record
 */
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

public class BasicTransferRecord {
	private final String name;
	private final ITransferAgreement agreement;
	
	private final ICurve pricingSeries;
	private final String priceExpression;
	private final int pricingDate;
	
	private final IPortSlot slot;
	// reference the BasicTransferRecord (in future)
	// Currently (August 2022) does not seem to be necessary
	// as is can be easily inferred
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
			this.pricingSeries = agreement.pricingSeries();
			this.priceExpression = agreement.priceExpression();
		}
		this.pricingDate = pricingDate;
		this.slot = slot;
		this.nextTransferName = nextTransferName;
	}
	
	public String getName() {
		return name;
	}
	public IEntity getFromEntity() {
		return this.agreement.fromEntity();
	}
	public IEntity getToEntity() {
		return this.agreement.toEntity();
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

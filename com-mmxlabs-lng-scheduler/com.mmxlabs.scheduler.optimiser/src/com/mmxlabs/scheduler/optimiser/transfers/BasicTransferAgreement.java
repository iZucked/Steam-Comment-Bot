package com.mmxlabs.scheduler.optimiser.transfers;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * Basic representation of the Transfer Agreement
 * @author FM
 *
 */
public class BasicTransferAgreement implements ITransferAgreement{
	private String name;
	private IEntity fromEntity;
	private IEntity toEntity;
	private ICurve pricingSeries;
	private String priceExpression;
	
	public BasicTransferAgreement(String name, IEntity fromEntity, IEntity toEntity, ICurve pricingSeries, String priceExpression) {
		super();
		this.name = name;
		this.fromEntity = fromEntity;
		this.toEntity = toEntity;
		this.pricingSeries = pricingSeries;
		this.priceExpression = priceExpression;
	}
	
	public String getName() {
		return name;
	}
	public IEntity getFromEntity() {
		return fromEntity;
	}
	public IEntity getToEntity() {
		return toEntity;
	}
	public ICurve getPricingSeries() {
		return pricingSeries;
	}
	public String getPriceExpression() {
		return priceExpression;
	}
	
}

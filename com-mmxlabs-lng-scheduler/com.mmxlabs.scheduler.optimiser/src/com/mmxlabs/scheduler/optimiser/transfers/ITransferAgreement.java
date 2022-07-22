package com.mmxlabs.scheduler.optimiser.transfers;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

public interface ITransferAgreement {
	String getPriceExpression();
	ICurve getPricingSeries();
	IEntity getFromEntity();
	IEntity getToEntity();
}

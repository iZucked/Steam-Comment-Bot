/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * Interface to Scenario Transformer extensions which handle contract logic.
 * 
 * @author hinton
 * 
 */
public interface IContractTransformer extends ITransformerExtension {

	/**
	 * Create an {@link ISalesPriceCalculator} from a {@link SalesContract}.
	 * 
	 * @param c
	 * 
	 * @param sc
	 * @return
	 */
	default ISalesPriceCalculator transformSalesPriceParameters(@Nullable SalesContract salesContract, @NonNull LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	/**
	 * Create an {@link ILoadPriceCalculator} from a {@link PurchaseContract}
	 * 
	 * @param pc
	 * @return
	 */
	default ILoadPriceCalculator transformPurchasePriceParameters(@Nullable PurchaseContract purchaseContract, @NonNull LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	/**
	 */
	Collection<EClass> getContractEClasses();
}

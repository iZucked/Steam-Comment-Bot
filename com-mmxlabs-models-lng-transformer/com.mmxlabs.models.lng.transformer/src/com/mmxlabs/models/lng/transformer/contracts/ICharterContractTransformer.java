/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;

/**
 *
 * @author alex
 * 
 */
public interface ICharterContractTransformer extends ITransformerExtension {
	ICharterContract createCharterContract(GenericCharterContract eGenericCharterContract);
}

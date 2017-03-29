/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;

/**
 *
 * @author alex
 * 
 */
public interface IBallastBonusContractTransformer extends ITransformerExtension {

	IBallastBonusContract createBallastBonusContract(BallastBonusContract eBallastBonusContract);

}

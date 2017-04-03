package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;

public class BallastBonusAnnotation {
	public static final String ANNOTATION_KEY = "BALLAST_BONUS_ANNOTATION";
	public IPort matchedPort = null;
	public long ballastBonusFee = 0L;
	public IBallastBonusRuleAnnotation ballastBonusRuleAnnotation = null;
}

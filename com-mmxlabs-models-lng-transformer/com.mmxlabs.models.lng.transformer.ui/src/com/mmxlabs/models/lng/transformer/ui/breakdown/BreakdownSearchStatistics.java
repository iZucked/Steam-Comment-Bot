/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

public class BreakdownSearchStatistics {

	private int statesSeen = 0;
	private int evaluationsPassed = 0;
	private int evaluationsFailedConstraints = 0;
	private int evaluationsFailedPNL = 0;
	
	public void logStateSeen() {
		this.setStatesSeen(this.getStatesSeen() + 1);
	}
	public void logEvaluationsPassed() {
		this.setEvaluationsPassed(this.getEvaluationsPassed() + 1);
	}
	public void logEvaluationsFailedConstraints() {
		this.setEvaluationsFailedConstraints(this.getEvaluationsFailedConstraints() + 1);
	}
	public void logEvaluationsFailedPNL() {
		this.setEvaluationsFailedPNL(this.getEvaluationsFailedPNL() + 1);
	}
	public int getStatesSeen() {
		return statesSeen;
	}
	public void setStatesSeen(int statesSeen) {
		this.statesSeen = statesSeen;
	}
	public int getEvaluationsPassed() {
		return evaluationsPassed;
	}
	public void setEvaluationsPassed(int evaluationsPassed) {
		this.evaluationsPassed = evaluationsPassed;
	}
	public int getEvaluationsFailedConstraints() {
		return evaluationsFailedConstraints;
	}
	public void setEvaluationsFailedConstraints(int evaluationsFailedConstraints) {
		this.evaluationsFailedConstraints = evaluationsFailedConstraints;
	}
	public int getEvaluationsFailedPNL() {
		return evaluationsFailedPNL;
	}
	public void setEvaluationsFailedPNL(int evaluationsFailedPNL) {
		this.evaluationsFailedPNL = evaluationsFailedPNL;
	}
	
}

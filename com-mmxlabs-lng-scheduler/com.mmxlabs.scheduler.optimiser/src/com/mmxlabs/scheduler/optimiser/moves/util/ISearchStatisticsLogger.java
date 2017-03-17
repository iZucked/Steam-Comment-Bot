/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

public interface ISearchStatisticsLogger {

	void logStateSeen();

	void logEvaluationsPassed();

	void logEvaluationsFailedConstraints();

	void logEvaluationsFailedPNL();
	// public int getStatesSeen() {
	// return statesSeen;
	// }
	// public void setStatesSeen(int statesSeen) {
	// this.statesSeen = statesSeen;
	// }
	// public int getEvaluationsPassed() {
	// return evaluationsPassed;
	// }
	// public void setEvaluationsPassed(int evaluationsPassed) {
	// this.evaluationsPassed = evaluationsPassed;
	// }
	// public int getEvaluationsFailedConstraints() {
	// return evaluationsFailedConstraints;
	// }
	// public void setEvaluationsFailedConstraints(int evaluationsFailedConstraints) {
	// this.evaluationsFailedConstraints = evaluationsFailedConstraints;
	// }
	// public int getEvaluationsFailedPNL() {
	// return evaluationsFailedPNL;
	// }
	// public void setEvaluationsFailedPNL(int evaluationsFailedPNL) {
	// this.evaluationsFailedPNL = evaluationsFailedPNL;
	// }

}
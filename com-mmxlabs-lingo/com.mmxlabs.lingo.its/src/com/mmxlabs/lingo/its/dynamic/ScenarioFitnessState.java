/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.util.LinkedList;
import java.util.List;

public class ScenarioFitnessState {
	private SolutionState initialState;
	private List<SolutionState> solutions = new LinkedList<>();

	public SolutionState getInitialState() {
		return initialState;
	}

	public void setInitialState(final SolutionState initialState) {
		this.initialState = initialState;
	}

	public List<SolutionState> getSolutions() {
		return solutions;
	}

	public void setSolutions(final List<SolutionState> solutions) {
		this.solutions = solutions;
	}

}
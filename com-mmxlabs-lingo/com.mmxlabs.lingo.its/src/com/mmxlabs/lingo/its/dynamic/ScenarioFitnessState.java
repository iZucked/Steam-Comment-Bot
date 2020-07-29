package com.mmxlabs.lingo.its.dynamic;

import java.util.LinkedList;
import java.util.List;

public class ScenarioFitnessState {
	private SolutionState initialState;
	private SolutionState bestState;
	private List<SolutionState> otherSolutions = new LinkedList<>();

	public SolutionState getInitialState() {
		return initialState;
	}

	public void setInitialState(SolutionState initialState) {
		this.initialState = initialState;
	}

	public SolutionState getBestState() {
		return bestState;
	}

	public void setBestState(SolutionState bestState) {
		this.bestState = bestState;
	}

	public List<SolutionState> getOtherSolutions() {
		return otherSolutions;
	}

	public void setOtherSolutions(List<SolutionState> otherSolutions) {
		this.otherSolutions = otherSolutions;
	}

}
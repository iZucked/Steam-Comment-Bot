package com.minimaxlabs.rnd.representation;

import java.io.Serializable;
import java.util.List;

public class LightWeightOutputData implements Serializable {
	public List<List<Integer>> sequences;
	
	public LightWeightOutputData(List<List<Integer>> sequences) {
		this.sequences = sequences;
	}
}

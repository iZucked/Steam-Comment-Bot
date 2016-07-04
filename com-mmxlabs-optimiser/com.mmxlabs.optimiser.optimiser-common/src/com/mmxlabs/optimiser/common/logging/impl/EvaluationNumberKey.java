/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.logging.impl;

public class EvaluationNumberKey implements Comparable<EvaluationNumberKey>{
	private int number;
	public EvaluationNumberKey(int number) {
		this.number = number;
	}
	@Override
	public int hashCode() {
		return number;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EvaluationNumberKey) {
			return ((EvaluationNumberKey) obj).number == this.number;
		}
		return false;
	}
	@Override
	public int compareTo(EvaluationNumberKey o) {
		if (o.equals(this)) {
			return 0;
		} else if (o.number < this.number) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public int getNumber() {
		return number;
	}
}

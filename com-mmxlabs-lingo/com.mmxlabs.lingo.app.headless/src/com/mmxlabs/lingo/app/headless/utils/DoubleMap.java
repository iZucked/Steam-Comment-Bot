/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

import java.util.Map;

public class DoubleMap {
	Map<String, Double> doubleMap;
	

	public DoubleMap() {
	}
	
	public DoubleMap(Map<String, Double> map) {
		setDoubleMap(map);
	}

	public Map<String, Double> getDoubleMap() {
		return doubleMap;
	}
	
	public void setDoubleMap(Map<String, Double> doubleMap) {
		this.doubleMap = doubleMap;
	}
}

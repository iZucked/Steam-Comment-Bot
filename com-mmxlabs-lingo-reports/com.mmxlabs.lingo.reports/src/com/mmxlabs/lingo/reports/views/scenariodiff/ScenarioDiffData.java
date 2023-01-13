/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.scenariodiff;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScenarioDiffData {
	public final Map<String, String> valuesByMonth;
	public ScenarioDiffData parent;
	public final List<ScenarioDiffData> children = new LinkedList<>();
	public String id = "default id";
	public String type = "null";

	public ScenarioDiffData(final Map<String, String> exposuresByMonth) {
		super();

		this.valuesByMonth = exposuresByMonth;
	}

	public ScenarioDiffData(final Map<String, String> exposuresByMonth, String id) {
		super();
		this.valuesByMonth = exposuresByMonth;
		this.id = id;

	}
}

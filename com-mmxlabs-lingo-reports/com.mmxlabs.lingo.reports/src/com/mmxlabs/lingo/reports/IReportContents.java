/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

public interface IReportContents {

	default String getHTMLContents() {
		return "HTML version not implemented";
	};

	default String getJSONContents() {
		return "JSON version not implemented";
	};

}

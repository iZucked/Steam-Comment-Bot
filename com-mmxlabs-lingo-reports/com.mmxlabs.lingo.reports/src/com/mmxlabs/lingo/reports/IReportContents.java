/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

public interface IReportContents {

	default String getHTMLContents() {
		throw new RuntimeException( "HTML version not implemented");
	}

	default String getJSONContents() {
		throw new RuntimeException( "JSON version not implemented");
	}

}

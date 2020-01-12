/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

public final class ReportContents {

	public static IReportContents makeHTML(String html) {
		return new IReportContents() {
			@Override
			public String getHTMLContents() {
				return html;
			}
		};
	}

	public static IReportContents makeJSON(String json) {
		return new IReportContents() {
			@Override
			public String getJSONContents() {
				return json;
			}
		};
	}

	public static IReportContents make(String html, String json) {
		return new IReportContents() {
			@Override
			public String getHTMLContents() {
				return html;
			}

			@Override
			public String getJSONContents() {
				return json;
			}
		};
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

public final class ReportContents {

	private ReportContents() {

	}

	public static IReportContents makeHTML(final String html) {
		return new IReportContents() {
			@Override
			public String getHTMLContents() {
				return html;
			}
		};
	}

	public static IReportContents makeJSON(final String json) {
		return new IReportContents() {
			@Override
			public String getJSONContents() {
				return json;
			}
		};
	}

	public static IReportContents make(final String html, final String json) {
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

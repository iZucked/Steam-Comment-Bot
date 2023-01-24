/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.util.function.Function;

import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class FunctionalFormatter extends BaseFormatter {

	private Function<Object, String> renderer;

	public FunctionalFormatter(Function<Object, String> renderer) {
		this.renderer = renderer;

	}

	@Override
	public String render(final Object object) {
		return renderer.apply(object);
	}
	
	public static FunctionalFormatter from(Function<Object, String> renderer) {
		return new FunctionalFormatter(renderer);
	}
}

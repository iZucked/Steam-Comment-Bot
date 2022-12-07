/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.scenariodiff;

import org.eclipse.jdt.annotation.NonNull;

public interface Matcher<@NonNull T> {

	public boolean matches(T o1, T o2);
}

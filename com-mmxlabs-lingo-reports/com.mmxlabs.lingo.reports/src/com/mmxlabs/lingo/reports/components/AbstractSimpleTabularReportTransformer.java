/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Generic content provider for simple table views.
 * 
 * @author Simon McGregor
 * 
 */
public abstract class AbstractSimpleTabularReportTransformer<T> {

	public static class ColumnManager<T> {
		private final String name;

		public ColumnManager(final String name) {
			this.name = name;
		}

		public String getColumnText(final T obj) {
			return "";
		}

		public String getName() {
			return name;
		}

		public void dispose() {

		}

		public Image getColumnImage(final T obj) {
			return null;
		}

		public Color getBackground(final T element) {
			return null;
		}

		public Color getForeground(final T element) {
			return null;
		}

		public int compare(final T obj1, final T obj2) {
			return 0;
		}

		public boolean isTree() {
			return false;
		}
	}

	abstract public List<ColumnManager<T>> getColumnManagers(@NonNull ISelectedDataProvider selectedDataProvider);

	// abstract public @NonNull List<@NonNull T> createData(@NonNull Schedule schedule, @NonNull LNGScenarioModel rootObject);

	@NonNullByDefault
	public abstract List<@NonNull T> createData(@Nullable Pair<Schedule, ScenarioResult> pinnedPair, List<Pair<Schedule, ScenarioResult>> otherPairs);
}

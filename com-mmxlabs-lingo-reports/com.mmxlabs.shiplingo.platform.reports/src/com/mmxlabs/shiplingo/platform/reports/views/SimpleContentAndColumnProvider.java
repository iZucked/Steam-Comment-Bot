/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

/**
 * Generic content provider for simple table views.
 * 
 * @author Simon McGregor
 * @since 2.0
 * 
 */
public abstract class SimpleContentAndColumnProvider<T> implements IStructuredContentProvider {

	protected static class ColumnManager<T> {
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
	}

	private T[] rowData = (T[]) new Object[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	abstract public List<ColumnManager<T>> getColumnManagers();

	/**
	 * @since 3.0
	 */
	abstract protected List<T> createData(final Schedule schedule, LNGScenarioModel rootObject, LNGPortfolioModel portfolioModel);

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		pinnedData.clear();
		rowData = (T[]) new Object[0];
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					rowData = createData((Schedule) o, synchOutput.getLNGScenarioModel(o), synchOutput.getLNGPortfolioModel(o)).toArray(rowData);
					return;
				}
			}
		}

	}

	private final List<IndexExposureData> pinnedData = new ArrayList<IndexExposureData>();

	public List<IndexExposureData> getPinnedScenarioData() {
		return pinnedData;
	}

	@Override
	public void dispose() {

	}
}

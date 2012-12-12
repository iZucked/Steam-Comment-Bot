/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public class ExposureContentProvider implements IStructuredContentProvider {
	protected static class ColumnManager<T> {
		private String name;
		
		public ColumnManager(String name) {
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
	
	public static class IndexExposureData {
		public final String indexName;
		public final double exposure;

		public IndexExposureData(final String indexName, final double exposure) {
			super();
			this.indexName = indexName;
			this.exposure = exposure;
		}

	}

	private IndexExposureData[] rowData = new IndexExposureData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	public List<ColumnManager<IndexExposureData>> getColumnManagers() {
		ArrayList<ColumnManager<IndexExposureData>> result = new ArrayList<ColumnManager<IndexExposureData>>();
		
		result.add(new ColumnManager<IndexExposureData>("Index") {
			public String getColumnText(IndexExposureData data) {
				return data.indexName;
			}
			
			public int compare(IndexExposureData o1, IndexExposureData o2) {
				return o1.indexName.compareTo(o2.indexName);
			}			
		});
		
		result.add(new ColumnManager<IndexExposureData>("Exposure") {
			public String getColumnText(IndexExposureData data) {
				return Double.toString(data.exposure);
			}
			
			public int compare(IndexExposureData o1, IndexExposureData o2) {
				return Double.compare(o1.exposure, o2.exposure);
			}
		});
		
		return result;		
	}
	
	
	private List<IndexExposureData> createRowData(final Schedule schedule, final MMXRootObject root) {
		final List<IndexExposureData> output = new ArrayList<IndexExposureData>();
		EObject object = schedule.eContainer();
		while ((object != null) && !(object instanceof MMXRootObject)) {
			if (object instanceof EObject) {
				object = ((EObject) object).eContainer();
			}
		}
		
		PricingModel pm = root.getSubModel(PricingModel.class);
		EList<Index<Double>> indices = pm.getCommodityIndices();
		
		for (Index<Double> index: indices) {
			output.add(new IndexExposureData(index.getName(), Exposures.getTotalExposure(schedule, index.getName())));
		}
		
		return output;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		pinnedData.clear();
		rowData = new IndexExposureData[0];
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					rowData = createRowData((Schedule) o, synchOutput.getRootObject(o)).toArray(rowData);
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

package com.mmxlabs.scheduler.optimiser.exposures;

import java.util.LinkedList;
import java.util.List;

public class ExposureRecords implements IExposureNode {

	public List<ExposureRecord> records = new LinkedList<>();

	public ExposureRecords(final ExposureRecord record) {
		records.add(record);
	}

	public ExposureRecords() {
	}

}

package com.mmxlabs.lngdataserver.lng.importers.lingodata.model;

import java.util.LinkedList;
import java.util.List;

public class DataManifest {

	private List<Entry> entries = new LinkedList<>();

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(final List<Entry> entries) {
		this.entries = entries;
	}
}

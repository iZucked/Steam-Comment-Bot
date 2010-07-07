package org.eclipse.nebula.widgets.ganttchart;

import java.util.ArrayList;
import java.util.List;

public class HeaderSet {

	private List<HeaderLevel> headers;
	
	public HeaderSet() {
		headers = new ArrayList<HeaderLevel>();
	}
	
	public void addHeaderLevel(HeaderLevel hl) {
		headers.add(hl);
	}
	
	public void removeHeaderLevel(HeaderLevel hl) {
		headers.remove(hl);
	}
	
	public List<HeaderLevel> getHeaderLevels() {
		return headers;
	}
}

package com.mmxlabs.lngdataserver.server.editors.model.layout;

import java.util.LinkedList;
import java.util.List;

public class Row extends ILayoutElement {

	public Row() {
		elementType = "row";
	}

	public List<ILayoutElement> children = new LinkedList<>();

}

package com.mmxlabs.lngdataserver.server.editors.model.layout;

import java.util.LinkedList;
import java.util.List;

public class Column extends ILayoutElement {

	public Column() {
		elementType = "column";
	}

	public List<ILayoutElement> children = new LinkedList<>();

}

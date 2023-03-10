/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.editors.model.layout;

import java.util.LinkedList;
import java.util.List;

public class Column extends ILayoutElement {

	public Column() {
		elementType = "column";
	}

	public List<ILayoutElement> children = new LinkedList<>();

}

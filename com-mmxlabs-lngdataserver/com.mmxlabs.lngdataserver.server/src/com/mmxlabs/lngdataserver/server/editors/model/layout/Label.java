/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.editors.model.layout;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Label extends ILayoutElement {
	public String label;

	public Label(String label) {
		elementType = "label";
		this.label = label;
	}

}

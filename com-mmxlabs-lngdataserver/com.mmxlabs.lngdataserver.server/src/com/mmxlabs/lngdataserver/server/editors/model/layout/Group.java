package com.mmxlabs.lngdataserver.server.editors.model.layout;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group extends ILayoutElement {
	public Group() {
		elementType = "group";
	}
	
	public String label;
	public ILayoutElement child;

}

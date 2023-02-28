package com.mmxlabs.lngdataserver.server.editors.model;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.server.editors.model.layout.ILayoutElement;

public class EditorField extends ILayoutElement {

	public EditorField() {
		elementType = "editor";
	}

	public String objectId; // Owner object Id
	public String featureName; // FeatureId

	public String type; // Editor type

	public String label; // Display name for field
	public Object value; // Current value

	// Validation
	public int severity;
	public String msg;

	public boolean multi = false;
	
	// Permitted values
	public List<Pair<String, String>> allowedValues;

}

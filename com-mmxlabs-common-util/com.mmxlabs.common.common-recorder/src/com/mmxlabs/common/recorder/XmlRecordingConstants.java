/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder;

/**
 * Set of constants used in the {@link InterfaceRecorder} to generate XML files.
 * 
 * @author Simon Goodall
 * 
 */
public final class XmlRecordingConstants {

	private XmlRecordingConstants() {

	}

	public static final String ELEMENT_OPERATIONS = "operations";
	public static final String ATTR_OPERATIONS_INTERFACE = "interface";

	public static final String ELEMENT_METHOD = "method";
	public static final String ATTR_METHOD_NAME = "name";
	public static final String ATTR_METHOD_RETURNED_REF = "ref-name";

	public static final String ELEMENT_ARGUMENT = "argument";
	public static final String ATTR_ARGUMENT_TYPE = "type";
	public static final String ATTR_ARGUMENT_VALUE = "value";
	public static final String ATTR_ARGUMENT_REF = "ref";

}

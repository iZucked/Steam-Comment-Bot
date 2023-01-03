/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.util.Map;
import java.util.Set;

public interface IFieldMap extends Map<String, String> {

	IFieldMap getSubMap(String keyPrefix);

	Set<String> getUnreadKeys();

	String getLastAccessedKey();

	boolean containsPrefix(String keyPrefix);
}

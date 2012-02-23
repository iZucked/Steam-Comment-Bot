package com.mmxlabs.models.util.importer;

import java.util.Map;
import java.util.Set;

public interface IFieldMap extends Map<String, String> {
	public IFieldMap getSubMap(final String keyPrefix);
	public Set<String> getUnreadKeys();
	public String getLastAccessedKey();
}

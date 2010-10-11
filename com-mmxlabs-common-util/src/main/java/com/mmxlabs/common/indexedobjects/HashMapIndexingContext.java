package com.mmxlabs.common.indexedobjects;

import java.util.HashMap;

public class HashMapIndexingContext implements IIndexingContext {
	private HashMap<Class<? extends IndexedObject>, Integer> typeIndices 
		= new HashMap<Class<? extends IndexedObject>, Integer>();
	
	@Override
	public synchronized int assignIndex(final IndexedObject indexedObject) {
		Class<? extends IndexedObject> type = indexedObject.getClass();
		int result = 0;
		if (typeIndices.containsKey(type)) {
			result = typeIndices.get(type) + 1;
		}
		typeIndices.put(type, result);
		return result;
	}
}

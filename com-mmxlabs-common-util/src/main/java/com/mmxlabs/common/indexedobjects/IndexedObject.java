package com.mmxlabs.common.indexedobjects;

public class IndexedObject implements IIndexedObject {
	public final int index;
	public IndexedObject(final IIndexingContext provider) {
		index = provider.assignIndex(this);
	}
	public final int getIndex() {
		return index;
	}
}

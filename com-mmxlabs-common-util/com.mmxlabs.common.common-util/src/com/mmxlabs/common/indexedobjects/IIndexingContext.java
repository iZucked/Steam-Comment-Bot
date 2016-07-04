/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

import com.mmxlabs.common.indexedobjects.impl.IndexedObject;

/**
 * An indexing context, which provides indices (unique numbers) to Objects. Separate counters are maintained for different classes.
 * 
 * Indices are assigned sequentially by the assignIndex method, although it needn't check for repeated objects; in {@link IndexedObject} the index is final and thus allocated once during object
 * construction.
 * 
 * To accommodate inheritance, the index for an object is selected in accordance with the most specialized type which has been registered with this context using the
 * {@link IIndexingContext#registerType(Class)} method.
 * 
 * The idea here is that if you are creating a bunch of objects of a given class C (or subclasses of C), and you want to be able to easily look-up related data, you make them {@link IndexedObject}s
 * (or similar), use the {@link IIndexingContext#registerType(Class)} method to register C with a context, and then use that context to assign them indices. These indices can then be used to index
 * into arrays for fast lookups.
 * 
 * @author hinton
 * 
 */
public interface IIndexingContext {
	void registerType(Class<? extends Object> type);

	int assignIndex(Object indexedObject);
}

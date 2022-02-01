/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree;

import java.util.List;

/**
 * The detail tree is a type which holds a key-value hierarchy, for mapping out
 * intermediate values in long calculations and the like.
 * 
 * @author Tom Hinton
 * 
 */
public interface IDetailTree {
	/**
	 * Get any sub-trees of this tree.
	 * 
	 * @return
	 */
	List<IDetailTree> getChildren();

	/**
	 * Get the key for this branch.
	 * 
	 * @return
	 */
	String getKey();

	/**
	 * Get the summary value for this branch and any children.
	 * 
	 * null if there is no summary value.
	 * 
	 * @return
	 */
	Object getValue();

	void addChild(IDetailTree child);

	IDetailTree addChild(String key, Object value);
}

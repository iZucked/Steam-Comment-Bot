/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree;

import java.util.List;

/**
 * The detail tree is a type which holds a key-value hierarchy, for mapping out intermediate values in long calculations and the like.
 * 
 * Perhaps one day it would be worth writing an interface which lets you describe a computation and will create bytecode for fast evaluation and for slow, details generating evaluation.
 * 
 * @author hinton
 * 
 */
public interface IDetailTree {
	/**
	 * Get any sub-trees of this tree.
	 * 
	 * @return
	 */
	public List<IDetailTree> getChildren();

	/**
	 * Get the key for this branch.
	 * 
	 * @return
	 */
	public String getKey();

	/**
	 * Get the summary value for this branch and any children.
	 * 
	 * null if there is no summary value.
	 * 
	 * TODO should the type information be here, or should there be another accessor for that?
	 * 
	 * @return
	 */
	public Object getValue();

	void addChild(IDetailTree child);

	IDetailTree addChild(String key, Object value);
}

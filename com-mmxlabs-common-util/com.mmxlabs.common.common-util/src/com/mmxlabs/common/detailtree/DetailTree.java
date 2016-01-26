/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple detail tree implementation
 * 
 * @author hinton
 * 
 */
public class DetailTree implements IDetailTree {
	private final LinkedList<IDetailTree> children = new LinkedList<IDetailTree>();
	private final String key;
	private final Object value;

	public DetailTree(final String key, final Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public DetailTree() {
		this("", null);
	}

	@Override
	public List<IDetailTree> getChildren() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void addChild(final IDetailTree child) {
		children.add(child);
	}

	/**
	 * Convenience method, same as addChild(new DetailTree(key, value));
	 * 
	 * @param key
	 * @param value
	 * @return the new child entry.
	 */
	@Override
	public DetailTree addChild(final String key, final Object value) {
		final DetailTree newChild = new DetailTree(key, value);
		addChild(newChild);
		return newChild;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();

		sb.append(key + (value == null ? "" : ": " + value));

		for (final IDetailTree child : children) {
			for (final String s : ((child + "").split("\n"))) {
				sb.append("\n   |" + s);
			}
		}

		return sb.toString();
	}
}

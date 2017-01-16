/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

import java.util.List;

public class StringList {
	List<String> list;

	public StringList() {
	}
	
	public StringList(List<String> list) {
		setList(list);
	}

	
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	
	
}

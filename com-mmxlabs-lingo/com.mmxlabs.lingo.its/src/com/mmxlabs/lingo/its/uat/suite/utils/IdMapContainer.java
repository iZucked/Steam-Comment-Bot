/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public class IdMapContainer {
	String name;
	List<IdMap> idMapList;

	public IdMapContainer(final String name) {
		this.name = name;
		this.idMapList = new ArrayList<IdMap>();
	}

	public void addToList(final IdMap m) {
		getIdMapList().add(m);
	}

	@NonNull
	public List<IdMap> getIdMapList() {
		return idMapList;
	}

}

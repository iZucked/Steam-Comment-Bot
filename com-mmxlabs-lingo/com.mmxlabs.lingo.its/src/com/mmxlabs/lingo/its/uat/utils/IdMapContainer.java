/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.utils;

import java.util.ArrayList;
import java.util.List;

public class IdMapContainer {
	String name;
	List<IdMap> idMapList;

	public IdMapContainer(String name) {
		this.name = name;
		setIdMapList(new ArrayList<IdMap>());
	}
	
	public void addToList(IdMap m) {
		getIdMapList().add(m);
	}

	public List<IdMap> getIdMapList() {
		return idMapList;
	}

	public void setIdMapList(List<IdMap> idMapList) {
		this.idMapList = idMapList;
	}
}

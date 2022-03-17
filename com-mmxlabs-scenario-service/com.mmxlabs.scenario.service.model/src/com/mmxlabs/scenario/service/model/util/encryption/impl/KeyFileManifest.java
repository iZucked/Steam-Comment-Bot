/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl;

import java.util.LinkedList;
import java.util.List;

public class KeyFileManifest {
	private String currentKey;
	private List<String> keyFiles = new LinkedList<>();

	public String getCurrentKey() {
		return currentKey;
	}

	public void setCurrentKey(String currentKey) {
		this.currentKey = currentKey;
	}

	public List<String> getKeyFiles() {
		return keyFiles;
	}

	public void setKeyFiles(List<String> keyFiles) {
		this.keyFiles = keyFiles;
	}
}

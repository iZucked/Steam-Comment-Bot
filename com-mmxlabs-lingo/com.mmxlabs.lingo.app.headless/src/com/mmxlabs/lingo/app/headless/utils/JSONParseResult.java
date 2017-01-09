/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.common.Pair;

public class JSONParseResult {
	Set<String> notInParams = new HashSet<String>();
	Set<String> notInJSON = new HashSet<String>();
	Set<Pair<String, Object>> notInType = new HashSet<>();
	Set<String> requirementNotSet = new HashSet<>();
	
	public void addToNotInParams(String el) {
		notInParams.add(el);
	}

	public void addToNotInJSON(String el) {
		notInJSON.add(el);
	}

	public void addToNotInType(String key, Object o) {
		notInType.add(new Pair<>(key, o));
	}

	public void setNotInJSON(Set<String> unknowns) {
		notInJSON = unknowns;
	}
	
	public void setDefaultNotSet(String defaultName) {
		requirementNotSet.add(defaultName);
	}
	
	public boolean allRequirementsPassed() {
		if (requirementNotSet.size() == 0) {
			return true;
		}
		return false;
	}
	
	public String[] getRequiredMissingText() {
		String[] errors = new String[requirementNotSet.size()];
		int i = -1;
		for (String requirement : requirementNotSet) {
			errors[++i] = String.format("Required parameter %s not present in JSON file or not parseable", requirement);
		}
		return errors;
	}
}

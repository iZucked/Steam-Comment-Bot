/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataser.lng.importers.merge.support;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.mmxcore.NamedObject;

public class MappingOption<T extends NamedObject> extends MergeOption {
	
	@NonNull
	private final T element;
	private final int listIndex;
	
	public MappingOption(@NonNull final T element, final int listIndex) {
		this.element = element;
		this.listIndex = listIndex;
	}

	@NonNull
	public T getElement() {
		return this.element;
	}

	@Override
	public String toString() {
		return ScenarioElementNameHelper.getName(element, "<Unknown>");
	}

	@Override
	public int getIndex(final int offset) {
		return offset + listIndex;
	}
}

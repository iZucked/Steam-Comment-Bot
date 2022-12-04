/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.List;

public class MultiEvent {
	private List<Object> elements;

	public MultiEvent(final List<Object> elements) {
		this.elements = elements;
	}

	public List<Object> getElements() {
		return elements;
	}

}

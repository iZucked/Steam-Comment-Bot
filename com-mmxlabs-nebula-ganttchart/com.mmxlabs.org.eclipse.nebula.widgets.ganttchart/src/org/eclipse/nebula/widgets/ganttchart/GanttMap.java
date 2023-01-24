/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GanttMap {
	private final Map<GanttEvent, List<GanttEvent>> _map;

	public GanttMap() {
		_map = new HashMap<>();
	}

	public void put(final GanttEvent key, final List<GanttEvent> connections) {
		_map.put(key, connections);
	}

	public void put(final GanttEvent value, final GanttEvent key) {
		if (_map.containsKey(value)) {
			final List<GanttEvent> vList = _map.get(value);
			if (!vList.contains(key)) {
				vList.add(key);
			}

			_map.put(value, vList);
		} else {
			final List<GanttEvent> vList = new ArrayList<>();
			vList.add(key);

			_map.put(value, vList);
		}
	}

	public void remove(final GanttEvent key) {
		_map.remove(key);
	}

	public List<GanttEvent> get(final GanttEvent obj) {
		return _map.get(obj);
	}

	public void clear() {
		_map.clear();
	}

}

/*******************************************************************************
 * Copyright (c) Emil Crumhorn - Hexapixel.com - emil.crumhorn@gmail.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    emil.crumhorn@gmail.com - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.ganttchart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class GanttMap {
	private HashMap<GanttEvent, List<GanttEvent>>	mMap;

	public GanttMap() {
		mMap = new HashMap<GanttEvent, List<GanttEvent>>();
	}
	
	public void put(GanttEvent key, List<GanttEvent> connections) {
		mMap.put(key, connections);
	}

	public void put(GanttEvent value, GanttEvent key) {
		if (mMap.containsKey(value)) {
			List<GanttEvent> v = mMap.get(value);
			if (!v.contains(key)) {
				v.add(key);
			}

			mMap.put(value, v);
		} else {
			List<GanttEvent> v = new ArrayList<GanttEvent>();
			v.add(key);

			mMap.put(value, v);
		}
	}

	public void remove(GanttEvent key) {
		mMap.remove(key);
	}
	
	public List<GanttEvent> get(GanttEvent obj) {
		return mMap.get(obj);
	}

	public void clear() {
		mMap.clear();
	}

}

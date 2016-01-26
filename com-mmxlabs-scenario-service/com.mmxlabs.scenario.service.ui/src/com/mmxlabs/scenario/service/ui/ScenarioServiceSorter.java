/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioServiceSorter extends ViewerSorter {

	private enum Types {
		FRAGMENT, CONTAINER, FOLDER, SCENARIO, UNKNOWN
	}

	public ScenarioServiceSorter() {
	}

	public ScenarioServiceSorter(final Collator collator) {
		super(collator);
	}

	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {

		final Types e1Type = getType(e1);
		final Types e2Type = getType(e2);

		if (e1Type == e2Type) {
			if (e1Type != Types.UNKNOWN) {
				final String n1 = getName(e1);
				final String n2 = getName(e2);

				if (n1 == null) {
					return -1;
				} else if (n2 == null) {
					return 1;
				}
				return n1.toLowerCase().compareTo(n2.toLowerCase());
			}

			return super.compare(viewer, e1Type, e2Type);
		}

		return e1Type.ordinal() - e2Type.ordinal();

	}

	String getName(final Object o) {
		if (o instanceof Container) {
			return ((Container) o).getName();
		} else if (o instanceof ScenarioFragment) {
			return ((ScenarioFragment) o).getName();
		}
		return null;
	}

	Types getType(final Object obj) {
		Types type = Types.UNKNOWN;
		if (obj instanceof ScenarioInstance) {
			type = Types.SCENARIO;
		} else if (obj instanceof Folder) {
			type = Types.FOLDER;
		} else if (obj instanceof Container) {
			type = Types.CONTAINER;
		} else if (obj instanceof ScenarioFragment) {
			type = Types.FRAGMENT;
		}
		return type;
	}
}

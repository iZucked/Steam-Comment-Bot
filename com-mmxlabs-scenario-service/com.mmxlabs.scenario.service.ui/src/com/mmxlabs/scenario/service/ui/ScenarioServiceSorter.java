/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioServiceSorter extends ViewerSorter {

	private enum Types {
		CONTAINER, FOLDER, SCENARIO, UNKNOWN
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
				final Container c1 = (Container) e1;
				final Container c2 = (Container) e2;

				// todo need nul cjecks
				if (c1.getName() == null) {
					return -1;
				} else if (c2.getName() == null) {
					return 1;
				}
				return c1.getName().compareTo(c2.getName());
			}

			return super.compare(viewer, e1Type, e2Type);
		}

		return e1Type.ordinal() - e2Type.ordinal();

	}

	Types getType(final Object obj) {
		Types type = Types.UNKNOWN;
		if (obj instanceof ScenarioInstance) {
			type = Types.SCENARIO;
		} else if (obj instanceof Folder) {
			type = Types.FOLDER;
		} else if (obj instanceof Container) {
			type = Types.CONTAINER;
		}
		return type;
	}
}

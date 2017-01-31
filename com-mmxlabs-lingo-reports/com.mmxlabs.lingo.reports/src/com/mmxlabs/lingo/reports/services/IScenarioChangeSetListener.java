/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;

public interface IScenarioChangeSetListener {

	void changeSetChanged(@Nullable ChangeSetTableRoot changeSetRoot, @Nullable ChangeSetTableGroup changeSet, @Nullable  Collection<ChangeSetTableRow> changeSetRows, boolean diffToBase);
	
}

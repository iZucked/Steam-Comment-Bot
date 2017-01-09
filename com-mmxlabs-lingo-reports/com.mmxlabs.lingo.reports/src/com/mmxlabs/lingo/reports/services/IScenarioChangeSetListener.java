/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;

public interface IScenarioChangeSetListener {

	void changeSetChanged(@Nullable ChangeSetRoot changeSetRoot, @Nullable ChangeSet changeSet, @Nullable  Collection<ChangeSetRow> changeSetRows, boolean diffToBase);
	
}

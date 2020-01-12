/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.extpoint;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface IWrappedCommandProvider {

	void registerEditingDomain(final Manifest manifest, EditingDomain editingDomain);

	void deregisterEditingDomain(final Manifest manifest, EditingDomain editingDomain);

	@Nullable
	Command provideCommand(@NonNull Command cmd, @NonNull EditingDomain editingDomain);
}

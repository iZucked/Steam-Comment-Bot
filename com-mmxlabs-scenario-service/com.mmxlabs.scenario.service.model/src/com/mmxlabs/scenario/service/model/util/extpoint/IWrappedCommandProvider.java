package com.mmxlabs.scenario.service.model.util.extpoint;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IWrappedCommandProvider {

	void registerEditingDomain(EditingDomain editingDomain);

	void deregisterEditingDomain(EditingDomain editingDomain);

	@Nullable
	Command provideCommand(@NonNull Command cmd, @NonNull EditingDomain editingDomain);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.mmxcore.MMXRootObject;

@NonNullByDefault
public interface IDialogPostChangeCommandProvider {

	Command provideExtraCommand(EditingDomain editingDomain, Command baseCommand, MMXRootObject rootObject, Collection<EObject> roots);
}

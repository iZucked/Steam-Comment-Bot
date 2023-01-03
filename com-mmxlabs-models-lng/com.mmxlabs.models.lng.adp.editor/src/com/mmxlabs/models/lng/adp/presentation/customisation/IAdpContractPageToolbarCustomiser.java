/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.customisation;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorData;

@NonNullByDefault
public interface IAdpContractPageToolbarCustomiser {

	void runSelectionChangePreHandleTasks();

	void runSelectionChangeHandlingTasks(EObject target);

	void customiseToolbar(Composite toolbarComposite, ADPEditorData editorData, Supplier<EObject> inputProvider, Consumer<EObject> inputConsumer);
}

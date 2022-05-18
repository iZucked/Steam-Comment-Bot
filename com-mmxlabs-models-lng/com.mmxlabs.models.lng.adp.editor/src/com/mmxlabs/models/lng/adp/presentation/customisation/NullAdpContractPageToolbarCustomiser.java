package com.mmxlabs.models.lng.adp.presentation.customisation;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorData;

@NonNullByDefault
public class NullAdpContractPageToolbarCustomiser implements IAdpContractPageToolbarCustomiser {

	@Override
	public void runSelectionChangePreHandleTasks() {
	}

	@Override
	public void runSelectionChangeHandlingTasks(final EObject target) {
	}

	@Override
	public void customiseToolbar(final Composite toolbarComposite, final ADPEditorData editorData, final Supplier<@NonNull EObject> inputProvider,
			final Consumer<@NonNull EObject> inputConsumer) {
	}

}

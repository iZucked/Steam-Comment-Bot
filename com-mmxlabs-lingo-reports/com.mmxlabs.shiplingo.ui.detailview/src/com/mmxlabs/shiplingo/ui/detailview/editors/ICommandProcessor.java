/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface ICommandProcessor {
	public final ICommandProcessor EXECUTE = new ICommandProcessor() {
		@Override
		public void processCommand(final Command command, final EObject target,
				final EStructuralFeature feature) {
			command.execute();
		}
	};

	public void processCommand(final Command command, final EObject target,
			final EStructuralFeature feature);
}
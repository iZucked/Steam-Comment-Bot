/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Command Handler to revert {@link ScenarioInstance} to last saved state.
 * 
 * @author Simon Goodall
 * 
 */
public class RevertScenarioCommandHandler extends AbstractHandler {

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				final Object element = iterator.next();
				if (element instanceof ScenarioInstance) {
					final ScenarioInstance model = (ScenarioInstance) element;
					final Map<Class<?>, Object> adapters = model.getAdapters();
					if (adapters != null) {
						final BasicCommandStack stack = (BasicCommandStack) adapters.get(BasicCommandStack.class);
						while (stack.isSaveNeeded() && stack.canUndo()) {
							stack.undo();
						}

					}
				}
			}
		}

		return null;
	}

	@Override
	public void setEnabled(final Object evaluationContext) {
		boolean enabled = false;
		if (evaluationContext instanceof IEvaluationContext) {
			final IEvaluationContext context = (IEvaluationContext) evaluationContext;
			final Object defaultVariable = context.getDefaultVariable();

			if (defaultVariable instanceof List<?>) {
				final List<?> variables = (List<?>) defaultVariable;

				for (final Object var : variables) {
					if (var instanceof ScenarioInstance) {
						final ScenarioInstance model = (ScenarioInstance) var;
						final Map<Class<?>, Object> adapters = model.getAdapters();
						if (adapters != null) {
							final BasicCommandStack stack = (BasicCommandStack) adapters.get(BasicCommandStack.class);
							enabled = stack.isSaveNeeded() && stack.canUndo();
						} else {
							super.setBaseEnabled(false);
							return;
						}
					} else {
						super.setBaseEnabled(false);
						return;
					}
				}
			}
		}

		super.setBaseEnabled(enabled);
	}
}

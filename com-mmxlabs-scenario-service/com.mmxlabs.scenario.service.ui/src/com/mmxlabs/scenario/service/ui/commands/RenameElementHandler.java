package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Command Handler to present an {@link InputDialog} to the user to rename a {@link Folder} or {@link ScenarioInstance}.
 * 
 * @author Simon Goodall
 * 
 */
public class RenameElementHandler extends AbstractHandler {

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
					final ScenarioInstance instance = (ScenarioInstance) element;

					final String newName = getNewName(instance.getName());
					if (newName != null) {
						instance.setName(newName);
					}
				} else if (element instanceof Folder) {
					final Folder folder = (Folder) element;
					final String newName = getNewName(folder.getName());
					if (newName != null) {
						folder.setName(newName);
					}
				}
			}
		}

		return null;
	}

	private String getNewName(final String oldName) {
		// TODO: Hook in an element specific validator
		final IInputValidator validator = null;
		final InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(), "Rename element", "Choose new element name", oldName, validator);

		if (dialog.open() == InputDialog.OK) {
			return dialog.getValue();
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
					if (var instanceof ScenarioInstance || var instanceof Folder) {
						enabled = true;
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

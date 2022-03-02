package com.mmxlabs.lingo.app.updater.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.app.updater.LiNGOUpdater;

public class UpdateLiNGOCommandHandler extends AbstractHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateLiNGOCommandHandler.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final LiNGOUpdater u = new LiNGOUpdater();

		Display.getDefault().asyncExec(() -> {
			try {
				u.updateWithDialog(null);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
				Display.getDefault().asyncExec(() -> MessageDialog.openError(Display.getDefault().getActiveShell(), "Error updating", e.getMessage()));
			}
		});

		return null;
	}
}

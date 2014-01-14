package com.mmxlabs.models.ui.editors.dialogs;

import com.mmxlabs.models.ui.editors.ICommandHandler;

public interface IDialogController {

	/**
	 * Request validation to be performed. Normally the dialog {@link ICommandHandler} would trigger this automatically on data changes. This method exists for triggering validation from code which is
	 * unable to use the dialog command handler.
	 */
	void validate();
}

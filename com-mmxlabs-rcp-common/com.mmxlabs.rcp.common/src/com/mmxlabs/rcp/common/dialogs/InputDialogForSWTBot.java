package com.mmxlabs.rcp.common.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Subclass of the standard {@link InputDialog} with an ID field for the text
 * field for use with SWT Bot
 * 
 * @author Simon Goodall
 *
 */
public class InputDialogForSWTBot extends InputDialog {

	private static final String ORG_ECLIPSE_SWTBOT_WIDGET_KEY = "org.eclipse.swtbot.widget.key";

	private final String textSWTBotId;

	public InputDialogForSWTBot(final Shell parentShell, final String dialogTitle, final String dialogMessage, final String initialValue, final IInputValidator validator, final String textSWTBotId) {
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
		this.textSWTBotId = textSWTBotId;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

		final Control c = super.createDialogArea(parent);

		final Text t = getText();
		if (t != null) {
			t.setData(ORG_ECLIPSE_SWTBOT_WIDGET_KEY, textSWTBotId);
		}
		return c;

	}

}

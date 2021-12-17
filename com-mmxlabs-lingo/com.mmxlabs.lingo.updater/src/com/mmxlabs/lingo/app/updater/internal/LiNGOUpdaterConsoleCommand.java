/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater.internal;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.app.updater.LiNGOUpdater;

public class LiNGOUpdaterConsoleCommand implements CommandProvider {

	public static final String COMMAND = "command";

	public void _updateapp(CommandInterpreter ci) throws Exception {

		String source = null;
		String user = null;
		String pw = null;

		String arg = null;
		while ((arg = ci.nextArgument()) != null) {
			if (source == null) {
				source = arg;
			} else if (user == null) {
				user = arg;
			} else if (pw == null) {
				pw = arg;
			}
		}

		LiNGOUpdater u = new LiNGOUpdater();
		u.setAuth(user, pw);

		final String s = source;
		Display.getDefault().asyncExec(() -> {
			try {
				u.updateWithDialog(s);
			} catch (Exception e) {
				e.printStackTrace();
				
				Display.getDefault().asyncExec(() -> {
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error updating", e.getMessage());

				});
			}
		});

	}

	@Override
	public String getHelp() {
//		StringBuilder buffer = new StringBuilder();
//		buffer.append("updateapp <url> <user> <pass> - Update with optional URL and basic auth credentials\n");
		return null;// buffer.toString();
	}

}

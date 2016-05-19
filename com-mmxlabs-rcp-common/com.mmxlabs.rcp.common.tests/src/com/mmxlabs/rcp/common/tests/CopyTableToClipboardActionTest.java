/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;

@RunWith(SWTBotJunit4ClassRunner.class)
public class CopyTableToClipboardActionTest {

	@Ignore("Almost always fails on build server, probably timing issue")
	@Test
	public void testCopyTableToClipboardAction() throws InterruptedException {

		// Get bot ref to workbench
		final SWTWorkbenchBot bot = new SWTWorkbenchBot();
		// Get shell
		final Shell shell = bot.activeShell().widget;

		bot.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {

				final StringBuilder expectedStringBuilder = new StringBuilder();
				shell.setLayout(new FillLayout());
				final Table table = new Table(shell, SWT.BORDER);
				// Generate TableItems and the expected output string
				for (int i = 0; i < 4; i++) {
					final TableItem iItem = new TableItem(table, 0);
					final String txt = "TableItem (0) -" + i;
					iItem.setText(txt);
					expectedStringBuilder.append(txt);
					expectedStringBuilder.append("\n");
				}

				// Open shell
				shell.setSize(200, 200);
				shell.open();

				// Create and run the copy action
				final CopyTableToClipboardAction a = new CopyTableToClipboardAction(table);
				a.run();
				// Grab clipboard contents
				final Clipboard cb = new Clipboard(bot.getDisplay());
				try {
					final TextTransfer transfer = TextTransfer.getInstance();
					final Object contents = cb.getContents(transfer);
					// Expect a String
					Assert.assertTrue(contents instanceof String);

					// Check string is as expected
					Assert.assertEquals(expectedStringBuilder.toString(), contents);
				} finally {

					cb.dispose();
				}
			}
		});
	}
}

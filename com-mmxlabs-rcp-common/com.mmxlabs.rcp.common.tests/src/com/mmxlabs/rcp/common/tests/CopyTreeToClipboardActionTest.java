/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit5.SWTBotJunit5Extension;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;

@ExtendWith(SWTBotJunit5Extension.class) 
public class CopyTreeToClipboardActionTest {

	@Disabled("Almost always fails on build server, probably timing issue")
	@Test
	public void testCopyTreeToClipboardAction() throws InterruptedException {

		SWTUtils.waitForDisplayToAppear();

		// Get bot ref to workbench
		final SWTWorkbenchBot bot = new SWTWorkbenchBot();

		// Get shell
		final Shell shell = bot.activeShell().widget;

		bot.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {

				final StringBuilder expectedStringBuilder = new StringBuilder();
				shell.setLayout(new FillLayout());
				final Tree tree = new Tree(shell, SWT.BORDER);

				// Generate TreeItems and the expected output string
				for (int i = 0; i < 4; i++) {
					final TreeItem iItem = new TreeItem(tree, 0);
					final String txt = "TreeItem (0) -" + i;
					iItem.setText(txt);
					expectedStringBuilder.append(txt);
					expectedStringBuilder.append("\n");
					for (int j = 0; j < 4; j++) {
						final TreeItem jItem = new TreeItem(iItem, 0);
						final String txt2 = "TreeItem (1) -" + j;
						jItem.setText(txt2);
						expectedStringBuilder.append(txt2);
						expectedStringBuilder.append("\n");
					}
				}

				// Open shell
				shell.setSize(200, 200);
				shell.open();

				
				try {
					Thread.sleep(5_000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Create and run the copy action
				final CopyTreeToClipboardAction a = new CopyTreeToClipboardAction(tree);
				a.run();

				// Grab clipboard contents
				final Clipboard cb = new Clipboard(bot.getDisplay());
				try {
					final TextTransfer transfer = TextTransfer.getInstance();
					final Object contents = cb.getContents(transfer);

					// Expect a String
					Assertions.assertTrue(contents instanceof String);

					// Check string is as expected
					Assertions.assertEquals(expectedStringBuilder.toString(), contents);
				} finally {

					cb.dispose();
				}
			}
		});
	}
}

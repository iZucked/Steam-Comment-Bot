/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.tests;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;

@RunWith(SWTBotJunit4ClassRunner.class)
public class CopyTreeToClipboardActionTest {
	private final CyclicBarrier swtBarrier = new CyclicBarrier(2);
	protected static Shell shell;
	protected static SWTBot bot;
	protected static Thread uiThread;
	protected static Display display;

	@Test
	public void testCopyTreeToClipboardAction() throws InterruptedException {

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

				// Create and run the copy action
				final CopyTreeToClipboardAction a = new CopyTreeToClipboardAction(tree);
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

	@Before
	public void createDisplay() throws InterruptedException, BrokenBarrierException {

		uiThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create or reuse existing display
				display = Display.getDefault();
				shell = new Shell(display);
				shell.setText("Testing: " + getClass().getName());
				shell.setLayout(new FillLayout());

				bot = new SWTBot(shell);
				shell.open();

				// Pause until we near the exit of createDisplay method
				try {
					swtBarrier.await();
				} catch (final Exception e) {
					e.printStackTrace();
				}

				try {
					while (!display.isDisposed() && !shell.isDisposed()) {
						try {
							if (!display.readAndDispatch()) {
								display.sleep();
							}
						} catch (Throwable t) {

						}
					}
				} finally {
					// Clean up display
					if (!display.isDisposed()) {
						display.dispose();
					}
				}
			}
		}, "UI Thead");
		// Make daemon so that JVM can exit while thread is still running.
		uiThread.setDaemon(true);
		uiThread.start();

		// Wait for display to be open before exiting this method
		swtBarrier.await();

	}

	@After
	public void disposeDisplay() throws InterruptedException {
		// Ensure display is disposed
		if (display != null && !display.isDisposed()) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					if (display != null && !display.isDisposed()) {
						display.dispose();
					}
				}
			});
		}
		// Wait for UI thread to finish
		uiThread.join();
		// Reset variables
		uiThread = null;
		shell = null;
		display = null;
	}
}

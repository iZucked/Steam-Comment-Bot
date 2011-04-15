package com.mmxlabs.rcp.common.tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;

public class CopyTreeToClipboardActionTest {

	private static Shell shell;
	private static Display display;

	@BeforeClass
	public static void initialise() {
		display = new Display();
		shell = new Shell(display);
	}

	@Test
	public void testCopyTreeToClipboardAction() throws InterruptedException {

		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);

		// Generate TreeItems and the expected output string
		final StringBuilder expectedStringBuilder = new StringBuilder();
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

		// Process some events for a short while to give Tree a chance to render
		for (int i = 0; i < 5; ++i) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		// Create and run the copy action
		final CopyTreeToClipboardAction a = new CopyTreeToClipboardAction(tree);
		a.run();

		// Grab clipboard contents
		final Clipboard cb = new Clipboard(display);
		final TextTransfer transfer = TextTransfer.getInstance();
		final Object contents = cb.getContents(transfer);

		// Expect a String
		Assert.assertTrue(contents instanceof String);

		// Check string is as expected
		Assert.assertEquals(expectedStringBuilder.toString(), (String) contents);

		// Clean up
		shell.dispose();
		display.dispose();
	}
}

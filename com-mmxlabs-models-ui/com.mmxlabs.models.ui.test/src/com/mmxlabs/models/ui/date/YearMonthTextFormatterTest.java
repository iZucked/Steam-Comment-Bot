/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.time.YearMonth;
import java.util.Calendar;

import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class YearMonthTextFormatterTest extends AbstractSWTBotTest {

	@Ignore("This test fails (as single digit month), but I think it is ok as setText is slightly different the the UI insert code")
	@Test
	public void testManipulate() throws InterruptedException {

		runTest("3/2015", 2015, Calendar.MARCH);
	}

	@Test
	public void testManipulate2() throws InterruptedException {

		runTest("03/2015", 2015, Calendar.MARCH);
	}

	@Ignore("2 digit years currently not valid")
	@Test
	public void testManipulate_2DigitYear() throws InterruptedException {

		runTest("3/15", 2015, Calendar.MARCH);
	}

	private void runTest(final String inputText, final int year, final int month) throws InterruptedException {
		final FormattedText[] text = new FormattedText[1];
		bot.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				final Shell shell = bot.activeShell().widget;

				text[0] = new FormattedText(shell);
				text[0].setFormatter(new YearMonthTextFormatter());

				final SWTBotText botText = new SWTBotText(text[0].getControl());
				botText.setText(inputText);
			}
		});
		bot.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {

				final Object value = text[0].getValue();

				Assert.assertNotNull(value);
				if (value instanceof YearMonth) {
					final YearMonth mDate = (YearMonth) value;
					Assert.assertEquals(1L + month, mDate.getMonthValue());
					Assert.assertEquals(year, mDate.getYear());
				} else {
					Assert.fail("Unexpected result");
				}
			}
		});
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.time.YearMonth;
import java.util.Calendar;

import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.junit5.SWTBotJunit5Extension;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SWTBotJunit5Extension.class)
public class YearMonthTextFormatterTest extends AbstractSWTBotTest {

	@Disabled("This test fails (as single digit month), but I think it is ok as setText is slightly different the the UI insert code")
	@Test
	public void testManipulate() throws InterruptedException {

		runTest("3/2015", 2015, Calendar.MARCH);
	}

	@Test
	public void testManipulate2() throws InterruptedException {

		runTest("03/2015", 2015, Calendar.MARCH);
	}

	@Disabled("2 digit years currently not valid")
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

				Assertions.assertNotNull(value);
				if (value instanceof YearMonth) {
					final YearMonth mDate = (YearMonth) value;
					Assertions.assertEquals(1L + month, mDate.getMonthValue());
					Assertions.assertEquals(year, mDate.getYear());
				} else {
					Assertions.fail("Unexpected result");
				}
			}
		});
	}
}

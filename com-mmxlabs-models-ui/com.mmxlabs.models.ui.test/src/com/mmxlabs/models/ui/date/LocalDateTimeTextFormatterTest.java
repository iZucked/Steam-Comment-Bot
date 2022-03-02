/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.time.LocalDateTime;
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
public class LocalDateTimeTextFormatterTest extends AbstractSWTBotTest {

	@Test
	public void testManipulate() throws InterruptedException {

		runTest("24/03/2015 12:00", 2015, Calendar.MARCH, 24, 12);
	}

	@Test
	public void testManipulate2() throws InterruptedException {

		runTest("04/03/2015 01:00", 2015, Calendar.MARCH, 4, 1);
	}

	@Disabled("2 digit years currently not valid")
	@Test
	public void testManipulate_2DigitYear() throws InterruptedException {

		runTest("24/03/15 12:00", 2015, Calendar.MARCH, 24, 12);
	}

	private void runTest(final String inputText, final int year, final int month, final int day, final int hourOfDay) throws InterruptedException {
		final FormattedText[] text = new FormattedText[1];
		bot.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				final Shell shell = bot.activeShell().widget;

				text[0] = new FormattedText(shell);
				text[0].setFormatter(new LocalDateTimeTextFormatter());

				final SWTBotText botText = new SWTBotText(text[0].getControl());
				botText.setText(inputText);
			}
		});
		// Break the process to allow UI thread to catch up
		bot.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {

				final Object value = text[0].getValue();

				Assertions.assertNotNull(value);
				if (value instanceof LocalDateTime) {
					final LocalDateTime mDate = (LocalDateTime) value;
					Assertions.assertEquals(hourOfDay, mDate.getHour());
					Assertions.assertEquals(day, mDate.getDayOfMonth());
					Assertions.assertEquals(1L + month, mDate.getMonthValue());
					Assertions.assertEquals(year, mDate.getYear());
				} else {
					Assertions.fail("Unexpected result");
				}
			}
		});
	}
}

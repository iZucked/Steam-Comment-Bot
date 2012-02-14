package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class NumberVerifyListener implements VerifyListener {
	private final EDataType type;
	/**
	 * Locale equivalent of period character
	 */
	final char period = DecimalFormatSymbols.getInstance().getDecimalSeparator();

	public NumberVerifyListener(final EDataType type) {
		this.type = type;
	}

	@Override
	public void verifyText(final VerifyEvent e) {

		final String newText = e.text;

		// When the control calls setText, no char will be set, but the text string will be
		if (!((e.keyCode == 0) && (e.keyLocation == 0) && (e.character == 0))) {
			// Stage 1: Verify Key presses
			if ((e.character >= '0') && (e.character <= '9')) {
				e.doit = true;
			} else if (e.character == period) {
				e.doit = true;
				// Handle Backspace
			} else if (e.character == '\b') {
				e.doit = true;
				// Always allow it
				return;
			} else {
				e.doit = false;
			}
		}

		// Step 2. parse number.
		if (e.doit) {

			// Check number of dp is valid
			final int idx = newText.indexOf(period);
			if (idx != -1) {
				final int digits = NumberTypes.getDigits(type);
				if (digits == 0) {
					e.doit = false;
					return;
				}

				// Handle .xxx case
				if (idx == 0) {
					if ((newText.length() - 1) > digits) {
						e.doit = false;
						return;
					}
					// Handle xxx. case
				} else if (idx == (newText.length() - 1)) {
					// Ok
				} else {
					final String[] nums = newText.split(period == '.' ? "\\." : "" + period);

					if (nums.length > 2) {
						// Too many periods!
						e.doit = false;
						return;
					}

					if (nums[1].length() > digits) {
						e.doit = false;
						return;
					}
				}
			}
			try {
				// Finally attempt to parse the number
				final Number number = NumberTypes.toNumber(type, newText);
				final long max = NumberTypes.getMaximum(type);
				if (number.longValue() > max) {
					e.doit = false;
				}

			} catch (final ParseException e1) {
				e.doit = false;
				throw new RuntimeException(e1);
			}
		}
	}
}
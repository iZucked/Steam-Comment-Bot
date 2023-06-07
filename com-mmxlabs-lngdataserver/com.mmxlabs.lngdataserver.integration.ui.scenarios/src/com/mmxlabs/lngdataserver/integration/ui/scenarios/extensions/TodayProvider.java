package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A class intended for use in Hub reports (although could easily be useful elsewhere) where the value of today is important. We need a mechanism for the ITS to have a consistent value for today when
 * re-running the test in the future. It is important to reset this value once the test is complete. E.g.
 * 
 * <pre>
 * try { TodayProvider.getInstance().setOverrride(LocalDate.of(....)); .. run test... } finally { TodayProvider.getInstance().unsetOverrride(); }
 * 
 * 
 * 
 * @author Simon Goodall
 *
 */
public final class TodayProvider {

	public static TodayProvider getInstance() {
		return INSTANCE;
	}

	private static final TodayProvider INSTANCE = new TodayProvider();

	private @Nullable LocalDateTime overrride;

	private TodayProvider() {

	}

	public void setOverrride(final LocalDateTime overrride) {
		this.overrride = overrride;
	}

	public void unsetOverrride() {
		this.overrride = null;
	}

	public @NonNull LocalDateTime getToday() {
		final LocalDateTime pOverride = overrride;
		if (pOverride != null) {
			return pOverride;
		}
		return LocalDateTime.now();
	}
}

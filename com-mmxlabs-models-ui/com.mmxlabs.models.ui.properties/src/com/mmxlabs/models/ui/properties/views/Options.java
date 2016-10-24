package com.mmxlabs.models.ui.properties.views;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class Options {
	public final @NonNull String category;
	public final String helpContextId;

	public final boolean showUnitsInColumn;

	public Options(@NonNull final String category) {
		this(category, null, true);
	}

	public Options(@NonNull final String category, @Nullable final String helpContextId, final boolean showUnitsInSeparateColumn) {
		this.category = category;
		this.helpContextId = helpContextId;
		showUnitsInColumn = showUnitsInSeparateColumn;
	}

}
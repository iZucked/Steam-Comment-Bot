package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioService;

public final class SharedWorkspacePathUtils {
	private SharedWorkspacePathUtils() {

	}

	public static @NonNull String getPathFor(final @NonNull Container parent, final @NonNull String child) {
		final StringBuilder sb = new StringBuilder(getPathFor(parent));
		if (sb.length() > 0) {
			sb.append("/");
		}
		try {
			sb.append(URLEncoder.encode(child, "UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			// Java doc says to use UTF-8, so not expecting this to be thrown.
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	public static @NonNull String getPathFor(final @NonNull Container container) {
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		Container c = container;
		while (c != null && !(c instanceof ScenarioService)) {
			if (!first) {
				sb.insert(0, "/");
			}

			try {
				sb.insert(0, URLEncoder.encode(c.getName(), "UTF-8"));
			} catch (final UnsupportedEncodingException e) {
				// Java doc says to use UTF-8, so not expecting this to be thrown.
				throw new RuntimeException(e);
			}
			c = c.getParent();
			first = false;
		}

		return sb.toString();
	}

	public static String[] getSegments(final @NonNull String path) {
		final String[] segments = path.split("/");

		for (int i = 0; i < segments.length; ++i) {
			try {
				segments[i] = URLDecoder.decode(segments[i], "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				// Java doc says to use UTF-8, so not expecting this to be thrown.
				throw new RuntimeException(e);
			}
		}

		return segments;
	}

	public static String compileSegments(final @NonNull String[] segments) {
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < segments.length; ++i) {
			if (i > 0) {
				sb.append("/");
			}
			try {
				sb.append(URLEncoder.encode(segments[i], "UTF-8"));
			} catch (final UnsupportedEncodingException e) {
				// Java doc says to use UTF-8, so not expecting this to be thrown.
				throw new RuntimeException(e);
			}
		}

		return sb.toString();
	}

	public static String getLastName(final @NonNull String path) {
		final String[] segments = getSegments(path);
		return segments[segments.length - 1];
	}

	public static String addChildSegment(final @NonNull String basePath, final @NonNull String name) {
		try {
			return basePath + "/" + URLEncoder.encode(name, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			// Java doc says to use UTF-8, so not expecting this to be thrown.
			throw new RuntimeException(e);
		}
	}
}

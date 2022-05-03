/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.icons.lingo.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.osgi.service.url.AbstractURLStreamHandlerService;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * A URI handler to manage icon:/ paths in e.g. plugin.xml. This takes a URI
 * such as "icons:/Play16?enabled" to map to the underlying resource.
 * 
 * @author Simon Goodall
 *
 */
public class IconsURLHandler extends AbstractURLStreamHandlerService {

	@Override
	public URLConnection openConnection(final URL u) throws IOException {

		// Determine the mode. This is an optional part of the URI. Assume enabled by
		// default

		final String modeStr = u.getQuery();
		IconMode mode = IconMode.Enabled;
		if (modeStr != null) {
			for (final IconMode m : IconMode.values()) {
				if (m.name().equalsIgnoreCase(modeStr)) {
					mode = m;
					break;
				}
			}
		}

		// Explicit path mapping
		if (u.getPath().startsWith("/icons")) {
			return new URL("platform:/plugin/com.mmxlabs.rcp.icons.lingo" + u.getPath()).openConnection();
		}

		// Assume a leading "/"
		String pathStr = u.getPath().substring(1);

		IconPaths path = null;

		String sizeStr = null;
		final int sizeSeparator = pathStr.indexOf("/");
		if (sizeSeparator > 0) {
			sizeStr = pathStr.substring(0, sizeSeparator);
			pathStr = pathStr.substring(sizeSeparator + 1);
		}

		// This allows a case insensitive match. ::valueOf is case-sensitive
		final int sz = sizeStr == null ? 0 : Integer.parseInt(sizeStr);
		for (final IconPaths p : IconPaths.values()) {
			if (p.match(sz, pathStr)) {
				path = p;
				break;
			}
		}

		if (path == null) {
			throw new IOException("Icon not defined " + pathStr);
		}

		final ImageDescriptor imageDescriptor = CommonImages.getImageDescriptor(path, mode);

		return new URLConnection(u) {

			@Override
			public void connect() throws IOException {

			}

			@Override
			public InputStream getInputStream() throws IOException {
				final ImageLoader loader = new ImageLoader();
				loader.data = new ImageData[] { imageDescriptor.getImageData(100) };
				final byte[] data;
				try (ByteArrayOutputStream boas = new ByteArrayOutputStream()) {
					loader.save(boas, SWT.IMAGE_PNG);
					data = boas.toByteArray();
				}
				return new ByteArrayInputStream(data);
			}
		};
	}
}

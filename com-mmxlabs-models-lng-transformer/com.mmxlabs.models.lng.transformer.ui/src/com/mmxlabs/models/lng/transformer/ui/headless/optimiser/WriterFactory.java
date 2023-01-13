/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class WriterFactory {
	public static PrintWriter getWriter(Path path) {
		return getWriter(path.toString());
	}

	public static PrintWriter getWriter(final String filename) {
		PrintWriter out = null;
		try {
			// clear file
			final FileWriter fw = new FileWriter(filename, false);
			fw.close();
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		} catch (final IOException e) {
			System.out.println(e);
		}
		return out;
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.file.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.io.ByteStreams;

public class FileScenarioServiceBackup {

	public void backup(final File target, final File source) throws IOException {

		FileOutputStream dest = null;
		ZipOutputStream out = null;

		try {
			dest = new FileOutputStream(target);
			out = new ZipOutputStream(new BufferedOutputStream(dest));

			final ZipOutputStream finalOut = out;

			Files.walkFileTree(source.toPath(), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

					// Skip the target zip file
					if (file.equals(target.toPath())) {
						return super.visitFile(file, attrs);
					}
					// Only include .xmi files
					if (file.toString().endsWith(".xmi") == false) {
						return super.visitFile(file, attrs);
					}

					String entryName = source.toPath().relativize(file).toString();
					final ZipEntry entry = new ZipEntry(entryName);
					finalOut.putNextEntry(entry);

					FileInputStream fis = null;
					BufferedInputStream bis = null;
					try {
						fis = new FileInputStream(file.toFile());
						bis = new BufferedInputStream(fis);
						ByteStreams.copy(bis, finalOut);

					} finally {
						if (bis != null) {
							bis.close();
						}
						if (fis != null) {
							fis.close();
						}
						finalOut.flush();
					}
					return super.visitFile(file, attrs);
				}
			});

		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			if (dest != null) {
				dest.close();
			}
		}
	}
}

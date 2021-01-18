/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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

		try (final FileOutputStream dest = new FileOutputStream(target)) {
			try (final ZipOutputStream finalOut = new ZipOutputStream(new BufferedOutputStream(dest))) {

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
						try {
							try (FileInputStream fis = new FileInputStream(file.toFile())) {
								try (BufferedInputStream bis = new BufferedInputStream(fis)) {
									ByteStreams.copy(bis, finalOut);
								}
							}
						} finally {
							finalOut.flush();
						}
						return super.visitFile(file, attrs);
					}
				});
			}
		}
	}
}

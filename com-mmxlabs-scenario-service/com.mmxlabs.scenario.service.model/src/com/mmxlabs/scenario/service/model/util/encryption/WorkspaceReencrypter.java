/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;

public class WorkspaceReencrypter {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceReencrypter.class);

	private static final String PROBLEM_OCCURRED_MESSAGE = "One or more problems were detected during re-encryption process. Please check the error log.";

	/**
	 * Separator used between the hexadecimal digits when converting a byte array to a string.
	 */
	private static final String KEY_DIGIT_SEPARATOR = ":";

	private static final String STATE_FILE = "workspace.state";

	enum Type {
		SCENARIO, DATA;
	}

	/**
	 * File visitor to find candidate files in a sub-tree to re-encrypt
	 *
	 */
	private static class FileVisitor extends SimpleFileVisitor<Path> {
		private final List<Pair<Type, File>> files;
		private final Type type;
		private final List<String> endsWithFilters;

		public FileVisitor(final List<Pair<Type, File>> files, final Type type, final List<String> endsWithFilters) {
			this.files = files;
			this.type = type;
			this.endsWithFilters = endsWithFilters;
		}

		public FileVisitor(final List<Pair<Type, File>> files, final Type type, String... endsWithFilters) {
			this(files, type, arrayToList(endsWithFilters));
		}

		@Override
		public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
			if (endsWithFilters.isEmpty()) {
				// No Filter
				files.add(Pair.of(type, file.toFile()));
			} else {
				final String string = file.toFile().toString();
				for (final String endWith : endsWithFilters) {
					if (string.endsWith(endWith)) {
						files.add(Pair.of(type, file.toFile()));
						break;
					}
				}
			}

			return FileVisitResult.CONTINUE;
		}
	}

	private List<Triple<String, Type, String[]>> searchPaths = new LinkedList<>();
	private List<Pair<String, Type>> searchFiles = new LinkedList<>();

	public void addEncryptionPath(String path, Type type, String... endsWithFilters) {
		searchPaths.add(Triple.of(path, type, endsWithFilters));
	}

	public void addEncryptionFile(String path, Type type) {
		searchFiles.add(Pair.of(path, type));
	}

	public void addDefaultPaths() {
		addEncryptionPath("scenarios", Type.SCENARIO, ".lingo");
		addEncryptionPath("scenario-service", Type.SCENARIO, ".lingo", ".lingo.backup");
		addEncryptionPath("refdata", Type.DATA, ".data");
		addEncryptionFile("anonyMap.data", Type.DATA);
	}

	public void migrateWorkspaceEncryption(final File workspace, final DelegatingEMFCipher cipher, final IProgressMonitor progressMonitor) throws Exception {
		final List<Pair<Type, File>> migratable = new LinkedList<>();

		for (Triple<String, Type, String[]> record : searchPaths) {
			Path path = Paths.get(workspace.getAbsolutePath(), record.getFirst());
			if (path.toFile().exists()) {
				Files.walkFileTree(path, new FileVisitor(migratable, record.getSecond(), record.getThird()));
			}
		}

		for (Pair<String, Type> p : searchFiles) {
			File f = Paths.get(workspace.getAbsolutePath(), p.getFirst()).toFile();
			if (f.exists()) {
				migratable.add(Pair.of(p.getSecond(), f));
			}
		}

		final IRunnableWithProgress r = monitor -> {

			monitor.beginTask("Re-encrypting data", migratable.size());
			boolean[] errorFound = new boolean[] { false };
			try {
				migratable.forEach(m -> {
					try {
						monitor.subTask(m.getSecond().getName());
						migrate(m.getSecond(), m.getFirst(), cipher);
					} catch (final Exception e) {
						errorFound[0] = true;
						LOGGER.error("Problem re-encrypting file " + m.getSecond().getName() + ". Retained original file. " + e.getMessage(), e);
					}
					monitor.worked(1);
				});
			} finally {
				monitor.done();
				if (errorFound[0]) {
					throw new RuntimeException(PROBLEM_OCCURRED_MESSAGE);
				}
			}
		};

		r.run(progressMonitor);
	}

	public void migrateWorkspaceEncryption(final Shell shell, final DelegatingEMFCipher cipher) throws Exception {

		// Is re-encryption enabled?
		if (!DataStreamReencrypter.ENABLED) {
			return;
		}

		// No cipher impl?
		if (cipher == null) {
			return;
		}
		// No default key?
		if (cipher.getDefaultKey() == null) {
			return;
		}

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		final File file = new File(workspaceLocation.toFile().getAbsolutePath() + File.separator + STATE_FILE);
		if (file.exists()) {
			final StateRecord current = readPropertiesFile(file);
			if (current != null && Objects.equals(current.keyid, KeyFileUtil.byteToString(cipher.getDefaultKey(), KEY_DIGIT_SEPARATOR))) {
				// Same key, no need to go forward
				return;
			}
		} else {
			// 2020-11-20: This assumption works for now, but once we start allowing keys to be generated and revoked, we may want to reconsider this check.
			if (cipher.listKeys().size() == 1) {
				// Single key file, lets assume any data is already correctly encrypted.
				writePropertiesFile(file, KeyFileUtil.byteToString(cipher.getDefaultKey(), KEY_DIGIT_SEPARATOR));
				return;
			}
		}
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell) {
			@Override
			protected void configureShell(final Shell shell) {
				super.configureShell(shell);
				shell.setText("Encryption key change detected");
			}
		};

		final boolean[] success = new boolean[1];
		dialog.run(true, true, monitor -> {
			try {
				migrateWorkspaceEncryption(workspaceLocation.toFile(), cipher, monitor);
				success[0] = true;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

		writePropertiesFile(file, KeyFileUtil.byteToString(cipher.getDefaultKey(), KEY_DIGIT_SEPARATOR));
		if (!success[0]) {
			MessageDialog.openError(shell, "Problem during re-encryption", PROBLEM_OCCURRED_MESSAGE);
		}
	}

	public void writeCurrentKeyToStateFile(final DelegatingEMFCipher cipher) throws Exception {
		// No cipher impl?
		if (cipher == null) {
			return;
		}
		// No default key?
		if (cipher.getDefaultKey() == null) {
			return;
		}

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		final File file = new File(workspaceLocation.toFile().getAbsolutePath() + File.separator + STATE_FILE);
		if (!file.exists()) {
			// Single key file, lets assume any data is already correctly encrypted.
			writePropertiesFile(file, KeyFileUtil.byteToString(cipher.getDefaultKey(), KEY_DIGIT_SEPARATOR));
		}
	}

	static class StateRecord {
		public String keyid;

		public StateRecord() {

		}

		public StateRecord(final String keyid) {
			this.keyid = keyid;
		}
	}

	private void writePropertiesFile(final File file, final String keyId) throws IOException {
		final ObjectMapper m = new ObjectMapper();
		m.writerWithDefaultPrettyPrinter().writeValue(file, new StateRecord(keyId));
	}

	private StateRecord readPropertiesFile(final File file) throws IOException {
		final ObjectMapper m = new ObjectMapper();
		return m.readValue(file, StateRecord.class);

	}

	private void migrate(final File originalFile, final Type fileType, final DelegatingEMFCipher cipher) throws Exception {

		final File newFile = new File(originalFile.getAbsoluteFile() + ".enc");
		if (newFile.exists()) {
			throw new IllegalStateException("Temp file already exists " + newFile.getAbsolutePath());
		}

		Map<String, String> originalDigests;

		try {
			// Read initial digests
			if (fileType == Type.SCENARIO) {
				try (FileInputStream in = new FileInputStream(originalFile)) {
					originalDigests = new DataStreamReencrypter().getScenarioDigests(cipher, in);
				}
				try (FileInputStream in = new FileInputStream(originalFile)) {
					try (FileOutputStream out = new FileOutputStream(newFile)) {
						DataStreamReencrypter.reencryptScenario(in, out, cipher);
					}
				}
			} else {
				originalDigests = new HashMap<>();
				try (FileInputStream in = new FileInputStream(originalFile)) {
					originalDigests.put(DataStreamReencrypter.GENERIC_DATA_KEY, new DataStreamReencrypter().getDataDigests(cipher, in));
				}

				try (FileInputStream in = new FileInputStream(originalFile)) {
					try (FileOutputStream out = new FileOutputStream(newFile)) {
						DataStreamReencrypter.reencryptData(in, out, cipher);
					}
				}
			}

			Map<String, String> newDigests;

			if (fileType == Type.SCENARIO) {
				try (FileInputStream in = new FileInputStream(newFile)) {
					newDigests = new DataStreamReencrypter().getScenarioDigests(cipher, in);
				}
			} else {
				newDigests = new HashMap<>();
				try (FileInputStream in = new FileInputStream(newFile)) {
					newDigests.put(DataStreamReencrypter.GENERIC_DATA_KEY, new DataStreamReencrypter().getDataDigests(cipher, in));
				}
			}
			if (originalDigests.equals(newDigests)) {
				// Success!

				// Delete original
				Files.delete(originalFile.toPath());

				// Rename new file over original
				Files.move(newFile.toPath(), originalFile.toPath());
			} else {
				// Failure!!
				if (newFile.exists()) {
					Files.delete(newFile.toPath());
				}
				throw new RuntimeException("Data digest check failed.");
			}
		} catch (Exception e) {
			// Failure!!
			if (newFile.exists()) {
				Files.delete(newFile.toPath());
			}
			throw e;
		}
	}

	private static List<String> arrayToList(String... values) {
		List<String> l = new ArrayList<>(values.length);
		for (String s : values) {
			l.add(s);
		}
		return l;
	}

}

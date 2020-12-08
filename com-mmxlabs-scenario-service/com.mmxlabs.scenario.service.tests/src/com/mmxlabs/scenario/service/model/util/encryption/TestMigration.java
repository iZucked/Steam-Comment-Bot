/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import com.google.common.io.Files;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.mmxlabs.scenario.service.model.util.encryption.GenericDataHelper.GDEntry;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioHelper.SSEntry;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;

public class TestMigration {

	private static TestKeyFileUtil keyFiles;

	@BeforeAll
	public static void initialiseData() throws Exception {

		DataStreamReencrypter.ENABLED = true;

		keyFiles = new TestKeyFileUtil();
	}

	@AfterAll
	public static void cleanupData() {

	}

	private File workspace;

	@BeforeEach
	public void initialiseWorkspace() throws Exception {

		workspace = Files.createTempDir();
	}

	@AfterEach
	public void cleanupWorkspace() throws IOException {
		MoreFiles.deleteRecursively(workspace.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
	}

	@RepeatedTest(10)
	public void test() throws Exception {

		List<GDEntry> dataFiles = new LinkedList<>();
		List<SSEntry> scenarioFiles = new LinkedList<>();

		final DelegatingEMFCipher oldCipher = keyFiles.loadOldKeyStore();

		// Stage one, create data folders.
		GenericDataHelper dataHelper = new GenericDataHelper(Paths.get(workspace.getAbsolutePath(), "refdata").toFile());
		for (String type : new String[] { "A", "B" }) {
			dataFiles.add(dataHelper.createRecord(type, "record1.data", 2048, oldCipher));
			dataFiles.add(dataHelper.createRecord(type, "record2.data", 2048 * 2048, oldCipher));
			dataFiles.add(dataHelper.createRecord(type, "record3.data", 2048 * 4, oldCipher));
			dataFiles.add(dataHelper.createRecord(type, "record4.data", 2048 * 16, oldCipher));
		}

		{
			ScenarioHelper scenarioHelper = new ScenarioHelper(Paths.get(workspace.getAbsolutePath(), "scenarios", "basecases").toFile());
			scenarioFiles.add(scenarioHelper.createRecord("basecase.lingo", 2048 * 2048, oldCipher));
		}
		{
			ScenarioHelper scenarioHelper = new ScenarioHelper(Paths.get(workspace.getAbsolutePath(), "scenarios", "team").toFile());
			scenarioFiles.add(scenarioHelper.createRecord("team1.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("team2.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("team3.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("team4.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("team5.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("team6.lingo", 2048 * 2048, oldCipher));
		}
		{
			ScenarioHelper scenarioHelper = new ScenarioHelper(Paths.get(workspace.getAbsolutePath(), "scenario-service").toFile());
			scenarioFiles.add(scenarioHelper.createRecord("workspace1.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("workspace1.lingo.backup", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("workspace2.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("workspace2.lingo.backup", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("workspace3.lingo", 2048 * 2048, oldCipher));
			scenarioFiles.add(scenarioHelper.createRecord("workspace3.lingo.backup", 2048 * 2048, oldCipher));
		}

		final DelegatingEMFCipher newCipher = keyFiles.loadNewKeyStore();

		WorkspaceReencrypter reencrypter = new WorkspaceReencrypter();
		reencrypter.addDefaultPaths();
		reencrypter.migrateWorkspaceEncryption(workspace, newCipher, new NullProgressMonitor());

		dataFiles.forEach(r -> {
			try {
				GenericDataHelper.check(r, newCipher);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		scenarioFiles.forEach(r -> {
			try {
				ScenarioHelper.check(r, newCipher);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

}

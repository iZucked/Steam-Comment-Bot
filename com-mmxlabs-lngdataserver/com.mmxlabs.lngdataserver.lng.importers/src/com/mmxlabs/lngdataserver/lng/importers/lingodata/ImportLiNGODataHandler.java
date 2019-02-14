/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.dnd.ILiNGODataImportHandler;

public class ImportLiNGODataHandler implements ILiNGODataImportHandler {

	@Override
	public boolean importLiNGOData(String filename, @NonNull ScenarioInstance scenarioInstance) {

		LingoDataImporter lingoDataImporter = new LingoDataImporter();

		try {
			lingoDataImporter.importIntoScenario(filename, scenarioInstance);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return false;
	}

}

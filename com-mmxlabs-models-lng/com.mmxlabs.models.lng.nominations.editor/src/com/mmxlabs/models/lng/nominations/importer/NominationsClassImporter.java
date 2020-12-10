/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class NominationsClassImporter extends DefaultClassImporter {
	
	private static final Logger logger = LoggerFactory.getLogger(NominationsClassImporter.class);
	
	private NominationsModel nominationsModel;
	
	public NominationsClassImporter(NominationsModel nominationsModel) {
		this.nominationsModel = nominationsModel;
	}
	
	@Override
	public Collection<EObject> importObjects(@NonNull final EClass targetClass, @NonNull final CSVReader reader, @NonNull final IMMXImportContext context) {
		final List<AbstractNomination> existingNominations = new ArrayList<AbstractNomination>();
		if (nominationsModel != null) {
			existingNominations.addAll(nominationsModel.getNominations());
			
			//We only want to import generated nominations if they have been overridden/changed in some way.
			List<AbstractNomination> generatedNominations = NominationsModelUtils.generateNominationsFromSpecs(nominationsModel);
			existingNominations.addAll(generatedNominations);

		}
		
		final List<EObject> result = new LinkedList<>();

		Map<String, String> row;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow(true))) {
				final ImportResults imported = importObject(null, targetClass, row, context);
				for (EObject obj : imported.getCreatedObjects()) {
					if (obj instanceof AbstractNomination && 
						NominationsModelUtils.containsNomination(existingNominations, (AbstractNomination)obj)) {
						context.addProblem(context.createProblem("Duplicate nomination", true, true, false));
						logger.error("Duplicate nomination found during import: "+obj);
					}
					else {		
						result.add(obj);
					}
				}
			}
		} catch (final IOException e) {
			context.addProblem(context.createProblem("IO Error " + e.getMessage(), true, true, false));
		} finally {
			context.popReader();
		}

		assert result != null;
		return result;
	}
}

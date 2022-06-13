/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV163 extends AbstractMigrationUnit {

	private static final List<String> physicalMarkerVessels = Stream.of("Aamira", "Al Aamriya", "Al Bahiya", "Al Dafna", "Al Gattara", "Al Ghariya", "Al Gharrafa", "Al Ghashamiya", "Al Ghuwairiya",
			"Al Hamla", "Al Huwaila", "Al Karaana", "Al Kharaitiyat", "Al Kharsaah", "Al Khattiya", "Al Khuwair", "Al Mafyar", "Al Mayeda", "Al Nuaman", "Al Oraiq", "Al Rekayyat", "Al Ruwais",
			"Al Sadd", "Al Safliya", "Al Sahla", "Al Samriya", "Al Shamal", "Al Sheehaniya", "Al Thumama", "Al Utouriya", "Bu Samra", "Duhail", "Fraiha", "Kumul", "Lijmiliya", "Maran Gas Leto",
			"Mekaines", "Mesaimeer", "Mozah", "Murwab", "Onaiza", "Pflng Satu", "Rasheeda", "Shagra", "Tembek", "Umm Al Amad", "Umm Slal", "Zarga").collect(Collectors.toList());

	private static final List<String> referenceMarkerVessels = Stream.of("Q-flex", "Q-max", "<Q-flex>", "<Q-max>").collect(Collectors.toList());

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 162;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 163;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		@NonNull
		final EObjectWrapper modelRoot = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");
		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
		final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");

		if (vessels != null) {
			for (final EObjectWrapper vessel : vessels) {
				vessel.setAttrib("marker", Boolean.FALSE);

				if (physicalMarkerVessels.contains(vessel.getAttrib("name")) 
						|| referenceMarkerVessels.contains(vessel.getAttrib("name"))) {
					vessel.setAttrib("marker", Boolean.TRUE);
				}
			}
		}
	}
}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

@NonNullByDefault
public class MigrateToV181 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 180;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 181;
	}

	// Renames CharterLengthEvent to GeneratedCharterLengthEvent
	// Adds CharterLengthEvent to cargo model
	// Make FuelUsage a super type of VesselEventVisit 
	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EPackage schedulePackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EClass clsCharterLength = MetamodelUtils.getEClass(schedulePackage, "CharterLengthEvent");
		final EClass clsGeneratedCharterLength = MetamodelUtils.getEClass(schedulePackage, "GeneratedCharterLengthEvent");
		final EFactory scheduleFactory = schedulePackage.getEFactoryInstance();
		final EClass clsSequence = MetamodelUtils.getEClass(schedulePackage, "Sequence");
		final EClass clsEvent = MetamodelUtils.getEClass(schedulePackage, "Event");
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		
		final TreeIterator<@NonNull EObject> iter = scenarioModel.eAllContents();
		
		final Map<EObjectWrapper, EObjectWrapper> replacements = new HashMap<>();
		while(iter.hasNext()) {
			final EObject next = iter.next();
			if(clsSequence.isInstance(next)) {
				final EObjectWrapper objSequence = (EObjectWrapper)next;
				final List<@NonNull EObjectWrapper> events = objSequence.getRefAsList("events");
				for(int i = 0; i < events.size(); i++) {
					if(clsCharterLength.isInstance(events.get(i))) {
						EObjectWrapper generatedCharterLength = convertToGeneratedCharterLength(scheduleFactory, events.get(i), clsGeneratedCharterLength);
						replacements.put(events.get(i), generatedCharterLength);
						// Copying the attributes/references of the charter length object to the generated charter
						// length object adds the generated charter length to the end of the events list
						events.remove(events.size() - 1);
						events.set(i, generatedCharterLength);
					}
				}
			}
		}

		final Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.UsageCrossReferencer.findAll(replacements.keySet(), scenarioModel);
		for (final var e : crossReferences.entrySet()) {
			final var charterLength = e.getKey();
			final Collection<EStructuralFeature.Setting> usages = e.getValue();
			for (final EStructuralFeature.Setting setting : usages) {
				if (setting.getEStructuralFeature() instanceof final EReference ref && ref.isContainment()) {
					continue;
				}

				// These are all the places we expect a charter length event to be used in the ecore
				// model
				if (clsEvent.isInstance(setting.getEObject()) && setting.getEStructuralFeature().getName().equals("previousEvent")) {
					final EObjectWrapper event = (EObjectWrapper) setting.getEObject();
					event.setRef("previousEvent", replacements.get(charterLength));
				} else if (clsEvent.isInstance(setting.getEObject()) && setting.getEStructuralFeature().getName().equals("nextEvent")) {
					final EObjectWrapper event = (EObjectWrapper) setting.getEObject();
					event.setRef("nextEvent", replacements.get(charterLength));
				} else {
					// !! Here replace in the list e.g. the super type
					final EObjectWrapper owner = (EObjectWrapper) setting.getEObject();
					if (setting.getEStructuralFeature().isMany()) {
						final List l = ((List<?>) owner.eGet(setting.getEStructuralFeature()));
						final int idx = l.indexOf(charterLength);
						l.set(idx, replacements.get(charterLength));

					} else {
						owner.eSet(setting.getEStructuralFeature(), replacements.get(charterLength));
					}
				}
			}
		}
	}

	private EObjectWrapper convertToGeneratedCharterLength(final EFactory sFactory, final EObjectWrapper original, final EClass toClass) {
		final EObjectWrapper generatedCharterLengthEvent = (EObjectWrapper) sFactory.create(toClass);
		for (final EStructuralFeature feature : original.eClass().getEAllStructuralFeatures()) {
			// Skip unset fields
			if (!original.eIsSet(feature)) {
				continue;
			}

			// Copy/move features
			if (feature instanceof final EAttribute attrib) {
				if (attrib.isMany()) {
					generatedCharterLengthEvent.setAttrib(feature.getName(), original.getAttribAsList(feature.getName()));
				} else {
					generatedCharterLengthEvent.setAttrib(feature.getName(), original.getAttrib(feature.getName()));
				}
			} else if (feature instanceof final EReference ref) {
				if (ref.isMany()) {
					generatedCharterLengthEvent.setRef(feature.getName(), original.getRefAsList(feature.getName()));
				} else {
					generatedCharterLengthEvent.setRef(feature.getName(), original.getRef(feature.getName()));
				}
			}
		}
		return generatedCharterLengthEvent;
	}

}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV23 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 22;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 23;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV22Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV23Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EPackage package_CargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EEnum enum_SequenceType = MetamodelUtils.getEEnum(package_ScheduleModel, "SequenceType");
		final EEnumLiteral literal_SequenceType_VESSEL = MetamodelUtils.getEEnum_Literal(enum_SequenceType, "VESSEL");
		final EEnumLiteral literal_SequenceType_SPOT_VESSEl = MetamodelUtils.getEEnum_Literal(enum_SequenceType, "SPOT_VESSEL");

		final EClass class_EventGrouping = MetamodelUtils.getEClass(package_ScheduleModel, "EventGrouping");

		final EObjectWrapper portfolioModel = model.getRef("portfolioModel");
		if (portfolioModel == null) {
			return;
		}
		final EObjectWrapper scheduleModel = portfolioModel.getRef("scheduleModel");
		if (scheduleModel == null) {
			return;
		}
		final EObjectWrapper schedule = scheduleModel.getRef("schedule");
		if (schedule != null) {
			final List<EObjectWrapper> sequences = schedule.getRefAsList("sequences");
			if (sequences != null) {
				for (final EObjectWrapper sequence : sequences) {
					final List<EObjectWrapper> eventsInGroup = new LinkedList<>();

					final Object sequenceType = sequence.getAttrib("sequenceType");
					if (sequenceType == literal_SequenceType_VESSEL | sequenceType == literal_SequenceType_SPOT_VESSEl) {
						final List<EObjectWrapper> events = sequence.getRefAsList("events");
						EObjectWrapper grouping = null;
						for (final EObjectWrapper event : events) {
							// If segment start, place all grouped events into previous grouping object and reset state
							if (isSegmentStart(event, package_ScheduleModel, package_CargoModel)) {
								if (grouping != null) {
									grouping.setRef("events", eventsInGroup);
								}
								eventsInGroup.clear();
								grouping = null;
							}
							// Set a grouping object if needed.
							if (grouping == null && class_EventGrouping.isInstance(event)) {
								grouping = event;
							}
							eventsInGroup.add(event);
						}

					}
				}
			}
		}
	}

	/**
	 * Returns true if the event is the start of a sequence of events (and thus the prior events sequence ends) For example this could the Load up to the end of the voyage before another load.
	 * 
	 * @param event
	 * @return
	 */
	public static boolean isSegmentStart(final EObjectWrapper event, final EPackage schedulePackage, final EPackage cargoPackage) {

		final EClass class_VesselEventVisit = MetamodelUtils.getEClass(schedulePackage, "VesselEventVisit");
		final EClass class_StartEvent = MetamodelUtils.getEClass(schedulePackage, "StartEvent");
		final EClass class_EndEvent = MetamodelUtils.getEClass(schedulePackage, "EndEvent");
		final EClass class_SlotVisit = MetamodelUtils.getEClass(schedulePackage, "SlotVisit");
		final EClass class_LoadSlot = MetamodelUtils.getEClass(cargoPackage, "LoadSlot");
		final EClass class_GeneratedCharterOut = MetamodelUtils.getEClass(schedulePackage, "GeneratedCharterOut");

		if (class_VesselEventVisit.isInstance(event)) {
			return true;
		}
		if (class_GeneratedCharterOut.isInstance(event)) {
			return true;
		}
		if (class_StartEvent.isInstance(event)) {
			return true;
		}
		if (class_EndEvent.isInstance(event)) {
			return true;
		}
		if (class_SlotVisit.isInstance(event)) {

			final EObjectWrapper slotAllocation = event.getRef("slotAllocation");
			if (slotAllocation != null) {
				final EObjectWrapper slot = slotAllocation.getRef("slot");
				if (class_LoadSlot.isInstance(slot)) {
					return true;
				}
			}
		}

		return false;
	}

}

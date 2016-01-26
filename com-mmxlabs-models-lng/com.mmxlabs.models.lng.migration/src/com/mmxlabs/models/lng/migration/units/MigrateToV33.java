/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV33 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 32;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 33;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final Map<EStructuralFeature, EStructuralFeature> timezoneMap = buildTimezoneMap(loader);

		Set<EPackage> validPackages = createValidPackagesSet(loader);

		final TreeIterator<EObject> itr = model.eAllContents();
		while (itr.hasNext()) {
			final EObject eObj = itr.next();
			final EClass eClass = eObj.eClass();
			if (!validPackages.contains(eClass.getEPackage())) {
				// Should be custom code, ignore and let custom code migration kick in.
				continue;
			}
			for (final EStructuralFeature feature : eClass.getEAllAttributes()) {
				if (feature.getEType() == EcorePackage.Literals.EDATE) {
					final Date date = (Date) eObj.eGet(feature);
					if (date != null) {
						final Calendar cal = Calendar.getInstance(getTimeZone(eObj, feature, timezoneMap));
						cal.setTime(date);

						final EStructuralFeature tmpFeature = eClass.getEStructuralFeature(feature.getName() + "Tmp");
						if (tmpFeature != null) {
							Object mDate = null;
							if (tmpFeature.getEType().getInstanceClassName() == "java.time.LocalDate") {
								mDate = LocalDate.of(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
							} else if (tmpFeature.getEType().getInstanceClassName() == "java.time.LocalDateTime") {
								mDate = LocalDateTime.of(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), 0);
							} else if (tmpFeature.getEType().getInstanceClassName() == "java.time.YearMonth") {
								mDate = YearMonth.of(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH));
							} else if (tmpFeature.getEType().getInstanceClassName() == "java.time.ZonedDateTime") {
								mDate = ZonedDateTime.of(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), 0, 0, 0,
										cal.getTimeZone().toZoneId());
							}
							if (mDate != null) {
								eObj.eUnset(feature);
								eObj.eSet(tmpFeature, mDate);
							}
						}
					}
				}
			}
		}
	}

	private Set<EPackage> createValidPackagesSet(MetamodelLoader loader) {
		Set<EPackage> validPackages = new HashSet<>();
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ActualsModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_LNGTypes));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel));
		validPackages.add(loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel));

		return validPackages;
	}

	private Map<EStructuralFeature, EStructuralFeature> buildTimezoneMap(final MetamodelLoader loader) {

		final EPackage package_CargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EPackage package_ActualsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ActualsModel);
		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EClass class_Slot = MetamodelUtils.getEClass(package_CargoModel, "Slot");
		final EClass class_VesselEvent = MetamodelUtils.getEClass(package_CargoModel, "VesselEvent");
		final EClass class_Event = MetamodelUtils.getEClass(package_ScheduleModel, "Event");
		final EClass class_SlotActuals = MetamodelUtils.getEClass(package_ActualsModel, "SlotActuals");
		final EClass class_ReturnActuals = MetamodelUtils.getEClass(package_ActualsModel, "ReturnActuals");

		final Map<EStructuralFeature, EStructuralFeature> map = new HashMap<EStructuralFeature, EStructuralFeature>();
		map.put(class_Slot.getEStructuralFeature("windowStart"), class_Slot.getEStructuralFeature("port"));
		// Already persisted as UTC
		// map.put(class_Slot.getEStructuralFeature("pricingEvent"), class_Slot.getEStructuralFeature("port"));
		map.put(class_VesselEvent.getEStructuralFeature("startBy"), class_VesselEvent.getEStructuralFeature("port"));
		map.put(class_VesselEvent.getEStructuralFeature("startAfter"), class_VesselEvent.getEStructuralFeature("port"));

		map.put(class_Event.getEStructuralFeature("start"), class_Event.getEStructuralFeature("port"));
		map.put(class_Event.getEStructuralFeature("end"), class_Event.getEStructuralFeature("port"));

		map.put(class_SlotActuals.getEStructuralFeature("operationsStart"), class_SlotActuals.getEStructuralFeature("titleTransferPoint"));
		map.put(class_SlotActuals.getEStructuralFeature("operationsEnd"), class_SlotActuals.getEStructuralFeature("titleTransferPoint"));
		map.put(class_ReturnActuals.getEStructuralFeature("operationsStart"), class_ReturnActuals.getEStructuralFeature("titleTransferPoint"));

		// TODO -> Also for F - history line

		return map;
	}

	private TimeZone getTimeZone(final EObject eObj, final EStructuralFeature feature, final Map<EStructuralFeature, EStructuralFeature> timezoneMap) {

		final EStructuralFeature tzFeature = timezoneMap.get(feature);
		if (tzFeature != null) {
			final EObject port = (EObject) eObj.eGet(tzFeature);
			if (port != null) {
				final String tz = (String) port.eGet(port.eClass().getEStructuralFeature("timeZone"));
				if (tz != null && !tz.isEmpty()) {
					return TimeZone.getTimeZone(tz);
				}
			}
		}

		return TimeZone.getTimeZone("UTC");
	}
}

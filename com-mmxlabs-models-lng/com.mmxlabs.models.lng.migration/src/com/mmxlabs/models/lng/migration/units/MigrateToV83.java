/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV83 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 82;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 83;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		final EObjectWrapper portModel = referenceModel.getRef("portModel");
		if (portModel == null) {
			return;
		}
		// Get the ID mapping
		Map<String, String> mappingData = buildMappingData();
		Map<String, String> preferredNameMap = buildPreferredNameData();
		{
			List<EObjectWrapper> ports = portModel.getRefAsList("ports");

			if (ports != null) {

				// Record the first port by ID.
				// Map<String, EObjectWrapper> firstPort = new HashMap<>();

				Set<String> seenIDs = new HashSet<>();
				Set<EObjectWrapper> duplicatePorts = new HashSet<>();
				for (EObjectWrapper port : ports) {
					EObjectWrapper location = port.getRef("location");
					Object name = port.getAttrib("name");
					location.setAttrib("name", name);
					List<String> otherNamesList = port.getAttribAsList("otherNames");
					location.setAttrib("otherNames", otherNamesList);
					port.unsetFeature("otherNames");

					String mmxId = mappingData.get(name);
					if (mmxId == null) {
						if (otherNamesList != null) {
							for (String n : otherNamesList) {
								mmxId = mappingData.get(n);
								if (mmxId != null) {
									break;
								}
							}
						}
					}
					if (mmxId == null) {
						// Some old ITS cases do not distinguish between the niigata's, pick one
						if (name.toString().equalsIgnoreCase("niigata")) {
							mmxId = "L_JP_Niiga";
						}
					}
					//
					// if (mmxId == null) {
					// throw new RuntimeException("No ID for " + name);
					// }
					// assert mmxId != null;
					//
					// if (preferredNameMap.containsKey(mmxId)) {
					// List<String> newNames = new LinkedList<>();
					// if (otherNamesList != null) {
					// newNames.addAll(otherNamesList);
					// }
					// if (!newNames.contains(name)) {
					// newNames.add(0, (String) name);
					// location.setAttrib("otherNames", newNames);
					// }
					// String preferredName = preferredNameMap.get(mmxId);
					// location.setAttrib("name", preferredName);
					// port.setAttrib("name", preferredName);
					// }

					// if (!seenIDs.add(mmxId)) {
					//
					// final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(Collections.singleton(port), model);
					//
					// final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(port);
					// if (usages != null) {
					// for (final EStructuralFeature.Setting setting : usages) {
					// if (setting.getEObject() != portModel) {
					// if (setting.getEStructuralFeature().isMany()) {
					// Collection<?> collection = (Collection<?>) setting.getEObject().eGet(setting.getEStructuralFeature());
					// if (collection.contains(firstPort.get(mmxId))) {
					// // Replacement is already in the collection, so just remove it
					// collection.remove(port);
					// } else {
					// EcoreUtil.replace(setting, port, firstPort.get(mmxId));
					// }
					// } else {
					// EcoreUtil.replace(setting, port, firstPort.get(mmxId));
					// }
					//
					// }
					// }
					// }
					// duplicatePorts.add(port);
					// continue;
					// } else {
					// // Record this port as an original
					// firstPort.put(mmxId, port);
					// }
					if (mmxId != null) {
						location.setAttrib("mmxId", mmxId);
					}

					location.setAttrib("timeZone", port.getAttrib("timeZone"));
					port.unsetFeature("timeZone");

					port.unsetFeature("atobviacCode");
					port.unsetFeature("vesonCode");
					port.unsetFeature("dataloyCode");
					port.unsetFeature("UNLocode");
					port.unsetFeature("externalCode");

				}
				ports.removeAll(duplicatePorts);

			}
		}

		EPackage portPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		EEnum enum_RouteOption = MetamodelUtils.getEEnum(portPackage, "RouteOption");

		List<EObjectWrapper> routes = portModel.getRefAsList("routes");
		if (routes != null) {
			for (EObjectWrapper route : routes) {

				route.unsetFeature("routingOptions");
				route.unsetFeature("canal");
			}
		}
		{
			final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(routes, model);
			for (EObjectWrapper route : routes) {
				final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(route);
				if (usages != null) {
					for (final EStructuralFeature.Setting setting : usages) {
						@NonNull
						String name = setting.getEStructuralFeature().getName();
						if (setting.getEObject() != portModel) {
							EObjectWrapper target = (EObjectWrapper) setting.getEObject();
							if (setting.getEStructuralFeature().isMany()) {

								Collection<EObjectWrapper> collection = (Collection<EObjectWrapper>) setting.getEObject().eGet(setting.getEStructuralFeature());
								List<Object> options = new LinkedList<>();
								for (EObjectWrapper r : collection) {
									options.add(r.getAttrib("routeOption"));
								}
								name = name.substring(0, name.length() - 1);
								target.setAttrib(name + "Options", route.getAttrib("routeOption"));
							} else {
								target.setAttrib(name + "Option", route.getAttrib("routeOption"));
								target.eUnset(setting.getEStructuralFeature());
							}
						}
					}
				}
			}
		}
		{
			EPackage package_PortModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
			EEnum enum_CanalEntry = MetamodelUtils.getEEnum(package_PortModel, "CanalEntry");
			EEnumLiteral enum_CanalEntry_NorthSide = MetamodelUtils.getEEnum_Literal(enum_CanalEntry, "Northside");
			EEnumLiteral enum_CanalEntry_SouthSide = MetamodelUtils.getEEnum_Literal(enum_CanalEntry, "Southside");

			EObjectWrapper cargoModel = model.getRef("cargoModel");
			EObjectWrapper canalBookings = cargoModel.getRef("canalBookings");
			if (canalBookings != null) {
				List<EObjectWrapper> bookings = canalBookings.getRefAsList("canalBookingSlots");
				if (bookings != null) {
					for (EObjectWrapper booking : bookings) {
						EObjectWrapper entryPoint = booking.getRef("entryPoint");
						if (entryPoint != null) {
							if (entryPoint.eContainingFeature().getName().toLowerCase().contains("northentrance")) {
								booking.setAttrib("canalEntrance", enum_CanalEntry_NorthSide);
							} else {
								booking.setAttrib("canalEntrance", enum_CanalEntry_SouthSide);
							}
						}
						booking.unsetFeature("entryPoint");
					}
				}
			}

			EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
			EClass class_Journey = MetamodelUtils.getEClass(package_ScheduleModel, "Journey");
			{
				EObjectWrapper scheduleModel = model.getRef("scheduleModel");
				TreeIterator<EObject> itr = scheduleModel.eAllContents();
				while (itr.hasNext()) {
					EObject obj = itr.next();
					if (class_Journey.isInstance(obj)) {
						EObjectWrapper eObjectWrapper = (EObjectWrapper) obj;
						EObjectWrapper entryPoint = eObjectWrapper.getRef("canalEntry");
						if (entryPoint != null) {
							if (entryPoint.eContainingFeature().getName().toLowerCase().contains("northentrance")) {
								eObjectWrapper.setAttrib("canalEntrance", enum_CanalEntry_NorthSide);
							} else {
								eObjectWrapper.setAttrib("canalEntrance", enum_CanalEntry_SouthSide);
							}
						}
						eObjectWrapper.unsetFeature("canalEntry");
					}
				}
			}
			{
				EObjectWrapper analyticsModel = model.getRef("analyticsModel");
				TreeIterator<EObject> itr = analyticsModel.eAllContents();
				while (itr.hasNext()) {
					EObject obj = itr.next();
					if (class_Journey.isInstance(obj)) {
						EObjectWrapper journey = (EObjectWrapper) obj;
						EObjectWrapper entryPoint = journey.getRef("canalEntry");
						if (entryPoint != null) {
							if (entryPoint.eContainingFeature().getName().toLowerCase().contains("northentrance")) {
								journey.setAttrib("canalEntrance", enum_CanalEntry_NorthSide);
							} else {
								journey.setAttrib("canalEntrance", enum_CanalEntry_SouthSide);
							}
						}
						journey.unsetFeature("canalEntry");
					}
				}
			}
		}

		portModel.setAttrib("portDataVersion", "private-" + EcoreUtil.generateUUID());
		portModel.setAttrib("distanceDataVersion", "private-" + EcoreUtil.generateUUID());

		final EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		pricingModel.setAttrib("marketCurveDataVersion", "private-" + EcoreUtil.generateUUID());

		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
		fleetModel.setAttrib("fleetDataVersion", "private-" + EcoreUtil.generateUUID());
	}

	public static Map<String, String> buildPreferredNameData() {
		Map<String, String> map = new HashMap<>();
		map.put("L_EG_Damie", "Damietta");
		map.put("L_US_LakeC", "Lake Charles");
		map.put("L_US_Elba", "Elba Island");
		return map;

	}

	public static Map<String, String> buildMappingData() {
		Map<String, String> map = new HashMap<>();

		map.put("Abu Dhabi", "L_AE_AbuDh");
		map.put("ADGAS", "L_AE_AbuDh");
		map.put("ADGAS LNG", "L_AE_AbuDh");
		map.put("Dubai", "L_AE_Jebel");
		map.put("Das Island", "L_AE_AbuDh");
		map.put("Das Is", "L_AE_AbuDh");
		map.put("Fujairah", "L_AE_Fujai");
		map.put("Jebel Ali - LNG Terminal", "L_AE_Jebel");
		map.put("Jebel Ali - LNG Terminal LNG", "L_AE_Jebel");
		map.put("Jebel Ali", "L_AE_Jebel");
		map.put("Soyo Angola LNG Terminal", "L_AO_Soyo");
		map.put("Soyo", "L_AO_Soyo");
		map.put("Soyo Terminal", "L_AO_Soyo");
		map.put("Bahia Blanca LNG Terminal", "L_AR_Bahia");
		map.put("Bahia Blanca", "L_AR_Bahia");
		map.put("Escobar LNG Terminal", "L_AR_Escob");
		map.put("Escobar", "L_AR_Escob");
		map.put("Barrow", "L_AU_Barro");
		map.put("Barrow Island", "L_AU_Barro");
		map.put("Dampier", "L_AU_Dampi");
		map.put("Darwin LNG Terminal", "L_AU_Darwi");
		map.put("Darwin LNG", "L_AU_Darwi");
		map.put("Wickham Point LNG Terminal - Darwin", "L_AU_Darwi");
		map.put("Wickham Point LNG Terminal", "L_AU_Darwi");
		map.put("Wickham Point LNG Terminal - Darwin Terminal", "L_AU_Darwi");
		map.put("Darwin", "L_AU_Darwi");
		map.put("Gladstone GLNG Terminal", "L_AU_Glads");
		map.put("Gladstone LNG Terminal", "L_AU_Glads");
		map.put("Gladstone", "L_AU_Glads");
		map.put("Karratha", "L_AU_Karra");
		map.put("Karratha Gas Plant", "L_AU_Karra");
		map.put("Withnell Bay", "L_AU_Karra");
		map.put("Pluto", "L_AU_Pluto");
		map.put("Pluto LNG Jetty", "L_AU_Pluto");
		map.put("Pluto LNG", "L_AU_Pluto");
		map.put("Chittagong", "L_BD_Chitt");
		map.put("Fluxys", "L_BE_Zeebr");
		map.put("Zeebrugge", "L_BE_Zeebr");
		map.put("Zeebrugge LNG Terminal", "L_BE_Zeebr");
		map.put("Bahrain", "L_BH_Bahra");
		map.put("Lumut Terminal, Brunei", "L_BN_Lumut");
		map.put("Lumut", "L_BN_Lumut");
		map.put("Brunei LNG", "L_BN_Lumut");
		map.put("Brunei", "L_BN_Lumut");
		map.put("Seria Terminal", "L_BN_Seria");
		map.put("Seria", "L_BN_Seria");
		map.put("Petrobras Guanabara LNG", "L_BR_Guana");
		map.put("Guanabara LNG Terminal", "L_BR_Guana");
		map.put("Guanabara", "L_BR_Guana");
		map.put("Guanabara Bay", "L_BR_Guana");
		map.put("Petrobras Pecem LNG", "L_BR_Pecem");
		map.put("Pecem", "L_BR_Pecem");
		map.put("Rio Grande", "L_BR_RioGr");
		map.put("Bahia", "L_BR_Salva");
		map.put("Salvador LNG", "L_BR_Salva");
		map.put("Salvador", "L_BR_Salva");
		map.put("Sao Francisco do Sul", "L_BR_SaoFr");
		map.put("Saint John", "L_CA_Canap");
		map.put("Canaport LNG", "L_CA_Canap");
		map.put("Canaport", "L_CA_Canap");
		map.put("Halifax", "L_CA_Halif");
		map.put("Kitimat", "L_CA_Kitim");
		map.put("Kitimat LNG", "L_CA_Kitim");
		map.put("Prince Rupert", "L_CA_Princ");
		map.put("Quebec", "L_CA_Quebe");
		map.put("Mejillones - Terminal Maratimo", "L_CL_Mejil");
		map.put("Mejillones", "L_CL_Mejil");
		map.put("Quintero", "L_CL_Quint");
		map.put("Quintero Bay LNG", "L_CL_Quint");
		map.put("Talcahuano LNG", "L_CL_Talca");
		map.put("Talcahuano", "L_CL_Talca");
		map.put("Kribi LNG", "L_CM_Kribi");
		map.put("Kribi", "L_CM_Kribi");
		map.put("Chiwan", "L_CN_Chiwa");
		map.put("Dalian - LNG Terminal", "L_CN_Dalia");
		map.put("Dalian", "L_CN_Dalia");
		map.put("Fujian LNG", "L_CN_Fujia");
		map.put("Fujian LNG Terminal", "L_CN_Fujia");
		map.put("Xiuyu", "L_CN_Fujia");
		map.put("Fujian", "L_CN_Fujia");
		map.put("Dapeng LNG", "L_CN_Guang");
		map.put("Guangdong LNG Terminal", "L_CN_Guang");
		map.put("Guangdong Dapeng", "L_CN_Guang");
		map.put("Dapeng", "L_CN_Guang");
		map.put("Guangdong", "L_CN_Guang");
		map.put("Haikou", "L_CN_Haiko");
		map.put("Heibei", "L_CN_Heibe");
		map.put("Caofeidian LNG Terminal", "L_CN_Heibe");
		map.put("Cao Fei Dian 11", "L_CN_Heibe");
		map.put("Tangshan", "L_CN_Heibe");
		map.put("Rudong", "L_CN_Jiang");
		map.put("Rudong LNG", "L_CN_Jiang");
		map.put("Jiangsu LNG Terminal", "L_CN_Jiang");
		map.put("Nantong", "L_CN_Jiang");
		map.put("Jiangsu", "L_CN_Jiang");
		map.put("Lianyungang", "L_CN_Liany");
		map.put("Lianyungang LNG", "L_CN_Liany");
		map.put("Ningbo", "L_CN_Ningb");
		map.put("Qingdao", "L_CN_Qingd");
		map.put("Qingdao LNG,Quingdao (formerly Tsingtao)", "L_CN_Qingd");
		map.put("Qinhuangdao LNG", "L_CN_Qinhu");
		map.put("Qinhuangdao", "L_CN_Qinhu");
		map.put("Qinzhou LNG", "L_CN_Qinzh");
		map.put("Qinzhou", "L_CN_Qinzh");
		map.put("Shanghai", "L_CN_Shang");
		map.put("Shanghai LNG", "L_CN_Shang");
		map.put("Shenzhen LNG", "L_CN_Shenz");
		map.put("Shenzhen", "L_CN_Shenz");
		map.put("Tianjin Lingang", "L_CN_Tianj");
		map.put("Tianjin", "L_CN_Tianj");
		map.put("Yangpu", "L_CN_Yangp");
		map.put("Yangpu LNG Terminal", "L_CN_Yangp");
		map.put("Yangshan", "L_CN_Yangs");
		map.put("Yangshan Deep Water Port", "L_CN_Yangs");
		map.put("Zhangjiagang LNG", "L_CN_Zhang");
		map.put("Zhangjiagang", "L_CN_Zhang");
		map.put("Zhuhai", "L_CN_Zhuha");
		map.put("Puerto Bahia", "L_CO_Puert");
		map.put("Balboa", "L_CP_Balbo");
		map.put("Colon", "L_CP_Colon");
		map.put("Colon, Panama", "L_CP_Colon");
		map.put("Port Said", "L_CP_PortS");
		map.put("Suez", "L_CP_Suez");
		map.put("Limassol LNG", "L_CY_Limas");
		map.put("Limassol", "L_CY_Limas");
		map.put("Rostock", "L_DE_Rosto");
		map.put("Rostock LNG", "L_DE_Rosto");
		map.put("Wilhelmshaven", "L_DE_Wilhe");
		map.put("Wilhelmshaven LNG", "L_DE_Wilhe");
		map.put("Caucedo Terminal", "L_DO_AESAn");
		map.put("AES Andres", "L_DO_AESAn");
		map.put("Dominican Republic", "L_DO_AESAn");
		map.put("Caucedo", "L_DO_AESAn");
		map.put("Andres", "L_DO_AESAn");
		map.put("Arzew", "L_DZ_Arzew");
		map.put("Bethioua", "L_DZ_Bethi");
		map.put("Skikda LNG", "L_DZ_Skikd");
		map.put("Skikda", "L_DZ_Skikd");
		map.put("Sonatrach", "L_DZ_Skikd");
		map.put("Sonatrach LNG", "L_DZ_Skikd");
		map.put("Paldiski", "L_EE_Paldi");
		map.put("Paldiski South Harbour", "L_EE_Paldi");
		map.put("Sokhna", "L_EG_AinSo");
		map.put("Ain Sokhna", "L_EG_AinSo");
		map.put("Ain Sukhna", "L_EG_AinSo");
		map.put("SEGAS", "L_EG_Damie");
		map.put("Damietta", "L_EG_Damie");
		map.put("Idku", "L_EG_Idku");
		map.put("Idku LNG", "L_EG_Idku");
		map.put("Barcelona LNG", "L_ES_Barce");
		map.put("Barcelona LNG Terminal", "L_ES_Barce");
		map.put("Barcelona", "L_ES_Barce");
		map.put("Bilbao LNG", "L_ES_Bilba");
		map.put("Bilbao - LNG Terminal", "L_ES_Bilba");
		map.put("Bilbao", "L_ES_Bilba");
		map.put("Cartagena LNG Terminal, Spain", "L_ES_Carta");
		map.put("Cartagena LNG", "L_ES_Carta");
		map.put("Cartagena", "L_ES_Carta");
		map.put("Cartagena LNG Terminal", "L_ES_Carta");
		map.put("Murgardos", "L_ES_ElFer");
		map.put("Reganose", "L_ES_ElFer");
		map.put("Reganosa", "L_ES_ElFer");
		map.put("Ferol", "L_ES_ElFer");
		map.put("El Ferrol", "L_ES_ElFer");
		map.put("La Coruna", "L_ES_ElFer");
		map.put("Ferrol", "L_ES_ElFer");
		map.put("Huelva", "L_ES_Huelv");
		map.put("Huelva LNG", "L_ES_Huelv");
		map.put("Sagunto", "L_ES_Sagun");
		map.put("Sagunto LNG Terminal", "L_ES_Sagun");
		map.put("Dunkirk, France", "L_FR_Dunki");
		map.put("Dunkirk", "L_FR_Dunki");
		map.put("Fos - Cavaou LNG Terminal", "L_FR_FosCa");
		map.put("Fos - Cavaou", "L_FR_FosCa");
		map.put("Fos Sur Mer", "L_FR_FosSu");
		map.put("Fos - LNG Terminal", "L_FR_FosSu");
		map.put("Le Havre", "L_FR_LeHav");
		map.put("Montoir De Bretagne", "L_FR_Monto");
		map.put("Montoir", "L_FR_Monto");
		map.put("Montoir Gas Terminal - Loire River", "L_FR_Monto");
		map.put("Dragon LNG Terminal", "L_GB_Drago");
		map.put("Dragon", "L_GB_Drago");
		map.put("Dragon LNG", "L_GB_Drago");
		map.put("Holyhead", "L_GB_Holyh");
		map.put("Isle of Grain", "L_GB_Isleo");
		map.put("Isle of Grain Gas Terminal", "L_GB_Isleo");
		map.put("Swansea", "L_GB_South");
		map.put("Swansea, U.K.", "L_GB_South");
		map.put("Milford Haven", "L_GB_South");
		map.put("South Hook", "L_GB_South");
		map.put("South Hook LNG", "L_GB_South");
		map.put("South Hook LNG Terminal", "L_GB_South");
		map.put("Excelerate LNG Terminal - Teesport", "L_GB_Teesi");
		map.put("Teeside", "L_GB_Teesi");
		map.put("Teesport", "L_GB_Teesi");
		map.put("Gibraltar", "L_GI_Gibra");
		map.put("Punta Europa", "L_GQ_Punta");
		map.put("Punta Europa LNG", "L_GQ_Punta");
		map.put("Punta Europa LNG Terminal", "L_GQ_Punta");
		map.put("Bioko Island", "L_GQ_Punta");
		map.put("Bioko Is", "L_GQ_Punta");
		map.put("Revithousa LNG Terminal", "L_GR_Revit");
		map.put("Revithousa", "L_GR_Revit");
		map.put("Revithoussa", "L_GR_Revit");
		map.put("Krk", "L_HR_KrkIs");
		map.put("Krk Island", "L_HR_KrkIs");
		map.put("Ambon", "L_ID_Ambon");
		map.put("Blanglacang", "L_ID_Arun");
		map.put("Blanglacang LNG Terminal", "L_ID_Arun");
		map.put("Blang Lacang", "L_ID_Arun");
		map.put("Arun", "L_ID_Arun");
		map.put("Arun LNG", "L_ID_Arun");
		map.put("Arun LNG Terminal", "L_ID_Arun");
		map.put("Badak", "L_ID_Bonta");
		map.put("Badak LNG Terminal", "L_ID_Bonta");
		map.put("Bontang LNG", "L_ID_Bonta");
		map.put("Bontang", "L_ID_Bonta");
		map.put("Jakarta", "L_ID_Jakar");
		map.put("Luwuk LNG", "L_ID_Luwuk");
		map.put("Luwuk", "L_ID_Luwuk");
		map.put("Panjang LNG", "L_ID_Panja");
		map.put("Panjang", "L_ID_Panja");
		map.put("Semarang", "L_ID_Semar");
		map.put("Semarang LNG", "L_ID_Semar");
		map.put("Tannguh", "L_ID_Tangg");
		map.put("Tangguh", "L_ID_Tangg");
		map.put("Tangguh LNG", "L_ID_Tangg");
		map.put("Limerick", "L_IE_Limer");
		map.put("Limerick LNG", "L_IE_Limer");
		map.put("Ashdod", "L_IL_Ashdo");
		map.put("Hadera", "L_IL_Hader");
		map.put("Dabhol LNG", "L_IN_Dabho");
		map.put("Dabhol", "L_IN_Dabho");
		map.put("Dabhol Port", "L_IN_Dabho");
		map.put("Dahej", "L_IN_Dahej");
		map.put("Dahej LNG Terminal", "L_IN_Dahej");
		map.put("Ennore", "L_IN_Ennor");
		map.put("Gangavaram", "L_IN_Ganga");
		map.put("Offshore Haldia No.1 (TA)", "L_IN_Haldi");
		map.put("Haldia", "L_IN_Haldi");
		map.put("Hazira", "L_IN_Hazir");
		map.put("Hazira LNG", "L_IN_Hazir");
		map.put("Hazira - LNG Terminal", "L_IN_Hazir");
		map.put("Kakinada", "L_IN_Kakin");
		map.put("Kakinada LNG", "L_IN_Kakin");
		map.put("Karwar - South Port", "L_IN_Karwa");
		map.put("Karwar - South Port South", "L_IN_Karwa");
		map.put("Karwar", "L_IN_Karwa");
		map.put("Kochi", "L_IN_Kochi");
		map.put("Kochi LNG Terminal", "L_IN_Kochi");
		map.put("Cochin", "L_IN_Kochi");
		map.put("Cochin LNG Terminal", "L_IN_Kochi");
		map.put("Mundra LNG", "L_IN_Mundr");
		map.put("Mundra", "L_IN_Mundr");
		map.put("Pipavav LNG", "L_IN_Pipav");
		map.put("Pipavav", "L_IN_Pipav");
		map.put("Khor al Zubair", "L_IQ_Khora");
		map.put("Pars LNG Terminal", "L_IR_ParsL");
		map.put("Pars", "L_IR_ParsL");
		map.put("Iran", "L_IR_Tomba");
		map.put("Tombak LNG Terminal", "L_IR_Tomba");
		map.put("Tombak", "L_IR_Tomba");
		map.put("Falconara", "L_IT_Falco");
		map.put("Gioia Tauro", "L_IT_Gioia");
		map.put("Livorno LNG Terminal", "L_IT_Livor");
		map.put("Livorno", "L_IT_Livor");
		map.put("Ortona LNG", "L_IT_Orton");
		map.put("Ortona", "L_IT_Orton");
		map.put("La Spezia LNG Terminal", "L_IT_Panig");
		map.put("La Spezia", "L_IT_Panig");
		map.put("Panigaglia", "L_IT_Panig");
		map.put("Porto Empedocle", "L_IT_Porto");
		map.put("Rosignano LNG", "L_IT_Rosig");
		map.put("Rovigo LNG Terminal", "L_IT_Rovig");
		map.put("Rovigo", "L_IT_Rovig");
		map.put("Taranto", "L_IT_Taran");
		map.put("Taranto LNG", "L_IT_Taran");
		map.put("Trieste LNG", "L_IT_Tries");
		map.put("Trieste", "L_IT_Tries");
		map.put("Port Esquivel", "L_JM_PortE");
		map.put("Aqaba", "L_JO_Aqaba");
		map.put("Chita - LNG", "L_JP_Chita");
		map.put("Chita LNG", "L_JP_Chita");
		map.put("Chita", "L_JP_Chita");
		map.put("Kumamoto", "L_JP_Fukuo");
		map.put("Fukuoko", "L_JP_Fukuo");
		map.put("Futtsu", "L_JP_Futts");
		map.put("Hachinohe", "L_JP_Hachi");
		map.put("Hakodate LNG", "L_JP_Hakod");
		map.put("Hakodate", "L_JP_Hakod");
		map.put("Hatsukaichi", "L_JP_Hatsu");
		map.put("Higashi-Ogishima", "L_JP_Higas");
		map.put("Higashi-Ohgishima", "L_JP_Higas");
		map.put("H-Ohgishima", "L_JP_Higas");
		map.put("H-Ogishima", "L_JP_Higas");
		map.put("Himeji", "L_JP_Himej");
		map.put("Hitachi", "L_JP_Hitac");
		map.put("Naoetsu", "L_JP_Joets");
		map.put("Joetsu", "L_JP_Joets");
		map.put("Kagoshima - LNG Berths", "L_JP_Kagos");
		map.put("Kagoshima", "L_JP_Kagos");
		map.put("Kawagoe", "L_JP_Kawag");
		map.put("Kitakyushu", "L_JP_Kitak");
		map.put("Kitakyushu LNG", "L_JP_Kitak");
		map.put("Kushiro LNG", "L_JP_Kushi");
		map.put("Kushiro", "L_JP_Kushi");
		map.put("Mizushima", "L_JP_Mizus");
		map.put("Mizushima LNG", "L_JP_Mizus");
		map.put("Nagasaki LNG", "L_JP_Nagas");
		map.put("Nagasaki", "L_JP_Nagas");
		map.put("Nanao", "L_JP_Nanao");
		map.put("Nanao LNG", "L_JP_Nanao");
		map.put("Negishi", "L_JP_Negis");
		map.put("Negishi LNG", "L_JP_Negis");
		map.put("Niigata-Higashi", "L_JP_Niiga");
		map.put("Niigata Higashi", "L_JP_Niiga");
		map.put("Niigata-Nishi", "L_JP_Niiga2");
		map.put("Niigata Nishi", "L_JP_Niiga2");
		map.put("Ohgishima", "L_JP_Ohgis");
		map.put("Ogishima Tokyo Gas", "L_JP_Ohgis");
		map.put("Ogishima - Tokyo Gas LNG Berth", "L_JP_Ohgis");
		map.put("Ogishima", "L_JP_Ohgis");
		map.put("Oita - LNG Berth", "L_JP_Oita");
		map.put("Oita", "L_JP_Oita");
		map.put("Okinawa LNG", "L_JP_Okina");
		map.put("Okinawa", "L_JP_Okina");
		map.put("Osaka", "L_JP_Osaka");
		map.put("Ishikariwan-Shinko", "L_JP_Otaru");
		map.put("Ishikariwanshinko", "L_JP_Otaru");
		map.put("Otaru", "L_JP_Otaru");
		map.put("Ishikariwan-Shinko Ishikariwan", "L_JP_Otaru");
		map.put("Ishikariwan", "L_JP_Otaru");
		map.put("Sakai", "L_JP_Sakai");
		map.put("Sakai LNG", "L_JP_Sakai");
		map.put("Sakaide", "L_JP_Sakai2");
		map.put("Sakaide LNG", "L_JP_Sakai2");
		map.put("Senboku", "L_JP_Senbo");
		map.put("Senboku LNG", "L_JP_Senbo");
		map.put("Sendai LNG", "L_JP_Senda");
		map.put("Sendai", "L_JP_Senda");
		map.put("Sodeshi", "L_JP_Shimi");
		map.put("Shimizu, (Honshu), Japan", "L_JP_Shimi");
		map.put("Shimizu", "L_JP_Shimi");
		map.put("Shin-minato", "L_JP_Shinm");
		map.put("Shinminato", "L_JP_Shinm");
		map.put("Sodegaura LNG", "L_JP_Sodeg");
		map.put("Sodegaura", "L_JP_Sodeg");
		map.put("Soma", "L_JP_Soma");
		map.put("Soma LNG", "L_JP_Soma");
		map.put("Tobata", "L_JP_Tobat");
		map.put("Tobata LNG", "L_JP_Tobat");
		map.put("Tokyo Bay", "L_JP_Tokyo");
		map.put("Tokyo", "L_JP_Tokyo");
		map.put("Wakayama LNG", "L_JP_Wakay");
		map.put("Wakayama", "L_JP_Wakay");
		map.put("Yanai LNG", "L_JP_Yanai");
		map.put("Yanai", "L_JP_Yanai");
		map.put("Yokkaichi - LNG", "L_JP_Yokka");
		map.put("Yokkaichi", "L_JP_Yokka");
		map.put("Yokohama", "L_JP_Yokoh");
		map.put("Mombasa LNG", "L_KE_Momba");
		map.put("Mombasa", "L_KE_Momba");
		map.put("Boryeong", "L_KR_Borye");
		map.put("Dangjin", "L_KR_Dangj");
		map.put("Donghae", "L_KR_Dongh");
		map.put("Gwangyang - LNG Terminal", "L_KR_Gwang");
		map.put("Gwangyang", "L_KR_Gwang");
		map.put("Inchon - Soraepogu LNG Terminal", "L_KR_Inche");
		map.put("Incheon", "L_KR_Inche");
		map.put("Inchon", "L_KR_Inche");
		map.put("Kwangyang", "L_KR_Kwang");
		map.put("Pyongtaek", "L_KR_Pyeon");
		map.put("Pyeongtaek", "L_KR_Pyeon");
		map.put("Pyeong Taek LNG Terminal", "L_KR_Pyeon");
		map.put("Pyeong Taek", "L_KR_Pyeon");
		map.put("Samchok", "L_KR_Samch");
		map.put("Samcheog", "L_KR_Samch");
		map.put("Tongyeong", "L_KR_TongY");
		map.put("Tong Yong", "L_KR_TongY");
		map.put("Mina Al-Ahmadi, Kuwait", "L_KW_MinaA");
		map.put("Mina Al Ahmadi, Kuwait", "L_KW_MinaA");
		map.put("Mina Al Ahmadi", "L_KW_MinaA");
		map.put("Mina Al-Ahmadi", "L_KW_MinaA");
		map.put("Tripoli, Lebanon", "L_LB_Tripo");
		map.put("Tripoli", "L_LB_Tripo");
		map.put("Klaipeda LNG", "L_LT_Klaip");
		map.put("Klaipeda", "L_LT_Klaip");
		map.put("Marsa Brega", "L_LY_Marsa");
		map.put("Marsa El Brega", "L_LY_Marsa");
		map.put("Marsa el Brega", "L_LY_Marsa");
		map.put("Jorf Lasfar", "L_MA_JorfL");
		map.put("Fort de France", "L_MQ_Fortd");
		map.put("Malta", "L_MT_Malta");
		map.put("Altamira, Mexico", "L_MX_Altam");
		map.put("Altamira", "L_MX_Altam");
		map.put("Costa Azul LNG Terminal", "L_MX_Costa");
		map.put("Costa Azul", "L_MX_Costa");
		map.put("Manzanillo, Mexico", "L_MX_Manza");
		map.put("Manzanillo", "L_MX_Manza");
		map.put("Rosarito LNG", "L_MX_Rosar");
		map.put("Rosarito", "L_MX_Rosar");
		map.put("Bintulu Terminal", "L_MY_Bintu");
		map.put("Bintulu LNG Terminal", "L_MY_Bintu");
		map.put("Bintulu", "L_MY_Bintu");
		map.put("Johore", "L_MY_Johor");
		map.put("Johore LNG", "L_MY_Johor");
		map.put("Lahad Datu", "L_MY_Lahad");
		map.put("Malacca", "L_MY_Malac");
		map.put("Malacca LNG", "L_MY_Malac");
		map.put("Malaysia LNG", "L_MY_Bintu");
		map.put("Sungai Udang", "L_MY_Sunga");
		map.put("Mozambique Port", "L_MZ_Mozam");
		map.put("Bonny Nigeria", "L_NG_Bonny");
		map.put("Bonny", "L_NG_Bonny");
		map.put("Bonny Is", "L_NG_Bonny");
		map.put("Bonny Island LNG Terminal", "L_NG_Bonny");
		map.put("Lagos", "L_NG_Bonny");
		map.put("Bonny Terminal", "L_NG_Bonny");
		map.put("Brass Terminal", "L_NG_Brass");
		map.put("Brass", "L_NG_Brass");
		map.put("Rotterdam LNG", "L_NL_Rotte");
		map.put("Gate", "L_NL_Rotte");
		map.put("Rotterdam", "L_NL_Rotte");
		map.put("Maasvlakte", "L_NL_Rotte");
		map.put("Hammerfest", "L_NO_Hamme");
		map.put("Hammerfest LNG Terminal - Melkoya Island", "L_NO_Hamme");
		map.put("Melkoya Island", "L_NO_Hamme");
		map.put("Melkoya Is", "L_NO_Hamme");
		map.put("Hammerfest LNG", "L_NO_Hamme");
		map.put("New Plymouth", "L_NZ_NewPl");
		map.put("Qalhat", "L_OM_Qalha");
		map.put("Qalhat LNG Terminal", "L_OM_Qalha");
		map.put("Oman LNG", "L_OM_Qalha");
		map.put("Sur", "L_OM_Qalha");
		map.put("Pampa Melchorita", "L_PE_Pampa");
		map.put("Peru LNG Terminal", "L_PE_Pampa");
		map.put("P.Melchorita", "L_PE_Pampa");
		map.put("Port Moresby", "L_PG_PortM");
		map.put("Batangas", "L_PH_Batan");
		map.put("Tabangao", "L_PH_Taban");
		map.put("Karachi LNG", "L_PK_Karac");
		map.put("Karachi", "L_PK_Karac");
		map.put("Port Qasim", "L_PK_PortQ");
		map.put("Swinoujscie LNG", "L_PL_Swino");
		map.put("Swinoujscie", "L_PL_Swino");
		map.put("Aguirre", "L_PR_Guaya");
		map.put("Guayama", "L_PR_Guaya");
		map.put("Guayanilla LNG Terminal", "L_PR_Penue");
		map.put("Guayanilla", "L_PR_Penue");
		map.put("EcoElectrica LNG", "L_PR_Penue");
		map.put("Penuelas", "L_PR_Penue");
		map.put("Panuelas", "L_PR_Penue");
		map.put("Sines LNG Terminal", "L_PT_Sines");
		map.put("Sines", "L_PT_Sines");
		map.put("Ras Laffan", "L_QA_RasLa");
		map.put("Ras Laffan (Rich)", "L_QA_RasLa");
		map.put("Ras Laffan (Lean)", "L_QA_RasLa");
		map.put("Drovyanoy", "L_RU_Drovy");
		map.put("Prigorodnoye", "L_RU_Sakha");
		map.put("Sakhalin", "L_RU_Sakha");
		map.put("Sakhalin-2", "L_RU_Sakha");
		map.put("Sakhalin II", "L_RU_Sakha");
		map.put("Murmansk", "L_RU_Shtok");
		map.put("Shtokman", "L_RU_Shtok");
		map.put("Vladivostok LNG", "L_RU_Vladi");
		map.put("Vladivostok", "L_RU_Vladi");
		map.put("Vyborg LNG", "L_RU_Vybor");
		map.put("Vyborg", "L_RU_Vybor");
		map.put("Jurong Island", "L_SG_Juron");
		map.put("Jurong Marine Base - Singapore Base", "L_SG_Juron");
		map.put("Jurong Island, Singapore", "L_SG_Juron");
		map.put("Jurong Marine Base - Singapore", "L_SG_Juron");
		map.put("Jurong Marine Base", "L_SG_Juron");
		map.put("Singapore", "L_SG_Juron");
		map.put("Koper", "L_SI_Koper");
		map.put("Koper LNG", "L_SI_Koper");
		map.put("Hainan Strait", "L_SP_Haina");
		map.put("Hainan", "L_SP_Haina");
		map.put("Acajutla", "L_SV_Acaju");
		map.put("Map Ta Phut", "L_TH_MapTa");
		map.put("Dili", "L_TP_Dili");
		map.put("Aliaga", "L_TR_Aliag");
		map.put("Aliaga LNG", "L_TR_Aliag");
		map.put("Marmara Ereglisi", "L_TR_Marma");
		map.put("Marmara", "L_TR_Marma");
		map.put("Ereglisi Terminal", "L_TR_Marma");
		map.put("Pointe-a-Pitre", "L_TT_Point");
		map.put("Pointe-a-Pitre a", "L_TT_Point");
		map.put("Pointe", "L_TT_Point");
		map.put("Point Fortin", "L_TT_Point2");
		map.put("Taichung", "L_TW_Taich");
		map.put("Taichung LNG", "L_TW_Taich");
		map.put("Yung-an LNG Terminal", "L_TW_YungA");
		map.put("Yung An", "L_TW_YungA");
		map.put("Yung-an", "L_TW_YungA");
		map.put("Dar-es-Salaam", "L_TZ_Dar-e");
		map.put("Yuzhny LNG", "L_UA_Yuzhn");
		map.put("Yuzhny", "L_UA_Yuzhn");
		map.put("Gulf Gateway Deepwater Port", "L_UM_GulfG");
		map.put("Gulf Gateway", "L_UM_GulfG");
		map.put("Gulf Gateway LNG Terminal", "L_UM_GulfG");
		map.put("Neptune", "L_UM_Neptu");
		map.put("Neptune Deepwater Port", "L_UM_Neptu");
		map.put("Northeast Gateway Deepwater Port", "L_UM_North");
		map.put("North East Gateway", "L_UM_North");
		map.put("Northeast Gateway", "L_UM_North");
		map.put("Brownsville", "L_US_Brown");
		map.put("Cameron LNG", "L_US_Camer");
		map.put("Cameron", "L_US_Camer");
		map.put("Coos Bay", "L_US_CoosB");
		map.put("Corpus Christi", "L_US_Corpu");
		map.put("Cove Point", "L_US_CoveP");
		map.put("Dominion Cove Point LNG", "L_US_CoveP");
		map.put("Cove Point - LNG", "L_US_CoveP");
		map.put("Cove Point, Md", "L_US_CoveP");
		map.put("Elba Island - Savanah", "L_US_Elba");
		map.put("Elba Is", "L_US_Elba");
		map.put("Southern LNG", "L_US_Elba");
		map.put("Savannah", "L_US_Elba");
		map.put("Elba Island Terminal", "L_US_Elba");
		map.put("Elba", "L_US_Elba");
		map.put("Savanah", "L_US_Elba");
		map.put("Elba Island", "L_US_Elba");
		map.put("Everett Terminal, Ma", "L_US_Evere");
		map.put("Everett, Massachusetts, U.S.A.", "L_US_Evere");
		map.put("Everett Marine Terminal", "L_US_Evere");
		map.put("Everett", "L_US_Evere");
		map.put("Freeport LNG Terminal, Texas, U.S.A.", "L_US_Freep");
		map.put("Freeport, Tx", "L_US_Freep");
		map.put("Freeport LNG", "L_US_Freep");
		map.put("Freeport", "L_US_Freep");
		map.put("Golden Pass", "L_US_Golde");
		map.put("Golden Pass LNG", "L_US_Golde");
		map.put("Kenai, Ak", "L_US_Kenai");
		map.put("Kenai", "L_US_Kenai");
		map.put("Nikiski", "L_US_Kenai");
		map.put("Kenai LNG", "L_US_Kenai");
		map.put("Kenai LNG Terminal", "L_US_Kenai");
		map.put("Trunkline LNG", "L_US_LakeC");
		map.put("Lake Charles", "L_US_LakeC");
		map.put("Lake Charles, La", "L_US_LakeC");
		map.put("Lake Charles - LNG Berths", "L_US_LakeC");
		map.put("Gulf LNG Terminal - Pascagoula", "L_US_Pasca");
		map.put("Pascagoula", "L_US_Pasca");
		map.put("Pascagoula LNG Terminal", "L_US_Pasca");
		map.put("Plaquemines LNG", "L_US_Plaqu");
		map.put("Plaquemines", "L_US_Plaqu");
		map.put("Port Arthur, Texas, U.S.A.", "L_US_PortA");
		map.put("Port Arthur", "L_US_PortA");
		map.put("Port Dolphin", "L_US_PortD");
		map.put("Tampa", "L_US_PortD");
		map.put("Sabine Pass", "L_US_Sabin");
		map.put("Sabine Pass LNG Terminal", "L_US_Sabin");
		map.put("Sabine Pass LNG", "L_US_Sabin");
		map.put("Sabine Pass, Tx", "L_US_Sabin");
		map.put("Montevideo LNG", "L_UY_Monte");
		map.put("Montevideo", "L_UY_Monte");
		map.put("Panama Canal", "L_V_Panam");
		map.put("Cai Mep Gas Terminal", "L_VN_CaiMe");
		map.put("Cai Mep", "L_VN_CaiMe");
		map.put("Ho Chi Min City (formerly Saigon)", "L_VN_HoChi");
		map.put("Ho Chi Min City", "L_VN_HoChi");
		map.put("Yemen LNG", "L_YE_BalHa");
		map.put("Yemen", "L_YE_BalHa");
		map.put("Bal Haf", "L_YE_BalHa");
		map.put("Bal Haf LNG", "L_YE_BalHa");
		map.put("Balhaf", "L_YE_BalHa");
		map.put("Santa Cruz de Tenerife", "L_YI_Tener");
		map.put("Tenerife", "L_YI_Tener");
		map.put("Cape Town", "L_ZA_CapeT");
		map.put("Coega", "L_ZA_Coega");
		map.put("Mossel Bay", "L_ZA_Mosse");

		return map;
	}

}

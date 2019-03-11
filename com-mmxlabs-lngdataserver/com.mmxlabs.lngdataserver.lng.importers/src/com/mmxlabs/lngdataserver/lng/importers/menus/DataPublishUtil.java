package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.CheckedSupplier;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsRepository;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.lngdataserver.integration.models.financial.settled.SettledPricesRepository;
import com.mmxlabs.lngdataserver.integration.models.financial.settled.SettledPricesVersion;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsRepository;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsVersion;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsRepository;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsVersion;
import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.exporters.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.bunkerfuels.BunkerFuelsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distances.PortAndDistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.financial.SettledPricesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.lngdataserver.lng.importers.portgroups.PortGroupsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.vesselgroups.VesselGroupsFromScenarioCopier;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class DataPublishUtil {

	public static final void uploadReferenceData(Collection<DataOptions> options, final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		for (DataOptions option : options) {
			switch (option) {
			case PricingData:
				checkAndUploadPricingData(modelRecord, scenarioModel);
				break;
			case PortData:
				checkAndUploadDistanceData(modelRecord, scenarioModel);
				checkAndUploadPortData(modelRecord, scenarioModel);
				checkAndUploadPortGroupsData(modelRecord, scenarioModel);
				break;
			case ADPData:
			case CargoData:
			case CommercialData:
			case FleetDatabase:
			case SpotCargoMarketsData:
			case SpotCharterMarketsData:
			default:
				break;
			}
		}
	}

	private static <T> boolean dataVersionUpload(final IDataRepository<T> repository, final String uuid, final CheckedSupplier<T, IOException> doLocalExport) throws IOException {
		final boolean hasUpstream = repository.hasUpstreamVersion(uuid);
		if (hasUpstream) {
			// Version already exists
			return true;
		} else {
			// Publish
			try {
				repository.publishVersion(doLocalExport.get());
				return true;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
 
	public static @Nullable String checkAndUploadPricingData(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		final String version = pricingModel.getMarketCurvesVersionRecord().getVersion();
		if (dataVersionUpload(PricingRepository.INSTANCE, version, () -> PricingFromScenarioCopier.generateVersion(pricingModel))) {
			return version;
		}
		return null;
	}

	public static @Nullable String checkAndUploadDistanceData(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final String version = portModel.getDistanceVersionRecord().getVersion();
		if (dataVersionUpload(DistanceRepository.INSTANCE, version, () -> DistancesFromScenarioCopier.generateVersion(portModel))) {
			return version;
		}
		return null;
	}

	public static @Nullable String checkAndUploadPortData(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final String version = portModel.getPortVersionRecord().getVersion();
		if (dataVersionUpload(PortsRepository.INSTANCE, version, () -> PortFromScenarioCopier.generateVersion(portModel))) {
			return version;
		}
		return null;
	}

	public static @Nullable String checkAndUploadPortGroupsData(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final String version = portModel.getPortGroupVersionRecord().getVersion();
		if (dataVersionUpload(PortGroupsRepository.INSTANCE, version, () -> PortGroupsFromScenarioCopier.generateVersion(portModel))) {
			return version;
		}
		return null;
	}

	private static void uploadBunkerFuels(IScenarioDataProvider sdp) throws Exception {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(sdp);
		BunkerFuelsVersion version = BunkerFuelsFromScenarioCopier.generateVersion(fleetModel);
		BunkerFuelsRepository.INSTANCE.publishVersion(version);
	}

	private static void uploadPortGroups(IScenarioDataProvider sdp) throws Exception {
		final PortModel portModel = ScenarioModelUtil.getPortModel(sdp);
		PortGroupsVersion version = PortGroupsFromScenarioCopier.generateVersion(portModel);
		PortGroupsRepository.INSTANCE.publishVersion(version);
	}

	private static void uploadSettledPrices(IScenarioDataProvider sdp) throws Exception {
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(sdp);
		SettledPricesVersion version = SettledPricesFromScenarioCopier.generateVersion(pricingModel);
		SettledPricesRepository.INSTANCE.publishVersion(version);
	}

	private static void uploadVesselGroups(IScenarioDataProvider sdp) throws Exception {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(sdp);
		VesselGroupsVersion version = VesselGroupsFromScenarioCopier.generateVersion(fleetModel);
		VesselGroupsRepository.INSTANCE.publishVersion(version);
	}

	private static DistancesVersion exportDistances(final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final DistancesVersion version = DistancesFromScenarioCopier.generateVersion(portModel);
		return version;
	}

	private static PortsVersion exportPort(final LNGScenarioModel scenarioModel) throws IOException {
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
		final PortsVersion version = PortFromScenarioCopier.generateVersion(portModel);
		return version;
	}

	// private static @Nullable String checkDistanceDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
	// final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
	// final String version = portModel.getDistanceDataVersion();
	// if (genericVersionUpload(DistanceRepository.INSTANCE, version, () -> exportDistances(scenarioModel))) {
	// return version;
	// }
	// return null;
	// }

	// private static @Nullable String checkFleetDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
	// final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
	// final String version = fleetModel.getFleetDataVersion();
	// if (genericVersionUpload(VesselsRepository.INSTANCE, version, () -> exportVessels(scenarioModel))) {
	// return version;
	// }
	// return null;
	// }

	// private static @Nullable String checkPortDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
	// final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
	// final String version = portModel.getPortDataVersion();
	// if (genericVersionUpload(PortsRepository.INSTANCE, version, () -> exportPort(scenarioModel))) {
	// return version;
	// }
	// return null;
	// }

	private static String exportVessels(final LNGScenarioModel scenarioModel) throws IOException {

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final VesselsVersion version = VesselsFromScenarioCopier.generateVersion(fleetModel);
		return new ObjectMapper().writeValueAsString(version);
	}

	// private static @Nullable String checkFleetDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
	// final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
	// final String version = fleetModel.getFleetDataVersion();
	// if (genericVersionUpload(VesselsRepository.INSTANCE, version, () -> exportVessels(scenarioModel))) {
	// return version;
	// }
	// return null;
	// }

	// private static @Nullable String checkPortDataVersion(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
	// final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
	// final String version = portModel.getPortDataVersion();
	// if (genericVersionUpload(PortsRepository.INSTANCE, version, () -> exportPort(scenarioModel))) {
	// return version;
	// }
	// return null;
	// }
}

package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.util.CheckedSupplier;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsRepository;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsRepository;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsRepository;
import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsRepository;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.lngdataserver.lng.io.bunkerfuels.BunkerFuelsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.portgroups.PortGroupsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vesselgroups.VesselGroupsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
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
			case FleetDatabase:
				checkAndUploadBunkerFuelsData(modelRecord, scenarioModel);
				checkAndUploadVesselData(modelRecord, scenarioModel);
				checkAndUploadVesselGroupsData(modelRecord, scenarioModel);
			case ADPData:
			case CargoData:
			case CommercialData:
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

	public static @Nullable String checkAndUploadVesselData(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final String version = fleetModel.getFleetVersionRecord().getVersion();
		if (dataVersionUpload(VesselsRepository.INSTANCE, version, () -> VesselsFromScenarioCopier.generateVersion(fleetModel))) {
			return version;
		}
		return null;
	}

	public static @Nullable String checkAndUploadVesselGroupsData(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final String version = fleetModel.getVesselGroupVersionRecord().getVersion();
		if (dataVersionUpload(VesselGroupsRepository.INSTANCE, version, () -> VesselGroupsFromScenarioCopier.generateVersion(fleetModel))) {
			return version;
		}
		return null;
	}

	public static @Nullable String checkAndUploadBunkerFuelsData(final ScenarioModelRecord modelRecord, final LNGScenarioModel scenarioModel) throws IOException {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final String version = fleetModel.getBunkerFuelsVersionRecord().getVersion();
		if (dataVersionUpload(BunkerFuelsRepository.INSTANCE, version, () -> BunkerFuelsFromScenarioCopier.generateVersion(fleetModel))) {
			return version;
		}
		return null;
	}

}

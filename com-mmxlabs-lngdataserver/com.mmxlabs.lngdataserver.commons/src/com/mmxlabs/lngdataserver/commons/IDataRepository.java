package com.mmxlabs.lngdataserver.commons;

import java.util.List;
import java.util.function.Consumer;

public interface IDataRepository {

	List<DataVersion> getVersions();

	boolean isReady();

	void syncUpstreamVersion(final String version) throws Exception;

	void publishVersion(String version) throws Exception;

	List<DataVersion> updateAvailable() throws Exception;

	void listenToPreferenceChanges();

	void stopListenToPreferenceChanges();

	void listenForNewLocalVersions();

	void listenForNewUpstreamVersions();

	void stopListeningForNewLocalVersions();

	void stopListeningForNewUpstreamVersions();

	void registerLocalVersionListener(final Consumer<String> versionConsumer);

	void registerUpstreamVersionListener(final Consumer<String> versionConsumer);

}
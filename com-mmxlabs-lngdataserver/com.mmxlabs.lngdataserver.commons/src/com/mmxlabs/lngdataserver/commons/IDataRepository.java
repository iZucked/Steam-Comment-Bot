package com.mmxlabs.lngdataserver.commons;

import java.util.List;
import java.util.function.Consumer;

public interface IDataRepository {

	List<DataVersion> getVersions();

	List<DataVersion> getUpstreamVersions();

	boolean isReady();

	boolean syncUpstreamVersion(final String version) throws Exception;

	boolean publishVersion(String version) throws Exception;

	List<DataVersion> updateAvailable() throws Exception;

	void listenToPreferenceChanges();

	void stopListenToPreferenceChanges();

	void startListenForNewLocalVersions();

	void startListenForNewUpstreamVersions();

	void stopListeningForNewLocalVersions();

	void stopListeningForNewUpstreamVersions();

	void registerLocalVersionListener(final Consumer<String> versionConsumer);

	void registerUpstreamVersionListener(final Consumer<String> versionConsumer);

}
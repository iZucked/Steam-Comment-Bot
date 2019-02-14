/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.commons;

import java.util.List;
import java.util.function.Consumer;

public interface IDataRepository {

	List<DataVersion> getLocalVersions();

	List<DataVersion> getUpstreamVersions();

	boolean isReady();

	boolean syncUpstreamVersion(final String version) throws Exception;

	boolean publishVersion(String version) throws Exception;

	List<DataVersion> updateAvailable() throws Exception;

	void startListenForNewLocalVersions();

	void startListenForNewUpstreamVersions();

	void stopListeningForNewLocalVersions();

	void stopListeningForNewUpstreamVersions();

	void registerLocalVersionListener(final Runnable versionConsumer);

	void registerUpstreamVersionListener(final Runnable versionConsumer);

	boolean hasLocalVersion(String version);

	boolean hasUpstreamVersion(String version);

	boolean publishUpstreamVersion(String versionJSON) throws Exception;

}
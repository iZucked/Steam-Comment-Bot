/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scenario.service.model.manager.ISharedDataModelType;

@NonNullByDefault
public class DataManifest {

	public static class EObjectData {
		private final ISharedDataModelType<?> key;
		private final String uriFragment;
		private final String dataVersion;

		public EObjectData(final ISharedDataModelType<?> key, final String uriFragment, final String dataVersion) {
			this.key = key;
			this.uriFragment = uriFragment;
			this.dataVersion = dataVersion;
		}

		public String getDataVersion() {
			return dataVersion;
		}

		public ISharedDataModelType<?> getKey() {
			return key;
		}

		public String getURIFragment() {
			return uriFragment;
		}
	}

	private final List<EObjectData> extraData = new LinkedList<>();
	private final URI archiveURI;
	private final URI rootObjectURI;

	public DataManifest(final URI archiveURI, final URI rootObjectURI) {
		this.archiveURI = archiveURI;
		this.rootObjectURI = rootObjectURI;

	}

	public URI getArchiveURI() {
		return archiveURI;
	}

	public List<EObjectData> getEObjectData() {
		return extraData;
	}

	public void add(final ISharedDataModelType<?> key, final String uriFragment, final String dataVersion) {
		extraData.add(new EObjectData(key, uriFragment, dataVersion));
	}

	public URI getRootObjectURI() {
		return rootObjectURI;
	}
}

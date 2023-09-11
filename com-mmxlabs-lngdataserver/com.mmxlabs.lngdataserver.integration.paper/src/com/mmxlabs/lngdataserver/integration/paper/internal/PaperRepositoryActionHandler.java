/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper.internal;

import java.util.List;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.lngdataserver.integration.paper.PaperRepository;
import com.mmxlabs.rcp.common.RunnerHelper;

public class PaperRepositoryActionHandler implements IDataBrowserActionsHandler {

	private final PaperRepository repository;
	private final CompositeNode dataRoot;

	public PaperRepositoryActionHandler(final PaperRepository repository, final CompositeNode dataRoot) {
		this.repository = repository;
		this.dataRoot = dataRoot;
	}

	@Override
	public boolean supportsPublish() {
		return false;
	}

	@Override
	public boolean publish(final String version) {
		return false;
	}

	@Override
	public boolean supportsDelete() {
		return false;
	}

	@Override
	public boolean delete(final String version) {
		return false;
	}

	@Override
	public boolean supportsSetCurrent() {
		return false;
	}

	@Override
	public boolean setCurrent(final String version) {
		return false;
	}

	@Override
	public boolean supportsSyncUpstream() {
		return false;
	}

	@Override
	public boolean syncUpstream() {
		return false;
	}

	@Override
	public boolean supportsRename() {
		return false;
	}

	@Override
	public boolean rename(final String oldVersion, final String newVersion) {
		return false;
	}

	@Override
	public boolean supportsRefreshLocal() {
		return true;
	}

	@Override
	public boolean refreshLocal() {
		final List<DataVersion> versions = repository.getLocalVersions();
		final String currentUUID = repository.getCurrentVersion();
		RunnerHelper.asyncExec(() -> {
			dataRoot.getChildren().clear();
			if (versions != null) {
				// Only display the n most recent versions.
				versions.stream() //
				.filter(v -> !("initial_version".equals(v.getFullIdentifier()))) //
				.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())) //
				.limit(10)//
				.forEach(v -> {
					final Node version = BrowserFactory.eINSTANCE.createLeaf();
					version.setParent(dataRoot);
					version.setDisplayName(v.getFullIdentifier());
					version.setVersionIdentifier(v.getIdentifier());
					version.setPublished(v.isPublished());
					if (currentUUID != null && v.getIdentifier().equals(currentUUID)) {
						dataRoot.setCurrent(version);
					}
					dataRoot.getChildren().add(version);
				});
			}
		});
		return true;
	}
}

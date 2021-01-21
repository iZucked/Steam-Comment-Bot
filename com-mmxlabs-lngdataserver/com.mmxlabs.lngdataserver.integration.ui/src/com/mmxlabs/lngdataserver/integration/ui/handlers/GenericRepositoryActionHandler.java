/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.handlers;

import java.util.List;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.rcp.common.RunnerHelper;

public class GenericRepositoryActionHandler implements IDataBrowserActionsHandler {

	private final IDataRepository<?> repository;
	private CompositeNode dataRoot;

	public GenericRepositoryActionHandler(IDataRepository<?> repository, CompositeNode dataRoot) {
		this.repository = repository;
		this.dataRoot = dataRoot;
	}

	@Override
	public boolean supportsPublish() {
		return false;
	}

	@Override
	public boolean publish(String version) {
		return false;
	}

	@Override
	public boolean supportsDelete() {
		return false;
	}

	@Override
	public boolean delete(String version) {
		return false;
	}

	@Override
	public boolean supportsSetCurrent() {
		return false;
	}

	@Override
	public boolean setCurrent(String version) {
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
	public boolean rename(String oldVersion, String newVersion) {
		return false;
	}

	@Override
	public boolean supportsRefreshLocal() {
		return true;
	}

	@Override
	public boolean refreshLocal() {
		List<DataVersion> versions = repository.getLocalVersions();
		RunnerHelper.asyncExec(() -> {
			dataRoot.getChildren().clear();
			if (versions != null) {
				boolean first = true;
				for (final DataVersion v : versions) {
					if ("initial_version".equals(v.getFullIdentifier())) {
						continue;
					}
					final Node version = BrowserFactory.eINSTANCE.createLeaf();
					version.setParent(dataRoot);
					version.setDisplayName(v.getFullIdentifier());
					version.setVersionIdentifier(v.getIdentifier());
					version.setPublished(v.isPublished());
					if (first) {
						dataRoot.setCurrent(version);
					}
					first = false;
					dataRoot.getChildren().add(version);
				}
			}
		});
		return true;
	}
}

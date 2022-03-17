/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.internal;

import java.util.List;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.rcp.common.RunnerHelper;

public class PricingRepositoryActionHandler implements IDataBrowserActionsHandler {

	private final PricingRepository repository;
	private final CompositeNode dataRoot;

	public PricingRepositoryActionHandler(final PricingRepository repository, final CompositeNode dataRoot) {
		this.repository = repository;
		this.dataRoot = dataRoot;
	}

	@Override
	public boolean supportsPublish() {
		return false;// repository.hasUpstream();
	}

	@Override
	public boolean publish(final String version) {
		// try {
		// repository.publishVersion(version);
		// return true;
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
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
		return false;// repository.hasUpstream();
	}

	@Override
	public boolean syncUpstream() {
		// IRunnableWithProgress r = (monitor) -> {
		// List<DataVersion> newVersions;
		// try {
		// newVersions = repository.updateAvailable();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// return;
		// }
		//
		// monitor.beginTask("Checking for pricing updates", newVersions.size());
		// try {
		// for (DataVersion version : newVersions) {
		// try {
		// repository.syncUpstreamVersion(version.getIdentifier());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// monitor.worked(1);
		// }
		// } finally {
		// monitor.done();
		// }
		// };
		// try {
		// PlatformUI.getWorkbench().getProgressService().run(true, false, r);
		// return true;
		// } catch (InvocationTargetException | InterruptedException e) {
		// e.printStackTrace();
		// }
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
		return true;// repository.isReady();
	}

	@Override
	public boolean refreshLocal() {
		final List<DataVersion> versions = repository.getLocalVersions();
		RunnerHelper.asyncExec(() -> {
			dataRoot.getChildren().clear();
			if (versions != null) {
				final boolean first = true;
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
							// if (first) {
							// dataRoot.setCurrent(version);
							// }
							// first = false;
							dataRoot.getChildren().add(version);
						});
			}
		});
		return true;
	}
}

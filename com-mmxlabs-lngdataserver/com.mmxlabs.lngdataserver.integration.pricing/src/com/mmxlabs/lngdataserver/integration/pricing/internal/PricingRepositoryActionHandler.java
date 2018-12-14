/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.internal;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.rcp.common.RunnerHelper;

public class PricingRepositoryActionHandler implements IDataBrowserActionsHandler {

	private final PricingRepository repository;
	private CompositeNode dataRoot;

	public PricingRepositoryActionHandler(PricingRepository repository, CompositeNode dataRoot) {
		this.repository = repository;
		this.dataRoot = dataRoot;
	}

	@Override
	public boolean supportsPublish() {
		return repository.hasUpstream();
	}

	@Override
	public boolean publish(String version) {
		try {
			repository.publishVersion(version);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		return repository.hasUpstream();
	}

	@Override
	public boolean syncUpstream() {
		IRunnableWithProgress r = (monitor) -> {
			List<DataVersion> newVersions;
			try {
				newVersions = repository.updateAvailable();
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}

			monitor.beginTask("Checking for pricing updates", newVersions.size());
			try {
				for (DataVersion version : newVersions) {
					try {
						repository.syncUpstreamVersion(version.getIdentifier());
					} catch (Exception e) {
						e.printStackTrace();
					}
					monitor.worked(1);
				}
			} finally {
				monitor.done();
			}
		};
		try {
			PlatformUI.getWorkbench().getProgressService().run(true, false, r);
			return true;
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
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
		return repository.isReady();
	}

	@Override
	public boolean refreshLocal() {
		List<DataVersion> versions = repository.getLocalVersions();
		RunnerHelper.asyncExec(() -> {
			dataRoot.getChildren().clear();
			if (versions != null) {
				boolean first = true;
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
//							if (first) {
//								dataRoot.setCurrent(version);
//							}
//							first = false;
							dataRoot.getChildren().add(version);
						});
			}
		});
		return true;
	}
}

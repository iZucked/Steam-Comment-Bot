package com.mmxlabs.lngdataserver.integration.pricing.internal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
		return true;
	}

	@Override
	public boolean delete(String version) {
		try {
			return repository.deleteVersion(version);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		return true;
	}

	@Override
	public boolean rename(String oldVersion, String newVersion) {
		try {
			return repository.renameVersion(oldVersion, newVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean supportsSetCurrent() {
		return true;
	}

	@Override
	public boolean setCurrent(String version) {
		try {
			return repository.setCurrentVersion(version);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean supportsRefreshLocal() {
		return repository.isReady();
	}

	@Override
	public boolean refreshLocal() {
		List<DataVersion> versions = repository.getVersions();
		RunnerHelper.asyncExec(() -> {
			dataRoot.getChildren().clear();
			if (versions != null) {
				for (final DataVersion v : versions) {
					final Node version = BrowserFactory.eINSTANCE.createLeaf();
					version.setParent(dataRoot);
					version.setDisplayName(v.getIdentifier());
					version.setPublished(v.isPublished());
					
					if (v.isCurrent()) {
						RunnerHelper.asyncExec(c -> dataRoot.setCurrent(version));
					}

					dataRoot.getChildren().add(version);
				}
			}
		});
		return true;
	}
}

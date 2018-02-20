package com.mmxlabs.lngdataserver.integration.ports.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.rcp.common.RunnerHelper;

public class PortsRepositoryActionHandler implements IDataBrowserActionsHandler {

	private final PortsRepository repository;
	private CompositeNode dataRoot;

	public PortsRepositoryActionHandler(PortsRepository repository, CompositeNode dataRoot) {
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

			monitor.beginTask("Checking for ports updates", newVersions.size());
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
		List<DataVersion> versions = repository.getVersions();
		RunnerHelper.asyncExec(() -> {
			dataRoot.getChildren().clear();
			if (versions != null) {
				boolean first = true;
				for (final DataVersion v : versions) {
					final Node version = BrowserFactory.eINSTANCE.createLeaf();
					version.setParent(dataRoot);
					version.setDisplayName(v.getIdentifier());
					version.setPublished(v.isPublished());
					if (first) {
						dataRoot.setLatest(version);
					}
					first = false;
					dataRoot.getChildren().add(version);
				}
			}
		});
		return true;
	}

}

package com.mmxlabs.lngdataserver.integration.distances;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.ApiException;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.browser.ui.DataExtension;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.integration.distances.internal.Activator;

public class DistancesDataExtension implements DataExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistancesDataExtension.class);

	@Override
	public CompositeNode getDataRoot() {
		LOGGER.debug("Distances versions for Data Browser requested");

		return Activator.getDefault().getDistancesDataRoot();
	}

	@Override
	public Consumer<String> getPublishCallback() {
		return version -> {
			try {
				Optional<Node> potential = Activator.getDefault().getDistancesDataRoot().getChildren().stream().filter(e -> e.getDisplayName().equals(version)).findFirst();
				if (potential.isPresent()) {
					Activator.getDefault().getDistanceRepository().publishVersion(version);
					potential.get().setPublished(true);
				}
			} catch (ApiException e1) {
				// TODO: what to do in this case?
				throw new RuntimeException(e1);
			}
		};
	}

	@Override
	public Runnable getRefreshUpstreamCallback() {
		return () -> {

			IRunnableWithProgress r = (monitor) -> {
				DistanceRepository local = Activator.getDefault().getDistanceRepository();
				List<DataVersion> newVersions;
				try {
					newVersions = local.updateAvailable();
				} catch (ApiException e1) {
					e1.printStackTrace();
					return;
				}

				monitor.beginTask("Checking for distance updates", newVersions.size());
				try {
					for (DataVersion version : newVersions) {
						try {
							local.syncUpstreamVersion(version.getIdentifier());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				} finally {
					monitor.done();
				}
			};
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, false, r);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		};
	}
}

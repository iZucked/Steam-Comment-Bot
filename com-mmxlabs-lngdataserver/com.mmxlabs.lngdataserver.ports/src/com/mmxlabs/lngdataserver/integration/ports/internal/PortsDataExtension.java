package com.mmxlabs.lngdataserver.integration.ports.internal;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.browser.ui.DataExtension;
import com.mmxlabs.lngdataserver.integration.ports.internal.Activator;


public class PortsDataExtension implements DataExtension {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PortsDataExtension.class);

	@Override
	public CompositeNode getDataRoot() {
		LOGGER.debug("Port versions for Data Browser requested");
		
		return Activator.getDefault().getPortsDataRoot();
	}

	@Override
	public Consumer<String> getPublishCallback() {
		return version -> {
//			try {
				Optional<Node> potential = Activator.getDefault().getPortsDataRoot().getChildren().stream().filter(e -> e.getDisplayName().equals(version)).findFirst();
				if (potential.isPresent()) {
					//Activator.getDefault().getVesselsRepository().publishVersion(version);
					potential.get().setPublished(true);
				}
//			} catch (IOException e) {
//				// TODO: what to do in error case?
//				throw new RuntimeException(e);
//			}
		};
	}
	
	@Override
	public Consumer<String> getRefreshUpstreamCallback() {
		return null;
	}
}

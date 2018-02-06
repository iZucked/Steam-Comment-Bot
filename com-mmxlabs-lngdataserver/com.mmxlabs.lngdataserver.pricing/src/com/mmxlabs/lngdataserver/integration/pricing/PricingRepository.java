package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.pricing.model.Curve;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

public class PricingRepository {
	private static final Logger LOG = LoggerFactory.getLogger(PricingRepository.class);
	
	private String backendUrl;
	private boolean listenForNewVersions;
	private final List<Consumer<String>> newVersionCallbacks = new LinkedList<Consumer<String>>();
	
	public PricingRepository() {
		// try to get ready
		isReady(); 
	}
	
	public PricingRepository(String backendUrl) {
		this.backendUrl = backendUrl;
	}
	
	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		}else if(BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			return true;
		}else {
			return false;
		}
	}
	
	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Pricing back-end not ready yet");
		}
	}
	
	public List<PricingVersion> getVersions() throws IOException {
		ensureReady();
		return PricingClient.getVersions(backendUrl);
	}
	
	public void publishVersion(String version) throws IOException {
		PricingClient.publishVersion(version, backendUrl);
	}
	
	public void registerVersionListener(Consumer<String> versionConsumer) {
		newVersionCallbacks.add(versionConsumer);
	}
	
	public void listenForNewVersions() {
		listenForNewVersions = true;
		
		new Thread(() ->  {
			while(listenForNewVersions) {
				CompletableFuture<String> newVersion = PricingClient.notifyOnNewVersion(backendUrl);
				try {
					String version = newVersion.get();
					newVersionCallbacks.forEach(c -> c.accept(version));
				} catch (InterruptedException e) {
					LOG.error(e.getMessage());
				} catch (ExecutionException e) {
					LOG.error(e.getMessage());
				}
				
				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}
	
	public void stopListenForNewVersion() {
		listenForNewVersions = false;
	}

	public IPricingProvider getLatestPrices() throws IOException {
		ensureReady();
		return getPricingProvider(getVersions().get(0).getIdentifier());
	}

	public IPricingProvider getPricingProvider(String version) throws IOException {
		ensureReady();
		
		List<Curve> commoditiesCurves = PricingClient.getCommodityCurves(backendUrl, version);
		
		List<Curve> baseFuelCurves = PricingClient.getFuelCurves(backendUrl, version);
		
		List<Curve> charterCurves = PricingClient.getCharterCurves(backendUrl, version);
		
		List<Curve> currencyCurves = PricingClient.getCurrencyCurves(backendUrl, version);
		
		return new ForwardingPricingProvider(backendUrl, version, commoditiesCurves, charterCurves, baseFuelCurves, currencyCurves);
	}
}

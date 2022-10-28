/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.license.ssl.internal;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.eclipse.ecf.filetransfer.service.IRetrieveFileTransfer;
import org.eclipse.ecf.filetransfer.service.IRetrieveFileTransferFactory;
import org.eclipse.ecf.provider.filetransfer.httpclient5.HttpClientRetrieveFileTransfer;

import com.mmxlabs.license.ssl.LicenseManager;
import com.mmxlabs.license.ssl.TrustStoreManager;

/**
 * Copy and paste from ECF so we can customise the https socket factory to use the system properties.
 * 
 * @author Simon Goodall
 * 
 */
public class HttpClientRetrieveFileTransferFactory implements IRetrieveFileTransferFactory {

	@Override
	public IRetrieveFileTransfer newInstance() {

		final HttpClientBuilder builder = HttpClientBuilder.create();
		final var sslBuilder = SSLContexts.custom();
		try {
			LicenseManager.loadLicenseKeystore(sslBuilder);
		} catch (final Exception e1) {
			e1.printStackTrace();
		}

		final boolean useJavaTruststore = false;
		final boolean useWindowsTruststore = true;

		try {
			final KeyStore p = TrustStoreManager.loadTruststore(true, useJavaTruststore, useWindowsTruststore);
			if (p != null) {
				sslBuilder.loadTrustMaterial(p, null);
			}

			final SSLContext sslContext = sslBuilder.build();
			var sslFactoryBuilder = SSLConnectionSocketFactoryBuilder.create();
			sslFactoryBuilder.setSslContext(sslContext);

			final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create() //
					.setSSLSocketFactory(sslFactoryBuilder.build()) //
					.build();

			builder.setConnectionManager(cm);
			builder.evictExpiredConnections();

		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		// HttpHost httpHost = URIUtils.extractHost(url.toURI());
		// configureProxyServer(httpHost, builder);

		return new HttpClientRetrieveFileTransfer(builder.build());
	}
}

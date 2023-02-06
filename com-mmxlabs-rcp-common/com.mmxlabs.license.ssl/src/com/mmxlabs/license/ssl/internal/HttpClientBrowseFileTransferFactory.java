/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.license.ssl.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.util.Proxy;
import org.eclipse.ecf.filetransfer.IRemoteFileSystemListener;
import org.eclipse.ecf.filetransfer.IRemoteFileSystemRequest;
import org.eclipse.ecf.filetransfer.RemoteFileSystemException;
import org.eclipse.ecf.filetransfer.identity.IFileID;
import org.eclipse.ecf.filetransfer.service.IRemoteFileSystemBrowser;
import org.eclipse.ecf.filetransfer.service.IRemoteFileSystemBrowserFactory;
import org.eclipse.ecf.provider.filetransfer.httpclient45.HttpClientFileSystemBrowser;
import org.eclipse.ecf.provider.filetransfer.identity.FileTransferNamespace;
import org.eclipse.osgi.util.NLS;

import com.mmxlabs.license.ssl.LicenseManager;
import com.mmxlabs.license.ssl.TrustStoreManager;

/**
 * Copy and paste from ECF so we can customise the https socket factory to use the system properties.
 * 
 * @author Simon Goodall
 * 
 */
@SuppressWarnings({ "deprecation", "restriction" })
public class HttpClientBrowseFileTransferFactory implements IRemoteFileSystemBrowserFactory {

	@Override
	public IRemoteFileSystemBrowser newInstance() {
		return new IRemoteFileSystemBrowser() {

			private Proxy proxy;
			private IConnectContext connectContext;

			@Override
			public Namespace getBrowseNamespace() {
				return IDFactory.getDefault().getNamespaceByName(FileTransferNamespace.PROTOCOL);
			}

			@Override
			public IRemoteFileSystemRequest sendBrowseRequest(IFileID directoryOrFileId, IRemoteFileSystemListener listener) throws RemoteFileSystemException {
				Assert.isNotNull(directoryOrFileId);
				Assert.isNotNull(listener);
				URL url;
				try {
					url = directoryOrFileId.getURL();
				} catch (final MalformedURLException e) {
					throw new RemoteFileSystemException(NLS.bind("Exception creating URL for {0}", directoryOrFileId)); //$NON-NLS-1$
				}

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
					builder.setSSLContext(sslContext);

				} catch (final Exception e1) {
					e1.printStackTrace();
				}
				// HttpHost httpHost = URIUtils.extractHost(url.toURI());
				// configureProxyServer(httpHost, builder);

				HttpClientFileSystemBrowser browser = new HttpClientFileSystemBrowser(builder.build(), directoryOrFileId, listener, url, connectContext, proxy);
				return browser.sendBrowseRequest();
			}

			@Override
			public void setConnectContextForAuthentication(IConnectContext connectContext) {
				this.connectContext = connectContext;
			}

			@Override
			public void setProxy(Proxy proxy) {
				this.proxy = proxy;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public <T> T getAdapter(Class<T> adapter) {
				return (T) null;
			}

		};

	}
}

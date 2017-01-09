/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.license.ssl.internal;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.eclipse.ecf.filetransfer.service.IRetrieveFileTransfer;
import org.eclipse.ecf.filetransfer.service.IRetrieveFileTransferFactory;
import org.eclipse.ecf.provider.filetransfer.httpclient4.HttpClientRetrieveFileTransfer;

/**
 * Copy and paste from ECF so we can customise the https socket factory to use the system properties.
 * 
 * @author Simon Goodall
 * 
 */
@SuppressWarnings({ "deprecation", "restriction" })
public class HttpClientRetrieveFileTransferFactory implements IRetrieveFileTransferFactory {

	@Override
	public IRetrieveFileTransfer newInstance() {

		SSLSocketFactory factory = new SSLSocketFactory(SSLContexts.createSystemDefault(), SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

		final SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		registry.register(new Scheme("https", 443, factory));

		return new HttpClientRetrieveFileTransfer(new DefaultHttpClient(new SingleClientConnManager(registry)));
	}
}

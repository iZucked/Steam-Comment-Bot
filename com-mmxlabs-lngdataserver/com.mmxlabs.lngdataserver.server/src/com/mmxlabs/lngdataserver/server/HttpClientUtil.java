package com.mmxlabs.lngdataserver.server;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.ssl.LicenseChecker;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HttpClientUtil {

	public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType MEDIA_TYPE_FORM_DATA = MediaType.parse("application/x-www-form-urlencoded");

	private static final OkHttpClient CLIENT = new OkHttpClient.Builder().build();

	public static OkHttpClient.Builder basicBuilder() {
		final OkHttpClient.Builder builder = CLIENT.newBuilder();
		try {

			final Pair<KeyStore, char[]> keyStorePair = LicenseChecker.loadLocalKeystore();
			final KeyStore keyStore = keyStorePair.getFirst();

			final SSLContext sslContext = SSLContext.getInstance("TLS");

			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);

			final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, keyStorePair.getSecond());
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

			final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			// All examples show first entry to be the X509 one...
			builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0]);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return builder;
	}
}

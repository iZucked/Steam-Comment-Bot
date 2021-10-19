/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub.common.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.ssl.LicenseChecker;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Handshake;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.internal.tls.OkHostnameVerifier;

public class HttpClientUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

	public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType MEDIA_TYPE_FORM_DATA = MediaType.parse("application/x-www-form-urlencoded");

	private static boolean disableSSLHostnameCheck = true;

	public static void setDisableSSLHostnameCheck(final boolean b) {
		disableSSLHostnameCheck = b;
	}

	private static final OkHttpClient CLIENT = new OkHttpClient.Builder().build();

	public static OkHttpClient.Builder basicBuilder() {
		// Set generous timeouts. Library defaults are 10 seconds
		return basicBuilder(20, 1, 1);
	}

	public static OkHttpClient.Builder basicBuilder(final int connectTimeout, final int readTimeout, final int writeTimeout) {
		final OkHttpClient.Builder builder = CLIENT.newBuilder();

		builder.connectTimeout(connectTimeout, TimeUnit.SECONDS) //
				.readTimeout(readTimeout, TimeUnit.MINUTES) //
				.writeTimeout(writeTimeout, TimeUnit.MINUTES) //
		;

		builder.hostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(final String hostname, final SSLSession session) {

				if (!disableSSLHostnameCheck) {
					return OkHostnameVerifier.INSTANCE.verify(hostname, session);
				}
				return true;
			}
		});

		try {

			final Pair<KeyStore, char[]> keyStorePair = LicenseChecker.loadLocalKeystore();
			if (keyStorePair != null) {
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
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return builder;
	}

	public static List<CertInfo> extractSSLInfoFromLocalStore() throws Exception {

		final List<CertInfo> infos = new LinkedList<>();
		{
			final String userHome = System.getProperty("user.home");
			if (userHome != null) {
				final File f = new File(userHome + "/mmxlabs/cacerts/");
				if (f.exists() && f.isDirectory()) {
					for (final File certFile : f.listFiles()) {
						if (certFile.isFile()) {
							try (FileInputStream inStream = new FileInputStream(certFile)) {
								final CertificateFactory factory = CertificateFactory.getInstance("X.509");
								final X509Certificate cert = (X509Certificate) factory.generateCertificate(inStream);
								final CertInfo info = CertInfo.from(cert);
								info.filename = certFile.getAbsolutePath();
								infos.add(info);
							} catch (final Exception e) {
								LOGGER.error("Unable to load certificate " + f.getAbsolutePath(), e);
							}
						}
					}
				}
			}
		}
		{

			final String userHome = System.getProperty("eclipse.home.location");

			try {
				File f = LicenseChecker.getCACertsFileFromEclipseHomeURL(userHome);
				if (f != null && f.exists() && f.isDirectory()) {
					for (final File certFile : f.listFiles()) {
						if (certFile.isFile()) {
							try (FileInputStream inStream = new FileInputStream(certFile)) {
								final CertificateFactory factory = CertificateFactory.getInstance("X.509");
								final X509Certificate cert = (X509Certificate) factory.generateCertificate(inStream);
								final CertInfo info = CertInfo.from(cert);
								info.filename = certFile.getAbsolutePath();
								infos.add(info);
							} catch (final Exception e) {
								LOGGER.error("Unable to read certificate " + f.getAbsolutePath(), e);
							}
						}
					}
				}
			} catch (final URISyntaxException e1) {
				// Ignore
			}
		}
		return infos;
	}

	public static List<CertInfo> extractSSLInfoFromHost(final String url) throws Exception {

		if (url.startsWith("http:")) {
			return Collections.emptyList();
		}

		final List<CertInfo> infos = new LinkedList<>();
		final String patternWithPort = "http[s]*://([a-zA-Z0-9\\.-_]*)(:[0-9]+)[//]*";
		final String pattern = "http[s]*://([a-zA-Z0-9\\.-_]*)[//]*";
		Matcher matcher = Pattern.compile(patternWithPort).matcher(url);
		if (!matcher.matches()) {
			matcher = Pattern.compile(pattern).matcher(url);
		}
		if (matcher.matches()) {
			final String hostStr = matcher.group(1);
			final String portStr = matcher.groupCount() > 1 ? matcher.group(2) : null;

			final Pair<KeyStore, char[]> keyStorePair = LicenseChecker.loadLocalKeystore();
			final KeyStore keyStore = keyStorePair.getFirst();

			final SSLContext sslContext = SSLContext.getInstance("TLS");

			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);

			final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, keyStorePair.getSecond());
			sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { new TrustingTrustManager() }, new SecureRandom());

			final SSLSocketFactory socketFactory = sslContext.getSocketFactory();

			final int port = portStr == null ? 443 : Integer.parseInt(portStr.substring(1));

			try (SSLSocket createSocket = (SSLSocket) socketFactory.createSocket(hostStr, port)) {
				createSocket.startHandshake();
				final SSLSession session = createSocket.getSession();
				for (final java.security.cert.Certificate c : session.getPeerCertificates()) {
					final X509Certificate cert = (X509Certificate) c;
					infos.add(CertInfo.from(cert));
				}
			}
		}
		return infos;
	}

	public static class CertInfo {
		public String filename;
		public String subject;
		public String altNames;
		public String issuer;
		public String key;
		public String serial;
		public String validity;
		public String thumbprint;

		public static CertInfo from(final X509Certificate cert) {
			final CertInfo certInfo = new CertInfo();
			certInfo.subject = cert.getSubjectDN().toString();
			certInfo.serial = cert.getSerialNumber().toString();
			certInfo.issuer = cert.getIssuerDN().toString();
			try {
				certInfo.altNames = cert.getSubjectAlternativeNames() == null ? ""
						: cert.getSubjectAlternativeNames().stream() //
								.map(Object::toString).collect(Collectors.joining(", "));
			} catch (final CertificateParsingException e) {
				e.printStackTrace();
			}
			try {
				certInfo.thumbprint = bytesToHex(MessageDigest.getInstance("SHA-1").digest(cert.getEncoded()));
			} catch (CertificateEncodingException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			certInfo.key = cert.getSigAlgName();
			certInfo.validity = String.format("Valid from %s to %s", cert.getNotBefore(), cert.getNotAfter());
			return certInfo;
		}

		private static final char[] hexArray = "0123456789abcdef".toCharArray();

		private static String bytesToHex(final byte[] bytes) {
			final char[] hexChars = new char[bytes.length * 2];
			for (int j = 0; j < bytes.length; j++) {
				final int v = bytes[j] & 0xFF;
				hexChars[j * 2] = hexArray[v >>> 4];
				hexChars[j * 2 + 1] = hexArray[v & 0x0F];
			}
			return new String(hexChars);
		}

		@Override
		public @NonNull String toString() {
			final StringBuilder sb = new StringBuilder();

			if (this.filename != null) {
				sb.append("Filename: " + this.filename + "\n");
			}
			sb.append("Subject: " + this.subject + "\n");
			sb.append("Issuer: " + this.issuer + "\n");
			sb.append("Serial: " + this.serial + "\n");
			sb.append("Alt names: " + this.altNames + "\n");
			sb.append("Key: " + this.key + "\n");
			sb.append("Validity: " + this.validity + "\n");
			sb.append("Thumbprint: " + this.thumbprint + "\n");
			sb.append("\n");

			return sb.toString();
		}

	}

	/**
	 * Trust manager which trusts everything. Only use for certificate checking
	 *
	 */
	private static class TrustingTrustManager implements X509TrustManager {

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {

		}

		public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {

		}
	}

	public static boolean extractSSLCompatibilityFromHost(final String url, final BiConsumer<TlsVersion, CipherSuite> action) throws Exception {

		if (!url.startsWith("https://")) {
			return false;
		}

		// Modern is the default in okhttp.
		for (final ConnectionSpec spec : new ConnectionSpec[] { ConnectionSpec.MODERN_TLS }) {

			for (final TlsVersion tlsVersion : spec.tlsVersions()) {
				for (final CipherSuite cipherSuite : spec.cipherSuites()) {
					if (checkCompatibility(url, spec, tlsVersion, cipherSuite)) {
						action.accept(tlsVersion, cipherSuite);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Method to extract the selected protocol and cipher for the request.
	 * 
	 * @param url
	 * @param selectedTlsVersion Output - expected length = 1
	 * @param selectedCipher     Output - expected length = 1
	 * @return
	 */
	public static boolean getSelectedProtocolAndVersion(final String url, final TlsVersion[] selectedTlsVersion, final CipherSuite[] selectedCipher) {

		final OkHttpClient.Builder builder = CLIENT.newBuilder();

		// Ignore any hostname issues here
		builder.hostnameVerifier((hostname, session) -> Boolean.TRUE);

		builder.addInterceptor(new Interceptor() {

			@Override
			public Response intercept(final Chain chain) throws IOException {
				final Response response = chain.proceed(chain.request());
				if (response != null) {
					final Handshake handshake = response.handshake();
					if (handshake != null) {
						selectedCipher[0] = handshake.cipherSuite();
						selectedTlsVersion[0] = handshake.tlsVersion();
					}
				}
				return response;
			}
		});

		final OkHttpClient client = builder.build();
		final Request request = new Request.Builder().url(url).get().build();

		try (Response response = client.newCall(request).execute()) {
			return true;
		} catch (final Exception e) {
			// Log.e("error", e.toString());
		}
		return false;
	}

	private static boolean checkCompatibility(final String url, final ConnectionSpec connectionSpec, final TlsVersion tlsVersion, final CipherSuite cipherSuite) {

		final OkHttpClient.Builder builder = basicBuilder();

		// Ignore any hostname issues here
		builder.hostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(final String hostname, final SSLSession session) {
				return true;
			}
		});

		final ConnectionSpec spec = new ConnectionSpec.Builder(connectionSpec) //
				.tlsVersions(tlsVersion) //
				.cipherSuites(cipherSuite) //
				.build();
		builder.connectionSpecs(Collections.singletonList(spec));

		final Request request = new Request.Builder().url(url).get().build();

		final OkHttpClient client = builder.build();
		try (Response response = client.newCall(request).execute()) {
			return true;
		} catch (final Exception e) {
			// e.printStackTrace();
		}
		return false;
	}
}

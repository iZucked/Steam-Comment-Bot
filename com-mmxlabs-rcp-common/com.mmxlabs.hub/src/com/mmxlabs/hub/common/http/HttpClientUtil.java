/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.common.http;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.ssl.SSLContexts;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.ssl.TrustStoreManager;

public class HttpClientUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
//

	private static boolean disableSSLHostnameCheck = true;

	public static void setDisableSSLHostnameCheck(final boolean b) {
		disableSSLHostnameCheck = b;
	}

//
//	public static OkHttpClient.Builder basicBuilder() {
//		// Set generous timeouts. Library defaults are 10 seconds
//		return basicBuilder(20, 1, 1);
//	}
//

	public static CloseableHttpClient createBasicHttpClient(HttpRequestBase request) {
		return createBasicHttpClient(URIUtils.extractHost(request.getURI()));
	}

	public static CloseableHttpClient createBasicHttpClient(HttpHost httpHost) {

		final HttpClientBuilder builder = HttpClientBuilder.create();

		var sslBuilder = SSLContexts.custom();
//			if (url.getHost().equals("updates.minimaxlabs.com")) {
//				try {
//					LicenseManager.loadLicenseKeystore(sslBuilder);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
		try {
			Pair<KeyStore, char[]> p = TrustStoreManager.loadLocalTruststore();
			if (p != null) {
				sslBuilder.loadTrustMaterial(p.getFirst(), null);
			}
			builder.setSSLContext(sslBuilder.build());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		configureProxyServer(httpHost, builder);

		return builder.build();
	}

	private static void configureProxyServer(HttpHost httpHost, final HttpClientBuilder builder) {
		final Bundle bundle = FrameworkUtil.getBundle(HttpClientUtil.class);
		final ServiceTracker<IProxyService, IProxyService> proxyTracker = new ServiceTracker<>(bundle.getBundleContext(), IProxyService.class.getName(), null);
		proxyTracker.open();
		try {
			final IProxyService service = proxyTracker.getService();
			if (service != null && service.isProxiesEnabled()) {
				final IProxyData[] data = service.select(new URI(httpHost.toURI()));
				if (data != null && data.length > 0) {
					for (final var pd : data) {
						if (Objects.equals(pd.getType(), IProxyData.HTTPS_PROXY_TYPE)) {
							HttpHost proxy = new HttpHost(pd.getHost(), pd.getPort());
							DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
							builder.setRoutePlanner(routePlanner);
						}
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			proxyTracker.close();
		}
	}

//	public static OkHttpClient.Builder basicBuilder(final int connectTimeout, final int readTimeout, final int writeTimeout) {
//		final OkHttpClient.Builder builder = CLIENT.newBuilder();
//
//		builder.connectTimeout(connectTimeout, TimeUnit.SECONDS) //
//				.readTimeout(readTimeout, TimeUnit.MINUTES) //
//				.writeTimeout(writeTimeout, TimeUnit.MINUTES) //
//		;
//
//		builder.hostnameVerifier(new HostnameVerifier() {
//
//			@Override
//			public boolean verify(final String hostname, final SSLSession session) {
//
//				if (!disableSSLHostnameCheck) {
//					return OkHostnameVerifier.INSTANCE.verify(hostname, session);
//				}
//				return true;
//			}
//		});
//
//		try {
//			TrustStoreManager.refresh();
//			final Pair<KeyStore, char[]> trustStorePair = TrustStoreManager.loadLocalTruststore();
//			final Pair<KeyStore, char[]> keyStorePair = TrustStoreManager.loadLocalKeystore();
//
//			if (trustStorePair != null && keyStorePair != null) {
//
//				final SSLContext sslContext = SSLContext.getInstance("TLS");
//
//				final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//				trustManagerFactory.init(trustStorePair.getFirst());
//
//				final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//				keyManagerFactory.init(keyStorePair.getFirst(), keyStorePair.getSecond());
//				sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
//
//				final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//				// All examples show first entry to be the X509 one...
//				builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0]);
//			}
//		} catch (final Exception e) {
//			e.printStackTrace();
//		}
//		return builder;
//	}

	public static List<CertInfo> extractSSLInfoFromLocalTrustStore() throws Exception {

		final List<CertInfo> infos = new LinkedList<>();
		{
			final Pair<KeyStore, char[]> trustStorePair = TrustStoreManager.loadLocalTruststore();

			if (trustStorePair != null) {
				KeyStore defaultStore = trustStorePair.getFirst();
				final Enumeration<String> enumerator = defaultStore.aliases();
				while (enumerator.hasMoreElements()) {
					final String alias = enumerator.nextElement();

					final X509Certificate cert = (X509Certificate) defaultStore.getCertificate(alias);
					final CertInfo info = CertInfo.from(cert);
					infos.add(info);
				}
			}
		}

		return infos;
	}

	public static List<CertInfo> extractSSLInfoFromWindowsTrustStore() throws Exception {

		final List<CertInfo> infos = new LinkedList<>();

		final KeyStore defaultStore = KeyStore.getInstance("Windows-ROOT");
		defaultStore.load(null);

		final Enumeration<String> enumerator = defaultStore.aliases();
		while (enumerator.hasMoreElements()) {
			final String alias = enumerator.nextElement();

			final X509Certificate cert = (X509Certificate) defaultStore.getCertificate(alias);
			final CertInfo info = CertInfo.from(cert);
			infos.add(info);
		}

		return infos;
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
				File f = TrustStoreManager.getCACertsFileFromEclipseHomeURL(userHome);
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

	public static List<CertInfo> extractSSLInfoFromHost(final String url, @Nullable Proxy proxy) throws Exception {

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

			final Pair<KeyStore, char[]> keyStorePair = TrustStoreManager.loadLocalTruststore();
			final KeyStore keyStore = keyStorePair.getFirst();

			final SSLContext sslContext = SSLContext.getInstance("TLS");

			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);

			final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, keyStorePair.getSecond());
			sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { new TrustingTrustManager() }, new SecureRandom());

			final SSLSocketFactory socketFactory = sslContext.getSocketFactory();

			final int port = portStr == null ? 443 : Integer.parseInt(portStr.substring(1));

			if (proxy != null) {
				try (Socket socket = new Socket(proxy)) {
					socket.connect(new InetSocketAddress(hostStr, port));
					try (SSLSocket createSocket = (SSLSocket) socketFactory.createSocket(socket, hostStr, port, true)) {
						createSocket.startHandshake();
						final SSLSession session = createSocket.getSession();
						for (final java.security.cert.Certificate c : session.getPeerCertificates()) {
							final X509Certificate cert = (X509Certificate) c;
							infos.add(CertInfo.from(cert));
						}
					}
				}
			} else {
				try (SSLSocket createSocket = (SSLSocket) socketFactory.createSocket(hostStr, port)) {
					createSocket.startHandshake();
					final SSLSession session = createSocket.getSession();
					for (final java.security.cert.Certificate c : session.getPeerCertificates()) {
						final X509Certificate cert = (X509Certificate) c;
						infos.add(CertInfo.from(cert));
					}
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
			certInfo.subject = cert.getSubjectX500Principal().toString();
			certInfo.serial = byteToString(cert.getSerialNumber().toByteArray(), ":");
			certInfo.issuer = cert.getIssuerX500Principal().toString();
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

		public static String byteToString(final byte[] bytes, final String separator) {

			final StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (final byte b : bytes) {
				if (first) {
					first = false;
				} else {
					sb.append(separator);
				}
				sb.append(String.format("%02X", b));
			}
			return sb.toString();
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

	public static boolean extractSSLCompatibilityFromHost(final String url, final BiConsumer<String, String> action) throws Exception {

		if (!url.startsWith("https://")) {
			return false;
		}

		var spec = SSLContexts.createSystemDefault().getSupportedSSLParameters();
//		SSLFaConnectionSocketFactory f = SSLConnectionSocketFactory.getSystemSocketFactory();
//		// Modern is the default in okhttp.
//		for (final ConnectionSpec spec : new ConnectionSpec[] { ConnectionSpec.MODERN_TLS }) {
//SSLContext c;//.getInstance(url, null)
//org.apache.http.conn.ssl.SSLContexts.createDefault().getSupportedSSLParameters().getCipherSuites()
			for (final String tlsVersion : spec.getProtocols()) {
				for (final String cipherSuite : spec.getCipherSuites()) {
					if (checkCompatibility(url, null, tlsVersion, cipherSuite)) {
						action.accept(tlsVersion, cipherSuite);
					}
				}
			}
//		}
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
	public static boolean getSelectedProtocolAndVersion(final String url, final String[] selectedTlsVersion, final String[] selectedCipher) {

//		TODO -- procy!
		
		HttpClientBuilder builder = HttpClientBuilder.create();
		// Ignore any hostname issues here
		builder.setHostnameVerifier(AllowAllHostnameVerifier.INSTANCE);
//		

		SSLContext sslContext = SSLContexts.createSystemDefault();

		builder.setSslcontext(sslContext);

		final HttpGet request = new HttpGet(url);
		
		configureProxyServer(URIUtils.extractHost(request.getURI()), builder);

		try (CloseableHttpClient client = builder.build()) {
			try (var response = client.execute(request)) {

				SSLSessionContext sslSessionContext = sslContext.getClientSessionContext();
				Enumeration<byte[]> sessionIds = sslSessionContext.getIds();
				while (sessionIds.hasMoreElements()) {
					SSLSession sslSession = sslSessionContext.getSession(sessionIds.nextElement());
//			            print("Client: " + sslSession.getPeerHost() + ":" + sslSession.getPeerPort());
//			            print("\tProtocol: " + sslSession.getProtocol());
					selectedTlsVersion[0] = sslSession.getProtocol();
//			            print("\tSessionID: " + byteArrayToHex(sslSession.getId()));
					selectedCipher[0] = sslSession.getCipherSuite();
//			            for (X509Certificate certificate : sslSession.getPeerCertificateChain()) {
//			                print("\tX509 Certificate: " + certificate.getSubjectDN());
//			                print("\t\tIssuer: " + certificate.getIssuerDN().getName());
//			                print("\t\tAlgorithm: " + certificate.getSigAlgName());
//			                print("\t\tValidity: " + certificate.getNotAfter());
//			            }
				}

				return true;
			}
		} catch (final Exception e) {
			// Log.e("error", e.toString());
		}
		return false;
	}

	private static boolean checkCompatibility(final String url, final String connectionSpec, final String tlsVersion, final String cipherSuite) {
		
		
//		TODO -- procy!
		
		try {
		
		HttpClientBuilder builder = HttpClientBuilder.create();
		// Ignore any hostname issues here
		builder.setHostnameVerifier(AllowAllHostnameVerifier.INSTANCE);
//		
//
		SSLContext sslContext = SSLContexts.custom()
				.loadTrustMaterial(TrustAllStrategy.INSTANCE)
				.build();
//				.us
//				
//				createSystemDefault();


SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(
    sslContext,
    new String[]{tlsVersion},   
    new String[]{cipherSuite},   
    AllowAllHostnameVerifier.INSTANCE);

//		sslContext.s
		builder.setSslcontext(sslContext);
		builder.setSSLSocketFactory(f);

		final HttpGet request = new HttpGet(url);
		configureProxyServer(URIUtils.extractHost(request.getURI()), builder);
		
		try (CloseableHttpClient client = builder.build()) {
			try (var response = client.execute(request)) {
//
//				SSLSessionContext sslSessionContext = sslContext.getClientSessionContext();
//				Enumeration<byte[]> sessionIds = sslSessionContext.getIds();
//				while (sessionIds.hasMoreElements()) {
//					SSLSession sslSession = sslSessionContext.getSession(sessionIds.nextElement());
////			            print("Client: " + sslSession.getPeerHost() + ":" + sslSession.getPeerPort());
////			            print("\tProtocol: " + sslSession.getProtocol());
//					selectedTlsVersion[0] = sslSession.getProtocol();
////			            print("\tSessionID: " + byteArrayToHex(sslSession.getId()));
//					selectedCipher[0] = sslSession.getCipherSuite();
////			            for (X509Certificate certificate : sslSession.getPeerCertificateChain()) {
////			                print("\tX509 Certificate: " + certificate.getSubjectDN());
////			                print("\t\tIssuer: " + certificate.getIssuerDN().getName());
////			                print("\t\tAlgorithm: " + certificate.getSigAlgName());
////			                print("\t\tValidity: " + certificate.getNotAfter());
////			            }
				return true;
				}
		}
		} catch (final Exception e) {
			// Log.e("error", e.toString());
		}
//
//		final ConnectionSpec spec = new ConnectionSpec.Builder(connectionSpec) //
//				.tlsVersions(tlsVersion) //
//				.cipherSuites(cipherSuite) //
//				.build();
//		builder.connectionSpecs(Collections.singletonList(spec));
//
//		final Request request = new Request.Builder().url(url).get().build();
//
//		final OkHttpClient client = builder.build();
//		try (Response response = client.newCall(request).execute()) {
//			return true;
//		} catch (final Exception e) {
//			// e.printStackTrace();
//		}
		return false;
	} 

	public static boolean isSuccessful(int code) {
		return code >= 200 && code < 400;
	}

	public static String basicAuthHeader(final @NonNull String username, final @NonNull String password) {
		final byte[] encodedAuth = Base64.getEncoder().encode(String.format("%s:%s", username, password).getBytes(StandardCharsets.UTF_8));
		return "Basic " + new String(encodedAuth);
	}

	public static @Nullable String getHeaderValue(final @NonNull HttpResponse response, final @NonNull String name) {
		final Header header = response.getLastHeader(name);
		if (header != null) {
			return header.getValue();
		}
		return null;
	}
}

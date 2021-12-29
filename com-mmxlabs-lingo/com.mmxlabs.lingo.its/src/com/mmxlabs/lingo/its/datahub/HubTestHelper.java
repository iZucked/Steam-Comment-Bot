/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;

public class HubTestHelper {

	static final Logger logger = LoggerFactory.getLogger(HubTestHelper.class);
	public static final String HOST = "localhost";

	public static void configureHub(String url, boolean enableBaseCase, boolean enableTeam) {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, url);
	}

	public static void asStandardUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_users", "u_users");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_basecase", "u_basecase");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asSuperUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_full", "u_full");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asAlternativeStandardUser() {
		BasicAuthenticationManager.getInstance().withCredentials("simon", "simon");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void asAlternativeBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("simon", "simon");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void setDatahubUrl(String upstreamUrl) {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, upstreamUrl);
	}

	/**
	 * Wait until a port from the portPool is available and return it. If no ports are available before the timeout duration is reached return -1
	 *
	 * @param portPool
	 * @param timeout
	 * @return port number or -1 if no ports are available
	 */
	public static int waitForAvailablePort(List<Integer> portPool, int timeout) {
		int availablePort = -1;
		long start = System.currentTimeMillis();
		long end = start + timeout;
		while (availablePort == -1 && System.currentTimeMillis() < end) {
			availablePort = getAvailablePort(portPool);
		}
		return availablePort;
	}

	/**
	 * Check if any port from a port pool is available
	 *
	 * @param portPool
	 * @return port number or -1 if no ports from the pool are available
	 */
	public static int getAvailablePort(List<Integer> portPool) {
		Optional<Integer> availablePort;
		// @formatter:off
		availablePort = portPool.stream()
				.filter(port -> isTcpPortAvailable(HOST, port))
				.findAny();
		// @formatter:on
		if (availablePort.isPresent()) {
			logger.info(String.format("Port %s is available", availablePort.get()));
			return availablePort.get();
		} else {
			logger.info("There are no available ports at the moment...");
			return -1;
		}
	}

	/**
	 * Check to see if a tcp port is available by trying to open it. Note: this is vulnerable to race conditions.
	 *
	 * @param host
	 * @param port
	 * @return true if the port is available and false otherwise. Could also return false if the bind operation fails
	 */
	public static boolean isTcpPortAvailable(String host, int port) {
		try (ServerSocket socket = new ServerSocket()) {
			socket.bind(new InetSocketAddress(InetAddress.getByName(host), port));
			return true;
		} catch (IOException ex) {
			logger.info(String.format("socket of port %s is already bound", port));
			return false;
		}
	}


	// various html element names
	public static String otherTile = "otherTile";
	public static String emailInputName = "loginfmt";
	public static String passwordInputName = "passwd";
	public static String nextButtonId = "idSIButton9";
	public static String noButtonId = "idBtn_Back";

	// test credentials
	public static String email = "test@minimaxlabs.com";
	// TODO update
	public static String password = "xUPffZ76Ef^X28YqQQC%ZyVr#";
	// javascript actions executed in the browser - tested in IE and Firefox
	public static String clickUserAnotherAccount = "document.getElementById('" + otherTile + "').click();";
	public static String fillEmail = "document.getElementsByName('" + emailInputName + "')[0].value = '" + email + "';";
	public static String focusEmail = "var email = document.getElementsByName('" + emailInputName
			+ "')[0]; var inputEvent = document.createEvent('KeyboardEvent'); inputEvent.initEvent('input', true, false, null, false, false, false, false, 0, 0); email.dispatchEvent(inputEvent);";
	public static String clickNext = "document.getElementById('" + nextButtonId + "').click();";
	public static String fillPassword = "document.getElementsByName('" + passwordInputName + "')[0].value = '" + password + "';";
	public static String focusPassword = "var password = document.getElementsByName('" + passwordInputName
			+ "')[0]; var inputEvent2 = document.createEvent('KeyboardEvent'); inputEvent2.initEvent('input', true, false, null, false, false, false, false, 0, 0); password.dispatchEvent(inputEvent2);";

	// check if the user is signed in
	public static String userIsSignedIn = "document.querySelector('div[data-test-id=" + email + "]') !== null ? document.querySelector('div[data-test-id=" + email + "]').click() : false;";
	public static String clickSignedIn = "document.querySelector('div[data-test-id=" + email + "]').click();";
	public static String clickNo = "document.getElementById('" + noButtonId + "').click();";
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import static okhttp3.ws.WebSocket.TEXT;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

public class UpstreamNotificationService implements WebSocketListener {

	private OkHttpClient client = HttpClientUtil.basicBuilder().readTimeout(0, TimeUnit.MILLISECONDS).build();
	private Map<String, List<Consumer<String>>> handlers = new ConcurrentHashMap<>();
	private WebSocketCall webSocketCall;

	public void registerHook(String topic, Consumer<String> handler) {
		handlers.computeIfAbsent(topic, k -> new LinkedList<>()).add(handler);
	}

	public void removeHook(String topic, Consumer<String> handler) {
		handlers.computeIfAbsent(topic, k -> new LinkedList<>()).remove(handler);

	}

	public UpstreamNotificationService() {

	}

	public synchronized void reconfigure(String url) {
		if (this.webSocketCall != null) {
			this.webSocketCall.cancel();
			this.webSocketCall = null;
		}
		if (url != null) {
			String wsURL = url.replaceFirst("http", "ws") + "/socket";

			Request request = new Request.Builder().url(wsURL).build();
			webSocketCall = WebSocketCall.create(client, request);
			webSocketCall.cancel();
			webSocketCall.enqueue(this);
		}
	}

	@Override
	public void onMessage(ResponseBody message) throws IOException {
		if (message.contentType() == TEXT) {
			System.out.println("MESSAGE: " + message.string());
		} else {
			System.out.println("MESSAGE: " + message.source().readByteString().hex());
		}
		message.close();
	}

	@Override
	public void onOpen(final WebSocket webSocket, Response response) {
//		for (String topic : handlers) {
//			webSocket.s
//		}
		System.out.println("OPEN: " + response.body().toString());

	}

	@Override
	public void onPong(Buffer payload) {
		System.out.println("PONG: " + payload.readUtf8());
	}

	@Override
	public void onClose(int code, String reason) {
		System.out.println("CLOSE: " + code + " " + reason);
	}

	@Override
	public void onFailure(IOException e, Response response) {
		e.printStackTrace();
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.runner.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.ipc.runner.protocol.Message;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * Connects a socket, attaches an object stream to it, and uses that stream to read and process commands.
 * 
 * @author hinton
 * 
 */
public class CommandLoop implements Runnable {
	private final static Logger log = LoggerFactory.getLogger(CommandLoop.class);
	private int port;
	private Socket socket;
	public CommandLoop() {
		log.debug("Command loop constructed");
	}

	public void setPort(final int port2) {
		this.port = port2;
	}

	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {
		log.debug("Running");
		try {
			socket = new Socket("localhost", port);
			log.debug("Socket connected");
			final ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

			final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
			// create a local job manager with a suitable factory
			log.debug("Creating reply processor");
			System.out.flush();
			/**
			 * Output messages can be added to this queue from anywhere and will be sent back.
			 */
			final BlockingQueue<Message> replies = new LinkedBlockingQueue<Message>();
			final Runnable replyRunnable = new Runnable() {
				@Override
				public void run() {
					log.debug("Reply transmission thread starting");
					System.out.flush();
					while (true) {
						Message message = null;
						try {
							message = replies.take();
						} catch (final InterruptedException e) {
						}
						if (socket.isClosed()) {
							return;
						}

						if (message != null) {
							// send message
							try {
								out.writeObject(message);
							} catch (IOException e) {
								log.debug("Error sending message from reply queue ", e);
								return;
							}
						}
					}
				}
			};

			// it seems we have to do this to force the classloader to load the message class.
			// not happy about that, really, but what can you do? If you don't do this OSGi's classloader
			// blows up entirely.
			final Message nothing = new Message(Message.Type.TERMINATE, null, null);

			log.debug("Starting reply processor");
			System.out.flush();
			final Thread replyThread = new Thread(replyRunnable);
			replyThread.start();
			System.err.println("Waiting for query");
			while (socket.isConnected()) {
				log.debug("Waiting for query");
				System.out.flush();
				final Object query = in.readObject();
				if (query instanceof Message) {
					final Message message = (Message) query;
					switch (message.getType()) {
					case START:
						final Object payload = message.getPayload();
						if (payload instanceof IJobDescriptor) {
							final IJobDescriptor jobDescriptor = (IJobDescriptor) payload;
						} else {
							log.error("Received a payload which is not an IJobDescriptor instance");
						}
						break;
					case PAUSE:
						break;
					case CANCEL:
						break;
					case RESUME:
						break;
					case TERMINATE:
						socket.close();
						break;
					}
				} else {
					log.warn("Received query of unknown type: " + query.getClass().getCanonicalName());
					System.out.flush();
				}
			}

			log.debug("Socket no longer connected, joining reply thread and shutting down");
			System.out.flush();

			try {
				replyThread.join();
			} catch (final InterruptedException e) {
			}
		} catch (final IOException exception) {
			log.error("IO Exception in main loop", exception);
			System.out.flush();
		} catch (ClassNotFoundException e) {
			log.error("Class not found exception in main loop", e);
			System.out.flush();
		} finally {
			log.debug("Finished");
			System.out.flush();
			try {
				if (!socket.isClosed())
					socket.close();
			} catch (final IOException e) {
				log.warn("Could not close socket in finally block", e);
				System.out.flush();
			}
		}
	}
}

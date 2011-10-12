/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.ipc.runner.protocol.Message;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlFactory;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * Creates IPC proxy controls. The second stack will need to be able to deserialize the descriptor and so on.
 * 
 * @author hinton
 * 
 */
public class IPCJobControlFactory implements IJobControlFactory {
	private static final Logger log = LoggerFactory.getLogger(IPCJobControlFactory.class);
	private final ServerSocket socket;

	private final Map<UUID, IPCJobControl> controlsByUUID = new HashMap<UUID, IPCJobControl>();

	public interface IConnectionStateListener {
		void clientConnected();
		void clientDisconnected();
	}

	/**
	 * Create an IPC job control factory; the activator
	 * 
	 * @param activator
	 * @throws IOException
	 */
	public IPCJobControlFactory() throws IOException {
		socket = new ServerSocket(0);
		log.debug("Listening on " + getLocalPort());
		final Thread acceptor = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					log.debug("Waiting for runner to connect");
					final Socket client = socket.accept();
					log.debug("Runner connected");
					final ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
					output.flush();
					final ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));

					// send a message
					final Message message = new Message(Message.Type.TERMINATE, UUID.randomUUID(), "boo");
					output.writeObject(message);
					output.flush();
				} catch (final IOException ex) {
					log.error("IO Exception in accepting thread", ex);
				} catch (final Exception ex) {
					log.error("Misc exception in accepting thread", ex);
				}
			}
		});
		acceptor.start();
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.jobmanager.jobs.IJobControlFactory#createJobControl(com.mmxlabs.jobmanager.jobs.IJobDescriptor)
	 */
	@Override
	public IJobControl createJobControl(final IJobDescriptor job) {
		final IPCJobControl result = new IPCJobControl(job, UUID.randomUUID(), null);
		controlsByUUID.put(result.getUUID(), result);
		return result;
	}

	public void addConnectionStateListener(final IConnectionStateListener connectionStateListener) {

	}

	public int getLocalPort() {
		return socket.getLocalPort();
	}

}

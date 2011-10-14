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
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.ipc.runner.impl.MessageProcessingLoop;
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
	private final MessageLoop loop;

	protected class MessageLoop extends MessageProcessingLoop {
		@Override
		protected Pair<ObjectInputStream, ObjectOutputStream> connect() {
			log.debug("Waiting for runner to connect");
			try {
				final Socket client = socket.accept();
				log.debug("Runner connected");

				final ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
				output.flush();
				final ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));

				for (final IConnectionStateListener listener : connectionStateListeners) {
					listener.clientConnected();
				}

				return new Pair<ObjectInputStream, ObjectOutputStream>(input, output);
			} catch (final Exception ex) {
				return null;
			}
		}

		@Override
		protected void receiveMessage(final Message message) {
			switch (message.getType()) {
			case NotifyProgressChanage:
			case NotifyStateChange:
			case ReplyOutput:
			case ReplyPauseable:
			case ReplyProgress:
			case ReplyState:
				final IPCJobControl control = controlsByUUID.get(message.getJob());
				if (control != null) {
					control.receiveMessage(message);
				}
			}
		}

		@Override
		public void sendMessage(final Message message) {
			super.sendMessage(message);
		}

		@Override
		protected void terminate() {
			for (final IConnectionStateListener listener : connectionStateListeners) {
				listener.clientDisconnected();
			}
		}
	}

	public interface IConnectionStateListener {
		void clientConnected();

		void clientDisconnected();
	}

	/**
	 * Create a new IPC JCF. This will need changing to run each job in its own process, but since the startup overhead for an equinox stack is large we might not really want that.
	 * 
	 * @param activator
	 * @throws IOException
	 */
	public IPCJobControlFactory() throws IOException {
		socket = new ServerSocket(0);
		loop = new MessageLoop();
		log.debug("Listening on " + getLocalPort());
		final Thread acceptor = new Thread(loop);
		acceptor.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobmanager.jobs.IJobControlFactory#createJobControl(com.mmxlabs.jobmanager.jobs.IJobDescriptor)
	 */
	@Override
	public IJobControl createJobControl(final IJobDescriptor job) {
		if (job instanceof Serializable) {
			final IPCJobControl result = new IPCJobControl(job, UUID.randomUUID(), loop);
			controlsByUUID.put(result.getUUID(), result);
			result.createRemote(); // has to happen after map addition has finished, in case of a race.
			return result;
		} else {
			return null;
		}
	}

	private final LinkedList<IConnectionStateListener> connectionStateListeners = new LinkedList<IConnectionStateListener>();

	public void addConnectionStateListener(final IConnectionStateListener connectionStateListener) {
		connectionStateListeners.add(connectionStateListener);
	}

	public int getLocalPort() {
		return socket.getLocalPort();
	}

}

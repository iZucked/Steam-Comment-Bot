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
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AdapterFactoryJobControlFactory;
import com.mmxlabs.jobmanager.ipc.runner.protocol.Message;
import com.mmxlabs.jobmanager.ipc.runner.protocol.Message.States;
import com.mmxlabs.jobmanager.ipc.runner.protocol.Message.Type;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlFactory;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.impl.JobManager;
import com.mmxlabs.jobmanager.manager.impl.JobManagerDescriptor;

/**
 * Connects a socket, attaches an object stream to it, and uses that stream to read and process commands.
 * 
 * @author hinton
 * 
 */
public class CommandLoop extends MessageProcessingLoop implements Runnable {
	private final static Logger log = LoggerFactory.getLogger(CommandLoop.class);

	private final HashMap<UUID, IJobControl> jobControlByUUID = new HashMap<UUID, IJobControl>();
	private final HashMap<IJobControl, UUID> uuidByJobControl = new HashMap<IJobControl, UUID>();

	private int port;
	private Socket socket;

	private final IJobManager myJobManager;

	public CommandLoop() {
		log.debug("Command loop constructed");
		final IJobControlFactory factory = new AdapterFactoryJobControlFactory();
		myJobManager = new JobManager(new JobManagerDescriptor("Local Job Manager"), factory);
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

	final IJobControlListener listener = new IJobControlListener() {
		@Override
		public boolean jobStateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {
			sendMessage(new Message(Type.NotifyStateChange, uuidByJobControl.get(job), new States(oldState, newState)));
			return true;
		}

		@Override
		public boolean jobProgressUpdated(final IJobControl job, final int progressDelta) {
			sendMessage(new Message(Type.NotifyProgressChanage, uuidByJobControl.get(job), progressDelta));
			return true;
		}
	};


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobmanager.ipc.runner.impl.MessageProcessingLoop#receiveMessage(com.mmxlabs.jobmanager.ipc.runner.protocol.Message)
	 */
	@Override
	protected void receiveMessage(final Message message) {
		final IJobControl job = jobControlByUUID.get(message.getJob());
		if ((message.getType() == Type.Create && job != null) || (job == null && message.getType() != Type.Create)) {
			return;
			// log error here
		}

		switch (message.getType()) {
		case Create:
			if (message.getPayload() instanceof IJobDescriptor) {
				final IJobControl control = myJobManager.submitJob((IJobDescriptor) message.getPayload());
				jobControlByUUID.put(message.getJob(), control);
				uuidByJobControl.put(control, message.getJob());
				control.addListener(listener);
			}
			break;
		case Destroy:
			job.removeListener(listener);
			myJobManager.removeJobDescriptor(job.getJobDescriptor());
			break;
		case Terminate:
			try {
				socket.close();
			} catch (IOException e) {

			}
			break;
		case Prepare:
			job.prepare();
			break;
		case Start:
			job.start();
			break;
		case Pause:
			job.pause();
			break;
		case Resume:
			job.resume();
			break;
		case Cancel:
			job.cancel();
			break;
		case GetPauseable:
			sendMessage(new Message(Type.ReplyPauseable, message.getJob(), job.isPauseable()));
			break;
		case GetState:
			sendMessage(new Message(Type.ReplyState, message.getJob(), job.getJobState()));
			break;
		case GetOutput:
			final Object output = job.getJobOutput();
			if (output instanceof Serializable)
			sendMessage(new Message(Type.ReplyOutput, message.getJob(), (Serializable) job.getJobOutput()));
			else
				sendMessage(new Message(Type.ReplyOutput, message.getJob(), new RuntimeException("Job provided a non-serializable output, which cannot be transmitted over a stream")));
			break;
		case GetProgress:
			sendMessage(new Message(Type.ReplyProgress, message.getJob(), job.getProgress()));
			break;
		}
	}

	@Override
	protected Pair<ObjectInputStream, ObjectOutputStream> connect() {
		try {
			socket = new Socket("localhost", port);
			log.debug("Socket connected");
			final ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

			final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();

			return new Pair<ObjectInputStream, ObjectOutputStream>(in, out);
		} catch (final Exception ex) {
			return null;
		}
	}
}

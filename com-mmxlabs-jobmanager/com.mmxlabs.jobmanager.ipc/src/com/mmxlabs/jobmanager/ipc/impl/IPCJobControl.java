/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import com.mmxlabs.jobmanager.ipc.runner.protocol.Message;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * @author hinton
 * 
 */
public class IPCJobControl implements IJobControl {
	private final IJobDescriptor descriptor;
	private final BlockingQueue<Message> messageChannel;
	private final UUID uuid;
	private Serializable payload;

	private Object lastOutput = null;
	private int lastPercentage = 0;
	private EJobState jobState = EJobState.UNKNOWN;

	private final List<IJobControlListener> controlListeners = new ArrayList<IJobControlListener>();

	/**
	 * @param job
	 * @param randomUUID
	 */
	public IPCJobControl(IJobDescriptor job, UUID randomUUID, final BlockingQueue<Message> replyChannel) {
		this.descriptor = job;
		this.uuid = randomUUID;
		this.messageChannel = replyChannel;
	}

	protected void sendMessage(final Message outbound) {
		try {
			messageChannel.put(outbound);
		} catch (final InterruptedException e) {

		}
	}

	protected void receiveMessage(final Message inbound) {
		switch (inbound.getType()) {
		case PERCENTAGE:
			updateState(jobState, jobState, lastPercentage, (Integer) inbound.getPayload());
			break;
		case ERROR:
			lastOutput = inbound.getPayload();
			updateState(jobState, EJobState.CANCELLING, lastPercentage, lastPercentage);
			updateState(jobState, EJobState.CANCELLED, lastPercentage, lastPercentage);
			break;
		case FINISHED:
			lastOutput = inbound.getPayload();
			updateState(jobState, EJobState.COMPLETED, lastPercentage, 100);
			break;
		case OUTPUT:
			lastOutput = inbound.getPayload();
			break;
		default:
			break;
		}
	}

	private void updateState(final EJobState oldState, final EJobState newState, final int oldPercentage, final int newPercentage) {
		final int percentageDelta = newPercentage - oldPercentage;
		final Iterator<IJobControlListener> iterator = controlListeners.iterator();
		while (iterator.hasNext()) {
			IJobControlListener listener = iterator.next();

			if (percentageDelta > 0) {
				if (!listener.jobProgressUpdated(this, percentageDelta)) {
					iterator.remove();
					listener = null;
				}
			}

			if (oldState != newState) {
				if (listener != null) {
					if (!listener.jobStateChanged(this, oldState, newState))
						iterator.remove();
				}
			}
		}

		this.jobState = newState;
		this.lastPercentage = newPercentage;
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return descriptor;
	}

	@Override
	public void prepare() {

	}

	@Override
	public void start() {
		sendMessage(new Message(Message.Type.START, uuid, payload));
	}

	@Override
	public void cancel() {
		sendMessage(new Message(Message.Type.CANCEL, uuid, null));
	}

	@Override
	public boolean isPauseable() {
		return true;
	}

	@Override
	public void pause() {
		sendMessage(new Message(Message.Type.PAUSE, uuid));
	}

	@Override
	public void resume() {
		sendMessage(new Message(Message.Type.RESUME, uuid));
	}

	@Override
	public EJobState getJobState() {
		return jobState;
	}

	@Override
	public int getProgress() {
		return lastPercentage;
	}

	@Override
	public Object getJobOutput() {
		return lastOutput;
	}

	@Override
	public void addListener(final IJobControlListener listener) {

	}

	@Override
	public void removeListener(final IJobControlListener listener) {

	}

	@Override
	public void dispose() {

	}

	/**
	 * @return
	 */
	public UUID getUUID() {
		return uuid;
	}
}

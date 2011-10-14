/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.jobmanager.ipc.impl.IPCJobControlFactory.MessageLoop;
import com.mmxlabs.jobmanager.ipc.runner.protocol.Message;
import com.mmxlabs.jobmanager.ipc.runner.protocol.Message.States;
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

	private final UUID uuid;

	private final List<IJobControlListener> controlListeners = new ArrayList<IJobControlListener>();
	private final MessageLoop loop;

	private final Map<Message.Type, BlockingQueue<Object>> resultQueues = CollectionsUtil.makeHashMap(Message.Type.ReplyOutput, new LinkedBlockingQueue<Object>(), Message.Type.ReplyProgress,
			new LinkedBlockingQueue<Object>(), Message.Type.ReplyPauseable, new LinkedBlockingQueue<Object>(), Message.Type.ReplyState, new LinkedBlockingQueue<Object>());
	/**
	 * @param job
	 * @param randomUUID
	 */
	public IPCJobControl(IJobDescriptor job, UUID randomUUID, final MessageLoop loop) {
		this.descriptor = job;
		this.uuid = randomUUID;
		this.loop = loop;
	}

	public void createRemote() {
		loop.sendMessage(new Message(Message.Type.Create, uuid, (Serializable) descriptor));
	}

	public UUID getUUID() {
		return uuid;
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return descriptor;
	}

	@Override
	public void prepare() {
		loop.sendMessage(new Message(Message.Type.Prepare, uuid));
	}

	@Override
	public void start() {
		loop.sendMessage(new Message(Message.Type.Start, uuid));
	}

	@Override
	public void cancel() {
		loop.sendMessage(new Message(Message.Type.Cancel, uuid));
	}

	private Object waitForAnswer(final Message.Type type) {
		try {
			final BlockingQueue<Object> queue = resultQueues.get(type);
			return queue.take();
		} catch (final Exception ex) {
			return null;
		}
	}

	@Override
	public boolean isPauseable() {
		loop.sendMessage(new Message(Message.Type.GetPauseable, uuid));
		return (Boolean) waitForAnswer(Message.Type.ReplyPauseable);
	}

	@Override
	public void pause() {
		loop.sendMessage(new Message(Message.Type.Pause, uuid));
	}

	@Override
	public void resume() {
		loop.sendMessage(new Message(Message.Type.Resume, uuid));
	}

	@Override
	public EJobState getJobState() {
		loop.sendMessage(new Message(Message.Type.GetState, uuid));
		return (EJobState) waitForAnswer(Message.Type.ReplyState);
	}

	@Override
	public int getProgress() {
		loop.sendMessage(new Message(Message.Type.GetProgress, uuid));
		return (Integer) waitForAnswer(Message.Type.ReplyProgress);
	}

	@Override
	public Object getJobOutput() {
		loop.sendMessage(new Message(Message.Type.GetOutput, uuid));
		return waitForAnswer(Message.Type.ReplyOutput);
	}

	@Override
	public void addListener(final IJobControlListener listener) {
		controlListeners.add(listener);
	}

	@Override
	public void removeListener(final IJobControlListener listener) {
		controlListeners.add(listener);
	}

	private void notifyProgress(int delta) {
		final Iterator<IJobControlListener> iterator = controlListeners.iterator();
		while (iterator.hasNext()) {
			final IJobControlListener listener = iterator.next();
			if (!listener.jobProgressUpdated(this, delta))
				iterator.remove();
		}
	}

	private void notifyState(final EJobState oldState, final EJobState newState) {
		final Iterator<IJobControlListener> iterator = controlListeners.iterator();
		while (iterator.hasNext()) {
			final IJobControlListener listener = iterator.next();
			if (!listener.jobStateChanged(this, oldState, newState))
				iterator.remove();
		}
	}

	@Override
	public void dispose() {
		loop.sendMessage(new Message(Message.Type.Destroy, uuid));
	}

	/**
	 * Handle an incoming message
	 * 
	 * @param message
	 */
	public void receiveMessage(final Message message) {
		switch (message.getType()) {
		case NotifyProgressChanage:
			notifyProgress((Integer) message.getPayload());
			break;
		case NotifyStateChange:
			final States states = (States) message.getPayload();
			notifyState(states.oldState, states.newState);
			break;
		case ReplyOutput:
		case ReplyPauseable:
		case ReplyProgress:
		case ReplyState:
			try {
				resultQueues.get(message.getType()).put(message.getPayload());
			} catch (final InterruptedException ex) {
				// bug
			}
			break;
		}
	}
}

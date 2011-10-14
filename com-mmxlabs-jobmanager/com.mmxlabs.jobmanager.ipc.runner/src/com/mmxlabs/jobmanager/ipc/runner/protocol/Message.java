/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.runner.protocol;

import java.io.Serializable;
import java.util.UUID;

import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;

/**
 * A message from the application to the runner process is represented by a query.
 * 
 * It may be that this would be better handled using RMI, but the web of objects required in our conventional interface is quite complicated, so this explicit method seems a bit easier. We'll have to
 * see if it works out in the end.
 * 
 * @author hinton
 * 
 */
public class Message implements Serializable {
	public final static class States implements Serializable {
		private static final long serialVersionUID = 1L;
		final public EJobState oldState, newState;

		public States(EJobState oldState, EJobState newState) {
			super();
			this.oldState = oldState;
			this.newState = newState;
		}
	}

	private static final long serialVersionUID = 1L;

	public enum Type {
		// these messages are out of band
		/**
		 * Sent to trigger the creation of a real job control to partner with a proxy control
		 */
		Create,
		/**
		 * Sent to trigger the destruction of a real job control because its proxy is dead
		 */
		Destroy,
		/**
		 * Sent to terminate the run loop in the runner
		 */
		Terminate,
		// these messages are all queries from the proxy side to the real implementation
		/**
		 * Proxy message for {@link IJobControl#prepare()}
		 */
		Prepare,
		/**
		 * For {@link IJobControl#start()}
		 */
		Start,
		/**
		 * For {@link IJobControl#cancel()}
		 */
		Cancel,
		/**
		 * For {@link IJobControl#isPauseable()}
		 */
		GetPauseable, Pause, Resume, GetState, GetProgress, GetOutput,
		// whereas these messages are all feedback to the proxy side from the real side.
		NotifyStateChange, NotifyProgressChanage,
		// these are responses from the get messages
		ReplyPauseable, ReplyState, ReplyOutput, ReplyProgress
	}

	private Type type;
	private UUID job;
	private Serializable payload;

	/**
	 * @param type
	 * @param job
	 * @param payload
	 */
	public Message(Type type, UUID job, Serializable payload) {
		super();
		this.type = type;
		this.job = job;
		this.payload = payload;
	}

	/**
	 * @param pause
	 * @param uuid
	 */
	public Message(Type pause, UUID uuid) {
		this(pause, uuid, null);
	}

	public final Type getType() {
		return type;
	}

	public final void setType(Type type) {
		this.type = type;
	}

	public final UUID getJob() {
		return job;
	}

	public final void setJob(UUID job) {
		this.job = job;
	}

	public final Object getPayload() {
		return payload;
	}

	public final void setPayload(Serializable payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		final String payString = payload + "";
		return "Message [type=" + type + ", job=" + job + ", payload=" + (payString.length() > 30 ? payString.substring(0, 30) : payString) + "]";
	}

}

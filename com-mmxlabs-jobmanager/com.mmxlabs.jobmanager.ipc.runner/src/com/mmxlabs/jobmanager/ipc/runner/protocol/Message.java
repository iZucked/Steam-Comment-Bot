/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.runner.protocol;

import java.io.Serializable;
import java.util.UUID;

/**
 * A message from the application to the runner process is represented by a query
 * 
 * @author hinton
 * 
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Type {
		START, PAUSE, RESUME, CANCEL, TERMINATE, PERCENTAGE, ERROR, OUTPUT, FINISHED
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
}

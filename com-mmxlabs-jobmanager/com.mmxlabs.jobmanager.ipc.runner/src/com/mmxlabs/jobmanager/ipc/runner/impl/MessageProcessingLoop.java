/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.runner.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.ipc.runner.protocol.Message;

/**
 * Handles message IO, at both sides.
 * 
 * @author hinton
 * 
 */
public abstract class MessageProcessingLoop implements Runnable {
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private final BlockingQueue<Message> outputBuffer = new LinkedBlockingQueue<Message>();

	public MessageProcessingLoop() {

	}

	@Override
	public void run() {
		try {
			@SuppressWarnings("unused")
			final Message nothing = new Message(Message.Type.Terminate, null, null);
			final Pair<ObjectInputStream, ObjectOutputStream> streams = connect();
			input = streams.getFirst();
			output = streams.getSecond();
			final Thread transmitThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						Message message = null;
						try {
							message = outputBuffer.take();
						} catch (final InterruptedException e) {
							return;
						}

						// if (socket.isClosed()) {
						// return;
						// }

						if (message != null) {
							// send message
							try {
								System.err.println("Send " + message);
								output.writeObject(message);
								output.flush();
								// reset the output stream to avoid exhausting the heap
								// if we don't do this it will keep a reference to every transmitted object
								// and eventually blow up.
								output.reset();
							} catch (IOException e) {
								return;
							}
						}
					}
				}
			});

			transmitThread.start();

			while (true) {
				try {
					System.err.println("Reading object");
					final Object in = input.readObject();
					if (in instanceof Message) {
						System.err.println("Recv " + in);
						receiveMessage((Message) in);
					}
				} catch (IOException e) {
					System.err.println("IO ex");
					e.printStackTrace();
					System.err.println(e.toString());
					transmitThread.interrupt();
					break;
				} catch (ClassNotFoundException e) {
					// log it
					System.err.println("Class not found");
				}
			}

			try {
				transmitThread.join();
			} catch (final InterruptedException e) {

			}
		} finally {
			System.err.println("Bye!");
			terminate();
		}
	}

	protected void sendMessage(final Message message) {
		try {
			outputBuffer.put(message);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void terminate() {

	}

	protected abstract Pair<ObjectInputStream, ObjectOutputStream> connect();

	protected abstract void receiveMessage(final Message message);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder;

import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.xml.stream.XMLStreamException;

/**
 * TODO: Read/write XMl TODO: Created objects should be assigned a name and stored in a map TODO: Read/Write should be based on the object ref name (ref="name" value="34") TODO: Store type + processor
 * (e.g. spring property editor)
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 */
public class RecorderInvocationHandler implements InvocationHandler {

	private final Object t;

	private boolean done;

	private final XmlStreamWriter writer;

	private final String lastMethod;

	public RecorderInvocationHandler(final Class<?> interfaceClass, final Object t, final OutputStream os, final String lastMethod) throws XMLStreamException {
		this.t = t;
		this.lastMethod = lastMethod;

		writer = new XmlStreamWriter(os);
		writer.writeHeader(interfaceClass);
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

		final Object ret = method.invoke(t, args);
		if (!done) {
			writer.recordMethod(method, args, ret);

			if (method.getName().equals(lastMethod)) {
				writer.writeFooter();
				writer.flush();
				writer.close();
				done = true;
			}
		}
		return ret;
	}
}

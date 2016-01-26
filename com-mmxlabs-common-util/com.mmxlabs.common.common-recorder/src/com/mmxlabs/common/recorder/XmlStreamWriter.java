/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

import com.mmxlabs.common.recorder.conversion.ITypeConvertorService;
import com.mmxlabs.common.recorder.conversion.impl.TypeConvertorService;

public class XmlStreamWriter {

	private final Map<Object, String> objectMap = new HashMap<Object, String>();

	private final OutputStream stream;

	private final AtomicInteger counter = new AtomicInteger(0);

	private final XMLEventFactory eventFactory = XMLEventFactory.newFactory();
	private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
	private XMLEventWriter eventWriter;

	private final ITypeConvertorService typeConvertor = new TypeConvertorService();

	private final String namespace = "http://www.mmxlabs.com/schemas/InterfaceRecorder-1.0.0";

	private final String prefix = "ir";

	public XmlStreamWriter(final OutputStream stream) {
		this.stream = stream;
	}

	public void close() throws IOException, XMLStreamException {
		eventWriter.close();
	}

	/**
	 * Write the XML and document headers
	 * 
	 * @throws XMLStreamException
	 */
	public void writeHeader(final Class<?> interfaceClass) throws XMLStreamException {

		eventWriter = outputFactory.createXMLEventWriter(stream);

		final StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);

		final StartElement startOperations = eventFactory.createStartElement(prefix, namespace, XmlRecordingConstants.ELEMENT_OPERATIONS);
		eventWriter.add(startOperations);

		eventWriter.add(eventFactory.createNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
		eventWriter.add(eventFactory.createAttribute("xsi:schemaLocation", namespace + " schemas/InterfaceRecorder-1.0.0.xsd"));
		// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// xsi:schemaLocation="http://www.eclipse.org/webtools/Catalogue Catalogue.xsd ">
		eventWriter.add(eventFactory.createNamespace(prefix, namespace));

		eventWriter.add(eventFactory.createAttribute(XmlRecordingConstants.ATTR_OPERATIONS_INTERFACE, interfaceClass.getCanonicalName()));
	}

	public void recordMethod(final Method m, final Object[] args, final Object ret) throws XMLStreamException {

		@SuppressWarnings("rawtypes")
		final Class[] paramTypes = m.getParameterTypes();

		final StartElement methodStart = eventFactory.createStartElement(prefix, namespace, XmlRecordingConstants.ELEMENT_METHOD);
		eventWriter.add(methodStart);

		eventWriter.add(eventFactory.createAttribute(XmlRecordingConstants.ATTR_METHOD_NAME, m.getName()));

		if ((m.getReturnType() != null) && !m.getReturnType().getName().equals("void")) {
			final String refName = "reference-" + counter.getAndIncrement();
			eventWriter.add(eventFactory.createAttribute(XmlRecordingConstants.ATTR_METHOD_RETURNED_REF, refName));
			objectMap.put(ret, refName);
		}

		for (int i = 0; i < paramTypes.length; ++i) {
			writeArgument(args[i], paramTypes[i]);
		}

		final EndElement endElement = eventFactory.createEndElement(prefix, namespace, XmlRecordingConstants.ELEMENT_METHOD);

		eventWriter.add(endElement);
	}

	private void writeArgument(final Object arg, final Class<?> paramType) throws XMLStreamException {
		eventWriter.add(eventFactory.createStartElement(prefix, namespace, XmlRecordingConstants.ELEMENT_ARGUMENT));

		eventWriter.add(eventFactory.createAttribute(XmlRecordingConstants.ATTR_ARGUMENT_TYPE, paramType.getCanonicalName()));

		if (objectMap.containsKey(arg)) {
			final String ref = objectMap.get(arg);
			eventWriter.add(eventFactory.createAttribute(XmlRecordingConstants.ATTR_ARGUMENT_REF, ref));
		} else {

			eventWriter.add(eventFactory.createAttribute(XmlRecordingConstants.ATTR_ARGUMENT_VALUE, typeConvertor.toString(paramType.getCanonicalName(), arg)));
		}
		eventWriter.add(eventFactory.createEndElement(prefix, namespace, XmlRecordingConstants.ELEMENT_ARGUMENT));
	}

	/**
	 * Write document footer
	 * 
	 * @throws XMLStreamException
	 */
	void writeFooter() throws XMLStreamException {

		final EndElement endOperations = eventFactory.createEndElement(prefix, namespace, XmlRecordingConstants.ELEMENT_OPERATIONS);
		eventWriter.add(endOperations);

		final EndDocument endDocument = eventFactory.createEndDocument();
		eventWriter.add(endDocument);
	}

	public void flush() throws IOException {
		stream.flush();
	}

}

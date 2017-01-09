/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.recorder.conversion.ITypeConvertorService;
import com.mmxlabs.common.recorder.conversion.impl.TypeConvertorService;

public class XmlStreamReader<T> {

	private final InputStream stream;

	private final @NonNull T object;

	private final Map<String, Object> objectMap = new HashMap<String, Object>();

	private final ITypeConvertorService conversionService = new TypeConvertorService();

	public XmlStreamReader(final @NonNull T object, final InputStream stream) {
		this.object = object;
		this.stream = stream;
	}

	public void parse() throws IOException, XMLStreamException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
			NoSuchMethodException {

		final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

		final XMLEventReader reader = inputFactory.createXMLEventReader(stream);

		final QName qname_methodName = new QName(XmlRecordingConstants.ATTR_METHOD_NAME);
		final QName qname_methodReturnedRef = new QName(XmlRecordingConstants.ATTR_METHOD_RETURNED_REF);

		final QName qname_argumentType = new QName(XmlRecordingConstants.ATTR_ARGUMENT_TYPE);
		final QName qname_argumentRef = new QName(XmlRecordingConstants.ATTR_ARGUMENT_REF);
		final QName qname_argumentValue = new QName(XmlRecordingConstants.ATTR_ARGUMENT_VALUE);

		final List<Class<?>> parameterTypes = new LinkedList<Class<?>>();
		final List<Object> arguments = new LinkedList<Object>();
		String methodName = null;
		String returnedRef = null;

		while (reader.hasNext()) {
			final XMLEvent event = reader.nextEvent();

			if (event.isStartElement()) {
				final StartElement startElement = event.asStartElement();
				if (XmlRecordingConstants.ELEMENT_METHOD.equals(startElement.getName().getLocalPart())) {

					methodName = startElement.getAttributeByName(qname_methodName).getValue();

					returnedRef = startElement.getAttributeByName(qname_methodReturnedRef).getValue();

				} else if (XmlRecordingConstants.ELEMENT_ARGUMENT.equals(startElement.getName().getLocalPart())) {

					final String type = startElement.getAttributeByName(qname_argumentType).getValue();

					final Class<?> typeClass = Class.forName(type);

					Object arg = null;
					final Attribute attribRef = startElement.getAttributeByName(qname_argumentRef);
					if (attribRef != null) {
						arg = objectMap.get(attribRef.getValue());
					} else {
						final Attribute attribValue = startElement.getAttributeByName(qname_argumentValue);

						// TODO: use a registry of "things" to convert string to
						// object
						arg = convert(attribValue.getValue(), type);
					}

					parameterTypes.add(typeClass);
					arguments.add(arg);
				}
			} else if (event.isEndElement()) {
				final EndElement endElement = event.asEndElement();

				if (XmlRecordingConstants.ELEMENT_METHOD.equals(endElement.getName().getLocalPart())) {
					final Class<?> cls = object.getClass();
					Class<?> parameterTypesArray[] = new Class[parameterTypes.size()];
					parameterTypesArray = parameterTypes.toArray(parameterTypesArray);
					final Object[] argumentsArray = arguments.toArray();
					final Method m = cls.getMethod(methodName, parameterTypesArray);

					final Object ret = m.invoke(object, argumentsArray);
					if (returnedRef != null) {
						objectMap.put(returnedRef, ret);
					}

					// Reset in preparation for new method call.
					methodName = null;
					returnedRef = null;
					arguments.clear();
					parameterTypes.clear();
				}
			}
		}
	}

	private Object convert(final String value, final String typeClass) {

		return conversionService.toObject(typeClass, value);
	}

}

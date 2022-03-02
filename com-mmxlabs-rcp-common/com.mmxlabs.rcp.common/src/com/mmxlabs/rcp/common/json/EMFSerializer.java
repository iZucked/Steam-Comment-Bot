/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Example usage
 * 
 * <pre>
 *		EObject subModel = ...;
 *		ObjectMapper mapper = new ObjectMapper();
 *		mapper.registerModule(new EMFJacksonModule());
 *		try {
 *			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("output.json"), subModel);
 *		} catch (Exception e) {
 *			// TODO Auto-generated catch block
 *			e.printStackTrace();
 *		}
 * </pre>
 * 
 * @author Simon Goodall
 *
 * @param <T>
 */
public class EMFSerializer<T extends EObject> extends StdSerializer<T> {

	private static final long serialVersionUID = 1L;

	private final EClass eClass;
	private final Class<T> cls;

	private @Nullable IJSONSerialisationConfigProvider serialisationConfig;

	public EMFSerializer(final EClass eClass, final Class<T> cls, @Nullable IJSONSerialisationConfigProvider serialisationConfig) {
		super(cls);
		this.cls = cls;
		this.eClass = eClass;
		this.serialisationConfig = serialisationConfig;
	}

	@Override
	public void serialize(final T value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {

		jgen.writeStartObject();
		if (value instanceof EObject) {
			jgen.writeStringField(JSONConstants.ATTRIBUTE_CLASS, eClass.getName());
			jgen.writeStringField(JSONConstants.LOOKUP_ID, JSONReference.of(value).getName());
		}

		for (final EStructuralFeature f : value.eClass().getEAllStructuralFeatures()) {
			if (serialisationConfig != null && serialisationConfig.isFeatureIgnored(f)) {
				continue;
			}
			if (value.eIsSet(f)) {
				if (f.isMany()) {
					jgen.writeArrayFieldStart(f.getName());

					for (final Object obj : (List<?>) value.eGet(f)) {
						if (f instanceof EReference ref) {
							if (ref.isContainment()) {
								jgen.writeObject(obj);
							} else {
								jgen.writeObject(JSONReference.of((EObject) obj));
							}
						} else if (f instanceof EAttribute eAtrib) {
							if (obj == null) {
								// Value may be "set" as null
								jgen.writeString("null");
							} else {
								final String v = eAtrib.getEAttributeType().getEPackage().getEFactoryInstance().convertToString(eAtrib.getEAttributeType(), obj);
								jgen.writeString(v);
							}
						}
					}

					jgen.writeEndArray();

				} else {
					final Object obj = value.eGet(f);
					if (f instanceof EReference ref) {
						if (ref.isContainment()) {
							jgen.writeObjectField(f.getName(), obj);
						} else {
							jgen.writeObjectField(f.getName(), JSONReference.of((EObject) obj));
						}
					} else if (f instanceof EAttribute eAtrib) {
						if (obj == null) {
							// Value may be "set" as null
							jgen.writeStringField(f.getName(), "null");
						} else {
							final String v = eAtrib.getEAttributeType().getEPackage().getEFactoryInstance().convertToString(eAtrib.getEAttributeType(), obj);
							jgen.writeStringField(f.getName(), v);
						}
					}
				}

			}
		}

		jgen.writeEndObject();
	}
}
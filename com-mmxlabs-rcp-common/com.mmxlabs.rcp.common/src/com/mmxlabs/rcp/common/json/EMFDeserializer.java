/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class EMFDeserializer<T extends EObject> extends StdDeserializer<T> {

	private static final long serialVersionUID = 1L;
	private final EClass eClass;
	private final Class<T> cls;

	public EMFDeserializer(final EClass eClass, final Class<T> cls) {
		super(cls);
		this.eClass = eClass;
		this.cls = cls;
	}

	@Override
	public T deserialize(final JsonParser jp, final DeserializationContext dc) throws IOException {
		final EFactory eFactoryInstance = eClass.getEPackage().getEFactoryInstance();
		final JsonNode node = jp.getCodec().readTree(jp);

		// Is this the correct deserialiser for this data?
		if (node.has(JSONConstants.ATTRIBUTE_CLASS)) {
			final EClass targetEClass = getEClass(eClass, node.get(JSONConstants.ATTRIBUTE_CLASS).asText());
			final Class c = targetEClass.getInstanceClass();
			if (cls != c) {
				final JsonParser parser = node.traverse();
				parser.setCodec(jp.getCodec());
				return (T) parser.readValueAs(c);
			}
		}
		final T result = cls.cast(eFactoryInstance.create(eClass));

		EMFDeserializationContext ctx = null;
		if (dc instanceof EMFDeserializationContext) {
			ctx = (EMFDeserializationContext) dc;
		}

		final Iterator<Entry<String, JsonNode>> fieldsItrs = node.fields();
		while (fieldsItrs.hasNext()) {
			final Entry<String, JsonNode> e = fieldsItrs.next();
			final EStructuralFeature f = eClass.getEStructuralFeature(e.getKey());
			if (f != null) {
				if (f instanceof final EReference eReference) {
					final Class<?> instanceClass = eReference.getEReferenceType().getInstanceClass();
					if (f.isMany()) {
						if (eReference.isContainment()) {

							final EClassifier eType = eReference.getEType();
							final List<Object> newList = new LinkedList<>();
							// Special case for EMap
							if ("java.util.Map$Entry".equals(eType.getInstanceTypeName()) && eType instanceof final EClass cls) {

								for (final JsonNode c : e.getValue()) {
									final JsonParser parser = c.traverse();
									parser.setCodec(jp.getCodec());
									final java.util.Map.Entry<String, String> obj = (java.util.Map.Entry<String, String>) parser.readValueAs(instanceClass);

									final EObject value = eType.getEPackage().getEFactoryInstance().create(cls);

									final EStructuralFeature keyFeature = cls.getEStructuralFeature("key");
									final EStructuralFeature valueFeature = cls.getEStructuralFeature("value");

									Object attribValue = obj.getKey();
									Object valueValue = obj.getValue();
									// TODO: What about references 
									if (keyFeature instanceof final EAttribute eAttrib) {
										attribValue = eAttrib.getEAttributeType().getEPackage().getEFactoryInstance().createFromString(eAttrib.getEAttributeType(), (String) attribValue);
									}
									if (valueFeature instanceof final EAttribute eAttrib) {
										valueValue = eAttrib.getEAttributeType().getEPackage().getEFactoryInstance().createFromString(eAttrib.getEAttributeType(), (String) valueValue);
									}
									value.eSet(keyFeature, attribValue);
									value.eSet(valueFeature, valueValue);
									newList.add(value);
								}
							} else {

								for (final JsonNode c : e.getValue()) {
									final JsonParser parser = c.traverse();
									parser.setCodec(jp.getCodec());
									final Object obj = parser.readValueAs(instanceClass);
									if (ctx != null) {
										ctx.registerType(obj);
									}
									newList.add(obj);
								}
							}
							result.eSet(f, newList);
						} else {
							for (final JsonNode childNode : e.getValue()) {
								final JsonParser parser = childNode.traverse();
								parser.setCodec(jp.getCodec());
								final JSONReference ref = parser.readValueAs(JSONReference.class);
								// Defer the lookup until later
								ctx.deferLookup(ref, (EObject) result, eReference);
							}
						}
					} else {
						if (eReference.isContainment()) {
							final JsonParser parser = e.getValue().traverse();
							parser.setCodec(jp.getCodec());
							final Object obj = parser.readValueAs(instanceClass);
							if (ctx != null) {
								ctx.registerType(obj);
							}
							result.eSet(f, obj);
						} else {
							final JsonParser parser = e.getValue().traverse();
							parser.setCodec(jp.getCodec());
							final JSONReference ref = parser.readValueAs(JSONReference.class);
							ctx.deferLookup(ref, (EObject) result, eReference);
						}
					}
				} else if (f instanceof final EAttribute eAttribute) {
					if (f.isMany()) {
						final List<Object> newList = new LinkedList<>();
						for (final JsonNode c : e.getValue()) {
							final Object value = eAttribute.getEAttributeType().getEPackage().getEFactoryInstance().createFromString(eAttribute.getEAttributeType(), c.asText());
							newList.add(value);
						}
						result.eSet(f, newList);
					} else {
						final Object value = eAttribute.getEAttributeType().getEPackage().getEFactoryInstance().createFromString(eAttribute.getEAttributeType(), e.getValue().asText());
						result.eSet(eAttribute, value);
					}
				} else {
					// ?
				}

			}
		}
		if (ctx != null) {
			if (node.has(JSONConstants.LOOKUP_ID)) {
				final String lookupId = node.get(JSONConstants.LOOKUP_ID).asText();
				ctx.registerType(lookupId, result);
			} else {
				ctx.registerType(result);
			}
		}
		return result;

	}

	public static EClass getEClass(final EClass outputEClass, final String kind) {

		if (kind == null) {
			return outputEClass;
		}

		final EPackage ePackage = outputEClass.getEPackage();
		// find a subclass with the right name
		for (final EClassifier classifier : ePackage.getEClassifiers()) {
			if (classifier instanceof final EClass possibleSubClass) {
				if (outputEClass.isSuperTypeOf(possibleSubClass) && (possibleSubClass.isAbstract() == false) && possibleSubClass.getName().equalsIgnoreCase(kind)) {
					return (EClass) classifier;
				}
			}
		}
		// All registry packages...
		for (final Object obj : Registry.INSTANCE.values()) {
			if (obj instanceof final EPackage ePackage2) {
				for (final EClassifier classifier : ePackage2.getEClassifiers()) {
					if (classifier instanceof final EClass possibleSubClass) {
						// EObject is not listed in supertypes?
						final boolean superTypeOf = outputEClass == EcorePackage.Literals.EOBJECT || outputEClass.isSuperTypeOf(possibleSubClass);
						final boolean b = possibleSubClass.isAbstract() == false;
						final boolean equalsIgnoreCase = possibleSubClass.getName().equalsIgnoreCase(kind);
						if (superTypeOf && b && equalsIgnoreCase) {
							return (EClass) classifier;
						}
					}
				}
			}

		}
		return outputEClass;
	}
}
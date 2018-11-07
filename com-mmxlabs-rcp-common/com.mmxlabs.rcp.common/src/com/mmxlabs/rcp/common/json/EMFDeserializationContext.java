package com.mmxlabs.rcp.common.json;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.mmxlabs.common.Pair;

public class EMFDeserializationContext extends DefaultDeserializationContext {

	private final List<Runnable> actions = new LinkedList<>();
	private final Map<Pair<String, String>, Object> nameMap = new HashMap<>();
	private final Map<Pair<String, String>, Object> idMap = new HashMap<>();

	public void registerType(final Object value) {

		if (value instanceof EObject) {
			final EObject eObject = (EObject) value;
			final EStructuralFeature nameFeature = eObject.eClass().getEStructuralFeature("name");
			final EStructuralFeature mmxidFeature = eObject.eClass().getEStructuralFeature("mmxid");
			final String name = nameFeature == null ? null : (String) eObject.eGet(nameFeature);
			String id = mmxidFeature == null ? null : (String) eObject.eGet(mmxidFeature);
			if (id == null) {
				for (final EOperation op : eObject.eClass().getEOperations()) {
					if (op.getName().equalsIgnoreCase("gettempmmxid")) {
						try {
							id = (String) eObject.eInvoke(op, ECollections.emptyEList());
						} catch (final InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

			registerType(eObject.eClass().getName(), id, name, eObject);
		}
	}

	public void registerType(final String type, final String id, final String name, final Object value) {
		if (name != null) {
			nameMap.put(new Pair<>(type, name), value);
		}
		if (id != null) {
			idMap.put(new Pair<>(type, id), value);
		}
	}

	public Object lookupType(final JSONReference ref) {
		final Object idValue = idMap.get(new Pair<>(ref.getClassType(), ref.getGlobalId()));
		if (idValue != null) {
			return idValue;
		}
		final Object nameValue = nameMap.get(new Pair<>(ref.getClassType(), ref.getName()));
		if (nameValue != null) {
			return nameValue;
		}

		throw new IllegalArgumentException();
	}

	public void deferLookup(final JSONReference ref, final EObject owner, final EReference feature) {

		if (feature.isMany()) {
			actions.add(() -> ((List) owner.eGet(feature)).add(lookupType(ref)));
		} else {
			actions.add(() -> owner.eSet(feature, lookupType(ref)));
		}

	}

	public void runDeferredActions() {

		final List<Runnable> actionsCopy = new LinkedList<>(actions);
		actions.clear();
		actionsCopy.forEach(Runnable::run);
	}

	/**
	 * Default constructor for a blueprint object, which will use the standard {@link DeserializerCache}, given factory.
	 */
	public EMFDeserializationContext(final DeserializerFactory df) {
		super(df, null);
	}

	protected EMFDeserializationContext(final EMFDeserializationContext src, final DeserializationConfig config, final JsonParser jp, final InjectableValues values) {
		super(src, config, jp, values);
	}

	protected EMFDeserializationContext(final EMFDeserializationContext src) {
		super(src);
	}

	protected EMFDeserializationContext(final EMFDeserializationContext src, final DeserializerFactory factory) {
		super(src, factory);
	}

	@Override
	public EMFDeserializationContext copy() {
		ClassUtil.verifyMustOverride(Impl.class, this, "copy");
		return new EMFDeserializationContext(this);
	}

	@Override
	public EMFDeserializationContext createInstance(final DeserializationConfig config, final JsonParser p, final InjectableValues values) {
		return new EMFDeserializationContext(this, config, p, values);
	}

	@Override
	public EMFDeserializationContext with(final DeserializerFactory factory) {
		return new EMFDeserializationContext(this, factory);
	}

};
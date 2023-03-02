package com.mmxlabs.lngdataserver.server.editors.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.lngdataserver.server.editors.model.layout.Column;
import com.mmxlabs.lngdataserver.server.editors.model.layout.Group;
import com.mmxlabs.lngdataserver.server.editors.model.layout.ILayoutElement;
import com.mmxlabs.lngdataserver.server.editors.model.layout.Label;
import com.mmxlabs.lngdataserver.server.editors.model.layout.Row;
import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class LayoutBuilder {

	private final ComposedAdapterFactory FACTORY = createAdapterFactory();

	private final Set<Pair<Object, EStructuralFeature>> seenEditors = new HashSet<>();

	private List<Runnable> finalActions = new LinkedList<>();

	private IScenarioDataProvider sdp;

	private ReferenceValueProviderCache referenceValueProviderCache;

	public RootBuilder begin(IScenarioDataProvider sdp) {
		this.sdp = sdp;
		referenceValueProviderCache = new ReferenceValueProviderCache(sdp.getTypedScenario(MMXRootObject.class));
		// WE can't use this. in the super(..) call, so we have to pass the object
		// reference into the constructor...
		final ILayoutElement[] root = new ILayoutElement[1];
		return new RootBuilder(this, root);
	}

	public class RootBuilder extends ElementBuilder<RootBuilder, LayoutBuilder> {
		private final ILayoutElement[] root;

		private RootBuilder(final LayoutBuilder parentBuilder, final ILayoutElement[] root) {
			super(parentBuilder, e -> root[0] = e);
			this.root = root;
		}

		public ILayoutElement build() {

			finalActions.forEach(Runnable::run);

			return root[0];
		}

	}

	//
	public class RowBuilder<T> extends ContainerBuilder<RowBuilder<T>, T> {

		private RowBuilder(final T parentBuilder, final Row row) {
			super(parentBuilder, row.children::add);
		}
	}

	public class GroupBuilder<T> extends ElementBuilder<GroupBuilder<T>, T> {

		private final Group g;

		private GroupBuilder(final T parentBuilder, final Group g) {
			super(parentBuilder, e -> g.child = e);
			this.g = g;
		}

		public GroupBuilder<T> withLabel(final String label) {
			g.label = label;
			return this;
		}
	}

	public class ColumnBuilder<T> extends ContainerBuilder<ColumnBuilder<T>, T> {

		private ColumnBuilder(final T parentBuilder, final Column col) {
			super(parentBuilder, col.children::add);
		}
	}

	public abstract class ElementBuilder<U, T> {

		private final T parentBuilder;
		private final Consumer<ILayoutElement> consumer;

		private ElementBuilder(final T parentBuilder, final Consumer<ILayoutElement> consumer) {
			this.parentBuilder = parentBuilder;
			this.consumer = consumer;
		}

		public T make() {
			return parentBuilder;
		}

		public EditorBuilder<U> withEditor(final UUIDObject input) {
			final EditorField editor = new EditorField();
			consumer.accept(editor);
			return new EditorBuilder<>((U) this, input, editor);
		}

		public U makeLabel(final String label) {
			consumer.accept(new Label(label));
			return (U) this;
		}

		//
		public RowBuilder<U> withRow() {
			final Row row = new Row();
			consumer.accept(row);
			return new RowBuilder<>((U) this, row);
		}

		public ColumnBuilder<U> withColumn() {
			final Column col = new Column();
			consumer.accept(col);
			return new ColumnBuilder<>((U) this, col);
		}

		public GroupBuilder<U> withGroup() {
			final Group g = new Group();
			consumer.accept(g);
			return new GroupBuilder<>((U) this, g);
		}
	}

	public abstract class ContainerBuilder<U, T> extends ElementBuilder<U, T> {

		private final Consumer<ILayoutElement> action;

		private ContainerBuilder(final T parentBuilder, final Consumer<ILayoutElement> action) {
			super(parentBuilder, action);
			this.action = action;
		}

		public U withChildren(final List<ILayoutElement> l) {
			l.forEach(action);
			return (U) this;
		}

		public U addRelectively(final UUIDObject input, final IStatus status) {
			if (input != null) {

				finalActions.add(() -> {
					final IInlineEditorContainer c = new IInlineEditorContainer() {

						@Override
						public @Nullable IInlineEditor addInlineEditor(final IInlineEditor ed) {
							if (ed != null && !seenEditors.contains(Pair.of(input, ed.getFeature()))) {
								if (ed.getFeature() instanceof final EAttribute attrib) {
									withEditor(input).forAttributeAndStatus(attrib, status).make();
								}
							}
							return null;
						}
					};
					final List<IComponentHelper> helpers = Activator.getDefault().getComponentHelperRegistry().getComponentHelpers(input.eClass());
					for (final IComponentHelper helper : helpers) {
						helper.addEditorsToComposite(c);
					}
				});
			}
			return (U) this;
		}

	}

	public class EditorBuilder<T> {

		private final EditorField field;
		private final UUIDObject input;
		private EStructuralFeature feature;
		private final T parentBuilder;

		private EditorBuilder(final T parentBuilder, final UUIDObject input, final EditorField row) {
			this.parentBuilder = parentBuilder;
			this.input = input;
			this.field = row;
		}

		public EditorBuilder<T> forAttributeAndStatus(final EAttribute attrib, final IStatus status) {
			forAttribute(attrib);
			withValidationStatus(status);
			return this;
		}

		public EditorBuilder<T> forReferenceAndStatus(final EReference ref, final IStatus status) {
			forReference(ref);
			withValidationStatus(status);
			return this;
		}

		public EditorBuilder<T> withoutlabel() {
			field.label = null;
			return this;

		}

		public EditorBuilder<T> forAttribute(final EAttribute attrib) {
			this.feature = attrib;

			seenEditors.add(Pair.of(input, attrib));

			field.featureName = attrib.getName();
			field.objectId = input.getUuid();
			field.label = getDisplayLabel(input, attrib);

			field.multi = attrib.isMany();

			
			field.type = "text";
			if (attrib.getEAttributeType() instanceof final EEnum e) {
				field.type = "combo";
				field.allowedValues = new LinkedList<>();
				for (final var v : e.getELiterals()) {
					field.allowedValues.add(Pair.of(v.toString(), v.toString()));
				}
			}
			if (attrib.getEAttributeType() == EcorePackage.Literals.EBOOLEAN) {
				field.type = "toggle";
			}
			if (attrib.getEAttributeType() == DateTimePackage.Literals.YEAR_MONTH) {
				field.type = "month";
			}

			final Object value = input.eGet(attrib);
			if (value != null) {
				field.value = value.toString();
			}

			return this;
		}

		public EditorBuilder<T> forReference(final EReference ref) {
			this.feature = ref;

			seenEditors.add(Pair.of(input, ref));

			field.featureName = ref.getName();
			field.objectId = input.getUuid();
			field.label = getDisplayLabel(input, ref);

			field.type = "text";

			IReferenceValueProvider referenceValueProvider = referenceValueProviderCache.getReferenceValueProvider(input.eClass(), ref);
			List<Pair<String, EObject>> allowedValues = referenceValueProvider.getAllowedValues(input, ref);
			if (allowedValues != null) {
				field.type = "auto-combo";
				field.allowedValues = new LinkedList<>();
				allowedValues.forEach(p -> {
					if (p.getSecond() == null) {
						field.allowedValues.add(Pair.of(p.getFirst(), null));

					} else if (p.getSecond() instanceof UUIDObject u) {
						field.allowedValues.add(Pair.of(p.getFirst(), u.getUuid()));

					}
				});
			}
			field.multi = ref.isMany();
			// field.allowedValues.add(Pair.of(v.getName(), v.getUuid()));

			// field.featureName = typedElement.getName();
			//
			// field.label = getDisplayLabel(input, typedElement);
			// field.type = "text";
			// if (ref.getEReferenceType() == PortPackage.Literals.PORT || ref.getEReferenceType() == TypesPackage.Literals.APORT_SET) {
			// field.type = "auto-combo";
			// field.allowedValues = new LinkedList<>();
			// final PortModel pm = ScenarioModelUtil.getPortModel(sdp);
			// for (final var v : pm.getPorts()) {
			// field.allowedValues.add(Pair.of(v.getName(), v.getUuid()));
			// }
			// if (input.eGet(ref) != null) {
			// final Port v = (Port) input.eGet(ref);
			// if (v != null) {
			// field.value = Pair.of(v.getName(), v.getUuid());// input.eGet(ref).toString();
			// }
			// }
			// }
			//// if (attrib.getEAttributeType() == EcorePackage.Literals.EBOOLEAN) {
			//// field.type = "toggle";
			//// }
			//// if (attrib.getEAttributeType() == DateTimePackage.Literals.YEAR_MONTH) {
			//// field.type = "month";
			//// }
			////

			// if (attrib.getEAttributeType() instanceof final EEnum e) {
			// field.type = "combo";
			// field.allowedValues = new LinkedList<>();
			// for (final var v : e.getELiterals()) {
			// field.allowedValues.add(Pair.of(v.toString(), v.toString()));
			// }
			// }
			// if (attrib.getEAttributeType() == EcorePackage.Literals.EBOOLEAN) {
			// field.type = "toggle";
			// }
			// if (attrib.getEAttributeType() == DateTimePackage.Literals.YEAR_MONTH) {
			// field.type = "month";
			// }
			//
			// final Object value = input.eGet(attrib);
			// if (value != null) {
			// field.value = value.toString();
			// }

			return this;
		}

		public EditorBuilder<T> withValidationStatus(final IStatus status) {

			if (feature == null) {
				throw new IllegalStateException();
			}
			final StringBuilder sb = new StringBuilder();
			final int severity = checkStatus(status, IStatus.OK, input, feature, sb);
			field.severity = severity;
			field.msg = sb.toString();

			return this;
		}

		public T make() {
			return parentBuilder;
		}

		/**
		 * Check status message applies to this editor.
		 * 
		 * @param status
		 * @return
		 */
		protected int checkStatus(final IStatus status, int currentSeverity, final EObject input, final ETypedElement typedElement, final StringBuilder sb) {
			if (status.isMultiStatus()) {
				final IStatus[] children = status.getChildren();
				for (final IStatus element : children) {
					final int severity = checkStatus(element, currentSeverity, input, typedElement, sb);

					// Is severity worse, then note it
					if (severity > currentSeverity) {
						currentSeverity = element.getSeverity();
					}

				}
			}
			if (status instanceof final IDetailConstraintStatus element) {
				final Collection<EObject> objects = element.getObjects();
				if (objects.contains(input)) {
					if (element.getFeaturesForEObject(input).contains(typedElement)) {

						sb.append(element.getMessage());
						sb.append("\n");

						// Is severity worse, then note it
						if (element.getSeverity() > currentSeverity) {
							currentSeverity = element.getSeverity();
						}

						return currentSeverity;
					}
				}
			}

			return currentSeverity;
		}

		private String getDisplayLabel(final EObject input, final ETypedElement typedElement) {
			// Set to blank by default - and replace below if the feature is
			// found
			String toolTip = "";
			String labelText = "";
			// This will fetch the property source of the input object
			final IItemPropertySource inputPropertySource = (IItemPropertySource) FACTORY.adapt(input, IItemPropertySource.class);
			// Iterate through the property descriptors to find a matching
			// descriptor for the feature
			for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(input)) {

				if (typedElement.equals(descriptor.getFeature(input))) {
					toolTip = descriptor.getDescription(input).replace("{0}", EditorUtils.unmangle(input.eClass().getName()).toLowerCase());

					labelText = descriptor.getDisplayName(input);
					break;
				}
			}
			return labelText;
		}
	}

	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}
}

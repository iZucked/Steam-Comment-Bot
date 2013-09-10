package com.mmxlabs.models.ui.properties.factory;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;

import com.google.inject.Module;
import com.mmxlabs.models.ui.properties.extensions.DetailPropertyFactoryExtensionPoint;

/**
 * The {@link DetailPropertyFactoryRegistry} can be used to group {@link DetailPropertyFactoryExtensionPoint}s by category and {@link EClass}. This needs to be combined with a Guice {@link Module}
 * with eclipse registry support;
 * 
 * *
 * 
 * <pre>
 * 
 * &#064;Override
 * protected void configure() {
 * 	install(Peaberry.osgiModule(bundleContext, EclipseRegistry.eclipseRegistry()));
 * 	bind(TypeLiterals.iterable(DetailPropertyFactoryExtensionPoint.class)).toProvider(Peaberry.service(DetailPropertyFactoryExtensionPoint.class).multiple());
 * 	bind(DetailPropertyFactoryRegistry.class);
 * }
 * </pre>
 * 
 * @author Simon Goodall
 * 
 */
public class DetailPropertyFactoryRegistry {

	private final Map<String, Map<EClass, DetailPropertyFactoryExtensionPoint>> factories = new HashMap<>();

	/**
	 * This method is called by the Guice/Peaberry frameworks to inject the known DetailPropertyFactory extensions into this instance.
	 * 
	 * @param factoryExtPoints
	 */
	@Inject
	public void init(final Iterable<DetailPropertyFactoryExtensionPoint> factoryExtPoints) {

		for (final DetailPropertyFactoryExtensionPoint ext : factoryExtPoints) {
			final EClass eClass = ext.getEClass();
			final String category = ext.getCategory();

			final Map<EClass, DetailPropertyFactoryExtensionPoint> map;
			if (factories.containsKey(category)) {
				map = factories.get(category);
			} else {
				map = new HashMap<>();
				factories.put(category, map);
			}
			map.put(eClass, ext);
		}
	}

	public @Nullable
	IDetailPropertyFactory getFactory(@NonNull final String category, @NonNull final EClass eClass) {
		final Map<EClass, DetailPropertyFactoryExtensionPoint> map;
		if (factories.containsKey(category)) {
			map = factories.get(category);
		} else {
			return null;
		}

		final DetailPropertyFactoryExtensionPoint ext;
		if (map.containsKey(eClass)) {
			ext = map.get(eClass);
		} else {
			return null;
		}
		return ext.createDetailPropertyFactory();
	}
}

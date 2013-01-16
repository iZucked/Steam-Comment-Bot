package com.mmxlabs.models.migration.internal;

import javax.inject.Inject;

import org.ops4j.peaberry.Export;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.migration.IMigrationRegistry;

public class MigrationActivator implements BundleActivator {

	private Injector inj;
	@Inject
	private Export<IMigrationRegistry> migrationRegistry;

	public void start(BundleContext bc) throws Exception {
		/* Setup Guice */
		inj = Guice.createInjector(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()), new MigrationActivationModule());
		//
		// /* Create bundle content */
		inj.injectMembers(this);
		//
		// /* Activate content. Call the relevant start()/open() methods */
		// singletonM.start();
		// ...
		// singletonK.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// /* Deactivate content. Call the relevant stop()/close()/dispose() methods */
		migrationRegistry.unput();
		// ...
		// exportN.unput();
		// singletonM.stop();
		// ...
		// singletonK.stop();
		//
		// /* Detach dead content */
		migrationRegistry = null;
		// ...
		// exportN = null;
		// singleton1 = null;
		// ...
		// singletonN = null;
	}

}

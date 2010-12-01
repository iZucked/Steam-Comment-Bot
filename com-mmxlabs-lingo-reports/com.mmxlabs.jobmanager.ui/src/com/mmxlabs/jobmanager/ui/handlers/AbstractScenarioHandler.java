package com.mmxlabs.jobmanager.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;
import scenario.ScenarioPackage;

public abstract class AbstractScenarioHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		ISelection selection = window.getSelectionService().getSelection();
		System.out.println("Selection = " + selection);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection items = (IStructuredSelection) selection;

//			ResourceSet resourceSet = new ResourceSetImpl();
//			resourceSet
//					.getResourceFactoryRegistry()
//					.getExtensionToFactoryMap()
//					.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
//							new XMIResourceFactoryImpl());
//			resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI,
//					ScenarioPackage.eINSTANCE);

			// get the job manager view

			for (final Object x : items.toList()) {
				if (x instanceof Scenario) {
					final Scenario scenario = (Scenario) x;
					handleScenario(event, "scenario", scenario);
				}
//				if (x instanceof IFile) {
//					IFile file = (IFile) x;
//
//					Resource resource = resourceSet.getResource(
//							URI.createFileURI(file.getLocation().toOSString()),
//							true);
//
//					for (EObject e : resource.getContents()) {
//						if (e instanceof Scenario) {
//							handleScenario(event, file.getName(), (Scenario) e);
//						}
//					}
//				}
			}

		}

		return null;
	}

	public abstract void handleScenario(final ExecutionEvent event,
			final String filename, final Scenario scenario);
}

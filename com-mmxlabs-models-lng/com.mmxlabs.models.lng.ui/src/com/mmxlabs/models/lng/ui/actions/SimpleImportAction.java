/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

/**
 * @author hinton
 * 
 */
public class SimpleImportAction extends ImportAction {

	private static final Logger log = LoggerFactory.getLogger(SimpleImportAction.class);
	
	/**
	 */
	public static class FieldInfoProvider {
		private final EObject container;
		private final EReference containment;
		
		public FieldInfoProvider() {
			this(null, null);
		}
		
		public FieldInfoProvider(final EObject container, final EReference containment) {
			this.container = container;
			this.containment = containment;
		}
		
		public EObject getContainer() {
			return container;
		}
		
		public EReference getContainment() {
			return containment;
		}
		
	}

	private final FieldInfoProvider fieldInfoProvider;
	
	/**
	 */
	public SimpleImportAction(final ImportHooksProvider iph, final FieldInfoProvider fip) {
		super(iph);
		this.fieldInfoProvider = fip;
	}
	
	public SimpleImportAction(final ImportHooksProvider iph, final EObject container, final EReference containment) {
		super(iph);
		this.fieldInfoProvider = new FieldInfoProvider(container, containment);
	}
	
	public SimpleImportAction(final IScenarioEditingLocation part, final ScenarioTableViewer viewer) {
		super(part);
		fieldInfoProvider = new FieldInfoProvider() {

			@Override
			public EObject getContainer() {
				return viewer.getCurrentContainer();
			}

			@Override
			public EReference getContainment() {
				return viewer.getCurrentContainment();
			}
			
		};
	}

	@Override
	protected void doImportStages(final DefaultImportContext context) {
		final EObject container = fieldInfoProvider.getContainer();
		final EReference containment = fieldInfoProvider.getContainment();

		final IClassImporter importer = getImporter(containment);
		// open file picker

		final String path = importHooksProvider.getImportFilePath();
				
		if (path == null) {
			return;
		}

		CSVReader reader = null;
		try {
			reader = new FileCSVReader(new File(path), importHooksProvider.getCsvSeparator());
			final Collection<EObject> importedObjects = importer.importObjects(containment.getEReferenceType(), reader, context);
			context.run();
			final Command cmd = mergeImports(container, containment, importedObjects);
			importHooksProvider.getEditingDomain().getCommandStack().execute(cmd);
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException e) {
			}
		}
	}
	
	

	protected IClassImporter getImporter(final EReference containment) {
		return Activator.getDefault().getImporterRegistry().getClassImporter(containment.getEReferenceType());
	}

	protected Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {
		return mergeLists(container, containment, new ArrayList<EObject>(imports));
	}
}

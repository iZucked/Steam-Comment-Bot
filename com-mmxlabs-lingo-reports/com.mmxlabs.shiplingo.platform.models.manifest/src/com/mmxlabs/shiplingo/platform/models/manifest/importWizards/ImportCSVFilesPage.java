/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.importWizards;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.shiplingo.platform.models.manifest.DemoJointModel;

public class ImportCSVFilesPage extends WizardPage {
	private class Chunk {
		public final EClass eClass;
		public final ISubmodelImporter importer;
		public final Map<String, String> keys;
		public Chunk(EClass eClass, ISubmodelImporter importer) {
			super();
			this.eClass = eClass;
			this.importer = importer;
			this.keys = new HashMap<String, String>();
		}
	}
	
	private List<Chunk> chunks = new LinkedList<Chunk>();
	
	protected ImportCSVFilesPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite arg0) {
		final Composite top = new Composite(arg0, SWT.NONE);
		top.setLayout(new GridLayout(1, false));
		for (final EClass subModelClass : DemoJointModel.getSubmodelClasses()) {
			final ISubmodelImporter importer = Activator.getDefault().getImporterRegistry().getSubmodelImporter(subModelClass);
			if (importer == null) continue;
			final Chunk chunk = new Chunk(subModelClass, importer);
			final Map<String, String> parts = importer.getRequiredInputs();
			chunks.add(chunk);

			if (parts.keySet().isEmpty()) continue;
			final Group g = new Group(top, SWT.NONE);
			g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			g.setLayout(new RowLayout(SWT.VERTICAL));
			g.setText(subModelClass.getName());	
			for (final Map.Entry<String, String> entry : parts.entrySet()) {
				final FileFieldEditor ffe = new FileFieldEditor(entry.getKey(), entry.getValue(), g);
				ffe.getTextControl(g).addModifyListener(new ModifyListener() {	
					@Override
					public void modifyText(ModifyEvent e) {
						chunk.keys.put(entry.getKey(), ffe.getStringValue());
					}
				});
			}
		}
		setControl(top);
	}

	public MMXRootObject doImport() {
		final MMXRootObject root = MMXCoreFactory.eINSTANCE.createMMXRootObject();
		final DefaultImportContext context = new DefaultImportContext();
		
		for (final Chunk c : chunks) {
			final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
			for (final String key : c.keys.keySet()) {
				try {
					final CSVReader r = new CSVReader(c.keys.get(key));
					readers.put(key, r);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			root.addSubModel(c.importer.importModel(readers, context));
		}
		
		context.run();
		return root;
	}	
}

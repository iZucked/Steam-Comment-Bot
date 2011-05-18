/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * Reflective export action, counterpart to {@link ImportCSVAction}
 * 
 * TODO support customized formats for contained objects (e.g. price curve
 * export) this may not be possible because not all states are exportable to the
 * required form (e.g. price curves with different dates)
 * 
 * @author Tom Hinton
 * 
 */
public abstract class ExportCSVAction extends Action {
	@Override
	public void run() {
		final List<EObject> objects = getObjectsToExport();
		// check for variadicity
		// TODO only top level variation is supported
		// TODO if two sub-types have separate fields with the same name
		// there will be a problem
		if (objects.size() == 0)
			return;

		// get save file name
		final FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		fileDialog.setFileName("export.csv"); // TODO set a more sensible name
		fileDialog.setFilterExtensions(new String[] { "*.csv" });
		final String saveFilePath = fileDialog.open();
		if (saveFilePath == null)
			return;

		exportObjects(objects, saveFilePath);
	}

	private void exportObjects(final List<EObject> objects,
			final String saveFilePath) {
		final HashSet<EClass> classes = new HashSet<EClass>();
		for (final EObject object : objects) {
			classes.add(object.eClass());
		}

		final LinkedList<EMFPath> paths = new LinkedList<EMFPath>();
		final HashSet<String> existingPathNames = new HashSet<String>();

		for (final EClass eClass : classes) {
			addPaths(new LinkedList<EReference>(), eClass, paths,
					existingPathNames);
		}

		try {
			final BufferedWriter bw = new BufferedWriter(new FileWriter(
					saveFilePath));

			if (classes.size() > 1) {
				bw.append("KIND");
			}
			boolean c3 = false;
			for (final EMFPath p : paths) {
				if (c3)
					bw.append(", ");
				c3 = true;
				bw.write(p.toString());
			}

			for (final EObject object : objects) {
				bw.newLine();
				boolean c = false;
				if (classes.size() > 1) {
					bw.write(object.eClass().getName());
					c = true;
				}
				for (final EMFPath p : paths) {
					if (c)
						bw.write(", ");
					c = true;
					final Object o = p.get(object);
					if (o != null) {
						if (o instanceof EList) {
							// multi-reference
							// TODO put contained objects in another file.
							// in fact, we can even export their contained
							// objects!

							final Object lastPathElement = p
									.getPathComponent(0);

							if (lastPathElement instanceof EReference
									&& ((EReference) lastPathElement)
											.isContainment()) {
								// contained objects belong in another file
								// what is the name of that file?
								final String exportFilename = getName(object)
										+ "-" + p.toString() + ".csv";
								final String adjacentFile = new File(
										saveFilePath).getParent()
										+ "/"
										+ exportFilename;
								bw.write(exportFilename);
								exportObjects((EList) o, adjacentFile);
							} else {
								final EList<EObject> e = (EList) o;
								bw.write("\"");
								boolean c2 = false;
								for (final EObject r : e) {
									if (c2)
										bw.write(",");
									c2 = true;
									bw.write(getName(r));
								}
								bw.write("\"");
							}
						} else if (o instanceof EObject) {
							// single reference
							bw.write(getName((EObject) o));
						} else {
							bw.write(o.toString());
						}
					}
				}
			}

			bw.close();

		} catch (IOException e) {
			// error
			e.printStackTrace();
		}
	}

	/**
	 * @param r
	 * @return
	 */
	private String getName(final EObject r) {
		for (final EAttribute a : r.eClass().getEAllAttributes()) {
			if (a.getName().equalsIgnoreCase("name")
					|| a.getName().equalsIgnoreCase("id")) {
				return (String) r.eGet(a);
			}
		}
		return "";
	}

	private void addPaths(final LinkedList<EReference> pathSoFar,
			final EClass eClass, final LinkedList<EMFPath> paths,
			final HashSet<String> existingNames) {
		for (final EStructuralFeature esf : eClass.getEAllStructuralFeatures()) {
			if (esf instanceof EAttribute) {
				final LinkedList<Object> subPath = new LinkedList<Object>(
						pathSoFar);
				subPath.add(esf);
				final EMFPath p = new EMFPath(true, subPath);
				final String name = p.toString();
				if (existingNames.contains(name) == false) {
					existingNames.add(name);
					paths.add(p);
				}
			} else if (esf instanceof EReference) {
				final EReference ref = (EReference) esf;
				final LinkedList<EReference> subPath = new LinkedList<EReference>(
						pathSoFar);
				subPath.add(ref);
				if ((ref.isMany() == false) && ref.isContainment()) {
					addPaths(subPath, ref.getEReferenceType(), paths,
							existingNames);
				} else {
					final EMFPath p = new EMFPath(true, subPath);
					final String name = p.toString();
					if (existingNames.contains(name) == false) {
						existingNames.add(name);
						paths.add(p);
					}
				}
			}
		}
	}

	protected abstract List<EObject> getObjectsToExport();
}

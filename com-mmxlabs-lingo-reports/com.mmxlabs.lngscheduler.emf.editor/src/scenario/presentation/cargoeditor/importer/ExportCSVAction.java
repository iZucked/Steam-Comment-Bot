/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.lngscheduler.emf.extras.EMFUtils;

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
	public ExportCSVAction() {
		super("Export to CSV", AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui", "$nl$/icons/full/etool16/export_wiz.gif"));
	}

	@Override
	public void run() {
		final List<EObject> objects = getObjectsToExport();
		// check for variadicity
		// TODO only top level variation is supported
		// TODO if two sub-types have separate fields with the same name
		// there will be a problem
		if (objects.size() == 0)
			return;

		final EClass topLevelClass = EMFUtils.findCommonSuperclass(objects);
		final EObjectImporter exporter = EObjectImporterFactory.getInstance()
				.getImporter(topLevelClass);
		final Map<String, Collection<Map<String, String>>> result = exporter
				.exportObjects(objects);

		for (final Map.Entry<String, Collection<Map<String, String>>> entry : result
				.entrySet()) {
			final String group = entry.getKey();

			final FileDialog fileDialog = new FileDialog(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell(),
					SWT.SAVE);
			fileDialog.setText("Choose export location for " + group + "s");
			fileDialog.setFileName(group + ".csv"); // TODO set a more sensible
													// name
			fileDialog.setFilterExtensions(new String[] { "*.csv" });
			final String saveFilePath = fileDialog.open();
			if (saveFilePath == null)
				return;
			writeObjects(saveFilePath, entry.getValue());
		}
	}

	/**
	 * Write out the given maps
	 * 
	 * @param value
	 */
	private void writeObjects(final String filename,
			final Collection<Map<String, String>> value) {
		try {
			final BufferedWriter bw = new BufferedWriter(new FileWriter(
					filename));

			final LinkedHashSet<String> keys = new LinkedHashSet<String>();
			for (final Map<String, String> map : value) {
				keys.addAll(map.keySet());
			}

			boolean comma = false;
			for (final String col : keys) {
				bw.write((comma ? ", " : "") + col);
				comma = true;
			}

			// bw.newLine();

			for (final Map<String, String> row : value) {
				bw.newLine();
				comma = false;
				for (final String col : keys) {
					final String v = row.get(col);
					final String ve = v == null ? "" : (v.contains(",") ? "\""
							+ v.replace("\"", "\"\"") + "\"" : v);
					bw.write((comma ? "," : "") + ve);
					comma = true;
				}
			}

			bw.flush();
			bw.close();
		} catch (final IOException ex) {
			return;
		}
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

			// Show always export KIND as we may only be exporting a single type
			// of object, but it is still part of a set of classes with a common
			// abstract class.
			{
				bw.append("KIND");
			}
			boolean c3 = false;
			for (final EMFPath p : paths) {
				if (c3) {
					bw.append(", ");
					c3 = true;
				}
				bw.write(p.toString());
			}

			for (final EObject object : objects) {
				bw.newLine();
				boolean c = false;
				// KIND Column
				{
					bw.write(object.eClass().getName());
					c = true;
				}
				for (final EMFPath p : paths) {
					if (c) {
						bw.write(", ");
						c = true;
					}
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
									if (c2) {
										bw.write(",");
										c2 = true;
									}
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

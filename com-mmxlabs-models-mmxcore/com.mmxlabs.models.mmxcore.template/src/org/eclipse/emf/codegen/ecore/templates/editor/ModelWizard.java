/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package org.eclipse.emf.codegen.ecore.templates.editor;

import org.eclipse.emf.codegen.ecore.genmodel.*;

public class ModelWizard
{
  protected static String nl;
  public static synchronized ModelWizard create(String lineSeparator)
  {
    nl = lineSeparator;
    ModelWizard result = new ModelWizard();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = "/**";
  protected final String TEXT_3 = NL + " * ";
  protected final String TEXT_4 = NL + " * <copyright>" + NL + " * </copyright>" + NL + " *" + NL + " * ";
  protected final String TEXT_5 = "Id";
  protected final String TEXT_6 = NL + " */";
  protected final String TEXT_7 = NL + "package ";
  protected final String TEXT_8 = ";" + NL + "" + NL + "" + NL + "import java.util.ArrayList;" + NL + "import java.util.Arrays;" + NL + "import java.util.Collection;" + NL + "import java.util.Collections;" + NL + "import java.util.HashMap;";
  protected final String TEXT_9 = NL + "import java.util.Iterator;";
  protected final String TEXT_10 = NL + "import java.util.List;" + NL + "import java.util.Map;" + NL + "import java.util.MissingResourceException;" + NL + "import java.util.StringTokenizer;" + NL;
  protected final String TEXT_11 = NL + "import org.eclipse.emf.common.CommonPlugin;" + NL;
  protected final String TEXT_12 = NL + "import org.eclipse.emf.common.util.URI;" + NL + "" + NL + "import org.eclipse.emf.ecore.EClass;" + NL + "import org.eclipse.emf.ecore.EClassifier;" + NL + "" + NL + "import org.eclipse.emf.ecore.resource.Resource;" + NL + "import org.eclipse.emf.ecore.resource.ResourceSet;" + NL + "" + NL + "import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;" + NL + "" + NL + "import org.eclipse.emf.ecore.EObject;" + NL + "" + NL + "import org.eclipse.emf.ecore.xmi.XMLResource;" + NL + "" + NL + "import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;" + NL;
  protected final String TEXT_13 = NL + "import org.eclipse.core.resources.IContainer;" + NL + "import org.eclipse.core.resources.IFile;" + NL + "import org.eclipse.core.resources.IFolder;" + NL + "import org.eclipse.core.resources.IProject;" + NL + "import org.eclipse.core.resources.IResource;" + NL + "import org.eclipse.core.resources.ResourcesPlugin;" + NL;
  protected final String TEXT_14 = NL + "import org.eclipse.core.runtime.IProgressMonitor;" + NL + "" + NL + "import org.eclipse.jface.dialogs.MessageDialog;" + NL + "" + NL + "import org.eclipse.jface.viewers.IStructuredSelection;" + NL + "" + NL + "import org.eclipse.jface.wizard.Wizard;" + NL + "import org.eclipse.jface.wizard.WizardPage;" + NL + "" + NL + "import org.eclipse.swt.SWT;" + NL + "" + NL + "import org.eclipse.swt.events.ModifyListener;" + NL + "import org.eclipse.swt.events.ModifyEvent;" + NL + "" + NL + "import org.eclipse.swt.layout.GridData;" + NL + "import org.eclipse.swt.layout.GridLayout;" + NL + "" + NL + "import org.eclipse.swt.widgets.Combo;" + NL + "import org.eclipse.swt.widgets.Composite;" + NL + "import org.eclipse.swt.widgets.Label;" + NL + "" + NL + "import org.eclipse.ui.INewWizard;" + NL + "import org.eclipse.ui.IWorkbench;" + NL;
  protected final String TEXT_15 = NL + "import org.eclipse.ui.actions.WorkspaceModifyOperation;" + NL + "" + NL + "import org.eclipse.ui.dialogs.WizardNewFileCreationPage;" + NL + "" + NL + "import org.eclipse.ui.part.FileEditorInput;" + NL + "import org.eclipse.ui.part.ISetSelectionTarget;" + NL;
  protected final String TEXT_16 = NL + "import ";
  protected final String TEXT_17 = ";";
  protected final String TEXT_18 = NL + "import ";
  protected final String TEXT_19 = ";" + NL + "import ";
  protected final String TEXT_20 = ";" + NL + NL;
  protected final String TEXT_21 = NL + NL + NL + "/**" + NL + " * This is a simple wizard for creating a new model file." + NL + " * <!-- begin-user-doc -->" + NL + " * <!-- end-user-doc -->" + NL + " * @generated" + NL + " */" + NL + "public class ";
  protected final String TEXT_22 = " extends Wizard implements INewWizard" + NL + "{";
  protected final String TEXT_23 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic static final ";
  protected final String TEXT_24 = " copyright = ";
  protected final String TEXT_25 = ";";
  protected final String TEXT_26 = NL;
  protected final String TEXT_27 = NL + "\t/**" + NL + "\t * The supported extensions for created files." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic static final ";
  protected final String TEXT_28 = " FILE_EXTENSIONS =" + NL + "\t\tCollections.unmodifiableList(Arrays.asList(";
  protected final String TEXT_29 = ".INSTANCE.getString(\"_UI_";
  protected final String TEXT_30 = "FilenameExtensions\").split(\"\\\\s*,\\\\s*\")));";
  protected final String TEXT_31 = NL + NL + "\t/**" + NL + "\t * A formatted list of supported file extensions, suitable for display." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic static final String FORMATTED_FILE_EXTENSIONS =" + NL + "\t\t";
  protected final String TEXT_32 = ".INSTANCE.getString(\"_UI_";
  protected final String TEXT_33 = "FilenameExtensions\").replaceAll(\"\\\\s*,\\\\s*\", \", \");";
  protected final String TEXT_34 = NL + NL + "\t/**" + NL + "\t * This caches an instance of the model package." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected ";
  protected final String TEXT_35 = " ";
  protected final String TEXT_36 = " = ";
  protected final String TEXT_37 = ".eINSTANCE;" + NL + "" + NL + "\t/**" + NL + "\t * This caches an instance of the model factory." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected ";
  protected final String TEXT_38 = " ";
  protected final String TEXT_39 = " = ";
  protected final String TEXT_40 = ".get";
  protected final String TEXT_41 = "();" + NL;
  protected final String TEXT_42 = NL + "\t/**" + NL + "\t * This is the file creation page." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected ";
  protected final String TEXT_43 = "NewFileCreationPage newFileCreationPage;" + NL;
  protected final String TEXT_44 = NL + "\t/**" + NL + "\t * This is the initial object creation page." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected ";
  protected final String TEXT_45 = "InitialObjectCreationPage initialObjectCreationPage;" + NL + "" + NL + "\t/**" + NL + "\t * Remember the selection during initialization for populating the default container." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected IStructuredSelection selection;" + NL + "" + NL + "\t/**" + NL + "\t * Remember the workbench during initialization." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected IWorkbench workbench;" + NL + "" + NL + "\t/**";
  protected final String TEXT_46 = NL + "\t * Caches the names of the types that can be created as the root object.";
  protected final String TEXT_47 = NL + "\t * Caches the names of the features representing global elements.";
  protected final String TEXT_48 = NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected ";
  protected final String TEXT_49 = " initialObjectNames;" + NL + "" + NL + "\t/**" + NL + "\t * This just records the information." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic void init(IWorkbench workbench, IStructuredSelection selection)" + NL + "\t{" + NL + "\t\tthis.workbench = workbench;" + NL + "\t\tthis.selection = selection;" + NL + "\t\tsetWindowTitle(";
  protected final String TEXT_50 = ".INSTANCE.getString(\"_UI_Wizard_label\"));";
  protected final String TEXT_51 = NL + "\t\tsetDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(";
  protected final String TEXT_52 = ".INSTANCE.getImage(\"full/wizban/New";
  protected final String TEXT_53 = "\")));";
  protected final String TEXT_54 = NL + "\t}" + NL + "" + NL + "\t/**";
  protected final String TEXT_55 = NL + "\t * Returns the names of the types that can be created as the root object.";
  protected final String TEXT_56 = NL + "\t * Returns the names of the features representing global elements.";
  protected final String TEXT_57 = NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected ";
  protected final String TEXT_58 = " getInitialObjectNames()" + NL + "\t{" + NL + "\t\tif (initialObjectNames == null)" + NL + "\t\t{" + NL + "\t\t\tinitialObjectNames = new ";
  protected final String TEXT_59 = "();";
  protected final String TEXT_60 = NL + "\t\t\tfor (EClassifier eClassifier : ";
  protected final String TEXT_61 = ".getEClassifiers())";
  protected final String TEXT_62 = NL + "\t\t\tfor (Iterator classifiers = ";
  protected final String TEXT_63 = ".getEClassifiers().iterator(); classifiers.hasNext(); )";
  protected final String TEXT_64 = NL + "\t\t\t{";
  protected final String TEXT_65 = NL + "\t\t\t\tEClassifier eClassifier = (EClassifier)classifiers.next();";
  protected final String TEXT_66 = NL + "\t\t\t\tif (eClassifier instanceof EClass)" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tEClass eClass = (EClass)eClassifier;" + NL + "\t\t\t\t\tif (!eClass.isAbstract())" + NL + "\t\t\t\t\t{" + NL + "\t\t\t\t\t\tinitialObjectNames.add(eClass.getName());" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}" + NL + "\t\t\t}";
  protected final String TEXT_67 = NL + "\t\t\tfor (";
  protected final String TEXT_68 = " eStructuralFeature : ";
  protected final String TEXT_69 = ".INSTANCE.getAllElements(";
  protected final String TEXT_70 = ".INSTANCE.getDocumentRoot(";
  protected final String TEXT_71 = ")))";
  protected final String TEXT_72 = NL + "\t\t\tfor (Iterator elements = ";
  protected final String TEXT_73 = ".INSTANCE.getAllElements(";
  protected final String TEXT_74 = ".INSTANCE.getDocumentRoot(";
  protected final String TEXT_75 = ")).iterator(); elements.hasNext(); )";
  protected final String TEXT_76 = NL + "\t\t\t{";
  protected final String TEXT_77 = NL + "\t\t\t\t";
  protected final String TEXT_78 = " eStructuralFeature = (";
  protected final String TEXT_79 = ")elements.next();";
  protected final String TEXT_80 = NL + "\t\t\t\tif (eStructuralFeature.isChangeable())" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tEClassifier eClassifier = eStructuralFeature.getEType();" + NL + "\t\t\t\t\tif (eClassifier instanceof EClass)" + NL + "\t\t\t\t\t{" + NL + "\t\t\t\t\t\tEClass eClass = (EClass)eClassifier;" + NL + "\t\t\t\t\t\tif (!eClass.isAbstract())" + NL + "\t\t\t\t\t\t{" + NL + "\t\t\t\t\t\t\tinitialObjectNames.add(eStructuralFeature.getName());" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}" + NL + "\t\t\t}";
  protected final String TEXT_81 = NL + "\t\t\tCollections.sort(initialObjectNames, ";
  protected final String TEXT_82 = "CommonPlugin.INSTANCE.getComparator()";
  protected final String TEXT_83 = "java.text.Collator.getInstance()";
  protected final String TEXT_84 = ");" + NL + "\t\t}" + NL + "\t\treturn initialObjectNames;" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * Create a new model." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected EObject createInitialModel()" + NL + "\t{";
  protected final String TEXT_85 = NL + "\t\tEClass eClass = (EClass)";
  protected final String TEXT_86 = ".getEClassifier(initialObjectCreationPage.getInitialObjectName());" + NL + "\t\tEObject rootObject = ";
  protected final String TEXT_87 = ".create(eClass);";
  protected final String TEXT_88 = NL + "\t\tEClass eClass = ";
  protected final String TEXT_89 = ".INSTANCE.getDocumentRoot(";
  protected final String TEXT_90 = ");" + NL + "\t\tEStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(initialObjectCreationPage.getInitialObjectName());" + NL + "\t\tEObject rootObject = ";
  protected final String TEXT_91 = ".create(eClass);" + NL + "\t\trootObject.eSet(eStructuralFeature, ";
  protected final String TEXT_92 = ".create((EClass)eStructuralFeature.getEType()));";
  protected final String TEXT_93 = NL + "\t\treturn rootObject;" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * Do the work after everything is specified." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_94 = NL + "\t@Override";
  protected final String TEXT_95 = NL + "\tpublic boolean performFinish()" + NL + "\t{" + NL + "\t\ttry" + NL + "\t\t{";
  protected final String TEXT_96 = NL + "\t\t\t// Get the URI of the model file." + NL + "\t\t\t//" + NL + "\t\t\tfinal URI fileURI = getModelURI();" + NL + "\t\t\tif (new ";
  protected final String TEXT_97 = "(fileURI.toFileString()).exists())" + NL + "\t\t\t{" + NL + "\t\t\t\tif (!MessageDialog.openQuestion" + NL + "\t\t\t\t\t\t(getShell()," + NL + "\t\t\t\t\t\t ";
  protected final String TEXT_98 = ".INSTANCE.getString(\"_UI_Question_title\"),";
  protected final String TEXT_99 = NL + "\t\t\t\t\t\t ";
  protected final String TEXT_100 = ".INSTANCE.getString(\"_WARN_FileConflict\", new String []{ fileURI.toFileString() })))";
  protected final String TEXT_101 = NL + "\t\t\t\t{" + NL + "\t\t\t\t\tinitialObjectCreationPage.selectFileField();" + NL + "\t\t\t\t\treturn false;" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\t// Do the work within an operation." + NL + "\t\t\t//" + NL + "\t\t\t";
  protected final String TEXT_102 = " operation = new ";
  protected final String TEXT_103 = "()" + NL + "\t\t\t{" + NL + "\t\t\t\tpublic void run(IProgressMonitor progressMonitor)";
  protected final String TEXT_104 = NL + "\t\t\t// Remember the file." + NL + "\t\t\t//" + NL + "\t\t\tfinal IFile modelFile = getModelFile();" + NL + "" + NL + "\t\t\t// Do the work within an operation." + NL + "\t\t\t//" + NL + "\t\t\tWorkspaceModifyOperation operation =" + NL + "\t\t\t\tnew WorkspaceModifyOperation()" + NL + "\t\t\t\t{";
  protected final String TEXT_105 = NL + "\t\t\t\t\t@Override";
  protected final String TEXT_106 = NL + "\t\t\t\t\tprotected void execute(IProgressMonitor progressMonitor)";
  protected final String TEXT_107 = NL + "\t\t\t\t\t{" + NL + "\t\t\t\t\t\ttry" + NL + "\t\t\t\t\t\t{" + NL + "\t\t\t\t\t\t\t// Create a resource set" + NL + "\t\t\t\t\t\t\t//" + NL + "\t\t\t\t\t\t\tResourceSet resourceSet = new ResourceSetImpl();" + NL;
  protected final String TEXT_108 = NL + "\t\t\t\t\t\t\t// Get the URI of the model file." + NL + "\t\t\t\t\t\t\t//" + NL + "\t\t\t\t\t\t\tURI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);" + NL;
  protected final String TEXT_109 = NL + "\t\t\t\t\t\t\t// Create a resource for this file." + NL + "\t\t\t\t\t\t\t//" + NL + "\t\t\t\t\t\t\tResource resource = resourceSet.createResource(fileURI";
  protected final String TEXT_110 = ", ";
  protected final String TEXT_111 = ".eCONTENT_TYPE";
  protected final String TEXT_112 = ");" + NL + "" + NL + "\t\t\t\t\t\t\t// Add the initial model object to the contents." + NL + "\t\t\t\t\t\t\t//" + NL + "\t\t\t\t\t\t\tEObject rootObject = createInitialModel();" + NL + "\t\t\t\t\t\t\tif (rootObject != null)" + NL + "\t\t\t\t\t\t\t{" + NL + "\t\t\t\t\t\t\t\tresource.getContents().add(rootObject);" + NL + "\t\t\t\t\t\t\t}" + NL + "" + NL + "\t\t\t\t\t\t\t// Save the contents of the resource to the file system." + NL + "\t\t\t\t\t\t\t//" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_113 = " options = new ";
  protected final String TEXT_114 = "();" + NL + "\t\t\t\t\t\t\toptions.put(XMLResource.OPTION_ENCODING, initialObjectCreationPage.getEncoding());" + NL + "\t\t\t\t\t\t\tresource.save(options);" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\tcatch (Exception exception)" + NL + "\t\t\t\t\t\t{" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_115 = ".INSTANCE.log(exception);" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\tfinally" + NL + "\t\t\t\t\t\t{" + NL + "\t\t\t\t\t\t\tprogressMonitor.done();" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t};" + NL + "" + NL + "\t\t\tgetContainer().run(false, false, operation);" + NL;
  protected final String TEXT_116 = NL + "\t\t\treturn ";
  protected final String TEXT_117 = ".openEditor(workbench, fileURI);\t\t\t";
  protected final String TEXT_118 = NL + "\t\t\t// Select the new file resource in the current view." + NL + "\t\t\t//" + NL + "\t\t\t";
  protected final String TEXT_119 = " workbenchWindow = workbench.getActiveWorkbenchWindow();" + NL + "\t\t\t";
  protected final String TEXT_120 = " page = workbenchWindow.getActivePage();" + NL + "\t\t\tfinal ";
  protected final String TEXT_121 = " activePart = page.getActivePart();" + NL + "\t\t\tif (activePart instanceof ISetSelectionTarget)" + NL + "\t\t\t{" + NL + "\t\t\t\tfinal ";
  protected final String TEXT_122 = " targetSelection = new ";
  protected final String TEXT_123 = "(modelFile);" + NL + "\t\t\t\tgetShell().getDisplay().asyncExec" + NL + "\t\t\t\t\t(new Runnable()" + NL + "\t\t\t\t\t {" + NL + "\t\t\t\t\t\t public void run()" + NL + "\t\t\t\t\t\t {" + NL + "\t\t\t\t\t\t\t ((ISetSelectionTarget)activePart).selectReveal(targetSelection);" + NL + "\t\t\t\t\t\t }" + NL + "\t\t\t\t\t });" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\t// Open an editor on the new file." + NL + "\t\t\t//" + NL + "\t\t\ttry" + NL + "\t\t\t{" + NL + "\t\t\t\tpage.openEditor" + NL + "\t\t\t\t\t(new FileEditorInput(modelFile)," + NL + "\t\t\t\t\t workbench.getEditorRegistry().getDefaultEditor";
  protected final String TEXT_124 = "(modelFile.getFullPath().toString()).getId());";
  protected final String TEXT_125 = NL + "\t\t\t\t\t \t(modelFile.getFullPath().toString()," + NL + "\t\t\t\t\t \t ";
  protected final String TEXT_126 = ".getContentTypeManager().getContentType(";
  protected final String TEXT_127 = ".eCONTENT_TYPE)).getId());";
  protected final String TEXT_128 = "\t\t\t\t\t \t " + NL + "\t\t\t}" + NL + "\t\t\tcatch (";
  protected final String TEXT_129 = " exception)" + NL + "\t\t\t{" + NL + "\t\t\t\tMessageDialog.openError(workbenchWindow.getShell(), ";
  protected final String TEXT_130 = ".INSTANCE.getString(\"_UI_OpenEditorError_label\"), exception.getMessage());";
  protected final String TEXT_131 = NL + "\t\t\t\treturn false;" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\treturn true;";
  protected final String TEXT_132 = NL + "\t\t}" + NL + "\t\tcatch (Exception exception)" + NL + "\t\t{" + NL + "\t\t\t";
  protected final String TEXT_133 = ".INSTANCE.log(exception);" + NL + "\t\t\treturn false;" + NL + "\t\t}" + NL + "\t}" + NL;
  protected final String TEXT_134 = NL + "\t/**" + NL + "\t * This is the one page of the wizard." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic class ";
  protected final String TEXT_135 = "NewFileCreationPage extends WizardNewFileCreationPage" + NL + "\t{" + NL + "\t\t/**" + NL + "\t\t * Pass in the selection." + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic ";
  protected final String TEXT_136 = "NewFileCreationPage(String pageId, IStructuredSelection selection)" + NL + "\t\t{" + NL + "\t\t\tsuper(pageId, selection);" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * The framework calls this to see if the file is correct." + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */";
  protected final String TEXT_137 = NL + "\t\t@Override";
  protected final String TEXT_138 = NL + "\t\tprotected boolean validatePage()" + NL + "\t\t{" + NL + "\t\t\tif (super.validatePage())" + NL + "\t\t\t{" + NL + "\t\t\t\tString extension = new ";
  protected final String TEXT_139 = "(getFileName()).getFileExtension();" + NL + "\t\t\t\tif (extension == null || !FILE_EXTENSIONS.contains(extension))" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tString key = FILE_EXTENSIONS.size() > 1 ? \"_WARN_FilenameExtensions\" : \"_WARN_FilenameExtension\";";
  protected final String TEXT_140 = NL + "\t\t\t\t\tsetErrorMessage(";
  protected final String TEXT_141 = ".INSTANCE.getString(key, new Object [] { FORMATTED_FILE_EXTENSIONS }));" + NL + "\t\t\t\t\treturn false;" + NL + "\t\t\t\t}" + NL + "\t\t\t\treturn true;" + NL + "\t\t\t}" + NL + "\t\t\treturn false;" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic IFile getModelFile()" + NL + "\t\t{" + NL + "\t\t\treturn ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));" + NL + "\t\t}" + NL + "\t}" + NL;
  protected final String TEXT_142 = NL + "\t/**" + NL + "\t * This is the page where the type of object to create is selected." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic class ";
  protected final String TEXT_143 = "InitialObjectCreationPage extends WizardPage" + NL + "\t{";
  protected final String TEXT_144 = NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tprotected ";
  protected final String TEXT_145 = " fileField;" + NL;
  protected final String TEXT_146 = NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tprotected Combo initialObjectField;" + NL + "" + NL + "\t\t/**" + NL + "\t\t * @generated" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t */" + NL + "\t\tprotected ";
  protected final String TEXT_147 = " encodings;" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tprotected Combo encodingField;" + NL + "" + NL + "\t\t/**" + NL + "\t\t * Pass in the selection." + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic ";
  protected final String TEXT_148 = "InitialObjectCreationPage(String pageId)" + NL + "\t\t{" + NL + "\t\t\tsuper(pageId);" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic void createControl(Composite parent)" + NL + "\t\t{" + NL + "\t\t\tComposite composite = new Composite(parent, SWT.NONE);" + NL + "\t\t\t{" + NL + "\t\t\t\tGridLayout layout = new GridLayout();" + NL + "\t\t\t\tlayout.numColumns = 1;" + NL + "\t\t\t\tlayout.verticalSpacing = 12;" + NL + "\t\t\t\tcomposite.setLayout(layout);" + NL + "" + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.verticalAlignment = GridData.FILL;" + NL + "\t\t\t\tdata.grabExcessVerticalSpace = true;" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tcomposite.setLayoutData(data);" + NL + "\t\t\t}" + NL;
  protected final String TEXT_149 = "\t\t\t" + NL + "\t\t\tLabel resourceURILabel = new Label(composite, SWT.LEFT);" + NL + "\t\t\t{" + NL + "\t\t\t\tresourceURILabel.setText(";
  protected final String TEXT_150 = ".INSTANCE.getString(\"_UI_File_label\"));";
  protected final String TEXT_151 = NL + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tresourceURILabel.setLayoutData(data);" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tComposite fileComposite = new Composite(composite, SWT.NONE);" + NL + "\t\t\t{" + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.END;" + NL + "\t\t\t\tfileComposite.setLayoutData(data);" + NL + "" + NL + "\t\t\t\tGridLayout layout = new GridLayout();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tlayout.marginHeight = 0;" + NL + "\t\t\t\tlayout.marginWidth = 0;" + NL + "\t\t\t\tlayout.numColumns = 2;" + NL + "\t\t\t\tfileComposite.setLayout(layout);" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tfileField = new ";
  protected final String TEXT_152 = "(fileComposite, SWT.BORDER);" + NL + "\t\t\t{" + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tdata.grabExcessHorizontalSpace = true;" + NL + "\t\t\t\tdata.horizontalSpan = 1;" + NL + "\t\t\t\tfileField.setLayoutData(data);" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tfileField.addModifyListener(validator);" + NL;
  protected final String TEXT_153 = NL + "\t\t\t";
  protected final String TEXT_154 = " resourceURIBrowseFileSystemButton = new ";
  protected final String TEXT_155 = "(fileComposite, SWT.PUSH);" + NL + "\t\t\tresourceURIBrowseFileSystemButton.setText(";
  protected final String TEXT_156 = ".INSTANCE.getString(\"_UI_Browse_label\"));";
  protected final String TEXT_157 = NL + NL + "\t\t\tresourceURIBrowseFileSystemButton.addSelectionListener" + NL + "\t\t\t\t(new ";
  protected final String TEXT_158 = "()" + NL + "\t\t\t\t {";
  protected final String TEXT_159 = NL + "\t\t\t\t\t @Override";
  protected final String TEXT_160 = NL + "\t\t\t\t\t public void widgetSelected(";
  protected final String TEXT_161 = " event)" + NL + "\t\t\t\t\t {" + NL + "\t\t\t\t\t\t String[] filters = ";
  protected final String TEXT_162 = "(String[])";
  protected final String TEXT_163 = ".FILE_EXTENSION_FILTERS.toArray(new String[";
  protected final String TEXT_164 = ".FILE_EXTENSION_FILTERS.size()]);" + NL + "\t\t\t\t\t\t String[] files = ";
  protected final String TEXT_165 = ".openFilePathDialog(getShell(), ";
  protected final String TEXT_166 = ".SAVE, filters);" + NL + "\t\t\t\t\t\t if (files.length > 0)" + NL + "\t\t\t\t\t\t {" + NL + "\t\t\t\t\t\t\t fileField.setText(files[0]);" + NL + "\t\t\t\t\t\t }" + NL + "\t\t\t\t\t }" + NL + "\t\t\t\t });";
  protected final String TEXT_167 = NL + "\t\t\tLabel containerLabel = new Label(composite, SWT.LEFT);" + NL + "\t\t\t{" + NL + "\t\t\t\tcontainerLabel.setText(";
  protected final String TEXT_168 = ".INSTANCE.getString(\"_UI_ModelObject\"));";
  protected final String TEXT_169 = NL + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tcontainerLabel.setLayoutData(data);" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tinitialObjectField = new Combo(composite, SWT.BORDER);" + NL + "\t\t\t{" + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tdata.grabExcessHorizontalSpace = true;" + NL + "\t\t\t\tinitialObjectField.setLayoutData(data);" + NL + "\t\t\t}" + NL;
  protected final String TEXT_170 = NL + "\t\t\tfor (String objectName : getInitialObjectNames())" + NL + "\t\t\t{" + NL + "\t\t\t\tinitialObjectField.add(getLabel(objectName));" + NL + "\t\t\t}";
  protected final String TEXT_171 = NL + "\t\t\tfor (Iterator i = getInitialObjectNames().iterator(); i.hasNext(); )" + NL + "\t\t\t{" + NL + "\t\t\t\tinitialObjectField.add(getLabel((String)i.next()));" + NL + "\t\t\t}";
  protected final String TEXT_172 = NL + NL + "\t\t\tif (initialObjectField.getItemCount() == 1)" + NL + "\t\t\t{" + NL + "\t\t\t\tinitialObjectField.select(0);" + NL + "\t\t\t}" + NL + "\t\t\tinitialObjectField.addModifyListener(validator);" + NL + "" + NL + "\t\t\tLabel encodingLabel = new Label(composite, SWT.LEFT);" + NL + "\t\t\t{" + NL + "\t\t\t\tencodingLabel.setText(";
  protected final String TEXT_173 = ".INSTANCE.getString(\"_UI_XMLEncoding\"));";
  protected final String TEXT_174 = NL + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tencodingLabel.setLayoutData(data);" + NL + "\t\t\t}" + NL + "\t\t\tencodingField = new Combo(composite, SWT.BORDER);" + NL + "\t\t\t{" + NL + "\t\t\t\tGridData data = new GridData();" + NL + "\t\t\t\tdata.horizontalAlignment = GridData.FILL;" + NL + "\t\t\t\tdata.grabExcessHorizontalSpace = true;" + NL + "\t\t\t\tencodingField.setLayoutData(data);" + NL + "\t\t\t}" + NL;
  protected final String TEXT_175 = NL + "\t\t\tfor (String encoding : getEncodings())" + NL + "\t\t\t{" + NL + "\t\t\t\tencodingField.add(encoding);" + NL + "\t\t\t}";
  protected final String TEXT_176 = NL + "\t\t\tfor (Iterator i = getEncodings().iterator(); i.hasNext(); )" + NL + "\t\t\t{" + NL + "\t\t\t\tencodingField.add((String)i.next());" + NL + "\t\t\t}";
  protected final String TEXT_177 = NL + NL + "\t\t\tencodingField.select(0);" + NL + "\t\t\tencodingField.addModifyListener(validator);" + NL + "" + NL + "\t\t\tsetPageComplete(validatePage());" + NL + "\t\t\tsetControl(composite);" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tprotected ModifyListener validator =" + NL + "\t\t\tnew ModifyListener()" + NL + "\t\t\t{" + NL + "\t\t\t\tpublic void modifyText(ModifyEvent e)" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tsetPageComplete(validatePage());" + NL + "\t\t\t\t}" + NL + "\t\t\t};" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tprotected boolean validatePage()" + NL + "\t\t{";
  protected final String TEXT_178 = NL + "\t\t\tURI fileURI = getFileURI();" + NL + "\t\t\tif (fileURI == null || fileURI.isEmpty())" + NL + "\t\t\t{" + NL + "\t\t\t\tsetErrorMessage(null);" + NL + "\t\t\t\treturn false;" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tString extension = fileURI.fileExtension();" + NL + "\t\t\tif (extension == null || !FILE_EXTENSIONS.contains(extension))" + NL + "\t\t\t{" + NL + "\t\t\t\tString key = FILE_EXTENSIONS.size() > 1 ? \"_WARN_FilenameExtensions\" : \"_WARN_FilenameExtension\";";
  protected final String TEXT_179 = NL + "\t\t\t\tsetErrorMessage(";
  protected final String TEXT_180 = ".INSTANCE.getString(key, new Object [] { FORMATTED_FILE_EXTENSIONS }));" + NL + "\t\t\t\treturn false;" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tsetErrorMessage(null);";
  protected final String TEXT_181 = NL + "\t\t\treturn getInitialObjectName() != null && getEncodings().contains(encodingField.getText());" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */";
  protected final String TEXT_182 = NL + "\t\t@Override";
  protected final String TEXT_183 = NL + "\t\tpublic void setVisible(boolean visible)" + NL + "\t\t{" + NL + "\t\t\tsuper.setVisible(visible);" + NL + "\t\t\tif (visible)" + NL + "\t\t\t{";
  protected final String TEXT_184 = NL + "\t\t\t\tif (initialObjectField.getItemCount() == 1)" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tinitialObjectField.clearSelection();" + NL + "\t\t\t\t\tencodingField.setFocus();" + NL + "\t\t\t\t}" + NL + "\t\t\t\telse" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tencodingField.clearSelection();" + NL + "\t\t\t\t\tinitialObjectField.setFocus();" + NL + "\t\t\t\t}";
  protected final String TEXT_185 = NL + "\t\t\t\tinitialObjectField.clearSelection();" + NL + "\t\t\t\tencodingField.clearSelection();" + NL + "\t\t\t\tfileField.setFocus();";
  protected final String TEXT_186 = NL + "\t\t\t}" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic String getInitialObjectName()" + NL + "\t\t{" + NL + "\t\t\tString label = initialObjectField.getText();" + NL;
  protected final String TEXT_187 = NL + "\t\t\tfor (String name : getInitialObjectNames())";
  protected final String TEXT_188 = NL + "\t\t\tfor (Iterator i = getInitialObjectNames().iterator(); i.hasNext(); )";
  protected final String TEXT_189 = NL + "\t\t\t{";
  protected final String TEXT_190 = NL + "\t\t\t\tString name = (String)i.next();";
  protected final String TEXT_191 = NL + "\t\t\t\tif (getLabel(name).equals(label))" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\treturn name;" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t\treturn null;" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic String getEncoding()" + NL + "\t\t{" + NL + "\t\t\treturn encodingField.getText();" + NL + "\t\t}";
  protected final String TEXT_192 = NL + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic URI getFileURI()" + NL + "\t\t{" + NL + "\t\t\ttry" + NL + "\t\t\t{" + NL + "\t\t\t\treturn URI.createFileURI(fileField.getText());" + NL + "\t\t\t}" + NL + "\t\t\tcatch (Exception exception)" + NL + "\t\t\t{" + NL + "\t\t\t\t// Ignore" + NL + "\t\t\t}" + NL + "\t\t\treturn null;" + NL + "\t\t}" + NL + "" + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tpublic void selectFileField()" + NL + "\t\t{" + NL + "\t\t\t\tinitialObjectField.clearSelection();" + NL + "\t\t\t\tencodingField.clearSelection();" + NL + "\t\t\t\tfileField.selectAll();" + NL + "\t\t\t\tfileField.setFocus();" + NL + "\t\t}\t\t";
  protected final String TEXT_193 = NL + NL + "\t\t/**" + NL + "\t\t * Returns the label for the specified ";
  protected final String TEXT_194 = " name." + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tprotected String getLabel(String ";
  protected final String TEXT_195 = "Name)" + NL + "\t\t{" + NL + "\t\t\ttry" + NL + "\t\t\t{" + NL + "\t\t\t\treturn ";
  protected final String TEXT_196 = ".INSTANCE.getString(\"";
  protected final String TEXT_197 = "\" + ";
  protected final String TEXT_198 = "Name + \"_";
  protected final String TEXT_199 = "\");";
  protected final String TEXT_200 = NL + "\t\t\t}" + NL + "\t\t\tcatch(MissingResourceException mre)" + NL + "\t\t\t{" + NL + "\t\t\t\t";
  protected final String TEXT_201 = ".INSTANCE.log(mre);" + NL + "\t\t\t}" + NL + "\t\t\treturn ";
  protected final String TEXT_202 = "Name;" + NL + "\t\t}";
  protected final String TEXT_203 = NL + NL + "\t\t/**" + NL + "\t\t * <!-- begin-user-doc -->" + NL + "\t\t * <!-- end-user-doc -->" + NL + "\t\t * @generated" + NL + "\t\t */" + NL + "\t\tprotected ";
  protected final String TEXT_204 = " getEncodings()" + NL + "\t\t{" + NL + "\t\t\tif (encodings == null)" + NL + "\t\t\t{" + NL + "\t\t\t\tencodings = new ";
  protected final String TEXT_205 = "();" + NL + "\t\t\t\tfor (StringTokenizer stringTokenizer = new StringTokenizer(";
  protected final String TEXT_206 = ".INSTANCE.getString(\"_UI_XMLEncodingChoices\")); stringTokenizer.hasMoreTokens(); )";
  protected final String TEXT_207 = NL + "\t\t\t\t{" + NL + "\t\t\t\t\tencodings.add(stringTokenizer.nextToken());" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t\treturn encodings;" + NL + "\t\t}" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * The framework calls this to create the contents of the wizard." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_208 = NL + "\t\t@Override";
  protected final String TEXT_209 = NL + "\tpublic void addPages()" + NL + "\t{";
  protected final String TEXT_210 = NL + "\t\t// Create a page, set the title, and the initial model file name." + NL + "\t\t//" + NL + "\t\tnewFileCreationPage = new ";
  protected final String TEXT_211 = "NewFileCreationPage(\"Whatever\", selection);";
  protected final String TEXT_212 = NL + "\t\tnewFileCreationPage.setTitle(";
  protected final String TEXT_213 = ".INSTANCE.getString(\"_UI_";
  protected final String TEXT_214 = "_label\"));";
  protected final String TEXT_215 = NL + "\t\tnewFileCreationPage.setDescription(";
  protected final String TEXT_216 = ".INSTANCE.getString(\"_UI_";
  protected final String TEXT_217 = "_description\"));";
  protected final String TEXT_218 = NL + "\t\tnewFileCreationPage.setFileName(";
  protected final String TEXT_219 = ".INSTANCE.getString(\"_UI_";
  protected final String TEXT_220 = "FilenameDefaultBase\") + \".\" + ";
  protected final String TEXT_221 = "(String)";
  protected final String TEXT_222 = "FILE_EXTENSIONS.get(0));";
  protected final String TEXT_223 = NL + "\t\taddPage(newFileCreationPage);" + NL + "" + NL + "\t\t// Try and get the resource selection to determine a current directory for the file dialog." + NL + "\t\t//" + NL + "\t\tif (selection != null && !selection.isEmpty())" + NL + "\t\t{" + NL + "\t\t\t// Get the resource..." + NL + "\t\t\t//" + NL + "\t\t\tObject selectedElement = selection.iterator().next();" + NL + "\t\t\tif (selectedElement instanceof IResource)" + NL + "\t\t\t{" + NL + "\t\t\t\t// Get the resource parent, if its a file." + NL + "\t\t\t\t//" + NL + "\t\t\t\tIResource selectedResource = (IResource)selectedElement;" + NL + "\t\t\t\tif (selectedResource.getType() == IResource.FILE)" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tselectedResource = selectedResource.getParent();" + NL + "\t\t\t\t}" + NL + "" + NL + "\t\t\t\t// This gives us a directory..." + NL + "\t\t\t\t//" + NL + "\t\t\t\tif (selectedResource instanceof IFolder || selectedResource instanceof IProject)" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\t// Set this for the container." + NL + "\t\t\t\t\t//" + NL + "\t\t\t\t\tnewFileCreationPage.setContainerFullPath(selectedResource.getFullPath());" + NL + "" + NL + "\t\t\t\t\t// Make up a unique new name here." + NL + "\t\t\t\t\t//" + NL + "\t\t\t\t\tString defaultModelBaseFilename = ";
  protected final String TEXT_224 = ".INSTANCE.getString(\"_UI_";
  protected final String TEXT_225 = "FilenameDefaultBase\");";
  protected final String TEXT_226 = NL + "\t\t\t\t\tString defaultModelFilenameExtension = ";
  protected final String TEXT_227 = "(String)";
  protected final String TEXT_228 = "FILE_EXTENSIONS.get(0);" + NL + "\t\t\t\t\tString modelFilename = defaultModelBaseFilename + \".\" + defaultModelFilenameExtension;";
  protected final String TEXT_229 = NL + "\t\t\t\t\tfor (int i = 1; ((IContainer)selectedResource).findMember(modelFilename) != null; ++i)" + NL + "\t\t\t\t\t{" + NL + "\t\t\t\t\t\tmodelFilename = defaultModelBaseFilename + i + \".\" + defaultModelFilenameExtension;";
  protected final String TEXT_230 = NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\tnewFileCreationPage.setFileName(modelFilename);" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t}";
  protected final String TEXT_231 = NL + "\t\tinitialObjectCreationPage = new ";
  protected final String TEXT_232 = "InitialObjectCreationPage(\"Whatever2\");";
  protected final String TEXT_233 = NL + "\t\tinitialObjectCreationPage.setTitle(";
  protected final String TEXT_234 = ".INSTANCE.getString(\"_UI_";
  protected final String TEXT_235 = "_label\"));";
  protected final String TEXT_236 = NL + "\t\tinitialObjectCreationPage.setDescription(";
  protected final String TEXT_237 = ".INSTANCE.getString(\"_UI_Wizard_initial_object_description\"));";
  protected final String TEXT_238 = NL + "\t\taddPage(initialObjectCreationPage);" + NL + "\t}" + NL;
  protected final String TEXT_239 = NL + "\t/**" + NL + "\t * Get the URI from the page." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic URI getModelURI()" + NL + "\t{" + NL + "\t\treturn initialObjectCreationPage.getFileURI();" + NL + "\t}" + NL;
  protected final String TEXT_240 = NL + "\t/**" + NL + "\t * Get the file from the page." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic IFile getModelFile()" + NL + "\t{" + NL + "\t\treturn newFileCreationPage.getModelFile();" + NL + "\t}" + NL;
  protected final String TEXT_241 = NL + "}";
  protected final String TEXT_242 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/**
 * <copyright>
 *
 * Copyright (c) 2002-2010 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 */

    GenPackage genPackage = (GenPackage)argument; GenModel genModel=genPackage.getGenModel();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    {GenBase copyrightHolder = argument instanceof GenBase ? (GenBase)argument : argument instanceof Object[] && ((Object[])argument)[0] instanceof GenBase ? (GenBase)((Object[])argument)[0] : null;
    if (copyrightHolder != null && copyrightHolder.hasCopyright()) {
    stringBuffer.append(TEXT_3);
    stringBuffer.append(copyrightHolder.getCopyright(copyrightHolder.getGenModel().getIndentation(stringBuffer)));
    } else {
    stringBuffer.append(TEXT_4);
    stringBuffer.append("$");
    stringBuffer.append(TEXT_5);
    stringBuffer.append("$");
    }}
    stringBuffer.append(TEXT_6);
    String _ListOfString = "List" + (genModel.useGenerics() ? "<" + genModel.getImportedName("java.lang.String") + ">" : "");
    String _ArrayListOfString = "ArrayList" + (genModel.useGenerics() ? "<" + genModel.getImportedName("java.lang.String") + ">" : "");
    String _CollectionOfString = "Collection" + (genModel.useGenerics() ? "<" + genModel.getImportedName("java.lang.String") + ">" : "");
    String _MapOfObjectToObject = "Map" + (genModel.useGenerics() ? "<" + genModel.getImportedName("java.lang.Object") + ", " + genModel.getImportedName("java.lang.Object") + ">" : "");
    String _HashMapOfObjectToObject = "HashMap" + (genModel.useGenerics() ? "<" + genModel.getImportedName("java.lang.Object") + ", " + genModel.getImportedName("java.lang.Object") + ">" : "");
    stringBuffer.append(TEXT_7);
    stringBuffer.append(genPackage.getPresentationPackageName());
    stringBuffer.append(TEXT_8);
    if (!genModel.useGenerics()) {
    stringBuffer.append(TEXT_9);
    }
    stringBuffer.append(TEXT_10);
    if (!genModel.isRichClientPlatform() && genModel.getComplianceLevel().getValue() >= GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_11);
    }
    stringBuffer.append(TEXT_12);
    if (!genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_13);
    }
    stringBuffer.append(TEXT_14);
    if (!genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_15);
    }
    if (!genModel.isSuppressEMFMetaData()) {
    stringBuffer.append(TEXT_16);
    stringBuffer.append(genPackage.getQualifiedFactoryInterfaceName());
    stringBuffer.append(TEXT_17);
    }
    stringBuffer.append(TEXT_18);
    stringBuffer.append(genPackage.getQualifiedPackageInterfaceName());
    stringBuffer.append(TEXT_19);
    stringBuffer.append(genPackage.getQualifiedEditPluginClassName());
    stringBuffer.append(TEXT_20);
    genModel.markImportLocation(stringBuffer);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_22);
    if (genModel.hasCopyrightField()) {
    stringBuffer.append(TEXT_23);
    stringBuffer.append(genModel.getImportedName("java.lang.String"));
    stringBuffer.append(TEXT_24);
    stringBuffer.append(genModel.getCopyrightFieldLiteral());
    stringBuffer.append(TEXT_25);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_26);
    }
    stringBuffer.append(TEXT_27);
    stringBuffer.append(_ListOfString);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_29);
    stringBuffer.append(genPackage.getEditorClassName());
    stringBuffer.append(TEXT_30);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_31);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_32);
    stringBuffer.append(genPackage.getEditorClassName());
    stringBuffer.append(TEXT_33);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(genModel.getNonNLS(3));
    stringBuffer.append(TEXT_34);
    stringBuffer.append(genModel.isSuppressEMFMetaData() ? genModel.getImportedName("org.eclipse.emf.ecore.EPackage") : genPackage.getPackageInterfaceName());
    stringBuffer.append(TEXT_35);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_36);
    stringBuffer.append(genPackage.getPackageInterfaceName());
    stringBuffer.append(TEXT_37);
    stringBuffer.append(genModel.isSuppressEMFMetaData() ? genModel.getImportedName("org.eclipse.emf.ecore.EFactory") : genPackage.getFactoryInterfaceName());
    stringBuffer.append(TEXT_38);
    stringBuffer.append(genPackage.getUncapFactoryName());
    stringBuffer.append(TEXT_39);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_40);
    stringBuffer.append(genModel.isSuppressEMFMetaData() ? "EFactoryInstance" : genPackage.getFactoryName());
    stringBuffer.append(TEXT_41);
    if (!genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_42);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_43);
    }
    stringBuffer.append(TEXT_44);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_45);
    if (!genPackage.hasDocumentRoot()) {
    stringBuffer.append(TEXT_46);
    } else {
    stringBuffer.append(TEXT_47);
    }
    stringBuffer.append(TEXT_48);
    stringBuffer.append(_ListOfString);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_50);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_51);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_52);
    stringBuffer.append(genPackage.getPrefix());
    stringBuffer.append(TEXT_53);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_54);
    if (!genPackage.hasDocumentRoot()) {
    stringBuffer.append(TEXT_55);
    } else {
    stringBuffer.append(TEXT_56);
    }
    stringBuffer.append(TEXT_57);
    stringBuffer.append(_CollectionOfString);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(_ArrayListOfString);
    stringBuffer.append(TEXT_59);
    if (!genPackage.hasDocumentRoot()) {
    if (genModel.useGenerics()) {
    stringBuffer.append(TEXT_60);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_61);
    } else {
    stringBuffer.append(TEXT_62);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_63);
    }
    stringBuffer.append(TEXT_64);
    if (!genModel.useGenerics()) {
    stringBuffer.append(TEXT_65);
    }
    stringBuffer.append(TEXT_66);
    } else {
    if (genModel.useGenerics()) {
    stringBuffer.append(TEXT_67);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EStructuralFeature"));
    stringBuffer.append(TEXT_68);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.ExtendedMetaData"));
    stringBuffer.append(TEXT_69);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.ExtendedMetaData"));
    stringBuffer.append(TEXT_70);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_71);
    } else {
    stringBuffer.append(TEXT_72);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.ExtendedMetaData"));
    stringBuffer.append(TEXT_73);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.ExtendedMetaData"));
    stringBuffer.append(TEXT_74);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_75);
    }
    stringBuffer.append(TEXT_76);
    if (!genModel.useGenerics()) {
    stringBuffer.append(TEXT_77);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EStructuralFeature"));
    stringBuffer.append(TEXT_78);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EStructuralFeature"));
    stringBuffer.append(TEXT_79);
    }
    stringBuffer.append(TEXT_80);
    }
    stringBuffer.append(TEXT_81);
    if (!genModel.isRichClientPlatform() && genModel.getComplianceLevel().getValue() >= GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_82);
    } else {
    stringBuffer.append(TEXT_83);
    }
    stringBuffer.append(TEXT_84);
    if (!genPackage.hasDocumentRoot()) {
    stringBuffer.append(TEXT_85);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_86);
    stringBuffer.append(genPackage.getUncapFactoryName());
    stringBuffer.append(TEXT_87);
    } else {
    stringBuffer.append(TEXT_88);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.ExtendedMetaData"));
    stringBuffer.append(TEXT_89);
    stringBuffer.append(genPackage.getUncapPackageName());
    stringBuffer.append(TEXT_90);
    stringBuffer.append(genPackage.getUncapFactoryName());
    stringBuffer.append(TEXT_91);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.EcoreUtil"));
    stringBuffer.append(TEXT_92);
    }
    stringBuffer.append(TEXT_93);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_94);
    }
    stringBuffer.append(TEXT_95);
    if (genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_96);
    stringBuffer.append(genModel.getImportedName("java.io.File"));
    stringBuffer.append(TEXT_97);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_98);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_99);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_100);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_101);
    stringBuffer.append(genModel.getImportedName("org.eclipse.jface.operation.IRunnableWithProgress"));
    stringBuffer.append(TEXT_102);
    stringBuffer.append(genModel.getImportedName("org.eclipse.jface.operation.IRunnableWithProgress"));
    stringBuffer.append(TEXT_103);
    } else {
    stringBuffer.append(TEXT_104);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_105);
    }
    stringBuffer.append(TEXT_106);
    }
    stringBuffer.append(TEXT_107);
    if (!genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_108);
    }
    stringBuffer.append(TEXT_109);
    if (genPackage.isContentType()) {
    stringBuffer.append(TEXT_110);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_111);
    }
    stringBuffer.append(TEXT_112);
    stringBuffer.append(_MapOfObjectToObject);
    stringBuffer.append(TEXT_113);
    stringBuffer.append(_HashMapOfObjectToObject);
    stringBuffer.append(TEXT_114);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_115);
    if (genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_116);
    stringBuffer.append(genModel.getImportedName(genModel.getQualifiedEditorAdvisorClassName()));
    stringBuffer.append(TEXT_117);
    } else {
    stringBuffer.append(TEXT_118);
    stringBuffer.append(genModel.getImportedName("org.eclipse.ui.IWorkbenchWindow"));
    stringBuffer.append(TEXT_119);
    stringBuffer.append(genModel.getImportedName("org.eclipse.ui.IWorkbenchPage"));
    stringBuffer.append(TEXT_120);
    stringBuffer.append(genModel.getImportedName("org.eclipse.ui.IWorkbenchPart"));
    stringBuffer.append(TEXT_121);
    stringBuffer.append(genModel.getImportedName("org.eclipse.jface.viewers.ISelection"));
    stringBuffer.append(TEXT_122);
    stringBuffer.append(genModel.getImportedName("org.eclipse.jface.viewers.StructuredSelection"));
    stringBuffer.append(TEXT_123);
    if (!genPackage.isContentType()) {
    stringBuffer.append(TEXT_124);
    } else {
    stringBuffer.append(TEXT_125);
    stringBuffer.append(genModel.getImportedName("org.eclipse.core.runtime.Platform"));
    stringBuffer.append(TEXT_126);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_127);
    }
    stringBuffer.append(TEXT_128);
    stringBuffer.append(genModel.getImportedName("org.eclipse.ui.PartInitException"));
    stringBuffer.append(TEXT_129);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_130);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_131);
    }
    stringBuffer.append(TEXT_132);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_133);
    if (!genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_134);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_135);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_136);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_137);
    }
    stringBuffer.append(TEXT_138);
    stringBuffer.append(genModel.getImportedName("org.eclipse.core.runtime.Path"));
    stringBuffer.append(TEXT_139);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_140);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_141);
    }
    stringBuffer.append(TEXT_142);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_143);
    if (genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_144);
    stringBuffer.append(genModel.getImportedName("org.eclipse.swt.widgets.Text"));
    stringBuffer.append(TEXT_145);
    }
    stringBuffer.append(TEXT_146);
    stringBuffer.append(_ListOfString);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_148);
    if (genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_149);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_150);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_151);
    stringBuffer.append(genModel.getImportedName("org.eclipse.swt.widgets.Text"));
    stringBuffer.append(TEXT_152);
    if (!genModel.isRichAjaxPlatform()) {
    stringBuffer.append(TEXT_153);
    stringBuffer.append(genModel.getImportedName("org.eclipse.swt.widgets.Button"));
    stringBuffer.append(TEXT_154);
    stringBuffer.append(genModel.getImportedName("org.eclipse.swt.widgets.Button"));
    stringBuffer.append(TEXT_155);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_156);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_157);
    stringBuffer.append(genModel.getImportedName("org.eclipse.swt.events.SelectionAdapter"));
    stringBuffer.append(TEXT_158);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_159);
    }
    stringBuffer.append(TEXT_160);
    stringBuffer.append(genModel.getImportedName("org.eclipse.swt.events.SelectionEvent"));
    stringBuffer.append(TEXT_161);
    if (!genModel.useGenerics()) {
    stringBuffer.append(TEXT_162);
    }
    stringBuffer.append(genPackage.getImportedEditorClassName());
    stringBuffer.append(TEXT_163);
    stringBuffer.append(genPackage.getImportedEditorClassName());
    stringBuffer.append(TEXT_164);
    stringBuffer.append(genModel.getImportedName(genModel.getQualifiedEditorAdvisorClassName()));
    stringBuffer.append(TEXT_165);
    stringBuffer.append(genModel.getImportedName("org.eclipse.swt.SWT"));
    stringBuffer.append(TEXT_166);
    }
    }
    stringBuffer.append(TEXT_167);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_168);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_169);
    if (genModel.useGenerics()) {
    stringBuffer.append(TEXT_170);
    } else {
    stringBuffer.append(TEXT_171);
    }
    stringBuffer.append(TEXT_172);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_173);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_174);
    if (genModel.useGenerics()) {
    stringBuffer.append(TEXT_175);
    } else {
    stringBuffer.append(TEXT_176);
    }
    stringBuffer.append(TEXT_177);
    if (genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_178);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_179);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_180);
    }
    stringBuffer.append(TEXT_181);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_182);
    }
    stringBuffer.append(TEXT_183);
    if (!genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_184);
    } else {
    stringBuffer.append(TEXT_185);
    }
    stringBuffer.append(TEXT_186);
    if (genModel.useGenerics()) {
    stringBuffer.append(TEXT_187);
    } else {
    stringBuffer.append(TEXT_188);
    }
    stringBuffer.append(TEXT_189);
    if (!genModel.useGenerics()) {
    stringBuffer.append(TEXT_190);
    }
    stringBuffer.append(TEXT_191);
    if (genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_192);
    }
    { String type = genPackage.hasDocumentRoot() ? "feature" : "type"; String prefix = genPackage.hasDocumentRoot() ? "_UI_DocumentRoot_" : "_UI_";
    stringBuffer.append(TEXT_193);
    stringBuffer.append(type);
    stringBuffer.append(TEXT_194);
    stringBuffer.append(type);
    stringBuffer.append(TEXT_195);
    stringBuffer.append(genPackage.getEditPluginClassName());
    stringBuffer.append(TEXT_196);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_197);
    stringBuffer.append(type);
    stringBuffer.append(TEXT_198);
    stringBuffer.append(type);
    stringBuffer.append(TEXT_199);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_200);
    stringBuffer.append(genModel.getImportedName(genModel.getQualifiedEditorPluginClassName()));
    stringBuffer.append(TEXT_201);
    stringBuffer.append(type);
    stringBuffer.append(TEXT_202);
    }
    stringBuffer.append(TEXT_203);
    stringBuffer.append(_CollectionOfString);
    stringBuffer.append(TEXT_204);
    stringBuffer.append(_ArrayListOfString);
    stringBuffer.append(TEXT_205);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_206);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_207);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_208);
    }
    stringBuffer.append(TEXT_209);
    if (!genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_210);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_211);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_212);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_213);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_214);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_215);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_216);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_217);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_218);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_219);
    stringBuffer.append(genPackage.getEditorClassName());
    stringBuffer.append(TEXT_220);
    if (!genModel.useGenerics()) {
    stringBuffer.append(TEXT_221);
    }
    stringBuffer.append(TEXT_222);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_223);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_224);
    stringBuffer.append(genPackage.getEditorClassName());
    stringBuffer.append(TEXT_225);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_226);
    if (!genModel.useGenerics()) {
    stringBuffer.append(TEXT_227);
    }
    stringBuffer.append(TEXT_228);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_229);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_230);
    }
    stringBuffer.append(TEXT_231);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_232);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_233);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_234);
    stringBuffer.append(genPackage.getModelWizardClassName());
    stringBuffer.append(TEXT_235);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_236);
    stringBuffer.append(genPackage.getImportedEditorPluginClassName());
    stringBuffer.append(TEXT_237);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_238);
    if (genModel.isRichClientPlatform()) {
    stringBuffer.append(TEXT_239);
    } else {
    stringBuffer.append(TEXT_240);
    }
    stringBuffer.append(TEXT_241);
    genModel.emitSortedImports();
    stringBuffer.append(TEXT_242);
    return stringBuffer.toString();
  }
}

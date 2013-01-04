package com.mmxlabs.models.migration.script.tests;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.emf.common.util.URI;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class JavascriptTest {
	@Test
	public void simplePackageTest() throws FileNotFoundException, ScriptException {

		final ScriptEngineManager manager = new ScriptEngineManager();
		final ScriptEngine engine = manager.getEngineByName("javascript");

		final MetamodelLoader loader = new MetamodelLoader();

		final URL resource = getClass().getResource("/model-v1.ecore");
		engine.put("pkg", loader.loadEPackage(URI.createURI(resource.toString()), ""));

		final InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/packagename.js"));

		engine.eval(reader);
		final String output = (String) engine.get("output");
		Assert.assertEquals("http://com.mmxlabs.models.migration.tests/model", output);
	}
}

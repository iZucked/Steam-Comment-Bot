package com.mmxlabs.models.lng.transformer.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides a defaulting mechanism for JSON data. 
 * 
 * @author simonmcgregor
 *
 */
public class DefaultingJsonEngine {
	Map<String, DefaultingJsonStructure<?,?>> filesLoaded = new HashMap<>();

	@SuppressWarnings("serial")
	public class JsonRegistryException extends RuntimeException {
		public JsonRegistryException(@NonNull String message) {
			super(message);
		}
	}
	
	/**
	 * Class describing a registry for DefaultingJsonStructure templates.
	 * @author simonmcgregor
	 *
	 */
	private class JsonObjectRegistry {
		Map<String, DefaultingJsonStructure<?,?>> objectMap = new HashMap<>();
		
		public void register(DefaultingJsonStructure<?,?> object, String id) {
			if (objectMap.containsKey(id)) {
				throw new JsonRegistryException(String.format("Attempted to register JSON template with id '%s' that is already in use.", id));
			}
			objectMap.put(id, object);
			
		}
		
		public DefaultingJsonStructure<?,?> findObject(String id) {
			if (objectMap.containsKey(id) == false) {
				throw new JsonRegistryException(String.format("Attempted to find JSON template with id '%s' and none is registered.", id));				
			}
			return objectMap.get(id);
		}		
	}
	
	private final @NonNull JsonObjectRegistry registry = new JsonObjectRegistry();
	private String[] searchPath; 
		
	/**
	 * Helper class representing useful information about the special *-properties in a JSONObject.
	 * 
	 * Can be used to generate a DefaultingJsonStructure of an appropriate sort.
	 * 
	 * @author simonmcgregor
	 *
	 */
	private class SpecialDefaultingParameters {
		public DefaultingJsonStructure<?,?> template = null;
		public String id = null;
		public JSONObject inputJSONObject = null;
		
		public Object jsonContentValue = null;
		
		public String arrayKey = null;
		public List<String> requiredFiles = new LinkedList<>(); 
		
		private boolean representsArray() {
			return jsonContentValue instanceof JSONArray || template instanceof DefaultingJsonArray;
		}
		
		public DefaultingJsonStructure<?, ?> createDefaultingJsonStructure() throws IOException {
			for (String filename: requiredFiles) {
				try {
					loadFile(filename);
				}
				catch (IOException e) {
					throw new IOException("Problem reading file " + filename, e);
				}
			}
			
			DefaultingJsonStructure<?,?> result;
			

			if (representsArray()) {
				result = new DefaultingJsonArray((JSONArray) jsonContentValue, arrayKey, DefaultingJsonEngine.this, (DefaultingJsonArray) template);
			}
			else if (jsonContentValue != null  || template instanceof DefaultingJsonLiteral) {
				result = new DefaultingJsonLiteral(jsonContentValue, DefaultingJsonEngine.this, (DefaultingJsonLiteral) template);
			}
			else {
				result = new DefaultingJsonObject(inputJSONObject, DefaultingJsonEngine.this, (DefaultingJsonObject) template);						
			}
			
			if (id != null) {
				registry.register(result, id);
			}
			
			return result;		
			
		}
	}
	
	
	
	/**
	 * Constructor.
	 * @param searchPaths An array of search paths for the engine to use when locating JSON files by filename.
	 */
	public DefaultingJsonEngine(String ...searchPaths) {
		this.searchPath = searchPaths;		
	}
	

	/**
	 * Sets initial literal values for the defaulting system.  
	 * @param parameters A {@link Map<String, Object>} for literal values to initialise the defaulting engine with. These 
	 * will be available for reuse by objects within the defaulting system.
	 */
	public DefaultingJsonEngine withLiteralValues(Map<String,Object> parameters) {
		if (parameters != null) {
			for (Entry<String, Object> entry: parameters.entrySet()) {
				final DefaultingJsonStructure<?,?> literal;
				final Object value = entry.getValue();
				try {
					if (DefaultingJsonLiteral.canMakeLiteral(value)) {				
						literal = new DefaultingJsonLiteral(entry.getValue(), DefaultingJsonEngine.this, null);
					}
					else {
						JSONObject jsonValue = new JSONObject(value);
						literal = new DefaultingJsonObject(jsonValue, DefaultingJsonEngine.this, null);
					}				
					registry.register(literal, entry.getKey());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return this;		
	}
	
	/**
	 * Returns a {@link DefaultingJsonStructure} object that has been identified in a JSON template
	 * with the id {@code id} using the special {@code"*id"} directive property, e.g.
	 * <pre>
	 * {
	 *    "*id": "id1",
	 *    "foo": "bar"
	 * }
	 * </pre>
	 * @param id
	 * @return
	 */
	public DefaultingJsonStructure<?, ?> findObject(String id) {
		return registry.findObject(id);
	}
	
	/**
	 * Helper functional type. 
	 * @author simonmcgregor
	 *
	 */
	private interface FileFinder {
		String getFileContents(String filename) throws IOException;
	}	
	
	/**
	 * Returns a {@link FileFinder} function that reads resources from the filesystem.
	 * @param path
	 * @return
	 */
	private FileFinder getDirectoryFinder(String path) {
		return (name -> {
			File file = new File(path + File.separator + name);
			if (file.exists() == false) {
				throw new IOException();
			}
			try {
				return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
			} catch (IOException e) {
				throw new IOException();
			}
		});
		
	}

	/**
	 * Returns a {@link FileFinder} function that reads resources using the Java class resource system.
	 * @param path
	 * @return
	 */
	private FileFinder getClassResourceFinder(String path) {
		return (name -> {
			InputStream stream = getClass().getResourceAsStream(path + "/" + name);
			if (stream == null) {
				throw new IOException();
			}
			String text = null;
		    try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name())) {
		        text = scanner.useDelimiter("\\A").next();
		    }
		    return text;			
		});
	}
	
	
	/**
	 * Returns the contents of a resource identified by filename. It is first searched for in the
	 * jar file within the 'default' folder of the current package, and then on the filesystem using
	 * the search path specified in the constructor. 
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	private String fileContents(String filename) throws FileNotFoundException {
		if (searchPath == null) {
			throw new FileNotFoundException(String.format("Cannot find file '%s' with null search path", filename));			
		}		
		
		Stream<FileFinder> dirFinders = Arrays.asList(searchPath).stream().map( path -> getDirectoryFinder(path) );
		List<FileFinder> finders = new LinkedList<FileFinder>(dirFinders.collect(Collectors.toList()));
		finders.add(0, getClassResourceFinder("default"));
				
		for (FileFinder finder: finders) {
			try {
				return finder.getFileContents(filename);
			} catch (IOException e) {
			}				
		}
		
		throw new FileNotFoundException(String.format("Cannot find file '%s' on search path %s", filename, String.join(";", searchPath)));		
		
	}
	

	/**
	 * Reads a JSON file and returns an appropriate DefaultingJsonStructure.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException 
	 */
	public DefaultingJsonStructure<?,?> loadFile(String filename) throws IOException {
		if (filesLoaded.containsKey(filename) == false) {
			String inputJSON = fileContents(filename);
			
			try {
				JSONObject obj = new JSONObject(inputJSON);
				filesLoaded.put(filename, newDefaultingJsonStructure(obj));
			}
			catch (JSONException e) {
				throw new JSONException(String.format("JSON problem processing %s: %s", filename, e.getMessage()));
			}			
		}	
		
		return filesLoaded.get(filename);
	}
	
	/**
	 * Returns a new {@link DefaultingJsonStructure} generated from a particular {@link JSONObject}. Special magic fields
	 * with a '*' prefix are used to interpret the JSONObject in a way that allows selective defaulting.
	 * @param root
	 * @return
	 * @throws IOException 
	 */
	public DefaultingJsonStructure<?,?> newDefaultingJsonStructure(JSONObject root) throws IOException {
		return newDefaultingJsonStructure(root, null);
	}

	/**
	 * Returns a {@link SpecialDefaultingParameters} object representing the relevant parameters needed to 
	 * construct an appropriate {@link DefaultingJsonStructure} object.
	 * 
	 * @param object The {@link JSONObject} in the source JSON that represents the {@link DefaultingJsonStructure} object.
	 * @param template A {@link DefaultingJsonStructure} object that is interpreted as a default template for the object which will be constructed.
	 * @return
	 */
	private SpecialDefaultingParameters getSpecialParameters(JSONObject object, DefaultingJsonStructure<?,?> template) {
		SpecialDefaultingParameters result = new SpecialDefaultingParameters();
		
		result.inputJSONObject  = object;
		
		if (object.has(DefaultingJsonStructure.TEMPLATE_TARGET)) {
			Object target = object.get(DefaultingJsonStructure.TEMPLATE_TARGET);
			if (target == JSONObject.NULL) {
				result.template = null;
			}
			else {
				result.template = (DefaultingJsonStructure<?,?>) registry.findObject((String) target);
			}
		}
		else {
			result.template = template;
		}
		
		if (object.has(DefaultingJsonStructure.ID_DEFINITION)) {
			result.id = object.getString(DefaultingJsonStructure.ID_DEFINITION);
		}
		
		if (object.has(DefaultingJsonStructure.CONTENT_VALUE)) {
			result.jsonContentValue = object.get(DefaultingJsonStructure.CONTENT_VALUE);			
		}
		
		if (object.has(DefaultingJsonStructure.ARRAY_KEY)) {
			result.arrayKey = object.getString(DefaultingJsonStructure.ARRAY_KEY);
		}
		else if (result.template instanceof DefaultingJsonArray) {
			result.arrayKey = ((DefaultingJsonArray) result.template).getArrayKey();	
		}
		
		if (object.has(DefaultingJsonStructure.REQUIRED_FILES)) {
			JSONArray filenames = object.getJSONArray(DefaultingJsonStructure.REQUIRED_FILES);
			for (int i = 0; i < filenames.length(); i++) {
				result.requiredFiles.add(filenames.getString(i));
			}
		}
		
		return result;
	}
	
	
	/**
	 * Returns a new {@link DefaultingJsonStructure} of the appropriate sort based on JSON from the source data.
	 * @param root The {@link JSONObject} in the source JSON that represents the {@link DefaultingJsonStructure} object.
	 * @param template A {@link DefaultingJsonStructure} object that is interpreted as a default template for the object which will be constructed.
	 * @return
	 * @throws IOException 
	 */
	public DefaultingJsonStructure<?,?> newDefaultingJsonStructure(JSONObject root, DefaultingJsonStructure<?,?> template) throws IOException {
		SpecialDefaultingParameters params = getSpecialParameters(root, template);
		
		return params.createDefaultingJsonStructure();		
	}
	
	/**
	 * Returns a new {@link DefaultingJsonArray} object based on JSON from the source data.
	 * @param array The {@link JSONArray} in the source JSON that represents the {@link DefaultingJsonArray} object.
	 * @param template A {@link DefaultingJsonArray} object that is interpreted as a default template for the object which will be constructed.
	 * @return
	 * @throws IOException 
	 */
	public DefaultingJsonStructure<?,?> newDefaultingJsonStructure(JSONArray array, DefaultingJsonArray template) throws IOException {
		String arrayKey = (template == null) ? null : template.getArrayKey();

		SpecialDefaultingParameters params = new SpecialDefaultingParameters();
		params.arrayKey = arrayKey;
		params.template = template;
		params.jsonContentValue = array;
		
		return params.createDefaultingJsonStructure();		
	}
	
}

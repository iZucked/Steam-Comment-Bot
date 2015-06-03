package com.mmxlabs.lingo.app.headless.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mmxlabs.common.Pair;

public class HeadlessJSONParser {
	SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");

	public Map<String, Pair<Object, Class<?>>> openFile(String filePath) {
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader(filePath));

			JSONObject jsonObject = (JSONObject) obj;
			Map<String, Pair<Object, Class<?>>> params = new HashMap<>();
			for (Object keyObject : jsonObject.keySet()) {
				if (keyObject instanceof String) {
					String key = (String) keyObject;
					String value = (String) jsonObject.get(key);
					if (matchDouble(value)) {
						params.put(key, new Pair<Object, Class<?>>(Double.valueOf(value), Double.class));
					} else if (matchInt(value)) {
						params.put(key, new Pair<Object, Class<?>>(Integer.valueOf(value), Integer.class));
					} else {
						params.put(key, new Pair<Object, Class<?>>(Integer.valueOf(value), Integer.class));
					}
				}
			}
			return params;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Pair<Object, Class<?>>> parseJSON(String filePath) {
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader(filePath));

			JSONObject jsonObject = (JSONObject) obj;
			Map<String, Pair<Object, Class<?>>> params = new HashMap<>();
			for (Object keyObject : jsonObject.keySet()) {
				if (keyObject instanceof String) {
					String key = (String) keyObject;
					Object value = jsonObject.get(key);
					if (value instanceof Integer) {
						params.put(key, new Pair<Object, Class<?>>((Integer) value, Integer.class));
					} else if (value instanceof Long) {
						params.put(key, new Pair<Object, Class<?>>(((Long) value).intValue(), Integer.class));
					} else if (value instanceof Float || value instanceof Double) {
						params.put(key, new Pair<Object, Class<?>>((Double) value, Double.class));
					} else if (value instanceof Boolean) {
						params.put(key, new Pair<Object, Class<?>>(value, Boolean.class));
					} else if (value instanceof JSONArray) {
						List<String> listString = new LinkedList<String>();
						if (((JSONArray) value).size() > 0) {
							for (Object o : ((JSONArray) value)) {
								if (o instanceof String) {
									listString.add((String) o);
								}
							}
							if (listString.size() == ((JSONArray) value).size()) {
								params.put(key, new Pair<Object, Class<?>>(new StringList(listString), StringList.class));
							}
						}
					} else if (value instanceof JSONObject) {
						JSONObject innerObject = (JSONObject) value;
						boolean valid = true;
						Map<String, Double> doubleMap = new HashMap<String, Double>();
						for (Object keyInnerObject : innerObject.keySet()) {
							if (keyInnerObject instanceof String) {
								String innerKey = (String) keyInnerObject;
								Object innerValue = innerObject.get(innerKey);
								if (innerValue instanceof Integer) {
									doubleMap.put(innerKey, ((Integer) innerValue).doubleValue());
								} else if (innerValue instanceof Double) {
									doubleMap.put(innerKey, (Double) innerValue);
								} else if (innerValue instanceof Long) {
									doubleMap.put(innerKey, ((Long) innerValue).doubleValue());
								} else {
									valid = false;
									break;
								}
							} else {
								valid = false;
								break;
							}
						}
						if (valid) {
							params.put(key, new Pair<Object, Class<?>>(new DoubleMap(doubleMap), DoubleMap.class));
						}
					} else if ((value instanceof String) && matchDate((String) value)) {
						Date parsedDate = parseDate((String) value);
						params.put(key, new Pair<Object, Class<?>>(parsedDate, Date.class));
					} else if (value instanceof String) {
						params.put(key, new Pair<Object, Class<?>>(value, String.class));
					}
				}
			}
			return params;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean matchDouble(String text) {
		return Pattern.matches("^[\\+\\-]{0,1}[0-9]+[\\.\\,]{1}[0-9]+$", text);
	}

	private boolean matchInt(String text) {
		return Pattern.matches("^[\\+\\-]{0,1}[0-9]+$", text);
	}

	public static void copyJSONFile(String sourceFilePath, String destFilePath) {
		try {
			copyFile(sourceFilePath, destFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void copyFile(String sourceFilePath, String destFilePath) throws IOException {
		File sourceFile = Paths.get(sourceFilePath).toFile();
		File destFile = Paths.get(destFilePath).toFile();
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
	
	boolean matchDate(String input) {
	     try {
	          dateFormat.parse(input);
	          return true;
	     }
	     catch(ParseException e){
	          return false;
	     }
	}

	Date parseDate(String dateString) {
		DateTimeZone timeZone = DateTimeZone.forID( "UTC" );
		DateTimeFormatter formatter = DateTimeFormat.forPattern( "YYYY/MM/dd" );
		DateTime dateTime = formatter.withZone( timeZone ).parseDateTime( dateString );
		return dateTime.toDate();

	}

}

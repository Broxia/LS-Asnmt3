package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;

public class CountryInfo extends ServerResource {
	private static String URL;
	private static String lastInput;
	@Get
	public Representation represent() {
		BufferedReader reader = null;
		JSONParser parser = new JSONParser();
		
		List<Object> info = new ArrayList<Object>();
		String userInput = getRequest().getAttributes().get("countryName").toString().toLowerCase();
//		if(!userInput.equals(lastInput)){
			URL = "http://restcountries.eu/rest/v1/name/" + userInput;
//			lastInput = userInput;
//		}
		
		try (InputStream input = new URL(URL).openStream()){
			reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
			JSONArray a = (JSONArray) parser.parse(reader);
			
			JSONObject country = (JSONObject) a.get(0);
			info.add(country.get("name"));
			info.add(country.get("capital"));
			info.add(country.get("population"));
			info.add(country.get("demonym"));
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		Map<String, Object> dataModel = new HashMap<>(1);
		dataModel.put("name", info.get(0));
		dataModel.put("capital", info.get(1));
		dataModel.put("population", info.get(2));
		dataModel.put("language", info.get(3));
		
		Configuration configuration = new Configuration();
		try {
			configuration.setDirectoryForTemplateLoading(new File("Server/templates/"));
            configuration.setObjectWrapper(new BeansWrapper());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		TemplateRepresentation template = new TemplateRepresentation("CountryTemplate.html", configuration, dataModel, MediaType.TEXT_HTML);
		template.setCharacterSet(CharacterSet.UTF_8);
		return template;
	}
}

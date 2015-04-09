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
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;

public class Countries extends ServerResource{
	private static final String URL = "http://restcountries.eu/rest/v1/all";
	
	
	@Get
	public Representation represent() {
		BufferedReader reader = null;
		JSONParser parser = new JSONParser();
		
		List<String> countries = new ArrayList<String>();
		
		try (InputStream input = new URL(URL).openStream()){
			reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
			JSONArray a = (JSONArray) parser.parse(reader);
			
			for (Object o : a){
				JSONObject country = (JSONObject) o;
				
				String name = (String) country.get("name");
				countries.add(name);
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		Map<String, Object> dataModel = new HashMap<>(1);
		dataModel.put("countries", countries);
		
		Configuration configuration = new Configuration();
		try {
			configuration.setDirectoryForTemplateLoading(new File("Server/templates/"));
            configuration.setObjectWrapper(new BeansWrapper());
		} catch (IOException e) {
			e.printStackTrace();
		}
		TemplateRepresentation template = new TemplateRepresentation("CountriesTemplate.html", configuration, dataModel, MediaType.TEXT_HTML);
		return template;
	}
}

package wasdev.sample.servlet;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.ibm.watson.developer_cloud.language_translation.v2.LanguageTranslation;
import com.ibm.watson.developer_cloud.language_translation.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private String TRANSLATION_SERVICE_PASSWORD = "";
    private String TRANSLATION_SERVICE_USERNAME = "";
    private String NLC_SERVICE_PASSWORD = "";
    private String NLC_SERVICE_USERNAME = "";
    private String NLC_SERVICE_CLASSIFIER_ID = "3a84dfx64-nlc-9397";
    
    static final private String VCAP_SERVICES_CREDENTIALS = "credentials";
    static final private String VCAP_SERVICES_PASSWORD = "password";
    static final private String VCAP_SERVICES_USERNAME = "username";
    static final private String VCAP_SERVICES_NLC = "natural_language_classifier";
    static final private String VCAP_SERVICES_LANG_TRANSLATION = "language_translation";
    

    @Override
	public void init() throws ServletException {
		super.init();
		
		//Get the credentials for the Watson services from the environment.
		final String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		
		if(VCAP_SERVICES != null) {
			final JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES);
			
			final Set<Entry<String, JsonElement>> entries = obj.entrySet();
			
			for ( Entry<String, JsonElement> entry : entries) {
				if(entry.getKey().equals(VCAP_SERVICES_LANG_TRANSLATION)) {
					final JsonObject entryObj = (JsonObject) ((JsonArray) entry.getValue()).get(0);
					final JsonObject credObj = (JsonObject) entryObj.get(VCAP_SERVICES_CREDENTIALS);
					TRANSLATION_SERVICE_USERNAME = credObj.get(VCAP_SERVICES_USERNAME).getAsString();
					TRANSLATION_SERVICE_PASSWORD =  credObj.get(VCAP_SERVICES_PASSWORD).getAsString();
				}
				else if(entry.getKey().equals(VCAP_SERVICES_NLC)) {
					final JsonObject entryObj = (JsonObject) ((JsonArray) entry.getValue()).get(0);
					final JsonObject credObj = (JsonObject) entryObj.get(VCAP_SERVICES_CREDENTIALS);
					NLC_SERVICE_USERNAME = credObj.get(VCAP_SERVICES_USERNAME).getAsString();
					NLC_SERVICE_PASSWORD =  credObj.get(VCAP_SERVICES_PASSWORD).getAsString();
				}
			}
 		}
		
	}




	/**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	final String txtToTranslate = request.getParameter("txtToTranslate");
    	System.out.println("Text to translate={" + txtToTranslate + "}");
    	
    	//Translate the text from Spanish to English
    	final LanguageTranslation service = new LanguageTranslation();
    	service.setUsernameAndPassword(TRANSLATION_SERVICE_USERNAME, TRANSLATION_SERVICE_PASSWORD);

    	final TranslationResult translationResult = service.translate(
    	  txtToTranslate, Language.SPANISH, Language.ENGLISH);
    	
    	final String translated = translationResult.getFirstTranslation();

    	System.out.println(translationResult);
    	
    	//get the Natural Language Classifiers for the English Text
    	final NaturalLanguageClassifier nlcService = new NaturalLanguageClassifier();
    	nlcService.setUsernameAndPassword(NLC_SERVICE_USERNAME, NLC_SERVICE_PASSWORD);

    	final Classification classification = nlcService.classify(NLC_SERVICE_CLASSIFIER_ID, translated);
    	final String topClassification = classification.getTopClass();
    	System.out.println(classification);
    	
    	final JSONObject resultsJSONObj = new JSONObject();    	
    	resultsJSONObj.put("translated", translated);
    	resultsJSONObj.put("topClassification", topClassification);
    	resultsJSONObj.put("original", txtToTranslate);    	
    	
    	final JSONObject retJSONObj = new JSONObject();
    	
    	retJSONObj.put("results", resultsJSONObj);
 
    	retJSONObj.writeJSONString(response.getWriter());
    }

}

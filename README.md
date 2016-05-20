# Spanish English Translator
A simple Java web page to translate Spanish text to English. Once translated, the text is sent to a Watson Natural Language Classifiers. This project can easily be setup in Bluemix following the instructions below. The instructions use basic weather classifier with only two classes: temperature and conditions. More information on classifiers can be found [here](https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/nl-classifier/).

# Bluemix Setup
Here are the steps to get this project working in Bluemix.

1. Clone this project to your local machine.
2. Log into Bluemix.net. If you don't have an account, create one.
3. Create a Language Translation service instance in Bluemix.
  1. From the Bluemix Catalog, create a Language Translation service. 
    * This project uses default translations, so no adjustments to translation are needed.
    * Once your service is created, default credentials should be created.
4.  Create a Natural Language Classifier service instance in Bluemix.
  1. From the Bluemix Catalog, create a Natural Language Classifier service.
  2. Once the credentials are created, make note of the username and password, as you will need them to train Watson.
  3. In a browser, download a copy of the weather_data_train.csv file located [here](https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/nl-classifier/get_start.shtml#create).
  4. From the Manage screen of your new Natural Language Classifier service in Bluemix, click the *Access the beta tookit* button.
  5. Add the weather_data_train.csv file and start the training. 
    * Note: Once training is complete, a classifier_id will be provided. Make note of this classifier_id.
5. Modify the manifest.yml file
  * Open the manifest.yml file located in the root directory of this project.
  * Update the Language Translation service and Natural Language Classifier service names to match the names of the services in your Bluemix.
  * Change the name and host to be unique names for your service.
6. Open the SimpleServlet.java file and update the NLC_SERVICE_CLASSIFIER_ID variable to reflect your classifier_id from the previous step.
  * src/main/java/wasdev/sample/servlet/SimpleServlect.java.
7. From a command line, run ```mvn install``` to install the dependencies and create the war file.
8. Deploy using CF CLI. Login and deploy instructions can be found [here](https://console.ng.bluemix.net/docs/starters/install_cli.html).

Once your web page is deployed, test it out.

# To Know About This Project

## Weather Related Classifications

Because the instructions for this project uses the basic weather_data_train.csv data, the returned classifications are based on weather related questions. So, if you ask weather related questions in Spanish, the classifications will be more accurate than if you are trying to translate sports related sentences.

## Error Handling

I have not implemented error handling in the AJAX calls that load the translated text and classifications. The [watson-developer-cloud/java-sdk](https://github.com/watson-developer-cloud/java-sdk) project has good error handling. Capturing specific errors to communicate to the end user is easy. I hope to add this when I get some time.

## Other Languages

This project could be easily update to support other languages. The Language Translation service has a set of default languages. Using the java-sdk, the language to translate to is a parameter in the call to translate.

# Learn More

Here are some related web pages:

1. [Watson Developer Cloud/java-sdk](https://github.com/watson-developer-cloud/java-sdk)
2. [Watson Language Translation](https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/language-translation.html)
3. [Natural Language Classifier service](https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/nl-classifier/index.shtml) 

  



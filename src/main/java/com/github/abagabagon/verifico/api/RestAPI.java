package com.github.abagabagon.verifico.api;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.abagabagon.verifico.enums.HTTPMethod;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAPI {
	
	private Logger log;
	
	public RestAPI() {
		this.log = LogManager.getLogger(RestAPI.class);
	}
	
	public final void setBaseURI(String baseURI) {
		if (baseURI == null) {
			log.fatal("Base URI is NULL.");
		} else {
			RestAssured.baseURI = baseURI;
		}
	}
	
	public final Response request(HTTPMethod method, String endPoint, RequestSpecification requestSpecification) {
		Response response = null;
		JsonPath responseJson = null;
		boolean status = false;
		
		for(int i = 1; i < 4; i++) {
			try {
				URL url = new URL(RestAssured.baseURI + endPoint);
				
				switch(method) {
				case GET:
					response = requestSpecification.get(url);
					break;
				case POST:
					response = requestSpecification.post(url);
					break;
				case PUT:
					response = requestSpecification.put(url);
					break;
				case DELETE:
					response = requestSpecification.delete(url);
					break;
				default:
					this.log.error("Unsupported HTTP Method.");
				}
				
				if (response.getStatusCode() == 500) {
					log.error("Encountered Internal Server Error (500) while processing " + method + " API Request. Retrying API Request (" + i + "/3).");
					log.error(response.asString());
					this.wait(1000);
				} else {
					status = true;
					break;
				}
			} catch(NullPointerException e) {
				log.error("Encountered NullPointerException while processing " + method + " API Request. One or more of the parameters provided is NULL.");
				log.debug(ExceptionUtils.getStackTrace(e));
				this.wait(1000);
			} catch (MalformedURLException e) {
				log.error("Encountered MalformedURLException while processing " + method + " API Request. Check Base URI and Path.");
				log.debug(ExceptionUtils.getStackTrace(e));
			} catch(Exception e) {
				log.error("Encountered Exception while processing " + method + " API Request. Retrying API Request (" + i + "/3).");
				log.debug(ExceptionUtils.getStackTrace(e));
				this.wait(1000);
			}
		}
		
		if(status) {
			responseJson = new JsonPath(response.asString());
			log.debug("STATUS: " + response.getStatusLine() + "(" + response.getStatusCode() + ")");
			log.debug("MESSAGE: " + responseJson.get("messages[0]"));
			log.debug("------------------------------------------------------------------------");
		} else {
			log.error("Failed to process " + method + " API Request.");
			log.debug("------------------------------------------------------------------------");
		}
		
		return response;
	}
	
	public final Response get(String endPoint, RequestSpecification requestSpecification) {
		log.debug("I process GET API Request.");
		Response response = this.request(HTTPMethod.GET, endPoint, requestSpecification);		
		return response;
	}
	
	public final Response post(String endPoint, RequestSpecification requestSpecification) {
		log.debug("I process GET API Request.");
		Response response = this.request(HTTPMethod.POST, endPoint, requestSpecification);		
		return response;
	}
	
	public final Response put(String endPoint, RequestSpecification requestSpecification) {
		log.debug("I process GET API Request.");
		Response response = this.request(HTTPMethod.PUT, endPoint, requestSpecification);		
		return response;
	}
	
	public final Response delete(String endPoint, RequestSpecification requestSpecification) {
		log.debug("I process GET API Request.");
		Response response = this.request(HTTPMethod.DELETE, endPoint, requestSpecification);		
		return response;
	}

	private final void wait(int duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch(Exception e) {
			this.log.error("Encountered Exception while waiting.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

}

package com.github.abagabagon.verifico.api;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAPI {
	
	private Logger log;
	
	public RestAPI() {
		this.log = LogManager.getLogger(RestAPI.class);
	}
	
	public final Response sendRequest(Method method, String endPoint, RequestSpecification requestSpecification) {
		Response response = null;
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
			case OPTIONS:
				response = requestSpecification.options(url);
				break;
			case PATCH:
				response = requestSpecification.patch(url);
				break;
			default:
				this.log.error("Unsupported HTTP Method.");
			}
		} catch(NullPointerException e) {
			log.error("Encountered NullPointerException while processing " + method + " API Request. One or more of the parameters provided is NULL.");
			log.debug(ExceptionUtils.getStackTrace(e));
		} catch (MalformedURLException e) {
			log.error("Encountered MalformedURLException while processing " + method + " API Request. Check Base URI and Path.");
			log.debug(ExceptionUtils.getStackTrace(e));
		} catch(Exception e) {
			log.error("Encountered Exception while processing " + method + " API Request.");
			log.debug(ExceptionUtils.getStackTrace(e));
		}
		
		return response;
	}

}

/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package net.jmpd.service.util;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Data generator for annotation sample service.
 */
public class SampleDataGenerator {
  public static final String HTTP_METHOD_PUT = "PUT";
  public static final String HTTP_METHOD_POST = "POST";

  public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HTTP_HEADER_ACCEPT = "Accept";
  
  public static final String APPLICATION_JSON = "application/json";

  private static final Logger LOG = LoggerFactory.getLogger(SampleDataGenerator.class);
  private static final boolean PRINT_RAW_CONTENT = true;

  public static void main(String[] args) {
    String serviceUrl = "http://localhost:8080/ServiceRegistry.svc";
    if(args.length == 1) {
      serviceUrl = args[0];
    }
    generateData(serviceUrl);
  }

  public static void generateData(String serviceUrl) {
    SampleDataGenerator app = new SampleDataGenerator();
    app.generateSampleData(serviceUrl);
  }

  public void generateSampleData(String serviceUrl) {
    print("Generate sample data for service on url: " + serviceUrl);
    String usedFormat = APPLICATION_JSON;

    String serviceBla = "{\"Id\":\"1\",\"Name\":\"bla\",\"Description\":\"a bla service\",\"ServiceUrl\":\"http://bla.com/kecks\",\"ResourceUrl\":\"http://bla/js\"}";
    String serviceBlubb = "{\"Id\":\"2\",\"Name\":\"blubb\",\"Description\":\"a blubb service\",\"ServiceUrl\":\"http://blubb.net/svc\",\"ResourceUrl\":\"http://blubb.net/js\"}";
        
    
    String serviceUri = serviceUrl + "/Services";
    createEntity(serviceUri, serviceBla, usedFormat);
    createEntity(serviceUri, serviceBlubb, usedFormat);
  }

  private void createEntity(String absoluteUri, String content, String contentType) {
    try {
      writeEntity(absoluteUri, content, contentType, HTTP_METHOD_POST);
    } catch (IOException e) {
      throw new RuntimeException("Exception during data source initialization generation.", e);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Exception during data source initialization generation.", e);
    }
  }

  private void writeEntity(String absoluteUri, String content, String contentType, String httpMethod)
      throws IOException, URISyntaxException {

    print(httpMethod + " request on uri: " + absoluteUri + ":\n  " + content + "\n");
    //
    HttpURLConnection connection = initializeConnection(absoluteUri, contentType, httpMethod);
    byte[] buffer = content.getBytes("UTF-8");
    connection.getOutputStream().write(buffer);

    // if a entity is created (via POST request) the response body contains the new created entity
    HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
    if(statusCode == HttpStatusCodes.CREATED) {
      // get the content as InputStream and de-serialize it into an ODataEntry object
      InputStream responseContent = connection.getInputStream();
      logRawContent(httpMethod + " response:\n  ", responseContent, "\n");
    } else if(statusCode == HttpStatusCodes.NO_CONTENT) {
      print("No content.");
    } else {
      checkStatus(connection);
    }

    //
    connection.disconnect();
  }

  private void print(String content) {
    LOG.info(content);
  }


  private HttpStatusCodes checkStatus(HttpURLConnection connection) throws IOException {
    HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
    if (400 <= httpStatusCode.getStatusCode() && httpStatusCode.getStatusCode() <= 599) {
      connection.disconnect();
      throw new RuntimeException("Http Connection failed with status " + httpStatusCode.getStatusCode() + " " + httpStatusCode.toString());
    }
    return httpStatusCode;
  }

  private InputStream logRawContent(String prefix, InputStream content, String postfix) throws IOException {
    if(PRINT_RAW_CONTENT) {
      byte[] buffer = streamToArray(content);
      print(prefix + new String(buffer, "UTF-8") + postfix);
      return new ByteArrayInputStream(buffer);
    }
    return content;
  }


  private HttpURLConnection initializeConnection(String absoluteUri, String contentType, String httpMethod)
          throws IOException {
    URL url = new URL(absoluteUri);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod(httpMethod);
    connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
    if(HTTP_METHOD_POST.equals(httpMethod) || HTTP_METHOD_PUT.equals(httpMethod)) {
      connection.setDoOutput(true);
      connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
    }

    return connection;
  }


  private byte[] streamToArray(InputStream stream) throws IOException {
    byte[] result = new byte[0];
    byte[] tmp = new byte[8192];
    int readCount = stream.read(tmp);
    while(readCount >= 0) {
      byte[] innerTmp = new byte[result.length + readCount];
      System.arraycopy(result, 0, innerTmp, 0, result.length);
      System.arraycopy(tmp, 0, innerTmp, result.length, readCount);
      result = innerTmp;
      readCount = stream.read(tmp);
    }
    stream.close();
    return result;
  }
}
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
package net.jmpd.service.model;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;

/**
 *
 */
@EdmEntityType(namespace = "ServiceRegistry")
@EdmEntitySet(name = "Services")
public class Service {
  @EdmKey
  @EdmProperty
  private String Id;	  
  @EdmProperty
  private String Name;
  @EdmProperty
  private String Description;
  @EdmProperty
  private String ServiceUrl;
  @EdmProperty
  private String ResourceUrl;

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
	Name = name;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription(String description) {
    Description = description;
  }

  public String getServiceUrl() {
    return ServiceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    ServiceUrl = serviceUrl;
  }

  public String getResourceUrl() {
    return ResourceUrl;
  }

  public void setResources(String resourceUrl) {
    ResourceUrl = resourceUrl;
  }

  @Override
  public String toString() {
    return "Service{" + "Id=" + Id + ", Name=" + Name + ", Description=" + Description + ", ServiceUrl=" + ServiceUrl +", ResourceUrl=" + ResourceUrl + '}';
  }

}

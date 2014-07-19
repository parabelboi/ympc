<!--

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.

-->
<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>jmpd generic service gui</title>
<style type="text/css">
body { font-family: Arial, sans-serif; font-size: 13px; line-height: 18px;
       color: blue; background-color: #ffffff; }
a { color: blue; text-decoration: none; }
a:focus { outline: thin dotted #4076cb; outline-offset: -1px; }
a:hover, a:active { outline: 0; }
a:hover { color: #404a7e; text-decoration: underline; }
h1, h2, h3, h4, h5, h6 { margin: 9px 0; font-family: inherit; font-weight: bold;
                         line-height: 1; color: blue; }
h1 { font-size: 36px; line-height: 40px; }
h2 { font-size: 30px; line-height: 40px; }
h3 { font-size: 24px; line-height: 40px; }
h4 { font-size: 18px; line-height: 20px; }
h5 { font-size: 14px; line-height: 20px; }
h6 { font-size: 12px; line-height: 20px; }
.logo { float: right; }
ul { padding: 0; margin: 0 0 9px 25px; }
ul ul { margin-bottom: 0; }
li { line-height: 18px; }
hr { margin: 18px 0;
     border: 0; border-top: 1px solid #cccccc; border-bottom: 1px solid #ffffff; }
table { border-collapse: collapse; border-spacing: 10px; }
th, td { border: 1px solid; padding: 20px; }
.code { font-family: "Courier New", monospace; font-size: 13px; line-height: 18px; }
</style>
</head>
<body>
    <h1>Debug Page for</h1>
    <hr />
    <h2>jmpd Service</h2>
    <table>
        <tr>
            <td valign="top">
                <h3>Service Document and Metadata</h3>
                <ul>
                    <li><a href="registry?_wadl" target="_blank">wadl</a></li>
                    <li><a href="registry/" target="_blank">service document</a></li>
                    <li><a href="registry/$metadata" target="_blank">metadata</a></li>
                </ul>
                <h3>EntitySets</h3>
                <ul>
                    <li><a href="registry/Services" target="_blank">Services</a></li>
                    <li><a href="registry/Services/?$orderby=Description" target="_blank">Services/?$orderby=Description</a></li>
                </ul>
                <h3>Entities</h3>
                <ul>
                    <li><a href="registry/Services('bla')" target="_blank">Services('bla')</a></li>
                    <li><a href="registry/Services('bla')/Description" target="_blank">Services('bla')/Description</a></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="bottom">
                <div class="code">
                    <%
                        if (request.getParameter("genSampleData") != null) { //genSampleData is the name of your button, not id of that button.
                            String requestUrl = request.getRequestURL().toString();
                            final String old_title="generate_services.jsp";
                            if(requestUrl.endsWith(old_title)) {
                                requestUrl = requestUrl.substring(0, requestUrl.length()-old_title.length());
                            }
                            eu.jmpd.service.util.SampleDataGenerator.generateData(requestUrl + "registry");
                            response.sendRedirect(requestUrl);
                        }
                    %>
                    <form method="POST">
                        <div>
                            For generation of sample data this button can be used.
                            <br/>
                            But be aware that multiple clicking results in multiple data generation.
                        </div>
                        <input type="submit" id="genSampleData" name="genSampleData" value="Generate sample Data"/>
                    </form>
                </div>
            </td>
        </tr>
    </table>
</body>
</html>

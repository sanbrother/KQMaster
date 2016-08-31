/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.neosoft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class LoginHelper {
    // private final static String LOGIN_URL = BASE_URL + "/" + "login_wkq1103_3023.jsp";
    private static String loginURL = "";
    
    /**
     * @brief Sleep for sometime randomly.
     * @param min Min time (Seconds)
     * @param max Max time (Seconds)
     */
    private final static void randomSleep(int min, int max) {
        Random rdm = new Random(System.currentTimeMillis());
        
        int rdmValue = min * 1000 + Math.abs(rdm.nextInt() % ((max - min) * 1000));
        System.out.println("randomSleep : " + rdmValue + " MS");
        
        try {
            Thread.sleep(rdmValue);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static HttpPost newPost(String url) {
        HttpPost httpost = new HttpPost(url);
        
        httpost.setHeader(
                "Accept",
                "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        httpost.setHeader("Accept-Language", "zh-CN");
        httpost.setHeader(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; CMDTDFJS)");
        httpost.setHeader("Accept-Encoding", "gzip, deflate");
        // httpget.setHeader("Host", BASE_URL); // ? Apache can't recognize this ?
        httpost.setHeader("Connection", "Keep-Alive");
        
        return httpost;
    }
    
    private static HttpGet newGet(String url) {
        HttpGet httpget = new HttpGet(url);

        httpget.setHeader(
                "Accept",
                "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        httpget.setHeader("Accept-Language", "zh-CN");
        httpget.setHeader(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; CMDTDFJS)");
        httpget.setHeader("Accept-Encoding", "gzip, deflate");
        // httpget.setHeader("Host", BASE_URL); // ? Apache can't recognize this ?
        httpget.setHeader("Connection", "Keep-Alive");
        
        return httpget;
    }

    public static void Login(String username, String password) throws ClientProtocolException, IOException  {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        	
        	
            HttpGet httpget = newGet(IConfig.BASE_URL);

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            System.out.println("Login form get 1: " + response.getStatusLine());

            // System.out.println(EntityUtils.toString(entity));
            
            String htmlContent = EntityUtils.toString(entity);
            Document doc = Jsoup.parse(htmlContent);
            Element loginForm = doc.getElementsByTag("form").first();
            
            for (Element e : loginForm.select("input")) {
                // System.out.println(e.attributes());
                if (e.attributes().hasKey("id") && e.attr("id").equals("loginButton")) {
                    continue;
                } else if (e.attributes().hasKey("name") && e.attributes().hasKey("value")) {
                    System.out.println(e.attr("name") + " --- " + e.attr("value"));
                    nvps.add(new BasicNameValuePair(e.attr("name"), e.attr("value")));
                } else if (e.attributes().hasKey("name")) {
                    System.out.println(e.attr("name"));
                    
                    if (e.attr("name").startsWith("ID"))
                    {
                    	nvps.add(new BasicNameValuePair(e.attr("name"), username));
                    } else if (e.attr("name").startsWith("KEY") && e.attr("name").length() > 20)
                    {
                    	nvps.add(new BasicNameValuePair(e.attr("name"), password));
                    } else {
                    	nvps.add(new BasicNameValuePair(e.attr("name"), ""));
                    }
                }
            }
            
            nvps.add(new BasicNameValuePair("login", "true"));
            nvps.add(new BasicNameValuePair("attend", ""));
            
            loginURL = IConfig.BASE_URL + loginForm.attr("action");
            
            EntityUtils.consume(entity);
            randomSleep(2, 4);
            
            
            
            // Log in
            HttpPost httpost = newPost(loginURL);
            httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            
            response = httpclient.execute(httpost);
            entity = response.getEntity();
            
            System.out.println("Login form get 2: " + response.getStatusLine());
            
            if (response.getStatusLine().getStatusCode() == 302) {
            	System.out.println("Location = " + response.getLastHeader("Location"));
            }
            
            EntityUtils.consume(entity);
            randomSleep(2, 5);

            
            httpget = newGet(IConfig.BASE_URL + "/attendance.jsp");

            response = httpclient.execute(httpget);
            entity = response.getEntity();
            
            System.out.println("Login form get 3: " + response.getStatusLine());
            
            
            htmlContent = EntityUtils.toString(entity);
            
            // System.out.println(htmlContent);
            
            doc = Jsoup.parse(htmlContent);
            loginForm = doc.getElementsByTag("form").first();

            // record
            httpost = newPost(IConfig.BASE_URL + "/record.jsp");
            nvps = new ArrayList<NameValuePair>();
            
            for (Element e : loginForm.select("input")) {
                if (e.attributes().hasKey("name") && e.attributes().hasKey("value")) {
                    System.out.println(e.attr("name") + " --- " + e.attr("value"));
                    nvps.add(new BasicNameValuePair(e.attr("name"), e.attr("value")));
                } else if (e.attributes().hasKey("name")) {
                    System.out.println(e.attr("name"));
                    {
                    	nvps.add(new BasicNameValuePair(e.attr("name"), ""));
                    }
                }
            }
            
            
            
            
            // System.out.println("Login form get 2: " + response.getStatusLine() + " Location = " + response.getLastHeader("Location"));
            EntityUtils.consume(entity);
            randomSleep(2, 5);
            
            httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            
            response = httpclient.execute(httpost);
            entity = response.getEntity();
            
            System.out.println("Login form get 4: " + response.getStatusLine() + " Location = " + response.getLastHeader("Location"));
            
            if (response.getStatusLine().getStatusCode() == 302) {
            	System.out.println("Location = " + response.getLastHeader("Location"));
            }
            
            EntityUtils.consume(entity);
            randomSleep(2, 5);
            
            //             
            httpget = newGet(IConfig.BASE_URL + "/attendance.jsp");

            response = httpclient.execute(httpget);
            entity = response.getEntity();
            
            System.out.println("Login form get 5: " + response.getStatusLine());
            
            
            htmlContent = EntityUtils.toString(entity);
            
            System.out.println(htmlContent);
            
            EntityUtils.consume(entity);
            randomSleep(2, 5);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }
}

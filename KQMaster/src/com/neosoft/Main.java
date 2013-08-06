package com.neosoft;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://localhost/");

        try {
            HttpResponse response = httpclient.execute(httpget);
            
            // Not OK
            if (200 != response.getStatusLine().getStatusCode()) {
                return;
            }
            
            HttpEntity entity = response.getEntity();
            
            if (null == entity) {
                return;
            }

            String htmlContent = EntityUtils.toString(entity);
            // System.out.println("HTML Contents:");
            // System.out.println(htmlContent);
            
            Document doc = Jsoup.parse(htmlContent);
            
            Element loginForm = doc.getElementsByTag("form").first();
            // select "<input ... />"
            loginForm.select("input");
            
            // Elements newsHeadlines = doc.select("#LoginForm");
            // System.out.println(newsHeadlines);
            // System.out.println(loginForm.select("input"));
            
            for (Element e : loginForm.select("input")) {
                // System.out.println(e.attributes());
                if (e.attributes().hasKey("id") && e.attr("id").equals("loginButton")) {
                    continue;
                } else if (e.attributes().hasKey("name") && e.attributes().hasKey("value")) {
                    System.out.println(e.attr("name") + " --- " + e.attr("value"));
                } else if (e.attributes().hasKey("name")) {
                    System.out.println(e.attr("name"));
                }
            }
            
            // Elements newsHeadlines = doc.select("LoginForm");
            // System.out.println(newsHeadlines);

            System.out.println("Initial set of cookies:");
            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

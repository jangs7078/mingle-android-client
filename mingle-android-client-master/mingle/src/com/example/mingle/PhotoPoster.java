package com.example.mingle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;



public class PhotoPoster {
	
	public static String postPhoto(ArrayList<String> photoPaths, String UID, final String baseURL) throws Exception {
	    
	    HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(baseURL + "add_photo?");
	    MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
	    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for(int i = 0; i < photoPaths.size(); i++) { 
	    	    		
	    	    		 FileBody fileBody = new FileBody(new File(photoPaths.get(i))); //image should be a String
	    	    	        builder.addPart("photo" + Integer.toString(i + 1), fileBody); 
	    	    		
	    	    	}    
		
		builder.addTextBody("uid", UID);
	    final HttpEntity yourEntity = builder.build();
	    
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    yourEntity.writeTo(bytes);
	    System.out.println(bytes.toString());
	    post.setEntity(yourEntity);
	    HttpResponse response = client.execute(post);        

	    return getContent(response);

	} 

	public static String getContent(HttpResponse response) throws IOException {
	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    String body = "";
	    String content = "";

	    while ((body = rd.readLine()) != null) 
	    {
	        content += body + "\n";
	    }
	    return content.trim();
	}

}

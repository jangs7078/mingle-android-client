package com.example.mingle;

import java.io.BufferedReader;
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
	    
	    class ProgressiveEntity implements HttpEntity {
	        private HttpEntity entity;
			
			public ProgressiveEntity(HttpEntity entity) {
				this.entity = entity;
			}
			
			@Override
	        public void consumeContent() throws IOException {
	            entity.consumeContent();                
	        }
	        @Override
	        public InputStream getContent() throws IOException,
	                IllegalStateException {
	            return entity.getContent();
	        }
	        @Override
	        public org.apache.http.Header getContentEncoding() {             
	            return entity.getContentEncoding();
	        }
	        @Override
	        public long getContentLength() {
	            return entity.getContentLength();
	        }
	        @Override
	        public org.apache.http.Header getContentType() {
	            return entity.getContentType();
	        }
	        @Override
	        public boolean isChunked() {             
	            return entity.isChunked();
	        }
	        @Override
	        public boolean isRepeatable() {
	            return entity.isRepeatable();
	        }
	        @Override
	        public boolean isStreaming() {             
	            return entity.isStreaming();
	        } // CONSIDER put a _real_ delegator into here!

	        @Override
	        public void writeTo(OutputStream outstream) throws IOException {

	            class ProxyOutputStream extends FilterOutputStream {
	                /**
	                 * @author Stephen Colebourne
	                 */

	                public ProxyOutputStream(OutputStream proxy) {
	                    super(proxy);    
	                }
	                public void write(int idx) throws IOException {
	                    out.write(idx);
	                }
	                public void write(byte[] bts) throws IOException {
	                    out.write(bts);
	                }
	                public void write(byte[] bts, int st, int end) throws IOException {
	                    out.write(bts, st, end);
	                }
	                public void flush() throws IOException {
	                    out.flush();
	                }
	                public void close() throws IOException {
	                    out.close();
	                }
	            } // CONSIDER import this class (and risk more Jar File Hell)

	            class ProgressiveOutputStream extends ProxyOutputStream {
	                public ProgressiveOutputStream(OutputStream proxy) {
	                    super(proxy);
	                }
	                public void write(byte[] bts, int st, int end) throws IOException {

	                    // FIXME  Put your progress bar stuff here!

	                    out.write(bts, st, end);
	                }
	            }

	            entity.writeTo(new ProgressiveOutputStream(outstream));
	        }
			

	    };
	    ProgressiveEntity myEntity = new ProgressiveEntity(yourEntity);
	    
	    post.setEntity(myEntity);
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

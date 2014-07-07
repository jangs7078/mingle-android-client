package com.example.mingle;

import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.view.View;

import com.example.mingle.HttpHelper;

import android.widget.*;

import com.example.mingle.MingleApplication;

//import com.google.android.gms.maps.model.Location;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {
    //public static int REQUEST_CODE = 1;
    //private Bitmap taken_photo_bitmap;		//Bitmap to save photo just taken
    //private ArrayList<Bitmap> photo_list;	//List of user's photos
    private String sex_option;				//Sex identity of user
    private String comment_option;			//Comment written by user
    private int num_option;					//Number of people with user
    
    
    //Server Address
    private static final String server_url = "http://ec2-54-178-214-176.ap-northeast-1.compute.amazonaws.com:8080";
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //InputStream stream = null;
    	
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            	
            	Uri selectedImage = imageUri;
                getContentResolver().notifyChange(selectedImage, null);
                ImageView imageView = (ImageView) findViewById(R.id.photoView1);
                ContentResolver cr = getContentResolver();
                
                try {
                     Bitmap taken_photo_bitmap = android.provider.MediaStore.Images.Media
                     .getBitmap(cr, selectedImage);
                    imageView.setImageBitmap(taken_photo_bitmap);
                    ((MingleApplication) this.getApplication()).currUser.addPhoto(taken_photo_bitmap);
                    Toast.makeText(this, selectedImage.toString(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Camera " + e.toString());
                    e.printStackTrace();
                }
            } 
   
    }
    

    
    private void getCurrentLocation() {
    	
    	// Acquire a reference to the system Location Manager
    	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	Criteria criteria = new Criteria();
    	String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        float lat = 0;
        float lon = 0;
        if(location != null){
        	
        	lat =(float) location.getLatitude();
        	lon =(float) location.getLongitude();
        } 
        ((MingleApplication) this.getApplication()).currUser.setLat(lat);
    	((MingleApplication) this.getApplication()).currUser.setLat(lon);
     
    	
    	
    	/*
    	// Define a listener that responds to location updates
    	LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	      //makeUseOfNewLocation(location);
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);*/
      
    }
    
    
    private void initializeUIViews() {
    	// Sets up the radiobutton
        sex_option = "M";
        RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        // get selected radio button from radioGroup
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group,
                    int checkedId) {
            	if(checkedId == R.id.radioFemale) sex_option="F";
            	else sex_option="M";
            }
        });
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        MingleApplication mingleApp = (MingleApplication) this.getApplication();
        
        //Initialize HttpHelper that supports HTTP GET/POST requests and socket connection
        mingleApp.connectHelper = new HttpHelper(server_url, this);
        //Initialize MingleUser object that stores current user's info
        mingleApp.currUser = new MingleUser();
        // Initialize the database helper that manages local storage
        mingleApp.dbHelper = new DatabaseHelper(this);

        initializeUIViews();
        // Get the user's current location
        //getCurrentLocation();
        
        //this.takePicture();
    }

    //On user creation request, get user's info and send request to server
    public void userCreateButtonPressed(View view) {
        //hard coded. need to be replaced later on
        comment_option = "hi";
        num_option = 4;
        
        MingleUser user =  ((MingleApplication) this.getApplication()).currUser;
        
        //Check validity of user input and send user creation request to server
        //if (user.isValid()) {
        	((MingleApplication) this.getApplication()).connectHelper.userCreateRequest(user.getPhotos(), 
        																			comment_option, 
        																			sex_option, 
        																			num_option, 
        																			user.getLong(), user.getLat());
       /*} else {
    	   showInvalidUserAlert();
           System.out.println("The user is not valid.");
       }*/
    }

    //Update MingleUser info and join Mingle Market
    //Called when user creation request confirmation returns from server
    public void joinMingle(JSONObject userData) {
        try {
            System.out.println(userData.toString());
            
            ((MingleApplication) this.getApplication()).currUser.setAttributes(userData.getString("UID"), userData.getString("SEX"), userData.getInt("NUM"), userData.getString("COMM"),
                                                                                        (float)userData.getDouble("LOC_LAT"), (float)userData.getDouble("LOC_LONG"), userData.getInt("DIST_LIM"));
        } catch(JSONException e){
            e.printStackTrace();
        }
        
        //Start activity for Mingle Market
        Intent i = new Intent(this, HuntActivity.class);
        startActivity(i);
    }
    
    private byte[] compressPicture(Bitmap pic) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;

    /* Takes a picture with the camera */
    private void takePicture() {
    	 Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	  File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT,
    	            Uri.fromFile(photo));
    	 imageUri = Uri.fromFile(photo);
    	 startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings )
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void showInvalidUserAlert() {
    	new AlertDialog.Builder(this)
        .setTitle("Invalid User")
        .setMessage("You need at least one photo and specify how many you are before mingling.")
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with delete
            }
         })
        .setIcon(android.R.drawable.ic_dialog_alert)
         .show();	
    }
}
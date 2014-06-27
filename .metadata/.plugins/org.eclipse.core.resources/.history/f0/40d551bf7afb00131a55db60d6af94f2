package com.example.mingle;

import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.example.mingle.HttpHelper;

import android.widget.*;

import com.example.mingle.MingleApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {
    //public static int REQUEST_CODE = 1;
    private Bitmap taken_photo_bitmap;
    private ArrayList<Bitmap> photo_list;
    private String sex_option;
    private String comment_option;
    private int num_option;
    private float latitude;
    private float longitude;
    

    private static final String server_url = "http://ec2-54-178-214-176.ap-northeast-1.compute.amazonaws.com:8080";
    
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //InputStream stream = null;
    	
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            	
            	Uri selectedImage = imageUri;
                getContentResolver().notifyChange(selectedImage, null);
                ImageView imageView = (ImageView) findViewById(R.id.photoView1);
                ContentResolver cr = getContentResolver();
                
                try {
                     taken_photo_bitmap = android.provider.MediaStore.Images.Media
                     .getBitmap(cr, selectedImage);
                    imageView.setImageBitmap(taken_photo_bitmap);
                    Toast.makeText(this, selectedImage.toString(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Camera " + e.toString());
                    e.printStackTrace();
                }
            } else if (requestCode == CONNECTION_FAILURE_RESOLUTION_REQUEST) { 
            	 switch (resultCode) {
                 	case RESULT_OK :
                 
                 		// Try the request again
                 		break;
            	 }
            	
            }
/*
 * If the result code is Activity.RESULT_OK, try
 * to connect again
 */
   
    }
    

   
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MingleApplication) this.getApplication()).initHelper = new HttpHelper(server_url, this);
        
        /*
        sex_option = "M";
        Switch mySwitch = (Switch)findViewById(R.id.sex_switch);
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)sex_option = "F";
                else sex_option = "M";
            }
        });
        
        */
        
        sex_option = "M";
        RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
     // get selected radio button from radioGroup
        if(radioSexGroup.getCheckedRadioButtonId() == 1) {
        	sex_option = "F";
        } 
        this.takePicture();
    }

    public void userCreateButtonPressed(View view) {
        //hard coded
        comment_option = "hi";
        num_option = 4;
        latitude = (float)4.11;
        longitude = (float)4.11;
        //if (new_user.isValid()) {
        ((MingleApplication) this.getApplication()).initHelper.sendInitialInfo(photo_list, comment_option, sex_option, num_option, longitude, latitude);
        /*initInfoObject.put("comm",comment);
            initInfoObject.put("sex",sex);
            initInfoObject.put("num",number);
            initInfoObject.put("loc_long",longitude);
            initInfoObject.put("loc_lat",latitude);*/
        //String base = "server_url"

          // Intent myIntent = new Intent(MainActivity.this, HuntActivity.class);
           //myIntent.putExtra("success", "value"); //Optional parameters
           //MainActivity.this.startActivity(myIntent);
       //} else {
       //    System.out.println("The user is not valid.");
       //}
    }

    public void createUser(JSONObject userData) {
        ((MingleApplication) this.getApplication()).currUser = new MingleUser();
        try {
            System.out.println(userData.toString());
            ((MingleApplication) this.getApplication()).currUser.addPhoto(taken_photo_bitmap);
            ((MingleApplication) this.getApplication()).currUser.setAttributes(userData.getString("UID"), userData.getString("SEX"), userData.getInt("NUM"), userData.getString("COMM"),
                                                                                        (float)userData.getDouble("LOC_LAT"), (float)userData.getDouble("LOC_LONG"), userData.getInt("DIST_LIM"));
        } catch(JSONException e){
            e.printStackTrace();
        }
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
    
    
    @Override
    protected void onResume() { 
    	
    }
    
    //////////////////////////////////////////////////Location services start here ////////////////////////////////////////////////////////
    /*
    private static class ErrorDialogFragment extends DialogFragment {
    	// Global field to contain the error dialog
    	private Dialog mDialog;
    	// Default constructor. Sets the dialog field to null
    	public ErrorDialogFragment() {
    			super();
    			mDialog = null;
    	}
    	// Set the dialog to display
    	public void setDialog(Dialog dialog) {
    		mDialog = dialog;
    	}
    	// Return a Dialog to the DialogFragment.
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
    		return mDialog;
    	}
    }

   
    private boolean servicesConnected() {
    // Check that Google Play services is available
    	int resultCode =
    			GooglePlayServicesUtil.
                	isGooglePlayServicesAvailable(this);
    // If Google Play services is available
    if (ConnectionResult.SUCCESS == resultCode) {
    // In debug mode, log the status
    Log.d("Location Updates",
            "Google Play services is available.");
    // Continue
    return true;
    // Google Play services was not available for some reason
    } else {
    // Get the error code
    int errorCode = connectionResult.getErrorCode();
    // Get the error dialog from Google Play services
    Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            errorCode,
            this,
            CONNECTION_FAILURE_RESOLUTION_REQUEST);

    // If Google Play services can provide an error dialog
    if (errorDialog != null) {
        // Create a new DialogFragment for the error dialog
        ErrorDialogFragment errorFragment =
                new ErrorDialogFragment();
        // Set the dialog in the DialogFragment
        errorFragment.setDialog(errorDialog);
        // Show the error dialog in the DialogFragment
        errorFragment.show(getSupportFragmentManager(),
                "Location Updates");
    }
    }
    }
    
    }

	*/
}





















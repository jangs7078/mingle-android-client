package com.example.mingle;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class ChatroomActivity extends ListActivity {

	
	Button btnSend;
	EditText txtSMS;
	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
		
		setContentView(R.layout.activity_chatroom);
    }
    
    public void sendSMS(View v){
    	
    	txtSMS=(EditText) findViewById(R.id.txt_inputText);
    	
		// TODO Auto-generated method stub
		String SMS=txtSMS.getText().toString();
		
		try {
			listItems.add(SMS);
	        adapter.notifyDataSetChanged();
	    } catch (Exception e) {				
			Toast.makeText(getApplicationContext(),	"SMS faild, please try again later!",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		txtSMS.setText("",BufferType.NORMAL);
	}

}

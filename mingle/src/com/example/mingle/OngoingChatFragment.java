package com.example.mingle;

import java.util.ArrayList;

import com.fortysevendeg.swipelistview.SwipeListView;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OngoingChatFragment extends Fragment {

	public ListView currentlychattinglistview;
	private ArrayList<String> currentItemData;
	private ArrayAdapter<String> currentlistAdapter ;
	
	private Activity parent; 
	
	public void addItem(String newChatter) { 
		currentItemData.add(newChatter);
	}
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 System.out.println("ongoing chatview create complete");
		parent = getActivity();
		
		View rootView = inflater.inflate(R.layout.ongoing_chat_fragment, container, false);
		
		currentlychattinglistview= (ListView)(rootView.findViewById(R.id.mingling)) ;
		
        // Stores 
        currentItemData = new ArrayList<String>();
        currentlistAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1, currentItemData);
        
        // Set the ArrayAdapter as the ListView's adapter.  
        currentlychattinglistview.setAdapter( currentlistAdapter );        
        
		return rootView;
	}
	
	
}

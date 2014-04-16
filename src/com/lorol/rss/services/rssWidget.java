package com.lorol.rss.services;

import java.io.IOException;
import java.net.URL;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;
import com.lorol.rss.R;

/*
 * This class the is the class that provides the service to the extension
 */

public class rssWidget extends DashClockExtension {
	
	public void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onUpdateData(int arg0) {
		
		boolean allGood = false;
		int mytags = 0;
		
		SharedPreferences speSettings = PreferenceManager.getDefaultSharedPreferences(this);
		
		boolean chkOnscreen = speSettings.getBoolean("scron", false);
		if (chkOnscreen) setUpdateWhenScreenOn(true);
		else setUpdateWhenScreenOn(false);
		
		ExtensionData edtInformation = new ExtensionData();
		edtInformation.visible(false);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo nifNetwork = connectivityManager.getActiveNetworkInfo();
        if (nifNetwork != null && nifNetwork.isConnected()) {
        
	
						try {
							
							boolean asXML = speSettings.getBoolean("pars", true);
							boolean rvr = speSettings.getBoolean("rvr", false);
							int toms = Integer.parseInt(speSettings.getString("toms", "3000"));	
							String[] SS = speSettings.getString("show_indexes", "1,0,3,2,4").split(",");
							
							Parser mypar = Parser.xmlParser();
							if (!asXML) mypar = Parser.htmlParser();
							
							URL myurl = new URL(speSettings.getString("rssurl","http://rss.cnn.com/rss/cnn_world.rss")); 
							// To fix the issues with & and other symbols in URL string
							
							Document doc = Jsoup.connect(myurl.toString())
								    .parser(mypar)  // parse as XML,
								    .timeout(toms)
								    .get();
							
							Elements lz = doc.select(speSettings.getString("pattern","item>title, item>pubdate"));
							if (lz.size() > 0) mytags = lz.size();
							
							try {
									int i = 0;
									int k = 0;
									
									while ((i < 5) && (i < mytags) && ( i < SS.length)){ 

											
											try {
													k = Integer.parseInt(SS[i]);
													if (rvr) k = mytags - k - 1;
	
												} catch (NumberFormatException e) {
													break;
												}
											
											if (lz.get(k).text() != null){	
												edtInformation
													.expandedBody((edtInformation.expandedBody() == null ? ""
													: edtInformation.expandedBody() + "\n")
													+ lz.get(k).text());
											 }
											i++;
									}									
									
									edtInformation.visible(true);
									allGood = true;
								} catch (Exception e) {

								}
	
						} catch (IOException e) {
		                    
		                    if (e instanceof HttpStatusException) {

		                    	if (((HttpStatusException) e).getStatusCode() >= 400 && ((HttpStatusException) e).getStatusCode() <= 599) {
		                    	}

		                    }

						}
        }
		
        boolean showOn = speSettings.getBoolean("showon", true);
        boolean numsOn = speSettings.getBoolean("nums", true);
        
        if (numsOn){
        	edtInformation.status( "(" + String.valueOf(mytags) + ") " + speSettings.getString("rsstitle", "CNN World"));
        } else {
        	edtInformation.status(speSettings.getString("rsstitle", "CNN World"));
        }
        
        if ((allGood)||(showOn)){
			if (!allGood){
				edtInformation.status(speSettings.getString("rsstitle", "CNN World") + " *");
				edtInformation.expandedBody(null); 
				edtInformation.visible(true);
			}		
    		edtInformation.icon(R.drawable.ic_dashclock);
    		edtInformation.clickIntent(null); // just refresh
        } else {
    		edtInformation.clean();
    		edtInformation.visible(false);
        }
        
        publishUpdate(edtInformation);
	}

	public void onDestroy() {
		super.onDestroy();
	}

}
package com.lorol.rss.services;

import java.io.IOException;

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
							
							Parser mypar = Parser.xmlParser();
							if (!asXML) mypar = Parser.htmlParser();
							
							Document doc = Jsoup.connect(speSettings.getString("rssurl","http://goo.im/rss&path=/devs/paranoidandroid/roms"))
								    .parser(mypar)  // parse as XML,
								    .get();
							
							Elements lz = doc.select(speSettings.getString("pattern","item>title:containsOwn(mako) , item>title:containsOwn(gapps)")); 
							
							try {
									
									int i = 0;
									while ((lz.get(i).text() != null) && (i < 4)){
											if (!lz.get(i).text().isEmpty()){	
												edtInformation
													.expandedBody((edtInformation.expandedBody() == null ? ""
													: edtInformation.expandedBody() + "\n")
													+ lz.get(i).text());
												i++;
											 }
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
        edtInformation.status(speSettings.getString("rsstitle", "Goo.im"));

        if ((allGood)||(showOn)){
			if (!allGood){
				edtInformation.status(speSettings.getString("rsstitle", "Goo.im") + "*");
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
package com.laithnurie.baka;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MangaList extends Activity {
	
	private LinearLayout mangaList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);
        
                
        Bundle extras = getIntent().getExtras();
        String feedURL = extras.getString("feedUrl");
       // getFeed(feedURL);
        
        new loadMangaList().execute(feedURL);
        
        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_manga_list, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    public class loadMangaList extends AsyncTask<String, Integer, TextView> {
    	
    	ProgressDialog pd;
    	String passedParam;


    	@Override
    	protected void onPreExecute() {
    		
    		pd = new ProgressDialog(MangaList.this);
    		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		pd.setMax(100);
    		pd.show();
    		
    	}
    	
    	@Override
    	protected TextView doInBackground(final String... params) {
    		
    		TextView title[] =null;	
    		TextView link[];
    		TextView desc[];
            mangaList = (LinearLayout) findViewById(R.id.mangaList);
                	    		
    		passedParam = params[0];
    	

    	
    		try {
    	
    			URL url = new URL("http://www.mangahere.com/rss/"+params[0]+".xml");
    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    			DocumentBuilder db = dbf.newDocumentBuilder();
    			Document doc = db.parse(new InputSource(url.openStream()));
    			doc.getDocumentElement().normalize();
    	
    			NodeList nodeList = doc.getElementsByTagName("item");
    	
    			title = new TextView[nodeList.getLength()];
    			link = new TextView[nodeList.getLength()];
    			desc = new TextView[nodeList.getLength()];
    			
	    		//mangaList.removeAllViewsInLayout();

    			
    			for (int i = 0; i < 100; i++) {
    					
    				Node node = nodeList.item(i);
    	
    				title[i] = new TextView(getApplicationContext());
    				link[i] = new TextView(getApplicationContext());
    				desc[i] = new TextView(getApplicationContext());
    	
    				Element fstElmnt = (Element) node;
    				NodeList titleList = fstElmnt.getElementsByTagName("title");
    				Element titleElement = (Element) titleList.item(0);
    				titleList = titleElement.getChildNodes();
    				if (titleList.item(0) !=null) {
    					title[i].setText("Chapter No = "+ ((Node) titleList.item(0)).getNodeValue());
    					//mangaList.addView(title[i]);
    					
    					return title[i];
    	
    				}
    				else {
    					title[i].setText("There is no Title");
    				}	
    				
    	
    				NodeList linkList = fstElmnt.getElementsByTagName("link");
    				Element linkElement = (Element) linkList.item(0);
    				linkList = linkElement.getChildNodes();
    				if (linkList.item(0) !=null) {
    					link[i].setText("Link = "+ ((Node) linkList.item(0)).getNodeValue());
    					//mangaList.addView(link[i]);
    	
    				}
    				else {
    					link[i].setText("no link available");
    				}
    	
    				
    				NodeList descList = fstElmnt.getElementsByTagName("description");
    				Element descElement = (Element) descList.item(0);
    				descList = descElement.getChildNodes();
    				if (descList.item(0) !=null) {
    					desc[i].setText("title = "+ ((Node) descList.item(0)).getNodeValue());
    					//mangaList.addView(desc[i]);
    				}
    				else {
    					desc[i].setText("No Description available");
    				}
    				
    				publishProgress(i);
    				Thread.sleep(88);	
    			}
    		} 
    		
    		catch (Exception e) {
    			Log.v("mangaList", e.getMessage());
    		}
    		return title[0];
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    		pd.incrementProgressBy(progress[0]);	
    	}
    	
    	@Override
        protected void onPostExecute(TextView result) {
        	pd.dismiss();

    		Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_LONG).show();
    		mangaList.addView(result);
        }
    }
}

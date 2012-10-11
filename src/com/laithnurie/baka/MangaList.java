package com.laithnurie.baka;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MangaList extends Activity {
	
	LinearLayout mangaList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
    
    
    public void getFeed(String feedUrl)	{
	
	TextView title[];	
	TextView link[];
	TextView desc[];

	mangaList.removeAllViewsInLayout();

	try {

		URL url = new URL("http://www.mangahere.com/rss/"+feedUrl+".xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(url.openStream()));
		doc.getDocumentElement().normalize();

		NodeList nodeList = doc.getElementsByTagName("item");

		title = new TextView[nodeList.getLength()];
		link = new TextView[nodeList.getLength()];
		desc = new TextView[nodeList.getLength()];
		
		for (int i = 0; i < 1; i++) {

			Node node = nodeList.item(i);

			title[i] = new TextView(this);
			link[i] = new TextView(this);
			desc[i] = new TextView(this);

			Element fstElmnt = (Element) node;
			NodeList titleList = fstElmnt.getElementsByTagName("title");
			Element titleElement = (Element) titleList.item(0);
			titleList = titleElement.getChildNodes();
			if (titleList.item(0) !=null) {
				title[i].setText("Chapter No = "+ ((Node) titleList.item(0)).getNodeValue());
				mangaList.addView(title[i]);

			}
			else {
				title[i].setText("There is no Title");
			}	
			

			NodeList linkList = fstElmnt.getElementsByTagName("link");
			Element linkElement = (Element) linkList.item(0);
			linkList = linkElement.getChildNodes();
			if (linkList.item(0) !=null) {
				link[i].setText("Link = "+ ((Node) linkList.item(0)).getNodeValue());
				mangaList.addView(link[i]);

			}
			else {
				link[i].setText("no link available");
			}

			
			NodeList descList = fstElmnt.getElementsByTagName("description");
			Element descElement = (Element) descList.item(0);
			descList = descElement.getChildNodes();
			if (descList.item(0) !=null) {
				desc[i].setText("title = "+ ((Node) descList.item(0)).getNodeValue());
				mangaList.addView(desc[i]);
			}
			else {
				desc[i].setText("No Description available");
			}
			
		}
	} 
	
	catch (Exception e) {
		System.out.println("XML Pasing Excpetion = " + e);
	}

	}

}

package com.androidpeople.xml.parsing;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XMLParsingDOMExample extends Activity {
	
	LinearLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/** Create a new layout to display the view */
		layout = (LinearLayout) findViewById(R.id.insideLayout);
		
		layout.setOrientation(1);
		/** Create a new textview array to display the results */
		
		Button button1 = (Button) findViewById(R.id.button1);
		final EditText et = (EditText) findViewById(R.id.editText1);
		
		button1.setOnClickListener(new View.OnClickListener() {
            		public void onClick(View v) {
            	
            			getFeed(et.getText().toString());
            
            		}
        	});
	}
	
	
	public void getFeed(String feedUrl)
	{
	
	TextView title[];	
	TextView link[];
	TextView desc[];

	layout.removeAllViewsInLayout();

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
				layout.addView(title[i]);

			}
			else {
				title[i].setText("There is no Title");
			}	
			

			NodeList linkList = fstElmnt.getElementsByTagName("link");
			Element linkElement = (Element) linkList.item(0);
			linkList = linkElement.getChildNodes();
			if (linkList.item(0) !=null) {
				link[i].setText("Link = "+ ((Node) linkList.item(0)).getNodeValue());
				layout.addView(link[i]);

			}
			else {
				link[i].setText("no link available");
			}

			
			NodeList descList = fstElmnt.getElementsByTagName("description");
			Element descElement = (Element) descList.item(0);
			descList = descElement.getChildNodes();
			if (descList.item(0) !=null) {
				desc[i].setText("title = "+ ((Node) descList.item(0)).getNodeValue());
				layout.addView(desc[i]);
			}
			else {
				desc[i].setText("No Description available");
			}
			
		}
	} 
	
	catch (Exception e) {
		System.out.println("XML Pasing Excpetion = " + e);
	}

	}}

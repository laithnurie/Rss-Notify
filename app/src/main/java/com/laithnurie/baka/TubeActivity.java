package com.laithnurie.baka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TubeActivity extends Activity {

    /**
     * Called when the activity is first created.
     */

    TextView bakerloo;
    TextView central;
    TextView circle;
    TextView district;
    TextView dlr;
    TextView hammersmith;
    TextView jubilee;
    TextView metropolitan;
    TextView northern;
    TextView overground;
    TextView piccadily;
    TextView victoria;
    TextView waterloocity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tube);

        bakerloo = (TextView) findViewById(R.id.baker_stat);
        central = (TextView) findViewById(R.id.central_stat);
        circle = (TextView) findViewById(R.id.circle_stat);
        district = (TextView) findViewById(R.id.district_stat);
        dlr = (TextView) findViewById(R.id.dlr_stat);
        hammersmith = (TextView) findViewById(R.id.hammer_stat);
        jubilee = (TextView) findViewById(R.id.jubilee_stat);
        metropolitan = (TextView) findViewById(R.id.metro_stat);
        northern = (TextView) findViewById(R.id.north_stat);
        overground = (TextView) findViewById(R.id.over_stat);
        piccadily = (TextView) findViewById(R.id.piccadily_stat);
        victoria = (TextView) findViewById(R.id.victoria_stat);
        waterloocity = (TextView) findViewById(R.id.water_stat);

        new loadTubeJson().execute();

    }


    public class loadTubeJson extends AsyncTask<String, Integer, NodeList> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(TubeActivity.this);
            pd.setCanceledOnTouchOutside(false);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(100);
            pd.show();
        }

        @Override
        protected NodeList doInBackground(final String... params) {

            Document doc = null;
            try {
                URL url = new URL("http://cloud.tfl.gov.uk/TrackerNet/LineStatus");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (doc != null) {
                return doc.getElementsByTagName("LineStatus");
            } else{
                return null;
            }
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            pd.incrementProgressBy(progress[0]);
        }

        @Override
        protected void onPostExecute(NodeList tubeData) {
            try {

                for (int i = 0; i < tubeData.getLength(); i++) {

                    Node line = tubeData.item(i);
                    String lineName = line.getChildNodes().item(3).getAttributes().getNamedItem("Name").getNodeValue();
                    String lineStatus = line.getChildNodes().item(5).getAttributes().getNamedItem("Description").getNodeValue();

                    Log.i("tube", lineName + " : " + lineStatus);

                    switch (i) {
                        case 0:
                            bakerloo.setText(lineStatus);
                            break;
                        case 1:
                            central.setText(lineStatus);
                            break;
                        case 2:
                            circle.setText(lineStatus);
                            break;
                        case 3:
                            district.setText(lineStatus);
                            break;
                        case 4:
                            hammersmith.setText(lineStatus);
                            break;
                        case 5:
                            jubilee.setText(lineStatus);
                            break;
                        case 6:
                            metropolitan.setText(lineStatus);
                            break;
                        case 7:
                            northern.setText(lineStatus);
                            break;
                        case 8:
                            piccadily.setText(lineStatus);
                            break;
                        case 9:
                            victoria.setText(lineStatus);
                            break;
                        case 10:
                            waterloocity.setText(lineStatus);
                            break;
                        case 11:
                            overground.setText(lineStatus);
                            break;
                        case 12:
                            dlr.setText(lineStatus);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
} 
package jj.home.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class StronaDodawaniaPrzedmiotow extends AppCompatActivity {
    public boolean czyZrobilemZdjecie = false;
    TextView TekstZNazwa;
    TextView TekstZTagami;
    String nazwaAktualnegoPliku;
    int IloscElemtentowXML = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strona_dodawania_przedmiotow);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nazwaAktualnegoPliku = extras.getString("nazwaAktualnegoPliku");
        }

        TekstZNazwa = findViewById(R.id.editText);
        TekstZTagami = findViewById(R.id.editText2);

        Button PrzyciskTworzacyKontener = (findViewById(R.id.button2));
        PrzyciskTworzacyKontener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(TekstZNazwa.getText().toString())) {
                    TekstZNazwa.setError("Proszę o wypełnienie.");
                } else if (TextUtils.isEmpty(TekstZTagami.getText().toString())) {
                    TekstZTagami.setError("Proszę o wypełnienie.");
                } else {
                    Toast.makeText(StronaDodawaniaPrzedmiotow.this, "Tworzę kontener", Toast.LENGTH_LONG).show();
                    ZapisDoPlikuXML();
                    finish();
                }
            }
        });
    }

    public void ZapisDoPlikuXML() {
        String zdjecie;
        String nazwa = TekstZNazwa.getText().toString();
        String tagi = TekstZTagami.getText().toString();

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        if (czyZrobilemZdjecie) {
            //TODO
            zdjecie = "R.drawable.inv";
        } else {
            zdjecie = "R.drawable.inv";
        }



        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();


        if(CzyIstniejePlik(nazwaAktualnegoPliku))
        {
            try {
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "przedmioty");

                // Fragment kodu wczytujacego plik xml przed dodaniem nowych elementow


                db = dbf.newDocumentBuilder();
                FileInputStream fis = openFileInput(nazwaAktualnegoPliku);
                doc = db.parse(fis);


                NodeList przedmioty = doc.getElementsByTagName("przedmiot");

                IloscElemtentowXML = przedmioty.getLength();

                for (int i = 0; i < przedmioty.getLength(); i++) {

                    Node city = przedmioty.item(i);

                    serializer.startTag("", city.getNodeName());
                    serializer.attribute("", "id", Integer.toString(i));

                    NodeList cityInfo = city.getChildNodes();
                    for (int j = 0; j < cityInfo.getLength(); j++) {
                        Node info = cityInfo.item(j);
                        serializer.startTag("", info.getNodeName());
                        serializer.text(info.getTextContent());
                        serializer.endTag("", info.getNodeName());
                    }
                    serializer.endTag("", city.getNodeName());

                }
                fis.close();


                serializer.startTag("", "przedmiot");
                serializer.attribute("", "id", Integer.toString(IloscElemtentowXML));


                serializer.startTag("", "nazwa");
                serializer.text(nazwa);
                serializer.endTag("", "nazwa");

                serializer.startTag("", "tagi");
                serializer.text(tagi);
                serializer.endTag("", "tagi");


                serializer.endTag("", "przedmiot");

                serializer.endTag("", "przedmioty");
                serializer.endDocument();
                String result = writer.toString();
                ZapsywanieWczytywanie.ZapisDoPliku(this, nazwaAktualnegoPliku, result);


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try{
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "przedmioty");
                serializer.startTag("", "przedmiot");
                serializer.attribute("", "id", Integer.toString(IloscElemtentowXML));


                serializer.startTag("", "nazwa");
                serializer.text(nazwa);
                serializer.endTag("", "nazwa");

                serializer.startTag("", "tagi");
                serializer.text(tagi);
                serializer.endTag("", "tagi");

                serializer.endTag("", "przedmiot");

                serializer.endTag("", "przedmioty");
                serializer.endDocument();
                String result = writer.toString();
                ZapsywanieWczytywanie.ZapisDoPliku(this, nazwaAktualnegoPliku, result);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    public boolean CzyIstniejePlik(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}

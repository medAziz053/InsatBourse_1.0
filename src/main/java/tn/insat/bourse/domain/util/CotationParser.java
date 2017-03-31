package tn.insat.bourse.domain.util;

import org.springframework.stereotype.Component;
import tn.insat.bourse.domain.Cotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tn.insat.bourse.domain.SimpleCotation;

/**
 * Created by WiKi on 24/12/2016.
 */
@Component
public class CotationParser {
    private static String ILBOURSA ="http://www.ilboursa.com/marches/aaz.aspx";
    private static String BVMT ="http://www.bvmt.com.tn/fr/entreprises/list";

    private String source= "";
    private List<Cotation> cotations= null;

    public CotationParser ()
    {

    }

    public void setSource(int i){
        switch(i){
            case(0): source= ILBOURSA;
                break;
            case(1): source= BVMT;
                break;
            default: source= BVMT;
                break;
        }
    }

    public List<Cotation> getCotations(){
        if(source.equals("")) return null;
        else{

            if(source.equals(ILBOURSA)){
                Document doc = null;
                try {
                    doc = Jsoup.connect("http://ilboursa.com/marches/aaz.aspx").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cotations = new ArrayList<Cotation>();
                Cotation temp ;
                int i=0;
                for (Element table : doc.select("table[class=tablesorter tbl100_6 tbl1]")) {
                    Elements links = table.getElementsByTag("a");
                    for (Element row : table.select("tr")) {
                        Elements tds = row.select("td");
                        if (tds.size() ==8) {
                            temp = new Cotation();
                            temp.setCotDetails(links.get(i).attr("href"));
                            i++;
                            temp.setCompanyName(tds.get(0).text());
                            temp.setOuverture(Float.parseFloat(tds.get(1).text()));
                            temp.setpHaut(Float.parseFloat(tds.get(2).text()));
                            temp.setpBas(Float.parseFloat(tds.get(3).text()));
                            temp.setVolumeTitres(Integer.parseInt(tds.get(4).text()));
                            temp.setVolumeDT(Integer.parseInt(tds.get(5).text()));
                            temp.setDernier(Float.parseFloat(tds.get(6).text()));
                            temp.setVariation(tds.get(7).text());

                            cotations.add(temp);

                        }
                    }
                }

            }
            return cotations;
        }

    }

    public List<SimpleCotation> getSortedVarCotations(){
        if(source.equals("")) return null;
        else{

            if(source.equals(ILBOURSA)){
                Document doc = null;
                try {
                    doc = Jsoup.connect("http://ilboursa.com/marches/aaz.aspx").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cotations = new ArrayList<Cotation>();
                Cotation temp ;
                int i=0;
                for (Element table : doc.select("table[class=tablesorter tbl100_6 tbl1]")) {
                    Elements links = table.getElementsByTag("a");
                    for (Element row : table.select("tr")) {
                        Elements tds = row.select("td");
                        if (tds.size() ==8) {
                            temp = new Cotation();
                            temp.setCotDetails(links.get(i).attr("href"));
                            i++;
                            temp.setCompanyName(tds.get(0).text());
                            temp.setOuverture(Float.parseFloat(tds.get(1).text()));
                            temp.setpHaut(Float.parseFloat(tds.get(2).text()));
                            temp.setpBas(Float.parseFloat(tds.get(3).text()));
                            temp.setVolumeTitres(Integer.parseInt(tds.get(4).text()));
                            temp.setVolumeDT(Integer.parseInt(tds.get(5).text()));
                            temp.setDernier(Float.parseFloat(tds.get(6).text()));
                            temp.setVariation(tds.get(7).text());
                            cotations.add(temp);

                        }
                    }
                }

            }
            SimpleCotation sc ;
            List<SimpleCotation> l = new ArrayList<SimpleCotation>();
            for (Cotation c : cotations)
            {
                sc = new SimpleCotation(c);
                l.add(sc);
            }
            Collections.sort(l, new Comparator<SimpleCotation>() {
                @Override
                public int compare(SimpleCotation sc1, SimpleCotation sc2) {
                    if (sc1.getVar() > sc2.getVar())return -1;
                    else if (sc1.getVar()<sc2.getVar()) return 1;
                    else return 0;
                }
            });

            return l;
        }

    }


}

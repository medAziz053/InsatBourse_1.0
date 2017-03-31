package tn.insat.bourse.domain;

/**
 * Created by WiKi on 24/12/2016.
 * following http://www.ilboursa.com/marches/aaz.aspx
 */

public class Cotation {

    private String companyName ;
    private float ouverture;
    private float pHaut;
    private float pBas;
    private int volumeTitres;
    private int volumeDT;
    private float dernier;
    private String variation;
    private String CotDetails;

    public String getCotDetails() {
        return CotDetails;
    }
    public void setCotDetails(String cotDetails) {
        CotDetails = "http://www.ilboursa.com/marches/"+cotDetails;
    }




    public Cotation(String companyName, float ouverture, float pHaut, float pBas, int volumeTitres, int volumeDT, float dernier, String variation) {
        this.companyName = companyName;
        this.ouverture = ouverture;
        this.pHaut = pHaut;
        this.pBas = pBas;
        this.volumeTitres = volumeTitres;
        this.volumeDT = volumeDT;
        this.dernier = dernier;
        this.variation = variation;
    }

    public Cotation(){}

    public float getVariationValue()
    {
        String v = this.variation.substring(0,this.variation.length()-1);
        return Float.parseFloat(v);

    }

    public String getCompanyName() {
        return companyName;
    }

    public float getOuverture() {
        return ouverture;
    }

    public float getpHaut() {
        return pHaut;
    }

    public float getpBas() {
        return pBas;
    }

    public int getVolumeTitres() {
        return volumeTitres;
    }

    public int getVolumeDT() {
        return volumeDT;
    }

    public float getDernier() {
        return dernier;
    }

    public String getVariation() {
        return variation;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setOuverture(float ouverture) {
        this.ouverture = ouverture;
    }

    public void setpBas(float pBas) {
        this.pBas = pBas;
    }

    public void setpHaut(float pHaut) {
        this.pHaut = pHaut;
    }

    public void setVolumeTitres(int volumeTitres) {
        this.volumeTitres = volumeTitres;
    }

    public void setVolumeDT(int volumeDT) {
        this.volumeDT = volumeDT;
    }

    public void setDernier(float dernier) {
        this.dernier = dernier;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    @Override
    public String toString() {
        return "Cotation{" +
            "companyName='" + companyName + '\'' +
            ", ouverture=" + ouverture +
            ", pHaut=" + pHaut +
            ", pBas=" + pBas +
            ", volumeTitres=" + volumeTitres +
            ", volumeDT=" + volumeDT +
            ", dernier=" + dernier +
            ", variation='" + variation + '\'' +
            '}';
    }

}

package tn.insat.bourse.domain;

/**
 * Created by WiKi on 27/12/2016.
 */
public class SimpleCotation {

    private String name;
    private float value;
    private float var;

    public SimpleCotation(Cotation c)
    {
        this.name=c.getCompanyName();
        this.value = c.getOuverture();
        this.var = c.getVariationValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(SimpleCotation c)
    {
        if (this.var < c.var) return -1;
        else if (this.var> c.var) return 1;
        else return 0;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getVar() {
        return var;
    }

    public void setVar(float var) {
        this.var = var;
    }
}

package tn.insat.bourse.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActionBourse.
 */
@Entity
@Table(name = "action_bourse")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActionBourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    private Portefeuil portefeuil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ActionBourse name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public ActionBourse price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ActionBourse quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Portefeuil getPortefeuil() {
        return portefeuil;
    }

    public ActionBourse portefeuil(Portefeuil portefeuil) {
        this.portefeuil = portefeuil;
        return this;
    }

    public void setPortefeuil(Portefeuil portefeuil) {
        this.portefeuil = portefeuil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionBourse actionBourse = (ActionBourse) o;
        if(actionBourse.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, actionBourse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ActionBourse{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", price='" + price + "'" +
            ", quantity='" + quantity + "'" +
            '}';
    }
}

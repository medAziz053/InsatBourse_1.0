package tn.insat.bourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tn.insat.bourse.domain.Cotation;
import tn.insat.bourse.domain.SimpleCotation;
import tn.insat.bourse.domain.util.CotationParser;

import java.util.List;

/**
 * Created by WiKi on 25/12/2016.
 */
@RestController
@RequestMapping("/cotation")
public class CotationController {

    @Autowired
    CotationParser cp ;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Cotation> getCotations()
    {
        cp.setSource(0);
        return cp.getCotations();
    }
    @RequestMapping(value = "/sorted", method = RequestMethod.GET)
    public List<SimpleCotation> getSortedCotations()
    {
        cp.setSource(0);
        return cp.getSortedVarCotations();
    }

}

package one.xingyi.restExample;

import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.entity.Embedded;

import java.util.HashMap;
import java.util.Map;

@XingYi()
public interface IPerson {
    @XingYiField(readInterfaces = Interfaces.personNameOps)
    String name(); //

    @XingYiField(interfaces = Interfaces.personAddressOps)
    IAddress address();

    @XingYiField(interfaces = Interfaces.personTelephoneOps)
    Embedded<ITelephoneNumber> telephone();


    @XingYiField(deprecated = true)
    default String line1() {throw new RuntimeException("not implemented");}

    @XingYiField(deprecated = true)
    default String line2() {throw new RuntimeException("not implemented");}

    //function lens_person_telephonenumber_telephonenumber(){ return lens("telephoneNumber");};
    //function lens_person_line2_string(){ return lens("line2");};
    //function lens_person_name_string(){ return lens("name");};
    //function lens_telephonenumber_number_string(){ return lens("number");};
    //function lens_person_line1_string(){ return lens("line1");};
}

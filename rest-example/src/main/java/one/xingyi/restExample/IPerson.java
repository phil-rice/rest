package one.xingyi.restExample;

import one.xingyi.restAnnotations.XingYi;
import one.xingyi.restAnnotations.XingYiLegacy;

@XingYi()
        // this makes all the methods available in person.
public interface IPerson { // this is no need for Person to extend IPerson. And this simplifies things a lot!
    @XingYi(Interfaces.personNameOps)
    String name(); //

    @XingYi(Interfaces.personTelephoneOps)
    ITelephoneNumber telephone();

    @XingYi(Interfaces.personAddressOps)
    IAddress address();

    @XingYiLegacy
    String personLine1();
    @XingYiLegacy
    String personLine2();
}
//creates
//    static final Lens<Person, String> personNameLens = Lens.<Person, String>create(Person::getName, Person::setName);
//    public String getName() { return name; }
//    public Person setName(String name) { return null; }
//creates
//    static final Lens<Person, Address> personAddressLens = Lens.<Person, Address>create(Person::getAddress, Person::setAddress);
//    public Address getAddress() { return address; }
//    public Person setAddress(Address address) { return null;}


//    @XingYiLegacy(personLine12Ops, "address.line1")

//    static final Lens<Person, String> personLine1Lens = personAddressLens.andThen(Address.addressLine1Lens);
//    public Address getLine1() { return personLine1Lens.get(this); }
//    public Person setLine1(String line1) { return personLine1Lens.set(this, line1);}

//    @XingYiLegacy(personLine12Ops, "address.line2")
//    String personLine2();

//    final static Person prototype = new Person("", ITelephoneNumber.protoype, IAddress.protoype);

//This could have a default value that we derive
//    final static Projection projection = Projection.project(
//            Projection.stringField("name"),
//            Projection.stringField("line1"),
//            Projection.stringField("line2"),
//            Projection.objectField("telephone"));


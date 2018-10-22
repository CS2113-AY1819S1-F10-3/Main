package seedu.addressbook.data.PoliceOfficers;

import org.javatuples.Triplet;
import seedu.addressbook.common.Location;

import java.util.LinkedHashMap;

public class Personnel {
    private static LinkedHashMap<String, Triplet<Boolean, Location,Boolean>> personnelList;
    static{
        personnelList = new LinkedHashMap<String,Triplet<Boolean, Location, Boolean>>();
        personnelList.put("HQP",Triplet.with(false,new Location(1.300009, 103.770602),false));
        personnelList.put("PO1",Triplet.with(false,new Location(1.300009, 103.770602),false));
        personnelList.put("PO2",Triplet.with(false,new Location(1.300009, 103.770602),false));
        personnelList.put("PO3",Triplet.with(false,new Location(1.300009, 103.770602),false));
        personnelList.put("PO4",Triplet.with(false,new Location(1.300009, 103.770602),false));
        personnelList.put("PO5",Triplet.with(false,new Location(1.300009, 103.770602),false));
    }



}

/*
 * Conductivity.java
 *
 * Created on 1. november 2000, 19:00
 */

package neqsim.physicalProperties.physicalPropertyMethods.commonPhasePhysicalProperties.conductivity;
/**
 *
 * @author  Even Solbraa
 * @version
 */
abstract class Conductivity extends neqsim.physicalProperties.physicalPropertyMethods.commonPhasePhysicalProperties.CommonPhysicalPropertyMethod implements  neqsim.physicalProperties.physicalPropertyMethods.methodInterface.ConductivityInterface{

    private static final long serialVersionUID = 1000;
    
    double conductivity=0;
    /** Creates new Conductivity */
    public Conductivity() {
    }
    
    public Conductivity(neqsim.physicalProperties.physicalPropertySystem.PhysicalPropertiesInterface phase) {
        super(phase);
    }
    
      public Object clone(){
        Conductivity properties = null;
        
        try{
            properties = (Conductivity) super.clone();
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
        }
        
        return properties;
    }
    
}

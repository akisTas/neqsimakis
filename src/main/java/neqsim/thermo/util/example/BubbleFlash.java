package neqsim.thermo.util.example;

import neqsim.thermo.system.SystemInterface;
import neqsim.thermo.system.SystemSrkCPAstatoil;
import neqsim.thermodynamicOperations.ThermodynamicOperations;

/*
 * TPflash.java
 *
 * Created on 27. september 2001, 09:43
 */

/**
 *
 * @author  esol
 * @version
 */
public class BubbleFlash {

    private static final long serialVersionUID = 1000;
    
    /** Creates new TPflash */
    public BubbleFlash() {
    }
    
    public static void main(String args[]){
        SystemInterface testSystem = new SystemSrkCPAstatoil(273.15 + 25.0, 1.0);
        //SystemInterface testSystem = new SystemSrkEos(288, 5.0);
        ThermodynamicOperations testOps = new ThermodynamicOperations(testSystem);
        
        testSystem.addComponent("methane", 0.05175);
        testSystem.addComponent("n-butane", 0.5175);
       // testSystem.addComponent("TEG", 0.0000000225);
       
      //  
      //  testSystem.addComponent("MEG", 30);
        testSystem.createDatabase(true);
        // 1- orginal no interaction 2- classic w interaction
        // 3- Huron-Vidal 4- Wong-Sandler
        testSystem.setMixingRule(10);
        //testSystem.setMixingRule("HV", "UNIFAC_PSRK");
        try{
           // testOps.dewPointPressureFlash();
          //  testOps.bubblePointPressureFlash(false);
           // testSystem.display();
            testOps.constantPhaseFractionPressureFlash(1.0);
             testSystem.display();
        }catch(Exception e){
            System.out.println(e.toString());
        }
        
      //  System.out.println("wt% MEG " + 100*testSystem.getPhase(1).getComponent("MEG").getx()*testSystem.getPhase(1).getComponent("MEG").getMolarMass()/testSystem.getPhase(1).getMolarMass());
      
      //  testSystem.display();
        
    }
}
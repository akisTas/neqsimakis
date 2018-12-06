package neqsim.thermo.util.example;

import neqsim.thermo.system.SystemInterface;
import neqsim.thermo.system.SystemSrkCPAstatoil;
import neqsim.thermodynamicOperations.ThermodynamicOperations;

/*
 * TPflash.java
 *
 * Created on 27. september 2001, 09:43
 */

/*
 *
 * @author esol @version
 */
public class TestUniSimFLash {

    private static final long serialVersionUID = 1000;

    /**
     * Creates new TPflash
     */
    public TestUniSimFLash() {
    }

    public static void main(String[] args) {

        // SystemInterface testSystem = new SystemSrkEos(288.15 + 5, 165.01325);//
        SystemInterface testSystem = new SystemSrkCPAstatoil(273.15 + 15.0, 25.0);//
        testSystem.addComponent("CO2", 0.0214);
        testSystem.addComponent("nitrogen", 0.00892);
        testSystem.addComponent("methane", 0.858);
        testSystem.addComponent("nC10", 0.00892);
        testSystem.addComponent("water", 0);

        testSystem.createDatabase(true);
        testSystem.setMixingRule(10);
        testSystem.setMultiPhaseCheck(true);

        ThermodynamicOperations testOps = new ThermodynamicOperations(testSystem);

        for (int i = 0; i < 1; i++) {
            testOps.TPflash();
        }
        testSystem.display();

        double[] composition = new double[]{0.1, 0.0, 1.1, 1.0, 1.0};
        testSystem.removeMoles();
        testSystem.setMolarComposition(composition);
        testSystem.init(0);
       // testSystem.setMultiPhaseCheck(true);

        for (int i = 0; i < 1; i++) {
            testOps.TPflash();
        }
        testSystem.display();

        /*
        composition = new double[]{0.1, 0.0, 1.1, 1.0, 1.0};
        testSystem.removeMoles();
        testSystem.setMolarComposition(composition);
        for (int i = 0; i < 1; i++) {
            testOps.TPflash();
        }
        testSystem.display();
       */
    }
}

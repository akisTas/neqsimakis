/*
 * Copyright 2018 ESOL.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neqsim.thermodynamicOperations.util.example;

import neqsim.thermo.system.SystemInterface;
import neqsim.thermo.system.SystemSrkCPAstatoil;
import neqsim.thermo.system.SystemSrkEos;
import neqsim.thermodynamicOperations.ThermodynamicOperations;
import org.apache.logging.log4j.*;

/**
 *
 * @author ESOL
 */
public class OLGApropGenerator {

    private static final long serialVersionUID = 1000;
    static Logger logger = LogManager.getLogger(OLGApropGenerator.class);

    public static void main(String args[]) {
        SystemInterface testSystem = new SystemSrkEos(273.15, 10.0);
        testSystem.addComponent("nitrogen", 0.01848);
        testSystem.addComponent("CO2", 0.837478);
        testSystem.addComponent("methane", 2.135464);
        testSystem.addComponent("ethane", 0.6941);
        testSystem.addComponent("propane", 0.46402);
        testSystem.addComponent("i-butane", 0.302664);
        testSystem.addComponent("n-butane", 0.2696);
        testSystem.addComponent("i-pentane", 0.18108);
        testSystem.addComponent("n-pentane", 0.422286);
        testSystem.addTBPfraction("C6_PC", 0.01753, 86.178 / 1000.0, 0.66399);
        testSystem.addTBPfraction("C7_PC", 0.0231839, 96.0 / 1000.0, 0.738);
        testSystem.addTBPfraction("C8_PC", 0.006674, 107.0 / 1000.0, 0.8097);
        testSystem.addTBPfraction("C9_PC", 0.000660625, 120.99 / 1000.0, 0.8863);
        testSystem.addTBPfraction("C10_PC", 8.07355e-5, 144.178 / 1000.0, 0.8526);

        // testSystem.addComponent("water", 28.97100);
        // testSystem.addComponent("TEG",65.65524299);
        testSystem.createDatabase(true);
        testSystem.setMixingRule(2);

        testSystem.setMultiPhaseCheck(true);

        ThermodynamicOperations testOps = new ThermodynamicOperations(testSystem);
        try {
            // testSystem.setTemperature(380.0);
            // testSystem.setPressure(80.0);
            // testOps.TPflash();
            // testSystem.display();

            testSystem.setTemperature(273.15 + 20.85);
            testSystem.setPressure(13);
            testOps.TPflash();
            testSystem.display();

            String fileName = "c:/temp//OLGAneqsim.tab";
            testOps.OLGApropTable(273.15, 273.15 + 50.0, 40, 1.0, 220.0, 40, fileName, 0);
            testOps.displayResult();

        } catch (Exception e) {
            testSystem.display();
            logger.error(e.toString());
        }

    }
}

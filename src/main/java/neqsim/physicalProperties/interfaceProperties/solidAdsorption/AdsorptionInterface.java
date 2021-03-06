/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package neqsim.physicalProperties.interfaceProperties.solidAdsorption;

/**
 *
 * @author ESOL
 */
public interface AdsorptionInterface extends neqsim.thermo.ThermodynamicConstantsInterface {

    public void calcAdorption(int phase);

    public double getSurfaceExess(int component);

    public void setSolidMaterial(String solidM);

    public double getSurfaceExcess(String componentName);
}

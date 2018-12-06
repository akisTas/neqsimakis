/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neqsim.processSimulation.processEquipment.distillation;

import neqsim.processSimulation.processEquipment.ProcessEquipmentInterface;
import neqsim.processSimulation.processEquipment.stream.StreamInterface;

/**
 *
 * @author ESOL
 */
public interface TrayInterface extends ProcessEquipmentInterface {

    public void run();

    public void addStream(StreamInterface newStream);

    public void setName(String name);

    public String getName();

    public void setHeatInput(double heatinp);

    public void runTransient();
}

/*
 * Test.java
 *
 * Created on 22. januar 2001, 22:59
 */

package neqsim.thermo.util.parameterFitting.binaryInteractionParameterFitting.ionicInteractionCoefficientFitting;

import neqsim.statistics.parameterFitting.nonLinearParameterFitting.LevenbergMarquardtFunction;
import neqsim.thermo.phase.PhaseModifiedFurstElectrolyteEos;

/**
 *
 * @author  Even Solbraa
 * @version
 */
public class IonicInteractionParameterFittingFunctionCo2nacl extends LevenbergMarquardtFunction {

    private static final long serialVersionUID = 1000;
    
    /** Creates new Test */
    public IonicInteractionParameterFittingFunctionCo2nacl() {
    }
    
    public double calcValue(double[] dependentValues){
        try{
            thermoOps.TPflash();
       }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return system.getPhase(1).getComponent(0).getx()/(1.0-system.getPhase(1).getComponent(2).getx()-system.getPhase(1).getComponent(3).getx());
    }
    
    public double calcTrueValue(double val){
        return val;
    }
    
    public void setFittingParams(int i, double value){
        params[i] = value;
        int CO2Numb=0, Naplusnumb=0;
        int j=0;
        do{
            CO2Numb = j;
            j++;
        }
        while(!system.getPhases()[0].getComponents()[j-1].getComponentName().equals("CO2"));
        j=0;
        
        do{
            Naplusnumb = j;
            j++;
        }
        while(!system.getPhases()[0].getComponents()[j-1].getComponentName().equals(system.getPhases()[0].getComponents()[2].getComponentName()));
      
        if(i==0){
            ((PhaseModifiedFurstElectrolyteEos)system.getPhases()[0]).getElectrolyteMixingRule().setWijParameter(Naplusnumb,CO2Numb, value);
            ((PhaseModifiedFurstElectrolyteEos)system.getPhases()[1]).getElectrolyteMixingRule().setWijParameter(Naplusnumb,CO2Numb, value);
        }
        
         if(i==1){
            ((PhaseModifiedFurstElectrolyteEos)system.getPhases()[0]).getElectrolyteMixingRule().setWijT1Parameter(Naplusnumb,CO2Numb, value);
            ((PhaseModifiedFurstElectrolyteEos)system.getPhases()[1]).getElectrolyteMixingRule().setWijT1Parameter(Naplusnumb,CO2Numb, value);
        }
       
    }
    
    
}
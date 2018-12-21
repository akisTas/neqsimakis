/*
 * PhysicalProperties.java
 *
 * Created on 29. oktober 2000, 16:13
 */
package neqsim.physicalProperties.physicalPropertySystem;

import neqsim.thermo.ThermodynamicConstantsInterface;
import neqsim.thermo.phase.PhaseInterface;
import org.apache.log4j.Logger;

/**
 *
 * @author Even Solbraa
 * @version
 */
public abstract class PhysicalProperties extends java.lang.Object implements PhysicalPropertiesInterface, java.io.Serializable, ThermodynamicConstantsInterface, Cloneable {

    /**
     * @param mixingRule the mixingRule to set
     */
    public void setMixingRule(neqsim.physicalProperties.mixingRule.PhysicalPropertyMixingRuleInterface mixingRule) {
        this.mixingRule = mixingRule;
    }

    private static final long serialVersionUID = 1000;
    static Logger logger = Logger.getLogger(PhysicalProperties.class);

    public PhaseInterface phase;
    protected int binaryDiffusionCoefficientMethod;
    protected int multicomponentDiffusionMethod;
    private neqsim.physicalProperties.mixingRule.PhysicalPropertyMixingRuleInterface mixingRule = null;
    public neqsim.physicalProperties.physicalPropertyMethods.methodInterface.ConductivityInterface conductivityCalc;
    public neqsim.physicalProperties.physicalPropertyMethods.methodInterface.ViscosityInterface viscosityCalc;
    public neqsim.physicalProperties.physicalPropertyMethods.methodInterface.DiffusivityInterface diffusivityCalc;
    public neqsim.physicalProperties.physicalPropertyMethods.methodInterface.DensityInterface densityCalc;
    public double kinematicViscosity = 0, density = 0, viscosity = 0, conductivity = 0;
    private double[] waxViscosityParameter = {37.82, 83.96, 8.559e6};

    /**
     * Creates new PhysicalProperties
     */
    public PhysicalProperties() {
    }

    public PhysicalProperties(PhaseInterface phase, int binaryDiffusionCoefficientMethod, int multicomponentDiffusionMethod) {
        this.phase = phase;
        this.binaryDiffusionCoefficientMethod = binaryDiffusionCoefficientMethod;
        this.multicomponentDiffusionMethod = multicomponentDiffusionMethod;
    }

    public Object clone() {
        PhysicalProperties properties = null;

        try {
            properties = (PhysicalProperties) super.clone();
        } catch (Exception e) {
            logger.error("Cloning failed.", e);
        }
        properties.densityCalc = (neqsim.physicalProperties.physicalPropertyMethods.methodInterface.DensityInterface) densityCalc.clone();
        properties.diffusivityCalc = (neqsim.physicalProperties.physicalPropertyMethods.methodInterface.DiffusivityInterface) diffusivityCalc.clone();
        properties.viscosityCalc = (neqsim.physicalProperties.physicalPropertyMethods.methodInterface.ViscosityInterface) viscosityCalc.clone();
        properties.conductivityCalc = (neqsim.physicalProperties.physicalPropertyMethods.methodInterface.ConductivityInterface) conductivityCalc.clone();
        if(mixingRule!=null) properties.mixingRule = (neqsim.physicalProperties.mixingRule.PhysicalPropertyMixingRuleInterface) mixingRule.clone();
        return properties;
    }

    public PhaseInterface getPhase() {
        return phase;
    }

    public neqsim.physicalProperties.mixingRule.PhysicalPropertyMixingRuleInterface getMixingRule() {
        return mixingRule;
    }

    public void setMixingRuleNull() {
        setMixingRule(null);
    }

    public neqsim.physicalProperties.physicalPropertyMethods.methodInterface.ViscosityInterface getViscosityModel() {
        return viscosityCalc;
    }

    public void setConductivityModel(String model) {
        if ("PFCT".equals(model)) {
            conductivityCalc = new neqsim.physicalProperties.physicalPropertyMethods.commonPhasePhysicalProperties.conductivity.PFCTConductivityMethodMod86(this);
        } else if ("polynom".equals(model)) {
            conductivityCalc = new neqsim.physicalProperties.physicalPropertyMethods.liquidPhysicalProperties.conductivity.Conductivity(this);
        } else if ("Chung".equals(model)) {
            conductivityCalc = new neqsim.physicalProperties.physicalPropertyMethods.gasPhysicalProperties.conductivity.ChungConductivityMethod(this);
        } else {
            conductivityCalc = new neqsim.physicalProperties.physicalPropertyMethods.commonPhasePhysicalProperties.conductivity.PFCTConductivityMethodMod86(this);
        }

    }

    public void setViscosityModel(String model) {
        if ("polynom".equals(model)) {
            viscosityCalc = new neqsim.physicalProperties.physicalPropertyMethods.liquidPhysicalProperties.viscosity.Viscosity(this);
        } else if ("friction theory".equals(model)) {
            viscosityCalc = new neqsim.physicalProperties.physicalPropertyMethods.commonPhasePhysicalProperties.viscosity.FrictionTheoryViscosityMethod(this);
        } else if ("LBC".equals(model)) {
            viscosityCalc = new neqsim.physicalProperties.physicalPropertyMethods.commonPhasePhysicalProperties.viscosity.LBCViscosityMethod(this);
        } else if ("PFCT".equals(model)) {
            viscosityCalc = new neqsim.physicalProperties.physicalPropertyMethods.commonPhasePhysicalProperties.viscosity.PFCTViscosityMethodMod86(this);
        }
    }

    public neqsim.physicalProperties.physicalPropertyMethods.methodInterface.ConductivityInterface getConductivityModel() {
        return conductivityCalc;
    }

    public void setBinaryDiffusionCoefficientMethod(int i) {
        binaryDiffusionCoefficientMethod = i;
    }

    public void setMulticomponentDiffusionMethod(int i) {
        multicomponentDiffusionMethod = i;
    }

    public double calcKinematicViscosity() {
        kinematicViscosity = viscosity / phase.getDensity();
        return kinematicViscosity;
    }

    public void setPhases() {
        conductivityCalc.setPhase(this);
        densityCalc.setPhase(this);
        viscosityCalc.setPhase(this);
        diffusivityCalc.setPhase(this);
    }

    public void init(PhaseInterface phase) {
        this.phase = phase;
        this.setPhases();
        density = densityCalc.calcDensity();
        viscosity = viscosityCalc.calcViscosity();
        kinematicViscosity = this.calcKinematicViscosity();
        diffusivityCalc.calcDiffusionCoeffisients(binaryDiffusionCoefficientMethod, multicomponentDiffusionMethod);
        conductivity = conductivityCalc.calcConductivity();
    }

    public void init(PhaseInterface phase, String type) {
        if (type.equals("density")) {
            density = densityCalc.calcDensity();
        } else if (type.equals("viscosity")) {
            viscosity = viscosityCalc.calcViscosity();
        } else if (type.equals("conductivity")) {
            conductivity = conductivityCalc.calcConductivity();
        } else {
            init(phase);
        }
    }

    public double getViscosityOfWaxyOil(double waxVolumeFraction, double shareRate) {

        return viscosity * (Math.exp(waxViscosityParameter[0] * waxVolumeFraction) + waxViscosityParameter[1] * waxVolumeFraction / Math.sqrt(shareRate) + waxViscosityParameter[2] * Math.pow(waxVolumeFraction, 4.0) / shareRate);
    }

    public double getViscosity() {
        if (viscosity < 0) {
            return 1e-5;
        }
        return viscosity;
    }

    public double getPureComponentViscosity(int i) {
        return viscosityCalc.getPureComponentViscosity(i);
    }

    public double getConductivity() {
        if (conductivity < 0) {
            return 1e-5;
        }
        return conductivity;
    }

    public double getDensity() {
        return density;
    }

    public double calcDensity() {
        return densityCalc.calcDensity();
    }

    public double getKinematicViscosity() {
        if (kinematicViscosity < 0) {
            return 1e-5;
        }
        return kinematicViscosity;
    }

    public double getDiffusionCoeffisient(int i, int j) {
        return diffusivityCalc.getMaxwellStefanBinaryDiffusionCoefficient(i, j);
    }

    public double getFickDiffusionCoeffisient(int i, int j) {
        return diffusivityCalc.getFickBinaryDiffusionCoefficient(i, j);
    }

    public void calcEffectiveDiffusionCoefficients() {
        this.init(phase);
        diffusivityCalc.calcEffectiveDiffusionCoeffisients();
    }

    public double getEffectiveDiffusionCoefficient(int i) {
        return diffusivityCalc.getEffectiveDiffusionCoefficient(i);
    }

    public double getEffectiveSchmidtNumber(int i) {
        return getKinematicViscosity() / diffusivityCalc.getEffectiveDiffusionCoefficient(i);
    }

    /**
     * @return the waxViscosityParameter
     */
    public double[] getWaxViscosityParameter() {
        return waxViscosityParameter;
    }

    /**
     * @param waxViscosityParameter the waxViscosityParameter to set
     */
    public void setWaxViscosityParameter(double[] waxViscosityParameter) {
        this.waxViscosityParameter = waxViscosityParameter;
    }

    public void setWaxViscosityParameter(int paramNumber, double waxViscosityParameter) {
        this.waxViscosityParameter[paramNumber] = waxViscosityParameter;
    }
}

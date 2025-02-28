package org.droidrage.lib.template;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import org.droidrage.lib.encoder.EncoderEx;
import org.droidrage.lib.motor.CANMotorEx;
import org.droidrage.lib.motor.SparkMaxEx;
import org.droidrage.lib.template.SetPower.Control;

public class ArmAbsoluteTemplate extends ArmTemplate {
    protected EncoderEx encoder;
    public ArmAbsoluteTemplate(
        SparkMaxEx[] motors,
        PIDController controller,
        ArmFeedforward feedforward,
        TrapezoidProfile.Constraints constraints,
        double maxPosition,
        double minPosition,
        double offset,
        Control control,
        String subsystemName,
        int mainNum,
        EncoderEx encoder
    ){
        super(motors, controller, feedforward, constraints,
        maxPosition, minPosition, offset, control, 
        subsystemName, mainNum);
        this.encoder=encoder;

    }

    @Override
    public void periodic() {
        encoder.periodic();
        switch(control){
            case PID:
                setVoltage(controller.calculate(getEncoderPosition(), targetRadianWriter.get()));
                // setVoltage((controller.calculate(getEncoderPosition(), getTargetPosition())) + .37);
                //.37 is kG ^^
                break;
            case FEEDFORWARD:
                setVoltage(controller.calculate(getEncoderPosition(), targetRadianWriter.get())
                +feedforward.calculate(getEncoderPosition(),.1)); 
                // + feedforward.calculate(getTargetPosition(), .5)); 
                //ks * Math.signum(velocity) + kg + kv * velocity + ka * acceleration; ^^
                break;
            case TRAPEZOID_PROFILE:
                current = profile.calculate(0.02, current, goal);

                setVoltage(controller.calculate(getEncoderPosition(), current.position)
                        + feedforward.calculate(current.position, current.velocity));
                break;
        };   
    }

    @Override
    protected void setVoltage(double voltage) {
        // if (encoder.isConnectedWriter.get()){
            voltageWriter.set(voltage);
            for (CANMotorEx motor: motors) {
                motor.setVoltage(voltage);
                //IMPORTANT: This flips the voltage to work right. Might NEED to change
            }
        // }
    }
    
    // THIS WORKS
    // @Override 
    // public double getEncoderPosition() {
    //     double raw = encoder.getPosition();
    //     double radian = MathUtil.inputModulus((raw * (2 * Math.PI)) + offset, 0, (2 * Math.PI));
    //     positionRadianWriter.write(radian);
    //     positionDegreeWriter.write(Math.toDegrees(radian));
    //     return radian;
    // }

    // TEST THIS
    @Override
    public double getEncoderPosition() {
        double radian = encoder.getRadian(offset);
        positionRadianWriter.write(radian);
        positionDegreeWriter.write(Math.toDegrees(radian));
        return radian;
    }
    
}

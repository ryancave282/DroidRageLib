package org.droidrage.lib.template;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.droidrage.lib.motor.CANMotorEx;
import org.droidrage.lib.shuffleboard.ShuffleboardValue;

//Works
public class SetPower {
    private final CANMotorEx[] motors;
    private final ShuffleboardValue<Double> powerWriter;
    private final int mainNum;

    public SetPower(
        CANMotorEx[] motors,
        String name,
        int mainNum
    ){
        this.motors=motors;
        this.mainNum=mainNum;
        powerWriter = ShuffleboardValue
            .create(0.0, name+"/Power", name)
            .build();
    }

    public Command setTargetPowerCommand(double power){
        return new InstantCommand(()->setTargetPower(power));
    }

    /*
     * Use this for initialization
     */
    public void setTargetPower(double power) {
        powerWriter.set(power);
        for (CANMotorEx motor: motors) {
            motor.setPower(power);
        }
    }
   
    public CANMotorEx getMotor() {
        return motors[mainNum];
    }

    public CANMotorEx[] getAllMotor() {
        return motors;
    } 
}

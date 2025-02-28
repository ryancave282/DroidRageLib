package org.droidrage.lib.motor;

import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.RobotController;
import org.droidrage.lib.Misc;

public class TalonEx extends CANMotorEx {
    private final TalonFX talon;
    private CANBus canbus;
    private TalonFXConfiguration config;
    private TalonFXConfigurator configure;
    private Alert talonTempAlert = new Alert("Temperature Warning", AlertType.kWarning);
    private Alert canAlert = new Alert("CAN Fault", AlertType.kWarning);
    
    private TalonEx(TalonFX motor) {
        this.talon = motor;
        config = new TalonFXConfiguration(); // Use to change configs
        configure = talon.getConfigurator(); // Use to apply configs
    }

    public static DirectionBuilder create(int deviceID, CANBus canbus) {
        TalonEx motor = new TalonEx(new TalonFX(deviceID, canbus));
        motor.motorID = deviceID;
        motor.canbus = canbus;
        return motor.new DirectionBuilder();
    }
    
    public static DirectionBuilder create(int deviceID) {
        TalonEx motor = new TalonEx(new TalonFX(deviceID));
        motor.motorID = deviceID;
        return motor.new DirectionBuilder();
    }

    @Override
    public void setAlert() {
        canAlert.set(talon.getStickyFault_Hardware().getValue()); // TODO: Set to correct sticky fault
        talonTempAlert.set(talon.getStickyFault_DeviceTemp().getValue());
    }
   
    @Override
    public void setDirection(Direction direction) {
        config.MotorOutput.Inverted = switch (direction) {
            case Forward -> InvertedValue.Clockwise_Positive;
            case Reversed -> InvertedValue.CounterClockwise_Positive;
        };
        configure.apply(config);
    }

    @Override
    public void setIdleMode(ZeroPowerMode mode) {
        talon.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
    }

    @Override
    public void setSupplyCurrentLimit(double currentLimit) {
        config.CurrentLimits.SupplyCurrentLimit = currentLimit;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        configure.apply(config);
        // talonTempAlert = new Alert(subSystemName + motorID + "Temp", AlertType.kWarning);
        // talonTempAlert.set(false);
    }

    @Override
    public void setStatorCurrentLimit(double currentLimit){
        config.CurrentLimits.StatorCurrentLimit = currentLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = true;
        configure.apply(config);
    }

    @Override
    public void setPower(double power) {
        if (isEnabledWriter.get()) {
            talon.set(power);
        }
        if (Misc.removeWriterWriter.get()) {
            outputWriter.set(power);
        }
    }

    @Override
    public void setVoltage(double outputVolts) {
        if(isEnabledWriter.get()){
            talon.setVoltage(outputVolts);
        }
        if(Misc.removeWriterWriter.get()){//if(!DriverStation.isFMSAttached())
            outputWriter.set(outputVolts);
        }
    }

    @Override
    public void setVoltage(Voltage voltage) {
        talon.setVoltage(voltage.in(Volts)/RobotController.getBatteryVoltage());
    }
    
    public void setPosition(double position) {
        talon.setPosition(position);
    }

    // Already in rotations per sec so, just covert to
    @Override
    public double getVelocity() {
        return talon.getVelocity().getValueAsDouble()*positionConversionFactor;
    }

    @Override
    public double getSpeed() {
        return talon.get();
    }

    @Override
    public double getPosition() {
        return talon.getPosition().getValueAsDouble()*positionConversionFactor;
    }

    @Override
    public int getDeviceID() {
        return talon.getDeviceID();
    }
     
    public CANBus getCANBus() {
        return canbus;
    }

    @Override
    public double getVoltage(){
        return talon.getMotorVoltage().getValueAsDouble();
    }

    @Override
    public void resetEncoder(int num) {
        talon.setPosition(num);
    }
    public void testTemp(double tempToCheck, double lowerSupply, double lowerStator){

        if(talon.getDeviceTemp().getValueAsDouble() > tempToCheck){
            talonTempAlert.set(true);
            setSupplyCurrentLimit(config.CurrentLimits.SupplyCurrentLimit - lowerSupply);
            setStatorCurrentLimit(config.CurrentLimits.StatorCurrentLimit - lowerStator);
        }
        // return talon.getDeviceTemp().getValueAsDouble();
    }

    
}

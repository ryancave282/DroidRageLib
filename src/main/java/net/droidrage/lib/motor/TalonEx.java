package net.droidrage.lib.motor;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import net.droidrage.lib.DroidRageConstants;

import static edu.wpi.first.units.Units.Volts;

public class TalonEx extends CANMotorEx {
    private final TalonFX motor;
    private CANBus canbus;
    private TalonFXConfiguration configuration = new TalonFXConfiguration();
    private MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();

    private TalonEx(TalonFX motor) {
        this.motor = motor;
        // motor.getConfigurator().apply(configuration);
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
    public void setDirection(Direction direction) {
        motorOutputConfigs.Inverted = switch (direction) {
            case Forward -> InvertedValue.Clockwise_Positive;
            case Reversed -> InvertedValue.CounterClockwise_Positive;
        };
        motor.getConfigurator().apply(motorOutputConfigs);

    }

    @Override
    public void setIdleMode(ZeroPowerMode mode) {
        motor.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
    }

    @Override
    public void setSupplyCurrentLimit(double currentLimit) {
        configuration.CurrentLimits.SupplyCurrentLimit = currentLimit;
        configuration.CurrentLimits.SupplyCurrentLimitEnable = true;
        motor.getConfigurator().apply(configuration);
    }

    @Override
    public void setStatorCurrentLimit(double statorCurrent){
        // CurrentLimitsConfigs configs = new CurrentLimitsConfigs();
        // configs.StatorCurrentLimit = 50;
        // motor.getConfigurator().apply(configs);
        configuration.CurrentLimits.StatorCurrentLimit = statorCurrent;
        configuration.CurrentLimits.StatorCurrentLimitEnable = true;
        motor.getConfigurator().apply(configuration);
    }

    @Override
    public void setPower(double power) {
        if (isEnabledWriter.get()) {
            motor.set(power);
        }
        if (DroidRageConstants.removeWriterWriter.get()) {
            outputWriter.set(power);
        }
    }

    @Override
    public void setVoltage(double outputVolts) {
        if(isEnabledWriter.get()){
            motor.setVoltage(outputVolts);
        }
        if(DroidRageConstants.removeWriterWriter.get()){//if(!DriverStation.isFMSAttached())
            outputWriter.set(outputVolts);
        }
    }

    @Override
    public void setVoltage(Voltage voltage) {
        motor.setVoltage(voltage.in(Volts)/ RobotController.getBatteryVoltage());
    }

    public void setPosition(double position) {
        motor.setPosition(position);
    }

    //Already in rotations per sec so, just covert to
    @Override
    public double getVelocity() {
        return motor.getVelocity().getValueAsDouble()*positionConversionFactor;
    }

    @Override
    public double getSpeed() {
        return motor.get();
    }

    @Override
    public double getPosition() {
        return motor.getPosition().getValueAsDouble()*positionConversionFactor;
    }

    @Override
    public int getDeviceID() {
        return motor.getDeviceID();
    }

    public CANBus getCANBus() {
        return canbus;
    }

    @Override
    public double getVoltage(){
        return motor.getMotorVoltage().getValueAsDouble();
    }

    @Override
    public void resetEncoder(int num) {
        motor.setPosition(num);
    }
}

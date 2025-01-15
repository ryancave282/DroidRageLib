package net.droidrage.lib.encoder;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;

public class CANcoderEx extends EncoderEx {
    private final CANcoder encoder;
    private final CANcoderConfiguration config = new CANcoderConfiguration();
    private double positionConversionFactor, velocityConversionFactor;

    public CANcoderEx(CANcoder encoder) {
        this.encoder = encoder;
    }

    public double getVelocity() {
        return encoder.getVelocity().getValueAsDouble()*velocityConversionFactor;
    }
        
    public double getPosition() {
        return encoder.getPosition().getValueAsDouble()*positionConversionFactor;
    }

    public static DirectionBuilder create(int deviceID) {
        CANcoderEx encoder = new CANcoderEx(new CANcoder(deviceID));
        encoder.deviceID = deviceID;
        return encoder.new DirectionBuilder();
    }

    @Override
    public void setDirection(EncoderDirection direction) {
        switch (direction) {
            case Reversed:
                config.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
            case Forward:
                config.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        }
    }

    public void setRange(EncoderRange range) {
        switch (range) {
            case PLUS_MINUS_HALF:
                config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;
            case ZERO_TO_ONE:
                config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 1;
        }
    }

    public void setMagnetSensorOffset(double offset) {
        config.MagnetSensor.MagnetOffset = offset;
    }

    public double getAbsolutePosition() {
        return encoder.getAbsolutePosition().getValueAsDouble();
    }

    @Override
    public int getDeviceID() {
        return encoder.getDeviceID();
    }

    @Override
    public void setOffset(double offset) {
        config.MagnetSensor.MagnetOffset = offset;
    }
}



package org.droidrage.lib.encoder;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.config.EncoderConfig;

import org.droidrage.lib.motor.SparkMaxEx;
import org.droidrage.lib.motor.SparkMaxEx.EncoderType;
import lombok.Setter;

public class SparkAbsoluteEncoderEx extends EncoderEx {
    protected final AbsoluteEncoder encoder;
    protected final SparkMaxEx motor;
    private final EncoderConfig config = new EncoderConfig();
    @Setter(onMethod = @__(@Override)) private double offset; // No use; offset calculations in EncoderEx.java
    @Setter(onMethod = @__(@Override)) private EncoderRange range; // No use; here for compatibility
    
    private SparkAbsoluteEncoderEx(AbsoluteEncoder encoder, SparkMaxEx motor) {
        this.encoder = encoder;
        this.motor = motor;
    }

    public static DirectionBuilder create(SparkMaxEx motor) {
        SparkAbsoluteEncoderEx encoder = new SparkAbsoluteEncoderEx(motor.getEncoder(EncoderType.Absolute), motor);
        return encoder.new DirectionBuilder();
    }

    @Override
    public double getPosition() { //Raw Position
        return encoder.getPosition();
    }

    @Override
    public double getDegree() {
        return getPosition()*(360);
    }

    @Override
    public double getRadian() {
        return getPosition()*(2*Math.PI);
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();  
    }

    @Override
    public void setDirection(EncoderDirection direction) {
        switch (direction) {
            case Reversed -> config.inverted(true);
            case Forward -> config.inverted(false);
        }
    }

    @Override
    public int getDeviceID() {
        return motor.getDeviceID();
    }
}

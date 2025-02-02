package net.droidrage.lib.encoder;

import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.config.EncoderConfig;
import net.droidrage.lib.motor.SparkMaxEx;

public class SparkAbsoluteEncoderEx extends EncoderEx {
    protected final SparkAbsoluteEncoder encoder;
    protected final EncoderConfig config = new EncoderConfig();
    
    private SparkAbsoluteEncoderEx(SparkAbsoluteEncoder encoder) {
        this.encoder = encoder;
    }

    public static DirectionBuilder create(SparkMaxEx motor) {
        SparkAbsoluteEncoderEx encoder = new SparkAbsoluteEncoderEx(motor.getAbsoluteEncoder());
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
        return 0;
    }

    @Override
    public void setRange(EncoderRange range) {
        // DOES NOTHING, but it is here for compatibility
    }

    @Override
    public void setOffset(double offset) {
        // DOES NOTHING, but it is here for compatibility
    }
}

package net.droidrage.lib.encoder;

import net.droidrage.lib.shuffleboard.ShuffleboardValue;

public abstract class EncoderEx {
    public enum EncoderRange {
        ZERO_TO_ONE,    //Typically ALWAYS uses 0 to 1
        PLUS_MINUS_HALF
    }

    public enum EncoderDirection {
        Forward,
        Reversed,
    }

    protected EncoderDirection direction;
    protected double positionConversionFactor;
    protected double velocityConversionFactor;
    public ShuffleboardValue<Double> degreeWriter;
    public ShuffleboardValue<Double> radianWriter;
    public ShuffleboardValue<Double> rawWriter;
    public ShuffleboardValue<Boolean> isConnectedWriter;
    public String name;
    protected String subSystemName;
    public int deviceID;
    

    public class DirectionBuilder {
        public SubsystemNameBuilder withDirection(EncoderDirection direction) {
            setDirection(direction);
            return new SubsystemNameBuilder();
        }
    }

    public class SubsystemNameBuilder {
        @SuppressWarnings("unchecked")
        public <T extends EncoderEx> T withSubsystemBase(String subsystemBaseName) {
            name = subsystemBaseName;
            rawWriter = ShuffleboardValue
                    .create(0.0, name + "/Pos/Raw", name)
                    .withSize(1, 2)
                    .build();
            degreeWriter = ShuffleboardValue
                    .create(0.0, name + "/Pos/Degree", name)
                    .withSize(1, 2)
                    .build();
            radianWriter = ShuffleboardValue
                    .create(0.0, name + "/Pos/Radian", name)
                    .withSize(1, 2)
                    .build();
            return (T) EncoderEx.this;
        }
    }

    public class RangeBuilder {
        @SuppressWarnings("unchecked")
        public <T extends EncoderEx> T withRange(EncoderRange range) {
            setRange(range);
            return (T) EncoderEx.this;
        }
    }
    public CANcoderEx withRange(EncoderRange range){
        setRange(range);
        return (CANcoderEx) this;
    }
    
    public CANcoderEx withOffset(double offset) {
        setOffset(offset);
        return (CANcoderEx) this;
    }

    public void periodic() {
        rawWriter.set(getPosition());
        degreeWriter.set(getDegree());
        radianWriter.set(getRadian());
    }
    public abstract double getVelocity();
    public abstract double getPosition();
    public double getDegree() {
        return getPosition() * 360;
    }
    
    public double getRadian() {
        return getPosition() * (2*Math.PI);
    }
    public abstract void setDirection(EncoderDirection direction);
    public abstract int getDeviceID();
    public abstract void setRange(EncoderRange range);
    public abstract void setOffset(double offset);
}

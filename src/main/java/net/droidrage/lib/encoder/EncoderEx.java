package net.droidrage.lib.encoder;

import net.droidrage.lib.shuffleboard.ShuffleboardValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
    public String subsystemName;
    protected String subSystemName;
    public int deviceID;
    

    public class DirectionBuilder {
        public OffsetBuilder withDirection(EncoderDirection direction) {
            setDirection(direction);
            return new OffsetBuilder();
        }
    }

    public class OffsetBuilder {
        public SubsystemNameBuilder withOffset(double offset) {
            setOffset(offset);
            return new SubsystemNameBuilder();
        }
    }

    public class SubsystemNameBuilder {

        public <T extends EncoderEx> T withSubsystemBase(String encoderName, SubsystemBase subsystemBase) {
            return withSubsystemBase(encoderName, subsystemBase.getClass().getSimpleName());
        }

        @SuppressWarnings("unchecked")
        public <T extends EncoderEx> T withSubsystemBase(String encoderName, String subsystemBase) {
            subsystemName = subsystemBase;
            rawWriter = ShuffleboardValue
                    .create(0.0, subsystemName + "/" + encoderName + "/Pos/Raw", subsystemName)
                    .withSize(1, 2)
                    .build();
            degreeWriter = ShuffleboardValue
                    .create(0.0, subsystemName + "/" + encoderName + "/Pos/Degree", subsystemName)
                    .withSize(1, 2)
                    .build();
            radianWriter = ShuffleboardValue
                    .create(0.0, subsystemName + "/" + encoderName + "/Pos/Radian", subsystemName)
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

    public void periodic() {
        rawWriter.set(getPosition());
        degreeWriter.set(getDegree());
        radianWriter.set(getRadian());
    }
    public abstract double getVelocity();
    public abstract double getPosition();
    public abstract double getDegree();
    public abstract double getRadian();
    public abstract void setDirection(EncoderDirection direction);
    public abstract int getDeviceID();
    public abstract void setRange(EncoderRange range);
    public abstract void setOffset(double offset);
}

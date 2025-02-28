package org.droidrage.lib;

import org.droidrage.lib.shuffleboard.ShuffleboardValue;

public class Misc {
    public static ShuffleboardValue<Boolean> removeWriterWriter = 
        ShuffleboardValue.create(true, "RemoveWritersWriter", "Robot")
        .withSize(1, 3)
        .build();

    public enum Control{
        PID,
        FEEDFORWARD,
        TRAPEZOID_PROFILE
    }
}
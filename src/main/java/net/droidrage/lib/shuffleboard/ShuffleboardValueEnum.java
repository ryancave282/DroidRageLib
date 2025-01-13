package net.droidrage.lib.shuffleboard;

public interface ShuffleboardValueEnum<T> {
    // public final ShuffleboardValue<T> shuffleboardValue;
    public ShuffleboardValue<T> getNum();
    public default T get() {
        return getNum().get();
    }
    public default void set(T value) {
        getNum().set(value);
    }
}
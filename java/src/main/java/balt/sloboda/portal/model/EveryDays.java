package balt.sloboda.portal.model;

public enum EveryDays {
    first(1),
    second( 2),
    third(3),
    fourth(4),
    last (-1),
    ;

    private final int value;
    EveryDays(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

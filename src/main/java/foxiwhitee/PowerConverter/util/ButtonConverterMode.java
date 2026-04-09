package foxiwhitee.PowerConverter.util;

public enum ButtonConverterMode {
    EU(304, 14, 304, 28),
    RF(304, 28, 304, 0);

    private final int euX, euY, rfX, rfY;

    ButtonConverterMode(int euX, int euY, int rfX, int rfY) {
        this.euX = euX;
        this.euY = euY;
        this.rfX = rfX;
        this.rfY = rfY;
    }

    public int getEuX() {
        return euX;
    }

    public int getEuY() {
        return euY;
    }

    public int getRfX() {
        return rfX;
    }

    public int getRfY() {
        return rfY;
    }
}

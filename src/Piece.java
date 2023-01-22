public abstract class Piece {
    private boolean white = false;
    public Piece(boolean white) {
        this.white = white;
    }
    public boolean isWhite() {
        return white;
    }
    public void setWhite(boolean white) {
        this.white = white;
    }
    public abstract boolean isValidMove(Board board, Spot start, Spot end);
}
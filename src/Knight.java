public class Knight extends Piece {
    public Knight(boolean white) {
        super(white);
    }
    @Override
    public boolean isValidMove(Board board, Spot start, Spot end) {
        // if same square
        if (start.equalsIgnorePiece(end)) {
            return false;
        }
        int dx = Math.abs(end.getX() - start.getX());
        int dy = Math.abs(end.getY() - start.getY());
        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            if (end.getPiece() == null || end.getPiece().isWhite() != start.getPiece().isWhite()) {
                return board.kingNotInCheck(start, end, start.getPiece().isWhite());
            }
        }
        return false;
    }
}
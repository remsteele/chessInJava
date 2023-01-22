public class Queen extends Piece {
    public Queen(boolean white) {
        super(white);
    }
    @Override
    public boolean isValidMove(Board board, Spot start, Spot end) {
        // if same square
        if (start.equalsIgnorePiece(end)) {
            return false;
        }
        // if move is valid for rook
        if (end.getX() == start.getX() || end.getY() == start.getY()) {
            // if path to piece blocked
            if (end.getX() == start.getX()) {
                int y = end.getY() > start.getY() ? 1 : -1;
                for (int i = 1; i < Math.abs(end.getY() - start.getY()); i++) {
                    if (board.getSpot(start.getX(), start.getY() + i * y).getPiece() != null) {
                        return false;
                    }
                }
            } else if (end.getY() == start.getY()) {
                int x = end.getX() > start.getX() ? 1 : -1;
                for (int i = 1; i < Math.abs(end.getX() - start.getX()); i++) {
                    if (board.getSpot(start.getX() + i * x, start.getY()).getPiece() != null) {
                        return false;
                    }
                }
            }
        }
        // if move is valid for bishop
        else if (Math.abs(end.getX() - start.getX()) == Math.abs(end.getY() - start.getY())) {
            int x = end.getX() > start.getX() ? 1 : -1;
            int y = end.getY() > start.getY() ? 1 : -1;
            for (int i = 1; i < Math.abs(end.getX() - start.getX()); i++) {
                if (board.getSpot(start.getX() + i * x, start.getY() + i * y).getPiece() != null) {
                    return false;
                }
            }
        }
        // if move is not valid for a rook or bishop
        else {
            return false;
        }
        // if end piece is same color
        if (end.getPiece() != null && end.getPiece().isWhite() == start.getPiece().isWhite()) {
            return false;
        }
        // if king not in check return true
        return board.kingNotInCheck(start, end, start.getPiece().isWhite());
    }
}
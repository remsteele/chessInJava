public class Pawn extends Piece {
    public Pawn(boolean white) {
        super(white);
    }
    @Override
    public boolean isValidMove(Board board, Spot start, Spot end) {
        // if same square
        if (start.equalsIgnorePiece(end)) {
            return false;
        }
        // white
        if (start.getPiece().isWhite()) {
            // if hasn't moved
            if (start.getX() == 6){
                if (end.equalsIgnorePiece(start.getX() - 1, start.getY()) ||
                        end.equalsIgnorePiece(start.getX() - 2, start.getY())) {
                    if (end.getX() == 4) {
                        if (board.getSpot(start.getX() - 1, start.getY()).getPiece() == null &&
                                board.getSpot(start.getX() - 2, start.getY()).getPiece() == null) {
                            return board.kingNotInCheck(start, end, start.getPiece().isWhite());
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
            // if has moved
            if (end.equalsIgnorePiece(start.getX() - 1, start.getY()) ) {
                if (board.getSpot(start.getX() - 1, start.getY()).getPiece() == null) {
                    return board.kingNotInCheck(start, end, start.getPiece().isWhite());
                }
                else {
                    return false;
                }
            }
            // check for takes
            if (end.equalsIgnorePiece(start.getX() - 1, start.getY() - 1) ||
                    end.equalsIgnorePiece(start.getX() - 1, start.getY() + 1)) {
                if (end.getPiece() != null) {
                    if (end.getPiece().isWhite() != start.getPiece().isWhite()) {
                        return board.kingNotInCheck(start, end, start.getPiece().isWhite());
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        // black
        else {
            // if hasn't moved
            if (start.getX() == 1) {
                // if one or two below
                if (end.equalsIgnorePiece(start.getX() + 1, start.getY()) ||
                        end.equalsIgnorePiece(start.getX() + 2, start.getY())) {
                    if (end.getX() == 3) {
                        if (board.getSpot(start.getX() + 1, start.getY()).getPiece() == null &&
                                board.getSpot(start.getX() + 2, start.getY()).getPiece() == null) {
                            return board.kingNotInCheck(start, end, start.getPiece().isWhite());
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
            // if has moved
            if (end.equalsIgnorePiece(start.getX() + 1, start.getY()) ) {
                if (board.getSpot(start.getX() + 1, start.getY()).getPiece() == null) {
                    return board.kingNotInCheck(start, end, start.getPiece().isWhite());
                }
                else {
                    return false;
                }
            }
            // check for takes
            if (end.equalsIgnorePiece(start.getX() + 1, start.getY() - 1) ||
                    end.equalsIgnorePiece(start.getX() + 1, start.getY() + 1)) {
                if (end.getPiece() != null) {
                    if (end.getPiece().isWhite() != start.getPiece().isWhite()) {
                        return board.kingNotInCheck(start, end, start.getPiece().isWhite());
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        // check en passant
        boolean isWhite = start.getPiece().isWhite();
        int x = start.getX();
        int y = start.getY();
        boolean isPawn = false;
        if (board.getLastMove() != null) {
            isPawn = board.getLastMove().getStart().getPiece() instanceof Pawn;
        }
        if ((isWhite && x == 3) || (!isWhite && x == 4)) {
            int[] directions = {-1, 1};
            for (int dir : directions) {
                if (end.equalsIgnorePiece(x + (isWhite ? -1 : 1), y + dir)) {
                    if (board.getLastMove().equalsIgnorePiece(x + (isWhite ? -2 : 2), y + dir, x, y + dir) &&
                            (isWhite != board.getSpot(x, y + dir).getPiece().isWhite()) && isPawn) {
                        boolean color = start.getPiece().isWhite();
                        if (board.kingNotInCheck(start, end, color)) {
                            return true;
                        }
                        else {
                            board.getSpot(x, y + dir).setPiece(new Pawn(!color));
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}
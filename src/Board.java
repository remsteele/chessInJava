import java.util.ArrayList;

public class Board {
    private Spot[][] squares = new Spot[8][8];
    private Spot whiteKing;
    private Spot blackKing;
    private MoveList moveHistory = new MoveList();
    private SpotList whitePieces = new SpotList();
    private SpotList blackPieces = new SpotList();
    private boolean turnWhite = true;

    public Board() {
        // starting fen: rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
        this.setFenPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }
    public Board(Spot[][] squares, Spot whiteKing, Spot blackKing, MoveList moveHistory,
                 SpotList whitePieces, SpotList blackPieces, boolean turnWhite) {
        this.squares = squares;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.moveHistory = moveHistory;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.turnWhite = turnWhite;
    }
    public void addPiece(Spot spot) {
        if (spot.getPiece() == null) {
            System.out.println("NULL ERROR: SPOT NOT ADDED TO LIST");
        }
        else if (spot.getPiece().isWhite()) {
            whitePieces.add(spot);
        }
        else {
            blackPieces.add(spot);
        }
    }
//    public static int countMoves(Board board, int depth, boolean white, int sum) {
//        Board newBoard = board;
//        board.setMove(newBoard.validMoves(white).get(0).getStart().clone(), newBoard.validMoves(white).get(0).getEnd().clone());
////        if (depth < 1) return newBoard.validMoves(white).size();
////        for (Move move : newBoard.validMoves(white)) {
////            newBoard.setMove(move.getStart(), move.getEnd());
////            newBoard.setLastMove(move);
////            newBoard.setMoveHistory(move);
////            sum += countMoves(newBoard, depth, !white, sum);
////        }
//        return sum;
//    }

    public void setMove(Spot start, Spot end) {
        Piece promotesTo = null;
        if (start.getPiece() instanceof Pawn
                && (end.getX() == 7 || end.getX() == 0)) {
            promotesTo = new Queen(start.getPiece().isWhite());
        }
        // if en passant
        int enPassant = 0;
        Spot pieceTaken;
        if (start.getPiece() instanceof Pawn &&
                end.getPiece() == null) {
            enPassant = end.getY() - start.getY();
        }
        if (enPassant == 0) {
            this.setLastMove(start.clone(), end.clone(), end.clone(), promotesTo);
            this.setMoveHistory(new Move(start.clone(), end.clone(), end.clone(), promotesTo));
        }
        else {
            this.setLastMove(start.clone(), end.clone(), new Spot(start.getX(), start.getY() + enPassant, this.getSpot(start.getX(), start.getY() + enPassant).getPiece()), promotesTo);
            this.setMoveHistory(new Move(start.clone(), end.clone(), new Spot(start.getX(), start.getY() + enPassant, this.getSpot(start.getX(), start.getY() + enPassant).getPiece()), promotesTo));
            this.remove(start.getX(), start.getY() + enPassant);
        }
        // if pawn on last rank -> promote
        if (start.getPiece() instanceof Pawn
                && (end.getX() == 7 || (end.getX()) == 0)) {
            end.setPiece(new Queen(start.getPiece().isWhite()));
        }
        // else move normally
        else {
            end.setPiece(start.getPiece());
        }
    }

    public void setMoveHistory(Move move) {
        if (move.getStart().getPiece().isWhite()) {
            whitePieces.remove(move.getStart());
            if (move.getTaken() != null) {
                blackPieces.remove(move.getTaken());
            }
            whitePieces.add(new Spot(move.getEnd().getX(), move.getEnd().getY(), move.getStart().getPiece()));
        }
        else {
            blackPieces.remove(move.getStart());
            if (move.getTaken() != null) {
                whitePieces.remove(move.getTaken());
            }
            blackPieces.add(new Spot(move.getEnd().getX(), move.getEnd().getY(), move.getStart().getPiece()));
        }
    }
    public int getMovesAmt() {
        return moveHistory.size();
    }
    public void remove(int x, int y) {
        squares[x][y].setPiece(null);
    }
    public Spot getWhiteKing() {
        return whiteKing;
    }
    public void setWhiteKing(Spot whiteKing) {
        this.whiteKing = whiteKing;
    }
    public Spot getBlackKing() {
        return blackKing;
    }
    public void setBlackKing(Spot blackKing) {
        this.blackKing = blackKing;
    }
    public Move getLastMove() {
        if (moveHistory.size() == 0) return null;
        else return moveHistory.get(moveHistory.size() - 1);
    }
    public void setLastMove(Move move) {
        moveHistory.add(move);
    }
    public void setLastMove(Spot start, Spot end, Spot taken, Piece promotesTo) {
        moveHistory.add(new Move(start, end, taken, promotesTo));
    }
    public boolean getTurnWhite() {
        return turnWhite;
    }
    public void setTurnWhite(boolean turnWhite) {
        this.turnWhite = turnWhite;
    }
    public Spot getSpot(int x, int y) {
        if (x > 7 || x < 0 || y > 7 || y < 0) {
            System.out.println("SPOT OUT OF BOUNDS ERROR");
            return null;
        }
        return squares[x][y];
    }
    public MoveList validMoves(boolean white) {
        MoveList validMoves = new MoveList();
        for (Spot spot : (white ? whitePieces : blackPieces)) {
            if (spot.getPiece() == null) System.out.println("NULL ERROR: CANNOT CHECK VALIDITY OF NULL PIECE");
            int[][] k_directions = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, 2}, {0, -2}};
            int[][] q_directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            int[][] r_directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            int[][] b_directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            int[][] n_directions = {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
            int[][] wp_directions = {{-1, 0}, {-2, 0}, {-1, 1}, {-1, -1}};
            int[][] bp_directions = {{1, 0}, {2, 0}, {1, 1}, {1, -1}};
            int[][] directions;
            if (spot.getPiece() instanceof King) directions = k_directions;
            else if (spot.getPiece() instanceof Queen) directions = q_directions;
            else if (spot.getPiece() instanceof Rook) directions = r_directions;
            else if (spot.getPiece() instanceof Bishop) directions = b_directions;
            else if (spot.getPiece() instanceof Knight) directions = n_directions;
            else directions = white ? wp_directions : bp_directions;
            int counting = ((spot.getPiece() instanceof King
                    || spot.getPiece() instanceof Knight
                    || spot.getPiece() instanceof Pawn) ? 2 : 8);
            for (int[] dir : directions) {
                for (int i = 1; i < counting; i++) {
                    int x = spot.getX() + dir[0] * i;
                    int y = spot.getY() + dir[1] * i;
                    if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                        if (spot.getPiece().isValidMove(this, spot, this.getSpot(x, y))) {
//                            System.out.println("From " + spot + " to " + this.getSpot(x, y));
                            // fix for taken and promotion
                            validMoves.add(new Move(spot, this.getSpot(x, y), null, null));
                        }
                    }
                }
            }
        }
        return validMoves;
    }
    public boolean kingNotInCheck(Spot start, Spot end, boolean white) {
        // if king change king location
        boolean isKing = false;
        if (start.getPiece() instanceof King) {
            isKing = true;
            if (white) {
                this.setWhiteKing(end);
            }
            else {
                this.setBlackKing(end);
            }
        }
        // make the move on the board
        Piece savePiece = end.getPiece();
        end.setPiece(start.getPiece());
        start.setPiece(null);
        Spot king = white ? whiteKing : blackKing;
        boolean inCheck = false;
        // for queen, rook, and bishop
        int[][] qrb_directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] dir : qrb_directions) {
            for (int i = 1; i < 8; i++) {
                int x = king.getX() + dir[0] * i;
                int y = king.getY() + dir[1] * i;
                if (x < 0 || x > 7 || y < 0 || y > 7) break;
                // if not empty square
                if (this.getSpot(x, y).getPiece() != null) {
                    // if straight
                    if (dir[0] == 0 || dir[1] == 0) {
                        // if rook or queen
                        if (this.getSpot(x, y).getPiece() instanceof Rook ||
                                this.getSpot(x, y).getPiece() instanceof Queen) {
                            if (this.getSpot(x, y).getPiece().isWhite() != white) {
                                inCheck = true;
                            }
                        }
                        break;
                    }
                    // if diagonal
                    else {
                        // if bishop or queen
                        if (this.getSpot(x, y).getPiece() instanceof Bishop ||
                                this.getSpot(x, y).getPiece() instanceof Queen) {
                            if (this.getSpot(x, y).getPiece().isWhite() != white) {
                                inCheck = true;
                            }
                        }
                        break;
                    }

                }
            }
            if (inCheck) break;
        }
        // for knight
        if (!inCheck) {
            int[][] n_directions = {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
            for (int[] dir : n_directions) {
                int x = king.getX() + dir[0];
                int y = king.getY() + dir[1];
                if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                    if (this.getSpot(x, y).getPiece() != null) {
                        if (this.getSpot(x, y).getPiece() instanceof Knight) {
                            if (this.getSpot(x, y).getPiece().isWhite() != white) {
                                inCheck = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        // for pawn
        if (!inCheck) {
            int x, y;
            if (white) {
                x = king.getX() - 1;
                y = king.getY() - 1;
                if (x >= 0 && y >= 0 && this.getSpot(x, y).getPiece() instanceof Pawn) {
                    if (this.getSpot(x, y).getPiece().isWhite() != white) {
                        inCheck = true;
                    }
                }
                x = king.getX() - 1;
                y = king.getY() + 1;
                if (x >= 0 && y <= 7 && this.getSpot(x, y).getPiece() instanceof Pawn) {
                    if (this.getSpot(x, y).getPiece().isWhite() != white) {
                        inCheck = true;
                    }
                }
            }
            else {
                x = king.getX() + 1;
                y = king.getY() - 1;
                if (x <= 7 && y >= 0 && this.getSpot(x, y).getPiece() instanceof Pawn) {
                    if (this.getSpot(x, y).getPiece().isWhite() != white) {
                        inCheck = true;
                    }
                }
                x = king.getX() + 1;
                y = king.getY() + 1;
                if (x <= 7 && y <= 7 && this.getSpot(x, y).getPiece() instanceof Pawn) {
                    if (this.getSpot(x, y).getPiece().isWhite() != white) {
                        inCheck = true;
                    }
                }
            }
        }
        // for king
        if (!inCheck) {
            int[][] k_directions = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
            for (int[] dir : k_directions) {
                int x = king.getX() + dir[0];
                int y = king.getY() + dir[1];
                if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                    if (this.getSpot(x, y).getPiece() != null) {
                        if (this.getSpot(x, y).getPiece() instanceof King) {
                            if (this.getSpot(x, y).getPiece().isWhite() != white) {
                                inCheck = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        // undo the move on the board
        start.setPiece(end.getPiece());
        end.setPiece(savePiece);
        if (isKing) {
            if (white) {
                this.setWhiteKing(start);
            }
            else {
                this.setBlackKing(start);
            }
        }
        return !inCheck;
    }
    public void setFenPosition(String fen) {
        int x = 0;
        int y = 0;
//        SpotList spots = new SpotList();
        char[] fenArr = fen.toCharArray();
        for (char c : fenArr) {
            if (c == '/') {
                x++;
                y = 0;
            }
            else if (Character.isDigit(c)) {
                y += Character.getNumericValue(c);
            }
            else {
                Piece piece = symbolToPiece(c);
                if (piece.isWhite()) {
                    whitePieces.add(new Spot(x, y, piece));
                }
                else {
                    blackPieces.add(new Spot(x, y, piece));
                }
                y++;
            }
        }
        for (int tx = 0; tx < 8; tx++) {
            for (int ty = 0; ty < 8; ty++) {
                SpotList spots;
                int index;
                int whiteIndex = whitePieces.contains(tx, ty);
                int blackIndex = blackPieces.contains(tx, ty);

                if (whiteIndex == -1 && blackIndex == -1) {
                    squares[tx][ty] = new Spot(tx, ty, null);
                }
                else {
                    if (whiteIndex > blackIndex) {
                        index = whiteIndex;
                        spots = whitePieces;
                    }
                    else {
                        index = blackIndex;
                        spots = blackPieces;
                    }
                    squares[tx][ty] = spots.get(index);
                    if (spots.get(index).getPiece() instanceof King) {
                        if (spots.get(index).getPiece().isWhite()) {
                            whiteKing = spots.get(index);
                        }
                        else {
                            blackKing = spots.get(index);
                        }
                    }
                }
            }
        }
    }
    private static Piece symbolToPiece(char c) {
        boolean white = Character.isUpperCase(c);
        if (Character.toLowerCase(c) == 'k') {
            return new King(white);
        }
        else if (Character.toLowerCase(c) == 'q') {
            return new Queen(white);
        }
        else if (Character.toLowerCase(c) == 'r') {
            return new Rook(white);
        }
        else if (Character.toLowerCase(c) == 'b') {
            return new Bishop(white);
        }
        else if (Character.toLowerCase(c) == 'n') {
            return new Knight(white);
        }
        else {
            return new Pawn(white);
        }
    }
}
class SpotList extends ArrayList<Spot> {
    public int contains(int x, int y) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getX() == x && this.get(i).getY() == y) {
                return i;
            }
        }
        return -1;
    }
    public void remove(Spot spot) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getX() == spot.getX() && this.get(i).getY() == spot.getY()) {
                this.remove(i);
            }
        }
    }
    @Override
    public SpotList clone() {
        SpotList temp = new SpotList();
        temp.addAll(this);
        return temp;
    }
}
class MoveList extends ArrayList<Move> {
    public boolean contains() {
        return false;
    }
    @Override
    public MoveList clone() {
        MoveList temp = new MoveList();
        temp.addAll(this);
        return temp;
    }
}

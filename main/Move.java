package main;

import pieces.Piece;

import java.util.Optional;

public class Move {
    /* Positions (source, destination) are encoded in coordinate notation
     as strings (i.e. "e1", "f6", "a4" etc.) */
    private Optional<String> source;
    private Optional<String> destination;

    /* main.Piece to promote a pawn advancing to last row, or
    *  piece to drop-in (from captured assets) */
    private final Optional<PieceType> replacement;

    private boolean enablesEnPassant;

    public void setEnablesEnPassant(boolean enablesEnPassant) {
        this.enablesEnPassant = enablesEnPassant;
    }

    /*
      Use the following 4 constructors for Pieces.main.Move:
      moveTo(src, dst), if emitting a standard move (advance, capture, castle)
      promote(src, dst, replace), if advancing a pawn to last row
      dropIn(dst, replace), if placing a captured piece
      resign(), if you want to resign
     */

    private Move(String source, String destination, PieceType replacement) {
        this.source = Optional.ofNullable(source);
        this.destination = Optional.ofNullable(destination);
        this.replacement = Optional.ofNullable(replacement);
    }

    public Optional<String> getSource() {
        return source;
    }

    public Optional<String> getDestination() {
        return destination;
    }

    public Optional<PieceType> getReplacement() {
        return replacement;
    }

    public int getSourceX() {
        return this.source.get().charAt(1) - '0';
    }

    public int getSourceY() {
        return this.source.get().charAt(0) - 'a' + 1;
    }

    public int getDestinationX() {
        return this.destination.get().charAt(1) - '0';
    }

    public int getDestinationY() {
        return this.destination.get().charAt(0) - 'a' + 1;
    }

    /**
        Translates a move from opponent's perspective to engine's perspective
     */
    private String translatePosition(String pos) {
        int row = pos.charAt(1) - '0';
        int col = pos.charAt(0) - 'a';
        int translatedRow = 9 - row;
//        int translatedCol = 9 - col;
        return "" + (char)('a' + col) + translatedRow;
    }

    public boolean isEnablesEnPassant() {
        return enablesEnPassant;
    }

    /**
     * Checks whether the move is an usual move/capture
     * @return true if move is NOT a drop-in or promotion, false otherwise
     */
    public boolean isNormal() {
        return source.isPresent() && destination.isPresent() && replacement.isEmpty();
    }

    /**
     * Check whether move is a promotion (pawn advancing to last row)
     * @return true if move is a promotion (promotion field set and source is not null)
     */
    public boolean isPromotion() {
        return source.isPresent() && destination.isPresent() && replacement.isPresent();
    }

    /**
     * Check whether the move is a crazyhouse drop-in (place a captured enemy piece
     * to fight on your side)
     */
    public boolean isDropIn() {
        return source.isEmpty() && destination.isPresent() && replacement.isPresent();
    }

    /**
     * Emit a move from src to dst. Validity is to be checked by engine (your implementation)
     * Positions are encoded as stated at beginning of file
     * Castles are encoded as follows:
     * source: position of king
     * destination: final position of king (two tiles away)
     * @param source initial tile
     * @param destination destination tile
     * @return move to be sent to board
     */
    public static Move moveTo(String source, String destination) {
        return new Move(source, destination, null);
    }

    /**
     * Emit a promotion move. Validity is to be checked by engine
     * (i.e. source contains a pawn in second to last row, etc.)
     * @param source initial tile of pawn
     * @param destination next tile (could be diagonal if also capturing)
     * @param replacement piece to promote to (must not be pawn or king)
     * @return move to be sent to board
     */
    public static Move promote(String source, String destination, PieceType replacement) {
        return new Move(source, destination, replacement);
    }

    /**
     * Emit a drop-in (Crazyhouse specific move where player summons
     * a captured piece onto a free tile. Pawns can not be dropped in first and last rows)
     * @param destination
     * @param replacement
     * @return
     */
    public static Move dropIn(String destination, PieceType replacement) {
        return new Move(null, destination, replacement);
    }

    public static Move resign() {
        return new Move(null, null, null);
    }
}

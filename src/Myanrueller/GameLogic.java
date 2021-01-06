package Myanrueller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameLogic implements GameLogicInterface {

    private GoPiece[][] render;
    private int player1Score, player2Score;
    private int move = 1;
    private boolean gameOver = false;

    GameLogic(GoPiece[][] gameBoard){
        this.render = gameBoard;
        player1Score = 0;
        player2Score = 0;
    }

    @Override
    public void placePiece(int x, int y, int player) throws Exception {
        if (gameOver) throw new Exception("Game over!");

        if (!getPiece(x, y).isEmpty()) throw new Exception("Place taken");

        GoPiece selectedPiece = getPiece(x, y);

        Set<GoPiece> patch = buildPatch(selectedPiece, player);

        if (isSuicideMove(selectedPiece, patch, player)){
            if (isKOMove(selectedPiece, patch, player)){
                selectedPiece.setPiece(player);
            }else throw new Exception("Suicide move");
        }else{
            selectedPiece.setPiece(player);
        }

        takeOpponentPieces(selectedPiece, player);
        move++;

        for (GoPiece[] row : render){
            for (GoPiece piece : row){
                piece.setForMoveLevel(move);
            }
        }

        if (isRepeatableState()){
            undo();
            throw new Exception("Repeatable state");
        }
    }

    private void undo(){
        move--;
        for (GoPiece[] row : render){
            for (GoPiece piece : row){
                piece.undoLastMove();
            }
        }
    }

    private boolean isRepeatableState() {
        boolean isRepeatableState = true;

        for (GoPiece[] row : render){
            for (GoPiece piece : row){
                isRepeatableState = piece.isRepeatableState() && isRepeatableState;
            }
        }
        return isRepeatableState;
    }

    private void takeOpponentPieces(GoPiece selectedPiece, int player) {
        final int other = player == 1 ? 2 : 1;
        final int x = selectedPiece.getX();
        final int y = selectedPiece.getY();

        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == other){ takeOverIfSurrounded(getPiece(x - 1, y), player); }
        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == other){ takeOverIfSurrounded(getPiece(x + 1, y), player); }
        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == other){ takeOverIfSurrounded(getPiece(x, y - 1), player); }
        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == other){ takeOverIfSurrounded(getPiece(x, y + 1), player); }
    }

    private void takeOverIfSurrounded(GoPiece piece, int player) {
        Set<GoPiece> patch = buildPatch(piece, piece.getPiece());

        if (isPatchSurrounded(patch)){
            updateScore(player == 1 ? 1 : 2, patch.size());
            for (GoPiece setPiece : patch) piece.setPiece(0);
        }
    }

    private void updateScore(int player, int increment) {
        switch (player){
            case 1:
                player1Score += increment;
                break;
            case 2:
                player2Score += increment;
        }
    }

    private boolean isKOMove(GoPiece selectedPiece, Set<GoPiece> patch, int player){
        boolean isKOMove = false;
        final int other = player == 1 ? 2 : 1;

        final int x = selectedPiece.getX();
        final int y = selectedPiece.getY();

        if (isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == other){
            isKOMove = canTakeOverArea(getPiece(x - 1, y), selectedPiece) || isKOMove;
        }

        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == other){
            isKOMove = canTakeOverArea(getPiece(x - 1, y), selectedPiece) || isKOMove; }

        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == other){
            isKOMove = canTakeOverArea(getPiece(x + 1, y), selectedPiece) || isKOMove; }

        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == other){
            isKOMove = canTakeOverArea(getPiece(x, y - 1), selectedPiece) || isKOMove; }

        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == other){
            isKOMove = canTakeOverArea(getPiece(x, y + 1), selectedPiece) || isKOMove; }

        return isKOMove;
    }

    private boolean canTakeOverArea(GoPiece piece, GoPiece selectedPiece){
        return isPatchSurrounded(buildPatch(piece, piece.getPlayer()), selectedPiece);
    }

    private boolean isPatchSurrounded(Set<GoPiece> buildPatch, GoPiece selectedPiece) {
        boolean isSurrounded = true;

        for (GoPiece piece : buildPatch){
            final int x = piece.getX();
            final int y = piece.getY();

            if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == 0 && getPiece(x - 1, y) != selectedPiece){ isSurrounded = false; }
            if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == 0 && getPiece(x + 1, y) != selectedPiece){ isSurrounded = false; }
            if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == 0 && getPiece(x, y - 1) != selectedPiece){ isSurrounded = false; }
            if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == 0 && getPiece(x, y + 1) != selectedPiece){ isSurrounded = false; }
        }
        return isSurrounded;
    }

    private boolean isPatchSurrounded(Set<GoPiece> patch) {
        boolean isSurrounded = true;
        for(GoPiece piece: patch) {
            final int x = piece.getX();
            final int y = piece.getY();
            if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == 0) { isSurrounded = false; break; }
            if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == 0) { isSurrounded = false; break; }
            if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == 0) { isSurrounded = false; break; }
            if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == 0) { isSurrounded = false; break; }
        }
        return isSurrounded;
    }



    private boolean hasEscapeRoute(GoPiece currentPiece, Set<GoPiece> patch, GoPiece selectedPiece){
        boolean hasEscapeRoute = false;

        for (GoPiece piece : patch){
            hasEscapeRoute = hasAvailableAdjacent(piece, currentPiece, selectedPiece) || hasEscapeRoute;
        }
        return  hasEscapeRoute;
    }

    private boolean hasAvailableAdjacent(GoPiece piece, GoPiece currentPiece, GoPiece selectedPiece) {
        int x = piece.getX();
        int y = piece.getY();

        boolean hasAvailableAdjacent = false;

        if(isValidIndex(x - 1, y) && isAvailablePlace(getPiece(x - 1, y), selectedPiece)) { hasAvailableAdjacent = true; }
        if(isValidIndex(x + 1, y) && isAvailablePlace(getPiece(x + 1, y), selectedPiece)) { hasAvailableAdjacent = true; }
        if(isValidIndex(x, y - 1) && isAvailablePlace(getPiece(x, y - 1), selectedPiece)) { hasAvailableAdjacent = true; }
        if(isValidIndex(x, y + 1) && isAvailablePlace(getPiece(x, y + 1), selectedPiece)) { hasAvailableAdjacent = true; }
        return hasAvailableAdjacent;
    }

    private boolean isAvailablePlace(GoPiece piece, GoPiece origin) {
        return piece.getPiece() == 0 && piece != origin;
    }

    private boolean isValidIndex(int x, int y) {
        return x >= 0 && y >= 0 && x < render.length && y < render[0].length;
    }

    private boolean isSuicideMove(GoPiece selectedPiece, Set<GoPiece> patch, int player) {
        boolean hasEscape = false;
        for (GoPiece piece : patch){
            hasEscape = hasEscapeRoute(piece, patch, selectedPiece) || hasEscape;
        }
        return !hasEscape;
    }

    private Set<GoPiece> buildPatch(GoPiece origin, int player) {
        Set<GoPiece> patch = new HashSet<>();

        buildPatch(origin, patch, player);

        return patch;
    }

    private void buildPatch(GoPiece origin, Set<GoPiece> patch, int player){
        if (patch.contains(origin)) return;

        patch.add(origin);

        int x = origin.getX();
        int y = origin.getY();


        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPlayer() == player) {
            buildPatch(getPiece(x - 1, y), patch, player);
        }

        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPlayer() == player) {
            buildPatch(getPiece(x + 1, y), patch, player);
        }

        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPlayer() == player) {
            buildPatch(getPiece(x, y - 1), patch, player);
        }

        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPlayer() == player) {
            buildPatch(getPiece(x, y + 1), patch, player);
        }
    }

    @Override
    public GoPiece getPiece(int x, int y) {
        return render[x][y];
    }

    @Override
    public boolean canPlacePiece(int x, int y) {
        return false;
    }

    private void addPatchToScore(List<GoPiece> patch){
        boolean isConnectedToPlayerOne = false;
        boolean isConnectedToPlayerTwo = false;

        for (GoPiece piece : patch){
            isConnectedToPlayerOne = isConnectedToPlayer(piece, 1) || isConnectedToPlayerOne;
            isConnectedToPlayerTwo = isConnectedToPlayer(piece, 2) || isConnectedToPlayerTwo;
        }

        if(!isConnectedToPlayerOne && isConnectedToPlayerTwo) player2Score += patch.size();
        if(isConnectedToPlayerOne && !isConnectedToPlayerTwo) player1Score += patch.size();
    }

    private boolean isConnectedToPlayer(GoPiece piece, int player) {
        boolean isConnectedToPlayer = false;
        final int x = piece.getX();
        final int y = piece.getY();
        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == player) isConnectedToPlayer = true;
        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == player) isConnectedToPlayer = true;
        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == player) isConnectedToPlayer = true;
        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == player) isConnectedToPlayer = true;
        return isConnectedToPlayer;
    }

    private List<List<GoPiece>> buildFreePatches(){
        Set<GoPiece> checkedFreePieces = new HashSet<>();
        List<List<GoPiece>> patches = new ArrayList<>();

        for (GoPiece[] row : render){
            for (GoPiece piece : row){
                if (piece.getPiece() == 0 && !checkedFreePieces.contains(piece))
                    patches.add(buildFreePatch(checkedFreePieces, piece));
            }
        }
        return patches;
    }

    private List<GoPiece> buildFreePatch(Set<GoPiece> checkedFreePieces, GoPiece startPiece) {
        List<GoPiece> patch = new ArrayList<>();
        if(checkedFreePieces.contains(startPiece)) return patch;
        checkedFreePieces.add(startPiece);
        patch.add(startPiece);
        final int x = startPiece.getX();
        final int y = startPiece.getY();
        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x - 1, y)));
        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x + 1, y)));
        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x, y - 1)));
        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x, y + 1)));
        checkedFreePieces.addAll(patch);
        return patch;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public String declareWinner() {
        return null;
    }

    @Override
    public int playerOneScore() {
        return player1Score;
    }

    @Override
    public int playerTwoScore() {
        return player2Score;
    }

    @Override
    public void endGame() {
        List<List<GoPiece>> patches = buildFreePatches();
        for (List<GoPiece> patch : patches) addPatchToScore(patch);
        gameOver = true;
    }

    private String determineWinner(){
        if (player1Score > player2Score){
            return "Black Wins!";
        }else if (player2Score > player1Score){
            return "White Wins!";
        }else{
            return "Draw";
        }
    }
}

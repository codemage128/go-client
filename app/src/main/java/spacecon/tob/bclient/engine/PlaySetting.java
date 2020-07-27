package spacecon.tob.bclient.engine;

/**
 * Created by HongGukSong on 9/19/2017.
 */

public class PlaySetting {

    public int nBlackPlayer;
    public int nWhitePlayer;
    public byte nHandicap = 0;
    public byte nBoardSize = 19;
    public byte nComLevel;
    public byte nGomi;

    public byte nType;//type

    public PlaySetting() {
        super();
    }

    public void copyTo(PlaySetting dest) {
        dest.nBlackPlayer = nBlackPlayer;
        dest.nWhitePlayer = nWhitePlayer;
        dest.nHandicap = nHandicap;
        dest.nBoardSize = nBoardSize;
        dest.nComLevel = nComLevel;
        dest.nGomi = nGomi;
        dest.nType = nType;
    }
}

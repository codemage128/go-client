package spacecon.tob.bclient.net;

/**
 * Created by HongGukSong on 7/30/2017.
 */

public class CLoginCookie {
    int	_UserAddrH;
    int _UserAddrL;
    // SocketId
    int	_UserKey;	// Random
    int	_UserId;	// DBId

    public void readFrom(CMessage msg) {
        _UserAddrH = msg.readInt();
        _UserAddrL = msg.readInt();
        _UserKey = msg.readInt();
        _UserId = msg.readInt();
    }
    public void writeTo(CMessage msg) {
        msg.writeInt(_UserAddrH);
        msg.writeInt(_UserAddrL);
        msg.writeInt(_UserKey);
        msg.writeInt(_UserId);
    }
}
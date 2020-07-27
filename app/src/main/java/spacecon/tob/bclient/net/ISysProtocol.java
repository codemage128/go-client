package spacecon.tob.bclient.net;

/**
 * Created by HongGukSong on 7/21/2017.
 */

public interface ISysProtocol {
    public int M_SYS_EMPTY					= 0xFFFFFFF;
    public int M_WAIT						= 0x65;
    public int M_CONNECT					= 0x66;

    public int M_REGISTER_PEER				= 0xC9;
    public int M_CS_UNREGISTER_PEER			= 0xCA;
    public int M_REQ_CONNECT_PEER			= 0xCB;
    public int M_CS_RES_CONNECT_PEER		= 0xCC;
    public int M_SC_CONNECT_PEER			= 0xCD;
    public int M_CS_CONNECT_PEER_OK			= 0xCE;
    public int M_SC_DISCONNECT_PEER			= 0xCF;

    public int M_CLIENTHELLO				= 0x12D;
    public int M_CERTIFICATE				= 0x12E;
    public int M_KEYEXCHANGE				= 0x12F;
    public int M_SERVERHELLO				= 0x130;
    public int M_HANDSHKFAIL				= 0x131;
    public int M_HANDSHKOK					= 0x132;

    public int M_SECURITYCLIENT_REFUSE		= 0x193;
    public int M_SECURITYCLIENT_CANCONNECT	= 0x194;
    public int M_SECURITYCLIENT_GMCONNECT	= 0x195;

    public int M_ADMIN_EXEC_COMMAND_RESULT	= 0x1F5;
    public int M_ADMIN_ADMIN_PONG			= 0x1F6;
    public int M_ADMIN_SID					= 0x1F7;
    public int M_SR							= 0x1F8;
    public int M_ADMIN_VIEW					= 0x1F9;
    public int M_ADMIN_ADMIN_EMAIL			= 0x1FA;
    public int M_ADMIN_GRAPH_UPDATE			= 0x1FB;
    public int M_ADMIN_INFO					= 0x1FC;
    public int M_ADMIN_GET_VIEW				= 0x1FD;
    public int M_ADMIN_STOPS				= 0x1FE;
    public int M_ADMIN_EXEC_COMMAND			= 0x1FF;
    public int M_ADMIN_ADMIN_PING			= 0x200;

    //add by cch 20100816
    public int M_ADMIN_TEST_PING			= 0x201;
    public int M_ADMIN_TEST_PONG			= 0x202;

    public int M_MODULE_15_TRANSPORT_GW_L5_ADDTP	= 0x259;
    public int M_MODULE_15_TRANSPORT_GW_L5_MSG		= 0x25A;
    public int M_MODULE_15_TRANSPORT_GW_L5_REMTP	= 0x25B;

    public int M_LOGIN_SERVER_RPC			= 0x2BD;
    public int M_LOGIN_SERVER_DC			= 0x2BE;
    public int M_LOGIN_SERVER_SCS			= 0x2BF;
    public int M_LOGIN_SERVER_FORCESV		= 0x2C0;
    public int M_LOGIN_SERVER_SV			= 0x2C1;
    public int M_LOGIN_SERVER_CC			= 0x2C2;
    public int M_LOGIN_SERVER_CS			= 0x2C3;
    public int M_KA							= 0x2C4;

    public int M_MODULE_GATEWAY_MOD_OP		= 0x385;
    public int M_MODULE_GATEWAY_MOD_UPD		= 0x386;
    public int M_EXCEPT						= 0x387;
    public int M_DEBUG_MOD_PING				= 0x388;

    public int M_UNIFIED_NETWORK_UN_SIDENT	= 0x44D;

    public int M_UNITIME_GUT				= 0x4B1;
    public int M_UNITIME_AUT				= 0x4B2;

    public int M_TRANSPORT_CLASS_CT_MSG		= 0x515;
    public int M_TRANSPORT_CLASS_CT_LRC		= 0x516;

    public int M_SERVICE_R_SH_ID			= 0x579;

    public int M_NET_DISPLAYER_LOG			= 0x5DD;

    public int M_NAMING_CLIENT_ACK_UNI		= 0x641;
    public int M_NAMING_CLIENT_RG			= 0x642;
    public int M_NAMING_CLIENT_QP			= 0x643;
    public int M_NAMING_CLIENT_RGB			= 0x644;
    public int M_NAMING_CLIENT_UNB			= 0x645;
    public int M_NAMING_CLIENT_RRG			= 0x646;
    public int M_NAMING_CLIENT_UNI			= 0x647;
}

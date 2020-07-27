package spacecon.tob.bclient.net;

/**
 * Created by HongGukSong on 7/21/2017.
 */

public interface IProtocol extends ISysProtocol {

    //써버->말단
    // connect
    public int M_SC_HANDSHKOK = M_HANDSHKOK; // 접속성공
    public int M_SC_HANDSHKFAIL = M_HANDSHKFAIL; // 접속실패


    public int M_SC_WAITCONNECTION = M_WAIT; // 접속대기([d:user수][d:user감소시간(ms)])
    public int M_SC_ADMITCONNECTION = M_CONNECT; // 접속허락()

    // disconnect
    public int M_CS_DC = M_LOGIN_SERVER_DC; //써버에서 로그아우트할때
    public int M_SC_DC = M_CS_DC; // 능동탈퇴(게임탈퇴;창문끄기)

    // TryEnterToShard
    public int M_SC_SCS = M_LOGIN_SERVER_SCS; // 지역싸이트진입요청응답실패 ([w:오유번호] 0이면 성공->지역싸이트접속)
    public int M_SC_SV = M_LOGIN_SERVER_SV; // 지역싸이트인증성공 (로그인과정) ([w:오유번호]  0이면 성공)

    public int M_CS_CS = M_LOGIN_SERVER_CS; //지역써버를 선택하였을때

    public int M_SM_VIEW = M_ADMIN_VIEW;

    //말단->써버
    public int M_SYS_DISCONNECTM_SYS_DISCONNECT = 0x7D0; // 써버와의 접속차단
    public int M_SYS_TIMEOUT = 0x7D1; // 응답대기 Timeout

    public int M_NT_LOGIN = 0x7D2; // Login
    public int M_NT_LOGOUT = 0x7D3; // Logout
    public int M_WS_LOGOUT = 0x7D4; // Logout

    public int M_CS_AUTH = 0x7D5; //login request
    public int M_CS_REG_PHONE_REQ = 0x07D6;
    public int M_SC_REG_PHONE_RES = 0x07D7;
    public int M_SC_SHARDS = 0x7D8; //login reponse

    public int M_SC_AUTH = M_CS_AUTH;

    // TryEnterToShard
    public int M_CS_CONNECTSHARD = 0x7D9;
    public int M_SC_CONNECTSHARD = M_CS_CONNECTSHARD; // 지역싸이트접속성공(파라메터 : CSecurityClient *)
    public int M_SC_LOGIN_FAIL = 0x7DA; // 로그인실패(로그인과정) ([w:오유번호]  0이면 성공)
    public int M_SC_IDENTIFICATION = 0x7DB; // 로그인완전성공([d:dbID])
    public int M_CS_CANCELCONNECTION = 0x7DC;
    public int M_SC_CANCELCONNECTION = M_CS_CANCELCONNECTION; //접속대기취소
    public int M_CS_REG = 0x7DD;
    public int M_SC_REG = M_CS_REG;
///////////////////////////////////////////////////////////////////////////
    public int M_CS_TEMP = 0x7DE;

    public int M_CS_AUTO_REG_REQ = 0x08A1;
    public int M_SC_AUTO_REG_RES = 0x08A2;

    public int M_CS_FIND_PWD = 0x08b1;
    public int M_SC_FIND_PWD = 0x08b2;
///////////////////////////////////////////////////////////////////////////

    // Update
    public int M_CS_FTPURI = 0x9C4; // update목록([s:제품이름][s:현재판번호])
    public int M_SC_FTPURI = M_CS_FTPURI; // update써버목록([s:최신판번호][s:써버 url][d:port][s:상대경로][s:userID][s:password])

    //관리말단 : 써버
    public int M_CS_UPDATEPATCH_REQ = 0x9C5; //갱신말단요청([b:OS형태][d:CurVersion]

    public int M_SC_UPDATEPATCH_RES = 0x9C6; //갱신말단응답([b:갱신결과][d:새버젼][U:패치파일이름][U:FTP호스트][d:FTP포트][U:FTP사용자계정][U:FTP암호][U:FTP디렉토리]

    //관리자용메세지
    public int MM_CS_LOGIN_REQ = 0xBB8; //관리자의 접속식별요청메세지 ([u:관리자이름][u:관리자암호]
    public int MM_SC_LOGIN_RES = 0xBB9; //관리자의 접속식별응답메세지

    public int MM_CS_LOGOUT_REQ = 0xBBA; //관리자의 로그아우트요청메세지
    public int MM_SC_LOGOUT_RES = 0xBBB; //관리자의 로그아우트응답메세지

    public int M_SM_REGSERVICE_DATABASE = 0xBBC;
    public int M_SM_SERVICE_DATABASE = 0xBBD;

    public int M_MS_REGUSER_REQ = 0xBC0; //사용자등록요청메세지
    public int M_SM_REGUSER_RES = 0xBC1; //사용자등록응답메세지

    public int MM_CS_MANCONSOLE = 0xBC2; //지령실행요청메세지
    public int MM_SC_MANCONSOLE_RES = 0xBC3; //지령실행응답메세지

    public int MM_CS_MANAGERLIST_REQ = 0xBC4; //관리자목록요청메세지
    public int MM_SC_MANAGERLIST_RES = 0xBC5; //관리자목록응답메세지

    public int MM_CS_CHANGE_MANINFO_REQ = 0xBC6; //관리자정보변경요청메세지
    public int MM_SC_CHANGE_MANINFO_RES = 0xBC7; //관리자정보변경응답메세지

    public int MM_CS_SERVICELIST_REQ = 0xBC8; //써비스목록요청메세지
    public int MM_SC_SERVICELIST_RES = 0xBC9; //써비스목록응답메세지

    public int MM_CS_SERVICEINFO_REQ = 0xBCA; //써비스의 상세정보요청메세지
    public int MM_SC_SERVICEINFO_RES = 0xBCB; //써비스의 상세정보응답메세지

    public int MM_CS_MAN_SERVICE_REQ = 0xBCC; //써비스조종요청메세지
    public int MM_SC_MAN_SERVICE_RES = 0xBCD; //써비스조종응답메세지

    public int MM_CS_LOGFILE_REQ = 0xBCE; //로그파일받기요청메세지
    public int MM_SC_LOGFILE_RES = 0xBCF; //로그파일받기응답메세지

    public int MM_SC_NTF_SERVICECHANGE = 0xBD0; //써비스변경통지메세지
    public int MM_CS_LOGFILE_RECV_CONFIRM = 0xBD1; //로그파일토막수신여부에 대한 통지메세지

    public int MM_CS_PINGPONG_REQ = 0xBD2; //ping-pong요청메세지
    public int MM_SC_PINGPONG_RES = 0xBD3; //ping-pong응답메세지

    public int MM_CS_ZONEINFO_REQ = 0xBD4; //지역써버정보요청메세지
    public int MM_SC_ZONEINFO_RES = 0xBD5; //지역써버정보응답메세지

    /***********************************************************************
     ************************************************************************ 	게임을 진행하는 과정에 통신하는 써버:말단사이의 메세지들
     ************************************************************************
     ************************************************************************/
    //강제로그인메세지
    public int M_CS_US_FORCE_LOGIN = 0xFA0;

    //사용자로그아우트메세지
    public int M_CS_LOGOUT = 0xFA1;
    public int M_SC_US_FORCE_LOGOUT = 0xFA2;

    //클라이언트에서 SS로부터 DC메쎄지를 받은후 최종적으로 PS에 등록하기 위하여 FES에 보내는 메쎄지
    public int M_CS_FES_CONNECTED = 0xFA3;
    public int M_SC_FES_CONNECTED = 0xFA4;
    //결과로 등록시 오유번호를 보낸다.

    //사용자목록요청
    public int M_CS_US_LIST_REQ = 0xFA5;
    public int M_SC_US_LIST_RES = 0xFA6;

    //사용자차분목록
    public int M_SC_US_CHANGELIST = 0xFA7;

    public int M_CS_US_INVITEROOM_REQ = 0xFA8;
    public int M_SC_US_INVITEROOM_REQ = 0xFA9;
    public int M_CS_US_INVITEROOM_ACCEPT_RES = 0xFAA;
    public int M_SC_US_INVITEROOM_ACCEPT_RES = 0xFAB;

    //친구목록요청
    public int M_CS_US_FRIENDLIST_REQ = 0xFAC;
    public int M_SC_US_FRIENDLIST_RES = 0xFAD;

    //불량목록요청
    public int M_CS_US_BADLIST_REQ = 0xFAE;
    public int M_SC_US_BADLIST_RES = 0xFAF;

    //써버자료목록
    public int M_CS_SH_LIST_REQ = 0xFB0;
    public int M_SC_SH_LIST = 0xFB1;
    public int M_SC_SH_CHANGELIST = 0x0FB2;

    //채팅메세지 add by cch 20091015
    public int M_CS_US_CHAT = 0x1068; //전체채팅
    public int M_SC_US_CHAT = 0x1069;

    //1:1대화요청
    public int M_CS_US_WISPER_REQ = 0x106A; //1:1대화 요청하기
    public int M_SC_US_WISPER_REQ = 0x106B; //1:1대화 요청받기
    public int M_CS_US_WISPER_RES = 0x106C; //1:1대화 요청에 대한 응답하기
    public int M_SC_US_WISPER_RES = 0x106D; //1:1대화 요청에 대한 응답받기

    //1:1대화취소
    public int M_CS_US_WISPER_END = 0x106E; //1:1대화
    public int M_SC_US_WISPER_END = 0x106F;

    //1:1대화메쎄지
    public int M_CS_US_WISPER_TEXT = 0x1070;
    public int M_SC_US_WISPER_TEXT = 0x1071;

    //메일보내기
    public int M_CS_US_SENDMEMO = 0x1072; //메일보내기[ucstring(제목)][ucstring(내용)][uint16(받을사람수)][ucstring(이름)]...
    public int M_SC_US_SENDMEMO_RES = 0x1073; //메일보내기에 대한 결과[uint8(1:성공; 0:실패)]
    public int M_CS_US_MEMO_REQ = 0x1074; //메모요청				[uin32(메일번호)]
    public int M_SC_US_MEMO_RES = 0x1075; //메모응답				[ucstring(보낸사람이름)][ucstring(제목)][ucstring(내용)]
    public int M_CS_US_MEMOLIST_REQ = 0x1076; //메모목록요청
    public int M_SC_US_MEMOLIST = 0x1077; //메모목록 보내기		[uint16(메일개수)][uint32(메일번호)][ucstring(송신자명)][TTime(보낸시간)][ucstring(제목)]...
    public int M_CS_US_REMOVEMEMO = 0x1078; //메모삭제				[uint16(메일개수)][uint32(메일번호)]...
    public int M_SC_US_REMOVEMEMO_RES = 0x1079; //메모삭제에 대한 결과	[uint8(1:성공; 0:실패)][uint16(메일개수)][uint32(메일번호)]...

    //친구등록(친구사기기) add by cch 20091015
    public int M_CS_US_ADDFRIEND = 0x107A;
    public int M_SC_US_ADDFRIEND_RES = 0x107B;

    //친구삭제 add by cch 20091015
    public int M_CS_US_REMOVEFRIEND = 0x107C;
    public int M_SC_US_REMOVEFRIEND_RES = 0x107D;

    //불량자등록 add by cch 20091015
    public int M_CS_US_ADDBAD = 0x107E;
    public int M_SC_US_ADDBAD_RES = 0x107F;

    //불량자삭제 add by cch 20091015
    public int M_CS_US_REMOVEBAD = 0x1080;
    public int M_SC_US_REMOVEBAD_RES = 0x1081;

    //사용자위치찾기
    public int M_CS_US_FINDUSER = 0x1082;
    public int M_SC_US_FINDUSER_RES = 0x1083;

    //사용자정보요청
    public int M_CS_US_INFO = 0x1084;
    public int M_SC_US_INFO = 0x1085;

    //대국신청 가능/불가능 상태설정
    public int M_CS_US_ENABLEREQGM = 0x1086;
    public int M_SC_US_ENABLEREQGM = 0x1087;

    //대국신청
    public int M_CS_US_SELOPP = 0x1130; //[u:상대방이름]
    public int M_SC_US_SELBYANOTHER = 0x1131; //[u:보낸 사람이름]
    public int M_CS_US_SELOPP_RES = 0x1132; //[u:상대방이름][b:응답결과]
    public int M_SC_US_SELOPP_RES = 0x1133; //[u:응답받을 상대방이름][b:응답결과]

    //방차분목록
    public int M_SC_RM_CHANGELIST = 0x1388;

    //방목록요청
    public int M_CS_RM_LIST_REQ = 0x1389;
    public int M_SC_RM_LIST_RES = 0x138A;
    public int M_CS_RM_QUIT_ROOM = 0x138B;
    public int M_SC_RM_QUIT_ROOM = 0x138C;

    //방창조(클라이언트->사용자써비스)
    public int M_CS_RM_CREATEROOM_REQ = 0x138D;
    public int M_SC_RM_CREATEROOM_RES = 0x138E;

    //방진입
    public int M_CS_RM_ENTERROOM = 0x138F; //[uint16(방번호)]
    public int M_SC_RM_ENTERROOM_RES = 0x1390; //[uint8(실패))]

    public int M_SC_RM_CHATMSG = 0x1391;
    public int M_CS_RM_CHATMSG = 0x1392;

    //이미 진행중이던 대국이 있는가 문의 add by cch 20100820
    public int M_CS_US_EXISTPREVGAME_REQ = 0x1393;
    //망차단 재접속이후 대국재개 add by cch 20091109
    public int M_SC_US_EXISTPREVGAME = 0x1394; //대국재개문의[w:방번호]
    public int M_CS_US_EXISTGAME_RES = 0x1395; //문의응답[w:방번호][b:응답]

    //기보목록및해당기보요청;응답관련메세지 add by cch 20091109
    public int M_CS_KB_KIBOLIST_REQ = 0x1396; //기보목록요청[b:요청형태][d:마지막으로 본 기보ID][u:사용자명]
    public int M_SC_KB_KIBOLIST = 0x1397; //기보목록응답[b:기보개수][KIBO_RECORD:기보1정보]...
    public int M_CS_KB_KIBO_REQ = 0x1398; //지정사용자의 기보요청[d:기보ID]
    public int M_SC_KB_KIBO_RES = 0x1399; //지정사용자의 기보응답[s:기보내용]


    //대국방정보요청 add by ysg 20091021
    public int M_CS_RM_ROOMINFO_REQ = 0x139A; //방정보요청
    public int M_SC_RM_ROOMINFO_RES = 0x139B; //방정보
    public int M_SC_RM_OBSERVERCHANGE = 0x139C; //관전자목록변경알림

    public int M_CS_GM_GAMEINFO_REQ = 0x139D; //대국정보요청
    public int M_SC_GM_GAMEINFO_RES = 0x139E; //대국정보


    /*hgs*/
    public int M_INTERFACE_DELSTONE = 0x654321;
    //대국설정

    public int M_CS_GM_SETTING = 0x1450;
    public int M_SC_GM_SETTING = 0x1451;
    public int M_CS_GM_SETRES = 0x1452;
    public int M_SC_GM_SETRES = 0x1453;
    public int M_CS_GM_PUTSTONE = 0x1454;
    public int M_SC_GM_PUTSTONE = 0x1455;
    public int M_CS_GM_RETRACT_REQ = 0x1456;
    public int M_SC_GM_RETRACT_REQ = 0x1457;
    public int M_CS_GM_RETRACT_RES = 0x1458;
    public int M_SC_GM_RETRACT_RES = 0x1459;
    public int M_CS_GM_THROW = 0x145A;
    public int M_SC_GM_THROW = 0x145B;
    public int M_CS_GM_DRAW_REQ = 0x145C;
    public int M_SC_GM_DRAW_REQ = 0x145D;
    public int M_CS_GM_DRAW_RES = 0x145E;
    public int M_SC_GM_DRAW_RES = 0x145F;
    public int M_CS_GM_ESTIMATE_REQ = 0x1460;
    public int M_SC_GM_ESTIMATE_REQ = 0x1461;
    public int M_CS_GM_ESTIMATE_RES = 0x1462;
    public int M_SC_GM_ESTIMATE_RES = 0x1463;
    public int M_SC_GM_HAVETIMEOVER = 0x1464; //[b:1,2:쓸차례의 색갈][w:방번호]
    public int M_SC_GM_COUNTDOWN = 0x1465; //[b:1,2:쓸차례의 색갈][w:방번호][b:CountDownNum]
    public int M_CS_GM_END = 0x1466; //대국결과알림 [w:방번호][b:종료결과][b:승자의 색][GAME_ENDINFO:게임종료정보]
    public int M_SC_GM_END = 0x1467; //대국결과알림 [w:방번호][b:종료결과][GAME_ENDINFO:게임종료정보]
    public int M_SC_GM_CONTINUEGAME = 0x1468;
    public int M_CS_GM_ALLOW_OPPROLE = 0x1469;
    public int M_SC_GM_ALLOW_OPPROLE = 0x146A;
    public int M_CS_GM_REPLAY_START = 0x146B;
    public int M_SC_GM_REPLAY_START = 0x146C;
    public int M_CS_GM_REPLAY_END = 0x146D; //복기끝
    public int M_SC_GM_REPLAY_END = 0x146E; //복기끝
    public int M_CS_GM_REPLAY_MOVE = 0x146F; //기보이동 [w:방번호][w:이동하는 기보위치]2000
    public int M_SC_GM_REPLAY_MOVE = 0x1470; //기보이동 [w:방번호][w:이동하는 기보위치]
    public int M_CS_GM_SET_REPLAYER = 0x1471; //진행자설정 [w:방번호][u:진행자명]
    public int M_SC_GM_SET_REPLAYER = 0x1472; //진행자설정 [w:방번호][u:진행자명]
    public int M_CS_GM_UNSET_REPLAYER = 0x1473; //진행자설정해제
    public int M_SC_GM_UNSET_REPLAYER = 0x1474; //진행자설정해제
    public int M_SC_GM_GAMER_DISCONNECTED = 0x1475; //대국자망절단통보
    public int M_SC_GM_GAMER_REENTER = 0x1476; //대국자재입실 통보

    //생중계방에서 리용되는 메세지들	add by cch 20091202
    public int M_CS_GM_TST_START = 0x14B4; //놓아보기 시작하겠음(해설자) [w:방번호][w:기보위치]
    public int M_SC_GM_TST_START = 0x14B5; //놓아보기시작통지 [w:방번호][w:기보위치]
    public int M_CS_GM_TST_RESTART = 0x14B6; //놓아보기재시작[w:방번호]
    public int M_SC_GM_TST_RESTART = 0x14B7; //놓아보기재시작통지[w:방번호]
    public int M_CS_GM_TST_END = 0x14B8; //놓아보기끝내겠음[w:방번호]
    public int M_SC_GM_TST_END = 0x14B9; //놓아보기끝통지[w:방번호]
    public int M_CS_GM_RELOADFINAL = 0x14BA; //최종착수와 일치시키기[w:방번호]
    public int M_SC_GM_RELOADFINAL = 0x14BB; //최종착수와 일치시키기[w:방번호]
    public int M_CS_GM_TST_STONE = 0x14BC; //놓아보기돌정보 [w:방번호][b:돌색][w:돌위치]
    public int M_SC_GM_TST_STONE = 0x14BD; //놓아보기돌정보 [w:방번호][b:돌색][w:돌위치]
    public int M_CS_GM_GAME_COMMENT = 0x14BE; //현 기본대국착수에 대한 설명 [w:방번호][w:기보위치][u:착수설명]
    public int M_SC_GM_GAME_COMMENT = 0x14BF; //현 기본대국착수에 대한 설명 [w:방번호][w:기보위치][u:착수설명]
    public int M_CS_GM_TEST_COMMENT = 0x14C0; //놓아보기착수에 대한 설명 [w:방번호][w:기보위치][u:착수설명]
    public int M_SC_GM_TEST_COMMENT = 0x14C1; //놓아보기착수에 대한 설명 [w:방번호][w:기보위치][u:착수설명]
    public int M_CS_GM_CHANGE_LIVEROOM = 0x14C2; //생중계방절환 [w:방번호][w:절환하려는 방번호]
    public int M_SC_GM_CHANGE_LIVEROOM = 0x14C3; //생중계방절환 [w:방번호][b:절환결과][w:절환하려는 방번호]

    //해설방에서 리용되는 메세지들	add by cch 20091210
    public int M_CS_CREATE_EMPTYKIBO = 0x1518; //새기보창조요청[w:방번호]
    public int M_SC_CREATE_EMPTYKIBO = 0x1519; //새기보창조응답[w:방번호]

    //씨나리오대국관련메세지들		add by cch 20100805
    public int M_CS_ENTER_SCENARIOROOM_REQ = 0x151A; //씨나리오대국방진입요청
    public int M_SC_ENTER_SCENARIOROOM_RES = 0x151B; //씨나리오대국방진입응답
    public int M_CS_SCN_ROOMINFO_REQ = 0x151C; //씨나리오대국방정보요청
    public int M_SC_SCN_ROOMINFO_RES = 0x151D; //씨나리오대국방정보응답

    public int M_CS_SCN_NEXTPROB_REQ = 0x151E; //다음문제이동요청
    public int M_SC_SCN_NEXTPROB_RES = 0x151F; //다음문제이동응답

    public int M_CS_GM_DEADSTONE = 0x1520; //사석지정요청
    public int M_SC_GM_DEADSTONE = 0x1521; //사석지정응답
    public int M_CS_GM_FINISH_CALC = 0x1522; //사석지정완료
    //신소제출용
    public int M_CS_US_OFFER_APPEAL_REQ = 0x1526;	//신소제출요청
    public int M_SC_US_OFFER_APPEAL_RES = 0x1527;	//신소제출응답

    //공개알림문통지용(써버->말단)
    public int M_SC_US_PUBLIC_NOTICE = 0x1528;  	//공개알림문통지

    //public int M_CS_INTERRUPTGAME_REQ = 0x1528; //대국림시중단요청메세지
    public int M_SC_INTERRUPTGAME_REQ = 0x1529; //대국림시중단요청중계메세지
    public int M_CS_INTERRUPTGAME_RES = 0x152A; //대국림시중단응답메세지
    public int M_SC_INTERRUPTGAME_RES = 0x152B; //대국림시중단응답중계메세지

    /***********************************기보에서 중단대국재개***********************************/
    public int M_CS_INTERRUPTGAMELIST_REQ = 0x152C; //림시중단대국목록요청메세지
    public int M_SC_INTERRUPTGAMELIST_RES = 0x152D; //림시중단대국목록응답메세지

    public int M_CS_RESUME_INTERRUPTGAME_REQ = 0x152E; //중딘대국재개요청메세지
    public int M_SC_RESUME_INTERRUPTGAME_REQ = 0x152F; //중단대국재개요청중계메세지

    public int M_CS_RESUME_INTERRUPTGAME_RES = 0x1530; //중단대국재개응답메세지
    public int M_SC_RESUME_INTERRUPTGAME_RES = 0x1531; //중단대국재개응답중계메세지

    //기보문제풀이 메쎄지
    public int M_CS_PROBLEMLIST_REQ = 0x1532;
    public int M_SC_PROBLEMLIST_RES = 0x1533;// 클래식기보 목록 응답 ::param->@count:uint8(기보개수)[@id:uint32(기보ID), @comment:ucstring(), @type:uint32(), @defCount:uint32(), @succCount:uint32(), @nTime:uint32(), @answerMoney:uint32(풀이방법비), @oppMoney:uint32(도전비), @upPoint:uint32(상승포인트)]
    public int M_CS_PROBLEM_REQ = 0x1534;
    public int M_SC_PROBLEM_RES = 0x1535;
    public int M_CS_SOLUTION_REQ = 0x1536;
    public int M_SC_SOLUTION_RES = 0x1537;

    public int M_CS_SAVE_KIBO_REQ = 0x1539;
    public int M_SC_SAVE_KIBO_RES = 0x1540;

    public int M_CS_AREANA_ENTER = 0x1541;
    public int M_SC_AREANA_ENTER = 0x1542;

    public int M_CS_AREANA_SETTING = 0x1543;
    public int M_SC_AREANA_SETTING = 0x1544;

    public int M_SC_AREANA_INFO = 0x1545; // Receive Message for Areana List and Competition parameter areana count : byte ,areana info : areana data*, competition count: byte, competition info: competion data*

    public int ERR_NOERR = 0x0; // success
    public int E_COMMON_FAILED = 0x1;
    public int E_DBERR = 0x2;
    public int E_ALREADY_EXSIT = 0x7D0;

//    public int M_SC_FESDC = 0x2000;
//    public int M_CS_USER_INFO_REQ = 0x2001;
//    public int M_SC_USER_INFO_RES = 0x2002;

    //대회용사용자관리써비스&말단사이의 메세지들 add by ccho 20111020
    public int M_CS_US_SERVERTIME_REQ = 0x2000;		//써버의 현재시간요청메세지
    public int M_SC_US_SERVERTIME_RES = 0x2001;		//써버의 현재시간응답메세지
    public int M_CS_TN_LIST_REQ = 0x2002;			//유효대회목록요청메세지
    public int M_SC_TN_LIST_RES = 0x2003;			//유효대회목록응답메세지
    public int M_CS_TN_ATTENDLIST_REQ = 0x2004;		//특정사용자의 참가대회목록요청메세지
    public int M_SC_TN_ATTENDLIST_RES = 0x2005;		//특정사용자의 참가대회목록응답메세지
    public int M_CS_TN_FAVORLIST_REQ = 0x2006;		//특정사용자의 애착대회목록요청메세지
    public int M_SC_TN_FAVORLIST_RES = 0x2007;		//특정사용자의 애착대회목록응답메세지
    public int M_CS_TN_CREATE_REQ = 0x2008;			//특정사용자의 대회주최요청메세지
    public int M_SC_TN_CREATE_RES = 0x2009;			//특정사용자의 대회주최응답메세지
    public int M_SC_TN_CHANGELIST = 0x200A;			//대회변경에 대한 응답메세지
    public int M_CS_TN_ADDFAVOR_REQ = 0x200B;		//애착대회추가요청메세지
    public int M_SC_TN_ADDFAVOR_RES = 0x200C;		//애착대회추가응답메세지
    public int M_CS_TN_RMVFAVOR_REQ = 0x200D;		//애착대회삭제요청메세지
    public int M_SC_TN_RMVFAVOR_RES = 0x200E;		//애착대회삭제응답메세지
    public int M_CS_TN_ATTEND_REQ = 0x200F;			//대회참가요청메세지
    public int M_SC_TN_ATTEND_RES = 0x2010;			//대회참가응답메세지
    public int M_CS_TN_ENTER_REQ = 0x2011;			//대회입실요청메세지
    public int M_SC_TN_ENTER_RES = 0x2012;			//대회입실응답메세지
    public int M_SC_TN_CHANGE_GMLIST_RES = 0x2013;	//특정대회장안에서의 경기자변경응답메세지
    public int M_CS_TN_GMLIST_REQ = 0x2014;			//특정대회의 경기자목록요청메세지
    public int M_SC_TN_GMLIST_RES = 0x2015;			//특정대회의 경기자목록응답메세지
    public int M_CS_TN_SCHEDULE_REQ = 0x2016;		//특정대회의 대전표요청메세지
    public int M_SC_TN_SCHEDULE_RES = 0x2017;		//특정대회의 대전표응답메세지
    public int M_CS_TN_QUIT_REQ = 0x2018;			//특정대회장에서 탈퇴요청메세지
    public int M_SC_TN_QUIT_RES = 0x2019;			//특정대회장에서 탈퇴응답메세지
    public int M_SC_TN_CHANGE_SCHEDULE = 0x201A;	//특정대회장에서 대전표갱신응답메세지
    public int M_CS_US_EXISTPREVTN_REQ = 0x201B;	//망차단되였던 사용자의 이미 들어가있던 대회에 대한 유무요청메세지
    public int M_SC_US_EXISTPREVTN_RES = 0x201C;	//망차단되였던 사용자의 이미 들어가있던 대회에 대한 유무응답메세지
    public int M_CS_US_EXISTTN_RES = 0x201D;		//망차단되였던 대회입실에 대한 접수/거절요청메세지
    public int M_CS_TN_INFO_REQ = 0x201E;			//특정대회의 상세정보요청메세지
    public int M_SC_TN_INFO_RES = 0x201F;			//특정대회의 상세정보응답메세지
    public int M_CS_TN_HISTORYLIST_REQ = 0x2020;	//대회리력목록요청메세지
    public int M_SC_TN_HISTORYLIST_RES = 0x2021;	//대회리력목록응답메세지
    public int M_CS_TN_KIBOLIST_REQ = 0x2022;		//대회리력에서 특정대회의 기보목록요청메세지
    public int M_SC_TN_KIBOLIST_RES = 0x2023;		//대회리력에서 특정대회의 기보목록응답메세지

    //추첨관련 메세지
    public int M_CS_RM_BETLIST_REQ = 0x2030;		//추첨목록요청
    public int M_SC_RM_BETLIST_RES	= 0x2031;		//추첨목록응답
    public int M_CS_RM_BETTING_REQ	= 0x2032;		//추첨
    public int M_SC_RM_BETTING_RES	= 0x2033;		//추첨응답
    public int M_SC_RM_CHANGE_BETTING	= 0x2034;	//추첨변경

    //
    public int M_SC_MN_NOTICE = 0x2050;			//특정사용자에게 보내는 관리측의 알림문통지메세지


    public int M_SC_ALIVE = 0x00D0;					//ping
    public int M_CS_ALIVE = M_SC_ALIVE;				//pong

}

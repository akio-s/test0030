package jp.co.token.optout;

import java.sql.*;

import com.ibm.as400.access.*;

import jp.co.token.optout.util.Tools;
import jp.co.token.optout.util.UTBLogManager;

/**
 * オプトアウトＤＢ登録処理クラス.
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WIBOptOutDB extends WIBDataSourceLookup {
    /** ユーザー情報クラス */
    private WICCommonInfo commonInfo;

    /**
     * コンストラクタ.
     *
     */
    public WIBOptOutDB() {
        super();
    }

    /** 
     * 共通セッション情報のsetter.
     *
     * @param commonInfo 共通セッション情報
     */
    public void setCommonInfo(WICCommonInfo commonInfo) {
        this.commonInfo = commonInfo;
    }

    /**
     * 共通セッション情報のgetter.
     *
     * @return 共通セッション情報
     */
    public WICCommonInfo getCommonInfo() {
        return this.commonInfo;
    }

    /**
     * オプトアウト登録処理.
     *
     * @throws WICDBException DB接続エラー発生時
     */
    public void execute() throws WICDBException {
        final String MY_METHOD_NAME = "WIBOptOutDB::execute()";
        Connection con = null;
        try {
            // コネクション取得
            con = getConnection();
            // 自動コミットモード
            con.setAutoCommit(true);
            // オプトアウトＤＢ登録
            if (commonInfo.getExist().equals("")) {
                insertOptOut(con);
            } else {
                updateOptOut(con);
            }
            // メール送信
            sendMail();
        } catch (SQLException e) {
            UTBLogManager.writeLog(this.getClass(), e.toString());
            throw new WICDBException(MY_METHOD_NAME, "90001", 
                                      e.getMessage(), e.getErrorCode());
        } finally {
            try {
                closeConnection(con);
                con = null;
            } catch (SQLException ex) {
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                                          ex.getMessage(), ex.getErrorCode());
            }
        }
    }

    /**
     * OPTOUT新規登録処理.
     *
     * @param con コネクション
     * @throws WICDBException DB接続例外発生時
     */
    private void insertOptOut(Connection con) throws WICDBException {
        final String MY_METHOD_NAME = "WIBOptOutDB::insertOptOut()";

        PreparedStatement stmt = null;
        StringBuffer sbSql = new StringBuffer();

        String userID           = commonInfo.getUserID();
        String weddingFlag      = commonInfo.getWeddingFlag();
        String weddingStopDate  = Tools.addFrontZero(commonInfo.getWeddingStopDate(), 8);
        String birthdayFlag     = commonInfo.getBirthdayFlag();
        String birthdayStopDate = Tools.addFrontZero(commonInfo.getBirthdayStopDate(), 8);
        String spouseFlag       = commonInfo.getSpouseFlag();
        String spouseStopDate   = Tools.addFrontZero(commonInfo.getSpouseStopDate(), 8);

        try{
            sbSql.append("INSERT INTO ");
            sbSql.append(getPoolDataLibrary1());
            sbSql.append(".SG0T ");
            sbSql.append("VALUES(?, ?, ?, ?, ?, ?, ?, ");
            sbSql.append("'OPTOUT', ?, ?, 'OUTPUT', ");
            sbSql.append("'          ', 00000000, 000000, '          ')");

            stmt = con.prepareStatement( sbSql.toString() );
            stmt.setString(1, userID);
            stmt.setString(2, weddingFlag);
            stmt.setString(3, weddingStopDate);
            stmt.setString(4, birthdayFlag);
            stmt.setString(5, birthdayStopDate);
            stmt.setString(6, spouseFlag);
            stmt.setString(7, spouseStopDate);
            stmt.setString(8, Tools.getSystemDate());
            stmt.setString(9, Tools.getSystemTime());

            stmt.executeUpdate();

            this.commonInfo.setExist("1");

        } catch (SQLException e){
            UTBLogManager.writeLog(this.getClass(), e.toString());
            UTBLogManager.writeLog(sbSql.toString());
            throw new WICDBException(MY_METHOD_NAME, "90001", 
                                      e.getMessage(), e.getErrorCode());
        } finally {
            try {
                closeStatement(stmt);
                stmt = null;
            } catch (SQLException ex) {
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                                          ex.getMessage(), ex.getErrorCode());
            }
        }
    }

    /**
     * OPTOUT更新処理.
     *
     * @param con コネクション
     * @throws WICDBException DB接続例外発生時
     */
    private void updateOptOut(Connection con) throws WICDBException {
        final String MY_METHOD_NAME = "WIBOptOutDB::updateOptOut()";

        PreparedStatement stmt = null;
        StringBuffer sbSql = new StringBuffer();

        String userID           = commonInfo.getUserID();
        String weddingFlag      = commonInfo.getWeddingFlag();
        String weddingStopDate  = Tools.addFrontZero(commonInfo.getWeddingStopDate(), 8);
        String birthdayFlag     = commonInfo.getBirthdayFlag();
        String birthdayStopDate = Tools.addFrontZero(commonInfo.getBirthdayStopDate(), 8);
        String spouseFlag       = commonInfo.getSpouseFlag();
        String spouseStopDate   = Tools.addFrontZero(commonInfo.getSpouseStopDate(), 8);

        try{
            sbSql.append ("UPDATE ");
            sbSql.append (getPoolDataLibrary1());
            sbSql.append (".SG0T ");
            sbSql.append ("SET ");
            sbSql.append ("G0WEDG = ?, G0WEDD = ?, G0BART = ?, G0BARD = ?, ");
            sbSql.append ("G0SPOU = ?, G0SPOD = ?, G0JNMX = ?, G0DATX = ?, ");
            sbSql.append ("G0TIMX = ?, G0FUNX = ? ");
            sbSql.append ("WHERE G0SYNO = ? ");

            stmt = con.prepareStatement( sbSql.toString() );
            stmt.setString(1, weddingFlag );
            stmt.setString(2, weddingStopDate );
            stmt.setString(3, birthdayFlag );
            stmt.setString(4, birthdayStopDate );
            stmt.setString(5, spouseFlag );
            stmt.setString(6, spouseStopDate );
            stmt.setString(7, "OPTOUT");
            stmt.setString(8, Tools.getSystemDate());
            stmt.setString(9, Tools.getSystemTime());
            stmt.setString(10, "OPTOUT");
            stmt.setString(11, userID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            UTBLogManager.writeLog(this.getClass(),e.toString());
            UTBLogManager.writeLog(sbSql.toString());
            throw new WICDBException(MY_METHOD_NAME, "90001", 
                                      e.getMessage(), e.getErrorCode());
        } finally {
            try {
                closeStatement(stmt);
                stmt = null;
            } catch (SQLException ex) {
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                                          ex.getMessage(), ex.getErrorCode());
            }
        }
    }

    /**
     * メール送信.
     * <pre>
     * AS/400のCLPGMを呼び出し、メール送信を行う
     * </pre>
     *
     * @throws WICDBException DB接続例外発生時
     */
    private void sendMail() throws WICDBException {
        final String MY_METHOD_NAME = "WIBOptOutDB::sendMail()";

		AS400 as400 = new AS400(getDbSystem(), getPoolDataSourceUser(), getPoolDataSourcePass());
        try {
            ProgramCall pc = new ProgramCall(as400);
            ProgramParameter[] plist = new ProgramParameter[2];

            AS400Text text1 = new AS400Text(22);
            byte[] sForm1 = text1.toBytes(this.commonInfo.getUserName());
            plist[0] = new ProgramParameter(sForm1);
            AS400Text text2 = new AS400Text(6);
            byte[] sForm2 = text2.toBytes(this.commonInfo.getUserID());
            plist[1] = new ProgramParameter(sForm2);

            CommandCall cmd = new CommandCall(as400);
            cmd.run("ADDLIBLE " + getPgmObject());

            QSYSObjectPathName pName = new QSYSObjectPathName(getPgmObject(), "SBTD110UC", "PGM");
            pc.setProgram(pName.getPath(), plist);
            if (pc.run() != true) {
                AS400Message[] msgList = pc.getMessageList();
                System.out.println("The program did not run. MESSAGE");
                for (int i=0; i<msgList.length; i++) {
                    System.out.println(msgList[i].getText());
                }
            }
        } catch (Exception ex) {
            throw new WICDBException(MY_METHOD_NAME, "90001", ex.getMessage(), 0);
        } finally {
        	as400.disconnectService(AS400.COMMAND);
        }
    }
}

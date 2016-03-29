package jp.co.token.optout;

import java.sql.*;
import java.util.ArrayList;

import com.ibm.websphere.ce.cm.StaleConnectionException;

import jp.co.token.optout.util.Tools;
import jp.co.token.optout.util.UTBLogManager;

/**
 * ログインビーン.
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WIBLoginDB extends WIBDataSourceLookup {
    /** 共通セッション情報クラス */
    private WICCommonInfo commonInfo;

    /**
     * コンストラクタ.
     *
     */
    public WIBLoginDB() {
        super();
    }

    /** 
     * 共通セッション共通情報のgetter.
     *
     * @return WICCommonInfo 共通セッション情報
     */
    public WICCommonInfo getCommonInfo() {
        return commonInfo;
    }

    /** 
     * セッション共通情報のsetter.
     *
     * @param commonInfo 共通セッション情報
     */
    public void setCommonInfo(WICCommonInfo commonInfo) {
        this.commonInfo = commonInfo;
    }

    /**
     * ログイン処理.
     *
     * @return 取得結果 (1:パスワードエラー 2:該当ユーザーなし)
     * @throws WICDBException DB接続エラー発生時
     */
    public int executeLogin() throws WICDBException {
        final String MY_METHOD_NAME = "WIBLoginDB::executeLogin()";
        Connection con = null;
        int iRet = 0;
        try {
            con = getConnection();
            con.setAutoCommit(true);

            // 認証チェック
            iRet = passCheckSyain(con);
            if (iRet == 0) {
                // オプトアウト情報の取得
                getOptoutData(con);
            }
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
        return iRet;
    }

    /**
     * ログイン認証処理.
     * <pre>
     * 社員マスタより、ユーザーに対するパスワードを取得し、
     * 入力されたパスワードと一致するかどうかのチェックを行う
     * </pre>
     *
     * @param con DBコネクション
     * @return 取得結果 (1:パスワードエラー 2:該当ユーザーなし)
     * @exception WICDBException DB接続エラー発生時
     */
    private int passCheckSyain(Connection con) throws WICDBException {
        final String MY_METHOD_NAME = "WIBLoginDB::passCheckSyain()";
        int iRet = 0;
        PreparedStatement stmt = null;
        ResultSet rs   = null;
        StringBuffer sbSql = new StringBuffer();
        // ログイン者
        String userId = commonInfo.getUserID();
        String passWord = commonInfo.getPassword();

        try{
            sbSql.append("SELECT ");
            sbSql.append("CP.CPSPSW2, GS.GSKJNM ");
            sbSql.append("FROM ");
            sbSql.append(getPoolDataLibrary2());
            sbSql.append(".CPSMST CP ");
            sbSql.append("INNER JOIN ");
            sbSql.append(getPoolDataLibrary3());
            sbSql.append(".GSHMST GS ");
            sbSql.append("ON CP.CPSUSID = GS.GSSYNO ");
            sbSql.append("WHERE CP.CPSUSID = ? ");
            sbSql.append("AND CP.CPSUSKB = '1'");
            stmt = con.prepareStatement(sbSql.toString());
            stmt.setString(1, userId);

            rs = stmt.executeQuery();

            // ユーザーデータあり
            if (rs.next()){
                String tantouPwd = rs.getString("CPSPSW2");
                if (Tools.null2Blank(tantouPwd).equals("")) {
                    iRet = 3;
                } else if (!Tools.null2Blank(tantouPwd).equals(passWord)) {
                    iRet = 1;
                } else {
                    String tantouNm = Tools.null2Blank(rs.getString("GSKJNM"));
                    commonInfo.setUserID(userId);
                    commonInfo.setPassword(tantouPwd);
                    commonInfo.setUserName(tantouNm);
                }
            } else {
                iRet = 2;
            }
        } catch (SQLException e) {
            UTBLogManager.writeLog(this.getClass(),e.toString());
            UTBLogManager.writeLog(sbSql.toString());
            throw new WICDBException(MY_METHOD_NAME, "90001", 
                                      e.getMessage(),e.getErrorCode());
        } finally {
            try {
                closeResultSet(rs);
                rs = null;
                closeStatement(stmt);
                stmt = null;
            } catch (SQLException ex) {
                throw new WICDBException(MY_METHOD_NAME, "90001",
                                          ex.getMessage(), ex.getErrorCode());
            }
        }
        return iRet;
    }
    /**
     * オプトアウト情報の取得.
     *
     * @param con DBコネクション
     * @throws WICDBException DB例外発生時
     */
    private void getOptoutData(Connection con) throws WICDBException {
        final String MY_METHOD_NAME = "WIBLoginDB::getOptoutData()";
        PreparedStatement stmt = null;
        ResultSet rs   = null;
        StringBuffer sbSql = new StringBuffer();

        String exist = "";
        String userID = "";
        String weddingFlag = "1";
        String weddingStopDate = "00000000";
        String birthdayFlag = "1";
        String birthdayStopDate = "00000000";
        String spouseFlag = "1";
        String spouseStopDate = "00000000";

        // ログイン者
        userID = commonInfo.getUserID();

        try {
            sbSql.append("SELECT ");
            sbSql.append("G0WEDG, G0WEDD, G0BART, G0BARD, ");
            sbSql.append("G0SPOU, G0SPOD ");
            sbSql.append(" FROM  ");
            sbSql.append(getPoolDataLibrary1());
            sbSql.append(".SG0T ");
            sbSql.append("WHERE G0SYNO = ? ");

            stmt = con.prepareStatement(sbSql.toString());
            stmt.setString(1, userID);

            rs = stmt.executeQuery();

            // ユーザーデータあり
            if (rs.next()){
                exist = "1";
                weddingFlag = Tools.null2Blank(rs.getString("G0WEDG"));
                weddingStopDate = Tools.null2Blank(rs.getString("G0WEDD"));
                birthdayFlag = Tools.null2Blank(rs.getString("G0BART"));
                birthdayStopDate = Tools.null2Blank(rs.getString("G0BARD"));
                spouseFlag = Tools.null2Blank(rs.getString("G0SPOU"));
                spouseStopDate = Tools.null2Blank(rs.getString("G0SPOD"));
            }
            commonInfo.setExist(exist);
            commonInfo.setWeddingFlag(weddingFlag);
            commonInfo.setWeddingStopDate(weddingStopDate);
            commonInfo.setBirthdayFlag(birthdayFlag);
            commonInfo.setBirthdayStopDate(birthdayStopDate);
            commonInfo.setSpouseFlag(spouseFlag);
            commonInfo.setSpouseStopDate(spouseStopDate);
        } catch(SQLException e) {
            UTBLogManager.writeLog(this.getClass(),e.toString());
            UTBLogManager.writeLog(sbSql.toString());
            throw new WICDBException(MY_METHOD_NAME, "90001", 
                                      e.getMessage(),e.getErrorCode());
        } finally {
            try {
                closeResultSet(rs);
                rs = null;
                closeStatement(stmt);
                stmt = null;
            } catch (SQLException ex) {
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                                          ex.getMessage(), ex.getErrorCode());
            }
        }
    }
}

/*
 * WISLogin.java
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/17
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    新規作成
 */
package jp.co.token.optout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.token.optout.util.Tools;

/**
 * グリーティングメールオプトアウトログインサーブレット.
 * <pre>
 * ログイン処理サーブレット
 * </pre>
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WISLogin extends WISServlet {
	/** オプトアウト画面 */
	private final static String NEXT_PAGE = "/optout.jsp";
	/** ログイン画面 */
	private final static String ERR_PAGE = "/login.jsp";

    /**
     * ログイン処理 主処理.
     *
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     */
    protected void performTask(HttpServletRequest req, HttpServletResponse res) {
    	final String MY_METHOD_NAME = "WISLogin::performTask()";
        // 初期表示・再表示判断フラグ
        String strLogin = "";

        try {
            // セッション情報取得
            WICCommonInfo commonInfo = checkSession(req);
            // httpパラメータを取得
            String strUserid = Tools.null2Blank(req.getParameter("USER_ID"));
            String strPasswd = Tools.null2Blank(req.getParameter("PASSWORD"));

            // ログイン状況フラグ
            strLogin = Tools.null2Blank((String)getSessionValue("LOGIN_FIRST", req));

            WIBLoginDB loginDB = new WIBLoginDB();

            commonInfo.setUserID(strUserid);
            commonInfo.setPassword(strPasswd);
            // 共通セッション情報
            loginDB.setCommonInfo(commonInfo);

            int ret = loginDB.executeLogin();
            // 共通セッション情報セット
            commonInfo = loginDB.getCommonInfo();
            //共通情報セット
            setSessionValue("COMMONINFO", commonInfo, req);

            // 認証条件すべてクリアの場合
            if (ret == 0) {
                // ログイン状況フラグ
                setSessionValue("LOGIN_FIRST", "1", req);
                // 次画面呼び出し(正常終了)
                callPage(NEXT_PAGE, req, res);
                return;
            } else {
                // アラート表示
                setSessionValue("ERROR_MESSAGE_ALERT", "1", req);
                // パスワード無効
                if (ret == 1) {
                    setSessionValue("ERROR_CODE", "95002", req);
		        // ユーザーＩＤ無効
                } else if (ret == 2) {
                    setSessionValue("ERROR_CODE", "95001", req);
                // パスワードの再設定が必要 
                } else if (ret == 3) {
					setSessionValue("ERROR_CODE", "95005", req);
                }
                // 次画面呼び出し
                callPage(ERR_PAGE, req, res);
                return;
            }
        } catch (WICDBException de) {
            systemErrProc(null, de, req, res);
        } catch (WICSessionException se) {
            sessionErrProc(MY_METHOD_NAME, se, req, res);
        } catch (Exception e) {
            e.printStackTrace();
            systemErrProc(MY_METHOD_NAME, "99999", null, 
                          new Exception("その他のエラー：" + e.getMessage()), 
                          req, res);
        }
    }
}
/*
 * WISLogout.java
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/17
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    新規作成
 * 01.01.00    20050905   SOFTEC D.KAWAKITA    ログアウト画面の廃止
 */
package jp.co.token.optout;

import javax.servlet.http.*;
  
/**
 * グリーティングメールオプトアウトログアウトサーブレット.
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.01.00
 */
public class WISLogout extends WISServlet {
	/** ログアウト画面JSP */
	private final static String NEXT_PAGE = "/Logout.jsp";

	/**
	 * メインリクエスト処理.
	 *
	 * @param req HttpServletRequestオブジェクト
	 * @param res HttpServletResponseオブジェクト
	 */
	protected void performTask(HttpServletRequest req, HttpServletResponse res) {
		final String MY_METHOD_NAME = "WISLogout::performTask()";
		try {
			// セッション情報取得
			WICCommonInfo commonInfo = checkSession(req);

			// セッション情報クリア
			HttpSession ss = null;
			ss = req.getSession(true);
			ss.invalidate();

			// ログアウト画面呼び出し
/* 2005.09.05 delete ----->
			callPage(NEXT_PAGE, req, res);
<----- 2005.09.05 delete */			
			return;
        } catch (WICSessionException e) {
            sessionErrProc(MY_METHOD_NAME, e, req, res);
        } catch (Exception e) {
            systemErrProc(MY_METHOD_NAME, "99999", null, 
                           new Exception("その他のエラー：" + e.getMessage()), 
                           req, res);
        }
    }
}
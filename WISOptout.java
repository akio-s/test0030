/*
 * WISOptout.java
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/17
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    新規作成
 */
package jp.co.token.optout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * グリーティングメールオプトアウト処理サーブレット
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WISOptout extends WISServlet {
	/** オプトアウト画面 */
	private final static String NEXT_PAGE = "/optout.jsp";
	/**
	 * 登録処理 主処理.
	 */
	protected void performTask(HttpServletRequest req, HttpServletResponse res) {
		final String MY_METHOD_NAME = "WISOptout::performTask()";
		try {
			// セッション情報取得
			WICCommonInfo commonInfo = checkSession(req);

			// httpパラメータを取得
			// 結婚祝いメール
			//String weddingFlag = "";
			//String vals1[] = req.getParameterValues("radio1");
			//for(int i = 0; i < vals1.length; ++i){
			//	weddingFlag = vals1[i];
			//}

			// 誕生日メール
			String birthdayFlag = "";
			String vals2[] = req.getParameterValues("radio2");
			for(int i = 0; i < vals2.length; ++i){
				birthdayFlag = vals2[i];
			}
			// 配偶者メール
			//String spouseFlag = "";
			//String vals3[] = req.getParameterValues("radio3");
			//for(int i = 0; i < vals3.length; ++i){
		//		spouseFlag = vals3[i];
			//}

			// commonInfoへのセット
			//commonInfo.setWeddingFlag(weddingFlag);
			commonInfo.setBirthdayFlag(birthdayFlag);
			//commonInfo.setSpouseFlag(spouseFlag);

			WIBOptOutDB outputDB = new WIBOptOutDB();
			// 共通セッション情報
			outputDB.setCommonInfo(commonInfo);

			outputDB.execute();
			//共通情報セット
			commonInfo = outputDB.getCommonInfo();
			setSessionValue("COMMONINFO", commonInfo, req);
			// アラート表示
			setSessionValue("ERROR_MESSAGE_ALERT", "1", req);
			setSessionValue("ERROR_CODE", "95004", req);

			// 次画面呼び出し(正常終了)
			callPage(NEXT_PAGE, req, res);
		} catch (WICDBException e) {
			systemErrProc(null, e, req, res);
		} catch (WICSessionException e) {
			sessionErrProc(MY_METHOD_NAME, e, req, res);
		} catch (Exception e) {
			e.printStackTrace();
			systemErrProc(MY_METHOD_NAME, "99999",
			              null, new Exception("その他のエラー：" +
			              e.getMessage()), req, res);
		}
	}
}
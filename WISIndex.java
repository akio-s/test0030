/*
 * WISIndex.java
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/17
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    新規作成
 * 01.01.00    20050915   SOFTEC D.KAWAKITA    StaleConnectionException対応
 */
package jp.co.token.optout;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.token.optout.util.*;

/**
 * グリーティングメールオプトアウト初期サーブレット.
 * <pre>
 * ログイン前の初期処理サーブレット
 * </pre>
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WISIndex extends WISServlet {
	/** ログイン画面JSP名 */
	private final static String NEXT_PAGE = "/login.jsp";

	/**
	 * メインリクエスト処理.
	 *
	 * @param req HttpServletRequestオブジェクト
	 * @param res HttpServletResponseオブジェクト
	 */
	protected void performTask(HttpServletRequest req, HttpServletResponse res) {
		final String MY_METHOD_NAME = "WISIndex::performTask()";
		try {
			// 初期化処理
			initialize();

			// セッション情報初期セット
			WICCommonInfo commonInfo = new WICCommonInfo();
			initSession(commonInfo, req);
			removeAllSession(req);

			// 環境取得 ｃｏｎｔｅｘｔ
			String strUrl = UTBEnvironment.getUrl();
			if (strUrl == null) {
				UTBLogManager.writeLog("環境取得できませんでした");
				strUrl = "OPTOUT";
			}
			commonInfo.setUrl(strUrl);

			// 実行環境ｈｔｔｐ取得
			String strHostHttp = UTBEnvironment.getHostHttp();
			if (strHostHttp == null) {
				UTBLogManager.writeLog("実行環境ｈｔｔｐを取得できませんでした");
				strHostHttp = "http://IS400P2.token.co.jp/";
			}
			commonInfo.setHostHttp(strHostHttp);

			// 実行環境ｕｒｌ取得
			String strHostUrl = UTBEnvironment.getHostUrl();
			if (strHostUrl == null){
				UTBLogManager.writeLog("実行環境ｕｒｌを取得できませんでした");
				strHostHttp = "/QIBM/UserData/WebAS51/Base/TS_CALL/installedApps/IS400P2_TS_CALL/SuccessEAR.ear/SuccessEARWeb.war/";
			}

			commonInfo.setHostUrl(strHostUrl);
			commonInfo.setUserID("");
			// 共通セッション情報セット
			setSessionValue("COMMONINFO", commonInfo, req);

			// その他各セッション情報のセット
			setSessionValue("LOGIN_FIRST", "0", req);
			setSessionValue("ERROR_MESSAGE_ALERT", "0", req);

// 2005.09.15 add =====>
			WIBLoginDB loginDB = new WIBLoginDB();
			// 共通セッション情報
			loginDB.setCommonInfo(commonInfo);
			if (!loginDB.executeDummy()) {
                UTBLogManager.writeLog("executeDummyでエラー");
            }
// 2005.09.15 add <=====

            // メニュー画面呼び出し
            callPage(NEXT_PAGE, req, res);
            return;
        } catch (WICDBException de) {
            systemErrProc(null, de, req, res);
        } catch (WICSessionException se) {
            sessionErrProc(MY_METHOD_NAME, se, req, res);
        } catch (Exception e) {
            systemErrProc(MY_METHOD_NAME, "99999", null, 
            new Exception("その他のエラー：" + e.getMessage()), req, res);
        }

    }

    /**
     * 各ユーティルの初期化処理.
     * <pre>
     * システムプロパティ、メッセージプロパティを読込み
     * 各値の取得をし、実行環境を整える
     * </pre>
     *
     * @exception WICDBException 各システム情報が正常に取得できなかった場合
     */
    public void initialize() throws WICDBException {
        final String MY_METHOD_NAME = "WISIndex:initialize()";
        try {
            //メッセージローダーを初期化する。
            String result = "";
            result = UTBMessageLoder.initialize();
            if (result.trim().length() != 0) {
                UTBMessageLoder.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", result, 0);
            }
            //ログマネージャーを初期化する。
            result = UTBLogManager.startLog();
            if (result.trim().length() != 0){
                UTBMessageLoder.setNullInstance();
                UTBLogManager.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", result, 0);
            }
            //データソースを取得する。
            WIBDataSourceLookup dsl = new WIBDataSourceLookup();
            if (!dsl.initializeDataSource()){
                UTBMessageLoder.setNullInstance();
                UTBLogManager.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                                  "データソースを取得できませんでした。", 0);
            }
            //URL情報を取得する。
            UTBEnvironment env = new UTBEnvironment();
            if (!env.initializeEnvironment()){
                UTBMessageLoder.setNullInstance();
                UTBLogManager.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                    "ＵＲＬを取得できませんでした。", 0);
            }
        } catch (Exception e) {
            throw new WICDBException(MY_METHOD_NAME, "90001", "その他エラー", 0);
        }
    }
}
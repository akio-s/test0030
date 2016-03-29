/*
 * WISServlet.java
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/17
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    新規作成
 */
package jp.co.token.optout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * ベースサーブレット.
 * <pre>
 * 各サーブレットのベースとなるクラス
 * 全てのサーブレットはこのクラスを継承する
 * </pre>
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public abstract class WISServlet extends HttpServlet {
	/** システムエラーJSP名 */
	private final static String SYSTEMERR_PAGE = "/SystemErr.jsp";
    /** セッションエラーJSP名 */
    private final static String SESSIONERR_PAGE = "/SessionErr.jsp";

	/**
	 * サーブレット初期化処理.
	 *
	 * @param config サーブレットコンフィグ
	 * @exception ServletException サーブレット例外発生時
	 */
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		ServletContext sc = getServletContext();
		// 各サーブレットでの初期処理
		doInit();
	}

	/**
	 * 初期処理.
     *
      * <pre>
     * 各サーブレットで初期処理を実行したい場合は
     * このメソッドを実装する
     * </pre>
     */
    protected void doInit() { 
    }

    /**
     * JSP呼び出し処理.
     * 
     * @param page 呼び出しJSP名
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     */
    protected void callPage(String page, HttpServletRequest req, 
                          HttpServletResponse res) {
        try {
            req.setCharacterEncoding("Shift_JIS");
            res.setContentType("text/html; charset=Shift_JIS");
            getServletContext().getRequestDispatcher(page).forward(req, res);
        } catch (Exception e) {
            try {
                WICCommonInfo commonInfo = checkSession( req );
                systemErrProc("WISServlet::callPage()", "90000", commonInfo, 
                  new Exception("Cannot call the page \"" + page + "\"" + 
                                e.getMessage()), req, res);
            } catch (WICSessionException ex) {
                sessionErrProc("WISServlet::callPage()", ex, req, res);
            }
        }
    }

    /**
     * セッション情報チェック処理.
     * <pre>
     * セッションオブジェクトより、保存してある共通セッション情報を取得する。
     * </pre>
     *
     * @param req HttpServletRequestオブジェクト
     * @exception WICSessionException セッション情報が存在しない場合
     */
    protected WICCommonInfo checkSession(HttpServletRequest req) 
                          throws WICSessionException {
        WICCommonInfo commonInfo = null;
        // セッションオブジェクト取得
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.err.println("★------checkSession::セッション例外発生:time over");
            throw new WICSessionException();
        }
        // 共通セッション情報オブジェクト取得
        commonInfo = (WICCommonInfo)session.getAttribute("COMMONINFO");
        if (commonInfo == null) {
            System.err.println("★------checkSession::セッション例外発生(commonInfo):time over");
            throw new WICSessionException();
        }
        setSessionValue("ERROR_MESSAGE_ALERT", "", req);
        setSessionValue("ERROR_CODE", "", req);
        return commonInfo;
    }

    /**
     * doGet().
     *
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     * @exception ServletException サーブレット例外発生時
     * @exception IOException IO例外発生時
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) 
                 throws ServletException, IOException {
        try {
            req.setCharacterEncoding("Shift_JIS");
            res.setContentType("text/html;charset=Shift_JIS");
            performTask(req, res);
        } catch (Exception e) {
            systemErrProc("WISServlet::doGet()", "99999", null, 
                          new Exception("その他のエラー：" + e.getMessage()), 
                          req, res);
        }
    }

    /**
     * doPost().
     *
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     * @exception ServletException サーブレット例外発生時
     * @exception IOException IO例外発生時
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
                 throws ServletException, IOException {
        try {
            req.setCharacterEncoding("Shift_JIS");
            res.setContentType("text/html;charset=Shift_JIS");
            performTask(req, res);
        } catch (Exception e) {
            systemErrProc("WISServlet::doPost()", "99999", null, 
                          new Exception("その他のエラー：" + e.getMessage()), 
                          req, res);
        }
    }

    /**
     * システム日付取得(yyyy.MM.dd HH:mm:ss).
     * 
     * @return システム日付.
     */
    private String getDateString() {
        SimpleDateFormat format_date = 
                         new SimpleDateFormat("yyyy.MM.dd HH:mm:ss ");
        String dateString = format_date.format(new Date());
        return dateString;
    }

    /**
     * セッション項目取得処理.
     * <pre>
     * セッション情報として保持している項目名を取得する
     * </pre>
     *
     * @param req HttpServletRequestオブジェクト
     * @return セッション項目名リスト
     * @exception WICSessionException セッション情報がNULLの場合
     */
    protected String[] getSessionKeys(HttpServletRequest req) 
                     throws WICSessionException {
        // セッションオブジェクト取得
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new WICSessionException();
        }

        String strRet[];
        ArrayList aRet = new ArrayList();
        Enumeration e = session.getAttributeNames();
        while (e.hasMoreElements()) {
               aRet.add((String)e.nextElement());
        }

        strRet = new String[aRet.size()];
        for (int i=0; i<aRet.size(); i++) {
            strRet[i] = (String)aRet.get(i);
        }

        return strRet;
    }

    /**
     * セッション情報取得処理.
     * <pre>
     * 引数で受け取ったKEYでセッション情報の値を取得する
     * </pre>
     *
     * @param key 取得するセッション情報のKEY
     * @param req HttpServletRequestオブジェクト
     * @return セッション情報値
     * @exception WICSessionException セッション情報がNULLの場合
     */
    protected Object getSessionValue(String key, HttpServletRequest req) 
                   throws WICSessionException {
        // セッションオブジェクト取得
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.err.println("★------getSessionValue::セッション例外発生");
            throw new WICSessionException();
        }
        return session.getAttribute(key);
    }

    /**
     * セッション情報初期化処理.
     * <pre>
     * セッション情報に共通セッション情報である
     * "COMMONINFO"の値をセットする
     * </pre>
     *
     * @param commonInfo 共通セッション情報
     * @param req HttpServletRequestオブジェクト
     * @exception WICSessionException セッション情報がNULLの場合
     */
    protected void initSession(WICCommonInfo commonInfo, HttpServletRequest req)
                 throws WICSessionException {
        // セッションオブジェクト取得
        HttpSession session = req.getSession(true);
        if (session == null) {
            System.err.println("★------getSessionValue::セッション例外発生");
            throw new WICSessionException();
        }
        session.setAttribute("COMMONINFO", commonInfo);
    }

    /**
     * エラーログ出力処理.
     * 
     * @param dateString システム日付
     * @param err_no システムエラー№
     * @param cust_id ログインユーザー会社コード
     * @param user_id ログインユーザーＩＤ
     * @param location エラー発生場所
     * @param message エラーメッセージ
     */
    private void outputLog(String dateString, String err_no, String cust_id, 
                            String user_id, String location, String message) {
        System.out.println(dateString + err_no + " " + cust_id + 
                            " " + user_id + " " + location + " " + message);
    }

    /**
     * メインリクエスト処理.
     *
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     */
    protected abstract void performTask(HttpServletRequest req, 
                                           HttpServletResponse res);

    /**
     * COMMONINFO以外のセッション情報を全て削除する.
     *
     * @param req HttpServletRequestオブジェクト
     * @exception WICSessionException セッション情報がNULLの場合
     */
    protected void removeAllSession(HttpServletRequest req) 
                 throws WICSessionException {
        HttpSession session = (HttpSession)req.getSession(false);
        if (session == null) {
            System.err.println("★------getSessionValue::セッション例外発生");
            throw new WICSessionException();
        }

        String[] sv = getSessionKeys(req);
        int i = 0;
        if (sv != null){
            while (i < sv.length){
                String key = (String)sv[i];
                if (key != null && key.equals("COMMONINFO")) {
                    i++;
                    continue;
                }
                removeSessionValue(key, req);
                i++;
            }
        }
    }

    /**
     * セッション情報削除処理.
     * <pre>
     * 引数で渡されたKEYでセッション情報を削除する
     * </pre>
     *
     * @param key セッション情報KEY
     * @param req HttpServletRequestオブジェクト
     * @exception WICSessionException セッション情報がNULLの場合
     */
    protected void removeSessionValue(String key, HttpServletRequest req) 
                     throws WICSessionException {
        HttpSession session = req.getSession( false );
        if (session == null) {
            System.err.println("★------getSessionValue::セッション例外発生");
            throw new WICSessionException();
        }
        session.removeAttribute( key );
    }

    /**
     * セッションエラー処理.
     * <pre>
     * セッション例外発生時にシステムアウトを出力する
     * </pre>
     *
     * @param location 例外発生場所(クラス：メソッド） 
     * @param ex セッション例外オブジェクト
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     */
    protected void sessionErrProc(String location, WICSessionException ex, 
                                    HttpServletRequest req, 
                                    HttpServletResponse res) {
        outputLog(getDateString(), "0", null, null, location, ex.getMessage());
        try {
            // セッション例外ページへ
            getServletContext().getRequestDispatcher(SESSIONERR_PAGE).forward(req, res);
        } catch (Exception e) {
            System.out.println(getDateString() + 
                                "sessionErrProc() can not call sessionErr.jsp: " + 
                                e.toString());
        }
    }

    /**
     * セッション情報設定処理.
     * <pre>
     * セッション情報に、指定されたKEYでオブジェクトを格納する
     * </pre>
     *
     * @param key セッション情報KEY
     * @param obj セッション情報に格納するオブジェクト
     * @param req HttpServletRequestオブジェクト
     * @exception WICSessionException セッション情報がNULLの場合
     */
    protected void setSessionValue(String key, Object obj, 
                                     HttpServletRequest req)
                     throws WICSessionException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.err.println("★------getSessionValue::セッション例外発生");
            throw new WICSessionException();
        }
        session.setAttribute(key, obj);
    }

    /**
     * システムエラー処理.
     * 
     * @param location エラー発生場所(クラス：メソッド)
     * @param err_no システムエラー№
     * @param commonInfo 共通セッション情報オブジェクト
     * @param ex 例外オブジェクト
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     */
    protected void systemErrProc(String location, String err_no, 
                                   WICCommonInfo commonInfo,Exception ex, 
                                   HttpServletRequest req, 
                                   HttpServletResponse res) {
        req.setAttribute("ERRMSG", ex.toString());

        if (commonInfo == null) {
            outputLog(getDateString(), err_no, null, null, location, ex.getMessage());
        } else {
            outputLog(getDateString(), err_no, "", commonInfo.getUserID(), 
                      location, ex.getMessage());
        }

        try {
            getServletContext().getRequestDispatcher(SYSTEMERR_PAGE).forward(req, res);
        } catch (Exception e) {
            System.out.println(getDateString() + 
                        "systemErrProc() can not call SystemErr.jsp: " + 
                        e.toString());
        }
    }

    /**
     * システムエラー処理.
     * 
     * @param commonInfo 共通セッション情報オブジェクト
     * @param ex 例外オブジェクト
     * @param req HttpServletRequestオブジェクト
     * @param res HttpServletResponseオブジェクト
     */
    protected void systemErrProc(WICCommonInfo commonInfo, WICDBException ex, 
                                   HttpServletRequest req, 
                                   HttpServletResponse res) {
        req.setAttribute("ERRMSG", ex.toString());
        if (commonInfo == null) {
            outputLog(getDateString(), ex.getErr_no(), null, null, 
                    ex.getLocation(), ex.getMessage());
        } else {
            outputLog(getDateString(), ex.getErr_no(), "",
              commonInfo.getUserID(), ex.getLocation(), ex.getMessage());
        }

        try {
            getServletContext().getRequestDispatcher(SYSTEMERR_PAGE).forward(req, res);
        } catch (Exception e) {
            System.out.println(getDateString() + 
                            "systemErrProc() can not call SystemErr.jsp: " + 
                            e.toString());
        }
    }
}
/*
 * WICSessionException.java
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/16
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050816   SOFTEC D.KAWAKITA    新規作成
 */
package jp.co.token.optout; 

/**
 * セッション例外クラス
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WICSessionException extends Exception {
    /** セッション例外メッセージ */
    private final static String msg = "セッションオブジェクトから共通情報を取得できませんでした";

    /**
     * コンストラクタ.
     *
     */
    public WICSessionException() {
        super(msg);
    }

    /**
     * エラーメッセージのgetter.
     *
     * @return エラーメッセージ
     */
    public String getMessage() {
        return msg;
    }
}
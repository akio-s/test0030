/*
 * WICDBException.java
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/16
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050816   SOFTEC D.KAWAKITA    新規作成
 */
package jp.co.token.optout;

/**
 * ＤＢ例外クラス
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WICDBException extends Exception {
	/** 例外メッセージ */
	private String msg;
	/** 例外発生場所 */
	private String location;
	/** エラー番号 */
    private String err_no;

    /**
     * コンストラクタ.
     * <pre>
     * SQL状態、理由を添えてWICDBExceptionを定義.
     * SQLException発生時に例外が発生した場合、その時のSQLエラーコードを
     * エラーメッセージに添えてWICDBExceptionを作成する
     * </pre>
     *
     * @param location 例外発生場所
     * @param err_no エラー番号
     * @param msg 例外メッセージ
     * @param err_code ＤＢエラー発生時にＤＢＭＳが返すエラーコード(内部エラー発生時は 0を設定)
       */
    public WICDBException(String location, String err_no, String msg, int err_code) {
        super(msg);
        this.location = location;
        this.err_no = err_no; 
        StringBuffer str = new StringBuffer();
        if (err_code != 0) {
            str.append(err_code);
            str.append(msg);
        } else {
            str.append(msg);
        }
        this.msg = str.toString();
    }

    /**
     * 例外メッセージのgetter.
     *
     * @return エラーメッセージ
     */
    public String getMessage() {
        return msg;
    }

    /**
     * エラー番号のgetter.
     *
     * @return エラー番号
     */
    public String getErr_no() {
        return err_no;
    }

    /**
     * エラー発生場所のgetter.
     *
     * @return エラー発生場所
     */
    public String getLocation() {
        return location;
    }
}
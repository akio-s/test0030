/*
 * WICCommonInfo
 *
 * 作成者      : SOFTEC D.KAWAKITA
 * 作成日      : 2005/08/16
 * 更新履歴    更新日     担当者               内容
 * 01.00.00    20050816   SOFTEC D.KAWAKITA    新規作成
 */
package jp.co.token.optout;

import jp.co.token.optout.util.Tools;

/**
 * 共通セッション情報クラス
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WICCommonInfo {
	/** optout存在FLAG */
    private String exist;
    /** ユーザーＩＤ */
    private String userID;
    /** ユーザー名 */
    private String userName;
    /** パスワード */
    private String password;
    /** 結婚記念日指示フラグ */
    private String weddingFlag;
    /** 結婚記念日配信停止指示日 */
    private String weddingStopDate;
    /** 誕生日指示フラグ */
    private String birthdayFlag;
    /** 誕生日配信停止指示日 */
    private String birthdayStopDate;
    /** 配偶者誕生日指示フラグ */
    private String spouseFlag;
    /** 配偶者誕生日配信停止指示日 */
    private String spouseStopDate;

    /** 実行環境http */
    private String hostHttp;
    /** 実行環境URL */
    private String hostUrl;
    /** 実行環境コンテキスト */
    private String url;

    /**
     * コンストラクタ.
     *
     */
    public WICCommonInfo(){                
        super();
    }

    /**
     * optout存在FLAGのsetter.
     *
     * @param isExsit optout存在FLAG
     */
    public void setExist(String exist) {
        this.exist = exist;
    }

    /**
     * optout存在FLAGのgetter.
     *
     * @return optout存在FLAG
     */
    public String getExist() {
        return this.exist;
    }

    /**
     * ユーザーＩＤのsetter.
     *
     * @param userID ユーザーＩＤ
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * ユーザーＩＤのgetter.
     *
     * @return ユーザーＩＤ
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * ユーザー名のsetter.
     *
     * @param userName ユーザー名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * ユーザー名のgetter.
     *
     * @return ユーザー名
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * パスワードのsetter.
     *
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * パスワードのgetter.
     *
     * @return パスワード
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 結婚記念日指示フラグのsetter.
     *
     * @param weddingFlag 結婚記念日指示フラグ
     */
    public void setWeddingFlag(String weddingFlag) {
        this.weddingFlag = weddingFlag;
        if (weddingFlag.equals("1")) {
            setWeddingStopDate("00000000");
        }
    }

    /**
     * 結婚記念日指示フラグのgetter.
     *
     * @return 結婚記念日指示フラグ
     */
    public String getWeddingFlag() {
        return this.weddingFlag;
    }

    /**
     * 結婚記念日配信停止指示日のsetter.
     *
     * @param weddingStopDate 結婚記念日配信停止指示日
     */
    public void setWeddingStopDate(String weddingStopDate) {
        // null
        if (weddingStopDate == null) {
            this.weddingStopDate = "00000000";
            return;
        }
        // 配信状態
        if (getWeddingFlag().equals("1")) {
            this.weddingStopDate = "00000000";
            return;
        }
        // 配信停止状態
        this.weddingStopDate = weddingStopDate;
    }

    /**
     * 結婚記念日配信停止指示日のgetter.
     *
     * @return 結婚記念日配信停止指示日
     */
    public String getWeddingStopDate() {
        // 配信状態
        if (getWeddingFlag().equals("1")) {
            return "00000000";
        } else {
            if (this.weddingStopDate.equals("00000000")) {
                return Tools.getSystemDate();
            } else {
                return this.weddingStopDate;
            }
        }
    }

    /**
     * 誕生日指示フラグのsetter.
     *
     * @param birthdayFlag 誕生日指示フラグ
     */
    public void setBirthdayFlag(String birthdayFlag) {
        this.birthdayFlag = birthdayFlag;
        if (birthdayFlag.equals("1")) {
            setBirthdayStopDate("00000000");
        }
    }

    /**
     * 誕生日指示フラグのgetter.
     *
     * @return 誕生日指示フラグ
     */
    public String getBirthdayFlag() {
        return this.birthdayFlag;
    }

    /**
     * 誕生日配信停止指示日のsetter.
     *
     * @param birthdayStopDate 誕生日配信停止指示日
     */
    public void setBirthdayStopDate(String birthdayStopDate) {
        // null
        if (birthdayStopDate == null) {
            this.birthdayStopDate = "00000000";
            return;
        }
        // 配信状態
        if (getBirthdayFlag().equals("1")) {
            this.birthdayStopDate = "00000000";
            return;
        }
        // 配信停止状態
        this.birthdayStopDate = birthdayStopDate;
    }

    /**
     * 誕生日配信停止指示日のgetter.
     *
     * @return 誕生日配信停止指示日
     */
    public String getBirthdayStopDate() {
        // 配信状態
        if (getBirthdayFlag().equals("1")) {
            return "00000000";
        } else {
            if (this.birthdayStopDate.equals("00000000")) {
                return Tools.getSystemDate();
            } else {
                return this.birthdayStopDate;
            }
        }
    }

    /**
     * 配偶者誕生日指示フラグのsetter.
     *
     * @param spouseFlag 配偶者誕生日指示フラグ
     */
    public void setSpouseFlag(String spouseFlag) {
        this.spouseFlag = spouseFlag;
        if (spouseFlag.equals("1")) {
            setSpouseStopDate("00000000");
        }
    }

    /**
     * 配偶者誕生日指示フラグのgetter.
     *
     * @return 配偶者誕生日指示フラグ
     */
    public String getSpouseFlag() {
        return this.spouseFlag;
    }

    /**
     * 配偶者誕生日配信停止指示日のsetter.
     *
     * @param spouseStopDate 配偶者誕生日配信停止指示日
     */
    public void setSpouseStopDate(String spouseStopDate) {
        // null
        if (spouseStopDate == null) {
            this.spouseStopDate = "00000000";
            return;
        }
        // 配信状態
        if (getSpouseFlag().equals("1")) {
            this.spouseStopDate = "00000000";
            return;
        }
        // 配信停止状態
        this.spouseStopDate = spouseStopDate;
    }

    /**
     * 配偶者誕生日配信停止指示日のgetter.
     *
     * @return 配偶者誕生日配信停止指示日
     */
    public String getSpouseStopDate() {
        // 配信状態
        if (getSpouseFlag().equals("1")) {
            return "00000000";
        } else {
            if (this.spouseStopDate.equals("00000000")) {
                return Tools.getSystemDate();
            } else {
                return this.spouseStopDate;
            }
        }
    }

    /**
     * 実行環境httpのsetter.
     *
     * @param hosthttp 実行環境http
     */
    public void setHostHttp(String hostHttp) {
        this.hostHttp = hostHttp;
    }

    /**
     * 実行環境httpのgetter.
     *
     * @return 実行環境http
     */
    public String getHostHttp() {
        return this.hostHttp;
    }

    /**
     * 実行環境URLのsetter.
     *
     * @param hostUrl 実行環境URL
     */
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    /**
     * 実行環境URLのgetter.
     *
     * @return 実行環境URL
     */
    public String getHostUrl() {
        return this.hostUrl;
    }

    /**
     * 実行環境コンテキストのsetter.
     *
     * @param url 実行環境コンテキスト
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 実行環境コンテキストのgetter.
     *
     * @return 実行環境コンテキスト
     */
    public String getUrl() {
        return this.url;
    }
}
/*
 * WICCommonInfo
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/16
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050816   SOFTEC D.KAWAKITA    �V�K�쐬
 */
package jp.co.token.optout;

import jp.co.token.optout.util.Tools;

/**
 * ���ʃZ�b�V�������N���X
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WICCommonInfo {
	/** optout����FLAG */
    private String exist;
    /** ���[�U�[�h�c */
    private String userID;
    /** ���[�U�[�� */
    private String userName;
    /** �p�X���[�h */
    private String password;
    /** �����L�O���w���t���O */
    private String weddingFlag;
    /** �����L�O���z�M��~�w���� */
    private String weddingStopDate;
    /** �a�����w���t���O */
    private String birthdayFlag;
    /** �a�����z�M��~�w���� */
    private String birthdayStopDate;
    /** �z��Ғa�����w���t���O */
    private String spouseFlag;
    /** �z��Ғa�����z�M��~�w���� */
    private String spouseStopDate;

    /** ���s��http */
    private String hostHttp;
    /** ���s��URL */
    private String hostUrl;
    /** ���s���R���e�L�X�g */
    private String url;

    /**
     * �R���X�g���N�^.
     *
     */
    public WICCommonInfo(){                
        super();
    }

    /**
     * optout����FLAG��setter.
     *
     * @param isExsit optout����FLAG
     */
    public void setExist(String exist) {
        this.exist = exist;
    }

    /**
     * optout����FLAG��getter.
     *
     * @return optout����FLAG
     */
    public String getExist() {
        return this.exist;
    }

    /**
     * ���[�U�[�h�c��setter.
     *
     * @param userID ���[�U�[�h�c
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * ���[�U�[�h�c��getter.
     *
     * @return ���[�U�[�h�c
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * ���[�U�[����setter.
     *
     * @param userName ���[�U�[��
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * ���[�U�[����getter.
     *
     * @return ���[�U�[��
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * �p�X���[�h��setter.
     *
     * @param password �p�X���[�h
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * �p�X���[�h��getter.
     *
     * @return �p�X���[�h
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * �����L�O���w���t���O��setter.
     *
     * @param weddingFlag �����L�O���w���t���O
     */
    public void setWeddingFlag(String weddingFlag) {
        this.weddingFlag = weddingFlag;
        if (weddingFlag.equals("1")) {
            setWeddingStopDate("00000000");
        }
    }

    /**
     * �����L�O���w���t���O��getter.
     *
     * @return �����L�O���w���t���O
     */
    public String getWeddingFlag() {
        return this.weddingFlag;
    }

    /**
     * �����L�O���z�M��~�w������setter.
     *
     * @param weddingStopDate �����L�O���z�M��~�w����
     */
    public void setWeddingStopDate(String weddingStopDate) {
        // null
        if (weddingStopDate == null) {
            this.weddingStopDate = "00000000";
            return;
        }
        // �z�M���
        if (getWeddingFlag().equals("1")) {
            this.weddingStopDate = "00000000";
            return;
        }
        // �z�M��~���
        this.weddingStopDate = weddingStopDate;
    }

    /**
     * �����L�O���z�M��~�w������getter.
     *
     * @return �����L�O���z�M��~�w����
     */
    public String getWeddingStopDate() {
        // �z�M���
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
     * �a�����w���t���O��setter.
     *
     * @param birthdayFlag �a�����w���t���O
     */
    public void setBirthdayFlag(String birthdayFlag) {
        this.birthdayFlag = birthdayFlag;
        if (birthdayFlag.equals("1")) {
            setBirthdayStopDate("00000000");
        }
    }

    /**
     * �a�����w���t���O��getter.
     *
     * @return �a�����w���t���O
     */
    public String getBirthdayFlag() {
        return this.birthdayFlag;
    }

    /**
     * �a�����z�M��~�w������setter.
     *
     * @param birthdayStopDate �a�����z�M��~�w����
     */
    public void setBirthdayStopDate(String birthdayStopDate) {
        // null
        if (birthdayStopDate == null) {
            this.birthdayStopDate = "00000000";
            return;
        }
        // �z�M���
        if (getBirthdayFlag().equals("1")) {
            this.birthdayStopDate = "00000000";
            return;
        }
        // �z�M��~���
        this.birthdayStopDate = birthdayStopDate;
    }

    /**
     * �a�����z�M��~�w������getter.
     *
     * @return �a�����z�M��~�w����
     */
    public String getBirthdayStopDate() {
        // �z�M���
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
     * �z��Ғa�����w���t���O��setter.
     *
     * @param spouseFlag �z��Ғa�����w���t���O
     */
    public void setSpouseFlag(String spouseFlag) {
        this.spouseFlag = spouseFlag;
        if (spouseFlag.equals("1")) {
            setSpouseStopDate("00000000");
        }
    }

    /**
     * �z��Ғa�����w���t���O��getter.
     *
     * @return �z��Ғa�����w���t���O
     */
    public String getSpouseFlag() {
        return this.spouseFlag;
    }

    /**
     * �z��Ғa�����z�M��~�w������setter.
     *
     * @param spouseStopDate �z��Ғa�����z�M��~�w����
     */
    public void setSpouseStopDate(String spouseStopDate) {
        // null
        if (spouseStopDate == null) {
            this.spouseStopDate = "00000000";
            return;
        }
        // �z�M���
        if (getSpouseFlag().equals("1")) {
            this.spouseStopDate = "00000000";
            return;
        }
        // �z�M��~���
        this.spouseStopDate = spouseStopDate;
    }

    /**
     * �z��Ғa�����z�M��~�w������getter.
     *
     * @return �z��Ғa�����z�M��~�w����
     */
    public String getSpouseStopDate() {
        // �z�M���
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
     * ���s��http��setter.
     *
     * @param hosthttp ���s��http
     */
    public void setHostHttp(String hostHttp) {
        this.hostHttp = hostHttp;
    }

    /**
     * ���s��http��getter.
     *
     * @return ���s��http
     */
    public String getHostHttp() {
        return this.hostHttp;
    }

    /**
     * ���s��URL��setter.
     *
     * @param hostUrl ���s��URL
     */
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    /**
     * ���s��URL��getter.
     *
     * @return ���s��URL
     */
    public String getHostUrl() {
        return this.hostUrl;
    }

    /**
     * ���s���R���e�L�X�g��setter.
     *
     * @param url ���s���R���e�L�X�g
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * ���s���R���e�L�X�g��getter.
     *
     * @return ���s���R���e�L�X�g
     */
    public String getUrl() {
        return this.url;
    }
}
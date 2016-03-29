/*
 * WISIndex.java
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/17
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    �V�K�쐬
 * 01.01.00    20050915   SOFTEC D.KAWAKITA    StaleConnectionException�Ή�
 */
package jp.co.token.optout;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.token.optout.util.*;

/**
 * �O���[�e�B���O���[���I�v�g�A�E�g�����T�[�u���b�g.
 * <pre>
 * ���O�C���O�̏��������T�[�u���b�g
 * </pre>
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WISIndex extends WISServlet {
	/** ���O�C�����JSP�� */
	private final static String NEXT_PAGE = "/login.jsp";

	/**
	 * ���C�����N�G�X�g����.
	 *
	 * @param req HttpServletRequest�I�u�W�F�N�g
	 * @param res HttpServletResponse�I�u�W�F�N�g
	 */
	protected void performTask(HttpServletRequest req, HttpServletResponse res) {
		final String MY_METHOD_NAME = "WISIndex::performTask()";
		try {
			// ����������
			initialize();

			// �Z�b�V������񏉊��Z�b�g
			WICCommonInfo commonInfo = new WICCommonInfo();
			initSession(commonInfo, req);
			removeAllSession(req);

			// ���擾 ��������������
			String strUrl = UTBEnvironment.getUrl();
			if (strUrl == null) {
				UTBLogManager.writeLog("���擾�ł��܂���ł���");
				strUrl = "OPTOUT";
			}
			commonInfo.setUrl(strUrl);

			// ���s�����������擾
			String strHostHttp = UTBEnvironment.getHostHttp();
			if (strHostHttp == null) {
				UTBLogManager.writeLog("���s�������������擾�ł��܂���ł���");
				strHostHttp = "http://IS400P2.token.co.jp/";
			}
			commonInfo.setHostHttp(strHostHttp);

			// ���s���������擾
			String strHostUrl = UTBEnvironment.getHostUrl();
			if (strHostUrl == null){
				UTBLogManager.writeLog("���s�����������擾�ł��܂���ł���");
				strHostHttp = "/QIBM/UserData/WebAS51/Base/TS_CALL/installedApps/IS400P2_TS_CALL/SuccessEAR.ear/SuccessEARWeb.war/";
			}

			commonInfo.setHostUrl(strHostUrl);
			commonInfo.setUserID("");
			// ���ʃZ�b�V�������Z�b�g
			setSessionValue("COMMONINFO", commonInfo, req);

			// ���̑��e�Z�b�V�������̃Z�b�g
			setSessionValue("LOGIN_FIRST", "0", req);
			setSessionValue("ERROR_MESSAGE_ALERT", "0", req);

// 2005.09.15 add =====>
			WIBLoginDB loginDB = new WIBLoginDB();
			// ���ʃZ�b�V�������
			loginDB.setCommonInfo(commonInfo);
			if (!loginDB.executeDummy()) {
                UTBLogManager.writeLog("executeDummy�ŃG���[");
            }
// 2005.09.15 add <=====

            // ���j���[��ʌĂяo��
            callPage(NEXT_PAGE, req, res);
            return;
        } catch (WICDBException de) {
            systemErrProc(null, de, req, res);
        } catch (WICSessionException se) {
            sessionErrProc(MY_METHOD_NAME, se, req, res);
        } catch (Exception e) {
            systemErrProc(MY_METHOD_NAME, "99999", null, 
            new Exception("���̑��̃G���[�F" + e.getMessage()), req, res);
        }

    }

    /**
     * �e���[�e�B���̏���������.
     * <pre>
     * �V�X�e���v���p�e�B�A���b�Z�[�W�v���p�e�B��Ǎ���
     * �e�l�̎擾�����A���s���𐮂���
     * </pre>
     *
     * @exception WICDBException �e�V�X�e����񂪐���Ɏ擾�ł��Ȃ������ꍇ
     */
    public void initialize() throws WICDBException {
        final String MY_METHOD_NAME = "WISIndex:initialize()";
        try {
            //���b�Z�[�W���[�_�[������������B
            String result = "";
            result = UTBMessageLoder.initialize();
            if (result.trim().length() != 0) {
                UTBMessageLoder.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", result, 0);
            }
            //���O�}�l�[�W���[������������B
            result = UTBLogManager.startLog();
            if (result.trim().length() != 0){
                UTBMessageLoder.setNullInstance();
                UTBLogManager.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", result, 0);
            }
            //�f�[�^�\�[�X���擾����B
            WIBDataSourceLookup dsl = new WIBDataSourceLookup();
            if (!dsl.initializeDataSource()){
                UTBMessageLoder.setNullInstance();
                UTBLogManager.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                                  "�f�[�^�\�[�X���擾�ł��܂���ł����B", 0);
            }
            //URL�����擾����B
            UTBEnvironment env = new UTBEnvironment();
            if (!env.initializeEnvironment()){
                UTBMessageLoder.setNullInstance();
                UTBLogManager.setNullInstance();
                throw new WICDBException(MY_METHOD_NAME, "90001", 
                    "�t�q�k���擾�ł��܂���ł����B", 0);
            }
        } catch (Exception e) {
            throw new WICDBException(MY_METHOD_NAME, "90001", "���̑��G���[", 0);
        }
    }
}
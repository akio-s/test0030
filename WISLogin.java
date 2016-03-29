/*
 * WISLogin.java
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/17
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    �V�K�쐬
 */
package jp.co.token.optout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.token.optout.util.Tools;

/**
 * �O���[�e�B���O���[���I�v�g�A�E�g���O�C���T�[�u���b�g.
 * <pre>
 * ���O�C�������T�[�u���b�g
 * </pre>
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WISLogin extends WISServlet {
	/** �I�v�g�A�E�g��� */
	private final static String NEXT_PAGE = "/optout.jsp";
	/** ���O�C����� */
	private final static String ERR_PAGE = "/login.jsp";

    /**
     * ���O�C������ �又��.
     *
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
     */
    protected void performTask(HttpServletRequest req, HttpServletResponse res) {
    	final String MY_METHOD_NAME = "WISLogin::performTask()";
        // �����\���E�ĕ\�����f�t���O
        String strLogin = "";

        try {
            // �Z�b�V�������擾
            WICCommonInfo commonInfo = checkSession(req);
            // http�p�����[�^���擾
            String strUserid = Tools.null2Blank(req.getParameter("USER_ID"));
            String strPasswd = Tools.null2Blank(req.getParameter("PASSWORD"));

            // ���O�C���󋵃t���O
            strLogin = Tools.null2Blank((String)getSessionValue("LOGIN_FIRST", req));

            WIBLoginDB loginDB = new WIBLoginDB();

            commonInfo.setUserID(strUserid);
            commonInfo.setPassword(strPasswd);
            // ���ʃZ�b�V�������
            loginDB.setCommonInfo(commonInfo);

            int ret = loginDB.executeLogin();
            // ���ʃZ�b�V�������Z�b�g
            commonInfo = loginDB.getCommonInfo();
            //���ʏ��Z�b�g
            setSessionValue("COMMONINFO", commonInfo, req);

            // �F�؏������ׂăN���A�̏ꍇ
            if (ret == 0) {
                // ���O�C���󋵃t���O
                setSessionValue("LOGIN_FIRST", "1", req);
                // ����ʌĂяo��(����I��)
                callPage(NEXT_PAGE, req, res);
                return;
            } else {
                // �A���[�g�\��
                setSessionValue("ERROR_MESSAGE_ALERT", "1", req);
                // �p�X���[�h����
                if (ret == 1) {
                    setSessionValue("ERROR_CODE", "95002", req);
		        // ���[�U�[�h�c����
                } else if (ret == 2) {
                    setSessionValue("ERROR_CODE", "95001", req);
                // �p�X���[�h�̍Đݒ肪�K�v 
                } else if (ret == 3) {
					setSessionValue("ERROR_CODE", "95005", req);
                }
                // ����ʌĂяo��
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
                          new Exception("���̑��̃G���[�F" + e.getMessage()), 
                          req, res);
        }
    }
}
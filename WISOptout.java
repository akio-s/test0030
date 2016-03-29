/*
 * WISOptout.java
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/17
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    �V�K�쐬
 */
package jp.co.token.optout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * �O���[�e�B���O���[���I�v�g�A�E�g�����T�[�u���b�g
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WISOptout extends WISServlet {
	/** �I�v�g�A�E�g��� */
	private final static String NEXT_PAGE = "/optout.jsp";
	/**
	 * �o�^���� �又��.
	 */
	protected void performTask(HttpServletRequest req, HttpServletResponse res) {
		final String MY_METHOD_NAME = "WISOptout::performTask()";
		try {
			// �Z�b�V�������擾
			WICCommonInfo commonInfo = checkSession(req);

			// http�p�����[�^���擾
			// �����j�����[��
			//String weddingFlag = "";
			//String vals1[] = req.getParameterValues("radio1");
			//for(int i = 0; i < vals1.length; ++i){
			//	weddingFlag = vals1[i];
			//}

			// �a�������[��
			String birthdayFlag = "";
			String vals2[] = req.getParameterValues("radio2");
			for(int i = 0; i < vals2.length; ++i){
				birthdayFlag = vals2[i];
			}
			// �z��҃��[��
			//String spouseFlag = "";
			//String vals3[] = req.getParameterValues("radio3");
			//for(int i = 0; i < vals3.length; ++i){
		//		spouseFlag = vals3[i];
			//}

			// commonInfo�ւ̃Z�b�g
			//commonInfo.setWeddingFlag(weddingFlag);
			commonInfo.setBirthdayFlag(birthdayFlag);
			//commonInfo.setSpouseFlag(spouseFlag);

			WIBOptOutDB outputDB = new WIBOptOutDB();
			// ���ʃZ�b�V�������
			outputDB.setCommonInfo(commonInfo);

			outputDB.execute();
			//���ʏ��Z�b�g
			commonInfo = outputDB.getCommonInfo();
			setSessionValue("COMMONINFO", commonInfo, req);
			// �A���[�g�\��
			setSessionValue("ERROR_MESSAGE_ALERT", "1", req);
			setSessionValue("ERROR_CODE", "95004", req);

			// ����ʌĂяo��(����I��)
			callPage(NEXT_PAGE, req, res);
		} catch (WICDBException e) {
			systemErrProc(null, e, req, res);
		} catch (WICSessionException e) {
			sessionErrProc(MY_METHOD_NAME, e, req, res);
		} catch (Exception e) {
			e.printStackTrace();
			systemErrProc(MY_METHOD_NAME, "99999",
			              null, new Exception("���̑��̃G���[�F" +
			              e.getMessage()), req, res);
		}
	}
}
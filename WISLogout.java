/*
 * WISLogout.java
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/17
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    �V�K�쐬
 * 01.01.00    20050905   SOFTEC D.KAWAKITA    ���O�A�E�g��ʂ̔p�~
 */
package jp.co.token.optout;

import javax.servlet.http.*;
  
/**
 * �O���[�e�B���O���[���I�v�g�A�E�g���O�A�E�g�T�[�u���b�g.
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.01.00
 */
public class WISLogout extends WISServlet {
	/** ���O�A�E�g���JSP */
	private final static String NEXT_PAGE = "/Logout.jsp";

	/**
	 * ���C�����N�G�X�g����.
	 *
	 * @param req HttpServletRequest�I�u�W�F�N�g
	 * @param res HttpServletResponse�I�u�W�F�N�g
	 */
	protected void performTask(HttpServletRequest req, HttpServletResponse res) {
		final String MY_METHOD_NAME = "WISLogout::performTask()";
		try {
			// �Z�b�V�������擾
			WICCommonInfo commonInfo = checkSession(req);

			// �Z�b�V�������N���A
			HttpSession ss = null;
			ss = req.getSession(true);
			ss.invalidate();

			// ���O�A�E�g��ʌĂяo��
/* 2005.09.05 delete ----->
			callPage(NEXT_PAGE, req, res);
<----- 2005.09.05 delete */			
			return;
        } catch (WICSessionException e) {
            sessionErrProc(MY_METHOD_NAME, e, req, res);
        } catch (Exception e) {
            systemErrProc(MY_METHOD_NAME, "99999", null, 
                           new Exception("���̑��̃G���[�F" + e.getMessage()), 
                           req, res);
        }
    }
}
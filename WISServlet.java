/*
 * WISServlet.java
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/17
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050817   SOFTEC D.KAWAKITA    �V�K�쐬
 */
package jp.co.token.optout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * �x�[�X�T�[�u���b�g.
 * <pre>
 * �e�T�[�u���b�g�̃x�[�X�ƂȂ�N���X
 * �S�ẴT�[�u���b�g�͂��̃N���X���p������
 * </pre>
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public abstract class WISServlet extends HttpServlet {
	/** �V�X�e���G���[JSP�� */
	private final static String SYSTEMERR_PAGE = "/SystemErr.jsp";
    /** �Z�b�V�����G���[JSP�� */
    private final static String SESSIONERR_PAGE = "/SessionErr.jsp";

	/**
	 * �T�[�u���b�g����������.
	 *
	 * @param config �T�[�u���b�g�R���t�B�O
	 * @exception ServletException �T�[�u���b�g��O������
	 */
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		ServletContext sc = getServletContext();
		// �e�T�[�u���b�g�ł̏�������
		doInit();
	}

	/**
	 * ��������.
     *
      * <pre>
     * �e�T�[�u���b�g�ŏ������������s�������ꍇ��
     * ���̃��\�b�h����������
     * </pre>
     */
    protected void doInit() { 
    }

    /**
     * JSP�Ăяo������.
     * 
     * @param page �Ăяo��JSP��
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
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
     * �Z�b�V�������`�F�b�N����.
     * <pre>
     * �Z�b�V�����I�u�W�F�N�g���A�ۑ����Ă��鋤�ʃZ�b�V���������擾����B
     * </pre>
     *
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @exception WICSessionException �Z�b�V������񂪑��݂��Ȃ��ꍇ
     */
    protected WICCommonInfo checkSession(HttpServletRequest req) 
                          throws WICSessionException {
        WICCommonInfo commonInfo = null;
        // �Z�b�V�����I�u�W�F�N�g�擾
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.err.println("��------checkSession::�Z�b�V������O����:time over");
            throw new WICSessionException();
        }
        // ���ʃZ�b�V�������I�u�W�F�N�g�擾
        commonInfo = (WICCommonInfo)session.getAttribute("COMMONINFO");
        if (commonInfo == null) {
            System.err.println("��------checkSession::�Z�b�V������O����(commonInfo):time over");
            throw new WICSessionException();
        }
        setSessionValue("ERROR_MESSAGE_ALERT", "", req);
        setSessionValue("ERROR_CODE", "", req);
        return commonInfo;
    }

    /**
     * doGet().
     *
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
     * @exception ServletException �T�[�u���b�g��O������
     * @exception IOException IO��O������
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) 
                 throws ServletException, IOException {
        try {
            req.setCharacterEncoding("Shift_JIS");
            res.setContentType("text/html;charset=Shift_JIS");
            performTask(req, res);
        } catch (Exception e) {
            systemErrProc("WISServlet::doGet()", "99999", null, 
                          new Exception("���̑��̃G���[�F" + e.getMessage()), 
                          req, res);
        }
    }

    /**
     * doPost().
     *
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
     * @exception ServletException �T�[�u���b�g��O������
     * @exception IOException IO��O������
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
                 throws ServletException, IOException {
        try {
            req.setCharacterEncoding("Shift_JIS");
            res.setContentType("text/html;charset=Shift_JIS");
            performTask(req, res);
        } catch (Exception e) {
            systemErrProc("WISServlet::doPost()", "99999", null, 
                          new Exception("���̑��̃G���[�F" + e.getMessage()), 
                          req, res);
        }
    }

    /**
     * �V�X�e�����t�擾(yyyy.MM.dd HH:mm:ss).
     * 
     * @return �V�X�e�����t.
     */
    private String getDateString() {
        SimpleDateFormat format_date = 
                         new SimpleDateFormat("yyyy.MM.dd HH:mm:ss ");
        String dateString = format_date.format(new Date());
        return dateString;
    }

    /**
     * �Z�b�V�������ڎ擾����.
     * <pre>
     * �Z�b�V�������Ƃ��ĕێ����Ă��鍀�ږ����擾����
     * </pre>
     *
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @return �Z�b�V�������ږ����X�g
     * @exception WICSessionException �Z�b�V�������NULL�̏ꍇ
     */
    protected String[] getSessionKeys(HttpServletRequest req) 
                     throws WICSessionException {
        // �Z�b�V�����I�u�W�F�N�g�擾
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
     * �Z�b�V�������擾����.
     * <pre>
     * �����Ŏ󂯎����KEY�ŃZ�b�V�������̒l���擾����
     * </pre>
     *
     * @param key �擾����Z�b�V��������KEY
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @return �Z�b�V�������l
     * @exception WICSessionException �Z�b�V�������NULL�̏ꍇ
     */
    protected Object getSessionValue(String key, HttpServletRequest req) 
                   throws WICSessionException {
        // �Z�b�V�����I�u�W�F�N�g�擾
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.err.println("��------getSessionValue::�Z�b�V������O����");
            throw new WICSessionException();
        }
        return session.getAttribute(key);
    }

    /**
     * �Z�b�V������񏉊�������.
     * <pre>
     * �Z�b�V�������ɋ��ʃZ�b�V�������ł���
     * "COMMONINFO"�̒l���Z�b�g����
     * </pre>
     *
     * @param commonInfo ���ʃZ�b�V�������
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @exception WICSessionException �Z�b�V�������NULL�̏ꍇ
     */
    protected void initSession(WICCommonInfo commonInfo, HttpServletRequest req)
                 throws WICSessionException {
        // �Z�b�V�����I�u�W�F�N�g�擾
        HttpSession session = req.getSession(true);
        if (session == null) {
            System.err.println("��------getSessionValue::�Z�b�V������O����");
            throw new WICSessionException();
        }
        session.setAttribute("COMMONINFO", commonInfo);
    }

    /**
     * �G���[���O�o�͏���.
     * 
     * @param dateString �V�X�e�����t
     * @param err_no �V�X�e���G���[��
     * @param cust_id ���O�C�����[�U�[��ЃR�[�h
     * @param user_id ���O�C�����[�U�[�h�c
     * @param location �G���[�����ꏊ
     * @param message �G���[���b�Z�[�W
     */
    private void outputLog(String dateString, String err_no, String cust_id, 
                            String user_id, String location, String message) {
        System.out.println(dateString + err_no + " " + cust_id + 
                            " " + user_id + " " + location + " " + message);
    }

    /**
     * ���C�����N�G�X�g����.
     *
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
     */
    protected abstract void performTask(HttpServletRequest req, 
                                           HttpServletResponse res);

    /**
     * COMMONINFO�ȊO�̃Z�b�V��������S�č폜����.
     *
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @exception WICSessionException �Z�b�V�������NULL�̏ꍇ
     */
    protected void removeAllSession(HttpServletRequest req) 
                 throws WICSessionException {
        HttpSession session = (HttpSession)req.getSession(false);
        if (session == null) {
            System.err.println("��------getSessionValue::�Z�b�V������O����");
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
     * �Z�b�V�������폜����.
     * <pre>
     * �����œn���ꂽKEY�ŃZ�b�V���������폜����
     * </pre>
     *
     * @param key �Z�b�V�������KEY
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @exception WICSessionException �Z�b�V�������NULL�̏ꍇ
     */
    protected void removeSessionValue(String key, HttpServletRequest req) 
                     throws WICSessionException {
        HttpSession session = req.getSession( false );
        if (session == null) {
            System.err.println("��------getSessionValue::�Z�b�V������O����");
            throw new WICSessionException();
        }
        session.removeAttribute( key );
    }

    /**
     * �Z�b�V�����G���[����.
     * <pre>
     * �Z�b�V������O�������ɃV�X�e���A�E�g���o�͂���
     * </pre>
     *
     * @param location ��O�����ꏊ(�N���X�F���\�b�h�j 
     * @param ex �Z�b�V������O�I�u�W�F�N�g
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
     */
    protected void sessionErrProc(String location, WICSessionException ex, 
                                    HttpServletRequest req, 
                                    HttpServletResponse res) {
        outputLog(getDateString(), "0", null, null, location, ex.getMessage());
        try {
            // �Z�b�V������O�y�[�W��
            getServletContext().getRequestDispatcher(SESSIONERR_PAGE).forward(req, res);
        } catch (Exception e) {
            System.out.println(getDateString() + 
                                "sessionErrProc() can not call sessionErr.jsp: " + 
                                e.toString());
        }
    }

    /**
     * �Z�b�V�������ݒ菈��.
     * <pre>
     * �Z�b�V�������ɁA�w�肳�ꂽKEY�ŃI�u�W�F�N�g���i�[����
     * </pre>
     *
     * @param key �Z�b�V�������KEY
     * @param obj �Z�b�V�������Ɋi�[����I�u�W�F�N�g
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @exception WICSessionException �Z�b�V�������NULL�̏ꍇ
     */
    protected void setSessionValue(String key, Object obj, 
                                     HttpServletRequest req)
                     throws WICSessionException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.err.println("��------getSessionValue::�Z�b�V������O����");
            throw new WICSessionException();
        }
        session.setAttribute(key, obj);
    }

    /**
     * �V�X�e���G���[����.
     * 
     * @param location �G���[�����ꏊ(�N���X�F���\�b�h)
     * @param err_no �V�X�e���G���[��
     * @param commonInfo ���ʃZ�b�V�������I�u�W�F�N�g
     * @param ex ��O�I�u�W�F�N�g
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
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
     * �V�X�e���G���[����.
     * 
     * @param commonInfo ���ʃZ�b�V�������I�u�W�F�N�g
     * @param ex ��O�I�u�W�F�N�g
     * @param req HttpServletRequest�I�u�W�F�N�g
     * @param res HttpServletResponse�I�u�W�F�N�g
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
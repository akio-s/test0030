/*
 * WICDBException.java
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/16
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050816   SOFTEC D.KAWAKITA    �V�K�쐬
 */
package jp.co.token.optout;

/**
 * �c�a��O�N���X
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WICDBException extends Exception {
	/** ��O���b�Z�[�W */
	private String msg;
	/** ��O�����ꏊ */
	private String location;
	/** �G���[�ԍ� */
    private String err_no;

    /**
     * �R���X�g���N�^.
     * <pre>
     * SQL��ԁA���R��Y����WICDBException���`.
     * SQLException�������ɗ�O�����������ꍇ�A���̎���SQL�G���[�R�[�h��
     * �G���[���b�Z�[�W�ɓY����WICDBException���쐬����
     * </pre>
     *
     * @param location ��O�����ꏊ
     * @param err_no �G���[�ԍ�
     * @param msg ��O���b�Z�[�W
     * @param err_code �c�a�G���[�������ɂc�a�l�r���Ԃ��G���[�R�[�h(�����G���[�������� 0��ݒ�)
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
     * ��O���b�Z�[�W��getter.
     *
     * @return �G���[���b�Z�[�W
     */
    public String getMessage() {
        return msg;
    }

    /**
     * �G���[�ԍ���getter.
     *
     * @return �G���[�ԍ�
     */
    public String getErr_no() {
        return err_no;
    }

    /**
     * �G���[�����ꏊ��getter.
     *
     * @return �G���[�����ꏊ
     */
    public String getLocation() {
        return location;
    }
}
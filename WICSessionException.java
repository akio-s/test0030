/*
 * WICSessionException.java
 *
 * �쐬��      : SOFTEC D.KAWAKITA
 * �쐬��      : 2005/08/16
 * �X�V����    �X�V��     �S����               ���e
 * 01.00.00    20050816   SOFTEC D.KAWAKITA    �V�K�쐬
 */
package jp.co.token.optout; 

/**
 * �Z�b�V������O�N���X
 *
 * @author SOFTEC(D.KAWAKITA)
 * @version 01.00.00
 */
public class WICSessionException extends Exception {
    /** �Z�b�V������O���b�Z�[�W */
    private final static String msg = "�Z�b�V�����I�u�W�F�N�g���狤�ʏ����擾�ł��܂���ł���";

    /**
     * �R���X�g���N�^.
     *
     */
    public WICSessionException() {
        super(msg);
    }

    /**
     * �G���[���b�Z�[�W��getter.
     *
     * @return �G���[���b�Z�[�W
     */
    public String getMessage() {
        return msg;
    }
}
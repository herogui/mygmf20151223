package com.aoc.gmf.MyEvent;


import org.json.JSONException;

import java.util.EventListener;

/**
 * �¼���������ʵ��java.util.EventListener�ӿڡ�����ص�������������Ҫ������
 * �ŵ����������,��Ϊ�¼�Դ������Ӧ���¼�ʱ��������������
 * @author Eric
 */
public class CusEventListener implements EventListener {

    //�¼������Ļص�����
    public void fireCusEvent(CusEvent e) throws JSONException {
        EventSourceObject eObject = (EventSourceObject) e.getSource();
       // String res = eObject.getString();
    }
}

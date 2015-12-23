package com.aoc.gmf.MyEvent;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * �¼�Դ.
 * @author Eric
 */
public class EventSourceObject {
    private String str;
    private Boolean isOk = false;
    private JSONObject jsonObject = null;
    //����������
    private Set<CusEventListener> listener;
    public EventSourceObject(){
        this.listener = new HashSet<CusEventListener>();
        this.str = "defaultstr";
    }
    //���¼�Դע�������
    public void addCusListener(CusEventListener cel){
        this.listener.add(cel);
    }
    //���¼�����ʱ,֪ͨע���ڸ��¼�Դ�ϵ����м�����������Ӧ�ķ�Ӧ�����ûص�������
    protected void notifies(){
        CusEventListener cel = null;
        Iterator<CusEventListener> iterator = this.listener.iterator();
        while(iterator.hasNext()){
            cel = iterator.next();
            try {
                cel.fireCusEvent(new CusEvent(this));
            }
            catch (Exception exp){}
        }
    }
    public String getString() {
        return str;
    }
    //ģ���¼�������������Ա����name��ֵ����仯ʱ�������¼���
    public void setString(String name) {
       // if(!this.str.equals(name))
        {
            this.str = name;
            notifies();
        }
    }

    public  Boolean getBoolean()
    {
        return  isOk;
    }

    public void setBoolean(Boolean _isOk) {
        this.isOk = _isOk;
        notifies();
    }

    public JSONObject getJsonObject()
    {
        return this.jsonObject;
    }

    public void  setJsonObject(JSONObject obj)
    {
        this.jsonObject = obj;
        notifies();
    }

    public  void  removeListener(CusEventListener cel)
    {
        this.listener.remove(cel);
    }
}

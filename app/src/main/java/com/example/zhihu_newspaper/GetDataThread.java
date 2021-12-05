package com.example.zhihu_newspaper;

import java.util.logging.Handler;

public class GetDataThread extends Thread {
    //重点
    private Handler handler ;
    private String type;
    /* 传入两个参数，第一个是用于通信的handler，第二个是动态类型 */
    public GetDataThread(Handler h, String type){
        this.handler = h ;
        this.type = type;
    }
}


package com.hencoder.hencoderpracticedraw1.proxy;

/**
 * Created by pucheng on 2018/3/11.
 */

public class Test {

    public static void main(String[] args){
        // 静态代理
        Purchase purchase = new Purchase(new Pudao());
        purchase.buy();
    }

}

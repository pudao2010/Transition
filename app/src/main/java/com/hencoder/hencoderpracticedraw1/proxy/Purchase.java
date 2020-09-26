package com.hencoder.hencoderpracticedraw1.proxy;

/**
 * Created by pucheng on 2018/3/11.
 */

public class Purchase implements IShop{

    private IShop iShop;

    public Purchase(IShop iShop) {
        this.iShop = iShop;
    }

    @Override
    public void buy() {
        iShop.buy();
    }
}

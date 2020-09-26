// IBookManager.aidl
package com.hencoder.hencoderpracticedraw1;
// 一定需要导包
import com.hencoder.hencoderpracticedraw1.Book;
// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    // 定义两个接口
    List<Book> getBookList();

    void addBook(in Book book);
}

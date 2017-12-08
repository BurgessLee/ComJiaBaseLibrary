package com.comjia.baselibrary.utils.upgrade;

public interface IUpdateAgent {

    UpdateInfo getInfo();

    void update();

    void ignore();
}
package com.comjia.baselibrary.utils.upgrade;

public interface IUpdateParser {

    UpdateInfo parse(String source) throws Exception;
}
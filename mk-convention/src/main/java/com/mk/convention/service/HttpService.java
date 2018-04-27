package com.mk.convention.service;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.List;

public interface HttpService {

    String doPost(String url, List<NameValuePair> parameters, String charset) throws IOException;

    String doGet(String url, List<NameValuePair> parameters, String charset) throws IOException;

}

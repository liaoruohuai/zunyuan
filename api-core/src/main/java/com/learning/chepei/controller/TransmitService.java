package com.learning.chepei.controller;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://webservice.framework.allinfinance.com/")
public interface TransmitService {

    @WebMethod
     public String sendService(
             @WebParam(name="req")
                     String req) ;

      /**
       *
       * @param ctx
       * @param req
       * @return
       * @throws Exception
       */
      @WebMethod
      public String sendMisService(
              @WebParam(name="ctx")
                      String ctx,
              @WebParam(name="req")
                      String req) ;
}

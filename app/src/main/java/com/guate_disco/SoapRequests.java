package com.guate_disco;

import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sashen
 * Date: 12/17/13
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class SoapRequests {

    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    //public final static String URL = "http://192.168.1.46:8084/WS-Guate-Disco/MyWebService?wsdl";
    //public final static String URL = "http://uakkb108bec2.akroma88.koding.io:9763/WS-Guate-Disco/MyWebService?wsdl";
    public final static String URL = "http://wsguatedisco-guate.rhcloud.com/MyWebService?wsdl";
    public static final String NAMESPACE = "http://pack1";
    public static final String SOAP_ACTION_PREFIX = "/";
    private static String METHOD = "getDiscos";
    private static String SESSION_ID;

    private final void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v("SOAP RETURN", "Request XML:\n" + ht.requestDump);
            Log.v("SOAP RETURN", "\n\n\nResponse XML:\n" + ht.responseDump);
        }
    }

    public String getObtainData(String fValue, String method, String parameter) {
        String data = null;
        METHOD = method;

        SoapObject request = new SoapObject(NAMESPACE+SOAP_ACTION_PREFIX, METHOD);
        request.addProperty(parameter, fValue);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(NAMESPACE + SOAP_ACTION_PREFIX + METHOD, envelope);
            testHttpResponse(ht);
            SoapPrimitive resultsString = (SoapPrimitive)envelope.getResponse();

            List<HeaderProperty> COOKIE_HEADER = (List<HeaderProperty>) ht.getServiceConnection().getResponseProperties();

            for (int i = 0; i < COOKIE_HEADER.size(); i++) {
                String key = COOKIE_HEADER.get(i).getKey();
                String value = COOKIE_HEADER.get(i).getValue();

                if (key != null && key.equalsIgnoreCase("set-cookie")) {
                    SoapRequests.SESSION_ID = value.trim();
                    Log.v("SOAP RETURN", "Cookie :" + SoapRequests.SESSION_ID);
                    break;
                }
            }
            data = resultsString.toString();

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
            data = "errorWS";
        } catch (IOException i) {
            i.printStackTrace();
            data = "errorWS";
        } catch (Exception q) {
            q.printStackTrace();
            data = "errorWS";
        }
        return data;
    }

    private final SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        return envelope;
    }

    private final HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,URL,60000);
        ht.debug = true;
        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

    private final List<HeaderProperty> getHeader() {
        List<HeaderProperty> header = new ArrayList<HeaderProperty>();
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapRequests.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }
}
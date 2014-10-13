package com.qteen;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class SoapRequests {
	private static final String NAMESPACE = "http://www.w3schools.com/webservices/";
	private static final String MAIN_REQUEST_URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
	private static final String SOAP_ACTION = "http://www.w3schools.com/webservices/FahrenheitToCelsius";
	private static String SESSION_ID;

	public String getCelsiusConversion(String fValue) {
		String data = null;
		String methodname = "FahrenheitToCelsius";

		SoapObject request = new SoapObject(NAMESPACE, methodname);
		request.addProperty("Fahrenheit", fValue);

		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

		HttpTransportSE ht = getHttpTransportSE();
		try {
			ht.call(SOAP_ACTION, envelope);
			SoapPrimitive resultsString = (SoapPrimitive) envelope
					.getResponse();

			List<HeaderProperty> COOKIE_HEADER = ht.getServiceConnection()
					.getResponseProperties();

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
		} catch (IOException i) {
			i.printStackTrace();
		} catch (Exception q) {
			q.printStackTrace();
		}
		return data;
	}

	private final SoapSerializationEnvelope getSoapSerializationEnvelope(
			SoapObject request) {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		envelope.setOutputSoapObject(request);

		return envelope;
	}

	private final HttpTransportSE getHttpTransportSE() {
		HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,
				MAIN_REQUEST_URL, 60000);
		ht.debug = true;
		ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
		return ht;
	}
}

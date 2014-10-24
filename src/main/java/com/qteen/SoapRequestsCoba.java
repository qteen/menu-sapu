package com.qteen;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SoapRequestsCoba {
	private static final String NAMESPACE = "http://tempuri.org/";
	private String urlStr;

	public SoapRequestsCoba(String urlStr) {
		this.urlStr = urlStr;
		// TODO Auto-generated constructor stub
	}

	public String getWeatherFromZipCode(String menu) {
		String data = null;
		String methodname = "tes";
		SoapObject request = new SoapObject(NAMESPACE, methodname);
		request.addProperty("strkode", "???");
		if (menu.equals("Berita")) {
			methodname = "member_getdata";
			request = new SoapObject(NAMESPACE, methodname);
			request.addProperty("strkode", "berita");
		} else if (menu.equals("Pembayaran")) {
			methodname = "member_getdata";
			request = new SoapObject(NAMESPACE, methodname);
			request.addProperty("strkode", "payment");
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(urlStr);
		try {
			ht.call(NAMESPACE + methodname, envelope);
			AttributeContainer resultsString = (AttributeContainer) envelope
					.getResponse();
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
}

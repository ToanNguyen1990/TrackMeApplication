package net.toannt.hacore.utils.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class MacAddressHelper {

    public static String getMacAddress() throws SocketException {
        List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
        for (NetworkInterface nif : all) {
            if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
            byte[] macBytes = nif.getHardwareAddress();
            if (macBytes == null) {
                return "";
            }
            StringBuilder res1 = new StringBuilder();
            for (byte b : macBytes) {
                res1.append(Integer.toHexString(b & 0xFF) + "-");
            }
            if (res1.length() > 0) {
                res1.deleteCharAt(res1.length() - 1);
            }
            System.out.println("DEVICE MAC " + res1.toString());
            return res1.toString();
        }
        return MqttClient.generateClientId();
    }
}

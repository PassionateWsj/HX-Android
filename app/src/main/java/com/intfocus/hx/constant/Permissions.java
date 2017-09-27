package com.intfocus.hx.constant;

public class Permissions {
    /*
    group:android.permission-group.CONTACTS
    permission:android.permission.WRITE_CONTACTS
    permission:android.permission.GET_ACCOUNTS
    permission:android.permission.READ_CONTACTS

    group:android.permission-group.PHONE
    permission:android.permission.READ_CALL_LOG
    permission:android.permission.READ_PHONE_STATE
    permission:android.permission.CALL_PHONE
    permission:android.permission.WRITE_CALL_LOG
    permission:android.permission.USE_SIP
    permission:android.permission.PROCESS_OUTGOING_CALLS
    permission:com.android.voicemail.permission.ADD_VOICEMAIL

    group:android.permission-group.CALENDAR
    permission:android.permission.READ_CALENDAR
    permission:android.permission.WRITE_CALENDAR

    group:android.permission-group.CAMERA
    permission:android.permission.CAMERA

    group:android.permission-group.SENSORS
    permission:android.permission.BODY_SENSORS

    group:android.permission-group.LOCATION
    permission:android.permission.ACCESS_FINE_LOCATION
    permission:android.permission.ACCESS_COARSE_LOCATION

    group:android.permission-group.STORAGE
    permission:android.permission.READ_EXTERNAL_STORAGE
    permission:android.permission.WRITE_EXTERNAL_STORAGE

    group:android.permission-group.MICROPHONE
    permission:android.permission.RECORD_AUDIO

    group:android.permission-group.SMS
    permission:android.permission.READ_SMS
    permission:android.permission.RECEIVE_WAP_PUSH
    permission:android.permission.RECEIVE_MMS
    permission:android.permission.RECEIVE_SMS
    permission:android.permission.SEND_SMS
    permission:android.permission.READ_CELL_BROADCASTS*/



    //Dangerous Permission，一般是涉及到用户隐私的，需要用户进行授权
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String USE_SIP = "android.permission.USE_SIP";
    public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    public static final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";
    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";
    public static final String CAMERA = "android.permission.CAMERA";
    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String READ_CELL_BROADCASTS = "android.permission.READ_CELL_BROADCASTS";



    public static final int WRITE_CONTACTS_CODE= 0x1601;
    public static final int GET_ACCOUNTS_CODE= 0x1602;
    public static final int READ_CONTACTS_CODE= 0x1603;
    public static final int READ_CALL_LOG_CODE= 0x1604;
    public static final int READ_PHONE_STATE_CODE= 0x1605;
    public static final int CALL_PHONE_CODE= 0x1606;
    public static final int WRITE_CALL_LOG_CODE= 0x1607;
    public static final int USE_SIP_CODE= 0x1608;
    public static final int PROCESS_OUTGOING_CALLS_CODE= 0x1609;
    public static final int ADD_VOICEMAIL_CODE= 0x1610;
    public static final int READ_CALENDAR_CODE= 0x1611;
    public static final int WRITE_CALENDAR_CODE= 0x1612;
    public static final int CAMERA_CODE= 0x1613;
    public static final int BODY_SENSORS_CODE= 0x1614;
    public static final int ACCESS_FINE_LOCATION_CODE= 0x1615;
    public static final int ACCESS_COARSE_LOCATION_CODE= 0x1616;
    public static final int READ_EXTERNAL_STORAGE_CODE= 0x1617;
    public static final int WRITE_EXTERNAL_STORAGE_CODE= 0x1618;
    public static final int RECORD_AUDIO_CODE= 0x1619;
    public static final int READ_SMS_CODE= 0x1620;
    public static final int RECEIVE_WAP_PUSH_CODE= 0x1621;
    public static final int RECEIVE_MMS_CODE= 0x1622;
    public static final int RECEIVE_SMS_CODE= 0x1623;
    public static final int SEND_SMS_CODE= 0x1624;
    public static final int READ_CELL_BROADCASTS_CODE= 0x1625;
}

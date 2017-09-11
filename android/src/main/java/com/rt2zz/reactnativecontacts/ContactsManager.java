package com.rt2zz.reactnativecontacts;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;

import java.util.ArrayList;

//import com.facebook.react.bridge.WritableMap;
//import com.facebook.react.bridge.Arguments;

//import android.util.Log;
//import com.google.firebase.crash.FirebaseCrash;

//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

//import java.util.Iterator;
//import java.io.InputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

import android.util.Base64;

public class ContactsManager extends ReactContextBaseJavaModule {

    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ContactsManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /*
     * Returns all contactable records on phone
     * queries CommonDataKinds.Contactables to get phones and emails
     */
    @ReactMethod
    public void getAll(final Callback callback) {
        getAllContacts(callback);
    }

    /**
     * Introduced for iOS compatibility.  Same as getAll
     *
     * @param callback callback
     */
    @ReactMethod
    public void getAllWithoutPhotos(final Callback callback) {
        getAllContacts(callback);
    }

    /*
    private WritableArray convertJSONArray(JSONArray jsonArray) {
        WritableArray result = Arguments.createArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object object;
            try {
                object = jsonArray.get(i);
            } catch (JSONException e) {
                return result;
            }
            if (object instanceof JSONObject) {
                result.pushMap(convertJSONObject((JSONObject) object));
            } else if (object instanceof JSONArray) {
                result.pushArray(convertJSONArray((JSONArray) object));
            } else if (object instanceof String) {
                result.pushString((String) object);
            } else if (object instanceof Integer) {
                result.pushInt((int) object);
            } else if (object instanceof Boolean) {
                result.pushBoolean((Boolean) object);
            } else if (object instanceof Double) {
                result.pushDouble((Double) object);
            }
        }
        return result;
    }

    private WritableMap convertJSONObject(JSONObject object) {
        WritableMap result = Arguments.createMap();
        Iterator<String> keyIterator = object.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value;
            try {
                value = object.get(key);
            } catch (JSONException e) {
                return result;
            }
            if (value instanceof JSONObject) {
                result.putMap(key, convertJSONObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                result.putArray(key, convertJSONArray((JSONArray) value));
            } else if (value instanceof String) {
                result.putString(key, (String) value);
            } else if (value instanceof Integer) {
                result.putInt(key, (int) value);
            } else if (value instanceof Boolean) {
                result.putBoolean(key, (Boolean) value);
            } else if (value instanceof Double) {
                result.putDouble(key, (Double) value);
            }
        }
        return result;
    }

    String doPostRequest(OkHttpClient client, String url, String json) throws IOException {
        //RequestBody body = RequestBody.create(JSON, json);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    */

    /**
     * Retrieves contacts.
     * Uses raw URI when <code>rawUri</code> is <code>true</code>, makes assets copy otherwise.
     * @param callback callback
     */
    private void getAllContacts(final Callback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = getReactApplicationContext();
                    ContentResolver cr = context.getContentResolver();
                    ContactsProvider contactsProvider = new ContactsProvider(cr);
                    WritableArray contacts = contactsProvider.getContacts();

                    /*
                    int size = contacts.size();
                    for (int i = 0; i < 100; i++) {
                        for (int j = 0; j < size; j++) {
                            WritableMap m = Arguments.createMap(); 
                            m.merge(contacts.getMap(j));
                            contacts.pushMap(m);
                        }
                    }
                    */

                    String jsonString = String.format("%s", contacts);
                    String encodeString = Base64.encodeToString(jsonString.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);

                    /* Load json string from raw resource
                    InputStream inputStream = context.getResources().openRawResource(R.raw.test_json);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    int ctr;
                    try {
                        ctr = inputStream.read();
                        while (ctr != -1) {
                            byteArrayOutputStream.write(ctr);
                            ctr = inputStream.read();
                        }
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String jsonString = byteArrayOutputStream.toString();
                    JSONArray jsonArray = new JSONArray(jsonString);
                    WritableArray contacts = convertJSONArray(jsonArray);
                    */

                    /* send contacts to api
                    String url = "https://xxxxxxxxxxxxxxxxxxxxxx";
                    OkHttpClient client = new OkHttpClient();

                    String contactString = String.format("%s", contacts);

                    int part = 10000;
                    if (contactString.length() < part) {
                        doPostRequest(client, url, contactString);
                    } else {
                        //ArrayList<Object> contactList = contacts.toArrayList();
                        //int clSize = contactList.size();
                        int clSize = contactString.length();
                        int head = 0;
                        int tail = part;
                        while (head < clSize) {
                            //FirebaseCrash.report(new Exception(String.format("head %d tail %d: %s", head, tail, contactList.subList(head, tail))));
                            //doPostRequest(client, url, String.format("head %d tail %d, %s", head, tail, contactList.subList(head, tail)));
                            doPostRequest(client, url, String.format("head %d tail %d total %d, %s", head, tail, clSize, contactString.substring(head, tail)));

                            head = tail;
                            tail += part;
                            if (tail >= clSize) {
                                tail = clSize;
                            }
                        }
                    }
                    */

                    // crash!
                    //contacts.pushMap((WritableMap) contacts.getMap(0));

                    //callback.invoke(null, contacts);
                    //callback.invoke(null, jsonString);
                    callback.invoke(null, encodeString);
                } catch (Exception e) {
                    /*
                    String url = "https://xxxxxxxxxxxxxxxxxxxxxx";
                    OkHttpClient client = new OkHttpClient();
                    try {
                        doPostRequest(client, url, String.format("getContacts Exception: %s", e.getMessage()));
                    } catch (Exception ee) {
                    }
                    FirebaseCrash.report(e);
                    */
                    callback.invoke(e.getMessage());
                }
            }
        });
    }

    /**
     * Retrieves <code>thumbnailPath</code> for contact, or <code>null</code> if not available.
     * @param contactId contact identifier, <code>recordID</code>
     * @param callback callback
     */
    @ReactMethod
    public void getPhotoForId(final String contactId, final Callback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Context context = getReactApplicationContext();
                ContentResolver cr = context.getContentResolver();
                ContactsProvider contactsProvider = new ContactsProvider(cr);
                String photoUri = contactsProvider.getPhotoUriFromContactId(contactId);

                callback.invoke(null, photoUri);
            }
        });
    }

    /*
     * Adds contact to phone's addressbook
     */
    @ReactMethod
    public void addContact(ReadableMap contact, Callback callback) {

        String givenName = contact.hasKey("givenName") ? contact.getString("givenName") : null;
        String middleName = contact.hasKey("middleName") ? contact.getString("middleName") : null;
        String familyName = contact.hasKey("familyName") ? contact.getString("familyName") : null;
        String company = contact.hasKey("company") ? contact.getString("company") : null;
        String jobTitle = contact.hasKey("jobTitle") ? contact.getString("jobTitle") : null;

        // String name = givenName;
        // name += middleName != "" ? " " + middleName : "";
        // name += familyName != "" ? " " + familyName : "";

        ReadableArray phoneNumbers = contact.hasKey("phoneNumbers") ? contact.getArray("phoneNumbers") : null;
        int numOfPhones = 0;
        String[] phones = null;
        Integer[] phonesLabels = null;
        if (phoneNumbers != null) {
            numOfPhones = phoneNumbers.size();
            phones = new String[numOfPhones];
            phonesLabels = new Integer[numOfPhones];
            for (int i = 0; i < numOfPhones; i++) {
                phones[i] = phoneNumbers.getMap(i).getString("number");
                String label = phoneNumbers.getMap(i).getString("label");
                phonesLabels[i] = mapStringToPhoneType(label);
            }
        }

        ReadableArray emailAddresses = contact.hasKey("emailAddresses") ? contact.getArray("emailAddresses") : null;
        int numOfEmails = 0;
        String[] emails = null;
        Integer[] emailsLabels = null;
        if (emailAddresses != null) {
            numOfEmails = emailAddresses.size();
            emails = new String[numOfEmails];
            emailsLabels = new Integer[numOfEmails];
            for (int i = 0; i < numOfEmails; i++) {
                emails[i] = emailAddresses.getMap(i).getString("email");
                String label = emailAddresses.getMap(i).getString("label");
                emailsLabels[i] = mapStringToEmailType(label);
            }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder op = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null);
        ops.add(op.build());

        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                // .withValue(StructuredName.DISPLAY_NAME, name)
                .withValue(StructuredName.GIVEN_NAME, givenName)
                .withValue(StructuredName.MIDDLE_NAME, middleName)
                .withValue(StructuredName.FAMILY_NAME, familyName);
        ops.add(op.build());

        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
                .withValue(Organization.COMPANY, company)
                .withValue(Organization.TITLE, jobTitle);
        ops.add(op.build());

        //TODO not sure where to allow yields
        op.withYieldAllowed(true);

        for (int i = 0; i < numOfPhones; i++) {
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Phone.NUMBER, phones[i])
                    .withValue(CommonDataKinds.Phone.TYPE, phonesLabels[i]);
            ops.add(op.build());
        }

        for (int i = 0; i < numOfEmails; i++) {
            op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Email.ADDRESS, emails[i])
                    .withValue(CommonDataKinds.Email.TYPE, emailsLabels[i]);
            ops.add(op.build());
        }

        Context ctx = getReactApplicationContext();
        try {
            ContentResolver cr = ctx.getContentResolver();
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
            callback.invoke(); // success
        } catch (Exception e) {
            callback.invoke(e.toString());
        }
    }

    /*
     * Update contact to phone's addressbook
     */
    @ReactMethod
    public void updateContact(ReadableMap contact, Callback callback) {

        String recordID = contact.hasKey("recordID") ? contact.getString("recordID") : null;

        String givenName = contact.hasKey("givenName") ? contact.getString("givenName") : null;
        String middleName = contact.hasKey("middleName") ? contact.getString("middleName") : null;
        String familyName = contact.hasKey("familyName") ? contact.getString("familyName") : null;
        String company = contact.hasKey("company") ? contact.getString("company") : null;
        String jobTitle = contact.hasKey("jobTitle") ? contact.getString("jobTitle") : null;

        ReadableArray phoneNumbers = contact.hasKey("phoneNumbers") ? contact.getArray("phoneNumbers") : null;
        int numOfPhones = 0;
        String[] phones = null;
        Integer[] phonesLabels = null;
        if (phoneNumbers != null) {
            numOfPhones = phoneNumbers.size();
            phones = new String[numOfPhones];
            phonesLabels = new Integer[numOfPhones];
            for (int i = 0; i < numOfPhones; i++) {
                ReadableMap phoneMap = phoneNumbers.getMap(i);
                String phoneNumber = phoneMap.getString("number");
                String phoneLabel = phoneMap.getString("label");
                phones[i] = phoneNumber;
                phonesLabels[i] = mapStringToPhoneType(phoneLabel);
            }
        }

        ReadableArray emailAddresses = contact.hasKey("emailAddresses") ? contact.getArray("emailAddresses") : null;
        int numOfEmails = 0;
        String[] emails = null;
        Integer[] emailsLabels = null;
        if (emailAddresses != null) {
            numOfEmails = emailAddresses.size();
            emails = new String[numOfEmails];
            emailsLabels = new Integer[numOfEmails];
            for (int i = 0; i < numOfEmails; i++) {
                ReadableMap emailMap = emailAddresses.getMap(i);
                emails[i] = emailMap.getString("email");
                String label = emailMap.getString("label");
                emailsLabels[i] = mapStringToEmailType(label);
            }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder op = ContentProviderOperation.newUpdate(RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(recordID)})
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null);
        ops.add(op.build());

        op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(recordID)})
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.GIVEN_NAME, givenName)
                .withValue(StructuredName.MIDDLE_NAME, middleName)
                .withValue(StructuredName.FAMILY_NAME, familyName);
        ops.add(op.build());

        op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + " = ?", new String[]{String.valueOf(recordID), Organization.CONTENT_ITEM_TYPE})
                .withValue(Organization.COMPANY, company)
                .withValue(Organization.TITLE, jobTitle);
        ops.add(op.build());

        op.withYieldAllowed(true);

        for (int i = 0; i < numOfPhones; i++) {
            op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + " = ?", new String[]{String.valueOf(recordID), CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Phone.NUMBER, phones[i])
                    .withValue(CommonDataKinds.Phone.TYPE, phonesLabels[i]);
            ops.add(op.build());
        }

        for (int i = 0; i < numOfEmails; i++) {
            op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + " = ?", new String[]{String.valueOf(recordID), CommonDataKinds.Email.CONTENT_ITEM_TYPE})
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Email.ADDRESS, emails[i])
                    .withValue(CommonDataKinds.Email.TYPE, emailsLabels[i]);
            ops.add(op.build());
        }

        Context ctx = getReactApplicationContext();
        try {
            ContentResolver cr = ctx.getContentResolver();
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
            callback.invoke(); // success
        } catch (Exception e) {
            callback.invoke(e.toString());
        }
    }

    /*
     * Check permission
     */
    @ReactMethod
    public void checkPermission(Callback callback) {
        callback.invoke(null, isPermissionGranted());
    }

    /*
     * Request permission
     */
    @ReactMethod
    public void requestPermission(Callback callback) {
        callback.invoke(null, isPermissionGranted());
    }

    /*
     * Check if READ_CONTACTS permission is granted
     */
    private String isPermissionGranted() {
        String permission = "android.permission.READ_CONTACTS";
        // return -1 for denied and 1
        int res = getReactApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED) ? "authorized" : "denied";
    }

    /*
     * TODO support all phone types
     * http://developer.android.com/reference/android/provider/ContactsContract.CommonDataKinds.Phone.html
     */
    private int mapStringToPhoneType(String label) {
        int phoneType;
        switch (label) {
            case "home":
                phoneType = CommonDataKinds.Phone.TYPE_HOME;
                break;
            case "work":
                phoneType = CommonDataKinds.Phone.TYPE_WORK;
                break;
            case "mobile":
                phoneType = CommonDataKinds.Phone.TYPE_MOBILE;
                break;
            default:
                phoneType = CommonDataKinds.Phone.TYPE_OTHER;
                break;
        }
        return phoneType;
    }

    /*
     * TODO support TYPE_CUSTOM
     * http://developer.android.com/reference/android/provider/ContactsContract.CommonDataKinds.Email.html
     */
    private int mapStringToEmailType(String label) {
        int emailType;
        switch (label) {
            case "home":
                emailType = CommonDataKinds.Email.TYPE_HOME;
                break;
            case "work":
                emailType = CommonDataKinds.Email.TYPE_WORK;
                break;
            case "mobile":
                emailType = CommonDataKinds.Email.TYPE_MOBILE;
                break;
            default:
                emailType = CommonDataKinds.Email.TYPE_OTHER;
                break;
        }
        return emailType;
    }

    @Override
    public String getName() {
        return "Contacts";
    }
}

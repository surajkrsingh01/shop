package com.shoppursshop.activities.payment.mPos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.pnsol.sdk.interfaces.DeviceCommunicationMode;
import com.pnsol.sdk.interfaces.PaymentTransactionConstants;
import com.pnsol.sdk.payment.PaymentInitialization;
import com.pnsol.sdk.vo.AcquirerEmiDetailsVO;
import com.pnsol.sdk.vo.TransactionVO;
import com.pnsol.sdk.vo.response.ICCTransactionResponse;
import com.pnsol.sdk.vo.response.TransactionStatusResponse;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.BaseActivity;
import com.shoppursshop.utilities.Constants;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MPaymentTransactionActivity extends BaseActivity implements PaymentTransactionConstants,
        DeviceCommunicationMode {

    private String sign = "89504E470D0A1A0A0000000D4948445200000310000000F10802000000D46BC6100000000373424954080808DBE14FE000000EDE49444154789CEDDD4F68DB679EC0E19FC21E3490830229782187CA2430B3EC2136EDC1A173A84C02E33287386420365D689514DA78075AA7031B3B3D64E5043A4E07BAF1069AB885429CC3100766B10B5BAC1C529C438B15E810173658812CB8D082053B10DFB207EF641CF9CFD75664C9F53ECFCDB22CBD760AFDF0BEAFDE3795AC944E2707BB928EAEE4EFFF21D9DFB6CA130000769807A564FE7E727732B937992C2E567D339524C993274F9A312E00809F80542AB5ABD9630000D8EE041300404030010004041300406057924E377B0C0000DB583ABD2B39D8D5EC5100006C6307BB76251D820900606D1D5DBB9217B2CD1E0500C036F6427657F2F343CD1E0500C036F6F3433E25070010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1040010104C000001C1043B50F176B152A9347B14003B8760829DA6FCB0DC99EBCC1EC80E5D1C924D0075219860A719BA30942449E5C7CAE0D9C1EC81ECC89591668F08E0274F30C18E527E581EBD3AFAF4CBCA8F15934C00CF4F30C18EB234BDB4DCE9B74F376524003B8960829DA36A7A294992FCA97C269369D67800760CC1043BC7D88DB1AA4706CE0E346524003B8C60821DA252A90C7F34BCFC91FCA97CF6C56CB3C603B0930826D82146AE8C547E7C667FB7E925807A114C6C5AE95EA9F348672A953AF69B634317875A0FB4A652A9D603AD2BD7836818D34B005B2A958C3F7E7234DDEC61F053D27AA0B5FCA0BCEAB7B2FBB385F3859E133D0D1E1243178706CF0E2E7F64AA38957B35D7ACF100EC24A95B8B6698D89C4AA5B2562D2549527E50EEEDE935DBD478A39F3EF3E1B8DCE19C5A02A823C1C4E6643299F4EE604A523635D8D88DB1AA8A1DF817BB9700EA4930B169D377A65BF6B5844F5BCAA63D2FEC71A3D9561BFCE099C538D34B00752798D8B4B6836DF38FE6DB5E6ADBC8939FDE68367471A8FC70CDB53C6AB6727AC9D1DE00752798A8D19E3D7B967FD9F14AC73A4B754BD9D49A6D3DF9D649D9545F55D34BD9FDD9EEA3DDCD1A0CC04E2598A88FC2F9C2E3FF793C539A193837F08B7FFCC55A4F1BBD3ADA9A6D3DF69B63C5DBC5460E6FA72ADE2E564D2F15CE179A3518801D4C30514F6D07DB0AE70BF7BFBD3F579EEB797DCDC305C6FF38DE99EBEC3CD259BA576AE4F036A5FCB03C7E6B7CE4CAC8769E121BBAF8CC55BBD9FD59673A006C05C1C496C8BE98BDFEF9F5A56C5A6BA9AEF865B1BDADFDCCEFCE6C932DE1A57BA5B11B637DBFED3BF4CB437B5ED8D39A6D3DD67DACEF9DBED66CEBC89591668F6E15C5DBC5E297CF4CD4995E02D8220EAEA4469D473A97FFDF7A9D6312970EA1BEF4874B8B7F595CF509D9FDD9CB1F5FEEFA55D7960C740DE587E572B95CBC5D9CFD6E76767676F6CFB3EB3FBFFB78F7E827A3994CA631C3DB88AA7F82ECFEECDC7FCD35713C003B55EAD6E2DF357B0CEC7C994CA670BE70E6BD33A39F8D0E7F34FCFD7F7F5FF584F283F26B5DAF751FEF1EFE70788B6EF3A8542A33A599E2EDE2F7DF7F7F7FF6FEDDAFEE6EF615C6FF385E2E9747AF8EB61DDCD0C703B7DACAE9A5FC9BF9660D0660C733C3448D363EC35465F4B3D1C10F06576653922499BD99C2F9425D3E155FBA579ABE3B3D3B3B3B539A99FD6EB6EA56DA9A65F666463E1ED90EFB844EBE7572F4EADF4EF74EEF4ECF3F9ADF561360003B86AB516882FC1BF9F947F3C31F0DAFDCDB54F9B1D2F74E5FFBCBEDCFB319BC52A9741EE96C6F6BEF7BA76FE4DF46EE7E75B7E65AEA78A563E5087B7B7ACFFCEE4CCDC3AB8BF2C3F2F25A4A9264F0ECA05A02D83A8289E6E87FB7FFFEB7F7BB7EBDCABEA5D237A59A378397EE95DA5F6EAF5AABDAA0F4EE74C72B1D03E7066E8EDF9C29CD3C79F264FACEF4C4E4C4CAB0BBF4FB4B9D473A9BB8577DE8C2331F8E4BEF4E3BAC12604B5992A346352FC95599FC6232FF567ED515BACD6E061FB932D2F74EDFC6DFBAE3958EF6B6F66C36DB76B0ADBDAD7DAD199AD2BD52FE54BEF44DF5A457666FA6F7446FFECD7C837735951F965BB3ADCB1F295C28B83C0E60EBA46E2D26C9F8E327B079B9C3CFE4D15471AAE6975A5858E87FBF7FADFF4C73877373E5B9F015BA8F07C75B77BCD2D1F37ACFC0B981A9E254F8822B5F3F7F6ACD2DD5FDEFF7D7FCBBD760E54836FBEB00B029C9F863C1448DEA184C4B664A336BDD4F97D99B295C286CF607D3BBD34BEB6BF5EA89E18F86D76AA6FCA97C5DDE22B4B0B0D0ACB706F87F2B197F6C0F13DB45DBC1B699AF67D6DA0C3E7876B0FDE5F69517AA8CDD18CB1DCEAD5C2F6B7BA9EDFEB7F70BE70BDD47BBEB755441FFBBFD53C5A955CFE11CBD3A7AF2AD93757997F5AD3C4273E0ACC538802D2798D85ED6DF0CDE99EB5CBE19FCE45B277B7B7A577E08AEFFFDFE99AF67B6E248A7DCABB9F947F32B3F3D97FCB599B67427F8D211A0CB1FC99FCA6FD1C955002C2798D876B22F6627FE34313139D1B2AF65E5772FFDFE52F64076ECC658FBCBED551FAD4F9224BD3B3D313931FCE19A6B67CF2F93C94CDF995E585858B942377A75347738B775CD347265A4AA0E4D2F01348660A23E4AF74A2BD7CB9E47D7AFBA66BF9D5D7533F8D26148AB2EC34DDF996ECC152B994CA6FFDDFE99D24CD50A5DE99B52EE70AEBE7F8A252BA797728773A697001A4330511F67DE3BD399EB4CA552AD075A9FE7D8C9E53299CCF087C3EB6C065F2E7F2A5FFCB2D8E04FF8B71D6C9BBE33BDB2993A739D431787D6FAA9DA4C7E31593DBDE428018046114CD459F94139773837F9C564BD5E709DCDE04F152E14AE7D72AD29475DAFDA4C49920C9E1DACEFE196831F0C2EFF32773857DBC15700D4403051A37516832A3F565EEB7AADF348671D57A6D6D90C9E24C9E0D9C1BACFE86C5CDBC1B6EB9F5F5FF978F1CB62F640B62EED387663ACFCA0BCFC11D34B008DE4A46F6A547E583EF4CB43AB9ED0BD5CFE547EE0EC401DB7DAB41E68AD4A87A7B2FBB3F937F33D277A1ABCB367FCD6F8B1EE634992B4EC6B59F50F923B9CBBF6C9B5E71955D56F9D3B9C9BFACFA99A5F0D804D49DD5A144C3C97E2EDE2D23452F176B1FCB0BC6A2E64F666CEBC77E6F4DBA79F7FC9AC78BBD899EB0C9FD67DBCBBF7446FF7D1E0ECEF3A5A0A9AFCA97C4B4BCBD0BFAE3ED755B850A8ED8F307663ACB7A777F923D7C7AEF79CE8A971AC006C92AB51A8A7B9F2DCAA07142DC9EECF5E1FBBFE9CAF1FDE7F52F58E850B85C65C1B727DECFF96E4E6CA736B1D6EB934A489C989CDBE78D59EF7ECFEEC56FC0A00ACC5D528D4DF54716AD5F39396E40EE76AB84465AA38557513CB929BE33707CEC55B79BA8F77DF1CBFB915BFEC72D9FDD9E4AFF7CA2D2C2CACB5DD2AD9D8ED784F4D15AB97DE9EB33B01D82CC1C45659FF436DDDC7BB37580CEBCC2A3D9D68992BCFAD5327CB9FBFA5134E139313D73EBDB6FC91F5DBB170A1B0B0B010BE6C55299A5E02683CC1C4165A5858587FFA67FD62982BCFE54FE5D7FFF1AAE70F9C1B5827509E6ACC84D35303E706D669C79A2D4D6501D000C9F8639BBED95AE587E5BEDFF64DFE47DD8E655AD2F14AC7F49DE955BF357E6B7CF4B3D1F01D2FFFFBE5D36F9FAEEFA8D6527E58EE7DBDF7EE5777EBFBB2B67E033446EAD6A27398D85A4B17C34D15A736725A77A8655FCBE97F3E3D539A59AB969224E93EDA3DF1A78970C2A9F744EF5ADFAABBEC8BD9E93BD36BDD8E57B3F2C3D58F5700A0EE04138D907B3537F3F5CCB54FAFD5BC38D5B2AFE5DAA7D7E61FCD5FFEF8F246EE3FC9BE982D9C2FCC3F9ABF397E73E50EA7FCA97CE38F055FBA1D6F23BBD437C84572000D2398689CFC1BF9F947F39BDDD393DE9D5E4AA5FC1BEB6D695ACBAA134ECD5AC9CA643285F385F5CF5FD888F4EEF4C0B901EB71000D630F134DB0C18D4D2DFB5AF26FE4CFBC77A68EB341E3B7C66FDEBAB9EA4D260D36FAD968DF6FFB16FFB2B8F465E142C16D2700DB9393BE69A6E2ED62EF3FF5AE7A3878FE54FEDA27D71A3FA406AB542A831F0CCE9466F26FE66B9B3F03A0010413CD77E90F97063F187C3AD1B224B337B3F0C342B3860400CBF9941CCDD7FF6EFFFCA3F99ED79FD98E73ECE8B1668D070056124C345F2693B9FEF9F5A9E2D4D256E8AE5F770D7F38DCEC4101C0DF58920300588F253900809860020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008082600808060020008EC4ABE9B6EF6180000B6B1EFA677253F949B3D0A00806DEC87F2AEE4EE64B3470100B08DDD9D4C25E9F493C78F9B3D1000806D2AF5B39FA5922479F2E449B3470200B04DA552299F9203000808260080806002000808260080406A95C7D2E9A4A33B79E9B5E4856CB2BFADE143020068B807A5E48772F2CD4472773C595CACFAE6FF024DC98A7EABBBE7060000000049454E44AE426082";

    private boolean checkFlag;
    private String amount = "11.00";
    private String merchantRefNo;
    // private final static String MAC_ADDRESS = "20:13:08:16:7D:6F";
    private static String MAC_ADDRESS = "macAddress";

    private String paymentType;
    private ListView emvList;
    String paymentoptioncode;
    private static String DEVICE_TYPE = "devicetype";
    // string
    // private QPOSListener qposListener;
    private PaymentInitialization initialization;
    private AcquirerEmiDetailsVO emivo;

    private JSONObject dataObject;
    private String deviceType,custMobile,custName,address,deviceName,deviceMACAddress;
    private int deviceCommMode;
    boolean approved;
    private double latitude,longitude;

    private BluetoothAdapter mBtAdapter;
    private boolean bluetoothOnFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpayment_transaction);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latitude = Double.parseDouble(sharedPreferences.getString(Constants.USER_LAT,"0.0"));
        longitude = Double.parseDouble(sharedPreferences.getString(Constants.USER_LONG,"0.0"));

        address = sharedPreferences.getString(Constants.ADDRESS,"");
       // deviceCommMode = getIntent().getIntExtra(DEVICE_COMMUNICATION_MODE, 0);
        custMobile = getIntent().getStringExtra("custMobile");
        custName = getIntent().getStringExtra("custName");
        deviceType  = getIntent().getStringExtra("devicetype");
        paymentType = getIntent().getStringExtra(PAYMENT_TYPE);
        amount = getIntent().getStringExtra("amount");
        deviceCommMode = getIntent().getIntExtra("deviceCommMode",0);
        deviceName = getIntent().getStringExtra("deviceName");
        deviceMACAddress = getIntent().getStringExtra("deviceMACAddress");
        merchantRefNo = getIntent().getStringExtra("referanceno");
        if (merchantRefNo == null || merchantRefNo.equalsIgnoreCase("null") ||
                merchantRefNo.equalsIgnoreCase(""))
        {
            merchantRefNo = String.valueOf(System.currentTimeMillis());
            editor.putString(Constants.MERCHANT_REF_NO,merchantRefNo);
            editor.commit();
        }

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

       // initiateTransaction();


    }


    public void initiateTransaction() {

       // amount = "2100.00";

        Log.i(TAG,"initiating transaction... paymentType "+paymentType+" merchantRefNo "+merchantRefNo+" deviceType "
        +deviceType+" amount "+amount+" merchantRefNo "+merchantRefNo+" mobile 9000000000");





        try {

            initialization = new PaymentInitialization(MPaymentTransactionActivity.this);
            if(initialization != null){
                Log.i(TAG,"sdk is initialized");
            }
           // initialization.initiateTransaction(handler, DeviceType.N910, amount, paymentType, null,
          //          "9000000000", 71.000001, 17.000001, merchantRefNo, null, null);

          /*  initialization.initiateCashTransaction(handler, amount, paymentType, "POS",
                    null, null, 71.000001, 17.000001,
                    merchantRefNo, "", "");*/

            /*initialization.initiateTransaction(handler, deviceType, address, amount, paymentType,
                    PaymentTransactionConstants.SALE,custMobile, custName,
                    latitude, longitude, merchantRefNo, null,
                    deviceCommMode
                    , merchantRefNo, appName,appVersion);*/

            try {

                initialization.initiateTransaction(handler, deviceName, deviceMACAddress, amount, paymentType,
                        PaymentTransactionConstants.SALE, custMobile, custName, latitude, longitude, merchantRefNo,
                        null, deviceCommMode, merchantRefNo, appName, appVersion);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }



         /*   initialization.initiateTransaction(handler, DeviceType.N910, "address", amount, paymentType,
                    "POS",
                    null, null, 71.000001, 17.000001,
                    merchantRefNo, null, deviceCommMode, merchantRefNo, "", "");*/

            //initialization.initiateTransaction(handler, DeviceType.N910, amount, paymentType, null,
            //        "9000000000", 71.000001, 17.000001, merchantRefNo, null, null);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTransDetails(ICCTransactionResponse iccTransactionResponse){
        Log.i(TAG,"Getting details..");
        try {
            PaymentInitialization initialization = new PaymentInitialization(
                    MPaymentTransactionActivity.this);
            initialization.initiateTransactionDetails(handler, iccTransactionResponse.getReferenceNumber(),
                    merchantRefNo,appName,appVersion);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }



    // The Handler that gets information back from the PaymentProcessThread
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

           // Log.i(TAG,"handler response... "+(String) msg.obj);

            if (msg.what == SOCKET_NOT_CONNECTED) {
                alertMessage("Socket not connected");
            }else if (msg.what == CHIP_TRANSACTION_APPROVED || msg.what == SWIP_TRANSACTION_APPROVED) {

                Log.i(TAG,"Chip transaction approved..");

                ICCTransactionResponse vo = (ICCTransactionResponse) msg.obj;

                Log.i(TAG,"Response "+vo.getResponseCode()+" "+vo.getResponseMessage());

                if (vo.isSignatureRequired()) {
                   /* Intent signature = new Intent(MPaymentTransactionActivity.this, SignatureCapture.class);
                    signature.putExtra("vo", vo);
                    signature.putExtra(PAYMENT_TYPE, paymentType);
                    finish();
                    startActivity(signature);*/

                } else {

                    getTransDetails(vo);
                }

            }else if (msg.what == SUCCESS) {
                //HostResponse response = (HostResponse) msg.obj;
                String data = null;
                try {
                   // data = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response);
                    List<TransactionStatusResponse> transactionStatusResponse = (List<TransactionStatusResponse>) msg.obj;
                    data = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(transactionStatusResponse.get(0));
                    JSONObject detailObject = new JSONObject(data);
                    Log.i(TAG,"data "+data);
                    String tranStatus = detailObject.getString("transactionStatus");
                    if(tranStatus.equals("Approved")){
                        approved = true;
                    }else{
                        approved = false;
                    }
                    //approved = dataObject.getBoolean("approved");
                   /* cardLevel = dataObject.getJSONObject("hostResponse").getString("cardLevel");
                    merchantRefNo = dataObject.getString("merchantRefInvoiceNo");
                    transId = dataObject.getString("transactionId");*/
                    detailObject.put("approved",approved);
                  //  detailObject.put("cardLevel",cardLevel);
                    detailObject.put("merchantRefInvoiceNo",merchantRefNo);
                    detailObject.put("transactionId",detailObject.getString("rrn"));
                    detailObject.put("transactionType",detailObject.getString("paymentMethod"));
                    detailObject.put("date",detailObject.getString("transactionDateNTime"));
                    // invNo = dataObject.getString("invoiceNo");
                    //  generateJson("Credit/Debit Card");
                    Intent i = new Intent(MPaymentTransactionActivity.this, MPayTransactionDetailsActivity.class);
                    i.putExtra("paymentResponseObject", detailObject.toString());
                    i.putExtra("txnResponse", transactionStatusResponse.get(0));
                    i.putExtra("orderNumber", getIntent().getStringExtra("orderNumber"));
                    i.putExtra("deviceType", getIntent().getStringExtra("deviceType"));
                    i.putExtra("totalAmount", amount);
                    i.putExtra("flag",getIntent().getStringExtra("flag"));
                    i.putExtra("ordCouponId",getIntent().getStringExtra("ordCouponId"));
                    i.putExtra("totalTax",getIntent().getFloatExtra("totalTax",0f));
                    i.putExtra("deliveryCharges",getIntent().getFloatExtra("deliveryCharges",0f));
                    i.putExtra("totDiscount",getIntent().getFloatExtra("totDiscount",0f));
                    i.putExtra("custCode", getIntent().getStringExtra("custCode"));
                    i.putExtra("custId", getIntent().getIntExtra("custId", 0));
                    i.putExtra("custName", getIntent().getStringExtra("custName"));
                    i.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
                    i.putExtra("custImage", getIntent().getStringExtra("custImage"));
                    i.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
                    MPaymentTransactionActivity.this.startActivity(i);
                    finish();
                } catch (JsonGenerationException e1) {
                    e1.printStackTrace();
                } catch (JsonMappingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (msg.what == TRANSACTION_INITIATED) {
                Toast.makeText(MPaymentTransactionActivity.this, "Transaction initiated", Toast.LENGTH_LONG).show();

            }else if (msg.what == TXN_COMPLETED) {
                Toast.makeText(MPaymentTransactionActivity.this, "Transaction Completed", Toast.LENGTH_LONG).show();

            } else if (msg.what == TRANSACTION_FAILED) {
                TransactionVO vo = (TransactionVO) msg.obj;
                Toast.makeText(MPaymentTransactionActivity.this, "Transaction Status : " + vo.getStatus(),
                        Toast.LENGTH_LONG).show();
                finish();
            } else if (msg.what == TRANSACTION_INITIATED) {
                Toast.makeText(MPaymentTransactionActivity.this, "Transaction initiated", Toast.LENGTH_LONG).show();
            }

            else if (msg.what == ERROR_MESSAGE) {
                ICCTransactionResponse vo = (ICCTransactionResponse) msg.obj;

                Log.i(TAG,"Response "+vo.getResponseCode()+" "+vo.getResponseMessage()+" "+vo.getTransactionStatus());
                /*if ((String) msg.obj != "") {
                    Toast.makeText(MPaymentTransactionActivity.this, "Error message  " + (String) msg.obj,
                            Toast.LENGTH_LONG).show();
                }
                finish();*/
            } else if (msg.what == TRANSACTION_PENDING) {

                Toast.makeText(MPaymentTransactionActivity.this, "Transaction pending" + "Pending status", Toast.LENGTH_LONG)
                        .show();
                // finish();
            } else if (msg.what == DISPLAY_STATUS) {
               // Toast.makeText(MPaymentTransactionActivity.this, "Display status", Toast.LENGTH_LONG).show();
            } else if (msg.what == PIN_ENTERED) {
                Toast.makeText(MPaymentTransactionActivity.this, "Pin entered", Toast.LENGTH_LONG).show();
            }

        };
    };

    public void alertMessage(String message) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert").setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        finish();
                    }
                }).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bluetooth is enabled or not if not then asks for enable
        if (!checkFlag) {
            if (mBtAdapter != null) {
                mBtAdapter.cancelDiscovery();
                if (!mBtAdapter.isEnabled()) {
                    bluetoothOnFlag = false;
                    Intent enableIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {
                    bluetoothOnFlag = true;
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkFlag) {
            if (bluetoothOnFlag) {
                initiateConnection();
            }
        }
    }

    private void initiateConnection() {

        isDeviceON(getString(R.string.pls_chk_cardreader_availble));
    }

    public void isDeviceON(String message) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);

        }
        builder.setTitle("Alert").setMessage(message)
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        initiateTransaction();
                    }
                }).show();
    }

}

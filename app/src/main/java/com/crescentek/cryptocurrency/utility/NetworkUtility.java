package com.crescentek.cryptocurrency.utility;

/**
 * Created by R.Android on 16-07-2018.
 */

public class NetworkUtility {

    //http://139.162.203.124:3001/icons/BC_Logo_.png

    public static String BASEURL ="http://139.162.203.124:3001";
    public static String REGISTRAION= BASEURL +"/auth";
    public static String EMAIL_ACTIVATION=BASEURL+"/users/activationEmail";
    public static String TRANSACTION_PIN=BASEURL+"/users/setTransactPin";
    public static String LOGIN=BASEURL+"/auth/login";
    public static String GET_COUNTRY=BASEURL+"/auth/countries";
    public static String SEND_ACTIVATION_OTP =BASEURL+"/users/sendActivationOTP";
    public static String ACTIVATION_OTP=BASEURL+"/users/activationOTP";
    public static String VERIFY_CODE=BASEURL+"/auth/verify/pwdcode";
    public static String RESET_PASSWORD=BASEURL+"/auth/reset/password";
    public static String RESEND_EMAIL=BASEURL+"/auth/resend/code";
    public static String PASSWORD_USER=BASEURL+"/users/reset/password";
    public static String PERSONAL_DETAILS=BASEURL+"/users/personalDetails";//Use In Home Screen
    public static String TWO_FACTOR=BASEURL+"/users/new/qrcode";
    public static String TWO_FACTOR_DISABLE=BASEURL+"/users/disableTwoFA";
    public static String VERIFY_2FA=BASEURL+"/users/verify2faToken/";
    public static String IMAGE_UPLOAD=BASEURL+"/users/kyc/upload";
    public static String LEVEL_DETAILS=BASEURL+"/users/level/details";
    public static String WALLET=BASEURL+"/transact/wallet/list";
    public static String HOME_CONTENT=BASEURL+"/services/contents/home";
    public static String TRANSACTION_REFERANCE=BASEURL+"/transact/deposit/trx/";
    public static String CHECK_USER=BASEURL+"/transact/checkUser";
    public static String SEND_CRYPTO=BASEURL+"/transact/send/crypto";
    public static String TRANSACTION_FEE=BASEURL+"/transact/fees";
    public static String BUY_CRYPTO=BASEURL+"/wallet/buyCrypto";
    public static String SELL_CRYPTO=BASEURL+"/wallet/sellCrypto";
    public static String ORDER_EXCHANGE=BASEURL+"/order";
    public static String ORDER_BUY_SELL=BASEURL+"/order/all/buy";
    public static String ORDER_BUY_SELL_TRADE=BASEURL+"/order/list";
    public static String ORDER_ALL_LIST=BASEURL+"/order/list/all/";
    public static String SERVICE_ALERT=BASEURL+"/services/alert";
    public static String DELETE_SERVICE_ALERT=BASEURL+"/services/alert/delete/";
    public static String REFFERAL_CODE=BASEURL+"/users/refferalCode";
    public static String SERVICE_ALERT_LIST=BASEURL+"/services/alert/";
    public static String USERS_CLAIM=BASEURL+"/users/claim";
    public static String ORDER_TRADES=BASEURL+"/order/trades/";
    public static String PROFILE_DETAILS=BASEURL+"/users/profile/details";
    public static String BANK_LIST=BASEURL+"/transact/bank/list";
    public static String GET_RATE=BASEURL+"/transact/crypto/getCryptoRate?crypto_id";
    public static String CANCEL_ORDERBOOK=BASEURL+"/order/cancel/";
    public static String TERM_CONDITION=BASEURL+"/services/content/terms_and_conditions";
    public static String PRIVACY_POLICY=BASEURL+"/services/content/privacy_and_policy";
    public static String TRANSACTION_RECORD=BASEURL+"/transact/all/trx";
    public static String FUND_WITHDRAW=BASEURL+"/wallet/fundWithdraw";
    public static String ADD_BANK_ACCOUNT=BASEURL+"/transact/add/account";
    public static String BANK_LIST_USER=BASEURL+"/users/bankList";
    public static String VERIFY_BVN=BASEURL+"/users/verifyBvn";//Implement At VerifyPersonalDetailActivity
    public static String HELP_CENTER=BASEURL+"/services/getQAT";
    public static String TRX_LIMIT=BASEURL+"/transact/trx/limit/";
    public static String IMAGE_URL="http://139.162.203.124:3001/icons/";

    public static String GET_BTC_RATE=BASEURL+"/transact/crypto/getCryptoRate?crypto_id=1";// by D 13/09/2018

}

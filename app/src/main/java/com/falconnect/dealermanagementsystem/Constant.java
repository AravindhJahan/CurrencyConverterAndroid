package com.falconnect.dealermanagementsystem;


public class Constant {

    //Buy Page API
    public static final String LOGIN_API = ConstantIP.IP + "user_login?";
    public static final String FORGOT_PASSWORD_API = ConstantIP.IP + "forgot_password?";
    public static final String DASH_BOARD_SPINNER_API = ConstantIP.IP +"apibuy";
    public static final String DASH_BOARD_SUB_SPINNER_API = ConstantIP.IP + "apibuyid?";
    public static final String CHANGE_PASSWORD_API = ConstantIP.IP + "change_password?";
    public static final String SEARCH_CAR_LISTING_API = ConstantIP.IP + "apisearchcarlisting?";
    public static final String SAVED_CAR_API = ConstantIP.IP + "apiview_savedcars?";
    public static final String SAVE_CAR_API = ConstantIP.IP + "api_save_car?";
    public static final String QUREIS_PAGE_API = ConstantIP.IP + "api_queries_car?";
    public static final String BIDS_POSTED_API = ConstantIP.IP + "api_bidding_list?";
    public static final String BIDS_POSTING_LISTVIEW_API = ConstantIP.IP + "api_bidding_viewmore?";
    public static final String NEW_BID_POSTING_AMOUNT_API = ConstantIP.IP + "api_addbidding_viewmore?";
    public static final String APPLY_FUNDING_APT = ConstantIP.IP + "doApi_viewfundingpage?";
    public static final String REVOKE_FUNDING_BUY = ConstantIP.IP + "doApi_revokefunding?";
    public static final String APPLY_FUNDING_SELECT_CAR = ConstantIP.IP + "api_view_fundingcar?";
    public static final String SEARCH_TEXTBOX = ConstantIP.IP + "apisearchcarlisting?";
    public static final String ADD_ALERT_API = ConstantIP.IP + "api_alert_car?";
    public static final String TOP_NOTE_API = ConstantIP.IP + "api_dosearch_tagfilter?";

    public static final String APPLY_LOAN_SELECT_CAR = ConstantIP.IP + "api_view_loancar?";

    public static final String DOCUMENT = ConstantIP.IP + "api_document_contact?";

    //Sell Page API
    public static final String APPLY_LOAN = ConstantIP.IP + "doApi_viewloanpage?";
    public static final String MY_POSTING = ConstantIP.IP + "view_mypost_list?";
    public static final String MY_INVENTORY = ConstantIP.IP + "view_inventory_dashboard?";
    public static final String MY_POSTING_DETAIL_HEADER = ConstantIP.IP + "view_mypost_details?";
    public static final String REVOKE_SELL = ConstantIP.IP + "api_revoke_loanlist?";
    public static final String QUERIES_RECEIVED = ConstantIP.IP + "api_queries_received?";
    public static final String MY_POSTING_DETAIL_DELETE = ConstantIP.IP + "api_postingtype_status?";
    public static final String MY_POSTING_DETAIL_REPOST = ConstantIP.IP + "api_postingtype_status?";
    public static final String MY_INVENTORY_DELETE = ConstantIP.IP + "api_inventory_delete?";


    //Manage Page API
    public static final String USER_VIEW = ConstantIP.IP + "api_view_user?";
    public static final String MY_USER_TAB_VIEW = ConstantIP.IP + "api_user_dashboard?";
    public static final String MYBRANCHESLIST = ConstantIP.IP + "api_branch_list?";
    public static final String CONTACTLIST = ConstantIP.IP + "api_view_allcontact?";
    public static final String BUSSINESSPROFILE = ConstantIP.IP + "api_business_profile?";
    public static final String EMPLOYEE_VIEW = ConstantIP.IP + "api_view_employee?";

    //Funding
    public static final String GET_DEALER_BRANCH = ConstantIP.IP + "api_get_dealerbranch?";


    //ADD APIS
    public static final String ADD_EMPLOYEE = ConstantIP.IP + "api_add_employee?";
    public static final String ADDBRANCHES = ConstantIP.IP + "api_add_branch?";
    public static final String ADDCONTACT = ConstantIP.IP + "api_add_contact?";
    public static final String ADD_USER_VIEW = ConstantIP.IP + "api_add_user?";


    //EDIT APIS
    public static final String EDITBUSSINESSPROFILE = ConstantIP.IP + "api_update_business_profile?";
    public static final String EDITUSER = ConstantIP.IP + "api_update_user?";
    public static final String EDITCONTACT = ConstantIP.IP + "api_update_contact?";
    public static final String EDITBRANCHES = ConstantIP.IP + "api_edit_branch?";
    public static final String EDITACCOUNT = ConstantIP.IP + "edit_account?";
    public static final String EDIT_EMPLOYEE = ConstantIP.IP + "api_edit_employee?";

    ///DELETE APIS
    public static final String DELETE_BRANCH = ConstantIP.IP + "api_delete_branch?";
    public static final String DELETE_CONTACT = ConstantIP.IP + "api_delete_contact?";
    public static final String DELETE_USER = ConstantIP.IP + "api_detele_user?";
    public static final String DELETE_EMPLOYEE = ConstantIP.IP + "api_delete_employee?";


    ///Notification
    public static final String ALERT_SIDEBAR = ConstantIP.IP + "api_alert_doshow?";
    public static final String QUERIES_SIDEBAR = ConstantIP.IP + "api_alert_notification_show?";
    public static final String ALERT_PAGEAPI = ConstantIP.IP + "api_alert_history?";
    public static final String ALERT_REVOKE = ConstantIP.IP + "api_alert_statusrevoke?";
    public static final String ALERT_DELETE = ConstantIP.IP + "api_alert_revoke?";

    //Confirmation
    public static final String CONFIRMATION_API_FUND = ConstantIP.IP + "api_apply_fundingcar?";
    public static final String CONFIRMATION_API_LOAN = ConstantIP.IP + "api_apply_loancar?";

    public static final String FILTER_API = ConstantIP.IP + "api_buy_filter?";
    public static final String FILTER_DETAIL_API = ConstantIP.IP + "api_search_filter?";
    public static final String FILTER_SELECT_API = ConstantIP.IP + "api_listunselected_filter?";
    public static final String FILTER_FINAL_API = ConstantIP.IP + "api_searchcar_filter?";

    /////Dealre Search
    public static final String DEALER_SEARCH = ConstantIP.IP + "api_dealer_search?";


    //Status
    public static final String USER_STATUS = ConstantIP.IP + "api_invokestatus_user?";

    //Search_EMPLOYEE
    public static final String EMPLOYEE_SEARCH = ConstantIP.IP + "api_dosearchemployee?";

    //Search_USER
    public static final String USER_SEARCH = ConstantIP.IP + "api_search_user?";

    //Search_CONTACT
    public static final String CONTACT_SEARCH = ConstantIP.IP + "api_search_contact?";

    //Leads Save
    public static final String LEADS_SAVE = ConstantIP.IP + "api_leadsdetails_contact?";
}


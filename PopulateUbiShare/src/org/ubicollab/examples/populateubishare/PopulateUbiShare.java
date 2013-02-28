package org.ubicollab.examples.populateubishare;


import java.util.Date;

import org.societies.android.api.cis.SocialContract;
import org.societies.thirdpartyservices.ijacketlib.IJacketDefines;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PopulateUbiShare extends Activity {

	private static String LOG_TAG = "populateUbiShare";
	ContentResolver cr;
	
	String accountName = "";
	private static String accountType = "com.box";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
		 setContentView(R.layout.main);
		
		cr = this.getApplication().getContentResolver();

		
/*		this.populateMe("tcarlyle@gmail.com");
		this.populatePeople("kato.stoelen@gmail.com", "Kato St�len", "kato.stoelen@gmail.com", "Kato@BOX");
		this.populatePeople("babak@farshchian.com", "Babak Farshchian", "babak@farshchian.com", "Babak@BOX");

		
		this.populateCommunity("tcarlyle@gmail.com", "football", "Flamengo", "Flamengo Supporters");//String ownerUserName, String type, String name, String description
		this.populateCommunity("tcarlyle@gmail.com", "football", "AC Milan", "AC Milan Supporters");//String ownerUserName, String type, String name, String description

		this.populateService("tcarlyle@gmail.com", "org.societies.thirdpartyservices.ijacketClient", "Disaster", "IjacketClient", "IjacketClient description", "false", "Ijacket", "", "http://files.ubicollab.net/apk/iJacket.apk");
		this.populateService("tcarlyle@gmail.com", "org.societies.thirdpartyservices.ijacket", "Disaster", "Ijacket", "Ijacket description", "false", "", "", "http://files.ubicollab.net/apk/iJacketClient.apk");
		
		Toast.makeText(PopulateUbiShare.this, "done populating", Toast.LENGTH_SHORT).show();*/
	}

	public void populateIDisasterDataSet(){
		
		//long idThom = this.populatePeople("tcarlyle@gmail.com", "Thomas Roedhale", "tcarlyle@gmail.com", "aaa");
		long idKnut = this.populatePeople("knut.roedhale@gmail.com", "Knut Roedhale", "knut.roedhale@gmail.com", "rescue team leader");
		long idKari = this.populatePeople("kari.rosevinger@gmail.com", "Kari  Rosevinger", "kari.rosevinger@gmail.com", "Rescue member (medic)");
		long idOla = this.populatePeople("ola.gullkjede@gmail.com", "Ola Gullkjede", "ola.gullkjede@gmail.com", "Rescue member (mechanics)");
		long idAage = this.populatePeople("aage.lillefot@gmail.com", "Aage Lillefot", "aage.lillefot@gmail.com", "Rescue member (network responsible)");
		long idUnni = this.populatePeople("unni.stornese@gmail.com", "Unni Stornese", "unni.stornese@gmail.com", "Rescue member (cook)");
		

		
		long iJacketID =  populateService("org.societies.thirdpartyservices.ijacket","SocialContract.ServiceConstants.SERVICE_TYPE_PROVIDER", 
		 		"iJacket", "A service to communicate with the smart Jacket","SocialContract.ServiceConstants.SERVICE_NOT_INSTALLED"
		 		,"org.societies.thirdpartyservices.ijacketclient","","http://files.ubicollab.org/app/iJacket.apk",0);

		long iJacketClientID =  populateService("org.societies.thirdpartyservices.ijacketclient","SocialContract.ServiceConstants.SERVICE_TYPE_CLIENT", 
		 		"iJacketClient", "A service client for remote control of the smart Jacket","SocialContract.ServiceConstants.SERVICE_NOT_INSTALLED"
		 		,"","","http://files.ubicollab.org/app/iJacketClient.apk",0);
			
	}
	
	public void populateIJacketClienDataSet(){
		//long idThomas = this.populatePeople("tcarlyle@gmail.com", "Thomas Vilarinho", "tcarlyle@gmail.com", "no description");
		long idThomas = addsMeToPeople();
		
		long idKari = this.populatePeople("kari.rosevinger@gmail.com", "Kari  Rosevinger", "kari.rosevinger@gmail.com", "Rescue member (medic)");
		

		
		long iJacketID =  populateService(IJacketDefines.AccountData.IJACKET_SERVICE_NAME,"SocialContract.ServiceConstants.SERVICE_TYPE_PROVIDER", 
		 		"iJacket", "A service to communicate with the smart Jacket","SocialContract.ServiceConstants.SERVICE_NOT_INSTALLED"
		 		,"org.societies.thirdpartyservices.ijacketclient","","http://files.ubicollab.org/app/iJacket.apk",0);

		long iJacketClientID =  populateService(IJacketDefines.AccountData.IJACKET_CLIENT_SERVICE_NAME,"SocialContract.ServiceConstants.SERVICE_TYPE_CLIENT", 
		 		"iJacketClient", "A service client for remote control of the smart Jacket","SocialContract.ServiceConstants.SERVICE_NOT_INSTALLED"
		 		,"","","http://files.ubicollab.org/app/iJacketClient.apk",0);
		
		long fireOperationID = populateCommunity(idThomas, "disaster", "Fire in Mukegata", "Fire extinguish operation in Munkegata");

		long earthquakeOperation  = populateCommunity(idThomas, "disaster", "Earthquake in Mexico City", "Earthquake in Mexico City");

		long tsunamiOperation  = populateCommunity(idThomas, "disaster", "Tsunami in Haugesund", "Tsunami opperation in Haugesund");
		
		long iJacketInFire = populateServInCommunity(fireOperationID, idThomas, iJacketID);

		long iJacketTsunami = populateServInCommunity(tsunamiOperation, idThomas, iJacketID);

		
	}

    /** Called when the user touches the button */
    public void clickButton(View view) {
    	accountName = fetchAccountName();
    	cleanUpDb();
    	//populateIJacketClienDataSet();
    	populateIDisasterDataSet();
    	Toast.makeText(PopulateUbiShare.this, "done populating", Toast.LENGTH_SHORT).show();
    }

	
	
	/**
	 * It will clean up the db except for the ME and the user
	 * 
	 * @return 0 if it failed, and 1 if it worked
	 */
	
    private int cleanUpDb(){

    	Log.d(LOG_TAG, "cleaning up db");
    	Uri uri;
		int me_id;
		int me_people_id;
		// get ME 
		
		/*uri =  Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.ME);
    	String mSelectionClause = SocialContract.Me.ACCOUNT_TYPE + " = ?";
    	String[] mSelectionArgs = {accountType};
		
    	Cursor cursor = cr.query(uri,null,mSelectionClause,mSelectionArgs,null);
       	if (null == cursor || cursor.getCount() < 1){
       		Log.d(LOG_TAG, "couldnt find ME account of type " + accountType);
       		return 0;
       	}else{
       		// user is already there, if it is pending, Ill change it to not pending
       		me_id  = cursor.getColumnIndex(SocialContract.Me._ID);
    		me_people_id  = cursor.getColumnIndex(SocialContract.Me._ID_PEOPLE);
    		
       	}*/
       	
       	// FIRST I CLEAN THE NON SYNCHRONIZED TABLES, BY DELETING
       	
       	
       	// clean PEOPLE table 
       	
		String	mSelectionClause = SocialContract.People._ID + " LIKE ?";
		String[]  mSelectionArgs = {"%"};
    	int nb = cr.delete(SocialContract.People.CONTENT_URI,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " people deleted");
       	
    	// clean Services table 
    	mSelectionClause = SocialContract.Services._ID + " LIKE ?";
    	nb = cr.delete(SocialContract.Services.CONTENT_URI,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " Services deleted");
       	
    	// CLEAN SYNCHRONIZED tables; BY SETTING THE DELETE FLAG TO 1
    	
    	// clean Communities table 
    	mSelectionClause = SocialContract.Communities._ID + " LIKE ?";
    	ContentValues mUpdateValues = new ContentValues();
    	mUpdateValues.put(SocialContract.Communities.DELETED, 1);
    	nb = cr.update(SocialContract.Communities.CONTENT_URI,mUpdateValues,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " Communities deleted");
    	
    	// clean CommunityActivity table 
    	mSelectionClause = SocialContract.CommunityActivity._ID + " LIKE ?";
    	mUpdateValues = new ContentValues();
    	mUpdateValues.put(SocialContract.CommunityActivity.DELETED, 1);
    	nb = cr.update(SocialContract.CommunityActivity.CONTENT_URI,mUpdateValues,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " CommunityActivity deleted");
    	
    	// clean Membership table 
    	mSelectionClause = SocialContract.Membership._ID + " LIKE ?";
    	mUpdateValues = new ContentValues();
    	mUpdateValues.put(SocialContract.Membership.DELETED, 1);
    	nb = cr.update(SocialContract.Membership.CONTENT_URI,mUpdateValues,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " Membership deleted");

    	// clean Sharing table 
    	mSelectionClause = SocialContract.Sharing._ID + " LIKE ?";
    	mUpdateValues = new ContentValues();
    	mUpdateValues.put(SocialContract.Sharing.DELETED, 1);
    	nb = cr.delete(SocialContract.Sharing.CONTENT_URI,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " Sharing deleted");

    	// clean Relationship table 
    	mSelectionClause = SocialContract.Relationship._ID + " LIKE ?";
    	mUpdateValues = new ContentValues();
    	mUpdateValues.put(SocialContract.Relationship.DELETED, 1);
    	nb = cr.update(SocialContract.Relationship.CONTENT_URI,mUpdateValues,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " Relationship deleted");
    	
    	return 1;
    }

	
    
    private String fetchAccountName(){
		Uri uri;

		// check if my account is on ME and is not pending
		
		uri =  Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.ME);
    	String mSelectionClause = SocialContract.Me.ACCOUNT_TYPE + " = ?";
    	String[] mSelectionArgs = {accountType};
		
    	Cursor cursor = cr.query(uri,null,mSelectionClause,mSelectionArgs,null);
       	if (null == cursor || cursor.getCount() < 1){
    			Log.d(LOG_TAG, "couldnt retrieve account name from ME table");
    			return "";
       	}else{
       		// user is already there, if it is pending, Ill change it to not pending
    		int i  = cursor.getColumnIndex(SocialContract.Me.ACCOUNT_NAME);
    		if( i == -1){
    			Log.d(LOG_TAG, "couldnt get Account name collumn from ME table");
    			return "";
    		}
    		String temp = "";
    		cursor.moveToFirst();
    		temp =	cursor.getString(i);
    		return temp;
       	}
    }

	
	
			
	
    /**
     * 
     * It will insert a people based on the input parameters and  
     * using the account specified in the main application
     * 
     * @param userName (same as global_id)
     * @param name
     * @param email
     * @param description
     * @return
     */
    
	private long populatePeople(String userName, String name, String email, String description) {
		
		Log.d(LOG_TAG, "going to populate " + userName);
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(SocialContract.People.GLOBAL_ID , userName);
		initialValues.put(SocialContract.People.USER_NAME , userName);
		initialValues.put(SocialContract.People.NAME , name);
		initialValues.put(SocialContract.People.EMAIL , email);
		initialValues.put(SocialContract.People.DESCRIPTION , description);
		initialValues.put(SocialContract.People.DIRTY , 1);
		initialValues.put(SocialContract.People.ACCOUNT_NAME , accountName);
		initialValues.put(SocialContract.People.ACCOUNT_TYPE , accountType);
		initialValues.put(SocialContract.People.CREATION_DATE, new Date().getTime() / 1000);
		initialValues.put(SocialContract.People.LAST_MODIFIED_DATE, new Date().getTime() / 1000);

		Uri uri = Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.PEOPLE);
		Uri responseURI = cr.insert(uri,initialValues);
		
		if(null == responseURI){
			Log.d(LOG_TAG, "failed trying to add the user on People table");
			return 0;
		}

		return ContentUris.parseId(responseURI);
	}
	

	/**
	 *  This method first retrieves the user (based on the ownerUserName field)
	 * and then creates a service with that user as the owner
	 * 
	 * @param ownerUserName
	 * @param serviceGlobalID
	 * @param serviceType
	 * @param serviceName
	 * @param description
	 * @param available
	 * @param dependency
	 * @param config
	 * @param url
	 * @return
	 */
	
	private long populateService(String globalID, String serviceType, 
			String serviceName,	String description, String available, String dependency, String config, 
			String url, long ownerId) {
		
		
		Log.d(LOG_TAG, "going to populate " + serviceName);
		
		ContentValues initialValues = new ContentValues();

		initialValues.put(SocialContract.Services.GLOBAL_ID , globalID);
		initialValues.put(SocialContract.Services.TYPE , serviceType);
		initialValues.put(SocialContract.Services.NAME , serviceName);
		initialValues.put(SocialContract.Services._ID_OWNER, ownerId);
		initialValues.put(SocialContract.Services.DESCRIPTION , description);
		initialValues.put(SocialContract.Services.AVAILABLE, available);
		initialValues.put(SocialContract.Services.DEPENDENCY, dependency);
		initialValues.put(SocialContract.Services.CONFIG, config);
		initialValues.put(SocialContract.Services.URL, url);
		initialValues.put(SocialContract.Services.DIRTY, 1);
		initialValues.put(SocialContract.Services.ACCOUNT_NAME , accountName);
		initialValues.put(SocialContract.Services.ACCOUNT_TYPE , accountType);
	
		Uri uri = Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.SERVICES);
		Uri responseURI = cr.insert(uri,initialValues);
		
		if(null == responseURI){
			Log.d(LOG_TAG, "failed trying to add the user on Communities table");
			return 0;
		}

		return ContentUris.parseId(responseURI);	
	}

	
	/**
	 * 
	 * This method populates a community and the membership table as well
	 * 
	 * @param ownerUserName
	 * @param type
	 * @param name
	 * @param description
	 * @return community_ID
	 */
	
	private long populateCommunity(long owner, String type, String name, String description){
		
		
		Log.d(LOG_TAG, "going to populate " + name);
	    // now we add the community
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(SocialContract.Communities.GLOBAL_ID , SocialContract.GLOBAL_ID_PENDING);
		initialValues.put(SocialContract.Communities.TYPE , type);
		initialValues.put(SocialContract.Communities.NAME , name);
		initialValues.put(SocialContract.Communities._ID_OWNER, owner);
		initialValues.put(SocialContract.Communities.DESCRIPTION , description);
		initialValues.put(SocialContract.Communities.DIRTY , 1);
		initialValues.put(SocialContract.Communities.ACCOUNT_NAME , accountName);
		initialValues.put(SocialContract.Communities.ACCOUNT_TYPE , accountType);
		
		Uri uri = Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.COMMUNITIES);
		Uri responseURI = cr.insert(uri,initialValues);
		
		if(null == responseURI){
			Log.d(LOG_TAG, "failed trying to add the user on Communities table");
			return 0;
		}
		
		long community_id = ContentUris.parseId(responseURI);
		
		initialValues = new ContentValues();
		initialValues.put(SocialContract.Membership.ACCOUNT_NAME , accountName);
		initialValues.put(SocialContract.Membership.ACCOUNT_TYPE , accountType);
		initialValues.put(SocialContract.Membership.DIRTY , 1);
		initialValues.put(SocialContract.Membership._ID_COMMUNITY, community_id);
		initialValues.put(SocialContract.Membership._ID_MEMBER,owner);
		initialValues.put(SocialContract.Membership.TYPE,"owner");

		uri = Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.MEMBERSHIP);
		responseURI = cr.insert(uri,initialValues);
		
		if(null == responseURI){
			Log.d(LOG_TAG, "failed trying to add the user as owner of ther community");
			return 0;
		}

		
		return community_id;
	}
	
	/**
	 * 
	 * share a service in a community
	 * 
	 * @param communityID
	 * @param ownerID
	 * @param serviceID
	 * @return sharing_ID
	 */
	
	private long populateServInCommunity(long communityID, long ownerID, long serviceID){
		
		
		Log.d(LOG_TAG, "going to populate " + serviceID + " shared on " + communityID);
	    // now we add the community
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(SocialContract.Sharing._ID_COMMUNITY, communityID);
		initialValues.put(SocialContract.Sharing._ID_OWNER , ownerID);
		initialValues.put(SocialContract.Sharing._ID_SERVICE , serviceID);
		initialValues.put(SocialContract.Sharing.ACCOUNT_NAME , accountName);
		initialValues.put(SocialContract.Sharing.ACCOUNT_TYPE , accountType);
		initialValues.put(SocialContract.Sharing.DIRTY , 1);
		
		Uri uri = Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.SHARING);
		Uri responseURI = cr.insert(uri,initialValues);
		
		if(null == responseURI){
			Log.d(LOG_TAG, "failed trying to add service on Sharing table");
			return 0;
		}

		return ContentUris.parseId(responseURI);
	}

	
	// return People ID
    private long addsMeToPeople(){
		if(accountName.equals(""))
			accountName = fetchAccountName();
		long idPeople_forMe = this.populatePeople(accountName, "My Name", accountName, "no description");

    	// update ME table 
    	String mSelectionClause = SocialContract.Me.ACCOUNT_NAME + " = ?";
    	String [] mSelectionArgs = {accountName};
    	ContentValues mUpdateValues = new ContentValues();
    	mUpdateValues.put(SocialContract.Me._ID_PEOPLE, idPeople_forMe);
    	int nb = cr.update(SocialContract.Me.CONTENT_URI,mUpdateValues,mSelectionClause,mSelectionArgs);
    	Log.d(LOG_TAG, nb + " me account updated");
		
		return idPeople_forMe;
       		
       	}

	
	
	// METHODS TO BE REWORKED
	
	/**
	 * It will insert the the username on the ME table and in the people table if not there.
	 * This method assumes that:
	 * userName and accountName are the same and we will set display name to that
	 * 
	 * The account name and user are taken from this application configuration
	 * 
	 * @return 0 if it failed, and 1 if it worked
	 */
	
	
    private int populateMe(String userName){
		ContentValues initialValues = new ContentValues();
		Uri uri;
		Uri responseURI;

		// check if my account is on ME and is not pending
		
		uri =  Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.ME);
    	String mSelectionClause = SocialContract.Me.USER_NAME + " = ?";
    	String[] mSelectionArgs = {userName};
		
    	Cursor cursor = cr.query(uri,null,mSelectionClause,mSelectionArgs,null);
       	if (null == cursor || cursor.getCount() < 1){
    		initialValues.put(SocialContract.Me.GLOBAL_ID , userName);
    		initialValues.put(SocialContract.Me.NAME , userName);
    		initialValues.put(SocialContract.Me.ACCOUNT_NAME , accountName);
    		initialValues.put(SocialContract.Me.DISPLAY_NAME , userName);
    		initialValues.put(SocialContract.Me.USER_NAME , userName);
    		initialValues.put(SocialContract.Me.ACCOUNT_TYPE , accountType);
    		
    		uri = Uri.parse(SocialContract.AUTHORITY_STRING + SocialContract.UriPathIndex.ME);
    		responseURI = cr.insert(uri,initialValues);
    		
    		if(null == responseURI){
    			Log.d(LOG_TAG, "failed trying to add the user on ME table");
    			return 0;
    		}
       	}else{
       		// user is already there, if it is pending, Ill change it to not pending
    		int index_id  = cursor.getColumnIndex(SocialContract.Me._ID);
    		int index_global_id  = cursor.getColumnIndex(SocialContract.Me.GLOBAL_ID);
    		String	global_id = cursor.getString(index_global_id);
    		if(global_id.equalsIgnoreCase("pending") == true){
    			// we will update as it is set to pending
    			int	ownerID = cursor.getInt(index_id);
    			ContentValues mUpdateValues = new ContentValues();
    			mUpdateValues.put(SocialContract.Me.GLOBAL_ID, userName);
    			
    			String mSelectUpdateClause = SocialContract.Me._ID + " = ?";
    	    	String[] mSelectUpdateArgs = {ownerID + ""};
    	    	
    	    	int mRowsUpdated = getContentResolver().update(
    	    			SocialContract.Me.CONTENT_URI,mUpdateValues,mSelectUpdateClause,mSelectUpdateArgs);
    	    	
    	    	if(1 != mRowsUpdated){
        			Log.d(LOG_TAG, "something went wrong when updating the user on ME table");
        			return 0;
    	    	}
    	    	
    		}
    		
    	    
       		
       	}
    	
	
    	
		
		return 1;
	}
    
	
	

	


}

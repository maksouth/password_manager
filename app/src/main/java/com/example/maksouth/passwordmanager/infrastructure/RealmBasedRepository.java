package com.example.maksouth.passwordmanager.infrastructure;

import android.content.Context;
import android.widget.Toast;

import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.DummyItemCredentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;
import com.example.maksouth.passwordmanager.entities.RealmItemCredentials;
import com.example.maksouth.passwordmanager.entities.RealmMasterCredentials;
import com.example.maksouth.passwordmanager.interfaces.Repository;

import java.io.InvalidClassException;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.Required;

/**
 * Created by maksouth on 23.02.17.
 */

public abstract class RealmBasedRepository implements Repository {

    private Realm realm;
    private final String ID_FIELD = "id";
    private final String LOGIN_FIELD = "login";
    private final String NAME_FIELD = "name";
    private final String PASSWORD_FIELD = "password";
    private final String ADDRESS_FIELD = "address";
    private final int INITIAL_CREDENTIALS_ID = 1;
    private Context context;

    @Override
    public boolean connect(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        this.context = context;
        Toast.makeText(context, new StringBuilder().append("Realm created: ").append(realm!=null).toString(), Toast.LENGTH_LONG).show();
        return realm!=null;
    }

    @Override
    public void disconnect() {
        realm = null;
    }

    @Override
    public void storeMasterPassword(Credentials masterPassword) throws InvalidClassException {
        if(!(masterPassword instanceof RealmMasterCredentials))
            throw new InvalidClassException("Credentials must inherit RealmObject");

        RealmMasterCredentials credentials = (RealmMasterCredentials) masterPassword;

        realm.where(RealmMasterCredentials.class).findAll().deleteAllFromRealm();

        realm.beginTransaction();

        realm.copyToRealmOrUpdate(credentials);

        realm.commitTransaction();

        Toast.makeText(context, ("Master pswd saved: "), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean storeItem(ItemCredentials item) {
        if(!(item instanceof RealmItemCredentials)) return false;

        RealmItemCredentials credentials = (RealmItemCredentials) item;
        credentials.setId(generateId());

        realm.beginTransaction();

        realm.copyToRealmOrUpdate(credentials);

        realm.commitTransaction();

        return true;
    }

    @Override
    public RealmMasterCredentials getMasterPassword() {
        RealmMasterCredentials credentials = realm.where(RealmMasterCredentials.class).findFirst();
        Toast.makeText(context, new StringBuilder("Master pswd obtain: ").append(credentials==null).toString(), Toast.LENGTH_LONG).show();
        return credentials;
        //return new RealmMasterCredentials();
    }

    @Override
    public List<ItemCredentials> getAllCredentials() {
        RealmResults<RealmItemCredentials> realmList = realm.where(RealmItemCredentials.class).findAll();
        Toast.makeText(context, new StringBuilder("All items obtain: ").append(realmList==null).toString(), Toast.LENGTH_LONG).show();
        return convertRealmResultsToJavaList(realmList);
    }

    @Override
    public ItemCredentials getCredentials(long id) {
        return realm.where(RealmItemCredentials.class).equalTo(ID_FIELD, id).findFirst();
    }

    @Override
    public int clearAll() {
        RealmResults realmResults = realm.where(RealmItemCredentials.class).findAll();
        int size = realmResults.size();

        if(realmResults.deleteAllFromRealm()) {
            return size;
        }else return 0;
    }

    private long generateId(){
        RealmResults<RealmItemCredentials> results = realm.where(RealmItemCredentials.class)     //get all saved item credentials
                                                     .findAllSorted(ID_FIELD, Sort.DESCENDING);  //sort in desc order by id
        if(results.isEmpty()) return INITIAL_CREDENTIALS_ID;
        return results.first().getId()+1;//get first with biggest id increase by 1 to get unique
    }

    /**
     * convert RealmResults with ItemCredentials
     * @param realmList - realm specific list with realm objects
     * @return java list - standart java collection list
     */
    private List<ItemCredentials> convertRealmResultsToJavaList(RealmResults<RealmItemCredentials> realmList){
        List<ItemCredentials> javaList = new LinkedList();

        for(RealmItemCredentials realmObject : realmList){
            javaList.add(realmObject);
        }

        return javaList;
    }
}

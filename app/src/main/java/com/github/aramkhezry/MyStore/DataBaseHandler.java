package com.github.aramkhezry.MyStore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.github.aramkhezry.MyStore.Dao.DaoMaster;
import com.github.aramkhezry.MyStore.Dao.DaoSession;
import com.github.aramkhezry.MyStore.Dao.Memorey;
import com.github.aramkhezry.MyStore.Dao.MemoreyDao;

import java.util.List;


/**
 * Created by Raman on 11/6/2016.
 */
public class DataBaseHandler {


    DaoSession daoSession;
    Context context;

    public DataBaseHandler(Context context) {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, baseDir +"/myMemory/devsblog.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
    }

    public void addMemory(Memorey memorey) {

        MemoreyDao memoreyDao=daoSession.getMemoreyDao();
        memoreyDao.insert(memorey);
        Log.d("aram",memoreyDao.count()+"");

    }




    public List<Memorey> getAllMemory() {
        MemoreyDao memoreyDao=daoSession.getMemoreyDao();
        Log.d("count_",memoreyDao.count()+"");
        return memoreyDao.queryBuilder()
                .orderDesc(MemoreyDao.Properties.Id)
                .list();
    }

    public void removeMemory(Long id) {

        MemoreyDao memoreyDao=daoSession.getMemoreyDao();
        memoreyDao.deleteByKey(id);
    }

    public void EditMemory(Memorey memorey) {
        MemoreyDao memoreyDao=daoSession.getMemoreyDao();
        memoreyDao.update(memorey);

    }

    public void addFavorite(Long id, boolean favorite) {

        MemoreyDao memoreyDao=daoSession.getMemoreyDao();
        Memorey memorey = memoreyDao.load(id);
        memorey.setFavorite(favorite);
        memoreyDao.update(memorey);

    }


    public List<Memorey> getAllMemoryFavorite() {
        MemoreyDao memoreyDao=daoSession.getMemoreyDao();
        Log.d("count",memoreyDao.count()+"");
        List <Memorey> memoreyList= memoreyDao.queryBuilder()
                .where(MemoreyDao.Properties.Favorite.eq(true))
                .orderDesc(MemoreyDao.Properties.Id)
                .list();
        return memoreyList;
    }

}

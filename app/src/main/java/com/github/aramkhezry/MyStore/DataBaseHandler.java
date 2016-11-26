package com.github.aramkhezry.MyStore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.github.aramkhezry.MyStore.Dao.DaoMaster;
import com.github.aramkhezry.MyStore.Dao.DaoSession;
import com.github.aramkhezry.MyStore.Dao.Memory;
import com.github.aramkhezry.MyStore.Dao.MemoryDao;

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

    public void addMemory(Memory memory) {

        MemoryDao memoryDao=daoSession.getMemoryDao();
        memoryDao.insert(memory);
        Log.d("aram",memoryDao.count()+"");

    }




    public List<Memory> getAllMemory() {
        MemoryDao memoryDao=daoSession.getMemoryDao();
        Log.d("count_",memoryDao.count()+"");
        return memoryDao.queryBuilder()
                .orderDesc(MemoryDao.Properties.Id)
                .list();
    }

    public void removeMemory(Long id) {

        MemoryDao memoreyDao=daoSession.getMemoryDao();
        memoreyDao.deleteByKey(id);
    }

    public void EditMemory(Memory memory) {
        MemoryDao memoreyDao=daoSession.getMemoryDao();
        memoreyDao.update(memory);

    }

    public void addFavorite(Long id, boolean favorite) {

        MemoryDao memoreyDao=daoSession.getMemoryDao();
        Memory memorey = memoreyDao.load(id);
        memorey.setFavorite(favorite);
        memoreyDao.update(memorey);

    }


    public List<Memory> getAllMemoryFavorite() {
        MemoryDao memoryDao=daoSession.getMemoryDao();
        Log.d("count",memoryDao.count()+"");
        List <Memory> memoryList= memoryDao.queryBuilder()
                .where(MemoryDao.Properties.Favorite.eq(true))
                .orderDesc(MemoryDao.Properties.Id)
                .list();
        return memoryList;
    }

}

/*
package com.max.memo3.Util;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class util_RealmSchema implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        //version 1 : 1/7/2019 10:51
        //class = Database_class_for_realm_1&2 , Custom_Scheduler_Record_AlarmManager
        if (oldVersion == 0){
            schema.create("Database_class_for_realm_1")
                    .addField("name1",String.class)
                    .addField("num1",int.class);
            schema.create("Database_class_for_realm_2")
                    .addField("name2",String.class)
                    .addField("num2",int.class)
                    .addRealmListField("list2",schema.get("Database_class_for_realm_1"));
            schema.create("Custom_Scheduler_Record_AlarmManager")
                    .addField("action",String.class)
                    .addField("requestCode",int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("year",int.class)
                    .addField("month",int.class)
                    .addField("date",int.class)
                    .addField("hour",int.class)
                    .addField("minute",int.class)
                    .addField("second",int.class);
            schema.create("Custom_Scheduler_Record_JobService")
                    .addField("action",String.class)
                    .addField("jobID",int.class,FieldAttribute.PRIMARY_KEY)
                    .addField("max_delay_ms",long.class)
                    .addField("year",int.class)
                    .addField("month",int.class)
                    .addField("date",int.class)
                    .addField("hour",int.class)
                    .addField("minute",int.class)
                    .addField("second",int.class);
            oldVersion++;
        }

        //version 2 : 4/7/2019 10:05
        //new class = Realm_Recycler_Test_Data
        if (oldVersion == 1) {
            schema.create("Realm_Recycler_Test_Data")
                    .addField("id",int.class,FieldAttribute.PRIMARY_KEY)
                    .addField("name_a",String.class)
                    .addField("name_b",String.class);
            oldVersion++;
        }

        //version 3 : 12/7/2019 15:07
        //new class = MemoRecord (dont 5/2/2020)
        if (oldVersion == 2){
//            schema.create("MemoRecord");
            oldVersion++;
        }

        //version 4 : 4/2/2020 22:37
        //nothing new, just mark a new number
        if (oldVersion == 3){
            oldVersion++;
        }
    }
}
*/
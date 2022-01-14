package com.app.mirrorsensei.UtilMax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.app.mirrorsensei.R;
import com.app.mirrorsensei.UtilMax.utilmax;
import com.google.gson.Gson;

//util for Mirror Sensei related things
public final class utilmaxMS {
    /** LIFECYCLE */
    private static boolean isInited = false;

    public static void INIT_MS(){
        if (isInited || !utilmax.getIsInited()) {
            return;
        } else {
            init_File();
            init_Account();

            isInited = true;
        }
    }

    /** FILE */
    public enum APP_FILE_LIST {
        userList{
            public Class<?> getRelatedStructureClass() {return UserListFS.class;}
        };
        public File getAppFile(){return utilmax.makeFile(this.name());}
        public abstract Class<?> getRelatedStructureClass();
        public <T> void save(T theClassObject){saveAppFile(this,theClassObject);}
        public <T> T load(Class<T> structureClass){return loadAppFile(this,structureClass);}
    }
    //FS = File Structure
    //CS = Cache Structure
    public static class UserListFS{
        public HashMap<String,String> userList;
        public UserListFS(){}
        public enum FILE_STRUCTURE_USERLIST {userName, userPassword}
    }
    public static class UserFS{
        public String userName, userEmail, userGender, learningTarget, currLevel, subjectCategory, progress;
        public int userAge, totalLearningDays, initialLevel;
        public UserFS(){}
        public enum FILE_STRUCTURE_USER {userName, userEmail, userAge, userGender, learningTarget, currLevel,
                                        totalLearningDays, subjectCategory, initialLevel, progress}
    }
    public static class MotionCS{
        public String userName;
        public float frameTime, differ;
        public boolean isCorrect;
        public int motionSkeletonGT;
        public MotionCS(){}
        public enum CACHE_STRUCTURE_MOTION {userName, frameTime, isCorrect, differ, motionSkeletonGT}
    }
    private static void init_File(){
        Arrays.stream(APP_FILE_LIST.values()).forEach(app_file_list -> {
            String filename = app_file_list.name();
//            log(filename);
            utilmax.createFile(filename);
        });
    }
    public static <T> void saveAppFile(APP_FILE_LIST file, T classObject){
        if (classObject.getClass() != file.getRelatedStructureClass()){return;}
        utilmax.saveFileJSONClass(file.getAppFile(),classObject);
    }
    public static <T> T loadAppFile(APP_FILE_LIST file, Class<T> structureClass){
        if (file.getRelatedStructureClass() != structureClass){return null;}
        return (T) utilmax.loadFileJSONClass(file.getAppFile(),file.getRelatedStructureClass());
    }

    /** ACCOUNT */
    public static String currUser;
    public static void init_Account(){
        currUser = utilmax.PREF.getString(utilmax.APP_CONTEXT.getResources().getString(R.string.currUser),null);
    }
    public static String accountSignIn(String name, String pwd){
        if (currUser != null){return null;}
        HashMap<String,String> userList = APP_FILE_LIST.userList.load(UserListFS.class).userList;
        if (!userList.containsKey(name)){return null;}
        if (!userList.get(name).equals(pwd)){return null;}
        utilmax.PREF.edit().putString(utilmax.APP_CONTEXT.getResources().getString(R.string.currUser),name).apply();
        return name;
    }
    public static void accountSignOut(){currUser = null;}
    public static boolean accountSignUp(String name, String pwd, String email, int age, String gender){
        UserListFS userList = APP_FILE_LIST.userList.load(UserListFS.class);
        if (userList.userList.containsKey(name)){return false;}
        //save in userList
        userList.userList.put(name,pwd);
        APP_FILE_LIST.userList.save(userList);
        //make new UserFS
        UserFS newUser = new UserFS();
        newUser.userName = name;
        newUser.userEmail = email;
        newUser.userAge = age;
        newUser.userGender = gender;
        newUser.learningTarget = "Not set";
        newUser.currLevel = "Beginner";
        newUser.totalLearningDays = 0;
        newUser.subjectCategory = "No Category";
        newUser.initialLevel = 1;
        newUser.progress = "No Progress";
        utilmax.createFile(name);
        utilmax.saveFileJSONClass(utilmax.makeFile(name),newUser);
        return true;
    }
    public static void saveUserFile(UserFS user){
        utilmax.saveFileJSONClass(utilmax.makeFile(user.userName),user);
    }
    public static UserFS loadUserFile(String userName){
        if (userName == null){return null;}
        if (!utilmax.makeFile(userName).isFile()){return null;}
        return utilmax.loadFileJSONClass(utilmax.makeFile(userName),UserFS.class);
    }
    public static UserFS loadUserFile(){
        if (currUser == null){return null;}
        return loadUserFile(currUser);
    }

    /** CAMERA */

}

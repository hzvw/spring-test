package com.example.lock;

/**
 * ClassName: LazySingleton
 * Package: com.example.lock
 * Description:
 *
 * @Author Harizon
 * @Create 2025/3/3 1:01
 * @Version 1.0
 */
public class LazySingleton {
    static  volatile LazySingleton install;

    private LazySingleton(){};

    public LazySingleton getInstall(){
        synchronized (LazySingleton.class){
            if(install == null){
                install = new LazySingleton();
            }
        }
        return install;
    }

}

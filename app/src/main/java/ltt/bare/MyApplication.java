package ltt.bare;

import android.app.Application;
import com.github.promeg.xlog_android.lib.XLogConfig;

/**
 * Config some application level stuffs.
 * Created by letientai299 on 3/5/16.
 */
public class MyApplication extends Application {
  public void onCreate() {
    super.onCreate();

    XLogConfig.config(XLogConfig.newConfigBuilder(this).build());
  }
}

import java.util.Properties;

import org.apache.log4j.Logger;

import util.DatabaseUtil;
import util.DateConvertion;
import util.LogUtil;
import util.PropertiesUtil;

public class backupSource2000 {
    private static Properties dpProp = PropertiesUtil.getInstance().getDbProp();
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger logger = LogUtil.getLogger(DatabaseUtil.class.getName());
    public static void main(String[] args ){
        String date= "20240614";
        String nextDate = DateConvertion.SetDate(date, true);
        String prevDate = DateConvertion.SetDate(date, false);
        
        DatabaseUtil.Backup2000(progProp.getProperty("select_source_2000_backup").replace("$date", date).replace("$prev_date",prevDate).replace("$next_date",nextDate), 1, date);
    }
}

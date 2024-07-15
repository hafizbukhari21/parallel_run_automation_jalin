import java.util.Properties;

import org.apache.log4j.Logger;

import util.DatabaseUtil;
import util.DateConvertion;
import util.LogUtil;
import util.PropertiesUtil;

public class PreRun {
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger logger = LogUtil.getLogger(DatabaseUtil.class.getName());
    public static void main(String[] args ){
        logger.info("Apps By: M. Hafiz Bukhari");
        logger.info("Jalin - ITQA");
        String date= "20240624";
        String nextDate = DateConvertion.SetDate(date, true);
        String prevDate = DateConvertion.SetDate(date, false);
        
        //Backup Source 2000
        DatabaseUtil.Backup2000(progProp.getProperty("select_source_2000_backup").replace("$date", date).replace("$prev_date",prevDate).replace("$next_date",nextDate), 2, date);


        //Delete Counter Invoice
        


    }
}
